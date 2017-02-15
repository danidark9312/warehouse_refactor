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
    String SUCCESS ="SUCCESS" ;

    interface MasterValues{
        int UNITTYPE = 22;
        int PRINTER = 60;
    }
    interface DEPARTMENTS{
        int RAW_MATERIAL=2;
    }

    interface ExceptionTags{
        String EXCECUTION_ERROR = "Excecution error";
    }
    interface ExceptionMessage{
        String LOADING_IMAGE_ERROR = "Error fetching the image from url";
        String JSON_PARSE = "Error parsing json";
    }

    interface WRActivityMessages{
        String MESSAGE_CONFIRM_TERMINAT = "Do you want to complete this Warehouse Receipt?";
        String TITLE_CONFIRM_TERMINAT = "Warehouse Receipt";

        String MESSAGE_CONFIRM_PARTIAL_LABEL = "Are you sure to clone this item?";
        String TITLE_CONFIRM_PARTIAL_LABEL = "Warehouse Labels";



    }
    interface PrintActivityMessages{
        String CANNOT_GO_BACK = "Can not go back with print process active";

    }
}
