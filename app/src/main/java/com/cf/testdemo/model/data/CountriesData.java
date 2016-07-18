package com.cf.testdemo.model.data;

/**
 * Created by arunkt on 12/07/16.
 */
public class CountriesData {

    private String id = "";
    private String code3L = "";
    private String code2L = "";
    private String name = "";
    private String name_official = "";
    private String flag_32 = "";
    private String flag_128 = "";
    private String latitude = "";
    private String longitude = "";

    public CountriesData(){
        id = "";
        code3L = "";
        code2L = "";
        name = "";
        name_official = "";
        flag_32 = "";
        flag_128 = "";
        latitude = "";
        longitude = "";
    }

    public String convertToString() {
        return getName() + " " + getName_official();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCode3L() {
        return code3L;
    }

    public void setCode3L(String code3L) {
        this.code3L = code3L;
    }

    public String getCode2L() {
        return code2L;
    }

    public void setCode2L(String code2L) {
        this.code2L = code2L;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName_official() {
        return name_official;
    }

    public void setName_official(String name_official) {
        this.name_official = name_official;
    }

    public String getFlag_32() {
        return flag_32;
    }

    public void setFlag_32(String flag_32) {
        this.flag_32 = flag_32;
    }

    public String getFlag_128() {
        return flag_128;
    }

    public void setFlag_128(String flag_128) {
        this.flag_128 = flag_128;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }
}
