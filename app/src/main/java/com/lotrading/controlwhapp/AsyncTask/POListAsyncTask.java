package com.lotrading.controlwhapp.AsyncTask;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import com.lotrading.controlwhapp.FilterCargoActivity;
import com.lotrading.controlwhapp.model.ModelService;
import com.lotrading.controlwhapp.model.Po;
import com.lotrading.controlwhapp.service.WarehouseServices;
import com.lotrading.controlwhapp.servicesError.RepositoryError;
import com.lotrading.controlwhapp.servicesError.RepositoryMapper;

import java.util.List;

import retrofit.RetrofitError;

public class POListAsyncTask extends AsyncTask<String, Integer, Boolean> {

	//dialog = ProgressDialog.show(FilterCargoActivity.this, "", "Loading...", true);
		private ProgressDialog Asycdialog = null;

		FilterCargoActivity filterCargoActivity = null;
		private String serviceCallback;
		RepositoryError repositoryError = null;
		Po poSearch = null;
		WarehouseServices warehouseServices = null;
		private List<Po> poListResult;


	public POListAsyncTask(WarehouseServices warehouseServices, FilterCargoActivity cargoActivity, Po poSearch) {
			this.poSearch = poSearch;
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
			boolean result = true;

			try {
				poListResult = warehouseServices.getPoList(poSearch);
				result = true;
			} catch(RetrofitError er) {
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
			filterCargoActivity.callbackFindListWhReceipt(poListResult);
		}
		
	}//end asyn task