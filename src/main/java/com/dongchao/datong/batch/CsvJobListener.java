package com.dongchao.datong.batch;

import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;

/**
 * 自定义job监听器,实现JobExecutionListener接口，可以监听任务的开始和结束
 */
public class CsvJobListener implements JobExecutionListener {
    private long startTime ;

    @Override
    public void beforeJob(JobExecution jobExecution) {
        startTime = System.currentTimeMillis() ;
        System.out.println("批处理任务开始--------------->");
    }

    @Override
    public void afterJob(JobExecution jobExecution) {
        System.out.println("------------------------>批处理任务结束");
        System.out.println("耗时："+(System.currentTimeMillis()-startTime)+"ms");
    }
}
