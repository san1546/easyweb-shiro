package com.hc.hyh.system.service;

import com.hc.hyh.common.PageResult;
import com.hc.hyh.common.exception.BusinessException;
import com.hc.hyh.common.exception.ParameterException;
import com.hc.hyh.system.model.StudentInfo;

import java.util.List;

public interface StudentService {

    StudentInfo getName(String studentName,String testno);

    PageResult<StudentInfo> list(int pageNum, int pageSize, boolean showDelete, String searchKey, String searchValue);

    List<StudentInfo> getTestNo(String testno);

    StudentInfo getById(Integer userId);

    boolean add(StudentInfo user) throws BusinessException;

    boolean update(StudentInfo user);

    boolean updateState(Integer userId, int state) throws ParameterException;

    boolean updatePsw(Integer userId, String username, String newPsw);

    boolean delete(Integer userId);

    boolean updateBatch(List<StudentInfo> dataInfoList);

    boolean insertBatch(List<StudentInfo> dataInfoList);

    int findCount(String testno);
}
