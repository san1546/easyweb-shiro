package com.hc.hyh.system.controller;

import com.hc.hyh.common.BaseController;
import com.hc.hyh.common.JsonResult;
import com.hc.hyh.system.model.Achieve;
import com.hc.hyh.system.service.AchieveService;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.hibernate.validator.internal.util.privilegedactions.GetResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;


/**
 * 成绩查询
 */
@Controller
@RequestMapping("/achievement")
@Slf4j
public class AchievementController extends BaseController {

    @Autowired
    public AchieveService achieveService;

    @RequestMapping("/view")
    public String achievementView(Model model) {
        return "achievement/achievement.html";
    }

    // 首页
    @RequestMapping("/index")
    public String indexView(Model model) {
        return "index/index.html";
    }


    // 资料下载
    @RequestMapping("/material")
    public String materialView(Model model) {
        return "index/material.html";
    }

    @RequestMapping("/list")
    @ResponseBody
    public JsonResult achievement(Achieve achieve) {
        if (achieve.getName() == null) {
            return JsonResult.error("姓名不能为空");
        }
        System.out.println("idno:" + achieve.getIdno());
        System.out.println("testno:" + achieve.getTestno());
//        if(!achieve.getIdno().equals("")){
//           String idno = achieve.getIdno();
//           String name = achieve.getName();
//            Achieve achieve1 = achieveService.getId_Score(idno,name);
//            if(achieve1!=null) {
//                return JsonResult.ok(200, "查询成功", achieve1);
//            }else{
//                return JsonResult.error("查询失败");
//            }
//        }else
        if (!achieve.getTestno().equals("")) {
            String testno = achieve.getTestno();
            String name = achieve.getName();
            Achieve achieve1 = achieveService.getTest_Score(testno, name);
            if (achieve1 != null) {
                return JsonResult.ok(200, "查询成功", achieve1);
            } else {
                return JsonResult.error("查询失败");
            }
        } else {
            return JsonResult.error("请填写身份证号（护照号）或者准考证号任意一项");
        }
    }

    @RequestMapping("/download")
    public void download(String testno, HttpServletResponse response, HttpServletRequest request) throws IOException {
        response.setContentType("application/force-download");
        String path = this.getClass().getClassLoader().getResource("/static//totalpdf/" + testno + ".pdf").getPath();
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
        if (ext == "docx") {
            response.setContentType("application/msword"); // word格式
        } else if (ext == "pdf") {
            response.setContentType("application/pdf"); // word格式
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

    }


}
