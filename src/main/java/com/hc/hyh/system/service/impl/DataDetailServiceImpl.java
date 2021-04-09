package com.hc.hyh.system.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.hc.hyh.common.PageResult;
import com.hc.hyh.common.exception.BusinessException;
import com.hc.hyh.common.exception.ParameterException;
import com.hc.hyh.common.utils.SpringContextUtil;
import com.hc.hyh.common.utils.StringUtil;
import com.hc.hyh.system.dao.DataDetailMapper;
import com.hc.hyh.system.model.DataDetailInfo;
import com.hc.hyh.system.service.DataDetailService;
import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSession;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DataDetailServiceImpl implements DataDetailService {
    @Autowired
    private DataDetailMapper dataDetailMapper;

    @Autowired
    private SqlSessionTemplate sqlSessionTemplate;//引入bean

    public DataDetailServiceImpl() {
        this.dataDetailMapper = SpringContextUtil.getBean(DataDetailMapper.class);
        this.sqlSessionTemplate = SpringContextUtil.getBean(SqlSessionTemplate.class);
    }

    @Override
    public DataDetailInfo getByUsername(String username) {
        return dataDetailMapper.getByUsername(username);
    }

    @Override
    public PageResult<DataDetailInfo> list(int pageNum, int pageSize, boolean showDelete, String column, String value) {
        Wrapper<DataDetailInfo> wrapper = new EntityWrapper<>();
        if (StringUtil.isNotBlank(column)) {
            wrapper.like(column, value);
        }
        if (!showDelete) {  // 不显示锁定的用户
            wrapper.eq("state", 0);
        }
        Page<DataDetailInfo> dataInfoPage = new Page<>(pageNum, pageSize);
//        List<DataDetailInfo> dataInfoList = dataDetailMapper.selectPage(dataInfoPage, wrapper.orderBy("testno", true));
        List<DataDetailInfo> dataInfoList = dataDetailMapper.selectPage(dataInfoPage, wrapper);
        if (dataInfoList != null && dataInfoList.size() > 0) {
//            System.out.println("part_num:"+dataInfoList.get(0).getPartNum());
//            System.out.println("name:"+dataInfoList.get(0).getName());
        }
        return new PageResult<>(dataInfoPage.getTotal(), dataInfoList);
    }

    @Override
    public DataDetailInfo getById(Integer userId) {
        return dataDetailMapper.selectById(userId);
    }

    @Override
    public boolean add(DataDetailInfo data) throws BusinessException {
        return dataDetailMapper.insert(data) > 0;
    }

    @Override
    public boolean update(DataDetailInfo data) {
        return dataDetailMapper.updateById(data) > 0;
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
        return dataDetailMapper.deleteById(userId) > 0;
    }

    @Override
    public List<DataDetailInfo> list(String testno) {
        Wrapper<DataDetailInfo> wrapper = new EntityWrapper<>();
        wrapper.eq("testno", testno);
        List<DataDetailInfo> dataInfoList = dataDetailMapper.selectList(wrapper);
        if (dataInfoList != null && dataInfoList.size() > 0) {
            for (DataDetailInfo DataDetailInfo:dataInfoList) {
            }
        }
        return dataInfoList;
    }

    @Override
    public boolean updateBatch(List<DataDetailInfo> dataInfoList) {
        SqlSession session = sqlSessionTemplate.getSqlSessionFactory().openSession(ExecutorType.BATCH, false);
        boolean result = false;
        try {
//            int i=0;
//            for (DataDetailInfo DataDetailInfo:dataInfoList) {
//                dataDetailMapper.updateById(DataDetailInfo);
//                if (i % 1000 == 0 || i == dataInfoList.size()-1) {                        //手动每1000个一提交，提交后无法回滚
//                    session.commit();
//                    session.clearCache();//注意，如果没有这个动作，可能会导致内存崩溃。
//                }
//                i++;
//            }
            dataDetailMapper.updateDetailBatch(dataInfoList);
            result = true;
        }catch (Exception e) {            //没有提交的数据可以回滚
            session.rollback();
            System.out.println("数据回滚：DataDetailInfo批量更新");
            System.out.println("异常:"+e.getMessage());
        } finally{
            session.close();
        }
        return result;
    }

    @Override
    public boolean insertBatch(List<DataDetailInfo> dataInfoList) {
        SqlSession session = sqlSessionTemplate.getSqlSessionFactory().openSession(ExecutorType.BATCH, false);
        boolean result = false;
        try {
//            int i=0;
//            for (DataDetailInfo DataDetailInfo:dataInfoList) {
//                dataDetailMapper.insert(DataDetailInfo);
//                if (i % 500 == 0 || i == dataInfoList.size()-1) {                        //手动每1000个一提交，提交后无法回滚
//                    session.commit();
//                    session.clearCache();//注意，如果没有这个动作，可能会导致内存崩溃。
//                }
//                i++;
//            }
            dataDetailMapper.insertDetailBatch(dataInfoList);
            session.commit();
            session.clearCache();//注意，如果没有这个动作，可能会导致内存崩溃。
            result = true;
        }catch (Exception e) {            //没有提交的数据可以回滚
            session.rollback();
            System.out.println("数据回滚：DataDetailInfo批量插入");
            System.out.println("异常:"+e.getMessage());
        } finally{
            session.close();
        }
        return result;
    }


    @Override
    public List<DataDetailInfo> findList(String testno,String part,String score,String title){
        return dataDetailMapper.getList(testno,part,score,title);
    }

    @Override
    public int findCount(String testno,String part,String score,String title){
        return dataDetailMapper.getCount(testno,part,score,title);
    }
}
