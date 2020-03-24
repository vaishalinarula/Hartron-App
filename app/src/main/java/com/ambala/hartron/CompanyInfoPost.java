package com.ambala.hartron;

import com.google.gson.annotations.SerializedName;

public class CompanyInfoPost {


    @SerializedName("whatsapp")
    private String whatsapp_no;

    @SerializedName("gmail")
    private String gmail_address;

    @SerializedName("enquiry")
    private String enquiry_no;

    @SerializedName("landline")
    private String landline_no;

    @SerializedName("administration")
    private String administration_no;

    @SerializedName("instagram")
    private String instagram_link;

    @SerializedName("facebook")
    private String facebook_link;

    @SerializedName("twitter")
    private String twitter_link;

    @SerializedName("youtube")
    private String youtube_link;

    @SerializedName("gallery")
    private String gallery_link;

    @SerializedName("lastupdated")
    private String last_updated;

     /*

    private String whatsapp;

    private String gmail;

    private String enquiry;


    private String landline;


    private String administration;

    private String instagram;

    private String facebook;

    private String twitter;

    private String youtube;


    private String gallery;

      */

    public String getWhatsapp_no() {
        return whatsapp_no;
    }

    public String getGmail_address() {
        return gmail_address;
    }

    public String getEnquiry_no() {
        return enquiry_no;
    }

    public String getLandline_no() {
        return landline_no;
    }

    public String getAdministration_no() {
        return administration_no;
    }

    public String getInstagram_link() {
        return instagram_link;
    }

    public String getFacebook_link() {
        return facebook_link;
    }

    public String getTwitter_link() {
        return twitter_link;
    }

    public String getYoutube_link() {
        return youtube_link;
    }

    public String getGallery_link() {
        return gallery_link;
    }

    public String getLast_updated(){
        return last_updated;
    }

}
