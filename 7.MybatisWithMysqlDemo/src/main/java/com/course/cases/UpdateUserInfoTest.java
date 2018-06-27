package com.course.cases;

import com.course.config.TestConfig;
import com.course.model.GetUserInfoCase;
import com.course.model.GetUserListCase;
import com.course.model.UpdateUserInfoCase;
import com.course.utils.DatabaseUtil;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.util.EntityUtils;
import org.apache.ibatis.session.SqlSession;
import org.json.JSONObject;
import org.testng.Assert;
import org.testng.annotations.Test;
import java.io.IOException;

public class UpdateUserInfoTest {

    @Test(dependsOnGroups = "loginTrue",description = "修改用户信息")
    public void updateUserInfo() throws IOException {
        SqlSession session = DatabaseUtil.getSqlSession();
        UpdateUserInfoCase updateUserInfoCase = session.selectOne("updateUserInfoCase",2);
        System.out.println(updateUserInfoCase.toString());
        System.out.println(TestConfig.updateUserInfoUrl);

        Assert.assertEquals(getResult(updateUserInfoCase,false),updateUserInfoCase.getExpected());

    }
    @Test(dependsOnGroups = "loginTrue",description = "删除用户信息")
    public void deleteUser() throws IOException {
        SqlSession session = DatabaseUtil.getSqlSession();
        UpdateUserInfoCase updateUserInfoCase = session.selectOne("updateUserInfoCase",1);
        System.out.println(updateUserInfoCase.getUserName());
        System.out.println(TestConfig.updateUserInfoUrl);

        System.out.println(updateUserInfoCase.getExpected());
        Assert.assertEquals(getResult(updateUserInfoCase,true),updateUserInfoCase.getExpected());
    }


    public String getResult(UpdateUserInfoCase updateUserInfoCase,Boolean delete) throws IOException {
        HttpPost post;
        JSONObject param = new JSONObject();
        if (delete){
            post = new HttpPost(TestConfig.deleteUserUrl);
            //参数
            param.put("userName",updateUserInfoCase.getUserName());
        }else{
            post = new HttpPost(TestConfig.updateUserInfoUrl);
            param.put("userName",updateUserInfoCase.getUserName());
            param.put("sex",updateUserInfoCase.getSex());
            param.put("age",updateUserInfoCase.getAge());
        }



        //头信息
        post.setHeader("content-type","application/json");
        StringEntity entity = new StringEntity(param.toString(),"utf-8");
        post.setEntity(entity);
        //cookies
        TestConfig.defaultHttpClient.setCookieStore(TestConfig.cookieStore);
        //执行
        HttpResponse response = TestConfig.defaultHttpClient.execute(post);
        //执行结果转换成String类型
        String result = EntityUtils.toString(response.getEntity(),"utf-8");
        System.out.println(result);
        return result;
    }

}

