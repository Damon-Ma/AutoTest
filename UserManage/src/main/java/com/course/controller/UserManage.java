package com.course.controller;

import com.course.model.User;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.log4j.Log4j2;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Objects;

@Log4j2
@RestController
@Api(value = "v1",description = "用户管理系统接口")
@RequestMapping(value = "v1")
public class UserManage {

    @Autowired
    private SqlSessionTemplate template;
    public int result; //用来存放sql执行结果


    //登录接口
    @RequestMapping(value = "/login",method = RequestMethod.POST)
    @ApiOperation(value = "登录接口",httpMethod = "POST")
    public Boolean login(@RequestBody User user, HttpServletResponse response){
        User u = template.selectOne("login",user);
        if (u != null) {
            if (u.getIsDelete().equals("0")) {
                String userCookie = u.getUserName() + "|true";
                Cookie loginCookie = new Cookie("login", userCookie);
                response.addCookie(loginCookie);
                if (u.getPermission().equals("1")){
                    Cookie isAdmin = new Cookie("isAdmin","true");
                    response.addCookie(isAdmin);
                    log.info(u.getUserName()+",登录成功，身份是：管理员!");
                    return true;
                }
                log.info(u.getUserName()+",登录成功，身份是:用户！");
                return true;
            }
        }
        log.info("登陆失败!使用用户名："+user.getUserName()+"\t密码："+user.getPassword());
        return false;
    }

    //注册接口
    @RequestMapping(value = "/signIn",method = RequestMethod.POST)
    @ApiOperation(value = "注册接口",httpMethod = "POST")
    public Boolean addUser(@RequestBody User user){
        try {
            result = template.insert("addUser",user);
            log.info("注册成功");
            return true;
        }catch (Exception e){
            log.info("注册失败,用户名已存在！");
            return false;
        }
    }

    //修改用户信息接口
    @RequestMapping(value = "updateUserInfo",method = RequestMethod.POST)
    @ApiOperation(value = "修改个人信息接口",httpMethod = "POST")
    public boolean updateUserInfo(@RequestBody User user,
                                  HttpServletRequest request){
        //获取cookie的用户名
        String cookieUser = getUserName(request);
        String userName = user.getUserName();
        boolean isAdmin = isAdmin(request);
        //user，限制传入username，password，sex，age
        if (userName.equals(cookieUser)){
            result = template.update("updateUserInfo",user);
            if (result>0){
                log.info("用户信息修改成功！");
                return true;
            }
        }
        //admin，传入username，sex，age，permission
        if (userName.equals(cookieUser) && isAdmin){
            result = template.update("updateUserInfo",user);
            if (result>0){
                log.info("用户信息修改成功！");
                return true;
            }
        }
        //admin修改user的permission 限制传入username，permission
        if (!userName.equals(cookieUser) && isAdmin){
            result = template.update("updateUserInfo",user);
            if (result>0){
                log.info(cookieUser+"将"+userName+"的权限改成了"+user.getPermission());
                return true;
            }
        }
        log.info("修改失败！！");
        return false;
    }

    //admin查询用户信息
    @RequestMapping(value = "selectUserInfo",method = RequestMethod.POST)
    @ApiOperation(value = "查找用户信息",httpMethod = "POST")
    public String selectUserInfo(@RequestBody User user,
                                       HttpServletRequest request){
        if (isAdmin(request)){
            List<User> users = template.selectList("selectUserInfo",user);
            for (int i =0;i<users.size();i++){
                log.info(users.get(i).toString());
            }
            return users.toString();
        }
        return null;
    }


    //删除用户信息，限制传入username
    @RequestMapping(value = "/deleteUser",method = RequestMethod.POST)
    @ApiOperation(value = "删除用户信息接口",httpMethod = "POST")
    public boolean deleteUser(@RequestBody User user,
                              HttpServletRequest request){
        String CookiesUser = getUserName(request);

        if(CookiesUser.equals(user.getUserName()) || isAdmin(request)){
            result = template.update("deleteUser",user);
            if (result>0){
                log.info(user.getUserName()+"用户删除成功！");
                return true;
            }
        }
        log.info("用户删除失败！");
        return false;
    }

    //获取登录cookie的userName
    public String getUserName(HttpServletRequest request){
        Cookie[] cookies = request.getCookies();
        if (Objects.isNull(cookies)){
            log.info("cookie为空");
            return null;
        }
        for (Cookie cookie:cookies){
            if (cookie.getName().equals("login")){
               String value = cookie.getValue();
               String userName = value.split("\\|")[0];
               log.info("userName:"+userName);
               return userName;
            }
        }
        return null;
    }
//    //验证登录Cookie
//    public boolean isLogin(HttpServletRequest request){
//        //String islogin = verifyCookie(request);
//        //如果login的cookie不为空则返回true，否则返回false
//        if (Objects.isNull(getUserNamr(request))){
//            return false;
//        }
//        return true;
//    }
    //验证管理员cookie
    public boolean isAdmin(HttpServletRequest request){
        Cookie[] cookies = request.getCookies();
        if (Objects.isNull(cookies)){
            log.info("cookie为空");
            return false;
        }
        for (Cookie cookie:cookies){
            if (cookie.getName().equals("isAdmin") && cookie.getValue().equals("true")){
                return true;
            }
        }
        return false;
    }
}
