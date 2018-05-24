package com.dongchao.datong.batch;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 用于手动触发批处理任务
 */
@Controller
public class BatchController {
    @Autowired
    private JobLauncher jobLauncher;
    @Autowired
    private Job job ;

    public JobParameters jobParameters ;

    @RequestMapping("/import")
    @ResponseBody
    public String importData(String fileName){
        String path = fileName + ".csv" ;
        //自定义批处理执行时需要的相关参数，这里可以动态的将csv文件地址传过去
        jobParameters = new JobParametersBuilder().addLong("time",System.currentTimeMillis())
                                                  .addString("import.file.path",path)
                                                  .toJobParameters();

        try {
            jobLauncher.run(job,jobParameters); //手动执行批处理任务
        } catch (JobExecutionAlreadyRunningException e) {
            e.printStackTrace();
        } catch (JobRestartException e) {
            e.printStackTrace();
        } catch (JobInstanceAlreadyCompleteException e) {
            e.printStackTrace();
        } catch (JobParametersInvalidException e) {
            e.printStackTrace();
        }
        return "success batch import data!" ;
    }
}
