package com.hc.hyh.system.model;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import lombok.Data;

/**
 * 试卷表
 */
@Data
@TableName("test_info_detail")
public class TestDetailInfo {

    private String testno;  // 卷号

    private String title;  // 题号

    private String correctAverage;  // 答对平均分

    private String errorAverage;  // 答错平均分

    private String correctPercentage;  // 答对百分比

    private String errorPercentage;  // 答错百分比

    private String percentageProductSquare;  // 百分比乘积开方

    private String totalStandardDeviation;  // 总分标准差

    private String pointBiserial;  // 点双列

    private String difficulty;  // 难度

    private String flag;  // 唯一标识

}
