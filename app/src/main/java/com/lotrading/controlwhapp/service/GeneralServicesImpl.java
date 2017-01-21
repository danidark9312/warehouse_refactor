package com.lotrading.controlwhapp.service;

import android.content.Context;

import com.lotrading.controlwhapp.client.GeneralClient;
import com.lotrading.controlwhapp.client.HTTPResponse;
import com.lotrading.controlwhapp.config.IConstants;
import com.lotrading.controlwhapp.model.Client;
import com.lotrading.controlwhapp.model.Location;
import com.lotrading.controlwhapp.model.MasterValuesResponse;
import com.lotrading.controlwhapp.model.Supplier;
import com.lotrading.controlwhapp.model.TruckCompany;

import java.util.List;

/**
 * Created by DESARROLLO PORTATIL on 14/12/2016.
 */

public class GeneralServicesImpl implements GeneralServices {
    GeneralClient generalClient;
    Context context;



    private GeneralServicesImpl() {
        ServicesFactory<GeneralClient> servicesFactory = new ServicesFactory(IConstants.THIRTY);
        generalClient = servicesFactory.getInstance(GeneralClient.class);
    }

    public List<Supplier> getListSupplier(String query){
        HTTPResponse<List<Supplier>> response = generalClient.getListSuppliers(query);
        List<Supplier> suppliers= response.getWrappedResponse();
        return suppliers;
    }
    public List<Client> getListClients(String query){
        HTTPResponse<List<Client>> response = generalClient.getListClients(query);
        List<Client> suppliers = response.getWrappedResponse();
        return suppliers;
    }
    public List<MasterValuesResponse> getMasterValues(String masterId){
        HTTPResponse<List<MasterValuesResponse>> response = generalClient.getMasterValue(masterId);
        List<MasterValuesResponse> masterValueList = response.getWrappedResponse();
        return masterValueList;
    }
    public List<TruckCompany> getTruckCompanies(){
        HTTPResponse<List<TruckCompany>> response = generalClient.getTruckCompanies();
        List<TruckCompany> suppliers = response.getWrappedResponse();
        return suppliers;
    }

    public List<Location> getLocationList(){
        HTTPResponse<List<Location>> locationsList = generalClient.getLocationsList();
        List<Location> wrappedResponse = locationsList.getWrappedResponse();
        return wrappedResponse;
    }


    public static GeneralServices getServicesInstance() {
        return new GeneralServicesImpl();
    }
}
