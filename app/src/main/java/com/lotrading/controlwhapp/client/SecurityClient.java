package com.lotrading.controlwhapp.client;


import com.lotrading.controlwhapp.model.UserLogin;

import retrofit.http.Body;
import retrofit.http.POST;

/**
 * Created by DESARROLLO PORTATIL on 14/12/2016.
 *
 */

public interface SecurityClient {

    @POST("/security/login")
    HTTPResponse<UserLogin> login(@Body UserLogin userLogin);


}
