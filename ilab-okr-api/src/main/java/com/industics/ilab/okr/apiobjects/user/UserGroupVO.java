package com.industics.ilab.okr.apiobjects.user;

import com.industics.isword.common.entity.EntityObject;


public class UserGroupVO extends EntityObject {
    private static final long serialVersionUID = 3822065139215249514L;

    private String id;
    private String userId;
    private String groupId;
    private String groupName;
    private boolean manager;

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }


    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public boolean isManager() {
        return manager;
    }

    public void setManager(boolean manager) {
        this.manager = manager;
    }
}
