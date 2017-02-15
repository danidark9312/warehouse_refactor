package com.lotrading.controlwhapp.view;

import android.app.ActionBar;
import android.content.Context;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.lotrading.controlwhapp.model.ModelItemRawMaterials;
import com.lotrading.controlwhapp.model.ModelItemWhReceipt;
import com.lotrading.controlwhapp.model.ModelWhReceipt;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;

/**
 * Created by daniel on 21/01/2017.
 */

public class ItemTableRow extends TableRow {

    ModelWhReceipt modelWhReceipt;
    public ItemTableRow(Context context,  ModelWhReceipt modelWhReceipt) {
        super(context);
        this.modelWhReceipt = modelWhReceipt;
        setLayoutParams(new TableLayout.LayoutParams(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.WRAP_CONTENT));

    }

    public void setRowData(ModelItemWhReceipt modelItemWhReceipt){

        // for (int j = 0; j <= 10 /* 10 fields */; j++) {
                addView(createColumn(modelItemWhReceipt.getHazmat() ? "YES": "NO"));

                addView(createColumn(String.valueOf(modelItemWhReceipt.getnPieces())));
                addView(createColumn(String.valueOf(modelItemWhReceipt.getNameUnitType())));

                addView(createColumn(String.valueOf(modelItemWhReceipt.getLength())));
                addView(createColumn(String.valueOf(modelItemWhReceipt.getWidth())));
                addView(createColumn(String.valueOf(modelItemWhReceipt.getHeight())));

                addView(createColumn(new DecimalFormat("#0.00").format(modelItemWhReceipt.getVolume())));
                addView(createColumn(String.valueOf((int) roundDecimal(modelItemWhReceipt.getWeigthLB(), 2))));
                addView(createColumn(String.valueOf(roundDecimal(modelItemWhReceipt.getWeigthKG(), 2))));

                if (this.modelWhReceipt.getIdDep() == 1 /* rm */&&  modelItemWhReceipt.getRelationIdRMItem()!= 1) {
                    int positionRelation = modelItemWhReceipt.getRelationIdRMItem();
                    ModelItemRawMaterials itemRMFather = modelWhReceipt.getListItemsRawMaterials().get(positionRelation);
                    String idCodeProduct = itemRMFather.getProductRef();
                    addView(createColumn(idCodeProduct));
            }
        // }



    }

    private TextView createColumn(String text){
        TextView column = new TextView(super.getContext());
        column.setLayoutParams(new TableRow.LayoutParams(
                ActionBar.LayoutParams.WRAP_CONTENT,
                ActionBar.LayoutParams.WRAP_CONTENT));
        column.setTextSize(TypedValue.COMPLEX_UNIT_SP, 19);
        column.setGravity(Gravity.CENTER_HORIZONTAL);

        column.setText(text);
        return column;
    }

    private double roundDecimal(double value, int dec) {
            String val = value + "";
            BigDecimal big = new BigDecimal(val);
            big = big.setScale(dec, RoundingMode.HALF_UP);
            return Double.parseDouble(big + "");

    }
}
