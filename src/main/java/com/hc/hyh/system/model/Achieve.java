package com.hc.hyh.system.model;

import com.baomidou.mybatisplus.annotations.TableName;
import lombok.Data;

/**
 * 成绩表
 */
@Data
@TableName("achievement")
public class Achieve {
    private String testno;
    private String idno;
    private String name;
    private String total_score;
    private String word_score;
    private String article_score;
}
