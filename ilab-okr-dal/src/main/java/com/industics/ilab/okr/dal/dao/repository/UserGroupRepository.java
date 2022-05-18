package com.industics.ilab.okr.dal.dao.repository;

import com.industics.ilab.okr.dal.entity.UserGroup;

import java.util.List;

public interface UserGroupRepository extends BaseRepository<UserGroup, String> {
    List<UserGroup> findAllByUserId(String userId);

    List<UserGroup> findAllByUserIdIn(List<String> userIds);

    List<UserGroup> findAllByGroupId(String groupId);

    List<UserGroup> findAllByGroupIdAndDeletedFalse(String groupId);

    List<UserGroup> findAllByGroupIdIsIn(List<String> groupIds);

    boolean existsByUserIdAndGroupId(String userId, String groupId);

    void removeByUserIdAndGroupId(String userId, String groupId);

    void removeAllByGroupId(String groupId);
}
