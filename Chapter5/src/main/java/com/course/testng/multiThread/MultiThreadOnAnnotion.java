package com.course.testng.multiThread;

import org.testng.annotations.Test;

/**
 * 多线程测试
 * */

public class MultiThreadOnAnnotion {
    //线程数，线程池
    @Test(invocationCount = 10,threadPoolSize = 3)
    public void test(){
        System.out.println(1);
        System.out.printf("当前运行线程：%s%n",Thread.currentThread().getId());
    }
}
