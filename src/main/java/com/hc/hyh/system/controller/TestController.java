package com.hc.hyh.system.controller;

import com.hc.hyh.common.BaseController;
import com.hc.hyh.common.JsonResult;
import com.hc.hyh.common.PageResult;
import com.hc.hyh.common.exception.ParameterException;
import com.hc.hyh.common.shiro.EndecryptUtil;
import com.hc.hyh.common.utils.BatchTask;
import com.hc.hyh.common.utils.Mutil;
import com.hc.hyh.common.utils.StringUtil;
import com.hc.hyh.system.model.*;
import com.hc.hyh.system.service.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.apache.commons.math3.stat.StatUtils.mean;

/**
 * 用户管理
 */
//@Controller
@RequestMapping("/system/test")
@Slf4j
public class TestController extends BaseController {

    @Autowired
    private DataService dataService;
    @Autowired
    private TestService testService;
    @Autowired
    private RoleService roleService;
    @Autowired
    private DataDetailService dataDetailService;
    @Autowired
    private TestDetailService testDetailService;
    @Autowired
    private StudentService studentService;

    private List<DataInfo> dataInfoList = new ArrayList<>();

    private List<TestInfo> testInfoList = new ArrayList<>();

    private  List<DataDetailInfo> dataDetailInfos = new ArrayList<>();

    private  List<StudentInfo> studentInfos = new ArrayList<>();

    private  List<TestDetailInfo> testDetailInfos = new ArrayList<>();

    private  String[] sum_total = new String[2];

    private  int num_total = 0;


    /**
     * 查询试卷统计信息
     */
    @RequiresPermissions("test:view")
    @RequestMapping
    public String view_test(Model model) {
        return "statistics/test_detail.html";
    }

    /**
     * 查询用户列表
     */
    @RequiresPermissions("data:view")
    @ResponseBody
    @RequestMapping("/list")
    public PageResult<TestDetailInfo> list(Integer page, Integer limit, String searchKey, String searchValue) {
        if (page == null) {
            page = 0;
            limit = 0;
        }
        if (StringUtil.isBlank(searchValue)) {
            searchKey = null;
        }
        return testDetailService.list(page, limit, true, searchKey, searchValue);
    }
}
