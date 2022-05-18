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
import com.industics.ilab.okr.apiobjects.user.UserGroupRequestVO;
import com.industics.ilab.okr.apiobjects.user.UserGroupVO;
import com.industics.ilab.okr.dal.dao.repository.UserGroupRepository;
import com.industics.ilab.okr.dal.dao.repository.UserRepository;
import com.industics.ilab.okr.dal.entity.UserGroup;
import com.industics.ilab.okr.dal.manager.GroupManager;
import com.industics.ilab.okr.dal.manager.UserGroupManager;

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
 * @Date 2018/11/7 11:08
 * @description
 */

@Service
public class UserGroupService extends AbstractService {
    private UserGroupRepository userGroupRepository;
    private UserGroupManager userGroupManager;
    private GroupManager groupManager;
    private UserRepository userRepository;

    @Transactional(readOnly = true, rollbackFor = Exception.class)
    public List<String> getUserGroupsByUserId(String userId) {
        return userGroupManager.getUserGroups(userId).stream().map(UserGroupVO::getGroupName).collect(Collectors.toList());
    }

    @Transactional(rollbackFor = Exception.class)
    public void addUserToGroup(UserGroupRequestVO userGroupRequestVO) {
        if (!groupManager.isExistById(userGroupRequestVO.getGroupId())) {
            throw new ApiErrorException(ErrorTypes.GROUP_NOT_FOUND, userGroupRequestVO.getGroupId());
        }
        if (!userRepository.existsById(userGroupRequestVO.getUserId())) {
            throw new ApiErrorException(ErrorTypes.USER_NOT_FOUND, userGroupRequestVO.getUserId());
        }
        if (userGroupManager.existsByUserAndGroups(userGroupRequestVO.getUserId(), userGroupRequestVO.getGroupId())) {
            throw new ApiErrorException(ErrorTypes.USER_ALREADY_IN_GROUP);
        }
        UserGroup userGroup = mapper.map(userGroupRequestVO, UserGroup.class);
        userGroup.setId(UniqueString.uuidUniqueString());
        userGroupRepository.save(userGroup);
    }

    @Transactional(rollbackFor = Exception.class)
    public void removeUserFromGroup(String userId, String groupId) {
        if (!groupManager.isExistById(groupId)) {
            throw new ApiErrorException(ErrorTypes.GROUP_NOT_FOUND, groupId);
        }
        if (!userGroupRepository.existsByUserIdAndGroupId(userId, groupId)) {
            throw new ApiErrorException(ErrorTypes.USER_NOT_IN_GROUP, userId, groupId);
        }
        userGroupRepository.removeByUserIdAndGroupId(userId, groupId);
    }

    @Autowired
    public void setUserGroupRepository(UserGroupRepository userGroupRepository) {
        this.userGroupRepository = userGroupRepository;
    }

    @Autowired
    public void setUserGroupManager(UserGroupManager userGroupManager) {
        this.userGroupManager = userGroupManager;
    }

    @Autowired
    public void setGroupManager(GroupManager groupManager) {
        this.groupManager = groupManager;
    }

    @Autowired
    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
}
