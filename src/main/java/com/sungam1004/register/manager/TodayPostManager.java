package com.sungam1004.register.manager;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class TodayPostManager {

    public static String post = "";

    public static List<String> questions = new ArrayList<>();

    public static void changePost(String post) {
        TodayPostManager.post = post;
    }

    public static void changePost(List<String> questions) {
        TodayPostManager.questions.clear();
        TodayPostManager.questions.addAll(questions);
    }
}
