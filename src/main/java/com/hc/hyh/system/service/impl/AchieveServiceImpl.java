package com.hc.hyh.system.service.impl;


import com.hc.hyh.common.utils.SpringContextUtil;
import com.hc.hyh.system.dao.AchieveMapper;
import com.hc.hyh.system.model.Achieve;
import com.hc.hyh.system.service.AchieveService;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class AchieveServiceImpl implements AchieveService {

    @Autowired
    private AchieveMapper achievemMapper;

    @Override
    public Achieve getTest_Score(String testno, String name) {
        return achievemMapper.getTest_Score(testno ,name);
    }

    @Override
    public Achieve getId_Score(String idno, String name) {
        return achievemMapper.getId_Score(idno ,name);
    }

}
