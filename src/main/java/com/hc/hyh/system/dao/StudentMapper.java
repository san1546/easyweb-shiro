package com.hc.hyh.system.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.hc.hyh.system.model.StudentInfo;
import com.hc.hyh.system.model.User;

import java.util.List;

public interface StudentMapper extends BaseMapper<StudentInfo> {

    StudentInfo getName(String studentName,String testno);

    List<StudentInfo> getTestNo(String testno);

    int getCount(String testno);

    int insertDetailBatch(List<StudentInfo> list);

    int updateDetailBatch(List<StudentInfo> list);
}
