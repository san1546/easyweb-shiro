package com.hc.hyh.system.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Writer;

@Controller
public class RebotsController {
    /**
     * robots txt
     *
     * @return
     */
    @GetMapping("/robots.txt")
    public void robotsTxt(HttpServletResponse response) throws IOException {
        Writer writer = response.getWriter();
        String lineSeparator = System.getProperty("line.separator", "\n");
        writer.append("User-agent: *").append(lineSeparator);
        writer.append("Disallow:").append("/login/*").append(lineSeparator);
        writer.append("Disallow:").append("/index/*").append(lineSeparator);
        writer.append("Disallow:").append("/achievement/*").append(lineSeparator);
    }
}
