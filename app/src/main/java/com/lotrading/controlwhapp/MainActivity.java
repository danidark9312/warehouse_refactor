package com.lotrading.controlwhapp;

import org.json.JSONException;
import org.json.JSONObject;

import com.lotrading.controlwhapp.config.IConstants;
import com.lotrading.controlwhapp.control.ControlApp;
import com.lotrading.controlwhapp.model.ModelEmployee;
import com.lotrading.controlwhapp.model.ModelService;
import com.lotrading.controlwhapp.model.UserLogin;
import com.lotrading.controlwhapp.service.LoginServices;
import com.lotrading.controlwhapp.service.LoginServicesImpl;
import com.lotrading.controlwhapp.servicesError.RepositoryError;
import com.lotrading.controlwhapp.servicesError.RepositoryMapper;
import com.lotrading.controlwhapp.utilities.CallWebService;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.ProgressDialog;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import retrofit.RetrofitError;

import static com.lotrading.controlwhapp.R.id.txtPassword;
import static com.lotrading.controlwhapp.config.IConstants.*;

public class MainActivity extends AbstractActivity implements OnClickListener {

	TextView lblResponseLogin;
	Button btnSend;
	private EditText etUsername;
	private EditText etPassword;
	private LoginServices loginServices;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN); //force hide keyboard
		/*
		initComponents();
		initListener();
		*/
		authUserLocalSaved(); //read file auth
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		authUserLocalSaved(); //read file auth
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	protected void initComponents() {
		lblResponseLogin = (TextView) findViewById(R.id.lblResponseLogin);
		btnSend = (Button) findViewById(R.id.btnLogin);
		etUsername = (EditText)findViewById(R.id.txtUsername);
		etPassword = (EditText)findViewById(R.id.txtPassword);
	}

	protected void initListener() {
		btnSend.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btnLogin:
			String username = (etUsername).getText().toString();
			String password = (etPassword).getText().toString();
			if(username.equals("") || password.equals("")){
				return;
			}
			checkLogin(username, password);
			break;
		}
	}
	
	
	private void authUserLocalSaved() {
		try {
			String strJson = ControlApp.getInstance().getControlSession().readFileAuth(this);
			JSONObject json = new JSONObject(strJson);
			String username = json.getString("login");
			String password = json.getString("pass");
			((EditText) findViewById(R.id.txtUsername)).setText(username);
			((EditText) findViewById(txtPassword)).setText(password);
		} catch (JSONException e) {
			Log.i(ExceptionTags.EXCECUTION_ERROR, ExceptionMessage.JSON_PARSE);
		}
	}

	
	//login user app
	private void checkLogin(String username, String password) {
		try{
			String service = "ServiceUser";
			String [][] params = {{"operation", "login"},{"user", username},{"password", password}};
			String callback = "callbackLogin";
			String loading = "Authenticating";
			ModelService objService = new ModelService(service, params, callback, loading);
			Log.d("login","login cal");

			TaskAsynCallService callServiceTask = new TaskAsynCallService(LoginServicesImpl.getServicesInstance(),new UserLogin(username,password));
			callServiceTask.execute(objService);

		}catch(Exception e){
			Log.e("MainActivity", "Error call service Login", e);
			Toast.makeText(MainActivity.this,"Error call service Login", Toast.LENGTH_SHORT).show();
		}
	}
	
	private void callbackLogin(UserLogin userLogin){
		try{

				this.lblResponseLogin.setText("Welcome");
				
				//creating employee session login in

				String id = userLogin.getId();
				String email = userLogin.getEmail();
				String name = userLogin.getEmail();
				String lastname = userLogin.getLastname();
				String login = userLogin.getLogin();

			    ModelEmployee employeeSession = new ModelEmployee(id, name, lastname, email, login, true);
			    ControlApp.getInstance().getControlSession().loginIn(this, employeeSession, null);


				//changing to activity home


				Intent intent = new Intent(this, HomeActivity.class);
				startActivity(intent);


			/*}else{
				this.lblResponseLogin.setText("Username or Password is incorrect.");
			}*/

		}catch(Exception e){
			Log.e("MainActivity", "Error parsing json response Login", e);
		}

	}


	private class TaskAsynCallService extends AsyncTask<ModelService, Integer, UserLogin> {

		private ProgressDialog Asycdialog = new ProgressDialog(MainActivity.this);
		private JSONObject respJSON;
		private String serviceCallback;
		private LoginServices loginServices;
		RepositoryError repositoryError = null;
		private UserLogin userLogin;

		public TaskAsynCallService(LoginServices loginServices,UserLogin userLogin) {
			this.userLogin = userLogin;
			this.loginServices = loginServices;
		}

		@Override
		protected void onPreExecute() {
			// set message of the dialog
			Asycdialog.setMessage(getString(R.string.title_loading));
			Asycdialog.setCancelable(false);
			Asycdialog.setCanceledOnTouchOutside(false);

			// show dialog
			Asycdialog.show();
			super.onPreExecute();
		}

		protected UserLogin doInBackground(ModelService... listModelService) {
			boolean result = Boolean.TRUE;
			Log.d("back","lgin background");
			UserLogin login = null;
			try {
				login = loginServices.login(userLogin);

				/*ModelService objService = listModelService[0]; // object service
				Asycdialog.setMessage(objService.getLoadingMessage());
				this.serviceCallback = objService.getCallback();
				CallWebService callWebService = new CallWebService();
				*/
				// respJSON = new JSONObject(callWebService.callWebServiceExecute(

				result = Boolean.TRUE;


			}catch(RetrofitError er){
				repositoryError = RepositoryMapper.convertRetrofitErrorToRepositoryError(er);

			}
			catch (Exception ex) {
				Log.e(ExceptionTags.EXCECUTION_ERROR, ExceptionTags.EXCECUTION_ERROR, ex);
				result = Boolean.FALSE;
				Toast.makeText(MainActivity.this, R.string.text_connection_error, Toast.LENGTH_SHORT).show();
			}
			return login;
		}


		protected void onPostExecute(UserLogin result) {
			if (repositoryError!=null) {
				if(repositoryError.getErrorId() == IConstants.ERROR_UNAUTHORIZED_CODE){
					Toast.makeText(MainActivity.this, R.string.text_user_password_error, Toast.LENGTH_SHORT).show();
				}else{
					Toast.makeText(MainActivity.this, R.string.text_connection_error, Toast.LENGTH_SHORT).show();
				}
			} else {
				callbackService(result);
			}
			Asycdialog.dismiss();
			super.onPostExecute(result);
		}

		private void callbackService(UserLogin result) {
				callbackLogin(result);
		}

	}// end asyn task

}
