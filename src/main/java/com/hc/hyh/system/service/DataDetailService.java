package com.hc.hyh.system.service;

import com.hc.hyh.common.PageResult;
import com.hc.hyh.common.exception.BusinessException;
import com.hc.hyh.common.exception.ParameterException;
import com.hc.hyh.system.model.DataDetailInfo;

import java.util.List;

public interface DataDetailService {

    DataDetailInfo getByUsername(String username);

    PageResult<DataDetailInfo> list(int pageNum, int pageSize, boolean showDelete, String searchKey, String searchValue);

    DataDetailInfo getById(Integer userId);

    boolean add(DataDetailInfo data) throws BusinessException;

    boolean update(DataDetailInfo data);

    boolean updateState(Integer userId, int state) throws ParameterException;

    boolean updatePsw(Integer userId, String username, String newPsw);

    boolean delete(Integer userId);

    List<DataDetailInfo> list(String testno);

    boolean updateBatch(List<DataDetailInfo> dataInfoList);

    boolean insertBatch(List<DataDetailInfo> dataInfoList);

    List<DataDetailInfo> findList(String testno,String part,String score,String title);

    int findCount(String testno,String part,String score,String title);
}
