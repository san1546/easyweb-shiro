package com.hc.hyh.system.dao;

import com.baomidou.mybatisplus.mapper.*;
import com.hc.hyh.system.model.*;

import java.util.*;

public interface CorpusMapper extends BaseMapper<CorpusInfo> {

    List<CorpusInfo> getName(String studentName);

    int insertBatch(List<CorpusInfo> list);

    int updateBatch(List<CorpusInfo> list);

}
