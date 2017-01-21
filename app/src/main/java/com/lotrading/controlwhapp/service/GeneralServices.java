package com.lotrading.controlwhapp.service;

import com.lotrading.controlwhapp.model.Client;
import com.lotrading.controlwhapp.model.Location;
import com.lotrading.controlwhapp.model.MasterValuesResponse;
import com.lotrading.controlwhapp.model.Supplier;
import com.lotrading.controlwhapp.model.TruckCompany;
import com.lotrading.controlwhapp.model.UserLogin;

import java.util.List;

/**
 * Created by daniel on 26/12/2016.
 */
public interface GeneralServices {
    List<Supplier> getListSupplier(String query);
    List<Client> getListClients(String query);
    List<TruckCompany> getTruckCompanies();
    List<MasterValuesResponse> getMasterValues(String masterId);
    List<Location> getLocationList();
}
