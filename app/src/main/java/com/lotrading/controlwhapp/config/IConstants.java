package com.lotrading.controlwhapp.config;

/**
 * Created by DESARROLLO PORTATIL on 13/12/2016.
 */

public interface IConstants {
    String URL_PRODUCCION = "http://10.2.2.6:8084/warehouseWS";
    String ERROR_REQUEST_TIMEOUT_MESSAGE = "La solicitud está tardando demasiado. Por favor inténtalo nuevamente.";
    String ERROR_DEFAULT_MESSAGE = "Ha ocurrido un error, inténtalo en unos minutos.";
    String ERROR_UNAUTHORIZED_MESSAGE = "El usuario no se encuentra autorizado en la aplicación";
    int ERROR_UNAUTHORIZED_CODE = 401;
    int ERROR_NOT_CONTENT = 204;
    int ERROR_DEFAULT_CODE = 0;
    int ERROR_NOT_FOUND = 404;
    int ERROR_BAD_REQUEST = 400;
    //String URL_PRODUCCION = "http://10.2.2.104:8090/warehouseWS";
    long NO_TIMEOUT= -1;
    long THIRTY = 30;

    interface MasterValues{
        int UNITTYPE = 22;
    }

    interface ExceptionTags{
        String EXCECUTION_ERROR = "Excecution error";
    }
    interface ExceptionMessage{
        String LOADING_IMAGE_ERROR = "Error fetching the image from url";
        String JSON_PARSE = "Error parsing json";
    }
}
