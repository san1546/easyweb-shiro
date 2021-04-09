package com.hc.hyh.system.model;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import lombok.Data;

/**
 * 试卷表
 */
@Data
@TableName("test_info")
public class TestInfo {
    private String flag;

    private String testno;  // 卷号

    private String part;  // 卷面第几部分

    private String partNum;  // 每一部分的题目数

    private String partAnswer;  // 该部分的答案

    private String partScore;  // 该部分的成绩

}
