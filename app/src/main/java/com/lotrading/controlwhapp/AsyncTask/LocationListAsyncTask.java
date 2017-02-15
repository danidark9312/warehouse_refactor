package com.lotrading.controlwhapp.AsyncTask;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.widget.Toast;

import com.lotrading.controlwhapp.WReceiptLOActivity;
import com.lotrading.controlwhapp.model.Location;
import com.lotrading.controlwhapp.service.GeneralServices;
import com.lotrading.controlwhapp.servicesError.RepositoryError;
import com.lotrading.controlwhapp.servicesError.RepositoryMapper;

import java.util.List;

import retrofit.RetrofitError;

public class LocationListAsyncTask extends AsyncTask<String, Integer, Boolean> {

		private ProgressDialog Asycdialog = null;
		WReceiptLOActivity filterCargoActivity = null;
		RepositoryError repositoryError = null;
		GeneralServices generalServices = null;
		List<Location> locations;
		int masterId;
		boolean isForPopup;

	public LocationListAsyncTask(GeneralServices generalServices, WReceiptLOActivity cargoActivity,boolean isForPopup) {
			this.generalServices = generalServices;
			this.filterCargoActivity = cargoActivity;
			this.isForPopup = isForPopup;

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
		protected Boolean doInBackground(String... string) {
			boolean result = true;

			try {

				locations = generalServices.getLocationList();
				result = (locations != null);

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
		if(isForPopup)
				filterCargoActivity.callbackGetListLocationsInflate(locations);
			else
				filterCargoActivity.callbackGetListLocations(locations);
		}
		
	}//end asyn task