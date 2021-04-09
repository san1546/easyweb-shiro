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
import com.hc.hyh.system.dao.StudentMapper;
import com.hc.hyh.system.model.Role;
import com.hc.hyh.system.model.StudentInfo;
import com.hc.hyh.system.service.StudentService;
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
public class StudentServiceImpl implements StudentService {
    @Autowired
    private StudentMapper studentMapper;
    @Autowired
    private SqlSessionTemplate sqlSessionTemplate;//引入bean

    public StudentServiceImpl() {
        this.studentMapper = SpringContextUtil.getBean(StudentMapper.class);
        this.sqlSessionTemplate = SpringContextUtil.getBean(SqlSessionTemplate.class);
    }

    @Override
    public StudentInfo getName(String studentName,String testno) {
        return studentMapper.getName(studentName,testno);
    }

    @Override
    public PageResult<StudentInfo> list(int pageNum, int pageSize, boolean showDelete, String searchKey, String searchValue) {
        return null;
    }

    @Override
    public List<StudentInfo> getTestNo(String testno){
        return studentMapper.getTestNo(testno);
    }

    @Override
    public StudentInfo getById(Integer userId) {
        return null;
    }

    @Override
    public boolean add(StudentInfo user) throws BusinessException {
        return false;
    }

    @Override
    public boolean update(StudentInfo user) {
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
    public boolean updateBatch(List<StudentInfo> studentInfoList) {
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
            studentMapper.updateDetailBatch(studentInfoList);
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
    public boolean insertBatch(List<StudentInfo> studentInfoList) {
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
            studentMapper.insertDetailBatch(studentInfoList);
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
    public int findCount(String testno) {
        return studentMapper.getCount(testno);
    }
}
