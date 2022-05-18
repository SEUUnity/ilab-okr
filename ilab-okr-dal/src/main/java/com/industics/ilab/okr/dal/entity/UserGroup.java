package com.industics.ilab.okr.dal.entity;

import com.industics.isword.common.jpa.entity.AbstractAuditableDeletableEntity;

import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@DynamicUpdate
@Table(name = "t_ilab_user_group")
public class UserGroup extends AbstractAuditableDeletableEntity {

    private static final long serialVersionUID = 6395372818375669519L;
    @Id
    @Column(name = "id", nullable = false, length = 32)
    private String id;
    @Column(name = "user_id", nullable = false, length = 32)
    private String userId;
    @Column(name = "group_id", nullable = false, length = 32)
    private String groupId;
    @Column(name = "is_manager")
    private boolean manager;

    public UserGroup(String userId, String groupId, boolean manager) {
        this.userId = userId;
        this.groupId = groupId;
        this.manager = manager;
    }

    public UserGroup() {
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
