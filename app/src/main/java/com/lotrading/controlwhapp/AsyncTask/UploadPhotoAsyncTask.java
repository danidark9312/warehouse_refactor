package com.lotrading.controlwhapp.AsyncTask;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.widget.Toast;

import com.lotrading.controlwhapp.TakePhotoActivity;
import com.lotrading.controlwhapp.model.UploadImageResponse;
import com.lotrading.controlwhapp.model.Warehouse;
import com.lotrading.controlwhapp.service.WarehouseServices;
import com.lotrading.controlwhapp.servicesError.RepositoryError;
import com.lotrading.controlwhapp.servicesError.RepositoryMapper;

import java.io.File;

import retrofit.RetrofitError;

public class UploadPhotoAsyncTask extends AsyncTask<String, Integer, Boolean> {



		private ProgressDialog Asycdialog = null;

		TakePhotoActivity takephotoActivity = null;

		RepositoryError repositoryError = null;
		WarehouseServices warehouseServices = null;
		private Warehouse warehouseResult;
		private File file;


	public UploadPhotoAsyncTask(WarehouseServices warehouseServices, TakePhotoActivity takePhotoActivity, File file) {
			this.file = file;
			this.warehouseServices = warehouseServices;
			this.takephotoActivity = takePhotoActivity;
		}

		@Override
        protected void onPreExecute() {
            //set message of the dialog
			Asycdialog = new ProgressDialog(this.takephotoActivity);
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
				UploadImageResponse photo = warehouseServices.uploadImage(file, "", "photo");
				result = photo.isUpload();

			} catch(RetrofitError er) {
				repositoryError = RepositoryMapper.convertRetrofitErrorToRepositoryError(er);
			}
			return result;
		}

		protected void onPostExecute(Boolean result) {
			super.onPostExecute(result);
			if(result == true){
				Toast.makeText(takephotoActivity, "Image Uploaded Succesully ", Toast.LENGTH_LONG).show();
			}else{
				Toast.makeText(takephotoActivity, "Error Uploading Image", Toast.LENGTH_LONG).show();
			}
			Asycdialog.dismiss();		}
		

		
	}//end asyn task