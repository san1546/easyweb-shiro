package com.hc.hyh.system.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.hc.hyh.system.model.DataDetailInfo;

import java.util.List;

public interface DataDetailMapper extends BaseMapper<DataDetailInfo> {

    DataDetailInfo getByUsername(String username);

    List<DataDetailInfo> getList(String testno,String part,String score,String title);

    int getCount(String testno,String part,String score,String title);

    int insertDetailBatch(List<DataDetailInfo> list);

    int updateDetailBatch(List<DataDetailInfo> list);

}
