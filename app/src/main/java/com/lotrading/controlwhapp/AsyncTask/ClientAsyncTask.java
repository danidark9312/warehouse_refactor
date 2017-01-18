package com.lotrading.controlwhapp.AsyncTask;

import android.app.ProgressDialog;
import android.os.AsyncTask;

import com.lotrading.controlwhapp.FilterCargoActivity;
import com.lotrading.controlwhapp.model.Client;
import com.lotrading.controlwhapp.model.Po;
import com.lotrading.controlwhapp.model.Supplier;
import com.lotrading.controlwhapp.service.GeneralServices;
import com.lotrading.controlwhapp.servicesError.RepositoryError;
import com.lotrading.controlwhapp.servicesError.RepositoryMapper;

import java.util.List;

import retrofit.RetrofitError;

public class ClientAsyncTask extends AsyncTask<String, Integer, List<Client>> {

	//dialog = ProgressDialog.show(FilterCargoActivity.this, "", "Loading...", true);
		private ProgressDialog Asycdialog = null;

		FilterCargoActivity filterCargoActivity = null;
		RepositoryError repositoryError = null;
		GeneralServices generalServices = null;

	public ClientAsyncTask(GeneralServices generalServices, FilterCargoActivity cargoActivity) {
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
		protected List<Client> doInBackground(String... listModelService) {
			List<Client> clients = null;

			try {
				clients = generalServices.getListClients(listModelService[0]);
			} catch(RetrofitError er) {
				repositoryError = RepositoryMapper.convertRetrofitErrorToRepositoryError(er);
			}
			return clients;
		}

		protected void onPostExecute(List<Client> clients) {
			super.onPostExecute(clients);
				callbackService(clients);
			Asycdialog.dismiss();

		}
		
		private void callbackService(List<Client> clients){
			filterCargoActivity.callbackGetListClients(clients);
		}
		
	}//end asyn task