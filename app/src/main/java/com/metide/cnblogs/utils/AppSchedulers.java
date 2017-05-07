package com.metide.cnblogs.utils;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Author   metide
 * Date     2017/4/20
 */

public class AppSchedulers {

    private static final int MAX_THREAD_POOL_SIZE = 12;
    private static final int CORE_THREAD_POOL_SIZE = 6;

    private static final ExecutorService sExecutorService =
            new ThreadPoolExecutor(CORE_THREAD_POOL_SIZE,
                                   MAX_THREAD_POOL_SIZE,
                                   60L,
                                   TimeUnit.SECONDS,
                                    new LinkedBlockingQueue<Runnable>(),
                                   Executors.defaultThreadFactory());

    public static Scheduler main(){return AndroidSchedulers.mainThread();}
    public static Scheduler io(){return Schedulers.from(sExecutorService);}
}
