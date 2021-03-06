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

package com.industics.ilab.okr.dal.dao.repository;

import com.industics.ilab.okr.dal.entity.User;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends BaseRepository<User, String> {

    List<User> findAllByIdIn(List<String> ids);
    List<User> findAllByUsernameIn(List<String> usernames);
    Optional<User> findOneByUsername(String username);
    List<User> findAllByDeletedFalse();
    User findOneById(String id);

    @Override
    <S extends User> S save(S s);

    @Override
    List<User> findAll();

    @Override
    <S extends User> List<S> saveAll(Iterable<S> iterable);
}
