package com.lotrading.controlwhapp;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.CoreProtocolPNames;
import org.apache.http.util.EntityUtils;

import com.lotrading.controlwhapp.AsyncTask.UploadPhotoAsyncTask;
import com.lotrading.controlwhapp.control.ControlApp;
import com.lotrading.controlwhapp.model.ModelFileUpload;

import com.lotrading.controlwhapp.config.Config;
import com.lotrading.controlwhapp.service.WarehouseServicesImpl;

import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class TakePhotoActivity extends Activity {
	
	private int TAKE_PICTURE = 1; //action take photo
	private String photoPath;
	private ImageView mImageView;
	private TextView mTextViewListPhotos;
	private TextView mTextViewListDocs;
	private enum TypePhoto { PHOTO, DOC };
	private TypePhoto currentTypePhoto;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_take_photo);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE); //landscape force
		initComponents();
		initEvents();
		
		repaintListPhotos();//repaint photos saved
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		repaintListPhotos();//repaint photos saved
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.take_photo, menu);
		return true;
	}

	private void repaintListPhotos() {
		try{
			ArrayList<ModelFileUpload> listPhotos = ControlApp.getInstance().getControlWhReceipt().getModelWhReceipt().getListPhotosWh();
			ArrayList<ModelFileUpload> listDocuments = ControlApp.getInstance().getControlWhReceipt().getModelWhReceipt().getListDocsWh();
			
			String strListPhotos = "";
			for (ModelFileUpload modelFileUpload : listPhotos) {
				strListPhotos += modelFileUpload.getNameFile() + "\n";
			}
			
			String strListDocs = "";
			for (ModelFileUpload modelFileUpload : listDocuments) {
				strListDocs += modelFileUpload.getNameFile() + "\n";
			}
			
			//set textviews
			mTextViewListPhotos.setText(strListPhotos);
			mTextViewListDocs.setText(strListDocs);
			
		}catch(Exception e){
			Log.e("TakePhoto", "err in method repaintListPhotos ", e);
		}
	}

	private void initComponents() {
		try{
			mImageView = (ImageView) findViewById(R.id.imagePreviewPhoto);
			
			mTextViewListPhotos = (TextView) findViewById(R.id.textViewListPhotosResumeTakePhoto);
			mTextViewListDocs = (TextView) findViewById(R.id.textViewListDocumentsResumeTakePhoto);
			
		}catch(Exception e){
			Log.e("TakePhoto", "err in method initEvents ", e);
		}
	}

	@SuppressLint("UseValueOf")
	private void initEvents() {
		try{
			Button btnCameraTakePhoto = (Button) findViewById(R.id.buttonTakePhotoLaunch);
			btnCameraTakePhoto.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View arg0) {
					currentTypePhoto = TypePhoto.PHOTO;
					takePhoto();
				}
			});
			
			Button btnCameraTakePhotoDocument = (Button) findViewById(R.id.buttonTakePhotoDocumentLaunch);
			btnCameraTakePhotoDocument.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View arg0) {
					currentTypePhoto = TypePhoto.DOC;
					takePhoto();
				}
			});
			
			Button btnContinueWhReceipt = (Button) findViewById(R.id.buttonTakePhotoContinueWhReceipt);
			btnContinueWhReceipt.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View arg0) {
					continueWhReceiptActivity();
				}
			});
			
		}catch(Exception e){
			Log.e("TakePhoto", "err in method initEvents ", e);
		}
	}
	
	private void continueWhReceiptActivity() {
		try{
			/*Intent intent = new Intent(this, WReceiptLOActivity.class);
			startActivity(intent);*/
			finish();
		}catch(Exception e){
			Log.e("TakePhoto", "err in method continueWhReceiptActivity ", e);
		}
	}
	
	
	private void takePhoto(){
		try{
			
			//set path photo
			String whNumber = ControlApp.getInstance().getControlWhReceipt().getModelWhReceipt().getWhReceiptNumber();
			String seq = "";
			if(currentTypePhoto == TypePhoto.PHOTO){
				seq = "photo_" + (int)(ControlApp.getInstance().getControlWhReceipt().getModelWhReceipt().getListPhotosWh().size() + 1);
			}else{
				seq = "doc_" + (int)(ControlApp.getInstance().getControlWhReceipt().getModelWhReceipt().getListDocsWh().size() + 1);
			}
			photoPath = Environment.getExternalStorageDirectory() + "/Pictures/"+ whNumber + "_" + seq +".jpg";
			
			//call intent take photo
			Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
			Uri output = Uri.fromFile(new File(photoPath));
	        intent.putExtra(MediaStore.EXTRA_OUTPUT, output);
	        startActivityForResult(intent, TAKE_PICTURE);
	        
		}catch(Exception e){
			Log.e("TakePhoto", "err in method takePhoto ", e);
			Toast.makeText(TakePhotoActivity.this, "error " + e, Toast.LENGTH_LONG).show();
		}
	}
	
	@Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		try{
			if (resultCode == Activity.RESULT_OK) {
				
		        File file = new File(photoPath);
		        if (file.exists()) {
		        	
		        	ModelFileUpload fileUpload = new ModelFileUpload(file.getName(), file.getAbsolutePath()); //create object file

		        	//add file to wh recipt
		        	if(currentTypePhoto == TypePhoto.PHOTO){
		        		ControlApp.getInstance().getControlWhReceipt().getModelWhReceipt().addPhotoToList(fileUpload);
		        	}else{
		        		ControlApp.getInstance().getControlWhReceipt().getModelWhReceipt().addDocToList(fileUpload);
		        	}
		        	
		        	setPic(file.getAbsolutePath());//show preview image
		        	sendToServer(file);
		        	repaintListPhotos();//repaint list
		        }else{
		            Toast.makeText(getApplicationContext(), "Photo was not taken", Toast.LENGTH_LONG).show();
				}
			}else{
				Toast.makeText(TakePhotoActivity.this, "Photo was canceled", Toast.LENGTH_LONG).show();
			}
		}catch(Exception e){
			Log.e("TakePhoto", "err in method onActivityResult ", e);
			Toast.makeText(TakePhotoActivity.this, "error activity: " + e, Toast.LENGTH_LONG).show();
		}
    }

	private void setPic(String mCurrentPhotoPath) {
		try{
		    // Get the dimensions of the View
		    int targetW = mImageView.getWidth();
		    int targetH = mImageView.getHeight();
		  
		    // Get the dimensions of the bitmap
		    BitmapFactory.Options bmOptions = new BitmapFactory.Options();
		    bmOptions.inJustDecodeBounds = true;
		    BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);
		    int photoW = bmOptions.outWidth;
		    int photoH = bmOptions.outHeight;
		  
		    // Determine how much to scale down the image
		    int scaleFactor = Math.min(photoW/targetW, photoH/targetH);
		  
		    // Decode the image file into a Bitmap sized to fill the View
		    bmOptions.inJustDecodeBounds = false;
		    bmOptions.inSampleSize = scaleFactor;
		    bmOptions.inPurgeable = true;
		  
		    Bitmap bitmap = BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);
		    mImageView.setImageBitmap(bitmap);
		}catch(Exception e){
			Log.e("TakePhoto", "err in method setPic ", e);
			Toast.makeText(TakePhotoActivity.this, "error setPic " + e, Toast.LENGTH_LONG).show();
		}
	}
	
	private void sendToServer(File file) {
		try{
			/*UploaderPhoto uploadPhotoTask = new UploaderPhoto();
			uploadPhotoTask.execute(file);*/
			new UploadPhotoAsyncTask(WarehouseServicesImpl.getServicesInstance(),this,file).execute();

		}catch(Exception e){
			Log.e("TakePhoto", "err in method setPic ", e);
			Toast.makeText(TakePhotoActivity.this, "error creating asy task in sendToServer " + e, Toast.LENGTH_LONG).show();
		}
	}


	private class UploaderPhoto extends AsyncTask<File, Void, Boolean> {

		private ProgressDialog Asycdialog = new ProgressDialog(TakePhotoActivity.this);
		
		@Override
		protected void onPreExecute() {
			// set message of the dialog
			Asycdialog.setMessage("Loading");
			Asycdialog.setCancelable(false);
			Asycdialog.setCanceledOnTouchOutside(false);
			Asycdialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			Asycdialog.show();// show dialog
			super.onPreExecute();
		}
		
		
		@SuppressWarnings("deprecation")
		@Override
		protected Boolean doInBackground(File... params) {
			try {
				
				HttpClient httpclient = new DefaultHttpClient();
	            httpclient.getParams().setParameter(CoreProtocolPNames.PROTOCOL_VERSION, HttpVersion.HTTP_1_1);

	            HttpPost httppost = new HttpPost(Config.SERVER_IP + "/ControlWarehouse/services/ServiceUploadImage.php");
	            File file = params[0];
	            
	            MultipartEntity multipartEntity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);  
	            multipartEntity.addPart("file", new FileBody(file));

	            httppost.setEntity(multipartEntity);
	            String result = (String)httpclient.execute(httppost, new FileUploadResponseHandler());
	            
				return true;
				
			} catch (Exception e) {
				Log.e("TakePhoto", "err call  UploaderPhoto ", e);
			}
			return false;
		}

		protected void onPostExecute(Boolean result) {
			try{
				super.onPostExecute(result);
				if(result == true){
					Toast.makeText(TakePhotoActivity.this, "Image Uploaded Succesully ", Toast.LENGTH_LONG).show();
				}else{
					Toast.makeText(TakePhotoActivity.this, "Error Uploading Image", Toast.LENGTH_LONG).show();
				}
				Asycdialog.dismiss();
			}catch(Exception e){
				Log.e("TakePhoto", "err onPostExecute ", e);
			}
			
		}
	}
	
	
	class FileUploadResponseHandler implements ResponseHandler {

	    @Override
	    public Object handleResponse(HttpResponse response) throws ClientProtocolException, IOException {
	        HttpEntity r_entity = response.getEntity();
	        String responseString = EntityUtils.toString(r_entity);
	        Log.d("UPLOAD", responseString);

	        return responseString;
	    }
	}

}
