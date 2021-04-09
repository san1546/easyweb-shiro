package com.hc.hyh.system.service;

import com.hc.hyh.common.PageResult;
import com.hc.hyh.common.exception.BusinessException;
import com.hc.hyh.common.exception.ParameterException;
import com.hc.hyh.system.model.DataInfo;

import java.util.List;

public interface DataService {

    List<DataInfo> getName(String studentName);

    PageResult<DataInfo> list(int pageNum, int pageSize, boolean showDelete, String searchKey, String searchValue);

    DataInfo getById(Integer userId);

    boolean add(DataInfo data) throws BusinessException;

    boolean update(DataInfo data);

    boolean updateState(Integer userId, int state) throws ParameterException;

    boolean updatePsw(Integer userId, String username, String newPsw);

    boolean delete(Integer userId);

    List<DataInfo> list(String testno);

    boolean updateBatch(List<DataInfo> dataInfoList);

    boolean insertBatch(List<DataInfo> dataInfoList);
}
