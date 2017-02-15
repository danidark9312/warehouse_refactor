package com.lotrading.controlwhapp.AsyncTask;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.widget.Toast;

import com.lotrading.controlwhapp.PanePrintActivity;
import com.lotrading.controlwhapp.WReceiptLOActivity;
import com.lotrading.controlwhapp.model.MasterValuesResponse;
import com.lotrading.controlwhapp.service.GeneralServices;
import com.lotrading.controlwhapp.servicesError.RepositoryError;
import com.lotrading.controlwhapp.servicesError.RepositoryMapper;

import java.util.List;

import retrofit.RetrofitError;

public class MasterValuesAsyncTask extends AsyncTask<String, Integer, Boolean> {

		private ProgressDialog Asycdialog = null;
		//WReceiptLOActivity activity = null;
		Activity activity = null;
		RepositoryError repositoryError = null;
		GeneralServices generalServices = null;
		List<MasterValuesResponse> masterValues;
		int masterId;

	public MasterValuesAsyncTask(int masterId,GeneralServices generalServices, Activity cargoActivity) {
			this.generalServices = generalServices;
			this.activity = cargoActivity;
			this.masterId = masterId;
		}

		@Override
        protected void onPreExecute() {
            //set message of the dialog
			Asycdialog = new ProgressDialog(this.activity);
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
				masterValues = generalServices.getMasterValues(String.valueOf(masterId));
				result = (masterValues != null);

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
				Toast.makeText(activity,"Error calling operation web service", Toast.LENGTH_SHORT).show();
			}
			Asycdialog.dismiss();
			super.onPostExecute(result);
		}
		
		private void callbackService(){
			if(activity instanceof WReceiptLOActivity){
				((WReceiptLOActivity)activity).callbackGetListUnitType(masterValues);
			}else if(activity instanceof PanePrintActivity)
				((PanePrintActivity)activity).callbackGetListPrinters(masterValues);
		}
		
	}//end asyn task