package com.hc.hyh.system.model;

import com.baomidou.mybatisplus.annotations.*;

import javax.persistence.*;

/**
 * 词表
 */
@TableName("phraselevel")
public class PhraseInfo {

    @TableId
    private String id;  // ID
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


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPhraseDetailLevel1() {
        return phraseDetailLevel1;
    }

    public void setPhraseDetailLevel1(String phraseDetailLevel1) {
        this.phraseDetailLevel1 = phraseDetailLevel1;
    }

    public String getPhraseDetailLevel2() {
        return phraseDetailLevel2;
    }

    public void setPhraseDetailLevel2(String phraseDetailLevel2) {
        this.phraseDetailLevel2 = phraseDetailLevel2;
    }

    public String getPhraseDetailLevel3() {
        return phraseDetailLevel3;
    }

    public void setPhraseDetailLevel3(String phraseDetailLevel3) {
        this.phraseDetailLevel3 = phraseDetailLevel3;
    }

    public String getPhraseDetailLevel4() {
        return phraseDetailLevel4;
    }

    public void setPhraseDetailLevel4(String phraseDetailLevel4) {
        this.phraseDetailLevel4 = phraseDetailLevel4;
    }

    public String getPhraseDetailLevel5() {
        return phraseDetailLevel5;
    }

    public void setPhraseDetailLevel5(String phraseDetailLevel5) {
        this.phraseDetailLevel5 = phraseDetailLevel5;
    }

    public String getPhraseDetailLevel6() {
        return phraseDetailLevel6;
    }

    public void setPhraseDetailLevel6(String phraseDetailLevel6) {
        this.phraseDetailLevel6 = phraseDetailLevel6;
    }

    public String getPhraseDetailLevel7() {
        return phraseDetailLevel7;
    }

    public void setPhraseDetailLevel7(String phraseDetailLevel7) {
        this.phraseDetailLevel7 = phraseDetailLevel7;
    }

    public String getPhraseDetailLevel8() {
        return phraseDetailLevel8;
    }

    public void setPhraseDetailLevel8(String phraseDetailLevel8) {
        this.phraseDetailLevel8 = phraseDetailLevel8;
    }

    public String getPhraseDetailLevel9() {
        return phraseDetailLevel9;
    }

    public void setPhraseDetailLevel9(String phraseDetailLevel9) {
        this.phraseDetailLevel9 = phraseDetailLevel9;
    }

    public String getPhraseDetailLevel10() {
        return phraseDetailLevel10;
    }

    public void setPhraseDetailLevel10(String phraseDetailLevel10) {
        this.phraseDetailLevel10 = phraseDetailLevel10;
    }

    public Integer getPhraseDetailTotalLevel1() {
        return phraseDetailTotalLevel1;
    }

    public void setPhraseDetailTotalLevel1(Integer phraseDetailTotalLevel1) {
        this.phraseDetailTotalLevel1 = phraseDetailTotalLevel1;
    }

    public Integer getPhraseDetailTotalLevel2() {
        return phraseDetailTotalLevel2;
    }

    public void setPhraseDetailTotalLevel2(Integer phraseDetailTotalLevel2) {
        this.phraseDetailTotalLevel2 = phraseDetailTotalLevel2;
    }

    public Integer getPhraseDetailTotalLevel3() {
        return phraseDetailTotalLevel3;
    }

    public void setPhraseDetailTotalLevel3(Integer phraseDetailTotalLevel3) {
        this.phraseDetailTotalLevel3 = phraseDetailTotalLevel3;
    }

    public Integer getPhraseDetailTotalLevel4() {
        return phraseDetailTotalLevel4;
    }

    public void setPhraseDetailTotalLevel4(Integer phraseDetailTotalLevel4) {
        this.phraseDetailTotalLevel4 = phraseDetailTotalLevel4;
    }

    public Integer getPhraseDetailTotalLevel5() {
        return phraseDetailTotalLevel5;
    }

    public void setPhraseDetailTotalLevel5(Integer phraseDetailTotalLevel5) {
        this.phraseDetailTotalLevel5 = phraseDetailTotalLevel5;
    }

    public Integer getPhraseDetailTotalLevel6() {
        return phraseDetailTotalLevel6;
    }

    public void setPhraseDetailTotalLevel6(Integer phraseDetailTotalLevel6) {
        this.phraseDetailTotalLevel6 = phraseDetailTotalLevel6;
    }

    public Integer getPhraseDetailTotalLevel7() {
        return phraseDetailTotalLevel7;
    }

    public void setPhraseDetailTotalLevel7(Integer phraseDetailTotalLevel7) {
        this.phraseDetailTotalLevel7 = phraseDetailTotalLevel7;
    }

    public Integer getPhraseDetailTotalLevel8() {
        return phraseDetailTotalLevel8;
    }

    public void setPhraseDetailTotalLevel8(Integer phraseDetailTotalLevel8) {
        this.phraseDetailTotalLevel8 = phraseDetailTotalLevel8;
    }

    public Integer getPhraseDetailTotalLevel9() {
        return phraseDetailTotalLevel9;
    }

    public void setPhraseDetailTotalLevel9(Integer phraseDetailTotalLevel9) {
        this.phraseDetailTotalLevel9 = phraseDetailTotalLevel9;
    }

    public Integer getPhraseDetailTotalLevel10() {
        return phraseDetailTotalLevel10;
    }

    public void setPhraseDetailTotalLevel10(Integer phraseDetailTotalLevel10) {
        this.phraseDetailTotalLevel10 = phraseDetailTotalLevel10;
    }
}
