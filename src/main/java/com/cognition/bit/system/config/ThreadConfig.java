package com.cognition.bit.system.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.*;

/**
 * @author Worry
 * @version 2019/9/16
 */
@Configuration
@EnableAsync
public class ThreadConfig {

    /**
     * 开始线程池
     * @return
     */
    @Bean("taskExecutor")
    public Executor taskExecutro(){
        ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
        //配置核心线程数
        taskExecutor.setCorePoolSize(10);
        //配置最大线程数
        taskExecutor.setMaxPoolSize(50);
        //配置队列大小
        taskExecutor.setQueueCapacity(1000);
        taskExecutor.setKeepAliveSeconds(60);
        //配置线程池中的线程的名称前缀
        taskExecutor.setThreadNamePrefix("threadPool--");
        //等线程完成时自动关闭
        taskExecutor.setWaitForTasksToCompleteOnShutdown(true);
        taskExecutor.setAwaitTerminationSeconds(120);
        // 设置拒绝策略：当pool已经达到max size的时候，如何处理新任务
        // CALLER_RUNS：不在新线程中执行任务，而是有调用者所在的线程来执行
        taskExecutor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        return taskExecutor;
    }
}
