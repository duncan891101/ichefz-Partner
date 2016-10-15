package com.playhut.partner.entity;

import java.util.List;

/**
 *
 */
public class MyMenuPackEntity {
    public int total;
    public List<PackInfo> pack_list;

    public static class PackInfo {
        public String pack_id;
        public String pack_title;
        public String pack_img;
        public String pack_brief_introduce;
        public String pack_desc;
        public int pack_state;
        public String person2;
        public String person4;
        public String max_quantity;
        public String how_it_work;
        public boolean expandState;
    }

}
