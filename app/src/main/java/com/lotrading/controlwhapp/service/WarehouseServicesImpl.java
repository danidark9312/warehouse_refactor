package com.lotrading.controlwhapp.service;

import com.lotrading.controlwhapp.client.HTTPResponse;
import com.lotrading.controlwhapp.client.WarehouseClient;
import com.lotrading.controlwhapp.config.IConstants;
import com.lotrading.controlwhapp.model.Po;
import com.lotrading.controlwhapp.model.SaveWarehouseResponse;
import com.lotrading.controlwhapp.model.UploadImageResponse;
import com.lotrading.controlwhapp.model.Warehouse;

import java.io.File;
import java.util.List;

import retrofit.http.Body;
import retrofit.mime.TypedFile;
import retrofit.mime.TypedString;

/**
 * Created by daniel on 23/12/2016.
 */

public class WarehouseServicesImpl implements WarehouseServices {
WarehouseClient securityClient;



    @Override
    public List<Po> getPoList(Po poSearch) {
        HTTPResponse<List<Po>> pOlist = securityClient.getPOlist(poSearch);
        return pOlist.getWrappedResponse();
    }
    @Override
    public Warehouse createWarehouse(@Body Warehouse warehouse){
        HTTPResponse<Warehouse> warehouseResult = securityClient.createWarehouse(warehouse);
        return warehouseResult.getWrappedResponse();
    }

    @Override
    public SaveWarehouseResponse saveWarehouse(@Body Warehouse warehouse){
        HTTPResponse<SaveWarehouseResponse> warehouseResult = securityClient.saveWareHouse(warehouse);
        return warehouseResult.getWrappedResponse();
    }

    @Override
    public boolean createPartialLabel(@Body Warehouse warehouse){
        HTTPResponse warehouseResult = securityClient.createPartialLabels(warehouse);
        return true;
    }

    @Override
    public UploadImageResponse uploadImage(File file, String photoType, String wh){
        HTTPResponse<UploadImageResponse> warehouseResult = securityClient.uploadImage(new TypedFile("multipart/form-data",file), new TypedString(wh), new TypedString(photoType));
        return warehouseResult.getWrappedResponse();
    }

    private WarehouseServicesImpl() {
        ServicesFactory<WarehouseClient> servicesFactory = new ServicesFactory(IConstants.THIRTY);
        securityClient = servicesFactory.getInstance(WarehouseClient.class);
    }

    public static WarehouseServices getServicesInstance(){
        return new WarehouseServicesImpl();
    }
}
