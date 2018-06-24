package com.course.controller;


import com.course.model.User;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.log4j.Log4j;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import sun.plugin2.main.server.ResultHandler;

@Log4j
@RestController
@Api(value = "v1",description = "这是我的第一个版本的demo")
@RequestMapping(value = "v1")
public class Demo {

    @Autowired
    private SqlSessionTemplate template;

    @RequestMapping(value = "/getusercount",method = RequestMethod.GET)
    @ApiOperation(value = "可以获取到用户数",httpMethod = "GET")
    public int getUserCount(){
        return template.selectOne("getUserCount");
    }

    @RequestMapping(value = "/updateuser",method = RequestMethod.POST)
    @ApiOperation(value = "修改用户数据",httpMethod = "POST")
    public int updateUser(@RequestBody User user){
        return template.update("updateUser",user);
    }

    @RequestMapping(value = "/adduser",method = RequestMethod.POST)
    @ApiOperation(value = "增加user",httpMethod = "POST")
    public int addUser(@RequestBody User user){
        return template.insert("addUser",user);
    }

    @RequestMapping(value = "/deluser",method = RequestMethod.GET)
    @ApiOperation(value = "删除user",httpMethod = "GET")
    public int delUser(@RequestParam int id){
        return template.delete("delUser",id);
    }
}
