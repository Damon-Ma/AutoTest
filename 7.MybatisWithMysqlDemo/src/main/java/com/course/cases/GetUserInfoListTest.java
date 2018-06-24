package com.course.cases;

import com.course.config.TestConfig;
import com.course.model.GetUserListCase;
import com.course.utils.DatabaseUtil;
import org.apache.ibatis.session.SqlSession;
import org.testng.annotations.Test;

import java.io.IOException;

public class GetUserInfoListTest {

    @Test(dependsOnGroups = "loginTrue",description = "获取用户性别为男的列表")
    public void getUserInfoList() throws IOException {
        SqlSession session = DatabaseUtil.getSqlSession();
        GetUserListCase getUserInfoListCase = session.selectOne("getUserListCase",1);
        System.out.println(getUserInfoListCase.toString());
        System.out.println(TestConfig.getUserListUrl);
    }
}
