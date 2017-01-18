package com.lotrading.controlwhapp.utilities;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import com.lotrading.controlwhapp.config.Config;

import android.util.Log;

public class CallWebService {
	
	public String callWebServiceExecute(String service, String[][] params){
		
		try{
			HttpClient httpClient = new DefaultHttpClient();
			
			//path services softlot (important!!)
			Log.e("service call",Config.SERVER_IP  + "/ControlWarehouse/services/"+ service +".php");
			HttpPost post = new HttpPost(Config.SERVER_IP + "/ControlWarehouse/services/"+ service +".php");
	
			List<NameValuePair> paramsPost = new ArrayList<NameValuePair>();
			for (int i = 0; i < params.length; i++) {
				Log.d("param", "nombre: "+params[i][0]+" value: "+params[i][1]);
				paramsPost.add(new BasicNameValuePair(params[i][0], params[i][1]));
			}
	
			post.setEntity(new UrlEncodedFormEntity(paramsPost));
	
			HttpResponse resp = httpClient.execute(post);
			Log.d("response",resp.getStatusLine().getStatusCode()+"");
			String strResponse = EntityUtils.toString(resp.getEntity());
	
			return strResponse;
		}catch(Exception e){
			Log.e("CallWebService", "Error!", e);
			return "";
		}
	}
		
}
