package com.dongchao.datong.batch;

import com.dongchao.datong.batch.bean.Person;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.launch.support.SimpleJobLauncher;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.repository.support.JobRepositoryFactoryBean;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.batch.item.validator.Validator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.ClassPathResource;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
@Profile("dev")
@Configuration
@EnableBatchProcessing //开启批处理支持,项目启动时自动执行批处理任务，实际生产环境多用手动触发或者定时器触发
public class BatchConfig {
    /*
     *配置ItemReader实例,ItemReader是用来读取数据的接口
     */
    @Bean
    @StepScope //定义这个reader的生效域，预防@Value属性注入时去读取系统配置文件出现错误
    public FlatFileItemReader<Person> triggerReader(@Value("#{jobParameters['import.file.path']}") String pathToFile){
        FlatFileItemReader reader = new FlatFileItemReader();
        reader.setResource(new ClassPathResource(pathToFile));
        //配置读取规则
        reader.setLineMapper(new DefaultLineMapper<Person>() {
            {
                setLineTokenizer(new DelimitedLineTokenizer() {
                    {
                        setNames(new String[]{"name", "age", "nation", "address"});
                    }
                });
                setFieldSetMapper(new BeanWrapperFieldSetMapper<Person>() {
                    {
                        setTargetType(Person.class);
                    }
                });
            }
        });

        return reader;
    }
    /**
     * 配置处理数据的接口，ItemProcessor
     */
    @Bean
    public ItemProcessor<Person, Person> processor() {
        CsvItemProcesser processer = new CsvItemProcesser();
        processer.setValidator(csvBeanValidator());
        return processer;
    }

    @Bean
    public Validator<Person> csvBeanValidator() {
        return new CsvBeanValidator<>();
    }

    /**
     * ItemWriter是用来输出数据的接口
     *
     * @param dataSource 数据源,spring能让容器中已有的bean以参数的形式注入，由于spring boot 已经定了datasource。此处注入的就是我们配置的datasource
     * @return
     */
    @Bean
    public ItemWriter<Person> writer(DataSource dataSource) {
        JdbcBatchItemWriter<Person> writer = new JdbcBatchItemWriter<>();//使用jdbcBatchItemWriter写数据到数据库
        writer.setItemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<>());

        String sql = "insert into person (name,age,nation,address) values (:name,:age,:nation,:address)";//TODO
        writer.setSql(sql);
        writer.setDataSource(dataSource);
        return writer;
    }

    /**
     * JobRepository 用来注册job的容器
     * @param dataSource spring boot 自动注入已经配置的datasource
     * @param platformTransactionManager 事务管理
     * @return
     * @throws Exception
     */
    @Bean
    public JobRepository jobRepository(DataSource dataSource, PlatformTransactionManager platformTransactionManager) throws Exception {
        JobRepositoryFactoryBean jobRepositoryFactoryBean = new JobRepositoryFactoryBean();
        jobRepositoryFactoryBean.setDataSource(dataSource);
        jobRepositoryFactoryBean.setTransactionManager(platformTransactionManager);
        jobRepositoryFactoryBean.setDatabaseType("mysql");

        return jobRepositoryFactoryBean.getObject();
    }

    /**
     * JobLauncher 用来启动job的接口
     * @param dataSource
     * @param platformTransactionManager
     * @return
     * @throws Exception
     */
    @Bean
    public SimpleJobLauncher jobLauncher(DataSource dataSource, PlatformTransactionManager platformTransactionManager) throws Exception {
        SimpleJobLauncher simpleJobLauncher = new SimpleJobLauncher();
        simpleJobLauncher.setJobRepository(jobRepository(dataSource, platformTransactionManager));

        return simpleJobLauncher;
    }

    /**
     * Step 执行 ，ItemReader ,ItemProcess,ItemWriter的过程
     * @param stepBuilderFactory
     * @param reader
     * @param writer
     * @param processor
     * @return
     */
    @Bean
    public Step step(StepBuilderFactory stepBuilderFactory,FlatFileItemReader<Person> reader,ItemWriter<Person> writer,ItemProcessor<Person,Person> processor){
        return stepBuilderFactory.get("step_import_person")
                                 .<Person,Person>chunk(5000) //每次提交5000条数据
                                 .reader(reader)  //绑定ItemReader
                                 .processor(processor) //绑定ItemProcessor
                                 .writer(writer)//绑定ItemWriter
                                 .build();
    }

    /**
     * Job 我们要实际要执行的任务，包含一个或者多个Step
     * @param jobBuilderFactory
     * @param step
     * @return
     */
    @Bean
    public Job importJob(JobBuilderFactory jobBuilderFactory, Step step){

        return jobBuilderFactory.get("importJob")
                                .incrementer(new RunIdIncrementer())
                                .flow(step)
                                .end()
                                .listener(new CsvJobListener())
                                .build();
    }

}
