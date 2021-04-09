package com.hc.hyh.system.model;

import com.baomidou.mybatisplus.annotations.*;
import lombok.Data;

/**
 * 语料库联合工具类，只用于查询
 */
@Data
public class CorpusUtil {
    private Integer id;
    private String corpusDetail; //语料详情
    private String wordDetailLevel1;  // 具体语料中一级字详情
    private String wordDetailLevel2;  // 具体语料中二级字详情
    private String wordDetailLevel3;  // 具体语料中三级字详情
    private String wordDetailLevel4;  // 具体语料中四级字详情
    private String wordDetailLevel5; //具体语料中五级字详情
    private String wordDetailLevel6; //具体语料中六级字详情
    private String wordDetailLevel7; //具体语料中七级字详情
    private String wordDetailLevel8; //具体语料中八级字详情
    private String wordDetailLevel9; //具体语料中九级字详情
    private String wordDetailLevel10; //具体语料中十级字详情
    private Integer wordDetailTotalLevel1; //具体语料中一级字的数量
    private Integer wordDetailTotalLevel2; //具体语料中二级字的数量
    private Integer wordDetailTotalLevel3; //具体语料中三级字的数量
    private Integer wordDetailTotalLevel4; //具体语料中四级字的数量
    private Integer wordDetailTotalLevel5; //具体语料中五级字的数量
    private Integer wordDetailTotalLevel6; //具体语料中六级字的数量
    private Integer wordDetailTotalLevel7; //具体语料中七级字的数量
    private Integer wordDetailTotalLevel8; //具体语料中八级字的数量
    private Integer wordDetailTotalLevel9; //具体语料中九级字的数量
    private Integer wordDetailTotalLevel10; //具体语料中十级字的数量
    private String phraseDetailLevel1;  // 具体语料中一级词详情
    private String phraseDetailLevel2;  // 具体语料中二级词详情
    private String phraseDetailLevel3;  // 具体语料中三级词详情
    private String phraseDetailLevel4;  // 具体语料中四级词详情
    private String phraseDetailLevel5; //具体语料中五级词详情
    private String phraseDetailLevel6; //具体语料中六级词详情
    private String phraseDetailLevel7; //具体语料中七级词详情
    private String phraseDetailLevel8; //具体语料中八级词详情
    private String phraseDetailLevel9; //具体语料中九级词详情
    private String phraseDetailLevel10; //具体语料中十级词详情
    private Integer phraseDetailTotalLevel1; //具体语料中一级词的数量
    private Integer phraseDetailTotalLevel2; //具体语料中二级词的数量
    private Integer phraseDetailTotalLevel3; //具体语料中三级词的数量
    private Integer phraseDetailTotalLevel4; //具体语料中四级词的数量
    private Integer phraseDetailTotalLevel5; //具体语料中五级词的数量
    private Integer phraseDetailTotalLevel6; //具体语料中六级词的数量
    private Integer phraseDetailTotalLevel7; //具体语料中七级词的数量
    private Integer phraseDetailTotalLevel8; //具体语料中八级词的数量
    private Integer phraseDetailTotalLevel9; //具体语料中九级词的数量
    private Integer phraseDetailTotalLevel10; //具体语料中十级词的数量
}
