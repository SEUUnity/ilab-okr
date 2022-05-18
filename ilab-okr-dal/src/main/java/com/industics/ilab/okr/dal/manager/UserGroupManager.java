package com.industics.ilab.okr.dal.manager;

import com.industics.ilab.okr.apiobjects.user.UserGroupVO;
import com.industics.ilab.okr.dal.dao.repository.GroupRepository;
import com.industics.ilab.okr.dal.dao.repository.UserGroupRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class UserGroupManager extends AbstractManager {
    private UserGroupRepository userGroupRepository;
    private GroupRepository groupRepository;

    public List<UserGroupVO> getUserGroups(String userId) {
        List<UserGroupVO> userGroupVOS = mapper.mapAsList(userGroupRepository.findAllByUserId(userId), UserGroupVO.class);
        for (UserGroupVO userGroupVO : userGroupVOS) {
            if (groupRepository.findById(userGroupVO.getGroupId()).isPresent()) {
                userGroupVO.setGroupName(groupRepository.findById(userGroupVO.getGroupId()).get().getName());
            }
        }
        return userGroupVOS;
    }

    public boolean existsByUserAndGroups(String userId, String groupId) {
        return userGroupRepository.existsByUserIdAndGroupId(userId, groupId);
    }

    @Autowired
    public void setUserGroupRepository(UserGroupRepository userGroupRepository) {
        this.userGroupRepository = userGroupRepository;
    }

    @Autowired
    public void setGroupRepository(GroupRepository groupRepository) {
        this.groupRepository = groupRepository;
    }
}
