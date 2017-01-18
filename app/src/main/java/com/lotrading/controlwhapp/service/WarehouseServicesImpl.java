package com.lotrading.controlwhapp.service;

import com.lotrading.controlwhapp.client.HTTPResponse;
import com.lotrading.controlwhapp.client.SecurityClient;
import com.lotrading.controlwhapp.client.WarehouseClient;
import com.lotrading.controlwhapp.config.IConstants;
import com.lotrading.controlwhapp.model.Po;
import com.lotrading.controlwhapp.model.UserLogin;
import com.lotrading.controlwhapp.model.Warehouse;
import com.lotrading.controlwhapp.utilities.Encryption;

import java.util.List;

import retrofit.http.Body;

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

    private WarehouseServicesImpl() {
        ServicesFactory<WarehouseClient> servicesFactory = new ServicesFactory(IConstants.THIRTY);
        securityClient = servicesFactory.getInstance(WarehouseClient.class);
    }

    public static WarehouseServices getServicesInstance(){
        return new WarehouseServicesImpl();
    }
}
