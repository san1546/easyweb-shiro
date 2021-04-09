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
import com.hc.hyh.system.model.DataInfo;
import com.hc.hyh.system.service.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.beetl.ext.fn.Json;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.*;

import static org.apache.commons.math3.stat.StatUtils.mean;

/**
 * 用户管理
 */
//@Controller
@RequestMapping("/system/data")
@Slf4j
public class DataController extends BaseController {

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
    @RequiresPermissions("data:view")
    @RequestMapping
    public String datainfo(Model model) {
        return "statistics/data_info.html";
    }


    /**
     * 查询用户列表
     */
    @RequiresPermissions("data:view")
    @ResponseBody
    @RequestMapping("/list")
    public PageResult<DataInfo> list(Integer page, Integer limit, String searchKey, String searchValue) {
        if (page == null) {
            page = 0;
            limit = 0;
        }
        if (StringUtil.isBlank(searchValue)) {
            searchKey = null;
        }
        return dataService.list(page, limit, true, searchKey, searchValue);
    }


    /**
     * 打开文件上传弹窗
     */
    @RequestMapping("/editForm")
    public String view_upload(Model model) {
        return "statistics/upload_form.html";
    }


    /**
     * 打开分数统计弹窗
     */
    @RequestMapping("/sumForm")
    public String view_sum(Model model) {
        return "statistics/sum_form.html";
    }

    /**
     * 打开数据统计弹窗
     */
    @RequestMapping("/totalForm")
    public String view_total(Model model) {
        return "statistics/total_form.html";
    }

    /**
     * 打开数据上传弹窗
     */
    @RequestMapping("/uploadForm")
    public String view_uploadScore(Model model) {
        return "statistics/upload_score.html";
    }

    /**
     * 数据上传
     */
    @RequiresPermissions("data:upload")
    @ResponseBody
    @RequestMapping("/upload")
    public JsonResult score(@RequestParam("file") MultipartFile file){
        Map<String, String> resObj = new HashMap<>();
        String filename = file.getOriginalFilename();
        System.out.println("filename:"+filename);
        if(filename.contains("_test")){
            return upload_test(file,filename);
        }else if(filename.contains("_score")){
            return upload_score(file,filename);
        }else if(filename.contains("_student")){
            return upload_student(file,filename);
        }else{
            return JsonResult.error();
        }
    }

    /**
     * 数据上传
     */
    @RequiresPermissions("data:uploadScore")
    @ResponseBody
    @RequestMapping("/uploadScore")
    public JsonResult uploadScore(@RequestParam("file") MultipartFile file, @RequestParam("testnum") String testNum, @RequestParam("score") String score, @RequestParam("testno") String testno){
        Map<String, String> resObj = new HashMap<>();
        String filename = file.getOriginalFilename();
        System.out.println("filename:"+filename);
        if(!testNum.isEmpty()){
            upload_exam(file,filename,testNum, score, testno);
            return  JsonResult.ok();
        }else{
            return JsonResult.error("输入框不能为空！");
        }
//        if(testNum)
//        if(filename.contains("_test")){
//            return upload_test(file,filename);
//        }else if(filename.contains("_score")){
//            return upload_score(file,filename);
//        }else if(filename.contains("_student")){
//            return upload_student(file,filename);
//        }else{
//            return JsonResult.error();
//        }

    }

    /**
     * 模板下载
     */
    @RequiresPermissions("data:download")
    @ResponseBody
    @RequestMapping("/download")
    public void download(String filename, HttpServletResponse response, HttpServletRequest request){
        response.setContentType("application/force-download");

        String path = this.getClass().getClassLoader().getResource("/static/files/" + filename).getPath();
        File file = new File(path);
        String fileName = file.getName();
        String ext = fileName.substring(fileName.lastIndexOf(".") + 1);
        String agent = (String) request.getHeader("USER-AGENT"); //判断浏览器类型
        try {
            if (agent != null && agent.indexOf("Fireforx") != -1) {
                fileName = new String(fileName.getBytes("UTF-8"), "iso-8859-1");   //UTF-8编码，防止输出文件名乱码
            } else {
                fileName = URLEncoder.encode(fileName, "UTF-8");
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        BufferedInputStream bis = null;
        OutputStream os = null;
        response.reset();
        response.setCharacterEncoding("utf-8");
        if (ext.equals("docx")) {
            response.setContentType("application/msword"); // word格式
        } else if (ext.equals("pdf")) {
            response.setContentType("application/pdf"); // word格式
        } else if (ext.equals("xlsx")) {
            response.setContentType("application/msexcel"); // excel格式
        }
        response.setHeader("Content-Disposition", "attachment; filename=" + fileName);
        try {
            bis = new BufferedInputStream(new FileInputStream(file));
            byte[] b = new byte[bis.available() + 1000];
            int i = 0;
            os = response.getOutputStream();   //直接下载导出
            while ((i = bis.read(b)) != -1) {
                os.write(b, 0, i);
            }
            os.flush();
            os.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (os != null) {
                try {
                    os.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
//        return JsonResult.ok();
    }

    /**
     * 上传成绩表数据
     */
    public JsonResult upload_exam(MultipartFile file,String filename,String testNum,String score, String testno){
        testno = "20200607";
        score = "1.0";
        String partScore = String.valueOf(Double.parseDouble(score) * Double.parseDouble(testNum));
        long time1=System.currentTimeMillis();
        if (!file.isEmpty()) {
            try{
                boolean isExcel2003 = true;
                String str = "";
                if (filename.matches("^.+\\.(?i)(xlsx)$")) {
                    isExcel2003 = false;
                }
                InputStream is = file.getInputStream();
                Workbook wb = null;
                if (isExcel2003) {
                    wb = new HSSFWorkbook(is);
                } else {
                    wb = new XSSFWorkbook(is);
                }
                Sheet sheet = wb.getSheetAt(0);
                if (sheet != null) {
                    str = "文件上传成功";
                } else {
                    str = "上传失败sheet为空";
                    return JsonResult.error(str);
                }


                // 用一个数组去存放题目数
                List list = new ArrayList();
                String temp = "1";
                int part = 0;

                for (int r = 0; r <= sheet.getLastRowNum(); r++) {
                    Row row = sheet.getRow(r);
                    Row row1 = sheet.getRow(1);
                    Row row2 = sheet.getRow(0);
                    DecimalFormat df = new DecimalFormat("0");
                    if (row == null) {
                        continue;
                    }

                    if(r==0){
                        int total = 0;
                        for(int i = 3 ;i < row.getLastCellNum(); i++){
                            String cell = String.valueOf(row.getCell(i));
                            int col = (int)Double.parseDouble(cell);
                            if(col % Integer.parseInt(testNum) == 0 ){
                                list.add(total);
                                total = 1;
                                part = part + 1;
                            }else {
                                total++;
                            }
                        }
                    }else {
//                        System.out.println("part:" + part);
                        StringBuffer answer = new StringBuffer("");

                        int i = 3; //真实列个数
                        int j = 0; //第几部分
                        double total = 0; //该部分总分
                        double sum = 0; //全卷总分
                        //从excel第二行开始获取每个单元格的数据
                        String row_cell[][] = new String[sheet.getLastRowNum() + 1][row.getLastCellNum()];
                        for (int c = 0; c <= sheet.getLastRowNum(); c++) {
                            for (int z = 0; z < sheet.getRow(c).getLastCellNum(); z++) {
                                switch (row.getCell(z).getCellType()) {
                                    case NUMERIC: {
                                        row_cell[c][z] = String.valueOf(row.getCell(z).getNumericCellValue());
                                        break;
                                    }

                                    case STRING: {
                                        row_cell[c][z] = row.getCell(z).getStringCellValue();
                                        break;
                                    }

                                    case BOOLEAN: {
                                        break;
                                    }
                                    case FORMULA: {
                                        break;
                                    }

                                    default:
                                        break;
                                }
                            }
                        }
                        for (; i <= row.getLastCellNum(); i++) {
//                            if(circle < Integer.parseInt(list.get(j).toString())){
//                            System.out.println("列数:" + row.getLastCellNum());
                            String cell = "";
//                            System.out.println("成绩:" + cell.matches("[\\d]+\\.[\\d]+"));
                            if(row.getCell(i) == null || !row.getCell(i).toString().matches("[\\d]+\\.[\\d]+")){
                                //处理空白单元格
                                cell = null;
//                                System.out.println("cell:"+cell);
                                break;
                            }else{
                                cell = row.getCell(i).toString();
//                                System.out.println("cell:"+cell);
                            }
//                            }else{
                            String name = "";
                            Date date = new Date();
                            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss ");
                            String time = simpleDateFormat.format(date);
                            /**
                              * 填写DataDetailInfo
                              */
                            DataDetailInfo dataDetailInfo = new DataDetailInfo();
                            dataDetailInfo.setFlag(row_cell[r][2]+"_"+String.valueOf(j+1)+"_"+(i-2));
                            dataDetailInfo.setTitle(String.valueOf(i-2));
                            dataDetailInfo.setStudentName(row_cell[r][1]);
                            dataDetailInfo.setTestno(testno);

                            dataDetailInfo.setScore(cell);
                            dataDetailInfo.setPart(String.valueOf(j+1));
                            dataDetailInfo.setCreateTime(time);
                            dataDetailInfo.setUpdateTime(time);
                            dataDetailInfos.add(dataDetailInfo);
                            int part_num = Integer.parseInt(testNum);
                            total = Double.parseDouble(cell) + total;
                            /**
                             * 填写TestInfo，DataInfo
                             */
                            if((i-2) % part_num == 0 ) {

                                DataInfo dataInfo = new DataInfo();
                                dataInfo.setFlag(row_cell[r][2]+"_"+String.valueOf(j+1));
                                dataInfo.setPart(String.valueOf(j+1));
                                dataInfo.setPartNum(testNum);
                                dataInfo.setPartScore(String.valueOf(total));
                                dataInfo.setTestno(testno);
                                dataInfo.setStudentName(row_cell[r][1]);
                                dataInfoList.add(dataInfo);

                                TestInfo testInfo = new TestInfo();
                                testInfo.setFlag(row_cell[1][2] + "_" + df.format(row2.getCell(i).getNumericCellValue()));
                                testInfo.setPart(String.valueOf(j + 1));
                                testInfo.setPartNum(testNum);
                                testInfo.setPartScore(partScore);
                                testInfo.setTestno(testno);
                                testInfoList.add(testInfo);

                                sum = sum + total;
                                total = 0;
                                j++;
                            }


                        }
                        if(i == row.getLastCellNum()){
                            sum = sum + total;
                            StudentInfo studentInfo = new StudentInfo();
                            studentInfo.setFlag(row_cell[r][2]+"_"+String.valueOf(j+1));
                            studentInfo.setStudentName(row_cell[r][1]);
                            studentInfo.setStudentNo(row_cell[r][2]);
                            studentInfo.setTestno(testno);
                            studentInfo.setIdno(row_cell[r][2]);
                            studentInfo.setGrade(String.valueOf(sum));
                            studentInfos.add(studentInfo);
                            total = 0;
                        }
                    }


                }
                if(dataDetailInfos.size()>0){
                    try {
                        BatchTask.batchDeal(dataDetailInfos,20,"dataDetailInfo_insert");
                        dataDetailInfos = new ArrayList<>();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                if(testInfoList.size()>0){
                    try {
                        BatchTask.batchDeal(testInfoList,2,"testInfo_insert");
                        testInfoList = new ArrayList<>();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                if(dataInfoList.size()>0){
                    try {
                        BatchTask.batchDeal(dataInfoList,10,"dataInfo_insert");
                        dataInfoList = new ArrayList<>();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                if(studentInfos.size()>0){
                    try {
                        BatchTask.batchDeal(studentInfos,10,"studentInfo_insert");
                        studentInfos = new ArrayList<>();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }else{
                    System.out.println("no");
                }
            } catch (IOException e) {
                log.info("成绩表的导入数据保存出错");
                return JsonResult.error();
            }
            long time2=System.currentTimeMillis();
            System.out.println("时间:"+(time2 - time1));
            return JsonResult.ok();
        } else {
            return JsonResult.error();
        }
    }

    /**
     * 上传学生成绩表数据
     */
    public JsonResult upload_score(MultipartFile file,String filename){
        long time1=System.currentTimeMillis();
        if (!file.isEmpty()) {
            try{
                boolean isExcel2003 = true;
                String str = "";
                if (filename.matches("^.+\\.(?i)(xlsx)$")) {
                    isExcel2003 = false;
                }
                InputStream is = file.getInputStream();
                Workbook wb = null;
                if (isExcel2003) {
                    wb = new HSSFWorkbook(is);
                } else {
                    wb = new XSSFWorkbook(is);
                }
                Sheet sheet = wb.getSheetAt(0);
                if (sheet != null) {
                    str = "文件上传成功";
                } else {
                    str = "上传失败sheet为空";
                    return JsonResult.error(str);
                }

                // 用一个数组去存放题目数
                List list = new ArrayList();
                String temp = "1";
                int part = 0;

                for (int r = 0; r <= sheet.getLastRowNum(); r++) {
                    Row row = sheet.getRow(r);
                    if (row == null) {
                        continue;
                    }

                    if(r==0){
                        int total = 0;
                        for(int i = 4 ;i < row.getLastCellNum(); i++){
                            String cell = String.valueOf(row.getCell(i)).substring(1, 2);
                            if(!temp.equals(cell)){
                                list.add(total);
                                total = 1;
                                temp = cell;
                                part = part + 1;
                            }else {
//                          temp = cell;
                                total++;
                            }
                            if(i==row.getLastCellNum()-1){
                                list.add(total);
                                part = part + 1;
                            }
                        }
                    }else {

                        StringBuffer answer = new StringBuffer("");

                        int i = 4; //真实列个数
                        int j = 0; //第几部分
                        int circle = 0; //循环第几次

                        //从excel第二行开始获取每个单元格的数据
                        for (; i < (Integer.parseInt(list.get(j).toString()) * part + 4); i++) {
                            if(circle < Integer.parseInt(list.get(j).toString())){
                                Cell cell = row.getCell(i);
                                if(cell==null || !cell.toString().matches("[a-zA-Z]+")){
                                    //处理空白单元格
                                    answer.append("null;");
                                }else{
                                    answer.append(cell+";");
                                }
                            }else{
                                DataInfo dataInfo = new DataInfo();
                                dataInfo.setFlag(row.getCell(2).getStringCellValue()+"_"+String.valueOf(j+1));
                                dataInfo.setStudentName(row.getCell(1).getStringCellValue());
                                dataInfo.setTestno(row.getCell(2).getStringCellValue());
                                dataInfo.setAnswer(answer.toString());
                                dataInfo.setPart(String.valueOf(j+1));
                                dataInfo.setPartNum(list.get(j).toString());
                                dataInfoList.add(dataInfo);
                                System.out.println("第"+(j+1)+"部分答案:"+answer);
                                j++;
                                circle = 0;
                                answer = new StringBuffer("");
                                Cell cell = row.getCell(i);
                                if(cell==null || !cell.toString().matches("[a-zA-Z]+")){
                                    //处理空白单元格
                                    answer.append("null;");
                                }else{
                                    answer.append(cell+";");
                                }
                            }
                            circle++;
                        }
                        System.out.println("第"+(j+1)+"部分答案:"+answer);
                        DataInfo dataInfo = new DataInfo();
                        dataInfo.setFlag(row.getCell(2).getStringCellValue()+"_"+String.valueOf(j+1));
                        dataInfo.setStudentName(row.getCell(1).getStringCellValue());
                        dataInfo.setTestno(row.getCell(2).getStringCellValue());
                        dataInfo.setAnswer(answer.toString());
                        dataInfo.setPart(String.valueOf(j+1));
                        dataInfo.setPartNum(list.get(j).toString());
                        dataInfoList.add(dataInfo);
                    }

                }
                if(dataInfoList.size()>0){
                    try {
                        BatchTask.batchDeal(dataInfoList,10,"dataInfo_insert");
                        dataInfoList = new ArrayList<>();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
//                    dataService.insertBatch(dataInfoList);
//                    dataInfoList = new ArrayList<>();
                }
            } catch (IOException e) {
                log.info("学生成绩表的导入数据保存出错");
                return JsonResult.error();
            }
            long time2=System.currentTimeMillis();
            System.out.println("时间:"+(time2 - time1));
            return JsonResult.ok();
        } else {
            return JsonResult.error();
        }
    }

    /**
     * 上传标准试卷数据
     */
    public JsonResult upload_test(MultipartFile file,String filename){
        if (!file.isEmpty()) {
            try{
                boolean isExcel2003 = true;
                String str = "";
                if (filename.matches("^.+\\.(?i)(xlsx)$")) {
                    isExcel2003 = false;
                }
                InputStream is = file.getInputStream();
                Workbook wb = null;
                if (isExcel2003) {
                    wb = new HSSFWorkbook(is);
                } else {
                    wb = new XSSFWorkbook(is);
                }
                Sheet sheet = wb.getSheetAt(0);
                if (sheet != null) {
                    str = "文件上传成功";
                } else {
                    str = "上传失败sheet为空";
                    return JsonResult.error(str);
                }

                // 用一个数组去存放题目数
                List list = new ArrayList();

                // 用一个数组去存放该题目的判断要求
                List type = new ArrayList();

                String temp = "1";

                for (int r = 0; r <= sheet.getLastRowNum(); r++) {
                    Row row = sheet.getRow(r);
                    if (row == null) {
                        continue;
                    }

                    if(r==0){
                        int total = 0;
                        for(int i = 1 ;i < row.getLastCellNum(); i++){
                            String row_cell = String.valueOf(row.getCell(i));
                            String restr = "";
                            if(row_cell.contains("(")&&row_cell.contains(")")){
                                restr = row_cell.substring(row_cell.indexOf("("),row_cell.indexOf(")")+1);
                            }else{
                                restr = "(equals)";
                            }
                            type.add(restr);
                            String cell = row_cell.substring(1, 2);
                            if(!temp.equals(cell)){
                                list.add(total);
                                total = 1;
                                temp = cell;
                            }else {
//                          temp = cell;
                                total++;
                            }
                            if(i==row.getLastCellNum()-1){
                                list.add(total);
                            }
                        }
                    }else {

                        StringBuffer answer = new StringBuffer("");


                        int i = 1; //真实列个数
                        int j = 0; //第几部分
                        int circle = 0; //循环第几次

                        String partNum = String.valueOf(Integer.parseInt(list.get(j).toString()) - 1);
                        //从excel第二行开始获取每个单元格的数据
                        for (; i <row.getLastCellNum(); i++) {
                            if(circle < Integer.parseInt(list.get(j).toString())){
                                Cell cell = row.getCell(i);
                                if(cell==null||cell.toString().equals("无")){
                                    //处理空白单元格
                                    answer.append("null;");
                                }else{
                                    answer.append(cell+type.get(i-1).toString()+";");
                                }
                            }else{
                                String answer_new = answer.substring(0,answer.lastIndexOf(";",answer.lastIndexOf(";")-1));
                                String score = answer.substring(answer.lastIndexOf(";",answer.lastIndexOf(";")-1)+1);
                                TestInfo testInfo = new TestInfo();
                                testInfo.setFlag((int)row.getCell(0).getNumericCellValue()+"_"+String.valueOf(j+1));
                                testInfo.setTestno(row.getCell(0).getStringCellValue());
                                testInfo.setPartAnswer(answer_new);
                                testInfo.setPart(String.valueOf(j+1));
                                testInfo.setPartNum(partNum);
                                testInfo.setPartScore(score.substring(0,score.length()-1).replaceAll("\\(.*?\\)|\\{.*?}|\\[.*?]|（.*?）", ""));
                                testInfoList.add(testInfo);
                                System.out.println("第"+(j+1)+"部分答案:"+answer_new);
                                j++;
                                circle = 0;
                                answer = new StringBuffer("");
                                Cell cell = row.getCell(i);
                                if(cell==null||cell.toString().equals("无")){
                                    //处理空白单元格
                                    answer.append("null;");
                                }else{
                                    answer.append(cell+type.get(i-1).toString()+";");
                                }
                            }
                            circle++;
                        }
                        String answer_new = answer.substring(0,answer.lastIndexOf(";",answer.lastIndexOf(";")-1));
                        String score = answer.substring(answer.lastIndexOf(";",answer.lastIndexOf(";")-1)+1);
                        TestInfo testInfo = new TestInfo();
                        testInfo.setFlag((int)row.getCell(0).getNumericCellValue()+"_"+String.valueOf(j+1));
                        testInfo.setTestno(row.getCell(0).getStringCellValue());
                        testInfo.setPartAnswer(answer_new);
                        testInfo.setPart(String.valueOf(j+1));
                        testInfo.setPartNum(partNum);
                        testInfo.setPartScore(score.substring(0,score.length()-1).replaceAll("\\(.*?\\)|\\{.*?}|\\[.*?]|（.*?）", ""));
                        testInfoList.add(testInfo);
                        testService.insertBatch(testInfoList);
                        testInfoList = new ArrayList<>();

                    }

                }
            } catch (IOException e) {
                log.info("基本试卷表的导入数据保存出错");
                return JsonResult.error();
            }
            return JsonResult.ok();
        } else {
            return JsonResult.error();
        }
    }

    /**
     * 上传学生信息数据
     */
    public JsonResult upload_student(MultipartFile file,String filename){
        System.out.println("student");
        return JsonResult.ok();
//        if (!file.isEmpty()) {
//            try{
//                boolean isExcel2003 = true;
//                String str = "";
//                if (filename.matches("^.+\\.(?i)(xlsx)$")) {
//                    isExcel2003 = false;
//                }
//                InputStream is = file.getInputStream();
//                Workbook wb = null;
//                if (isExcel2003) {
//                    wb = new HSSFWorkbook(is);
//                } else {
//                    wb = new XSSFWorkbook(is);
//                }
//                Sheet sheet = wb.getSheetAt(0);
//                if (sheet != null) {
//                    str = "文件上传成功";
//                } else {
//                    str = "上传失败sheet为空";
//                    return JsonResult.error();
//                }
//
//                // 用一个数组去存放题目数
//                List list = new ArrayList();
//                String temp = "1";
//
//                for (int r = 0; r <= sheet.getLastRowNum(); r++) {
//                    Row row = sheet.getRow(r);
//                    if (row == null) {
//                        continue;
//                    }
//
//                    if(r==0){
//                        int total = 0;
//                        for(int i = 4 ;i < row.getLastCellNum(); i++){
//                            String cell = String.valueOf(row.getCell(i)).substring(1, 2);
//                            if(!temp.equals(cell)){
//                                list.add(total);
//                                total = 1;
//                                temp = cell;
//                            }else {
////                          temp = cell;
//                                total++;
//                            }
//                            if(i==row.getLastCellNum()-1){
//                                list.add(total);
//                            }
//                        }
//                    }else {
//
//                        StringBuffer answer = new StringBuffer("");
//
//
//                        int i = 4; //真实列个数
//                        int j = 0; //第几部分
//                        int circle = 0; //循环第几次
//
//                        //从excel第二行开始获取每个单元格的数据
//                        for (; i <row.getLastCellNum(); i++) {
//                            if(circle < Integer.parseInt(list.get(j).toString())){
//                                Cell cell = row.getCell(i);
//                                if(cell==null){
//                                    //处理空白单元格
////                          System.out.println("空白");
//                                    answer.append(";");
//                                }else{
//                                    answer.append(cell+";");
//                                }
//                            }else{
//                                DataInfo dataInfo = new DataInfo();
//                                dataInfo.setStudentName(row.getCell(1).getStringCellValue());
//                                dataInfo.setTestno((int)row.getCell(2).getNumericCellValue());
//                                dataInfo.setAnswer(answer.toString());
//                                dataInfo.setPart(String.valueOf(j+1));
//                                dataInfo.setPart_num(list.get(j).toString());
//                                dataService.add(dataInfo);
//                                System.out.println("第"+(j+1)+"部分答案:"+answer);
//                                j++;
//                                circle = 0;
//                                answer = new StringBuffer("");
//                                Cell cell = row.getCell(i);
//                                if(cell==null){
//                                    //处理空白单元格
////                          System.out.println("空白");
//                                    answer.append(";");
//                                }else{
//                                    answer.append(cell+";");
//                                }
//                            }
//                            circle++;
//                        }
//                        System.out.println("第"+(j+1)+"部分答案:"+answer);
//                        DataInfo dataInfo = new DataInfo();
//                        dataInfo.setStudentName(row.getCell(1).getStringCellValue());
//                        dataInfo.setTestno((int)row.getCell(2).getNumericCellValue());
//                        dataInfo.setAnswer(answer.toString());
//                        dataInfo.setPart(String.valueOf(j+1));
//                        dataInfo.setPart_num(list.get(j).toString());
//                        dataService.add(dataInfo);
//
//                    }
//
//                }
//            } catch (IOException e) {
//                log.info("考勤查看的导入数据保存出错");
//                return JsonResult.error();
//            }
//            return JsonResult.ok();
//        } else {
//            return JsonResult.error();
//        }
    }


    /**
     * 统计分数
     **/
    @RequiresPermissions("data:sum")
    @ResponseBody
    @RequestMapping("/sum")
    public JsonResult sum(String testno) {
        long time1=System.currentTimeMillis();
        List<DataInfo> dataInfoLists = dataService.list(testno);
        if(dataInfoLists.size()>0){
            int i = 0;
            boolean flag = false;
            for (DataInfo dataInfo : dataInfoLists) {
                if(i==dataInfoLists.size()-1){
                    flag = true;
//                    System.out.println("aa:"+dataInfo.getStudentName());
                }
                JsonResult jsonResult = sum_score(dataInfo,flag);
                i++;
                if (!jsonResult.get("code").toString().equals("200")) {
                    return JsonResult.error("标准题库中没有这个试卷号");
                }
            }
            try {
                BatchTask.batchDeal(dataInfoList,10,"dataInfo_insert");
                BatchTask.batchDeal(dataDetailInfos,20,"dataDetailInfo_insert");
                BatchTask.batchDeal(studentInfos,10,"studentInfo_insert");
                dataInfoList = new ArrayList<>();
                dataDetailInfos = new ArrayList<>();
                studentInfos = new ArrayList<>();
                sum_total = new String[2];
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            long time2=System.currentTimeMillis();
            System.out.println("时间:"+(time2 - time1));
            return JsonResult.ok("操作成功");

        }else {
            return JsonResult.error("学生题库中没有这个试卷号");
        }
    }

    /**
     * 统计数据
     **/
    @RequiresPermissions("data:total")
    @ResponseBody
    @RequestMapping("/total")
    public JsonResult total(String testno,String correctPercentage,String errorPercentage,String percentageProductSquare,String totalStandardDeviation,String pointBiserial,String difficulty) {
        System.out.println("testno:"+testno);
        List<TestInfo> testInfos = testService.getList(testno);
        DecimalFormat df = new DecimalFormat("0.0");

        //获取格式化对象(double,小数点保留两位)
        for (TestInfo testInfo:testInfos) {
            int i = 0;
            for( ; i < Integer.parseInt(testInfo.getPartNum());i++){
                int title = i + 1;
                String score = String.valueOf(df.format(Double.parseDouble(testInfo.getPartScore())/Double.parseDouble(testInfo.getPartNum())));
//                System.out.println("score:" + score);
                String part = String.valueOf((Integer.parseInt(testInfo.getPart()) - 1 ) * Integer.parseInt(testInfo.getPartNum())  + title);
                TestDetailInfo testDetailInfo = new TestDetailInfo();
                //正确的平均分
                testDetailInfo = totalAverage(testno, testInfo.getPart(),score,part, "correct",testDetailInfo);
                //错误的平均分
                testDetailInfo = totalAverage(testno,testInfo.getPart(),"0.0",part,"error",testDetailInfo);
//                System.out.println("平均分" + testDetailInfo.getPercentageProductSquare());
                //总分标准差
                testDetailInfo = standardDeviation(testno,testDetailInfo);
                //信度
                testDetailInfo = squareDifferent(testno,testDetailInfo);
                //点双列(区分度)
                testDetailInfo = pointBiserial(testDetailInfo);
                testDetailInfos.add(testDetailInfo);
//                System.out.println("正确平均分:"+testDetailInfo.getCorrectAverage());
//                System.out.println("错误平均分:"+testDetailInfo.getErrorAverage());
//                System.out.println("正确百分数:"+testDetailInfo.getCorrectPercentage());
//                System.out.println("错误百分数:"+testDetailInfo.getErrorPercentage());
//                System.out.println("百分比乘积开方:"+testDetailInfo.getPercentageProductSquare());
//                System.out.println("总分标准差:"+testDetailInfo.getTotalStandardDeviation());
//                System.out.println("点双列:"+testDetailInfo.getPointBiserial());
            }
        }

        if(testDetailInfos.size()>0) {
            try {
                BatchTask.batchDeal(testDetailInfos, 2, "testDetailInfo_insert");
                testDetailInfos = new ArrayList<>();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
//        System.out.println("correctPercentage:"+correctPercentage);
//        System.out.println("errorPercentage:"+errorPercentage);
//        System.out.println("percentageProductSquare:"+percentageProductSquare);
//        System.out.println("totalStandardDeviation:"+totalStandardDeviation);
//        System.out.println("pointBiserial:"+pointBiserial);
//        System.out.println("difficulty:"+difficulty);
        return JsonResult.ok();
    }

    /**
     * 统计平均数、百分数
     */
    public TestDetailInfo totalAverage(String testno,String part,String score,String title,String status,TestDetailInfo testDetailInfo){
//        System.out.println("part-title:" + part + "-" + title);
        //  找出正确/错误的学生数据，并计算人数
        dataDetailInfos = dataDetailService.findList(testno,part,score,title);
        int num = dataDetailService.findCount(testno,part,score,title);
        double total = 0;
        for (DataDetailInfo dataDetailInfo:dataDetailInfos) {
            StudentInfo studentInfo = studentService.getName(dataDetailInfo.getStudentName(),testno);
//            System.out.println("姓名:" + dataDetailInfo.getStudentName());
            total = total + Double.parseDouble(studentInfo.getGrade());
        }
        num_total = studentService.findCount(testno);
//        System.out.println("num_total:"+num_total);
        double average = 0;
        if(total != 0 && num != 0){
            average = (double)total / (double)num;
        }

        //获取格式化对象(double,小数点保留两位)
        DecimalFormat df = new DecimalFormat("0.00");

        if(status.equals("correct")){
            System.out.println("正确学生人数：" + num);
            testDetailInfo.setCorrectAverage(String.valueOf(df.format(average)));
            if(num_total!=0) {
                double percentage = (double)num / (double)num_total;
                double square = Math.sqrt(average*percentage);
                testDetailInfo.setCorrectPercentage(String.valueOf(percentage));
                testDetailInfo.setPercentageProductSquare(String.valueOf(df.format(square)));
            }
        }else{
            System.out.println("错误学生人数：" + num);
            testDetailInfo.setErrorAverage(String.valueOf(df.format(average)));
            if(num_total!=0) {
                double percentage = (double)num / (double)num_total;
                double square = Math.sqrt(average*percentage);
                testDetailInfo.setErrorPercentage(String.valueOf(percentage));
                testDetailInfo.setPercentageProductSquare(String.valueOf(df.format(square)));
            }

        }
        if(testDetailInfo.getCorrectPercentage()!=null&&testDetailInfo.getErrorPercentage()!=null){
          testDetailInfo = square(testDetailInfo.getCorrectPercentage(),testDetailInfo.getErrorPercentage(),testDetailInfo);
        }
        testDetailInfo.setTitle(part+"_"+String.format("%02d",Integer.parseInt(title)));
        testDetailInfo.setFlag(testno+"_"+part+"_"+String.format("%02d",Integer.parseInt(title)));
        testDetailInfo.setTestno(testno);
        return testDetailInfo;
    }


    /**
     * 统计百分比乘积开方
     */
    public TestDetailInfo square(String correct,String error,TestDetailInfo testDetailInfo){
        //获取格式化对象(double,小数点保留两位)
        DecimalFormat df = new DecimalFormat("0.00");//“#”可以理解为在正常的数字显示中，如果前缀与后缀出现不必要的多余的0，则将其忽略
        //获取格式化对象(百分数,小数点保留两位)
        NumberFormat nt = NumberFormat.getPercentInstance();
        //设置百分数精确度2即保留两位小数
        nt.setMinimumFractionDigits(2);
        double double_correct = Double.parseDouble(correct);
        double double_error = Double.parseDouble(error);
        System.out.println("double_correct" + double_correct);
        System.out.println("double_error" + double_error);
        double square = Math.sqrt(double_correct*double_error);
//        System.out.println("square" + square);
        testDetailInfo.setPercentageProductSquare(String.valueOf(df.format(square)));
        testDetailInfo.setCorrectPercentage(String.valueOf(nt.format(double_correct)));
        testDetailInfo.setErrorPercentage(String.valueOf(nt.format(double_error)));
        testDetailInfo.setDifficulty(String.valueOf(nt.format(double_correct)));
        return testDetailInfo;
    }


    public TestDetailInfo squareDifferent(String testno,TestDetailInfo testDetailInfo){
        return testDetailInfo;
    }

        /**
         * 统计总分标准差
         */
    public TestDetailInfo standardDeviation(String testno,TestDetailInfo testDetailInfo){
        List<StudentInfo> studentList = studentService.getTestNo(testno);
        double[] list = new double[studentList.size()];
        int i = 0;
        for (StudentInfo studentInfo: studentList) {
            list[i] = Double.parseDouble(studentInfo.getGrade());
            i++;
        }
        //获取格式化对象(double,小数点保留两位)
        DecimalFormat df = new DecimalFormat("0.00");//“#”可以理解为在正常的数字显示中，如果前缀与后缀出现不必要的多余的0，则将其忽略
//        for (int i = 0; i < list.length; i++) {
//            System.out.println("double["+i+"]:"+list);
//        }
        double deviation = standardDeviation(list);
        testDetailInfo.setTotalStandardDeviation(String.valueOf(df.format(deviation)));
        return testDetailInfo;
    }

    /**
     * 统计点双列(区分度)
     */
    public TestDetailInfo pointBiserial(TestDetailInfo testDetailInfo){
        //获取格式化对象(double,小数点保留两位)
        DecimalFormat df = new DecimalFormat("0.00");//“#”可以理解为在正常的数字显示中，如果前缀与后缀出现不必要的多余的0，则将其忽略
//        System.out.println("correctAverage:"+testDetailInfo.getCorrectAverage());
        double correctAverage = Double.parseDouble(testDetailInfo.getCorrectAverage());
        double errorAverage = Double.parseDouble(testDetailInfo.getErrorAverage());
        double percentageProductSquare = Double.parseDouble(testDetailInfo.getPercentageProductSquare());
        double totalStandardDeviation = Double.parseDouble(testDetailInfo.getTotalStandardDeviation());
        double pointBiserial = (correctAverage-errorAverage)*percentageProductSquare/totalStandardDeviation;
        testDetailInfo.setPointBiserial(String.valueOf(df.format(pointBiserial)));
        return testDetailInfo;
    }
    /**
     *
     *  * @描述:变异性量数：方差 <br/>
     *  * @方法名: variance <br/>
     *  * @param in <br/>
     *  * @return <br/>
     *  * @返回类型 double <br/>
     *  * @创建人 micheal <br/>
     *  * @创建时间 2019年1月2日下午10:47:27 <br/>
     *  * @修改人 micheal <br/>
     *  * @修改时间 2019年1月2日下午10:47:27 <br/>
     *  * @修改备注 <br/>
     *  * @since <br/>
     *  * @throws <br/>
     *  
     */
    public static double variance(double[] in) {
        double t_mean = mean(in);
        double t_sumPerPow = 0;
        for (int i = 0; i < in.length; i++) {
            t_sumPerPow = Mutil.add(t_sumPerPow, Math.pow(Mutil.subtract(in[i], t_mean), 2));
        }
        return Mutil.divide(t_sumPerPow, (in.length - 1), 2);
    }


    /**
     *
     *  * @描述:变异性量数：标准差（无偏估计） <br/>
     *  * @方法名: standardDeviation <br/>
     *  * @param in <br/>
     *  * @return <br/>
     *  * @返回类型 double <br/>
     *  * @创建人 micheal <br/>
     *  * @创建时间 2019年1月2日下午10:32:07 <br/>
     *  * @修改人 micheal <br/>
     *  * @修改时间 2019年1月2日下午10:32:07 <br/>
     *  * @修改备注 <br/>
     *  * @since <br/>
     *  * @throws <br/>
     *  
     */
    public static double standardDeviation(double[] in) {
        return Math.sqrt(variance(in));
    }

    /**
     *
     *  * @描述:变异性量数：标准差（有偏估计） <br/>
     *  * @方法名: SD <br/>
     *  * @param in <br/>
     *  * @return <br/>
     *  * @返回类型 double <br/>
     *  * @创建人 micheal <br/>
     *  * @创建时间 2019年1月2日下午10:32:07 <br/>
     *  * @修改人 micheal <br/>
     *  * @修改时间 2019年1月2日下午10:32:07 <br/>
     *  * @修改备注 <br/>
     *  * @since <br/>
     *  * @throws <br/>
     *  
     */
    public static double standardDeviation2(double[] in) {
        double t_mean = mean(in);
        double t_sumPerPow = 0;
        for (int i = 0; i < in.length; i++) {
            t_sumPerPow = Mutil.add(t_sumPerPow, Math.pow(Mutil.subtract(in[i], t_mean), 2));
        }
        return Math.sqrt(Mutil.divide(t_sumPerPow, (in.length), 2));


    }




    /**
     * 统计试卷每部分成绩
     */
    public JsonResult sum_score(DataInfo dataInfo,boolean flag){
        TestInfo testInfo = testService.getTestNo(dataInfo.getTestno(),dataInfo.getPart());
        if(testInfo!=null){
            int score = new Double(Double.parseDouble(testInfo.getPartScore())/Double.parseDouble(testInfo.getPartNum())).intValue();
            int total = spilt_single(testInfo,dataInfo,score);
            System.out.println("name:"+dataInfo.getStudentName());
            System.out.println("total:"+total);
            dataInfo.setPartScore(String.valueOf(total));

            if(sum_total[0]!=null && sum_total[0].equals(dataInfo.getStudentName())){
                sum_total[1] = String.valueOf((Integer.parseInt(sum_total[1]) + total));
                if(flag){
                    System.out.println("学生姓名前:"+sum_total[0]);
                    System.out.println("总分前:"+ sum_total[1]);
                    if(sum_total[0]!=null){
                        StudentInfo studentInfo = new StudentInfo();
                        studentInfo.setFlag(dataInfo.getTestno()+"_"+sum_total[0]);
                        studentInfo.setStudentName(sum_total[0]);
                        studentInfo.setTestno(String.valueOf(dataInfo.getTestno()));
                        studentInfo.setGrade(sum_total[1]);
                        studentInfos.add(studentInfo);
                    }
                    sum_total[0] = dataInfo.getStudentName();
                    sum_total[1] = String.valueOf(total);
                    System.out.println("学生姓名后:"+ sum_total[0]);
                    System.out.println("总分后:"+ sum_total[1]);
                }
            }
            else{
                System.out.println("学生姓名前:"+sum_total[0]);
                System.out.println("总分前:"+ sum_total[1]);
                if(sum_total[0]!=null){
                    StudentInfo studentInfo = new StudentInfo();
                    studentInfo.setFlag(dataInfo.getTestno()+"_"+sum_total[0]);
                    studentInfo.setStudentName(sum_total[0]);
                    studentInfo.setTestno(String.valueOf(dataInfo.getTestno()));
                    studentInfo.setGrade(sum_total[1]);
                    studentInfos.add(studentInfo);
                }
                sum_total[0] = dataInfo.getStudentName();
                sum_total[1] = String.valueOf(total);
                System.out.println("学生姓名后:"+ sum_total[0]);
                System.out.println("总分后:"+ sum_total[1]);
            }
            dataInfoList.add(dataInfo);
            return JsonResult.ok();
        }else{
            return JsonResult.error();
        }
    }

    /**
     * 将答案分割，并统计每个人每小题得分，每部分得分，总分
     */
    public int spilt_single(TestInfo testInfo,DataInfo dataInfo, int score){
        String[] standard_answer = testInfo.getPartAnswer().split(";");
        String[] answer = dataInfo.getAnswer().split(";");
        int total = 0;
        for (int i = 0; i < answer.length; i++) {
            System.out.printf("standard_answer["+i+"]"+ standard_answer[i]);
            System.out.println("name:"+dataInfo.getStudentName());
            System.out.println("answer["+i+"]"+answer[i]);
//            System.out.println("i"+i);
            if(answer[i].equals("null")){
                DataDetailInfo dataDetailInfo = new DataDetailInfo();
                dataDetailInfo.setStudentName(dataInfo.getStudentName());
                dataDetailInfo.setPart(dataInfo.getPart());
                dataDetailInfo.setTestno(dataInfo.getTestno());
                dataDetailInfo.setScore("0");
                dataDetailInfo.setTitle(String.valueOf(i+1));
                dataDetailInfo.setFlag(dataInfo.getStudentName()+dataInfo.getPart()+String.valueOf(i+1));
                dataDetailInfos.add(dataDetailInfo);
            }else {
                if(standard_answer[i].contains("(contain)")){
                    standard_answer[i] = standard_answer[i].replaceAll("\\(.*?\\)|\\{.*?}|\\[.*?]|（.*?）", "");
                    DataDetailInfo dataDetailInfo = new DataDetailInfo();
                    dataDetailInfo.setStudentName(dataInfo.getStudentName());
                    dataDetailInfo.setPart(dataInfo.getPart());
                    dataDetailInfo.setTestno(dataInfo.getTestno());
                    dataDetailInfo.setTitle(String.valueOf(i+1));
                    dataDetailInfo.setFlag(dataInfo.getStudentName()+dataInfo.getPart()+String.valueOf(i+1));
                    if (answer[i].contains(standard_answer[i])) {
                        total += score;
                        dataDetailInfo.setScore(String.valueOf(score));
                    }else{
                        dataDetailInfo.setScore("0");
                    }
                    dataDetailInfos.add(dataDetailInfo);
                }else if(standard_answer[i].contains("(more)")){
                    standard_answer[i] = standard_answer[i].replaceAll("\\(.*?\\)|\\{.*?}|\\[.*?]|（.*?）", "");
                    String[] strings = standard_answer[i].split(",");
                    DataDetailInfo dataDetailInfo = new DataDetailInfo();
                    dataDetailInfo.setStudentName(dataInfo.getStudentName());
                    dataDetailInfo.setPart(dataInfo.getPart());
                    dataDetailInfo.setTestno(dataInfo.getTestno());
                    dataDetailInfo.setTitle(String.valueOf(i+1));
                    dataDetailInfo.setFlag(dataInfo.getStudentName()+dataInfo.getPart()+String.valueOf(i+1));
                    for (String string : strings) {
                        if (string.equals(answer[i])) {
                            total += score;
                            dataDetailInfo.setScore(String.valueOf(score));
                            //如果选项和其中一个答案相符，则跳出循环
                            break;
                        }else{
                            dataDetailInfo.setScore("0");
                        }
                    }
                    dataDetailInfos.add(dataDetailInfo);
                }else if(standard_answer[i].contains("equals")){
                    standard_answer[i] = standard_answer[i].replaceAll("\\(.*?\\)|\\{.*?}|\\[.*?]|（.*?）", "");
                    DataDetailInfo dataDetailInfo = new DataDetailInfo();
                    dataDetailInfo.setStudentName(dataInfo.getStudentName());
                    dataDetailInfo.setPart(dataInfo.getPart());
                    dataDetailInfo.setTestno(dataInfo.getTestno());
                    dataDetailInfo.setTitle(String.valueOf(i+1));
                    dataDetailInfo.setFlag(dataInfo.getStudentName()+dataInfo.getPart()+String.valueOf(i+1));
                    if (answer[i].equals(standard_answer[i])) {
                        total += score;
                        dataDetailInfo.setScore(String.valueOf(score));
                    }else{
                        dataDetailInfo.setScore("0");
                    }
                    dataDetailInfos.add(dataDetailInfo);
                }else{
                    DataDetailInfo dataDetailInfo = new DataDetailInfo();
                    dataDetailInfo.setStudentName(dataInfo.getStudentName());
                    dataDetailInfo.setPart(dataInfo.getPart());
                    dataDetailInfo.setTestno(dataInfo.getTestno());
                    dataDetailInfo.setScore("0");
                    dataDetailInfo.setTitle(String.valueOf(i+1));
                    dataDetailInfo.setFlag(dataInfo.getStudentName()+dataInfo.getPart()+String.valueOf(i+1));
                    dataDetailInfos.add(dataDetailInfo);
                }


//                if (answer[i].length() > 1) {
//                    /**
//                     * 多选中只要一个对了整个答案都是对的
//                     */
////                List answer_list = isSingle(answer[i]);
////                for (int j = 0; j < answer_list.size(); j++) {
////                    if(standard_answer[i].contains(answer_list.get(j).toString())){
////                        flag = true;
////                    }
////                }
////                if(flag){
////                    total += score;
////                }
//                    if (standard_answer[i].contains(",")) {
//                        String[] strings = standard_answer[i].split(",");
//                        for (String stirng : strings) {
//                            if (stirng.equals(answer[i])) {
//                                total += score;
//                                DataDetailInfo dataDetailInfo = new DataDetailInfo();
//                                dataDetailInfo.setStudentName(dataInfo.getStudentName());
//                                dataDetailInfo.setPart(dataInfo.getPart());
//                                dataDetailInfo.setTestno(dataInfo.getTestno());
//                                dataDetailInfo.setScore(String.valueOf(score));
//                                dataDetailInfo.setTitle(String.valueOf(i+1));
//                                dataDetailInfo.setFlag(dataInfo.getStudentName()+dataInfo.getPart()+String.valueOf(i+1));
//                                dataDetailInfos.add(dataDetailInfo);
//                            }else{
//                                DataDetailInfo dataDetailInfo = new DataDetailInfo();
//                                dataDetailInfo.setStudentName(dataInfo.getStudentName());
//                                dataDetailInfo.setPart(dataInfo.getPart());
//                                dataDetailInfo.setTestno(dataInfo.getTestno());
//                                dataDetailInfo.setTitle(String.valueOf(i+1));
//                                dataDetailInfo.setFlag(dataInfo.getStudentName()+dataInfo.getPart()+String.valueOf(i+1));
//                                dataDetailInfo.setScore("0");
//                                dataDetailInfos.add(dataDetailInfo);
//                            }
//                        }
//                    } else {
//                        if (standard_answer[i].equals(answer[i])) {
//                            total += score;
//                            DataDetailInfo dataDetailInfo = new DataDetailInfo();
//                            dataDetailInfo.setStudentName(dataInfo.getStudentName());
//                            dataDetailInfo.setPart(dataInfo.getPart());
//                            dataDetailInfo.setTestno(dataInfo.getTestno());
//                            dataDetailInfo.setScore(String.valueOf(score));
//                            dataDetailInfo.setTitle(String.valueOf(i+1));
//                            dataDetailInfo.setFlag(dataInfo.getStudentName()+dataInfo.getPart()+String.valueOf(i+1));
//                            dataDetailInfos.add(dataDetailInfo);
//                        }else{
//                            DataDetailInfo dataDetailInfo = new DataDetailInfo();
//                            dataDetailInfo.setStudentName(dataInfo.getStudentName());
//                            dataDetailInfo.setPart(dataInfo.getPart());
//                            dataDetailInfo.setTestno(dataInfo.getTestno());
//                            dataDetailInfo.setScore("0");
//                            dataDetailInfo.setTitle(String.valueOf(i+1));
//                            dataDetailInfo.setFlag(dataInfo.getStudentName()+dataInfo.getPart()+String.valueOf(i+1));
//                            dataDetailInfos.add(dataDetailInfo);
//                        }
//                    }
//                } else {
//                    if (answer[i].equals(standard_answer[i])) {
//                        total += score;
//                        DataDetailInfo dataDetailInfo = new DataDetailInfo();
//                        dataDetailInfo.setStudentName(dataInfo.getStudentName());
//                        dataDetailInfo.setPart(dataInfo.getPart());
//                        dataDetailInfo.setTestno(dataInfo.getTestno());
//                        dataDetailInfo.setScore(String.valueOf(score));
//                        dataDetailInfo.setTitle(String.valueOf(i+1));
//                        dataDetailInfo.setFlag(dataInfo.getStudentName()+dataInfo.getPart()+String.valueOf(i+1));
//                        dataDetailInfos.add(dataDetailInfo);
//                    }else{
//                        DataDetailInfo dataDetailInfo = new DataDetailInfo();
//                        dataDetailInfo.setStudentName(dataInfo.getStudentName());
//                        dataDetailInfo.setPart(dataInfo.getPart());
//                        dataDetailInfo.setTestno(dataInfo.getTestno());
//                        dataDetailInfo.setScore("0");
//                        dataDetailInfo.setTitle(String.valueOf(i+1));
//                        dataDetailInfo.setFlag(dataInfo.getStudentName()+dataInfo.getPart()+String.valueOf(i+1));
//                        dataDetailInfos.add(dataDetailInfo);
//                    }
//                }
            }
        }
        return total;
    }


    /**
     * 判断是否是单选
     */
    public List isSingle(String name){
        List<String> list = new ArrayList<String>(); //定义对象依次存放每一个字符
           for(int i = 0; i < name.length() ; i++){

               String ss = name.substring(i,i+1);

               list.add(ss);
           }
       return list;
    }

    private List<Role> getRoles(String roleStr) {
        List<Role> roles = new ArrayList<>();
        String[] split = roleStr.split(",");
        for (String t : split) {
            if (t.equals("1")) {
                throw new ParameterException("不能添加超级管理员");
            }
            roles.add(new Role(Integer.parseInt(t)));
        }
        return roles;
    }

    /**
     * 修改用户状态
     **/
    @RequiresPermissions("data:delete")
    @ResponseBody
    @RequestMapping("/updateState")
    public JsonResult updateState(Integer userId, Integer state) {
        if (dataService.updateState(userId, state)) {
            return JsonResult.ok();
        } else {
            return JsonResult.error();
        }
    }

    /**
     * 修改自己密码
     **/
    @ResponseBody
    @RequestMapping("/updatePsw")
    public JsonResult updatePsw(String oldPsw, String newPsw) {
        if ("admin".equals(getLoginUser().getUsername())) {
            return JsonResult.error("演示账号admin关闭该功能");
        }
        String finalSecret = EndecryptUtil.encrytMd5(oldPsw, getLoginUserName(), 3);
        if (!finalSecret.equals(getLoginUser().getPassword())) {
            return JsonResult.error("原密码输入不正确");
        }
        if (dataService.updatePsw(getLoginUserId(), getLoginUserName(), newPsw)) {
            return JsonResult.ok("修改成功");
        } else {
            return JsonResult.error("修改失败");
        }
    }


}
