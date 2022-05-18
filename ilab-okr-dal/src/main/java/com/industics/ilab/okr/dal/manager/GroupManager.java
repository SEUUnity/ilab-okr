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

package com.industics.ilab.okr.dal.manager;


import com.industics.ilab.okr.apiobjects.user.GroupVO;
import com.industics.ilab.okr.dal.dao.repository.GroupRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Description of class
 *
 * @author tong
 * @Date 2018/11/7 10:51
 * @description
 */

@Component
public class GroupManager extends AbstractManager {
    private GroupRepository groupRepository;

    public boolean isExistByName(String name) {
        return groupRepository.existsByName(name);
    }

    public boolean isExistById(String id) {
        return groupRepository.existsById(id);
    }

    public List<GroupVO> getGroupList(String type) {
        if (null != type && !"".equals(type)){
            return mapper.mapAsList(groupRepository.findAllByType(type), GroupVO.class).stream().sorted(Comparator.comparing(GroupVO::getSequence)).collect(Collectors.toList());
        }
        return mapper.mapAsList(groupRepository.findAll(), GroupVO.class).stream().sorted(Comparator.comparing(GroupVO::getSequence)).collect(Collectors.toList());
    }

    @Autowired
    public void setGroupRepository(GroupRepository groupRepository) {
        this.groupRepository = groupRepository;
    }
}
