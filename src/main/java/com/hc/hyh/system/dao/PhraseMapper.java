package com.hc.hyh.system.dao;

import com.baomidou.mybatisplus.mapper.*;
import com.hc.hyh.system.model.*;

import java.util.*;

public interface PhraseMapper extends BaseMapper<PhraseInfo> {

    PhraseInfo getID(String id);

    int insertPhraseBatch(List<PhraseInfo> list);

    int updateBatch(List<PhraseInfo> list);

}
