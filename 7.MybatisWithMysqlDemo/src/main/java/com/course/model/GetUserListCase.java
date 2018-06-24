package com.course.model;

import lombok.Data;

@Data
public class GetUserListCase {
    private int id;
    private String userName;
    private int age;
    private String sex;
    private String expected;
}
