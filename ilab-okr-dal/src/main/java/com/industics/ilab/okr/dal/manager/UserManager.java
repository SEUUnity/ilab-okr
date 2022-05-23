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
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.management.ObjectName;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class UserManager extends AbstractManager {

    private UserRepository userRepository;
    private UserGroupRepository userGroupRepository;
    private GroupRepository groupRepository;
    private UserMapper userMapper;

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


    public void addAdmin(String name, String username,String password,int permission){
        userMapper.addAdmin(name,username,password,permission);
    }

    public void updateAdmin(String admin_id,String name, String username,String password,int permission){
        userMapper.updateAdmin(admin_id,name,username,password,permission);
    }

    public Map<String, Object> getAdminByUsername(String username){
        return userMapper.getAdminByUsername(username);
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
