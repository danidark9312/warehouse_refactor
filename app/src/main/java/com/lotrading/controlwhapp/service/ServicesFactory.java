package com.lotrading.controlwhapp.service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.lotrading.controlwhapp.config.IConstants;
import com.squareup.okhttp.OkHttpClient;

import java.util.concurrent.TimeUnit;

import retrofit.RequestInterceptor;
import retrofit.RestAdapter;
import retrofit.android.AndroidLog;
import retrofit.client.OkClient;
import retrofit.converter.GsonConverter;

//
///**
// * Created by JoseTabares on 13/05/16.
// *
// * @param <T> Tipo de Servicio para realizar la petición.
// */
public class ServicesFactory<T> {

    private static final String API_BASE_PATH = IConstants.URL_PRODUCCION;
    private RestAdapter restAdapter;

    /**
     * Constructor, configura la petición http.
     *

     */
    public ServicesFactory() {
        this(IConstants.NO_TIMEOUT);
    }

    public ServicesFactory(long timeOut) {
        RequestInterceptor requestInterceptor = getRequestInterceptor();
        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();

        final OkHttpClient okHttpClient = new OkHttpClient();

        if(timeOut!=IConstants.NO_TIMEOUT){
            okHttpClient.setReadTimeout(timeOut, TimeUnit.SECONDS);
            okHttpClient.setConnectTimeout(timeOut, TimeUnit.SECONDS);
        }


        restAdapter = new RestAdapter.Builder()
                .setEndpoint(API_BASE_PATH)
                .setRequestInterceptor(requestInterceptor)
                .setConverter(new GsonConverter(gson))
                .setClient(new OkClient(okHttpClient))
                .setLogLevel(RestAdapter.LogLevel.FULL).setLog(new AndroidLog("YOUR_LOG_TAG"))
                .build();
    }

    private RequestInterceptor getRequestInterceptor() {
        return new RequestInterceptor() {
            @Override
            public void intercept(RequestFacade requestFacade) {
                requestFacade.addHeader("Accept", "application/json");
//            String token = app.getToken();
//            if (token != null) {
//                requestFacade.addHeader(IConstants.TOKEN, token);
//            }
            }
        };
    }

    /**
     * Obtiene la instancia del servicio.
     *
     * @param service Tipo de servicio.
     * @return Instancia del servicio.
     */
    public T getInstance(Class<T> service) {
        return restAdapter.create(service);
    }

}
