package com.hc.hyh.system.service;

import com.hc.hyh.common.*;
import com.hc.hyh.common.exception.*;
import com.hc.hyh.system.model.*;

import java.util.*;

public interface WordService {

    PageResult<WordInfo> list(int pageNum, int pageSize, boolean showDelete, String searchKey, String searchValue);

    WordInfo getById(String userId);

    boolean add(WordInfo Word) throws BusinessException;

    boolean update(WordInfo Word);

    boolean updateState(Integer userId, int state) throws ParameterException;

    boolean updatePsw(Integer userId, String username, String newPsw);

    boolean delete(Integer userId);

    List<WordInfo> list(String testno);

    boolean updateBatch(List<WordInfo> WordInfoList);

    boolean insertBatch(List<WordInfo> WordInfoList);
}
