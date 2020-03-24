package com.ambala.hartron;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface JsonPlaceHolderApi {
     String mBASEURL = "http://103.87.24.58/";
   // String mBASEURL = "https://unbeautiful-caliber.000webhostapp.com/";

    @FormUrlEncoded
    @POST("sakshamkaksha/focuslogin.php")
    Call<List<UserLoginPost>> getUserLoginStatus(@Field("f_uid") String user_id,
                                                 @Field("f_pwd") String password
    );

    @FormUrlEncoded
    @POST("sakshamkaksha/focusfeedback.php")
    Call<List<FeedbackPost>> getFeedbackStatus(@Field("serial_no") String enrollment_no,
                                               @Field("feedback") String feedback);

    @FormUrlEncoded
    @POST("sakshamkaksha/companyinfo.php")
    Call<List<CompanyInfoPost>> getCompanyInfo(@Field("cid") String cid);
}
