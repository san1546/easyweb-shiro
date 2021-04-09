package com.hc.hyh.system.dao;

import com.baomidou.mybatisplus.mapper.*;
import com.hc.hyh.system.model.*;

import java.util.*;

public interface WordMapper extends BaseMapper<WordInfo> {

    WordInfo getID(String id);

    int insertWordBatch(List<WordInfo> list);

    int updateBatch(List<WordInfo> list);

}
