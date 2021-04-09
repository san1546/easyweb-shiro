package com.hc.hyh.system.model;

import com.baomidou.mybatisplus.annotations.*;
import lombok.Data;
import org.springframework.data.annotation.*;

import javax.persistence.*;
import javax.persistence.Id;
import java.util.*;

/**
 * 语料库表
 */
@Data
@TableName("chinese_corpus")
public class CorpusInfo {

    @TableId
    private Integer id;

    private String corpusDetail; //语料详情

    private String wordLevelId;  // 字表类

    private String phraseLevelId;  // 词表类

}
