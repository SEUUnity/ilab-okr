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

package com.industics.ilab.okr.dal.dao.repository;


import com.industics.ilab.okr.dal.entity.Group;

import java.util.List;
import java.util.Optional;

/**
 * Description of class
 *
 * @author tong
 * @Date 2018/11/7 10:39
 * @description
 */


public interface GroupRepository extends BaseRepository<Group, String> {
    List<Group> findAll();

    List<Group> findAllByType(String type);

    List<Group> findAllByTypeAndDeletedFalse(String type);

    List<Group> findAllByIdIn(List<String> ids);

    Optional<Group> findById(String id);

    Optional<Group> findByName(String name);

    boolean existsByName(String name);

    void removeById(String id);
}
