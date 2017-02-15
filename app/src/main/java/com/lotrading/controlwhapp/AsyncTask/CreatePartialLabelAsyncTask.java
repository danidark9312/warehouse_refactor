package com.lotrading.controlwhapp.AsyncTask;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.widget.Toast;

import com.lotrading.controlwhapp.WReceiptLOActivity;
import com.lotrading.controlwhapp.model.Warehouse;
import com.lotrading.controlwhapp.service.WarehouseServices;
import com.lotrading.controlwhapp.servicesError.RepositoryError;
import com.lotrading.controlwhapp.servicesError.RepositoryMapper;

import retrofit.RetrofitError;

public class CreatePartialLabelAsyncTask extends AsyncTask<String, Integer, Boolean> {

	   private final Warehouse warehouse;
	//dialog = ProgressDialog.show(FilterCargoActivity.this, "", "Loading...", true);
		private ProgressDialog Asycdialog = null;

		WReceiptLOActivity wReceiptLOActivity = null;
		private String serviceCallback;
		RepositoryError repositoryError = null;
		WarehouseServices warehouseServices = null;
		private Warehouse warehouseResult;


	public CreatePartialLabelAsyncTask(WarehouseServices warehouseServices, WReceiptLOActivity wReceiptLOActivity, Warehouse warehouse) {
			this.warehouse = warehouse;
			this.warehouseServices = warehouseServices;
			this.wReceiptLOActivity = wReceiptLOActivity;
		}

		@Override
        protected void onPreExecute() {
            //set message of the dialog
			Asycdialog = new ProgressDialog(this.wReceiptLOActivity);
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
				result = warehouseServices.createPartialLabel(this.warehouse);

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
				Toast.makeText(wReceiptLOActivity,"Error calling operation web service", Toast.LENGTH_SHORT).show();
			}
			Asycdialog.dismiss();
			super.onPostExecute(result);
		}
		
		private void callbackService(){
			wReceiptLOActivity.callbackGetPartialLabels();
		}
		
	}//end asyn task