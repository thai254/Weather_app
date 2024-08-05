package com.ltuddd.weatherapi;

import java.util.List;

public class Districts {
    private String name_en;
    private String dis_name;
    private String dis_lati;
    private String dis_longi;
    public Districts(String id, String name_en, String latitude, String longitude) {
        this.dis_name = id;
        this.name_en = name_en;
        this.dis_lati = latitude;
        this.dis_longi = longitude;
    }

    public String getId() {
        return dis_name;
    }
    public String getName_en() {
        return name_en;
    }
    public String getLat() {
        return dis_lati;
    }

    public String getLong() {
        return dis_longi;
    }

    @Override
    public String toString() {
        return dis_name;
    }

    public static String[] findDistrictLatLonByEnName(List<Districts> districtsList, String enName) {
        for (Districts district : districtsList) {
            if (district.getName_en().equalsIgnoreCase(enName)) {
                String latitude = district.getLat();
                String longitude = district.getLong();
                String name_dis = district.getName_en();
                return new String[]{latitude, longitude, name_dis};
            }
        }
        return null;
    }
    public static int findPositionByDistrictName(List<Districts> districtsList, String districtName) {
        for (int i = 0; i < districtsList.size(); i++) {
            Districts district = districtsList.get(i);
            if (district.getName_en().equalsIgnoreCase(districtName)) {
                return i;
            }
        }
        return -1; // Trả về -1 nếu không tìm thấy
    }
}
