package com.hc.hyh.system.service;

import com.hc.hyh.common.*;
import com.hc.hyh.common.exception.*;
import com.hc.hyh.system.model.*;

import java.util.*;

public interface CorpusService {

    List<CorpusInfo> getName(String studentName);

    PageResult<CorpusInfo> list(int pageNum, int pageSize, boolean showDelete, String searchKey, String searchValue);

    CorpusInfo getById(Integer userId);

    boolean add(CorpusInfo Corpus) throws BusinessException;

    boolean update(CorpusInfo Corpus);

    boolean updateState(Integer userId, int state) throws ParameterException;

    boolean updatePsw(Integer userId, String username, String newPsw);

    boolean delete(Integer userId);

    List<CorpusInfo> list(String testno);

    boolean updateBatch(List<CorpusInfo> CorpusInfoList);

    boolean insertBatch(List<CorpusInfo> CorpusInfoList);
}
