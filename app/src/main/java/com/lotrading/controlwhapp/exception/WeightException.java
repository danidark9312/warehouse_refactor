package com.lotrading.controlwhapp.exception;

/**
 * Created by DESARROLLO PORTATIL on 18/07/2016.
 */
public class WeightException extends Exception {
    public WeightException (double min,double max){
        super("Weight Discrepancy, must be between "+String.format("%.2f", min)+" and "+String.format("%.2f", max));
    }
    public WeightException (){
        super("Weight Discrepancy");
    }
}
