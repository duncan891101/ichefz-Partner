package com.playhut.partner.entity;

import java.io.Serializable;
import java.util.List;

/**
 *
 */
public class SelectPackEntity {
    public List<Packs> packs;

    public static class Packs implements Serializable {
        public String pack_id;
        public String img;
        public String title;
        public int pack_state;
        public boolean isCheck;
    }
}
