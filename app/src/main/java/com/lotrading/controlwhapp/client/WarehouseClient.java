package com.lotrading.controlwhapp.client;


import com.lotrading.controlwhapp.model.Po;
import com.lotrading.controlwhapp.model.SaveWarehouseResponse;
import com.lotrading.controlwhapp.model.UploadImageResponse;
import com.lotrading.controlwhapp.model.UserLogin;
import com.lotrading.controlwhapp.model.Warehouse;

import java.util.List;

import retrofit.http.Body;
import retrofit.http.Multipart;
import retrofit.http.POST;
import retrofit.http.Part;
import retrofit.mime.TypedFile;
import retrofit.mime.TypedString;

/**
 * Created by DESARROLLO PORTATIL on 14/12/2016.
 *
 */

public interface WarehouseClient {

    @POST("/warehouse/getPoList")
    HTTPResponse<List<Po>> getPOlist(@Body Po poSearch);

    @POST("/warehouse/createWareHouseReceipt")
    HTTPResponse<Warehouse> createWarehouse(@Body Warehouse poSearch);

    @Multipart
    @POST("/warehouse/uploadImage")HTTPResponse<UploadImageResponse> uploadImage(@Part("uploadedFile")TypedFile uploadedFile, @Part("whNumber")TypedString whNumber, @Part("docType")TypedString docType);


    @POST("/warehouse/createPartialLabels")
    HTTPResponse<Warehouse> createPartialLabels(@Body Warehouse warehouse);


    @POST("/warehouse/saveWareHouseReceipt")
    HTTPResponse<SaveWarehouseResponse> saveWareHouse(@Body Warehouse poSearch);

}
