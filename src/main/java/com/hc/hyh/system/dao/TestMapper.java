package com.hc.hyh.system.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.hc.hyh.system.model.TestInfo;

import java.util.List;

public interface TestMapper extends BaseMapper<TestInfo> {

    TestInfo getByTestNo(String testno,String part);

    List<TestInfo> getList(String testno);

    int insertTestBatch(List<TestInfo> list);

}
