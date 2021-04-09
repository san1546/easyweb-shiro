package com.hc.hyh.system.model;

import com.baomidou.mybatisplus.annotations.*;

import javax.persistence.*;

/**
 * 字表
 */
@TableName("wordlevel")
public class WordInfo {

    @TableId
    private String id;  // ID
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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getWordDetailLevel1() {
        return wordDetailLevel1;
    }

    public void setWordDetailLevel1(String wordDetailLevel1) {
        this.wordDetailLevel1 = wordDetailLevel1;
    }

    public String getWordDetailLevel2() {
        return wordDetailLevel2;
    }

    public void setWordDetailLevel2(String wordDetailLevel2) {
        this.wordDetailLevel2 = wordDetailLevel2;
    }

    public String getWordDetailLevel3() {
        return wordDetailLevel3;
    }

    public void setWordDetailLevel3(String wordDetailLevel3) {
        this.wordDetailLevel3 = wordDetailLevel3;
    }

    public String getWordDetailLevel4() {
        return wordDetailLevel4;
    }

    public void setWordDetailLevel4(String wordDetailLevel4) {
        this.wordDetailLevel4 = wordDetailLevel4;
    }

    public String getWordDetailLevel5() {
        return wordDetailLevel5;
    }

    public void setWordDetailLevel5(String wordDetailLevel5) {
        this.wordDetailLevel5 = wordDetailLevel5;
    }

    public String getWordDetailLevel6() {
        return wordDetailLevel6;
    }

    public void setWordDetailLevel6(String wordDetailLevel6) {
        this.wordDetailLevel6 = wordDetailLevel6;
    }

    public String getWordDetailLevel7() {
        return wordDetailLevel7;
    }

    public void setWordDetailLevel7(String wordDetailLevel7) {
        this.wordDetailLevel7 = wordDetailLevel7;
    }

    public String getWordDetailLevel8() {
        return wordDetailLevel8;
    }

    public void setWordDetailLevel8(String wordDetailLevel8) {
        this.wordDetailLevel8 = wordDetailLevel8;
    }

    public String getWordDetailLevel9() {
        return wordDetailLevel9;
    }

    public void setWordDetailLevel9(String wordDetailLevel9) {
        this.wordDetailLevel9 = wordDetailLevel9;
    }

    public String getWordDetailLevel10() {
        return wordDetailLevel10;
    }

    public void setWordDetailLevel10(String wordDetailLevel10) {
        this.wordDetailLevel10 = wordDetailLevel10;
    }

    public Integer getWordDetailTotalLevel1() {
        return wordDetailTotalLevel1;
    }

    public void setWordDetailTotalLevel1(Integer wordDetailTotalLevel1) {
        this.wordDetailTotalLevel1 = wordDetailTotalLevel1;
    }

    public Integer getWordDetailTotalLevel2() {
        return wordDetailTotalLevel2;
    }

    public void setWordDetailTotalLevel2(Integer wordDetailTotalLevel2) {
        this.wordDetailTotalLevel2 = wordDetailTotalLevel2;
    }

    public Integer getWordDetailTotalLevel3() {
        return wordDetailTotalLevel3;
    }

    public void setWordDetailTotalLevel3(Integer wordDetailTotalLevel3) {
        this.wordDetailTotalLevel3 = wordDetailTotalLevel3;
    }

    public Integer getWordDetailTotalLevel4() {
        return wordDetailTotalLevel4;
    }

    public void setWordDetailTotalLevel4(Integer wordDetailTotalLevel4) {
        this.wordDetailTotalLevel4 = wordDetailTotalLevel4;
    }

    public Integer getWordDetailTotalLevel5() {
        return wordDetailTotalLevel5;
    }

    public void setWordDetailTotalLevel5(Integer wordDetailTotalLevel5) {
        this.wordDetailTotalLevel5 = wordDetailTotalLevel5;
    }

    public Integer getWordDetailTotalLevel6() {
        return wordDetailTotalLevel6;
    }

    public void setWordDetailTotalLevel6(Integer wordDetailTotalLevel6) {
        this.wordDetailTotalLevel6 = wordDetailTotalLevel6;
    }

    public Integer getWordDetailTotalLevel7() {
        return wordDetailTotalLevel7;
    }

    public void setWordDetailTotalLevel7(Integer wordDetailTotalLevel7) {
        this.wordDetailTotalLevel7 = wordDetailTotalLevel7;
    }

    public Integer getWordDetailTotalLevel8() {
        return wordDetailTotalLevel8;
    }

    public void setWordDetailTotalLevel8(Integer wordDetailTotalLevel8) {
        this.wordDetailTotalLevel8 = wordDetailTotalLevel8;
    }

    public Integer getWordDetailTotalLevel9() {
        return wordDetailTotalLevel9;
    }

    public void setWordDetailTotalLevel9(Integer wordDetailTotalLevel9) {
        this.wordDetailTotalLevel9 = wordDetailTotalLevel9;
    }

    public Integer getWordDetailTotalLevel10() {
        return wordDetailTotalLevel10;
    }

    public void setWordDetailTotalLevel10(Integer wordDetailTotalLevel10) {
        this.wordDetailTotalLevel10 = wordDetailTotalLevel10;
    }
}
