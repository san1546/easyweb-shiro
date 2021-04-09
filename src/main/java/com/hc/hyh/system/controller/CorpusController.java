package com.hc.hyh.system.controller;

import com.hc.hyh.common.*;
import com.hc.hyh.common.exception.*;
import com.hc.hyh.common.shiro.*;
import com.hc.hyh.common.utils.*;
import com.hc.hyh.system.model.*;
import com.hc.hyh.system.service.*;
import lombok.extern.slf4j.*;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.formula.functions.*;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.*;
import org.apache.shiro.authz.annotation.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.stereotype.*;
import org.springframework.ui.*;
import org.springframework.util.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.*;

import java.io.*;
import java.text.*;
import java.util.*;

import static org.apache.commons.math3.stat.StatUtils.*;

/**
 * 语料管理
 */
@RequestMapping("/system/corpus")
@Slf4j
public class CorpusController extends BaseController {

    @Autowired
    private CorpusService corpusService;
    @Autowired
    private WordService wordService;
    @Autowired
    private PhraseService phraseService;

    private  List<CorpusInfo> corpusInfos = new ArrayList<>();

    private  List<WordInfo> wordInfos = new ArrayList<>();

    private  List<PhraseInfo> phraseInfos = new ArrayList<>();

    @RequiresPermissions("corpus:view")
    @RequestMapping
    public String corpusinfo(Model model) {
//        List<Role> roles = roleService.list(false);
//        model.addAttribute("roles", roles);
        return "corpus/corpus_info.html";
    }


    /**
     * 查询语料列表
     */
    @RequiresPermissions("corpus:view")
    @ResponseBody
    @RequestMapping("/list")
    public PageResult<CorpusInfo> list(Integer page, Integer limit, String searchKey, String searchValue) {
        if (page == null) {
            page = 0;
            limit = 0;
        }
        if (StringUtil.isBlank(searchValue)) {
            searchKey = null;
        }
        return corpusService.list(page, limit, true, searchKey, searchValue);
    }


    /**
     * 打开文件上传弹窗
     */
    @RequestMapping("/editForm")
    public String view_upload(Model model) {
        return "corpus/upload_form.html";
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
     * 数据上传
     */
    @RequiresPermissions("corpus:upload")
    @ResponseBody
    @RequestMapping("/upload")
    public JsonResult score(@RequestParam("file") MultipartFile file){
        Map<String, String> resObj = new HashMap<>();
        String filename = file.getOriginalFilename();
        System.out.println("filename:"+filename);
            return upload_corpus(file,filename);
    }


    /**
     * 上传语料库卷数据
     */
    public JsonResult upload_corpus(MultipartFile file,String filename){
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
                for (int i = 0; i < wb.getNumberOfSheets(); i++) {
                    Sheet sheet = wb.getSheetAt(i);
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

                    for (int r = 1; r <= sheet.getLastRowNum(); r++) {
                        Row row = sheet.getRow(r);
                        if (row == null) {
                            continue;
                        }
//                        for(int c = 0 ;c < row.getLastCellNum(); i++){
//                            if(row.getCell(c)==null){
//                                continue;
//                            }else{
//                                String cell = row.getCell(c).toString();
//                                System.out.println("第"+c+"列：" + cell);
//                            }
//                        }
                        CorpusInfo corpusInfo = new CorpusInfo();
                        System.out.println(sheet.getRow(0).getCell(0)+"：" + row.getCell(0));
                        System.out.println(sheet.getRow(0).getCell(6)+"：" + row.getCell(6));
                        System.out.println(sheet.getRow(0).getCell(18)+"：" + row.getCell(18));
                        String[] words = row.getCell(6).toString().split(" ");
                        String[] phrases = row.getCell(18).toString().split(" ");
                        WordInfo wordInfo = (SplitWord(words));
                        PhraseInfo phraseInfo  = SplitPhrase(phrases);
                        corpusInfo.setCorpusDetail(row.getCell(0).toString());
                        corpusInfo.setWordLevelId(wordInfo.getId());
                        corpusInfo.setPhraseLevelId(phraseInfo.getId());
                        corpusInfos.add(corpusInfo);
                        wordInfos.add(wordInfo);
                        phraseInfos.add(phraseInfo);
                    }
                    wordService.insertBatch(wordInfos);
                    wordInfos = new ArrayList<>();
                    phraseService.insertBatch(phraseInfos);
                    phraseInfos = new ArrayList<>();
                    corpusService.insertBatch(corpusInfos);
                    corpusInfos = new ArrayList<>();
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


    public WordInfo SplitWord(String[] cells){
        int sum_1 = 0;
        int sum_2 = 0;
        int sum_3 = 0;
        int sum_4 = 0;
        int sum_5 = 0;
        int sum_6 = 0;
        int sum_7 = 0;
        int sum_8 = 0;
        int sum_9 = 0;
        int sum_10 = 0;
        String corpus_temp_1 = "";
        String corpus_temp_2 = "";
        String corpus_temp_3 = "";
        String corpus_temp_4 = "";
        String corpus_temp_5 = "";
        String corpus_temp_6 = "";
        String corpus_temp_7 = "";
        String corpus_temp_8 = "";
        String corpus_temp_9 = "";
        String corpus_temp_10 = "";
        for (int j = 0; j < cells.length; j++) {
            if(cells[j].contains("/1")){
                sum_1 += 1;
                if(corpus_temp_1.equals("")){
                    corpus_temp_1 = cells[j].substring(0, cells[j].lastIndexOf("/"));
                }else {
                    corpus_temp_1 = corpus_temp_1 + ";" + cells[j].substring(0, cells[j].lastIndexOf("/"));
                }
            }else if (cells[j].contains("/2")){
                sum_2 += 1;
                if(corpus_temp_2.equals("")){
                    corpus_temp_2 = cells[j].substring(0, cells[j].lastIndexOf("/"));
                }else {
                    corpus_temp_2 = corpus_temp_2 + ";" + cells[j].substring(0, cells[j].lastIndexOf("/"));
                }
            }else if (cells[j].contains("/3")){
                sum_3 += 1;
                if(corpus_temp_3.equals("")){
                    corpus_temp_3 = cells[j].substring(0, cells[j].lastIndexOf("/"));
                }else {
                    corpus_temp_3 = corpus_temp_3 + ";" + cells[j].substring(0, cells[j].lastIndexOf("/"));
                }
            }else if (cells[j].contains("/4")){
                sum_4 += 1;
                if(corpus_temp_4.equals("")){
                    corpus_temp_4 = cells[j].substring(0, cells[j].lastIndexOf("/"));
                }else {
                    corpus_temp_4 = corpus_temp_4 + ";" + cells[j].substring(0, cells[j].lastIndexOf("/"));
                }
            }else if (cells[j].contains("/5")){
                sum_5 += 1;
                if(corpus_temp_5.equals("")){
                    corpus_temp_5 = cells[j].substring(0, cells[j].lastIndexOf("/"));
                }else {
                    corpus_temp_5 = corpus_temp_5 + ";" + cells[j].substring(0, cells[j].lastIndexOf("/"));
                }
            }else if (cells[j].contains("/6")){
                sum_6 += 1;
                if(corpus_temp_6.equals("")){
                    corpus_temp_6 = cells[j].substring(0, cells[j].lastIndexOf("/"));
                }else {
                    corpus_temp_6 = corpus_temp_6 + ";" + cells[j].substring(0, cells[j].lastIndexOf("/"));
                }
            }else if (cells[j].contains("/7")){
                sum_7 += 1;
                if(corpus_temp_7.equals("")){
                    corpus_temp_7 = cells[j].substring(0, cells[j].lastIndexOf("/"));
                }else {
                    corpus_temp_7 = corpus_temp_7 + ";" + cells[j].substring(0, cells[j].lastIndexOf("/"));
                }
            }else if (cells[j].contains("/8")){
                sum_8 += 1;
                if(corpus_temp_8.equals("")){
                    corpus_temp_8 = cells[j].substring(0, cells[j].lastIndexOf("/"));
                }else {
                    corpus_temp_8 = corpus_temp_8 + ";" + cells[j].substring(0, cells[j].lastIndexOf("/"));
                }
            }else if (cells[j].contains("/9")){
                sum_9 += 1;
                if(corpus_temp_9.equals("")){
                    corpus_temp_9 = cells[j].substring(0, cells[j].lastIndexOf("/"));
                }else {
                    corpus_temp_9 = corpus_temp_9 + ";" + cells[j].substring(0, cells[j].lastIndexOf("/"));
                }
            }else if (cells[j].contains("/10")){
                sum_10 += 1;
                if(corpus_temp_10.equals("")){
                    corpus_temp_10 = cells[j].substring(0, cells[j].lastIndexOf("/"));
                }else {
                    corpus_temp_10 = corpus_temp_10 + ";" + cells[j].substring(0, cells[j].lastIndexOf("/"));
                }
            }
        }
        WordInfo wordInfo = new WordInfo();
        wordInfo.setId(genUniqueKey());
        wordInfo.setWordDetailLevel1(corpus_temp_1);
        wordInfo.setWordDetailLevel2(corpus_temp_2);
        wordInfo.setWordDetailLevel3(corpus_temp_3);
        wordInfo.setWordDetailLevel4(corpus_temp_4);
        wordInfo.setWordDetailLevel5(corpus_temp_5);
        wordInfo.setWordDetailLevel6(corpus_temp_6);
        wordInfo.setWordDetailLevel7(corpus_temp_7);
        wordInfo.setWordDetailLevel8(corpus_temp_8);
        wordInfo.setWordDetailLevel9(corpus_temp_9);
        wordInfo.setWordDetailLevel10(corpus_temp_10);
        wordInfo.setWordDetailTotalLevel1(sum_1);
        wordInfo.setWordDetailTotalLevel2(sum_2);
        wordInfo.setWordDetailTotalLevel3(sum_3);
        wordInfo.setWordDetailTotalLevel4(sum_4);
        wordInfo.setWordDetailTotalLevel5(sum_5);
        wordInfo.setWordDetailTotalLevel6(sum_6);
        wordInfo.setWordDetailTotalLevel7(sum_7);
        wordInfo.setWordDetailTotalLevel8(sum_8);
        wordInfo.setWordDetailTotalLevel9(sum_9);
        wordInfo.setWordDetailTotalLevel10(sum_10);
        return wordInfo;
    }

    public PhraseInfo SplitPhrase(String[] cells){
        int sum_1 = 0;
        int sum_2 = 0;
        int sum_3 = 0;
        int sum_4 = 0;
        int sum_5 = 0;
        int sum_6 = 0;
        int sum_7 = 0;
        int sum_8 = 0;
        int sum_9 = 0;
        int sum_10 = 0;
        String corpus_temp_1 = "";
        String corpus_temp_2 = "";
        String corpus_temp_3 = "";
        String corpus_temp_4 = "";
        String corpus_temp_5 = "";
        String corpus_temp_6 = "";
        String corpus_temp_7 = "";
        String corpus_temp_8 = "";
        String corpus_temp_9 = "";
        String corpus_temp_10 = "";
        for (int j = 0; j < cells.length; j++) {
            if(cells[j].contains("/1")){
                sum_1 += 1;
                if(corpus_temp_1.equals("")){
                    corpus_temp_1 = cells[j].substring(0, cells[j].lastIndexOf("/"));
                }else {
                    corpus_temp_1 = corpus_temp_1 + ";" + cells[j].substring(0, cells[j].lastIndexOf("/"));
                }
            }else if (cells[j].contains("/2")){
                sum_2 += 1;
                if(corpus_temp_2.equals("")){
                    corpus_temp_2 = cells[j].substring(0, cells[j].lastIndexOf("/"));
                }else {
                    corpus_temp_2 = corpus_temp_2 + ";" + cells[j].substring(0, cells[j].lastIndexOf("/"));
                }
            }else if (cells[j].contains("/3")){
                sum_3 += 1;
                if(corpus_temp_3.equals("")){
                    corpus_temp_3 = cells[j].substring(0, cells[j].lastIndexOf("/"));
                }else {
                    corpus_temp_3 = corpus_temp_3 + ";" + cells[j].substring(0, cells[j].lastIndexOf("/"));
                }
            }else if (cells[j].contains("/4")){
                sum_4 += 1;
                if(corpus_temp_4.equals("")){
                    corpus_temp_4 = cells[j].substring(0, cells[j].lastIndexOf("/"));
                }else {
                    corpus_temp_4 = corpus_temp_4 + ";" + cells[j].substring(0, cells[j].lastIndexOf("/"));
                }
            }else if (cells[j].contains("/5")){
                sum_5 += 1;
                if(corpus_temp_5.equals("")){
                    corpus_temp_5 = cells[j].substring(0, cells[j].lastIndexOf("/"));
                }else {
                    corpus_temp_5 = corpus_temp_5 + ";" + cells[j].substring(0, cells[j].lastIndexOf("/"));
                }
            }else if (cells[j].contains("/6")){
                sum_6 += 1;
                if(corpus_temp_6.equals("")){
                    corpus_temp_6 = cells[j].substring(0, cells[j].lastIndexOf("/"));
                }else {
                    corpus_temp_6 = corpus_temp_6 + ";" + cells[j].substring(0, cells[j].lastIndexOf("/"));
                }
            }else if (cells[j].contains("/7")){
                sum_7 += 1;
                if(corpus_temp_7.equals("")){
                    corpus_temp_7 = cells[j].substring(0, cells[j].lastIndexOf("/"));
                }else {
                    corpus_temp_7 = corpus_temp_7 + ";" + cells[j].substring(0, cells[j].lastIndexOf("/"));
                }
            }else if (cells[j].contains("/8")){
                sum_8 += 1;
                if(corpus_temp_8.equals("")){
                    corpus_temp_8 = cells[j].substring(0, cells[j].lastIndexOf("/"));
                }else {
                    corpus_temp_8 = corpus_temp_8 + ";" + cells[j].substring(0, cells[j].lastIndexOf("/"));
                }
            }else if (cells[j].contains("/9")){
                sum_9 += 1;
                if(corpus_temp_9.equals("")){
                    corpus_temp_9 = cells[j].substring(0, cells[j].lastIndexOf("/"));
                }else {
                    corpus_temp_9 = corpus_temp_9 + ";" + cells[j].substring(0, cells[j].lastIndexOf("/"));
                }
            }else if (cells[j].contains("/10")){
                sum_10 += 1;
                if(corpus_temp_10.equals("")){
                    corpus_temp_10 = cells[j].substring(0, cells[j].lastIndexOf("/"));
                }else {
                    corpus_temp_10 = corpus_temp_10 + ";" + cells[j].substring(0, cells[j].lastIndexOf("/"));
                }
            }
        }
        PhraseInfo phraseInfo = new PhraseInfo();
        phraseInfo.setId(genUniqueKey());
        phraseInfo.setPhraseDetailLevel1(corpus_temp_1);
        phraseInfo.setPhraseDetailLevel2(corpus_temp_2);
        phraseInfo.setPhraseDetailLevel3(corpus_temp_3);
        phraseInfo.setPhraseDetailLevel4(corpus_temp_4);
        phraseInfo.setPhraseDetailLevel5(corpus_temp_5);
        phraseInfo.setPhraseDetailLevel6(corpus_temp_6);
        phraseInfo.setPhraseDetailLevel7(corpus_temp_7);
        phraseInfo.setPhraseDetailLevel8(corpus_temp_8);
        phraseInfo.setPhraseDetailLevel9(corpus_temp_9);
        phraseInfo.setPhraseDetailLevel10(corpus_temp_10);
        phraseInfo.setPhraseDetailTotalLevel1(sum_1);
        phraseInfo.setPhraseDetailTotalLevel2(sum_2);
        phraseInfo.setPhraseDetailTotalLevel3(sum_3);
        phraseInfo.setPhraseDetailTotalLevel4(sum_4);
        phraseInfo.setPhraseDetailTotalLevel5(sum_5);
        phraseInfo.setPhraseDetailTotalLevel6(sum_6);
        phraseInfo.setPhraseDetailTotalLevel7(sum_7);
        phraseInfo.setPhraseDetailTotalLevel8(sum_8);
        phraseInfo.setPhraseDetailTotalLevel9(sum_9);
        phraseInfo.setPhraseDetailTotalLevel10(sum_10);
        return phraseInfo;
    }

    /*
        生成唯一主键
        格式：时间+随机数
         */
    public static synchronized String genUniqueKey() {
        Random random = new Random();
        Integer number = random.nextInt(900000) + 100000;
        return System.currentTimeMillis() + String.valueOf(number);
    }


    public static void main(String[] args) {
        String a ="";
        if(a.equals("")){
            System.out.println("true");
        }else{
            System.out.println("flase");
        }
    }
}
