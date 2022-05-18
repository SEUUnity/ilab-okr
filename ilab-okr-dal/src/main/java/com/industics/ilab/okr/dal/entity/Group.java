package com.industics.ilab.okr.dal.entity;


import com.industics.isword.common.jpa.entity.AbstractAuditableDeletableEntity;

import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@DynamicUpdate
@Table(name = "t_ilab_group")
public class Group extends AbstractAuditableDeletableEntity {

    private static final long serialVersionUID = -5001924436313376599L;
    @Id
    @Column(name = "id", nullable = false, length = 32)
    @GeneratedValue(generator = "cust_uuid")
    @GenericGenerator(name = "cust_uuid", strategy = "com.industics.isword.common.jpa.generator.UuidIdentifierGenerator")
    private String id;
    @Column(name = "name", nullable = false, length = 128)
    private String name;
    @Column(name = "type", length = 32)
    private String type;
    @Column(name = "sequence")
    private Integer sequence;
    @Column(name = "parent_id")
    private String parentId;
    @Column(name = "is_root")
    private boolean root;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Integer getSequence() {
        return sequence;
    }

    public void setSequence(Integer sequence) {
        this.sequence = sequence;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public boolean isRoot() {
        return root;
    }

    public void setRoot(boolean root) {
        this.root = root;
    }
}
