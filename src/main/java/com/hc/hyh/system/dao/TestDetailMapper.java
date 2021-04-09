package com.hc.hyh.system.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.hc.hyh.system.model.TestDetailInfo;

import java.util.List;

public interface TestDetailMapper extends BaseMapper<TestDetailInfo> {

    TestDetailInfo getByTestNo(int testno, String title);

    int insertTestDetailBatch(List<TestDetailInfo> list);

    int updateTestDetailBatch(List<TestDetailInfo> list);
}
