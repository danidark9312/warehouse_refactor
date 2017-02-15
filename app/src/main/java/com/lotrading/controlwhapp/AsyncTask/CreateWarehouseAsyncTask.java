package com.lotrading.controlwhapp.AsyncTask;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.lotrading.controlwhapp.FilterCargoActivity;
import com.lotrading.controlwhapp.config.IConstants;
import com.lotrading.controlwhapp.model.Po;
import com.lotrading.controlwhapp.model.Warehouse;
import com.lotrading.controlwhapp.service.WarehouseServices;
import com.lotrading.controlwhapp.servicesError.RepositoryError;
import com.lotrading.controlwhapp.servicesError.RepositoryMapper;

import java.util.List;

import retrofit.RetrofitError;

public class CreateWarehouseAsyncTask extends AsyncTask<String, Integer, Boolean> {

	   private final Warehouse warehouse;
	//dialog = ProgressDialog.show(FilterCargoActivity.this, "", "Loading...", true);
		private ProgressDialog Asycdialog = null;

		FilterCargoActivity filterCargoActivity = null;
		private String serviceCallback;
		RepositoryError repositoryError = null;
		WarehouseServices warehouseServices = null;
		private Warehouse warehouseResult;


	public CreateWarehouseAsyncTask(WarehouseServices warehouseServices, FilterCargoActivity cargoActivity, Warehouse warehouse) {
			this.warehouse = warehouse;
			this.warehouseServices = warehouseServices;

			this.filterCargoActivity = cargoActivity;
		}

		@Override
        protected void onPreExecute() {
            //set message of the dialog
			Asycdialog = new ProgressDialog(this.filterCargoActivity);
            Asycdialog.setMessage("Loading");
            Asycdialog.setCancelable(false);
			Asycdialog.setCanceledOnTouchOutside(false);
            //show dialog
            Asycdialog.show();

        }
		@Override
		protected Boolean doInBackground(String... listModelService) {
			boolean result = false;

			try {
				warehouseResult = warehouseServices.createWarehouse(this.warehouse);
				result = warehouseResult!=null;

			} catch(RetrofitError er) {
				Log.e("Error caling services",IConstants.ERROR_DEFAULT_MESSAGE,er);
				repositoryError = RepositoryMapper.convertRetrofitErrorToRepositoryError(er);
			}
			return result;
		}

		protected void onPostExecute(Boolean result) {
			//response async
			if(result){
				callbackService();
			}else{
				Toast.makeText(filterCargoActivity,"Error calling operation web service", Toast.LENGTH_SHORT).show();
			}
			Asycdialog.dismiss();
			super.onPostExecute(result);
		}
		
		private void callbackService(){
			filterCargoActivity.callbackCreateWarehouseReceipt(warehouseResult);
		}
		
	}//end asyn task