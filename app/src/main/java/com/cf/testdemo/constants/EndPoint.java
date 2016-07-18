package com.cf.testdemo.constants;

public class EndPoint {

    private static final String STAGING_DOMAIN = "http://54.201.23.67/"; // staging 54.201.23.67
    private static final String STAGING_DOMAIN_PORT = "http://54.201.23.67:2016/";
    private static final String PRODUCTION_DOMAIN = "https://safe.capitalfloat.com/";

    private static final String BASE_URL = STAGING_DOMAIN_PORT;
    private static final String SOURCES = "cf/";
    private static final String DSA = "dsa_app/";
    private static final String RESPONSE_TYPE = "json";

    /** API */
    //    /cf/dsa_app/new_lead
    public static final String new_lead_API = "http://52.38.43.33:8083/testdemo";

}