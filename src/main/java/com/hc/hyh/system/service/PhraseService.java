package com.hc.hyh.system.service;

import com.hc.hyh.common.*;
import com.hc.hyh.common.exception.*;
import com.hc.hyh.system.model.*;

import java.util.*;

public interface PhraseService {

    PhraseInfo getID(String studentName);

    PageResult<PhraseInfo> list(int pageNum, int pageSize, boolean showDelete, String searchKey, String searchValue);

    PhraseInfo getById(Integer userId);

    boolean add(PhraseInfo Phrase) throws BusinessException;

    boolean update(PhraseInfo Phrase);

    boolean updateState(Integer userId, int state) throws ParameterException;

    boolean updatePsw(Integer userId, String username, String newPsw);

    boolean delete(Integer userId);

    List<PhraseInfo> list(String testno);

    boolean updateBatch(List<PhraseInfo> PhraseInfoList);

    boolean insertBatch(List<PhraseInfo> PhraseInfoList);
}
