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

package com.industics.ilab.okr.dal.dao.mapper;


import com.industics.ilab.okr.apiobjects.user.UserVO;
import com.industics.ilab.okr.dal.entity.Group;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Description of class
 *
 * @author tong
 * @Date 2018/11/13 10:33
 * @description
 */
@Repository
@Mapper
public interface UserMapper {
    List<UserVO> queryUsers(@Param("fullname") String fullname,
                            @Param("mobile") String mobile);

    Group getUserOkrOrgName(@Param("userId") String userId);
    int adminLogin(@Param("username") String username,
                   @Param("password") String password);
}
