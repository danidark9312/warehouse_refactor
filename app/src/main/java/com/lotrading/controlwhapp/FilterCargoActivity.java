package com.lotrading.controlwhapp;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.lotrading.controlwhapp.AsyncTask.ClientAsyncTask;
import com.lotrading.controlwhapp.AsyncTask.CreateWarehouseAsyncTask;
import com.lotrading.controlwhapp.AsyncTask.POListAsyncTask;
import com.lotrading.controlwhapp.AsyncTask.SupplierAsyncTask;
import com.lotrading.controlwhapp.control.ControlApp;
import com.lotrading.controlwhapp.control.ControlListPartners;
import com.lotrading.controlwhapp.model.Client;
import com.lotrading.controlwhapp.model.ModelClient;
import com.lotrading.controlwhapp.model.ModelItemIndustrialPurchase;
import com.lotrading.controlwhapp.model.ModelItemRawMaterials;
import com.lotrading.controlwhapp.model.ModelService;
import com.lotrading.controlwhapp.model.ModelSupplier;
import com.lotrading.controlwhapp.model.ModelWhReceipt;
import com.lotrading.controlwhapp.model.Po;
import com.lotrading.controlwhapp.model.Supplier;
import com.lotrading.controlwhapp.model.Warehouse;
import com.lotrading.controlwhapp.model.WarehouseItem;
import com.lotrading.controlwhapp.service.GeneralServicesImpl;
import com.lotrading.controlwhapp.service.WarehouseServices;
import com.lotrading.controlwhapp.service.WarehouseServicesImpl;
import com.lotrading.controlwhapp.servicesError.RepositoryError;
import com.lotrading.controlwhapp.servicesError.RepositoryMapper;
import com.lotrading.controlwhapp.utilities.CallWebService;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import retrofit.RetrofitError;

public class FilterCargoActivity extends AbstractActivity implements OnClickListener{
	
	
	private List<Map<String, String>> cargoListMap = new ArrayList<Map<String,String>>();
	private ListView lvResultsWhReceipt;
	private RelativeLayout rlResume;
	private EditText txtPO;
	private EditText txtTracking;
	private AutoCompleteTextView txtAutoClient;
	private AutoCompleteTextView txtAutoSupplier;
	private TextView lblDepResume;
	private TextView lblPOResume;
	private TextView lblIDResume;
	private TextView lblSupplierResume;
	private TextView lblRepResume;
	private TextView lblClientResume;
	private Button btnFilter;
	private Button btnLaunchReceipt;
	private Button btnClearFilter;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_filter_cargo);
		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN); //force hide keyboard
	}



	@Override
	protected void initComponents() {
		lblDepResume = (TextView) findViewById(R.id.lblDepResume);
		lblPOResume = (TextView) findViewById(R.id.lblPOResume);
		lblIDResume = (TextView) findViewById(R.id.lblIDResume);
		lblClientResume = (TextView) findViewById(R.id.lblClientResume);
		lblSupplierResume = (TextView) findViewById(R.id.lblSupplierResume);
		lblRepResume = (TextView) findViewById(R.id.lblRepResume);
		txtAutoSupplier = (AutoCompleteTextView) findViewById(R.id.txtSupplierFilter);//auto complete component
		txtAutoClient = (AutoCompleteTextView) findViewById(R.id.txtClientFilter);//auto complete component
		rlResume = (RelativeLayout) findViewById(R.id.relativeLayoutResume); //relative layout resume
		lvResultsWhReceipt = (ListView) findViewById(R.id.listCargoFilter); //list view component
		txtPO = (EditText) findViewById(R.id.txtFilterPO);
		txtTracking = (EditText) findViewById(R.id.txtFilterTracking);
		btnFilter = (Button) findViewById(R.id.btnFilterReceipt2);
		btnLaunchReceipt = (Button) findViewById(R.id.btnLaunchReceiptPane);
		btnClearFilter = (Button) findViewById(R.id.btnFilterReceiptClear);

	}

	protected void initListener() {

			btnFilter.setOnClickListener(this);
			btnLaunchReceipt.setOnClickListener(this);
			btnClearFilter.setOnClickListener(this);
			txtAutoClient.setOnItemClickListener(getClienteAutoCompleteListener());
			txtAutoClient.addTextChangedListener(getTextWatcherCliente());
			txtAutoSupplier.setOnItemClickListener(getClickListenerForSupplierAutocomplete());
			txtAutoSupplier.addTextChangedListener(getTextWatcherSupplierAutocomplete());
			txtTracking.setOnKeyListener(getClickListenerForTracking());
			lvResultsWhReceipt.setOnItemClickListener(getItemClickListenerForListView());

	}



	@Override
	public void onClick(View v) { 
		try{
			switch (v.getId()) {
				case R.id.btnFilterReceipt2: 
					requestFilter();
					break;
				case R.id.btnLaunchReceiptPane:
					//create whreceipt in datatable and launch activity depending on dep 
					launchButtonWhReceipt();
					break;
				case R.id.btnFilterReceiptClear:
					//reset fields filter
					txtTracking.setText("");
					txtPO.setText("");
					txtAutoClient.setText("");
					txtAutoSupplier.setText("");
					break;
				default:
					break;
			}
		}catch(Exception e){
			Log.e("FilterActivity", "Error onClick events ", e);
		}
	}


	
	private void requestFilter(){
		try{ 
			rlResume.setVisibility(View.INVISIBLE); //hide details item list
			// TaskAsynCallService filterTaskService = new TaskAsynCallService();
			String PO = (String) (txtPO).getText().toString();
			String tracking = (String) (txtTracking).getText().toString();
			String clientID = getItemIdClientSelected((String) (txtAutoClient).getText().toString());
			String supplierID = getItemIdSupplierSelected((String) (txtAutoSupplier).getText().toString());
			
			String service = "ServiceWhReceipt";
			String [][] params = {{"operation", "findListWhReceipt"}, {"po", PO}, {"tracking", tracking}, {"idClient", clientID}, {"idSupplier", supplierID}};
			String callback = "callbackFindListWhReceipt";
			ModelService objService = new ModelService(service, params, callback);
			
			// filterTaskService.execute(objService);

			Po poSearch = new Po();
			poSearch.setPoNumber(PO);
			poSearch.setTracking(tracking);
			poSearch.setClientId(clientID);
			poSearch.setSupplierId(supplierID);

			POListAsyncTask poListAsyncTask = new POListAsyncTask(WarehouseServicesImpl.getServicesInstance(),this,poSearch);
			poListAsyncTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
			poListAsyncTask.execute();
		}catch(Exception e){
			Log.e("FilterActivity", "Error in method  requestFilter ", e);
		}
	}
	
	
	/*
	 * create warehouse receipt 
	 * call service create warehouse receipt
	 * return id whReceipt and dateReceipt (date timezone Miami)
	 */
	
	public void launchButtonWhReceipt(){
		try{
			int idDepatment = ControlApp.getInstance().getControlWhReceipt().getModelWhReceipt().getIdDep();
			//Toast.makeText(this, "idDepartment: "+idDepatment, Toast.LENGTH_LONG).show();
			String idEmployeeEntered = ControlApp.getInstance().getControlWhReceipt().getModelWhReceipt().getIdEmployeeEntered();
			if(idEmployeeEntered == "0"){
				sendToLogin();
			}else{
				callServiceCreateWarehouseReceipt();
			}

		}catch(Exception e){
			Log.e("FilterActivity", "Error in method  launchWhReceipt ", e);
		}
	}
	
	
	private void callServiceCreateWarehouseReceipt(){
		try{ 
			ModelWhReceipt whReceiptObj = ControlApp.getInstance().getControlWhReceipt().getModelWhReceipt();

			Warehouse warehouse = new Warehouse();

			warehouse.setIdPo(Integer.valueOf(whReceiptObj.getIdPODepartment()));
			warehouse.setIdDepartment(Integer.valueOf(String.valueOf(whReceiptObj.getIdDep())));
			warehouse.setPoNumber(whReceiptObj.getPo());
			warehouse.setIdSupplier(Integer.valueOf(String.valueOf(whReceiptObj.getIdSupplier())));
			warehouse.setIdClient(Integer.valueOf(String.valueOf(whReceiptObj.getIdClient())));
			warehouse.setIdEmployee(Integer.valueOf(whReceiptObj.getIdEmployeeEntered()));
			warehouse.setDateReceipt(whReceiptObj.getDateTimeReceived());
			warehouse.setIdGroup(Integer.valueOf(String.valueOf(whReceiptObj.getIdDep())));
			warehouse.setIdStatus(Integer.valueOf(String.valueOf(whReceiptObj.getStatus())));

			String service = "ServiceWhReceipt"; 
			//Log.e("Departamento","ID: "+whReceiptObj.getIdDep());
			String [][] params = {
						{"operation", "createWarehouseReceipt"}, 
						{"idPo", whReceiptObj.getIdPODepartment() + ""},
						{"idDepartment", whReceiptObj.getIdDep() + ""},
						{"po", whReceiptObj.getPo()},
						{"idSupplier", whReceiptObj.getIdSupplier() + ""},
						{"idClient", whReceiptObj.getIdClient() + ""},
						{"idEmployee", whReceiptObj.getIdEmployeeEntered() + ""},
						{"dateReceived", whReceiptObj.getDateTimeReceived()},
						{"idGroup", whReceiptObj.getIdDep() + ""},
						{"idStatus", whReceiptObj.getStatus() + ""}
					};


			String callback = "callbackCreateWarehouseReceipt";
			String loadingMessage = "Creating Warehouse Receipt";
			
			//ModelService objService = new ModelService(service, params, callback, loadingMessage);
			
			//TaskAsynCallService callServiceTask = new TaskAsynCallService();
			CreateWarehouseAsyncTask createWarehouseAsyncTask = new CreateWarehouseAsyncTask(WarehouseServicesImpl.getServicesInstance(),this,warehouse);
			createWarehouseAsyncTask.execute();
		}catch(Exception e){
			Log.e("FilterActivity", "Error in method  callServiceCreateWarehouseReceipt ", e);
		}
	}
	
	//callback service create warehouse receipt in database
	public void callbackCreateWarehouseReceipt(Warehouse warehouse){
		try{

			String idWhReceipt = warehouse.getWhReceiptId();


			String whReceiptNumber= warehouse.getWhReceiptNumber();
			if(!idWhReceipt.equals("0")){ //was created successfully
				String dateTimeReceived = warehouse.getDateReceipt();
				ControlApp.getInstance().getControlWhReceipt().getModelWhReceipt().setIdWhReceipt(idWhReceipt);
				ControlApp.getInstance().getControlWhReceipt().getModelWhReceipt().setWhReceiptNumber(whReceiptNumber);
				ControlApp.getInstance().getControlWhReceipt().getModelWhReceipt().setDateTimeReceived(dateTimeReceived);
				
				//check department id and launch activity 
				int idDepatment = ControlApp.getInstance().getControlWhReceipt().getModelWhReceipt().getIdDep();
				List<WarehouseItem> wareHouseItems = null;
				wareHouseItems = warehouse.getWareHouseItems();
				switch (idDepatment) {
				case 2://rm
					for(WarehouseItem warehouseItem : wareHouseItems){
						int idItem = (warehouseItem.getIdItem());
						int itemNumber = Integer.parseInt(warehouseItem.getItemNumber());
						double quantity = warehouseItem.getQuantity();
						String productName = warehouseItem.getProductName();
						String productCode = String.valueOf(warehouseItem.getIdProduct());
						int orderToPlaceItemId = Integer.parseInt(warehouseItem.getOrderToPlaceItemId());
						String unit = warehouseItem.getUnitType();
						boolean isHazardous = warehouseItem.getHazardaous()=="1";
						boolean isUrgent = warehouseItem.getUrgent()=="1";
						Log.e("isHazardous", String.valueOf(isHazardous));
						ModelItemRawMaterials itemRawMaterial = new ModelItemRawMaterials(idItem, itemNumber, productName, productCode, quantity, orderToPlaceItemId, unit,isHazardous);
						itemRawMaterial.setUrgent(isUrgent);
						ControlApp.getInstance().getControlWhReceipt().getModelWhReceipt().addItemRawMaterials(itemRawMaterial);
					}
					Intent intentRM = new Intent(this, WReceiptLOActivity.class);
					startActivity(intentRM);
					break;
				case 3: //ip  

					for(WarehouseItem warehouseItem : wareHouseItems){
						int idItem = (warehouseItem.getIdItem());
						int itemNumber = Integer.parseInt(warehouseItem.getItemNumber());
						double quantity = warehouseItem.getQuantity();
						String productClientDesciption = warehouseItem.getClientDescription();
						String productClientRef = warehouseItem.getClientRef();
						String productSupplierDescription = warehouseItem.getSupplierDescription();
						String productSupplierRef = warehouseItem.getSupplierRef();
						String idUnitType = String.valueOf(warehouseItem.getIdUnitType());
						String unitType = warehouseItem.getUnitType();
						String idProduct = (warehouseItem.getIdProduct());
						String productName = warehouseItem.getProductName();
						String manufacturerRef = warehouseItem.getManufacturerRef();
						double qtyArrived = warehouseItem.getQuantityArrived();
						//create each object for show popup in next activity
						Log.d("item data", "itemNumber: "+itemNumber+" supplierDesc:."+productSupplierDescription+" dupplier reference: "+
								productSupplierRef+"client descripcion:. "+productClientDesciption+" client reference: "+productClientRef+"qty: "+
										quantity+" unit:"+unitType+" qtyArr:."+qtyArrived);
						
						ModelItemIndustrialPurchase itemPurchaseOrder = new ModelItemIndustrialPurchase(idItem, itemNumber, (int)quantity, productClientDesciption, productClientRef, productSupplierDescription, productSupplierRef, Integer.valueOf(idUnitType), unitType, idProduct, productName, manufacturerRef, qtyArrived);
						ControlApp.getInstance().getControlWhReceipt().getModelWhReceipt().addItemIndustrialPurchase(itemPurchaseOrder);
					}
					Intent intentIP = new Intent(this, WReceiptLOActivity.class);
					startActivity(intentIP);
					
					break;
				case 4:
					Intent intentLO = new Intent(this, WReceiptLOActivity.class);
					startActivity(intentLO);
					break;
				default:
					break;
				}
			}else{
				if(warehouse.getIdEmployee()!=null){
					Toast.makeText(this, "The PO is already open by: "+ warehouse.getIdEmployee()+". wait until is released" , Toast.LENGTH_LONG).show();
				}
			}


		}catch(Exception e){
			Log.e("FilterActivity", "Error in method  callbackFindListWhReceipt (Parsing JSON Object) ", e);
		}
	}
	
	/*
	 * fill and load info clients for auto-complete field
	 * 
	 */
	private void loadDataAutoClient(){
		try{
			ArrayList<String> arrayAdapter = new ArrayList<String>();
			
			ArrayList<ModelClient> listClients = ControlApp.getInstance().getControlListPartners().getListClients();
			for (ModelClient eachClient : listClients) {
				arrayAdapter.add(eachClient.getCode() + " - " + eachClient.getName());
			}
			loadAdapterAutoCompleteClient(arrayAdapter);
		}catch(Exception e){
			Log.e("FilterActivity", "Error get list clients ", e);
		}
	}


	
	//adapter array android.
	private void loadAdapterAutoCompleteClient(ArrayList<String> listClient){
			ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, listClient);

			txtAutoClient.setAdapter(adapter);

			adapter.notifyDataSetChanged();
			txtAutoClient.showDropDown();


	}


				//in list clients get id by code client
			private String getItemIdClientSelected(String code){
				try{
					ModelClient client = ControlApp.getInstance().getControlListPartners().findClientIdByCode(code);
					if(client != null){
						return client.getIdClient()+ "";
			}
		}catch(Exception e){
			Log.e("FilterActivity", "Error get id client by code ", e);
		}
		return "";
	}
	
	
	/*
	 * fill and load info supplier for auto-complete field
	 * 
	 */
	private void loadDataAutoSupplier(){
		try{
			ArrayList<String> arrayAdapter = new ArrayList<String>();
			
			ArrayList<ModelSupplier> listSuppliers = ControlApp.getInstance().getControlListPartners().getListSuppliers();
			for (ModelSupplier eachSupplier : listSuppliers) {
				arrayAdapter.add(eachSupplier.getName());
			}
			loadAdapterAutoCompleteSupplier(arrayAdapter);
		}catch(Exception e){
			Log.e("FilterActivity", "Error get list suppliers ", e);
		}
	}
	
	private void loadAdapterAutoCompleteSupplier(ArrayList<String> listSupplier){
			if(listSupplier == null)
				listSupplier = new ArrayList<String>();
			ListAdapter adapter = txtAutoSupplier.getAdapter();
			if(adapter == null){
				adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, listSupplier);
				txtAutoSupplier.setAdapter((ArrayAdapter<String>)adapter);
			}
			ArrayAdapter arrayAdapter = (ArrayAdapter) adapter;
			arrayAdapter.clear();
			arrayAdapter.addAll(listSupplier);
			arrayAdapter.notifyDataSetChanged();
			txtAutoSupplier.showDropDown();

	}

	
	//in list suppliers get id by name
	private String getItemIdSupplierSelected(String name){
		try{
			ModelSupplier supplier =  ControlApp.getInstance().getControlListPartners().findSupplierIdByName(name);
			if(supplier != null){
				return supplier.getIdSupplier() + "";
			}
		}catch(Exception e){
			Log.e("FilterActivity", "Error get id supplier by name ", e);
		}
		return "";
	}
	
	/*
	 * list filter receipts
	 * 
	 */
	//create adapter for list view
	private boolean loadAdapterListViewFilter(){
		try{
			SimpleAdapter simpleAdpt = new SimpleAdapter(this, cargoListMap, android.R.layout.simple_list_item_2, new String[] {"cargo", "data"}, new int[] {android.R.id.text1, android.R.id.text2});
			lvResultsWhReceipt.setAdapter(simpleAdpt);
			return true;
		}catch(Exception e){
			return false;
		}
	}
	
	//get map for item clicked
	private Map<String, String> getItemListClicked(int position){
		try{
			Map<String, String> item = cargoListMap.get(position);
			return item;
		}catch(Exception e){
			Toast.makeText(FilterCargoActivity.this, "Error getting ID Item", Toast.LENGTH_SHORT).show();
		}
		return null;
	}
	
	//fill information list receipts
	public void callbackFindListWhReceipt(List<Po> poList){
		try{
			
			cargoListMap = new ArrayList<Map<String,String>>();


			for(Po eachWHReceipt : poList){


				String id = eachWHReceipt.getId();
				String idno="" ;
				if(eachWHReceipt.getIdno()!=null && eachWHReceipt.getIdno().trim()!=""){
					idno= eachWHReceipt.getIdno();
				}

				String po = eachWHReceipt.getPoNumber();
				String supplierId = eachWHReceipt.getSupplierId();
				String supplierName = eachWHReceipt.getSupplierName();
				String clientId = eachWHReceipt.getClientId();
				String clientName = eachWHReceipt.getClientName();
				String tracking = "";
				String tmpTracking = eachWHReceipt.getTracking();
				if(tmpTracking!=null && tmpTracking.trim()!=""){
					tracking=tmpTracking;
				}
				String rep = eachWHReceipt.getRep();

				String dataObjectList[][] = {
						{"ID", id},
						{"IDNo", idno},
						{"cargo", "PO # " + po}, //text1 list
						{"data", "Client: " + clientName + "\nSupplier: " + supplierName}, //text2 list
						{"po", po},
						{"supplierId", supplierId},
						{"supplier", supplierName},
						{"clientId", clientId},
						{"client", clientName},
						{"tracking", tracking},
						{"rep", rep},
						{"department", eachWHReceipt.getDepartamentoByCodigo()},
						{"idDeparment", eachWHReceipt.getIdDepartment() +
								""}
						//obtener el departamento

				};
				cargoListMap.add(createHashMap(dataObjectList));



				//show total results
				TextView totalResults = (TextView) findViewById(R.id.lblResumeTotalQueries);
				totalResults.setText(" " + cargoListMap.size()); //total results for list
				if(cargoListMap.size() > 0){ //hide keyboard
					hideKeyboard(FilterCargoActivity.this);
				}

			}
			Log.e("size:","size: "+poList.size());
			//Toast.makeText(FilterCargoActivity.this,"OK JSON Parsing", Toast.LENGTH_SHORT).show();
			loadAdapterListViewFilter();
		}catch(Exception ex){
			Toast.makeText(FilterCargoActivity.this,"Error parsing JSON", Toast.LENGTH_SHORT).show();
			Log.e("FilterCargoActivity", "Error parsing JSON in method callbackFindListWhReceipt", ex);
		}
	}
	
	//create hash map struct for list receipts
	private Map<String, String> createHashMap(String[][] dataObjectList) {
		HashMap<String, String> hashMapCargo = new HashMap<String, String>(2);
		for (int i = 0; i < dataObjectList.length; i++) {
			hashMapCargo.put(dataObjectList[i][0], dataObjectList[i][1]);
		}
		return hashMapCargo;
	}
	
	
	/*
	 * load data suppliers
	 * @author JAS
	 */
	
	private void callServiceGetSuppliers(String nameSupplier){
		try{
			//if(ControlApp.getInstance().getControlListPartners().getListSuppliers().size() == 0){
				String service = "ServicePartner";
				String [][] params = {{"operation", "getListSuppliers"}, {"nameSupplier", nameSupplier}};
				String callback = "callbackGetListSuppliers";
				String loadingMessage = "Loading Suppliers";
				
				ModelService objService = new ModelService(service, params, callback, loadingMessage);

			SupplierAsyncTask supplierAsyncTask = new SupplierAsyncTask(GeneralServicesImpl.getServicesInstance(),this);
			// supplierAsyncTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
			supplierAsyncTask.execute(nameSupplier);

				/*TaskAsynCallService callServiceTask = new TaskAsynCallService();
				callServiceTask.execute(objService);
				*/

			//}
		}catch(Exception ex){
			Log.e("HomeActivity", "Error in method callServiceGetSuppliers ", ex);
		}
	}
	
	public void callbackGetListSuppliers(List<Supplier> suppliers){
			ControlListPartners controlListPartners = ControlApp.getInstance().getControlListPartners();
			for(Supplier supp : suppliers){
				int idSupplier = Integer.parseInt(supp.getIdPartner());
				String nameSupplier = supp.getNamePartner();
				controlListPartners.addSupplier(new ModelSupplier(idSupplier, nameSupplier));
			}
			loadDataAutoSupplier();
	}
	
	/*
	 * load data clients
	 */
	private void callServiceGetClients(String codeClient, String nameClient){
		try{
			//if(ControlApp.getInstance().getControlListPartners().getListClients().size() == 0){
				String service = "ServicePartner";
				String [][] params = {{"operation", "getListClients"}, {"codeClient", codeClient}, {"nameClient", nameClient}};
				String callback = "callbackGetListClients";
				String loadingMessage = "Loading Clients";
				
				ModelService objService = new ModelService(service, params, callback, loadingMessage);
				
				/*TaskAsynCallService callServiceTask = new TaskAsynCallService();
				callServiceTask.execute(objService);
				*/
			 ClientAsyncTask clientAsyncTask = new ClientAsyncTask(GeneralServicesImpl.getServicesInstance(),this);
			 clientAsyncTask.execute(nameClient);
			//}
		}catch(Exception ex){
			Log.e("HomeActivity", "Error in method callServiceGetClients ", ex);
		}
	}
	
	public void callbackGetListClients(List<Client> clients){


			ControlApp.getInstance().getControlListPartners().clearListClients(); //important clear arraylist

			for(Client client:clients){

				int idClient = Integer.parseInt(client.getIdPartner());//eachClientResponse.getInt("id_partner");
				String codeClient = client.getCodePartner();//eachClientResponse.getString("code_partner");
				String nameClient = client.getNamePartner();//eachClientResponse.getString("name_partner");
				
				ControlApp.getInstance().getControlListPartners().addClient(new ModelClient(idClient, codeClient, nameClient));
			}
			//set clients list adapter with results
			loadDataAutoClient();

	}
	
	
	/*
	 * call web services asynchronous task
	 * 
	 */
	//asyn task
	private class TaskAsynCallService extends AsyncTask<ModelService, Integer, Boolean> {
		
		//dialog = ProgressDialog.show(FilterCargoActivity.this, "", "Loading...", true);
		private ProgressDialog Asycdialog = new ProgressDialog(FilterCargoActivity.this);
		private JSONObject respJSON;
		private String serviceCallback;
		RepositoryError repositoryError = null;
		Po poSearch = null;



		public TaskAsynCallService() {

		}

		@Override
        protected void onPreExecute() {
            //set message of the dialog
            Asycdialog.setMessage("Loading");
            Asycdialog.setCancelable(false);
			Asycdialog.setCanceledOnTouchOutside(false);
            //show dialog
            Asycdialog.show();
            super.onPreExecute();
        }
		
		protected Boolean doInBackground(ModelService... listModelService) {
			boolean result = true;
			String response = "";
			try {
				ModelService objService = listModelService[0]; //object service
				this.serviceCallback = objService.getCallback();
				CallWebService callWebService = new CallWebService();
				response = callWebService.callWebServiceExecute(objService.getService(), objService.getParams());
				Log.d("json1: ",response);
				respJSON = new JSONObject(response);

				result = true;
				
			} catch(RetrofitError er){
				repositoryError = RepositoryMapper.convertRetrofitErrorToRepositoryError(er);

			} catch (JSONException e) {
				e.printStackTrace();
			}
			return result;
		}

		protected void onPostExecute(Boolean result) {
			//response async
			if(result){
				callbackService();
			}else{
				Toast.makeText(FilterCargoActivity.this,"Error calling operation web service", Toast.LENGTH_SHORT).show();
			}
			Asycdialog.dismiss();
		}
		
		private void callbackService(){
			if(serviceCallback.equals("callbackFindListWhReceipt")){
				Log.d("callbackFindList", "call back find list receipt 728");
				// callbackFindListWhReceipt(respJSON); //load info JSON response
			}else if(serviceCallback.equals("callbackCreateWarehouseReceipt")){
				// Log.d("callbackCreateWarehouseReceipt", "callbackCreateWarehouseReceipt 732");
				// callbackCreateWarehouseReceipt(respJSON);
			}else if(serviceCallback.equals("callbackGetListClients")){
				//callbackGetListClients(respJSON);
			}else if(serviceCallback.equals("callbackGetListSuppliers")){
				//callbackGetListSuppliers(respJSON);
			}
		}
		
	}//end asyn task
	



	private OnItemClickListener getClienteAutoCompleteListener(){
		return new OnItemClickListener() {
			@Override
			public void onItemClick (AdapterView<?> parent, View view, int position, long id) {
				String itemSelected = (String) parent.getItemAtPosition(position);
				String codeItem = (itemSelected.split("-")[0]).trim();
				txtAutoClient.setText(codeItem);
			}
		};
	}

	private TextWatcher getTextWatcherCliente(){
		return new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				String currentText = s.toString();
				if(currentText.length() == 2 && !currentText.equals("") && before == 0){ //call service only if length textclient = 2
					callServiceGetClients(currentText, currentText); //use the same arguments
				}
			}
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			}
			@Override
			public void afterTextChanged(Editable s) {
			}
		};
	}

	public OnItemClickListener getClickListenerForSupplierAutocomplete(){
		return new OnItemClickListener() {
			@Override
			public void onItemClick (AdapterView<?> parent, View view, int position, long id) {
				Log.v("filterActivity", "selected item client: id=" + id + " pos=" + position + " parent=" + parent.getItemAtPosition(position));
				String itemSelected = ((String) parent.getItemAtPosition(position)).trim();
				txtAutoSupplier.setText(itemSelected);
			}
		};
	}

	private TextWatcher getTextWatcherSupplierAutocomplete(){
		return new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				String currentText = s.toString();
				if(currentText.length() == 2 && !currentText.equals("") && before == 0){ //call service only if length textsupplier = 2
					callServiceGetSuppliers(currentText); //use the same arguments
				}
			}
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			}
			@Override
			public void afterTextChanged(Editable s) {
			}
		};
	}

	private View.OnKeyListener getClickListenerForTracking(){
		return new View.OnKeyListener() {
			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
					requestFilter();
					return true;
				}
				return false;
			}
		};
	}

	private AdapterView.OnItemClickListener getItemClickListenerForListView(){
		return new AdapterView.OnItemClickListener() {
			public void onItemClick(AdapterView<?> parentAdapter, View view, int position, long id) {
				Map<String, String> item;
				if ((item = getItemListClicked(position)) != null) {
					lblDepResume.setText(item.get("department"));
					lblPOResume.setText(item.get("po"));
					lblIDResume.setText(item.get("IDNo"));
					lblClientResume.setText(item.get("client"));
					lblSupplierResume.setText(item.get("supplier"));
					lblRepResume.setText(item.get("rep"));
					rlResume.setVisibility(View.VISIBLE); // show details item list

					int clientidd =0;
					if(!item.get("clientId").equals("null")){
						clientidd=Integer.parseInt(item.get("clientId"));
					}
					ControlApp.getInstance().getControlWhReceipt().createNewWhReceipt(item.get("ID"), item.get("IDNo"), item.get("po"), item.get("rep"),
							clientidd, item.get("client"), Integer.parseInt(item.get("supplierId")),
							item.get("supplier"), Integer.parseInt(item.get("idDeparment")), item.get("department"));
				}
			}
		};
	}
	

	private void sendToLogin(){
		Intent intent = new Intent(this,MainActivity.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
		startActivity(intent);
		finish();
	};
}

