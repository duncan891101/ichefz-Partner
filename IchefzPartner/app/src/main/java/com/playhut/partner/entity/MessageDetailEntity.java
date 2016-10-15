package com.playhut.partner.entity;

import java.util.List;

/**
 *
 */
public class MessageDetailEntity {
    public int total;
    public List<Message> message_list;

    public static class Message {
        public String message_id;
        public String sender_id;
        public String receipt_id;
        public String profile_picture;
        public String first_name;
        public String last_name;
        public String time;
        public String content;
        public int status;
        public String parent_id;
        public List<Child> child;
        public boolean isCheck; // CheckBox的选择状态
        public boolean isShow; // CheckBox是否可见
    }

    public static class Child {
        public String message_id;
        public String sender_id;
        public String receipt_id;
        public String profile_picture;
        public String first_name;
        public String last_name;
        public String time;
        public String content;
        public int status;
        public String parent_id;
        public boolean isCheck;
    }

}
