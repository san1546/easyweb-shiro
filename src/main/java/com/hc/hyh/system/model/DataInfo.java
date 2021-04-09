package com.hc.hyh.system.model;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * 成绩表
 */
@Data
@TableName("data_info")
public class DataInfo {

    private String flag;

    private String studentName; //学生姓名

    private String testno;  // 卷号

    private String part;  // 卷面第几部分

    private String partNum;  // 每一部分的题目数

    private String answer;  // 该部分的答案

    private String partScore;  // 该部分的成绩
}
