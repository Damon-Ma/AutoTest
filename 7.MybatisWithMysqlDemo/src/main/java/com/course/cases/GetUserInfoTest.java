package com.course.cases;

import com.course.config.TestConfig;
import com.course.model.GetUserInfoCase;
import com.course.model.GetUserListCase;
import com.course.model.User;
import com.course.utils.DatabaseUtil;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.util.EntityUtils;
import org.apache.ibatis.session.SqlSession;
import org.json.JSONArray;
import org.json.JSONObject;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GetUserInfoTest {

    @Test(dependsOnGroups = "loginTrue",description = "获取用户信息")
    public void getUserInfo() throws IOException, InterruptedException {
        SqlSession session = DatabaseUtil.getSqlSession();
        GetUserListCase getUserListCase = session.selectOne("getUserListCase",2);
        System.out.println(getUserListCase.toString());
        System.out.println(TestConfig.getUserInfoUrl);


        //访问结果
        JSONArray result = getUserInfoList(getUserListCase);
        //取出返回结果的第0个元素
        JSONArray resultJson = new JSONArray(result.getString(0));
        System.out.println("请求结果："+resultJson.toString());
        String expected= getUserListCase.getExpected();
        Thread.sleep(3000);
        List<User> users = session.selectList(expected,getUserListCase);

        JSONArray userArray = new JSONArray(users);
        System.out.println("查询结果："+userArray.toString());
        Assert.assertEquals(resultJson.toString(),userArray.toString());

    }


    public JSONArray getUserInfoList(GetUserListCase getUserListCase) throws IOException {

        HttpPost post = new HttpPost(TestConfig.getUserInfoUrl);
        //设置参数
        JSONObject param = new JSONObject();
        param.put("userName",getUserListCase.getUserName());
        param.put("age",getUserListCase.getAge());
        param.put("sex",getUserListCase.getSex());
        System.out.println("请求参数是："+param);
        //格式转换
        StringEntity entity = new StringEntity(param.toString(),"utf-8");
        //添加cookies
        TestConfig.defaultHttpClient.setCookieStore(TestConfig.cookieStore);
        //添加头信息
        post.setHeader("content-type","application/json");
        //添加参数
        post.setEntity(entity);
        //访问
        HttpResponse response = TestConfig.defaultHttpClient.execute(post);
        //转换格式
        String result = EntityUtils.toString(response.getEntity());

        List resultList = Arrays.asList(result);
        JSONArray array = new JSONArray(resultList);
        return array;
    }
}
