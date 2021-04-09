package com.hc.hyh.system.service;

import com.hc.hyh.common.PageResult;
import com.hc.hyh.common.exception.BusinessException;
import com.hc.hyh.common.exception.ParameterException;
import com.hc.hyh.system.model.TestInfo;

import java.util.List;

public interface TestService {

    TestInfo getTestNo(String testno,String part);

    List<TestInfo> getList(String testno);

    PageResult<TestInfo> list(int pageNum, int pageSize, boolean showDelete, String searchKey, String searchValue);

    TestInfo getById(Integer userId);

    boolean add(TestInfo user) throws BusinessException;

    boolean update(TestInfo user);

    boolean updateState(Integer userId, int state) throws ParameterException;

    boolean updatePsw(Integer userId, String username, String newPsw);

    boolean delete(Integer userId);

    boolean insertBatch(List<TestInfo> testInfoList);
}
