package com.lotrading.controlwhapp.service;

import com.lotrading.controlwhapp.model.UserLogin;

/**
 * Created by DESARROLLO PORTATIL on 14/12/2016.
 */

public interface LoginServices extends Services{
    UserLogin login(UserLogin userLogin);

}
