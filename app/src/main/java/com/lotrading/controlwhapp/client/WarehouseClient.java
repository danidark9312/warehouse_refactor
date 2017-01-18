package com.lotrading.controlwhapp.client;


import com.lotrading.controlwhapp.model.Po;
import com.lotrading.controlwhapp.model.UserLogin;
import com.lotrading.controlwhapp.model.Warehouse;

import java.util.List;

import retrofit.http.Body;
import retrofit.http.POST;

/**
 * Created by DESARROLLO PORTATIL on 14/12/2016.
 *
 */

public interface WarehouseClient {

    @POST("/warehouse/getPoList")
    HTTPResponse<List<Po>> getPOlist(@Body Po poSearch);

    @POST("/warehouse/createWareHouseReceipt")
    HTTPResponse<Warehouse> createWarehouse(@Body Warehouse poSearch);


}
