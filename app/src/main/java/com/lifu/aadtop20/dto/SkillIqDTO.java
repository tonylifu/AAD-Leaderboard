package com.lifu.aadtop20.dto;

public class SkillIqDTO {
    public String name;
    public String score;
    public String country;
    public String badgeUrl;

    @Override
    public String toString() {
        return "SkillIqDTO{" +
                "name='" + name + '\'' +
                ", score=" + score +
                ", country='" + country + '\'' +
                ", badgeUrl='" + badgeUrl + '\'' +
                '}';
    }
}
