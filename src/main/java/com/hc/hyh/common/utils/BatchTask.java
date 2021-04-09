package com.hc.hyh.common.utils;


import com.hc.hyh.system.service.DataService;
import com.hc.hyh.system.service.TestDetailService;
import com.hc.hyh.system.service.impl.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Slf4j
/**
 * 批处理任务工具
 */
public class BatchTask implements Runnable {
    private List list;
    private CountDownLatch countDownLatch;
    private String flag;



    public BatchTask(List data, CountDownLatch countDownLatch,String flag) {
        this.list = data;
        this.countDownLatch = countDownLatch;
        this.flag = flag;
    }

    @Override
    public void run() {
        if (null != list) {
            // 业务逻辑，例如批量insert或者update
            if(flag.equals("dataInfo_insert")){
                DataServiceImpl dataService = new DataServiceImpl();
                dataService.insertBatch(list);
                log.info("现在操作的数据是{}", list);
            }
            else if(flag.equals("dataInfo_update")){
                DataServiceImpl dataService = new DataServiceImpl();
                dataService.updateBatch(list);
                log.info("现在操作的数据是{}", list);
            }
            else if(flag.equals("dataDetailInfo_insert")){
                DataDetailServiceImpl dataDetailService = new DataDetailServiceImpl();
                dataDetailService.insertBatch(list);
                log.info("现在操作的数据是{}", list);
            }
            else if(flag.equals("studentInfo_insert")){
                StudentServiceImpl studentService = new StudentServiceImpl();
                studentService.insertBatch(list);
                log.info("现在操作的数据是{}", list);
            }
            else if(flag.equals("testDetailInfo_insert")){
                TestDetailServiceImpl testDetailService = new TestDetailServiceImpl();
                testDetailService.insertBatch(list);
                log.info("现在操作的数据是{}", list);
            }
            else if(flag.equals("testInfo_insert")){
                TestServiceImpl testService = new TestServiceImpl();
                testService.insertBatch(list);
                log.info("现在操作的数据是{}", list);
            }
        }
        // 发出线程任务完成的信号
        countDownLatch.countDown();
    }

    /**
     * 通过多线程批处理插入数据，比之前插入数据快10倍
     * @param data
     * @param batchNum
     * @param flag
     * @throws InterruptedException
     */
    public static void batchDeal(List data, int batchNum,String flag) throws InterruptedException {
        int totalNum = data.size();
        int pageNum = totalNum % batchNum == 0 ? totalNum / batchNum : totalNum / batchNum + 1;
        ExecutorService executor = Executors.newFixedThreadPool(pageNum);
        try {
            CountDownLatch countDownLatch = new CountDownLatch(pageNum);
            List subData = null;
            int fromIndex, toIndex;
            for (int i = 0; i < pageNum; i++) {
                fromIndex = i * batchNum;
                toIndex = Math.min(totalNum, fromIndex + batchNum);
                subData = data.subList(fromIndex, toIndex);
//                System.out.println("subData:" + subData);
                BatchTask task = new BatchTask(subData, countDownLatch,flag);
                executor.execute(task);
            }
            // 主线程必须在启动其它线程后立即调用CountDownLatch.await()方法，
            // 这样主线程的操作就会在这个方法上阻塞，直到其它线程完成各自的任务。
            // 计数器的值等于0时，主线程就能通过await()方法恢复执行自己的任务。
            countDownLatch.await();
            log.info("数据操作完成!可以在此开始其它业务");
        } finally {
            // 关闭线程池，释放资源
            executor.shutdown();
        }
    }
}