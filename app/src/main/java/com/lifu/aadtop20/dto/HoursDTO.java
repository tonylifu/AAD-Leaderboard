package com.lifu.aadtop20.dto;

public class HoursDTO {
    public String name;
    public String hours;
    public String country;
    public String badgeUrl;

    @Override
    public String toString() {
        return "HoursDTO{" +
                "name='" + name + '\'' +
                ", hours=" + hours +
                ", country='" + country + '\'' +
                ", badgeUrl='" + badgeUrl + '\'' +
                '}';
    }
}
