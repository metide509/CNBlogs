package com.metide.cnblogs.utils;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Author   metide
 * Date     2017/4/20
 */

public class AppSchleher {

    private static final int MAX_THREAD_POOL_SIZE = 6;
    private static final int CORE_THREAD_POOL_SIZE = 2;

    private static final ExecutorService sExecutorService =
            new ThreadPoolExecutor(CORE_THREAD_POOL_SIZE,
                                   MAX_THREAD_POOL_SIZE,
                                   60L,
                                   TimeUnit.SECONDS,
                                    new LinkedBlockingQueue<Runnable>(),
                                   Executors.defaultThreadFactory());

    public static ExecutorService io(){
        return sExecutorService;
    }
}
