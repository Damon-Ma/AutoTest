package com.course.httpclient.cookies;

import org.apache.http.HttpResponse;
import org.apache.http.client.CookieStore;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.cookie.Cookie;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;
import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

public class MyCookiesForPost {
    private String url;
    private ResourceBundle bundle;
    private CookieStore store;

    @BeforeTest
    public void beforeTest(){
        bundle = ResourceBundle.getBundle("application",Locale.CHINA);
        url = bundle.getString("test.url");
    }

    @Test
    public void test1() throws IOException {
        String uri = bundle.getString("getCookies.uri");
        String testUrl = this.url + uri;

        HttpGet get = new HttpGet(testUrl);

        DefaultHttpClient client = new DefaultHttpClient();
        HttpResponse response = client.execute(get);

        String result = EntityUtils.toString(response.getEntity(),"utf-8");
        System.out.println(result);

        store = client.getCookieStore();
        List<Cookie> cookies = store.getCookies();
        for (Cookie cookie : cookies){
            String name = cookie.getName();
            String value = cookie.getValue();
            System.out.println("Cookie的name="+name + "\tCookie的value="+value);
        }
    }

    @Test(dependsOnMethods = "test1")
    public void test2() throws IOException {
        System.out.println("post请求开始了");
        String uri = bundle.getString("postwithcookies.uri");
        String testUrl = url + uri;
        //创建post请求
        HttpPost post = new HttpPost(testUrl);
        //创建client
        DefaultHttpClient client = new DefaultHttpClient();
        //创建json对象，并赋值
        JSONObject json = new JSONObject();
        json.put("name","huhansan");
        json.put("sex","man");

        //添加头信息
        post.setHeader("content-type","application/json");
        //添加json参数
        StringEntity entity = new StringEntity(json.toString(),"utf-8");
        post.setEntity(entity);
        //添加cookies
        client.setCookieStore(this.store);
        //post请求返会结果
        HttpResponse response = client.execute(post);
        System.out.println("打印出response:"+response);
        String result = EntityUtils.toString(response.getEntity(),"utf-8");
        System.out.println(result);


        //判断返回的json信息
        JSONObject resultJson = new JSONObject(result);
        String success = resultJson.getString("huhansan");
        int status = resultJson.getInt("status");

        Assert.assertEquals(success,"success");
        Assert.assertEquals(status,1);


    }
}
