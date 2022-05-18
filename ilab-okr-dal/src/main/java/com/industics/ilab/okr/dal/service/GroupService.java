/*
 * Copyright (c) 2018. Industics Co., Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.industics.ilab.okr.dal.service;


import com.industics.ilab.okr.apiobjects.etype.ErrorTypes;
import com.industics.ilab.okr.apiobjects.user.GroupRequestVO;
import com.industics.ilab.okr.apiobjects.user.GroupVO;
import com.industics.ilab.okr.apiobjects.user.UserVO;
import com.industics.ilab.okr.dal.dao.repository.GroupRepository;
import com.industics.ilab.okr.dal.dao.repository.UserGroupRepository;
import com.industics.ilab.okr.dal.dao.repository.UserRepository;
import com.industics.ilab.okr.dal.entity.Group;
import com.industics.ilab.okr.dal.entity.UserGroup;
import com.industics.ilab.okr.dal.manager.GroupManager;
import com.industics.isword.common.exception.ApiErrorException;
import com.industics.isword.common.utils.UniqueString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Description of class
 *
 * @author tong
 * @Date 2018/11/7 10:48
 * @description
 */

@Service
public class GroupService extends AbstractService {
    private GroupRepository groupRepository;
    private GroupManager groupManager;
    private UserGroupRepository userGroupRepository;
    private UserRepository userRepository;

    @Transactional(rollbackFor = Exception.class)
    public void createGroup(GroupRequestVO groupRequestVO) {
        if (groupManager.isExistByName(groupRequestVO.getName())) {
            throw new ApiErrorException(ErrorTypes.GROUP_ALREADY_EXISTS, groupRequestVO.getName());
        }
        Group group = mapper.map(groupRequestVO, Group.class);
        group.setId(UniqueString.uuidUniqueString());
        groupRepository.save(group);
    }

    @Transactional(rollbackFor = Exception.class)
    public void deleteGroup(String groupId) {
        if (groupManager.isExistById(groupId)) {
            throw new ApiErrorException(ErrorTypes.GROUP_NOT_FOUND, groupId);
        }
        userGroupRepository.removeAllByGroupId(groupId);
        groupRepository.removeById(groupId);
    }

    @Transactional(readOnly = true, rollbackFor = Exception.class)
    public List<GroupVO> getGroupList(String type) {
        return groupManager.getGroupList(type);
    }

    @Transactional(readOnly = true, rollbackFor = Exception.class)
    public List<UserVO> getUsersByGroup(String groupId) {
        List<String> userIds = userGroupRepository.findAllByGroupId(groupId).stream().map(UserGroup::getUserId).collect(Collectors.toList());
        return mapper.mapAsList(userRepository.findAllByIdIn(userIds), UserVO.class);
    }

    @Transactional(readOnly = true, rollbackFor = Exception.class)
    public List<UserVO> getUsersByGroupType(String groupType) {
        List<String> groupIds = groupRepository.findAllByType(groupType).stream().map(Group::getId).collect(Collectors.toList());
        List<String> userIds = userGroupRepository.findAllByGroupIdIsIn(groupIds).stream().map(UserGroup::getUserId).collect(Collectors.toList());
        return mapper.mapAsList(userRepository.findAllByIdIn(userIds), UserVO.class);
    }

    @Autowired
    public void setGroupRepository(GroupRepository groupRepository) {
        this.groupRepository = groupRepository;
    }

    @Autowired
    public void setGroupManager(GroupManager groupManager) {
        this.groupManager = groupManager;
    }

    @Autowired
    public void setUserGroupRepository(UserGroupRepository userGroupRepository) {
        this.userGroupRepository = userGroupRepository;
    }

    @Autowired
    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
}
