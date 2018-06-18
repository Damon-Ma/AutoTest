package com.course.server;

import com.course.bean.User;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController()
@Api(value = "/",description = "这是我所有的post方法")
@RequestMapping("/v1")
public class MyPostMethod {
    //保存cookies信息
    private Cookie cookie;
    @RequestMapping(value = "/login",method = RequestMethod.POST)
    @ApiOperation(value = "通过post请求返回cookies",httpMethod = "POST")

    public String login(@RequestParam(value = "username",required = true) String username,
                              @RequestParam(value = "password",required = true) String password,
                              HttpServletResponse response){
        //用户名密码正确登录成功，添加cookies
                if (username.equals("zhangsan") & password.equals("123456")){
                       cookie = new Cookie("login","true");
                       response.addCookie(cookie);
                       return "恭喜您登录成功!";
                }
                return "用户名或密码错误!";
    }
    //登录成功返回用户列表
    @RequestMapping(value = "/getuserlist",method = RequestMethod.POST)
    @ApiOperation(value = "验证用户名密码cookies成功，返回用户列表",httpMethod = "POST")
    public String getUserList(HttpServletRequest request,
                              @RequestBody User u
                              ){
        User user;
        Cookie[] cookies = request.getCookies();
        for (Cookie cookie :cookies){
            if (cookie.getName().equals("login")
                    && cookie.getValue().equals("true")
                    && u.getUsername().equals("zhangsan")
                    && u.getPassword().equals("123456")
                    ){

                user = new User();
                user.setName("lisi");
                user.setAge("18");
                user.setSex("man");

                return user.toString();
            }
        }

        return "您的参数非法！";
    }

}