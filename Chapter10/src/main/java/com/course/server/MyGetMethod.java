package com.course.server;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;


@RestController
@Api(value = "/",description = "这是我全部的get请求接口")
public class MyGetMethod{

    /**
     * 获取cookie的get请求
     * */
    @RequestMapping(value = "/getcookie",method = RequestMethod.GET)
    @ResponseBody
    @ApiOperation(value = "获取cookies",httpMethod = "GET")
    public String getCookies(HttpServletResponse response){
        Cookie cookie = new Cookie("login","true");
        response.addCookie(cookie);
        return "恭喜你获得cookies成功";
    }
    /**
    * 携带cookie才能访问的get请求
    * */
    @ApiOperation(value = "要求客户端携带cookies访问",httpMethod = "GET")
    @RequestMapping(value = "getwithcookie",method = RequestMethod.GET)
    public String getWithCookies(HttpServletRequest request){
        Cookie[] cookies = request.getCookies();
        if (Objects.isNull(cookies)){
            return "你必须带着cookies访问我";
        }
        for (Cookie cookie :cookies){
            if (cookie.getName().equals("login") && cookie.getValue().equals("true")){
                return "恭喜你登录成功！";
            }
        }
        return "Cookie错误,禁止登录！！";
    }

    /**
     * 带参数请求get接口开发
     * 方法1
     * */
    @ApiOperation(value = "需要携带参数才能访问的get方法1",httpMethod = "GET")
    @RequestMapping(value = "/getparam",method = RequestMethod.GET)
    public Map getParam(@RequestParam Integer start,
                            @RequestParam Integer end){
        Map<String ,Integer> myList = new HashMap<>();
        myList.put("鞋子",200);
        myList.put("小浣熊",1);
        myList.put("衣服",88);

        return myList;
    }
    /*
    * 带参数请求get接口开发
    *方法2
    * */
    @ApiOperation(value = "需要携带参数才能访问的get方法2",httpMethod = "GET")
    @RequestMapping(value = "/getwithparam/{start}/{end}",method = RequestMethod.GET)
    public Map getWithParam(@PathVariable Integer start,
                            @PathVariable Integer end){
        Map<String,Integer> myList = new HashMap<>();
        myList.put("nike鞋子",400);
        myList.put("大豫竹方便面",1);
        myList.put("优衣库T恤",78);

        return myList;
    }

}
