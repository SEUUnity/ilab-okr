/*
 * Copyright (c) 2018 Industics Co., Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.industics.ilab.okr.dal.manager;

import com.industics.ilab.okr.apiobjects.etype.ErrorTypes;
import com.industics.ilab.okr.apiobjects.user.GroupVO;
import com.industics.ilab.okr.apiobjects.user.UserTreeVO;
import com.industics.ilab.okr.apiobjects.user.UserVO;
import com.industics.ilab.okr.dal.dao.mapper.UserMapper;
import com.industics.ilab.okr.dal.dao.repository.GroupRepository;
import com.industics.ilab.okr.dal.dao.repository.UserGroupRepository;
import com.industics.ilab.okr.dal.dao.repository.UserRepository;
import com.industics.ilab.okr.dal.entity.Group;
import com.industics.ilab.okr.dal.entity.User;
import com.industics.ilab.okr.dal.entity.UserGroup;
import com.industics.isword.common.exception.ApiErrorException;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class UserManager extends AbstractManager {

    private UserRepository userRepository;
    private UserGroupRepository userGroupRepository;
    private GroupRepository groupRepository;
    private UserMapper userMapper;
    @Autowired
    JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    String from;

    @Transactional(rollbackFor = Exception.class, readOnly = true)
    public UserVO getUserVO(String userId) {
        UserVO userVO = userRepository.findById(userId)
                .map(user -> mapper.map(user, UserVO.class))
                .orElseThrow(() -> new ApiErrorException(ErrorTypes.USER_NOT_FOUND, userId));
        List<UserGroup> userGroups = userGroupRepository.findAllByUserId(userId);
        List<String> groupIds = userGroups.stream().map(UserGroup::getGroupId).distinct().collect(Collectors.toList());
        List<GroupVO> groupVOS = mapper.mapAsList(groupRepository.findAllByIdIn(groupIds), GroupVO.class);
        Map<String, Boolean> userGroupMap = userGroups.stream().collect(Collectors.toMap(UserGroup::getGroupId, UserGroup::isManager));
        groupVOS.forEach(groupVO -> groupVO.setManager(userGroupMap.get(groupVO.getId())));
        groupVOS.sort(Comparator.comparing(GroupVO::getSequence));
        userVO.setGroups(groupVOS);
        return userVO;
    }

    @Transactional(rollbackFor = Exception.class, readOnly = true)
    public UserTreeVO getUserTree() {
        List<Group> okrGroups = groupRepository.findAllByTypeAndDeletedFalse("OKR");
        Optional<Group> rootGroupOptional = okrGroups.stream().filter(Group::isRoot).findFirst();
        if (!rootGroupOptional.isPresent()) {
            throw new ApiErrorException(ErrorTypes.GROUP_NOT_FOUND, "OKR ROOT");
        }

        Group rootGroup = rootGroupOptional.get();
        List<UserTreeVO> managerTree = new ArrayList<>();
        UserTreeVO rootUserTree = groupToUserTree(rootGroup);
        managerTree.add(rootUserTree);

        Map<String, List<Group>> parentGroupMap = okrGroups.stream().collect(Collectors.groupingBy(Group::getParentId));
        buildUserTree(rootGroup, rootUserTree, parentGroupMap, null);

        UserTreeVO root = new UserTreeVO();
        root.setChildren(managerTree);
        root.setFullname("组织结构");
        return root;
    }

    private void buildUserTree(Group group, UserTreeVO userTreeVO, Map<String, List<Group>> parentGroupMap, String parentOkrManager) {
        List<UserTreeVO> treeList = getUserTreeList(group.getId(), parentOkrManager);
        List<Group> subGroups = parentGroupMap.get(group.getId());
        if (CollectionUtils.isNotEmpty(subGroups)) {
            List<UserTreeVO> subUserTrees = new ArrayList<>();
            String okrManager = treeList.stream().filter(UserTreeVO::isManager).map(UserTreeVO::getId).findFirst().orElse(null);
            subGroups.forEach(subGroup -> {
                UserTreeVO subUserTree = groupToUserTree(subGroup);
                subUserTrees.add(subUserTree);
                buildUserTree(subGroup, subUserTree, parentGroupMap, okrManager);
            });
            treeList.addAll(subUserTrees.stream().sorted(Comparator.comparing(UserTreeVO::getSequence)).collect(Collectors.toList()));
        }
        userTreeVO.setChildren(treeList);
    }

    private List<UserTreeVO> getUserTreeList(String groupId, String parentOkrManager) {
        List<UserGroup> userGroups = userGroupRepository.findAllByGroupIdAndDeletedFalse(groupId);
        Optional<String> okrManagerOptional = userGroups.stream().filter(UserGroup::isManager).map(UserGroup::getUserId).findFirst();
        List<String> userIds = userGroups.stream().map(UserGroup::getUserId).distinct().collect(Collectors.toList());
        Map<String, User> userMap = userRepository.findAllByIdIn(userIds).stream().collect(Collectors.toMap(User::getId, Function.identity()));
        return userGroups.stream().filter(userGroup -> userMap.containsKey(userGroup.getUserId())).map(userGroup -> {
            User user = userMap.get(userGroup.getUserId());
            UserTreeVO userTreeVO = mapper.map(user, UserTreeVO.class);
            userTreeVO.setDept(false);
            userTreeVO.setManager(userGroup.isManager());
            if (userTreeVO.isManager()) {
                userTreeVO.setOkrManager(parentOkrManager);
            } else {
                okrManagerOptional.ifPresent(userTreeVO::setOkrManager);
            }
            return userTreeVO;
        }).sorted(Comparator.comparing(UserTreeVO::isManager).reversed().thenComparing(UserTreeVO::getUsername)).collect(Collectors.toList());
    }

    private UserTreeVO groupToUserTree(Group group) {
        UserTreeVO userTreeVO = new UserTreeVO();
        userTreeVO.setId(group.getId());
        userTreeVO.setFullname(group.getName());
        userTreeVO.setSequence(group.getSequence());
        userTreeVO.setDept(true);
        return userTreeVO;
    }

    public int adminLogin(String username,String password){
        return userMapper.adminLogin(username,password);
    }

    private void fillUserTreeVO(UserTreeVO treeVO, Map<String, List<User>> userMap, List<String> alreadyAdd) {
        if (alreadyAdd == null) {
            alreadyAdd = new ArrayList<>();
        }
        if (alreadyAdd.contains(treeVO.getId())) {
            return;
        }
        if (userMap.get(treeVO.getId()) == null) {
            return;
        }

        treeVO.setChildren(mapper.mapAsList(userMap.get(treeVO.getId()), UserTreeVO.class));
        UserTreeVO self = mapper.map(treeVO, UserTreeVO.class);
        self.setChildren(null);
        treeVO.getChildren().add(0, self);
        treeVO.getChildren().forEach(userTreeVO -> userTreeVO.setDept(false));

        Group userOrg = userMapper.getUserOkrOrgName(treeVO.getId());
        if (userOrg == null) {
            treeVO.setFullname(treeVO.getFullname() + "-部门");
        } else {
            treeVO.setFullname(userOrg.getName());
            treeVO.setSequence(userOrg.getSequence());
        }

        treeVO.setDept(true);
        alreadyAdd.add(treeVO.getId());
        for (UserTreeVO child : treeVO.getChildren()) {
            fillUserTreeVO(child, userMap, alreadyAdd);
        }
    }

    @Transactional(readOnly = true, rollbackFor = Exception.class)
    public List<UserVO> getUsers(String fullname, String mobile) {
        List<UserVO> userVOS = userMapper.queryUsers(fullname, mobile);
        if (!CollectionUtils.isEmpty(userVOS)) {
            List<UserGroup> allUserGroups = userGroupRepository.findAllByUserIdIn(userVOS.stream().map(UserVO::getId).collect(Collectors.toList()));
            List<String> allGroupIds = allUserGroups.stream().map(UserGroup::getGroupId).distinct().collect(Collectors.toList());
            List<GroupVO> allGroupVOS = mapper.mapAsList(groupRepository.findAllByIdIn(allGroupIds), GroupVO.class);
            userVOS.forEach(userVO -> {
                List<UserGroup> userGroups = allUserGroups.stream().filter(userGroup -> userGroup.getUserId().equals(userVO.getId())).collect(Collectors.toList());
                List<String> groupIds = userGroups.stream().map(UserGroup::getGroupId).distinct().collect(Collectors.toList());
                List<GroupVO> groupVOS = allGroupVOS.stream().filter(groupVO -> groupIds.contains(groupVO.getId())).collect(Collectors.toList());
                groupVOS.sort(Comparator.comparing(GroupVO::getSequence));
                userVO.setGroups(groupVOS);
            });
        }
        return userVOS;
    }


    public void addAdmin(String name, String username,String password,String permission){
        userMapper.addAdmin(name,username,password,permission);
    }

    public void deleteAdmin(String admin_id){
        userMapper.deleteAdmin(admin_id);
    }

    public void updateAdmin(String admin_id,String name, String username,String permission){
        userMapper.updateAdmin(admin_id,name,username,permission);
    }

    public void updateLastLogin(String admin_id){
        userMapper.updateLastLogin(admin_id);
    }

    public void updateAdminPassword(String admin_id,String password){
        userMapper.updateAdminPassword(admin_id,password);
    }

    public Map<String, Object> getAdminByUsername(String username){
        return userMapper.getAdminByUsername(username);
    }
    public Map<String, Object> getUserByEmail(String email){
        return userMapper.getUserByEmail(email);
    }
    public Map<String, Object> getUserByID(String user_id){
        return userMapper.getUserByID(user_id);
    }
    public Map<String, Object> getRegisterByEmail(String email){
        return userMapper.getRegisterByEmail(email);
    }
    public Map<String, Object> getAdminByID(String admin_id){
        return userMapper.getAdminByID(admin_id);
    }
    public List<Map<String, Object>> getAdmins(){
        return userMapper.getAdmins();
    }

    public List<Map<String,Object>> getUsers(List<String>status,int page_num,int data_num){
        return userMapper.getUsers(status,(page_num-1)*data_num,data_num);
    }

    public int getUsersCount(List<String>status){
        return userMapper.getUsersCount(status);
    }

    public void updateUserStatus(List<String>ids,String status)
    {
        userMapper.updateUserStatus(ids,status);
    }
    public void addUserBL(String user_id,String work_num,String name,String avatar,String email,String phone,String we_chat){
        userMapper.addUserBL(user_id,work_num,name,avatar,email,phone,we_chat);
    }

    public void updateUser(String user_id,String name,String work_num,String we_chat){
        userMapper.updateUser(user_id,name,work_num,we_chat);
    }

    public void updateWorkNum(String user_id,String work_num){
        userMapper.updateWorkNum(user_id,work_num);
    }

    public Map<String,Object> getUserByWorkNum(String work_num){
        return userMapper.getUserByWorkNum(work_num);
    }


    public String randomCode() {
        StringBuilder str = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < 6; i++) {
            str.append(random.nextInt(10));
        }
        return str.toString();
    }

    public boolean validTime(Map<String,Object> userRegister){
        int dateDifference=Integer.parseInt(userRegister.get("dateDifference").toString());
        int valid=5*60;
        if(dateDifference<=valid){
            return true;
        }else {
            return false;
        }

    }
    public int sendMail(String email) {
        try {
            Map<String,Object> userRegister=userMapper.getRegisterByEmail(email);
            SimpleMailMessage mailMessage = new SimpleMailMessage();
            mailMessage.setSubject("【HR内推系统】验证码邮件");//主题
            //生成随机数
            String Code = randomCode();
            //更新验证码
            if(userRegister==null)
                userMapper.addRegister(email,Code);
            else{
                if(validTime(userRegister)){
                    return 2;
                }else {
                    userMapper.updateRegister(email,Code);
                }
            }

            mailMessage.setText("亲爱的用户：\n" + "     您好！您正在使用邮箱验证，本次请求的验证码为：" + Code + "，本验证码5分钟内有效，请在5分钟内完成验证。" +
                    "（请勿泄露此验证码）如非本人操作，请忽略该邮件。（这是一封自动发送的邮件，请不要直接回复)\n" + "                                                            " +
                    "                                                     HR内推系统");//内容
            mailMessage.setTo(email);//发给谁

            mailMessage.setFrom(from);//你自己的邮箱
            mailSender.send(mailMessage);//发送
            return 1;
        }catch (Exception e){
            e.printStackTrace();
            return 0;
        }
    }

    @Autowired
    public void setUserMapper(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    @Autowired
    public void setUserGroupRepository(UserGroupRepository userGroupRepository) {
        this.userGroupRepository = userGroupRepository;
    }

    @Autowired
    public void setGroupRepository(GroupRepository groupRepository) {
        this.groupRepository = groupRepository;
    }

    @Autowired
    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

}
