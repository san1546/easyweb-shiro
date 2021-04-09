package com.hc.hyh.system.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.hc.hyh.system.model.Achieve;


public interface AchieveMapper extends BaseMapper<Achieve> {

    Achieve getTest_Score(String testno ,String name);

    Achieve getId_Score(String idno ,String name);
}
