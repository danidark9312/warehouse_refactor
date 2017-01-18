package com.lotrading.controlwhapp;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

import com.lotrading.controlwhapp.control.ControlApp;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import static com.lotrading.controlwhapp.config.IConstants.*;

public class HomeActivity extends AbstractActivity implements OnClickListener {

    ImageButton btnCargoReceipt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE); //landscape force
        initComponents();
        initListener();
        loadInfoEmployee(); //load info employee view
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.action_logout:
                ControlApp.getInstance().getControlSession().loginOut(this);
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    protected void initComponents() {
        btnCargoReceipt = (ImageButton) findViewById(R.id.btnCargoReceipt);

    }

    protected void initListener() {
        btnCargoReceipt.setOnClickListener(this);
    }

    private void loadInfoEmployee() {
            String nameEmployee = ControlApp.getInstance().getControlSession().getEmployeeSession().getCompleteName();
            String loginEmployee = ControlApp.getInstance().getControlSession().getEmployeeSession().getLogin();
            String emailEmployee = ControlApp.getInstance().getControlSession().getEmployeeSession().getEmail();
            String dateLoginEmployee = ControlApp.getInstance().getControlSession().getEmployeeSession().getDateTimeLogin();

            TextView txtNameEmploye = (TextView) findViewById(R.id.lblHomeNameEmployee);
            txtNameEmploye.setText(nameEmployee);

            TextView txtLoginEmploye = (TextView) findViewById(R.id.lblHomeLoginEmployee);
            txtLoginEmploye.setText(loginEmployee);

            TextView txtEmailEmploye = (TextView) findViewById(R.id.lblHomeEmailEmployee);
            txtEmailEmploye.setText(emailEmployee);

            TextView txtDateLoginEmploye = (TextView) findViewById(R.id.lblHomeDateLoginEmployee);
            txtDateLoginEmploye.setText(dateLoginEmployee);

            //load image employee (server location)
            loadPhotoEmployee();
            }

    private void loadPhotoEmployee() {
        if (ControlApp.getInstance().getControlSession().getEmployeeSession().getImage() == null) {
            //invoke asyn task load imagen url
            LoadImages loadImageAsy = new LoadImages();
            loadImageAsy.execute();
        } else {
            drawPhotoEmployee(); //imagen has been created
        }
    }

    private void drawPhotoEmployee() {
        Bitmap image = ControlApp.getInstance().getControlSession().getEmployeeSession().getImage();
        ImageView imgPhotoProfile = (ImageView) findViewById(R.id.imageViewHomePhotoEmployee);
        imgPhotoProfile.setImageBitmap(image);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnCargoReceipt:
                Intent intent = new Intent(this, FilterCargoActivity.class);
                startActivity(intent);
                break;
        }
    }


    /* load imagen url */
    private class LoadImages extends AsyncTask<String, Void, Bitmap> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Bitmap doInBackground(String... params) {
            Bitmap imagen = null;
            URL imageUrl = null;
            HttpURLConnection conn = null;
            try {
                String uriImage = ControlApp.getInstance().getControlSession().getEmployeeSession().getUrlImage();
                imageUrl = new URL(uriImage);
                conn = (HttpURLConnection) imageUrl.openConnection();
                conn.setConnectTimeout(2 * 1000);
                conn.connect();
                imagen = BitmapFactory.decodeStream(conn.getInputStream());
            } catch (IOException e) {
                Log.e(ExceptionTags.EXCECUTION_ERROR, ExceptionMessage.LOADING_IMAGE_ERROR);
            }
            return imagen;


        }

        @Override
        protected void onPostExecute(Bitmap result) {
            super.onPostExecute(result);
            if (result != null) {
                ControlApp.getInstance().getControlSession().getEmployeeSession().setImage(result);
            }
            drawPhotoEmployee();
        }

    }
}
