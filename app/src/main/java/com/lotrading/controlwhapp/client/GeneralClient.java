package com.lotrading.controlwhapp.client;


import com.lotrading.controlwhapp.model.Client;
import com.lotrading.controlwhapp.model.Location;
import com.lotrading.controlwhapp.model.MasterValuesResponse;
import com.lotrading.controlwhapp.model.Supplier;
import com.lotrading.controlwhapp.model.TruckCompany;
import com.lotrading.controlwhapp.model.UserLogin;

import java.util.List;

import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.Query;

/**
 * Created by DESARROLLO PORTATIL on 14/12/2016.
 *
 */

public interface GeneralClient {

    @GET("/general/getListSuppliers")
    HTTPResponse<List<Supplier>> getListSuppliers(@Query("nameSupplier") String nameSupplier);

    @GET("/general/getListClients")
    HTTPResponse<List<Client>> getListClients(@Query("nameClient") String nameSupplier);

    @GET("/general/getListTruckCompanies")
    HTTPResponse<List<TruckCompany>> getTruckCompanies();

    @GET("/general/getMasterValues")
    HTTPResponse<List<MasterValuesResponse>> getMasterValue(@Query("masterId") String masterId);

    @GET("/general/getLocationsList")
    HTTPResponse<List<Location>> getLocationsList();

}


