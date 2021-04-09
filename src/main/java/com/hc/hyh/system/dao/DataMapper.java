package com.hc.hyh.system.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.hc.hyh.system.model.DataInfo;
import com.hc.hyh.system.model.User;

import java.util.List;

public interface DataMapper extends BaseMapper<DataInfo> {

    List<DataInfo> getName(String studentName);

    int insertBatch(List<DataInfo> list);

    int updateBatch(List<DataInfo> list);

}
