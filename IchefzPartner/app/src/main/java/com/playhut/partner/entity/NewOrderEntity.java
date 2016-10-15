package com.playhut.partner.entity;

import java.util.List;

/**
 *
 */
public class NewOrderEntity {
    public int total;
    public List<Order> order_list;

    public static class Order {
        public String order_id;
        public String order_number;
        public String time;
        public String customer_first_name;
        public String customer_last_name;
        public String customer_number;
        public String customer_address;
        public String remark;
        public int order_state;
        public int chef_handle;
        public String subtotal;
        public List<OrderItem> order_items;
    }

    public static class OrderItem {
        public String mtype;
        public List<String> picture;
        public String title;
        public String person;
        public String quantity;
        public String item_price;
    }

}
