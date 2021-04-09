package com.hc.hyh.system.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.hc.hyh.common.PageResult;
import com.hc.hyh.common.exception.BusinessException;
import com.hc.hyh.common.exception.ParameterException;
import com.hc.hyh.common.utils.SpringContextUtil;
import com.hc.hyh.common.utils.StringUtil;
import com.hc.hyh.system.dao.TestDetailMapper;
import com.hc.hyh.system.model.DataDetailInfo;
import com.hc.hyh.system.model.TestDetailInfo;
import com.hc.hyh.system.service.TestDetailService;
import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSession;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import java.util.List;

@Service
public class TestDetailServiceImpl implements TestDetailService {
    @Autowired
    private TestDetailMapper testDetailMapper;
    @Autowired
    private SqlSessionTemplate sqlSessionTemplate;//引入bean

    public TestDetailServiceImpl() {
        this.testDetailMapper = SpringContextUtil.getBean(TestDetailMapper.class);
        this.sqlSessionTemplate = SpringContextUtil.getBean(SqlSessionTemplate.class);
    }

    @Override
    public TestDetailInfo getTestNo(int testno,String title) {
        return testDetailMapper.getByTestNo(testno,title);
    }

    @Override
    public PageResult<TestDetailInfo> list(int pageNum, int pageSize, boolean showDelete, String column, String value) {
         Wrapper<TestDetailInfo> wrapper = new EntityWrapper<>();
        if (StringUtil.isNotBlank(column)) {
            wrapper.like(column, value);
        }
        if (!showDelete) {  // 不显示锁定的用户
            wrapper.eq("state", 0);
        }
        Page<TestDetailInfo> testPage = new Page<>(pageNum, pageSize);
//        List<DataDetailInfo> dataInfoList = dataDetailMapper.selectPage(dataInfoPage, wrapper.orderBy("testno", true));
        List<TestDetailInfo> testDetailInfoList = testDetailMapper.selectPage(testPage, wrapper);
        if (testDetailInfoList != null && testDetailInfoList.size() > 0) {
//            System.out.println("part_num:"+dataInfoList.get(0).getPartNum());
//            System.out.println("name:"+dataInfoList.get(0).getName());
        }
        return new PageResult<>(testPage.getTotal(), testDetailInfoList);
    }

    @Override
    public TestDetailInfo getById(Integer userId) {
        return null;
    }

    @Override
    public boolean add(TestDetailInfo data) throws BusinessException {
        return testDetailMapper.insert(data) > 0;
    }

    @Override
    public boolean update(TestDetailInfo user) {
        return false;
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
        return false;
    }

    @Override
    public boolean updateBatch(List<TestDetailInfo> testDetailInfoList) {
        SqlSession session = sqlSessionTemplate.getSqlSessionFactory().openSession(ExecutorType.BATCH, false);
        boolean result = false;
        try {
//            int i=0;
//            for (TestDetailInfo testDetailInfo:testDetailInfoList) {
//                testDetailMapper.updateById(testDetailInfo);
//                if (i % 1000 == 0 || i == testDetailInfoList.size()-1) {                        //手动每1000个一提交，提交后无法回滚
//                    session.commit();
//                    session.clearCache();//注意，如果没有这个动作，可能会导致内存崩溃。
//                }
//                i++;
//            }
            testDetailMapper.updateTestDetailBatch(testDetailInfoList);
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
    public boolean insertBatch(List<TestDetailInfo> testDetailInfoList) {
        SqlSession session = sqlSessionTemplate.getSqlSessionFactory().openSession(ExecutorType.BATCH, false);
        boolean result = false;
        try {
//            int i=0;
//            for (TestDetailInfo testDetailInfo:testDetailInfoList) {
//                testDetailMapper.insert(testDetailInfo);
//                if (i % 1000 == 0 || i == testDetailInfoList.size()-1) {                        //手动每1000个一提交，提交后无法回滚
//                    session.commit();
//                    session.clearCache();//注意，如果没有这个动作，可能会导致内存崩溃。
//                }
//                i++;
//            }
            testDetailMapper.insertTestDetailBatch(testDetailInfoList);
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


}
