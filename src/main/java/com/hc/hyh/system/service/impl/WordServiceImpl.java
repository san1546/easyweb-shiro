package com.hc.hyh.system.service.impl;

import com.baomidou.mybatisplus.mapper.*;
import com.baomidou.mybatisplus.plugins.*;
import com.hc.hyh.common.*;
import com.hc.hyh.common.exception.*;
import com.hc.hyh.common.utils.*;
import com.hc.hyh.system.dao.*;
import com.hc.hyh.system.model.*;
import com.hc.hyh.system.service.*;
import org.apache.ibatis.session.*;
import org.mybatis.spring.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.stereotype.*;

import java.util.*;

@Service
public class WordServiceImpl implements WordService {
    @Autowired
    private WordMapper wordMapper;

    @Autowired
    private SqlSessionTemplate sqlSessionTemplate;//引入bean

    public WordServiceImpl() {
        this.wordMapper = SpringContextUtil.getBean(WordMapper.class);
        this.sqlSessionTemplate = SpringContextUtil.getBean(SqlSessionTemplate.class);
    }


    @Override
    public PageResult<WordInfo> list(int pageNum, int pageSize, boolean showDelete, String column, String value) {
        Wrapper<WordInfo> wrapper = new EntityWrapper<>();
        if (StringUtil.isNotBlank(column)) {
            wrapper.like(column, value);
        }
        if (!showDelete) {  // 不显示锁定的用户
            wrapper.eq("state", 0);
        }
        Page<WordInfo> WordInfoPage = new Page<>(pageNum, pageSize);
//        List<WordInfo> WordInfoList = wordMapper.selectPage(WordInfoPage, wrapper.orderBy("testno", true));
        List<WordInfo> WordInfoList = wordMapper.selectPage(WordInfoPage, wrapper);
        if (WordInfoList != null && WordInfoList.size() > 0) {
//            System.out.println("part_num:"+WordInfoList.get(0).getPartNum());
//            System.out.println("name:"+WordInfoList.get(0).getName());
        }
        return new PageResult<>(WordInfoPage.getTotal(), WordInfoList);
    }

    @Override
    public WordInfo getById(String userId) {
        return wordMapper.getID(userId);
    }

    @Override
    public boolean add(WordInfo Word) throws BusinessException {
        return wordMapper.insert(Word) > 0;
    }

    @Override
    public boolean update(WordInfo Word) {
        return wordMapper.updateById(Word) > 0;
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
        return wordMapper.deleteById(userId) > 0;
    }

    @Override
    public List<WordInfo> list(String testno) {
        Wrapper<WordInfo> wrapper = new EntityWrapper<>();
        wrapper.eq("testno", testno);
        List<WordInfo> WordInfoList = wordMapper.selectList(wrapper);
        if (WordInfoList != null && WordInfoList.size() > 0) {
            for (WordInfo WordInfo:WordInfoList) {
            }
        }
        return WordInfoList;
    }

    @Override
    public boolean updateBatch(List<WordInfo> WordInfoList) {
        SqlSession session = sqlSessionTemplate.getSqlSessionFactory().openSession(ExecutorType.BATCH, false);
        boolean result = false;
        try {
//            int i=0;
//            for (WordInfo Wordinfo:WordInfoList) {
//                wordMapper.updateById(Wordinfo);
//                if (i % 500 == 0 || i == WordInfoList.size()-1) {                        //手动每1000个一提交，提交后无法回滚
//                    session.commit();
//                    session.clearCache();//注意，如果没有这个动作，可能会导致内存崩溃。
//                }
//                i++;
//            }

            wordMapper.updateBatch(WordInfoList);

            result = true;
        }catch (Exception e) {            //没有提交的数据可以回滚
            session.rollback();
            System.out.println("数据回滚：WordInfo批量更新");
            System.out.println("异常:"+e.getMessage());
        } finally{
            session.close();
        }
        return result;
    }

    @Override
    public boolean insertBatch(List<WordInfo> WordInfoList) {
        SqlSession session = sqlSessionTemplate.getSqlSessionFactory().openSession(ExecutorType.BATCH, false);
        boolean result = false;
        try {
//            int i=0;
//            for (WordInfo Wordinfo:WordInfoList) {
//                wordMapper.insert(Wordinfo);
//                if (i % 500 == 0 || i == WordInfoList.size()-1) {                        //手动每1000个一提交，提交后无法回滚
//                    session.commit();
//                    session.clearCache();//注意，如果没有这个动作，可能会导致内存崩溃。
//                }
//                i++;
//            }
            wordMapper.insertWordBatch(WordInfoList);
            session.commit();
            session.clearCache();
            result = true;
        }catch (Exception e) {            //没有提交的数据可以回滚
            session.rollback();
            System.out.println("数据回滚：WordInfo批量插入");
            System.out.println("异常:"+e.getMessage());
        } finally{
            session.close();
        }
        return result;
    }





}
