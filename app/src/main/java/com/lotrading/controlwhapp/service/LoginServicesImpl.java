package com.lotrading.controlwhapp.service;

import android.content.Context;

import com.lotrading.controlwhapp.client.HTTPResponse;
import com.lotrading.controlwhapp.client.SecurityClient;
import com.lotrading.controlwhapp.config.IConstants;
import com.lotrading.controlwhapp.model.UserLogin;
import com.lotrading.controlwhapp.utilities.Encryption;

/**
 * Created by DESARROLLO PORTATIL on 14/12/2016.
 */

public class LoginServicesImpl implements LoginServices{
    SecurityClient securityClient;
    Context context;



    private LoginServicesImpl() {
        ServicesFactory<SecurityClient> servicesFactory = new ServicesFactory(IConstants.THIRTY);
        securityClient = servicesFactory.getInstance(SecurityClient.class);
    }

    public UserLogin login(UserLogin userLogin){
        String encryptPass = null;
        UserLogin login = null;
        try {
            encryptPass = new Encryption().encrypt(userLogin.getPassword());
            userLogin.setPassword(encryptPass);
            HTTPResponse<UserLogin> httpResponse= securityClient.login(userLogin);
            login = httpResponse.getWrappedResponse();
        } catch (Encryption.EncryptionException e) {
            e.printStackTrace();
        }
        return login;
    }


    public static LoginServices getServicesInstance() {
        return new LoginServicesImpl();
    }
}
