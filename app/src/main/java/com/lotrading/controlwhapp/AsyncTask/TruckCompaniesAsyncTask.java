package com.lotrading.controlwhapp.AsyncTask;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.widget.Toast;

import com.lotrading.controlwhapp.FilterCargoActivity;
import com.lotrading.controlwhapp.WReceiptLOActivity;
import com.lotrading.controlwhapp.model.TruckCompany;
import com.lotrading.controlwhapp.model.Warehouse;
import com.lotrading.controlwhapp.service.GeneralServices;
import com.lotrading.controlwhapp.service.WarehouseServices;
import com.lotrading.controlwhapp.servicesError.RepositoryError;
import com.lotrading.controlwhapp.servicesError.RepositoryMapper;

import java.util.List;

import retrofit.RetrofitError;

public class TruckCompaniesAsyncTask extends AsyncTask<String, Integer, Boolean> {

		private ProgressDialog Asycdialog = null;
		WReceiptLOActivity filterCargoActivity = null;
		RepositoryError repositoryError = null;
		GeneralServices generalServices = null;
		List<TruckCompany> truckCompanies;

	public TruckCompaniesAsyncTask(GeneralServices generalServices, WReceiptLOActivity cargoActivity) {
			this.generalServices = generalServices;
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
				truckCompanies = generalServices.getTruckCompanies();
				result = truckCompanies!=null;

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
			filterCargoActivity.callbackGetListTruckCompanies(truckCompanies);
		}
		
	}//end asyn task