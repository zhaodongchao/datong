package com.dongchao.datong.batch;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 用于手动触发批处理任务
 */
@Controller
public class BatchController {
    private static Logger logger = LoggerFactory.getLogger(BatchController.class);
    @Autowired
    private JobLauncher jobLauncher;
    @Autowired
    private Job job ;

    public JobParameters jobParameters ;

    @RequestMapping("/import")
    @ResponseBody
    public String importData(String fileName){
        logger.info("---------------正在进行批处理任务----------------------");
        String path = fileName + ".csv" ;
        //自定义批处理执行时需要的相关参数，这里可以动态的将csv文件地址传过去
        jobParameters = new JobParametersBuilder().addLong("time",System.currentTimeMillis())
                                                  .addString("import.file.path",path)
                                                  .toJobParameters();

        try {
            jobLauncher.run(job,jobParameters); //手动执行批处理任务
            logger.info("--------------------批处理任务成功结束---------------------");
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("-------------------------------批处理任务执行失败----------------------------");
        }
        return "success batch import data!" ;
    }
}
