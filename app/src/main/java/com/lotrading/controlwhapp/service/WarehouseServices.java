package com.lotrading.controlwhapp.service;

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
public interface WarehouseServices {
    List<Po> getPoList(Po poSearch);
    Warehouse createWarehouse(@Body Warehouse warehouse);
    UploadImageResponse uploadImage(File file, String photoType, String wh);
    boolean createPartialLabel(@Body Warehouse warehouse);
    SaveWarehouseResponse saveWarehouse(@Body Warehouse warehouse);
}
