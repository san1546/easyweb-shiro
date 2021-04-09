package com.hc.hyh.system.service;

import com.hc.hyh.common.PageResult;
import com.hc.hyh.common.exception.BusinessException;
import com.hc.hyh.common.exception.ParameterException;
import com.hc.hyh.system.model.TestDetailInfo;

import java.util.List;

public interface TestDetailService {

    TestDetailInfo getTestNo(int testno, String part);

    PageResult<TestDetailInfo> list(int pageNum, int pageSize, boolean showDelete, String searchKey, String searchValue);

    TestDetailInfo getById(Integer userId);

    boolean add(TestDetailInfo user) throws BusinessException;

    boolean update(TestDetailInfo user);

    boolean updateState(Integer userId, int state) throws ParameterException;

    boolean updatePsw(Integer userId, String username, String newPsw);

    boolean delete(Integer userId);

    boolean updateBatch(List<TestDetailInfo> testDetailInfoList);

    boolean insertBatch(List<TestDetailInfo> TestDetailInfoList);
}
