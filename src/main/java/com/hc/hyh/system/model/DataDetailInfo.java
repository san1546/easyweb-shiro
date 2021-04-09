package com.hc.hyh.system.model;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import lombok.Data;

import java.util.Date;

/**
 * 成绩表
 */
@Data
@TableName("data_info_detail")
public class DataDetailInfo {

    private String studentName; //姓名

    private String testno;  // 卷号

    private String part;  // 卷面第几部分

    private String score;  // 每一题成绩

    private String title;  // 题号

    private String flag;  // 唯一标识

    private String createTime; //创建时间

    private String updateTime; //更新时间

}
