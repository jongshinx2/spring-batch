package com.example.springbatch.interfaces.rest;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.job.Job;
import org.springframework.batch.core.job.JobExecution;
import org.springframework.batch.core.job.parameters.JobParameters;
import org.springframework.batch.core.job.parameters.JobParametersBuilder;
import org.springframework.batch.core.launch.JobOperator;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.Date;

@RequiredArgsConstructor
@RequestMapping("/api/users")
@RestController
public class UserApi {

    //private final JobLauncher jobLauncher; JobLauncher deprecated in Spring Batch 6
    private final JobOperator jobOperator;
    private final Job job;

    @GetMapping("/start-batch")
    public String startBatchJob() throws Exception {
        JobParameters jobParameters = new JobParametersBuilder().addLong("run.id", System.currentTimeMillis(), true).toJobParameters();
        jobOperator.start(job, jobParameters);
        return "Batch job started successfully.";
    }

//    @PostMapping("/restart-batch")
//    public String restartBatchJob(Long executionId) throws Exception {
//        jobOperator.restart();
//        return "Batch job restarted successfully.";
//    }
}
