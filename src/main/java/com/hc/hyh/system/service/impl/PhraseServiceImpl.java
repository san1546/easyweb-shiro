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
public class PhraseServiceImpl implements PhraseService {
    @Autowired
    private PhraseMapper phraseMapper;

    @Autowired
    private SqlSessionTemplate sqlSessionTemplate;//引入bean

    public PhraseServiceImpl() {
        this.phraseMapper = SpringContextUtil.getBean(PhraseMapper.class);
        this.sqlSessionTemplate = SpringContextUtil.getBean(SqlSessionTemplate.class);
    }

    @Override
    public PhraseInfo getID(String id) {
        return phraseMapper.getID(id);
    }

    @Override
    public PageResult<PhraseInfo> list(int pageNum, int pageSize, boolean showDelete, String column, String value) {
        Wrapper<PhraseInfo> wrapper = new EntityWrapper<>();
        if (StringUtil.isNotBlank(column)) {
            wrapper.like(column, value);
        }
        if (!showDelete) {  // 不显示锁定的用户
            wrapper.eq("state", 0);
        }
        Page<PhraseInfo> PhraseInfoPage = new Page<>(pageNum, pageSize);
//        List<PhraseInfo> PhraseInfoList = phraseMapper.selectPage(PhraseInfoPage, wrapper.orderBy("testno", true));
        List<PhraseInfo> PhraseInfoList = phraseMapper.selectPage(PhraseInfoPage, wrapper);
        if (PhraseInfoList != null && PhraseInfoList.size() > 0) {
//            System.out.println("part_num:"+PhraseInfoList.get(0).getPartNum());
//            System.out.println("name:"+PhraseInfoList.get(0).getName());
        }
        return new PageResult<>(PhraseInfoPage.getTotal(), PhraseInfoList);
    }

    @Override
    public PhraseInfo getById(Integer userId) {
        return phraseMapper.selectById(userId);
    }

    @Override
    public boolean add(PhraseInfo Phrase) throws BusinessException {
        return phraseMapper.insert(Phrase) > 0;
    }

    @Override
    public boolean update(PhraseInfo Phrase) {
        return phraseMapper.updateById(Phrase) > 0;
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
        return phraseMapper.deleteById(userId) > 0;
    }

    @Override
    public List<PhraseInfo> list(String testno) {
        Wrapper<PhraseInfo> wrapper = new EntityWrapper<>();
        wrapper.eq("testno", testno);
        List<PhraseInfo> PhraseInfoList = phraseMapper.selectList(wrapper);
        if (PhraseInfoList != null && PhraseInfoList.size() > 0) {
            for (PhraseInfo PhraseInfo:PhraseInfoList) {
            }
        }
        return PhraseInfoList;
    }

    @Override
    public boolean updateBatch(List<PhraseInfo> PhraseInfoList) {
        SqlSession session = sqlSessionTemplate.getSqlSessionFactory().openSession(ExecutorType.BATCH, false);
        boolean result = false;
        try {
//            int i=0;
//            for (PhraseInfo Phraseinfo:PhraseInfoList) {
//                phraseMapper.updateById(Phraseinfo);
//                if (i % 500 == 0 || i == PhraseInfoList.size()-1) {                        //手动每1000个一提交，提交后无法回滚
//                    session.commit();
//                    session.clearCache();//注意，如果没有这个动作，可能会导致内存崩溃。
//                }
//                i++;
//            }

            phraseMapper.updateBatch(PhraseInfoList);

            result = true;
        }catch (Exception e) {            //没有提交的数据可以回滚
            session.rollback();
            System.out.println("数据回滚：PhraseInfo批量更新");
            System.out.println("异常:"+e.getMessage());
        } finally{
            session.close();
        }
        return result;
    }

    @Override
    public boolean insertBatch(List<PhraseInfo> PhraseInfoList) {
        SqlSession session = sqlSessionTemplate.getSqlSessionFactory().openSession(ExecutorType.BATCH, false);
        boolean result = false;
        try {
//            int i=0;
//            for (PhraseInfo Phraseinfo:PhraseInfoList) {
//                phraseMapper.insert(Phraseinfo);
//                if (i % 500 == 0 || i == PhraseInfoList.size()-1) {                        //手动每1000个一提交，提交后无法回滚
//                    session.commit();
//                    session.clearCache();//注意，如果没有这个动作，可能会导致内存崩溃。
//                }
//                i++;
//            }
            phraseMapper.insertPhraseBatch(PhraseInfoList);
            session.commit();
            session.clearCache();
            result = true;
        }catch (Exception e) {            //没有提交的数据可以回滚
            session.rollback();
            System.out.println("数据回滚：PhraseInfo批量插入");
            System.out.println("异常:"+e.getMessage());
        } finally{
            session.close();
        }
        return result;
    }





}
