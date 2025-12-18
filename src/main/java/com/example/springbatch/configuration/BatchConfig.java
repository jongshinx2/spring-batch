package com.example.springbatch.configuration;

import com.example.springbatch.domain.User;
import com.example.springbatch.infrastructure.UserProcessor;
import jakarta.persistence.EntityManagerFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.Job;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.job.parameters.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.Step;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.infrastructure.item.ItemProcessor;
import org.springframework.batch.infrastructure.item.ItemReader;
import org.springframework.batch.infrastructure.item.ItemWriter;
import org.springframework.batch.infrastructure.item.database.JpaItemWriter;
import org.springframework.batch.infrastructure.item.database.JpaPagingItemReader;
import org.springframework.batch.infrastructure.item.database.builder.JpaPagingItemReaderBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@RequiredArgsConstructor
@Configuration
public class BatchConfig {

    private final JobRepository jobRepository;
    private final EntityManagerFactory entityManagerFactory;
    private final JobCompletionListener jobCompletionListener;

    @Bean
    public Job job(Step step) {
        return new JobBuilder("job", jobRepository)
                .start(step)
                .listener(jobCompletionListener)
                .incrementer(new RunIdIncrementer())
                .build();
    }

    @Bean
    public Step userProcessingstep(ItemReader<User> reader,
                                   ItemProcessor<User, User> processor,
                                   ItemWriter<User> writer) {
        return new StepBuilder("userProcessingStep", jobRepository)
                .<User, User>chunk(10)  //  transactionManager 사용 불가 deprecated
                .reader(reader)
                .processor(processor)
                .writer(writer)
                .build();
    }

    @Bean
    public JpaPagingItemReader<User> reader() {
        return new JpaPagingItemReaderBuilder<User>()
                .name("userReader")
                .entityManagerFactory(entityManagerFactory)
                .queryString("SELECT u FROM User u")
                .pageSize(10)
                .build();
    }

    @Bean
    public UserProcessor processor() {
        return new UserProcessor();
    }

    @Bean
    public JpaItemWriter<User> writer() {
        return new JpaItemWriter<>(entityManagerFactory);
    }
}
