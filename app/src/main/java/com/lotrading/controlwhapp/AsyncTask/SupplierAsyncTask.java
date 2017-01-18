package com.lotrading.controlwhapp.AsyncTask;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.widget.Toast;

import com.lotrading.controlwhapp.FilterCargoActivity;
import com.lotrading.controlwhapp.client.GeneralClient;
import com.lotrading.controlwhapp.model.Po;
import com.lotrading.controlwhapp.model.Supplier;
import com.lotrading.controlwhapp.service.GeneralServices;
import com.lotrading.controlwhapp.service.WarehouseServices;
import com.lotrading.controlwhapp.servicesError.RepositoryError;
import com.lotrading.controlwhapp.servicesError.RepositoryMapper;

import java.util.List;

import retrofit.RetrofitError;

public class SupplierAsyncTask extends AsyncTask<String, Integer, List<Supplier>> {

	//dialog = ProgressDialog.show(FilterCargoActivity.this, "", "Loading...", true);
		private ProgressDialog Asycdialog = null;

		FilterCargoActivity filterCargoActivity = null;
		private String serviceCallback;
		RepositoryError repositoryError = null;
		GeneralServices generalServices = null;
		private List<Po> poListResult;


	public SupplierAsyncTask(GeneralServices generalServices, FilterCargoActivity cargoActivity) {
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
			super.onPreExecute();

        }
		@Override
		protected List<Supplier> doInBackground(String... listModelService) {
			List<Supplier> suppliers = null;

			try {
				suppliers = generalServices.getListSupplier(listModelService[0]);
			} catch(RetrofitError er) {
				repositoryError = RepositoryMapper.convertRetrofitErrorToRepositoryError(er);
			}
			return suppliers;
		}

		protected void onPostExecute(List<Supplier> suppliers) {
			super.onPostExecute(suppliers);
				callbackService(suppliers);
			Asycdialog.dismiss();

		}
		
		private void callbackService(List<Supplier> suppliers){
			filterCargoActivity.callbackGetListSuppliers(suppliers);
		}
		
	}//end asyn task