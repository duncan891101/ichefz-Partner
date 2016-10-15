package com.playhut.partner.entity;

import java.util.List;

/**
 *
 */
public class FinanceListEntity {
    public int total;
    public String total_amount;
    public List<OrderList> order_list;

    public static class OrderList {
        public String order_id;
        public String order_number;
        public String time;
        public String price;
        public String img;
    }
}
