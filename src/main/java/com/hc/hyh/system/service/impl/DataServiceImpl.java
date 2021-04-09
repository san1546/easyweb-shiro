package com.hc.hyh.system.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.hc.hyh.common.PageResult;
import com.hc.hyh.common.exception.BusinessException;
import com.hc.hyh.common.exception.ParameterException;
import com.hc.hyh.common.shiro.EndecryptUtil;
import com.hc.hyh.common.utils.SpringContextUtil;
import com.hc.hyh.common.utils.StringUtil;
import com.hc.hyh.system.dao.DataMapper;
import com.hc.hyh.system.model.DataInfo;
import com.hc.hyh.system.service.DataService;
import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSession;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class DataServiceImpl implements DataService {
    @Autowired
    private DataMapper dataMapper;

    @Autowired
    private SqlSessionTemplate sqlSessionTemplate;//引入bean

    public DataServiceImpl() {
        this.dataMapper = SpringContextUtil.getBean(DataMapper.class);
        this.sqlSessionTemplate = SpringContextUtil.getBean(SqlSessionTemplate.class);
    }

    @Override
    public List<DataInfo> getName(String studentName) {
        return dataMapper.getName(studentName);
    }

    @Override
    public PageResult<DataInfo> list(int pageNum, int pageSize, boolean showDelete, String column, String value) {
        Wrapper<DataInfo> wrapper = new EntityWrapper<>();
        if (StringUtil.isNotBlank(column)) {
            wrapper.like(column, value);
        }
        if (!showDelete) {  // 不显示锁定的用户
            wrapper.eq("state", 0);
        }
        Page<DataInfo> dataInfoPage = new Page<>(pageNum, pageSize);
//        List<DataInfo> dataInfoList = dataMapper.selectPage(dataInfoPage, wrapper.orderBy("testno", true));
        List<DataInfo> dataInfoList = dataMapper.selectPage(dataInfoPage, wrapper);
        if (dataInfoList != null && dataInfoList.size() > 0) {
//            System.out.println("part_num:"+dataInfoList.get(0).getPartNum());
//            System.out.println("name:"+dataInfoList.get(0).getName());
        }
        return new PageResult<>(dataInfoPage.getTotal(), dataInfoList);
    }

    @Override
    public DataInfo getById(Integer userId) {
        return dataMapper.selectById(userId);
    }

    @Override
    public boolean add(DataInfo data) throws BusinessException {
        return dataMapper.insert(data) > 0;
    }

    @Override
    public boolean update(DataInfo data) {
        return dataMapper.updateById(data) > 0;
    }

    @Override
    public boolean updateState(Integer userId, int state) throws ParameterException {
        return false;
    }

    @Override
    public boolean updatePsw(Integer userId, String username, String newPsw) {
        return false;
    }

    @Override
    public boolean delete(Integer userId) {
        return dataMapper.deleteById(userId) > 0;
    }

    @Override
    public List<DataInfo> list(String testno) {
        Wrapper<DataInfo> wrapper = new EntityWrapper<>();
        wrapper.eq("testno", testno);
        List<DataInfo> dataInfoList = dataMapper.selectList(wrapper);
        if (dataInfoList != null && dataInfoList.size() > 0) {
            for (DataInfo dataInfo:dataInfoList) {
            }
        }
        return dataInfoList;
    }

    @Override
    public boolean updateBatch(List<DataInfo> dataInfoList) {
        SqlSession session = sqlSessionTemplate.getSqlSessionFactory().openSession(ExecutorType.BATCH, false);
        boolean result = false;
        try {
//            int i=0;
//            for (DataInfo datainfo:dataInfoList) {
//                dataMapper.updateById(datainfo);
//                if (i % 500 == 0 || i == dataInfoList.size()-1) {                        //手动每1000个一提交，提交后无法回滚
//                    session.commit();
//                    session.clearCache();//注意，如果没有这个动作，可能会导致内存崩溃。
//                }
//                i++;
//            }

            dataMapper.updateBatch(dataInfoList);

            result = true;
        }catch (Exception e) {            //没有提交的数据可以回滚
            session.rollback();
            System.out.println("数据回滚：DataInfo批量更新");
            System.out.println("异常:"+e.getMessage());
        } finally{
            session.close();
        }
        return result;
    }

    @Override
    public boolean insertBatch(List<DataInfo> dataInfoList) {
        SqlSession session = sqlSessionTemplate.getSqlSessionFactory().openSession(ExecutorType.BATCH, false);
        boolean result = false;
        try {
//            int i=0;
//            for (DataInfo datainfo:dataInfoList) {
//                dataMapper.insert(datainfo);
//                if (i % 500 == 0 || i == dataInfoList.size()-1) {                        //手动每1000个一提交，提交后无法回滚
//                    session.commit();
//                    session.clearCache();//注意，如果没有这个动作，可能会导致内存崩溃。
//                }
//                i++;
//            }
            dataMapper.insertBatch(dataInfoList);
            session.commit();
            session.clearCache();
            result = true;
        }catch (Exception e) {            //没有提交的数据可以回滚
            session.rollback();
            System.out.println("数据回滚：DataInfo批量插入");
            System.out.println("异常:"+e.getMessage());
        } finally{
            session.close();
        }
        return result;
    }





}
