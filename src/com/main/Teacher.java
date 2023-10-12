package com.main;

import java.util.HashMap;

public class Teacher {
    private HashMap<String, Object> teacher;

    public Teacher(String name, String email, Object number) {
         teacher.put("name", name);
         teacher.put("email", email);
         teacher.put("number", number);
    }
}
