package com.course.httpclient.cookies;

import org.apache.http.HttpResponse;
import org.apache.http.client.CookieStore;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

public class MyCookiesForGet {
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
        String uri = bundle.getString("withcookies.uri");
        String testUrl = this.url + uri;

        HttpGet get = new HttpGet(testUrl);
        DefaultHttpClient client = new DefaultHttpClient();
        client.setCookieStore(this.store);

        HttpResponse response = client.execute(get);

        int status = response.getStatusLine().getStatusCode();

        if (status == 200){
            String result = EntityUtils.toString(response.getEntity(),"utf-8");
            System.out.println(result);
        }
    }
}
