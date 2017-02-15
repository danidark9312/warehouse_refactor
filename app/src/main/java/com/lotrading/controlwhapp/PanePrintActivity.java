package com.lotrading.controlwhapp;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.lotrading.controlwhapp.AsyncTask.MasterValuesAsyncTask;
import com.lotrading.controlwhapp.config.IConstants;
import com.lotrading.controlwhapp.control.ControlApp;
import com.lotrading.controlwhapp.model.MasterValuesResponse;
import com.lotrading.controlwhapp.model.ModelMasterValue;
import com.lotrading.controlwhapp.model.ModelService;
import com.lotrading.controlwhapp.model.ModelWhReceipt;
import com.lotrading.controlwhapp.service.GeneralServicesImpl;
import com.lotrading.controlwhapp.utilities.*;
import com.zebra.sdk.comm.Connection;
import com.zebra.sdk.comm.ConnectionException;
import com.zebra.sdk.comm.TcpConnection;
import com.zebra.sdk.device.ZebraIllegalArgumentException;
import com.zebra.sdk.graphics.internal.ZebraImageAndroid;
import com.zebra.sdk.printer.PrinterLanguage;
import com.zebra.sdk.printer.ZebraPrinter;
import com.zebra.sdk.printer.ZebraPrinterFactory;
import com.zebra.sdk.printer.ZebraPrinterLanguageUnknownException;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Looper;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class PanePrintActivity extends Activity {

	private Button continueWH;
	private ListView lvPrinters;
	private List<Map<String, String>> listPrinterMap = new ArrayList<Map<String,String>>();
	private ArrayList<Bitmap> listLabels  = new ArrayList<Bitmap>();
	private Button btnPrint;
	private String printerIP;
	private String printerPort;
	private TextView lblPrinterStatus;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_pane_print);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE); // landscape force
		initComponents();
		loadInfo();
		loadLabelsToServer();
		loadDataPrinters(); //load info printers to server
		
		//load extra data
		Bundle extras = getIntent().getExtras();
		if(extras!=null){
			Boolean back = extras.getBoolean("back");
			if(back){
				continueWH.setEnabled(true);
			}else{
				continueWH.setEnabled(false);
			}
		}
	}
	
	@Override
	public void onBackPressed() {
	    Toast.makeText(PanePrintActivity.this, IConstants.PrintActivityMessages.CANNOT_GO_BACK, Toast.LENGTH_LONG).show();
	}
	
	
	private void initComponents() {
		try {
			
			//status printer
			lblPrinterStatus = (TextView) findViewById(R.id.textViewInfoPrinterStatusValue);

			lvPrinters = (ListView) findViewById(R.id.listViewPrintersAvailable); // list view component
			lvPrinters.setOnItemClickListener(new AdapterView.OnItemClickListener() {
				public void onItemClick(AdapterView<?> parentAdapter, View view, int position, long id) {
					//
					TextView lblPrinterName = (TextView) findViewById(R.id.textViewInfoPrinterNameValue);
					TextView lblPrinterIP = (TextView) findViewById(R.id.textViewInfoPrinterIPValue);
					TextView lblPrinterPort = (TextView) findViewById(R.id.textViewInfoPrinterPortValue);
					
					//printer selected to print
					Map<String, String> item;
					if ((item = getItemListClicked(position)) != null) {
						lblPrinterName.setText(item.get("printer"));
						lblPrinterIP.setText(item.get("IP"));
						lblPrinterPort.setText(item.get("port"));
						lblPrinterStatus.setText(item.get("status"));
						
						/*
						 * set printer settings (important!!)
						 */
						printerIP = item.get("IP");
						printerPort = item.get("port");
								
						RelativeLayout rlPrinterInfo = (RelativeLayout) findViewById(R.id.relativeLayoutInfoPrinter); //relative layout resume
						rlPrinterInfo.setVisibility(View.VISIBLE); // show details printer
					}
					
				}

				private Map<String, String> getItemListClicked(int position) {
					try{
						Map<String, String> item = listPrinterMap.get(position);
						return item;
					}catch(Exception e){
						Toast.makeText(PanePrintActivity.this, "Error getting ID Item", Toast.LENGTH_SHORT).show();
					}
					return null;
				}
			});
			continueWH=(Button) findViewById(R.id.Button_continueWH);
			continueWH.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					Toast.makeText(PanePrintActivity.this, "Go back", Toast.LENGTH_LONG).show();
					Intent whIntent = new Intent().setClass(PanePrintActivity.this,WReceiptLOActivity.class);
					startActivity(whIntent);
				}
			});
			
			btnPrint = (Button) findViewById(R.id.buttonPanePrintPrintLabels);
			btnPrint.setEnabled(true);
			btnPrint.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					Toast.makeText(PanePrintActivity.this, "Print labels", Toast.LENGTH_LONG).show();
					printLabelEvent();
				}
			});
			
			Button btnPrintTest = (Button) findViewById(R.id.buttonPanePrintPrintLabelsTest);
			btnPrintTest.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					Toast.makeText(PanePrintActivity.this, "Print test", Toast.LENGTH_LONG).show();
					printerTestEvent();
				}
			}); 
			
			//finish wh receipt and go back to home activity
			Button btnGoBack = (Button) findViewById(R.id.buttonGoBackHomePrinterPane);
			btnGoBack.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					Intent takePhotoIntent = new Intent().setClass(PanePrintActivity.this, HomeActivity.class);
	                startActivity(takePhotoIntent);
				}
			});
		

		} catch (Exception ex) {
			Log.e("PanePrintActivity", "Error in method initComponents", ex);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.pane_print, menu);
		return true;
	}
	
	
	private void loadInfo(){
		try{
			TextView lblPrinterWhNumber = (TextView) findViewById(R.id.textViewWhNumberPrinterValue);
			TextView lblPrinterWhPO = (TextView) findViewById(R.id.textViewPOPrinterLabelValue);
			TextView lblPrinterWhLabels = (TextView) findViewById(R.id.textViewTotalLabelsPrinterValue);
			
			ModelWhReceipt currentWhr = ControlApp.getInstance().getControlWhReceipt().getModelWhReceipt();
			
			lblPrinterWhNumber.setText(currentWhr.getWhReceiptNumber() + "");
			lblPrinterWhPO.setText(currentWhr.getPo() + "");
			lblPrinterWhLabels.setText(currentWhr.getNumLabels() + " Labels");
			
		}catch(Exception ex){
			Log.e("PanePrintActivity", "Error in method loadInfo", ex);
		}
	}
	
	
	/*
	 * load data unit type
	 */
	private void loadDataPrinters(){
		try{
			if(ControlApp.getInstance().getControlListMasterValues().getListPrinters().size() == 0){
				new MasterValuesAsyncTask(IConstants.MasterValues.PRINTER, GeneralServicesImpl.getServicesInstance(), PanePrintActivity.this).execute();
			}else{
				createHashMapPrinterList(); //paint printers list
			}
		}catch(Exception ex){
			Log.e("WhReceiptLOActivity", "Error in method loadDataUnitTypes ", ex);
		}
	}
	
	public void callbackGetListPrinters(List<MasterValuesResponse> printers){
			for(MasterValuesResponse masterValuesResponse : printers){

				int masterId = Integer.parseInt(masterValuesResponse.getMasterId());
				int valueId = Integer.parseInt(masterValuesResponse.getValueId());
				String value = masterValuesResponse.getValue();
				String value2 = masterValuesResponse.getValue2();
				String value3 = masterValuesResponse.getValue3();
				
				ControlApp.getInstance().getControlListMasterValues().addPrinter(new ModelMasterValue(masterId, valueId, value, value2, value3));
			}
			createHashMapPrinterList();

	}
	
	
	private void createHashMapPrinterList(){
		try{
			
			ArrayList<ModelMasterValue> listPrinters = ControlApp.getInstance().getControlListMasterValues().getListPrinters();
			Log.i("WhReceiptLOActivity", "***************** total printers " + listPrinters.size());
			for (ModelMasterValue eachPrinter : listPrinters) {
				String dataObjectList[][] = {
						{"ID", eachPrinter.getValueId() +  ""},
						{"printer", eachPrinter.getValue() +  ""}, //text1 list
						{"data", "IP: "+ eachPrinter.getValue2() +"\nPort: " + eachPrinter.getValue3()}, //text2 list
						{"IP", eachPrinter.getValue2() +  ""},
						{"port", eachPrinter.getValue3() +  ""},
						{"status", "Active"}
				};
				listPrinterMap.add(createHashMap(dataObjectList));
			}
			
			loadAdapterListViewFilter();
			
		}catch(Exception ex){
			Log.e("PanePrintActivity", "Error in method createHashMapPrinterList", ex);
		}
	}
	
	//create hash map struct for list printers
	private Map<String, String> createHashMap(String[][] dataObjectList) {
		HashMap<String, String> hashMapCargo = new HashMap<String, String>(2);
		for (int i = 0; i < dataObjectList.length; i++) {
			hashMapCargo.put(dataObjectList[i][0], dataObjectList[i][1]);
		}
		return hashMapCargo;
	}
	
	// create adapter for list view
	private boolean loadAdapterListViewFilter() {
		try {
			SimpleAdapter simpleAdpt = new SimpleAdapter(this, listPrinterMap, android.R.layout.simple_list_item_2, new String[] {"printer", "data"}, new int[] { android.R.id.text1, android.R.id.text2 });
			lvPrinters.setAdapter(simpleAdpt);
			return true;
		} catch (Exception e) {
			return false;
		}
	}
	
	/**
	 * image process
	 * @author SEBASARIZ
	 *
	 */
	//get each label in server
	private void loadLabelsToServer() {
			ModelWhReceipt currentWhr = ControlApp.getInstance().getControlWhReceipt().getModelWhReceipt();
			String pathImageBase =  currentWhr.getUrlLabelBase() + currentWhr.getWhReceiptNumber() + "/";
			Log.e("pathImageBase","pathImageBase: "+pathImageBase);
			Log.e("Title", "Aqui esta todos los labels_ "+currentWhr.getNumLabels());
			for(int i = 0; i < currentWhr.getNumLabels(); i++){
				String urlLabel = pathImageBase +  (i + 1);
				Log.e("urlLabel","urlLabel: "+urlLabel);
				TaskLoadImage loadImageAsy = new TaskLoadImage();
				loadImageAsy.execute(urlLabel);
			}
	}
	
	private void addLabelInList(Bitmap labelBitmap) {
	if(labelBitmap.isRecycled()){
		Log.d("addLabelInList", "reciclada");
	}else{
		Log.d("addLabelInList", "no reciclada");
	}

	listLabels.add(labelBitmap);
	Log.i("PanePrintActivity", "label loaded " + labelBitmap.getWidth());
	}
	
	/* load imagen url */
	private class TaskLoadImage extends AsyncTask<String, Void, Bitmap>{
		
		private ProgressDialog Asycdialog = new ProgressDialog(PanePrintActivity.this);
		
	    @Override
	    protected void onPreExecute() {
	    	super.onPreExecute();
	    	Asycdialog.setMessage("Loading Labels");
			Asycdialog.setCancelable(false);
			Asycdialog.setCanceledOnTouchOutside(false);
			Asycdialog.show();// show dialog
	    }
	 
	    @Override
	    protected Bitmap doInBackground(String... params) {
	        try{
	        	URL imageUrl = null;
				HttpURLConnection conn = null;
				String uriImage = params[0];
				imageUrl = new URL(uriImage);
				conn = (HttpURLConnection) imageUrl.openConnection();
				conn.connect();
				BitmapFactory.Options options = new BitmapFactory.Options();
				options.inPreferredConfig=Config.RGB_565;
				Bitmap imagen = BitmapFactory.decodeStream(conn.getInputStream(),null,options);
				Log.d("bitmap","2create decodestream");
				conn.disconnect();
				if(imagen.isRecycled()) {

					Log.d("bitmap","reciclada");
				}else {
					Log.d("bitmap","no reciclada");
				}
				return imagen;
	        }catch(Exception ex){
	        	Log.e("HomeActivity", "Error calling TaskLoadImage Asyn task", ex);
	        }
	        return null;
	    }
	     
	    @Override
	    protected void onPostExecute(Bitmap result) {
	        super.onPostExecute(result);
	        if(result != null){
	        	addLabelInList(result);
	        }else{
	        	Log.e("PanePrintActivity", "Error bitmal null");
	        	addLabelInList(null);
	        }
	        Asycdialog.dismiss();
	    }

	}//end asyn task
	
	/*
	 * Printer settings connect and print
	 */
	
	private Connection printerConnection;
	private ZebraPrinter printer;
	
	private void printerTestEvent(){
		try{
			new Thread(new Runnable() {
                public void run() {
                    Looper.prepare();
                    doConnectionTest();
                    Looper.loop();
                    Looper.myLooper().quit();
                }
            }).start();
		}catch(Exception e){
			Log.e("PanePrintActivity", "Error in method  printerTestEvent");
		}
	}
	
	public ZebraPrinter connect() {
		Log.i("PanePrintActivity", "********Connecting...");
		printerConnection = null;
			int port = Integer.parseInt(printerPort);
			printerConnection = new TcpConnection(printerIP, port);
		try {
			printerConnection.open();
			Log.i("PanePrintActivity", "********Connected");
		} catch (ConnectionException e) {
			Log.i("PanePrintActivity", "********Comm Error! Disconnecting");
			DemoSleeper.sleep(1000);
			disconnect();
		}

		ZebraPrinter printer = null;

		if (printerConnection.isConnected()) {
			try {
				printer = ZebraPrinterFactory.getInstance(printerConnection);
				Log.i("PanePrintActivity","********Determining Printer Language");
				PrinterLanguage pl = printer.getPrinterControlLanguage();
				Log.i("PanePrintActivity", "********Printer Language: " + pl);
			} catch (ConnectionException e) {
				Log.i("PanePrintActivity", "********Unknown Printer Language");
				printer = null;
				DemoSleeper.sleep(1000);
				disconnect();
			} catch (ZebraPrinterLanguageUnknownException e) {
				Log.i("PanePrintActivity", "********Unknown Printer Language");
				printer = null;
				DemoSleeper.sleep(1000);
				disconnect();
			}
		}

		return printer;
	}
	
	private void doConnectionTest() {
        printer = connect();
        if (printer != null) {
            sendTestLabel();
        } else {
            disconnect();
        }
    }
	
	private void sendTestLabel() {
        try {
            byte[] configLabel = getConfigLabel();
            printerConnection.write(configLabel);
            DemoSleeper.sleep(1500);
        } catch (ConnectionException e) {
        	Toast.makeText(PanePrintActivity.this, "Exception send test label", Toast.LENGTH_LONG).show();
        } finally {
            disconnect();
        }
    }
	
	private byte[] getConfigLabel() {
        PrinterLanguage printerLanguage = printer.getPrinterControlLanguage();

        byte[] configLabel = null;
        if (printerLanguage == PrinterLanguage.ZPL) {
            configLabel = "^XA^FO17,16^GB379,371,8^FS^FT65,255^A0N,135,134^FDTEST^FS^XZ".getBytes();
        } else if (printerLanguage == PrinterLanguage.CPCL) {
            String cpclConfigLabel = "! 0 200 200 406 1\r\n" + "ON-FEED IGNORE\r\n" + "BOX 20 20 380 380 8\r\n" + "T 0 6 137 177 TEST\r\n" + "PRINT\r\n";
            configLabel = cpclConfigLabel.getBytes();
        }
        return configLabel;
    }
	
	public void disconnect() {
        try {
            if (printerConnection != null) {
                printerConnection.close();
            }
            Log.i("PanePrintActivity", "********Not Connected - Inactive");
        } catch (ConnectionException e) {
        	Log.i("PanePrintActivity", "********COMM Error! Disconnected");
        } finally {
            
        }
    }
	
	/*
	 * print image label 
	 */
	
	private void printLabelEvent(){
		Log.d("print","print label");
		try{
			for (int i=0;listLabels.size()>i ;i++) {
				Bitmap bitmap =listLabels.get(i);
				if(bitmap != null){
					if(bitmap.isRecycled()){
						Log.d("recycled 2","recicladaaaa");
					}else{
						Log.d("recycled 2","no recicladaaa");
					}
					Bitmap bitmapR = rotate(bitmap);
					bitmap.recycle();
					bitmap=null;
					listLabels.set(i, bitmapR);
				}
			}

			printLabelBitmap();
		}catch(Exception e){
			Log.e("PanePrintActivity", "err in method printLabelEvent");
		}
	}

	private void printLabelBitmap() {
			new Thread(new Runnable() {
				
	            public void run() {
	                try {
	                	Looper.prepare();
	                	for(Bitmap bitmap:listLabels){
	                	if(bitmap.isRecycled()){
	                		Log.d("recycled","Siii reciclada");
	                	}else{
	                		Log.d("recycled","no y el problema es de SDK");
	                	}
	                    //create printer connection
	                    int port = Integer.parseInt(printerPort);
	                    Log.d("printer","ip: "+printerIP+", port:"+port);
	            		printerConnection = new TcpConnection(printerIP, port);
	                    Connection connection = printerConnection;
	                    connection.open();
	                    ZebraPrinter printer = ZebraPrinterFactory.getInstance(connection);
	                    printer.printImage(new ZebraImageAndroid(bitmap), 0, 15, 800, 1200, false);
	                    connection.close();
	                    bitmap.recycle(); 
	                    Thread.sleep(300); 
	                	}
	                } catch (ConnectionException e) { 
	                	Log.e("PanePrintActivity", "Error in ConnectionException" + e);
	                } catch (ZebraPrinterLanguageUnknownException e) {
	                	Log.e("PanePrintActivity", "Error in ZebraPrinterLanguageUnknownException" + e);
	                } catch (InterruptedException e) {
						e.printStackTrace();
					} finally {
	                	Log.d("recicle","cambio");
	                    Looper.myLooper().quit();
	                    
	                    
	                }
	            }
	        }).start(); 
			btnPrint.setEnabled(false);
		    }
	
	private Bitmap rotate(Bitmap bitmapOriginal){
		try{
			Matrix matrix = new Matrix();
			matrix.postRotate(90);
			Bitmap rotated = Bitmap.createBitmap(bitmapOriginal, 0, 0,  bitmapOriginal.getWidth(), bitmapOriginal.getHeight(),  matrix, true);
			bitmapOriginal.recycle();
			return rotated;
		}catch(Exception e){
			return bitmapOriginal;
		}
	}

	
}
