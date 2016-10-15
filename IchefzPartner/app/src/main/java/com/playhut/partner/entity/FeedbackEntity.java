package com.playhut.partner.entity;

import java.util.List;

/**
 *
 */
public class FeedbackEntity {
    public int total;
    public List<Feedback> list;

    public static class Feedback {
        public String customer_first_name;
        public String customer_last_name;
        public String time;
        public int level;
        public String content;
        public List<String> imgs;
        public String customer_picture;
    }
}
