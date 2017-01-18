package com.lotrading.controlwhapp.service;

import com.lotrading.controlwhapp.model.Po;
import com.lotrading.controlwhapp.model.Warehouse;

import java.util.List;

import retrofit.http.Body;

/**
 * Created by daniel on 23/12/2016.
 */
public interface WarehouseServices {
    List<Po> getPoList(Po poSearch);
    Warehouse createWarehouse(@Body Warehouse warehouse);
}
