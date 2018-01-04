package com.alan.mapper;

import com.alan.model.User;
import org.apache.ibatis.annotations.Mapper;

/**
 * @Author: 孟庆浩
 * @Description:
 * @Date: Created in 18-1-4 下午3:06
 * @Modified By:
 */
@Mapper
public interface UserMapper {

    int insert(User user);

    User findUserById(Integer id);
}
