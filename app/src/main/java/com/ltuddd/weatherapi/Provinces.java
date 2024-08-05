package com.ltuddd.weatherapi;

import java.util.List;

public class Provinces {
    private String pro_id;
    private String pro_name;
    private String pro_name_en;
    public Provinces(String id, String name, String name_en) {
        this.pro_id = id;
        this.pro_name = name;
        this.pro_name_en = name_en;
    }

    public String getId() {
        return pro_id;
    }

    public String getName() {
        return pro_name;
    }

    public String getName_En() {
        return pro_name_en;
    }

    @Override
    public String toString() {
        return pro_name;
    }

    public static String findProvincesIdByEnName(List<Provinces> provincesList, String enName) {
        for (Provinces province : provincesList) {
            if (province.getName_En().equalsIgnoreCase(enName)) {
                return province.getId();
            }
        }
        return null;
    }

}
