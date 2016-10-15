package com.playhut.partner.entity;

import java.util.List;

/**
 *
 */
public class MessageListEntity {
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
    }
}
