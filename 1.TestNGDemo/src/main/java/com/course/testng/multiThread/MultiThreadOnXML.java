package com.course.testng.multiThread;

import org.testng.annotations.Test;

public class MultiThreadOnXML {
    @Test
    public void test1(){
        System.out.printf("当前运行线程111111：%s%n",Thread.currentThread().getId());
    }
    @Test
    public void test2(){
        System.out.printf("当前运行线程222222：%s%n",Thread.currentThread().getId());
    }
    @Test
    public void test3(){
        System.out.printf("当前运行线程333333：%s%n",Thread.currentThread().getId());
    }

}
