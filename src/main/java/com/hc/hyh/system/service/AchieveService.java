package com.hc.hyh.system.service;


import com.hc.hyh.system.model.Achieve;

public interface AchieveService {


    Achieve getTest_Score(String testno, String name);

    Achieve getId_Score(String idno, String name);
}
