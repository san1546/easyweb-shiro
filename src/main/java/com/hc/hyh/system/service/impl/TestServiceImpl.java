package com.hc.hyh.system.service.impl;

import com.hc.hyh.common.PageResult;
import com.hc.hyh.common.exception.BusinessException;
import com.hc.hyh.common.exception.ParameterException;
import com.hc.hyh.common.utils.SpringContextUtil;
import com.hc.hyh.system.dao.TestMapper;
import com.hc.hyh.system.model.TestInfo;
import com.hc.hyh.system.service.TestService;
import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSession;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TestServiceImpl implements TestService {
    @Autowired
    private TestMapper testMapper;
    @Autowired
    private SqlSessionTemplate sqlSessionTemplate;//引入bean

    public TestServiceImpl() {
        this.testMapper = SpringContextUtil.getBean(TestMapper.class);
        this.sqlSessionTemplate = SpringContextUtil.getBean(SqlSessionTemplate.class);
    }
    @Override
    public TestInfo getTestNo(String testno,String part) {
        return testMapper.getByTestNo(testno,part);
    }

    @Override
    public List<TestInfo> getList(String testno){
        return testMapper.getList(testno);
    }


    @Override
    public PageResult<TestInfo> list(int pageNum, int pageSize, boolean showDelete, String searchKey, String searchValue) {
        return null;
    }

    @Override
    public TestInfo getById(Integer userId) {
        return null;
    }

    @Override
    public boolean add(TestInfo data) throws BusinessException {
        return testMapper.insert(data) > 0;
    }

    @Override
    public boolean update(TestInfo user) {
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
    public boolean insertBatch(List<TestInfo> testInfoList) {
        SqlSession session = sqlSessionTemplate.getSqlSessionFactory().openSession(ExecutorType.BATCH, false);
        boolean result = false;
        try {
//            int i=0;
//            for (TestInfo datainfo:testInfoList) {
//                if (i % 1000 == 0 || i == testInfoList.size()-1) {                        //手动每1000个一提交，提交后无法回滚
//                    session.commit();
//                    session.clearCache();//注意，如果没有这个动作，可能会导致内存崩溃。
//                }
//                i++;
//            }
            testMapper.insertTestBatch(testInfoList);
            session.commit();
            session.clearCache();
            result = true;
        }catch (Exception e) {            //没有提交的数据可以回滚
            session.rollback();
            System.out.println("事务回滚");
        } finally{
            session.close();
        }
        return result;
    }


}
