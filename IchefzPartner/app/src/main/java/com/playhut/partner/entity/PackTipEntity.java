package com.playhut.partner.entity;

import java.util.List;

/**
 *
 */
public class PackTipEntity {

    public List<Tip> apron_imgs;

    public static class Tip {
        public String id;
        public String img;
        public String title;
        public String description;
    }
}
