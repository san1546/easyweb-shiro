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

import java.lang.reflect.*;
import java.util.*;

@Service
public class CorpusServiceImpl implements CorpusService {
    @Autowired
    private CorpusMapper corpusMapper;
    @Autowired
    private WordMapper wordMapper;
    @Autowired
    private PhraseMapper phraseMapper;
    @Autowired
    private SqlSessionTemplate sqlSessionTemplate;//引入bean

    public CorpusServiceImpl() {
        this.corpusMapper = SpringContextUtil.getBean(CorpusMapper.class);
        this.sqlSessionTemplate = SpringContextUtil.getBean(SqlSessionTemplate.class);
    }

    @Override
    public List<CorpusInfo> getName(String studentName) {
        return corpusMapper.getName(studentName);
    }

    @Override
    public PageResult<CorpusInfo> list(int pageNum, int pageSize, boolean showDelete, String column, String value) {
        Wrapper<CorpusInfo> wrapper = new EntityWrapper<>();
        List corpusUtils = new ArrayList<>();
        if (StringUtil.isNotBlank(column)) {
            wrapper.like(column, value);
        }
        if (!showDelete) {  // 不显示锁定的用户
            wrapper.eq("state", 0);
        }
        Page<CorpusInfo> CorpusInfoPage = new Page<>(pageNum, pageSize);
        List<CorpusInfo> CorpusInfoList = corpusMapper.selectPage(CorpusInfoPage, wrapper);
        if (CorpusInfoList != null && CorpusInfoList.size() > 0) {
            for (int i = 0; i < CorpusInfoList.size(); i++) {
                CorpusUtil corpusUtil = new CorpusUtil();
                PhraseInfo phraseInfo = phraseMapper.getID(CorpusInfoList.get(i).getPhraseLevelId());
                System.out.println("phraseInfo："+phraseInfo.getId());
                WordInfo wordInfo = wordMapper.getID(CorpusInfoList.get(i).getWordLevelId());
                System.out.println("wordInfo："+wordInfo.getId());
                corpusUtil.setId(CorpusInfoList.get(i).getId());
                corpusUtil.setCorpusDetail(CorpusInfoList.get(i).getCorpusDetail());
                corpusUtil.setPhraseDetailLevel1(phraseInfo.getPhraseDetailLevel1());
                corpusUtil.setPhraseDetailLevel2(phraseInfo.getPhraseDetailLevel2());
                corpusUtil.setPhraseDetailLevel3(phraseInfo.getPhraseDetailLevel3());
                corpusUtil.setPhraseDetailLevel4(phraseInfo.getPhraseDetailLevel4());
                corpusUtil.setPhraseDetailLevel5(phraseInfo.getPhraseDetailLevel5());
                corpusUtil.setPhraseDetailLevel6(phraseInfo.getPhraseDetailLevel6());
                corpusUtil.setPhraseDetailLevel7(phraseInfo.getPhraseDetailLevel7());
                corpusUtil.setPhraseDetailLevel8(phraseInfo.getPhraseDetailLevel8());
                corpusUtil.setPhraseDetailLevel9(phraseInfo.getPhraseDetailLevel9());
                corpusUtil.setPhraseDetailLevel10(phraseInfo.getPhraseDetailLevel10());
                corpusUtil.setWordDetailLevel1(wordInfo.getWordDetailLevel1());
                corpusUtil.setWordDetailLevel2(wordInfo.getWordDetailLevel2());
                corpusUtil.setWordDetailLevel3(wordInfo.getWordDetailLevel3());
                corpusUtil.setWordDetailLevel4(wordInfo.getWordDetailLevel4());
                corpusUtil.setWordDetailLevel5(wordInfo.getWordDetailLevel5());
                corpusUtil.setWordDetailLevel6(wordInfo.getWordDetailLevel6());
                corpusUtil.setWordDetailLevel7(wordInfo.getWordDetailLevel7());
                corpusUtil.setWordDetailLevel8(wordInfo.getWordDetailLevel8());
                corpusUtil.setWordDetailLevel9(wordInfo.getWordDetailLevel9());
                corpusUtil.setWordDetailLevel10(wordInfo.getWordDetailLevel10());
                System.out.println("PhraseDetailLevel:"+phraseInfo.getPhraseDetailLevel1());
                System.out.println("WordDetailLevel:"+wordInfo.getWordDetailLevel1());
                System.out.println("PhraseDetailLevel_list:"+corpusUtil.getPhraseDetailLevel1());
                System.out.println("WordDetailLevel_list:"+corpusUtil.getWordDetailLevel1());
                corpusUtils.add(corpusUtil);
            }
            for (int i = 0; i < corpusUtils.size(); i++) {
                System.out.println("corpusUtil:"+corpusUtils.get(i));
            }
        }
        return new PageResult<>(CorpusInfoPage.getTotal(), corpusUtils);
    }


    /**
     * 遍历对象中的所有属性值
     * @param obj
     * @throws NoSuchMethodException
     * @throws IllegalAccessException
     * @throws IllegalArgumentException
     * @throws InvocationTargetException
     */
    public static void Reflect(Object obj) throws NoSuchMethodException, IllegalAccessException, IllegalArgumentException, InvocationTargetException{
        Field[] field = obj.getClass().getDeclaredFields();     //获取实体类的所有属性，返回Field数组
        for(int j=0 ; j<field.length ; j++){                 //遍历所有属性
            String fname = field[j].getName();             //获取属性名称

            System.out.println("attribute fname:"+fname);
            fname = fname.substring(0,1).toUpperCase()+fname.substring(1); //将属性的首字符大写，方便构造get，set方法
            String ftype = field[j].getGenericType().toString();             //获取属性的类型
            if(ftype.equals("class java.lang.String")){         //如果ftype是类类型，则前面包含"class "，后面跟类名
                Method m = obj.getClass().getMethod("get"+fname);
                String value = (String) m.invoke(obj);             //利用反射原理，调用getter方法获取属性值
                if(value != null){
                    //对属性值的操作逻辑（如改变编码）	
                    System.out.println("attribute value:"+value);
                }
            }
            if(ftype.equals("class java.lang.Integer")){
                Method m = obj.getClass().getMethod("get"+fname);
                Integer value = (Integer) m.invoke(obj);
                if(value != null){
                    System.out.println("attribute value:"+value);
                }
            }
            if(ftype.equals("class java.lang.Short")){
                Method m = obj.getClass().getMethod("get"+fname);
                Short value = (Short) m.invoke(obj);
                if(value != null){
                    System.out.println("attribute value:"+value);    }
            }
            if(ftype.equals("class java.lang.Double")){
                Method m = obj.getClass().getMethod("get"+fname);
                Double value = (Double) m.invoke(obj);
                if(value != null){
                    System.out.println("attribute value:"+value);
                }
            }
            if(ftype.equals("class java.lang.Boolean")){
                Method m = obj.getClass().getMethod("get"+fname);
                Boolean value = (Boolean) m.invoke(obj);
                if(value != null){
                    System.out.println("attribute value:"+value);
                }
            }
            if(ftype.equals("class java.util.Date")){
                Method m = obj.getClass().getMethod("get"+fname);
                Date value = (Date) m.invoke(obj);
                if(value != null){
                    System.out.println("attribute value:"+value.toLocaleString());
                }
            }
        }
    }

    
    
    @Override
    public CorpusInfo getById(Integer userId) {
        return corpusMapper.selectById(userId);
    }

    @Override
    public boolean add(CorpusInfo Corpus) throws BusinessException {
        return corpusMapper.insert(Corpus) > 0;
    }

    @Override
    public boolean update(CorpusInfo Corpus) {
        return corpusMapper.updateById(Corpus) > 0;
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
        return corpusMapper.deleteById(userId) > 0;
    }

    @Override
    public List<CorpusInfo> list(String testno) {
        Wrapper<CorpusInfo> wrapper = new EntityWrapper<>();
        wrapper.eq("testno", testno);
        List<CorpusInfo> CorpusInfoList = corpusMapper.selectList(wrapper);
        if (CorpusInfoList != null && CorpusInfoList.size() > 0) {
            for (CorpusInfo CorpusInfo:CorpusInfoList) {
            }
        }
        return CorpusInfoList;
    }

    @Override
    public boolean updateBatch(List<CorpusInfo> CorpusInfoList) {
        SqlSession session = sqlSessionTemplate.getSqlSessionFactory().openSession(ExecutorType.BATCH, false);
        boolean result = false;
        try {
//            int i=0;
//            for (CorpusInfo Corpusinfo:CorpusInfoList) {
//                corpusMapper.updateById(Corpusinfo);
//                if (i % 500 == 0 || i == CorpusInfoList.size()-1) {                        //手动每1000个一提交，提交后无法回滚
//                    session.commit();
//                    session.clearCache();//注意，如果没有这个动作，可能会导致内存崩溃。
//                }
//                i++;
//            }

            corpusMapper.updateBatch(CorpusInfoList);

            result = true;
        }catch (Exception e) {            //没有提交的数据可以回滚
            session.rollback();
            System.out.println("数据回滚：CorpusInfo批量更新");
            System.out.println("异常:"+e.getMessage());
        } finally{
            session.close();
        }
        return result;
    }

    @Override
    public boolean insertBatch(List<CorpusInfo> CorpusInfoList) {
        SqlSession session = sqlSessionTemplate.getSqlSessionFactory().openSession(ExecutorType.BATCH, false);
        boolean result = false;
        try {
//            int i=0;
//            for (CorpusInfo Corpusinfo:CorpusInfoList) {
//                corpusMapper.insert(Corpusinfo);
//                if (i % 500 == 0 || i == CorpusInfoList.size()-1) {                        //手动每1000个一提交，提交后无法回滚
//                    session.commit();
//                    session.clearCache();//注意，如果没有这个动作，可能会导致内存崩溃。
//                }
//                i++;
//            }
            corpusMapper.insertBatch(CorpusInfoList);
            session.commit();
            session.clearCache();
            result = true;
        }catch (Exception e) {            //没有提交的数据可以回滚
            session.rollback();
            System.out.println("数据回滚：CorpusInfo批量插入");
            System.out.println("异常:"+e.getMessage());
        } finally{
            session.close();
        }
        return result;
    }





}
