package com.hc.hyh.system.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.hc.hyh.system.model.Role;

import java.util.List;

public interface RoleMapper extends BaseMapper<Role> {

    List<Role> selectByUserId(Integer userId);
}
