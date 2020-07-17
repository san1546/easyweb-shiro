package com.hc.hyh.system.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.hc.hyh.system.model.User;

public interface UserMapper extends BaseMapper<User> {

    User getByUsername(String username);
}
