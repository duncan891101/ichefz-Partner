package com.playhut.partner.entity;

import java.util.List;

/**
 *
 */
public class MyMenuSetEntity {
    public int total;
    public List<SetInfo> set_list;

    public static class SetInfo {
        public String set_id;
        public List<PackInfo> sets;
        public String set_title;
        public String set_desc;
        public int set_state;
        public String person2;
        public String person4;
        public String max_quantity;
        public boolean expandState; // false - 未展开   true - 展开
    }

    public static class PackInfo {
        public String pack_id;
        public String pack_title;
        public String pack_img;
        public int pack_state;
    }

}
