package com.lotrading.controlwhapp;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.lotrading.controlwhapp.AsyncTask.LocationListAsyncTask;
import com.lotrading.controlwhapp.AsyncTask.MasterValuesAsyncTask;
import com.lotrading.controlwhapp.AsyncTask.TruckCompaniesAsyncTask;
import com.lotrading.controlwhapp.config.IConstants;
import com.lotrading.controlwhapp.control.ControlApp;
import com.lotrading.controlwhapp.control.ControlWhReceipt;
import com.lotrading.controlwhapp.exception.WeightException;
import com.lotrading.controlwhapp.model.Location;
import com.lotrading.controlwhapp.model.MasterValuesResponse;
import com.lotrading.controlwhapp.model.ModelCarrier;
import com.lotrading.controlwhapp.model.ModelItemIndustrialPurchase;
import com.lotrading.controlwhapp.model.ModelItemRawMaterials;
import com.lotrading.controlwhapp.model.ModelItemWhReceipt;
import com.lotrading.controlwhapp.model.ModelLocation;
import com.lotrading.controlwhapp.model.ModelLocationItemWh;
import com.lotrading.controlwhapp.model.ModelMasterValue;
import com.lotrading.controlwhapp.model.ModelService;
import com.lotrading.controlwhapp.model.ModelWhReceipt;
import com.lotrading.controlwhapp.model.TruckCompany;
import com.lotrading.controlwhapp.service.GeneralServicesImpl;
import com.lotrading.controlwhapp.utilities.CallWebService;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.ActionBar.LayoutParams;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.graphics.Typeface;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ListAdapter;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

public class WReceiptLOActivity extends Activity implements OnClickListener {

	// activity attributes
	private AutoCompleteTextView txtAutoTruckCompany;

	private enum TypeDialog {
		ADD, EDIT
	};

	private TypeDialog currentActionDialog;
	private int currentIdPostitionEditItemDialog;
	private TableLayout tableItemsWhLO;
	private TableLayout tableLocationsItemWhLO;
	private TableLayout tableLocationsItemWhLO_inflate;
	private TableLayout tableTrackingsItemWhLO;
	private TableLayout tableTrackingsItemWhLO_inflate;
	private TableLayout tableItemWhIP;
	private TableLayout tableItemWhRM;

	// dialog attributes
	// locations list, used in dialog wh receipt
	private ArrayList<ModelLocationItemWh> listLocationsTemp;
	private Spinner spinnerTypeUnit;
	private AutoCompleteTextView txtAutoLocations,txtAutoLocationsInflate;
	// tracking list, for dialog
	private ArrayList<String> listTrackingsTemp;

	private HashMap<RelativeLayout,ArrayList<String>> recordsTrackingList = new HashMap<RelativeLayout, ArrayList<String>>();
	private HashMap<RelativeLayout,ArrayList<ModelLocationItemWh>> recordsLocationList = new HashMap<RelativeLayout, ArrayList<ModelLocationItemWh>>();


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_wreceipt_lo);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE); // landscape
		// force
		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN); // force


		initComponents();
		initEvents();
		// load info wh receipt in layout
		loadInfoWhReceipt();
		// paint table items wh
		loadTableItemsWh();
		loadDataTruckCompany(); // data truck company
		loadDataUnitTypes(); // unit type data list (box, pallet, etc)table
		// master values database
		// showGUIDepartment(); this will be execute after loaddataunitTypes is
		// completed
	}

	private void showGUIDepartment() {
			int idDep = ControlApp.getInstance().getControlWhReceipt().getModelWhReceipt().getIdDep();
			if (idDep == 2) {
				boolean isSavedRM = ControlApp.getInstance().getControlWhReceipt().getModelWhReceipt().isSavedRM();
				if (isSavedRM == false) {
					createDialogRawMaterials();
				}
			} else if (idDep == 3) { // ip
				boolean isSavedIP = ControlApp.getInstance().getControlWhReceipt().getModelWhReceipt().isSavedIP();
				if (isSavedIP == false) {
					createDialogIndustrialPurcharse();
				}
			}


			}// else{ //lo }
	@Override
	protected void onResume() {
		super.onResume();
		loadInfoWhReceipt();
		loadTableItemsWh();
	}

	@Override
	public void onBackPressed() {
		// Do Here what ever you want do on back press;
		Toast.makeText(WReceiptLOActivity.this,
				"Can not go back with Wh receipt process active",
				Toast.LENGTH_LONG).show();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.wreceipt_lo, menu);
		return true;
	}

	// Assign info wh receipt to init fields
	private void loadInfoWhReceipt() {
			// create components
			TextView lblPOReceiptLO = (TextView) findViewById(R.id.lblPOReceiptLO);
			TextView lblIDWhReceiptLO = (TextView) findViewById(R.id.lblWHNReceiptLO);

			TextView lblDateReceiptLO = (TextView) findViewById(R.id.lblDateReceiptLO);
			TextView lblEnteredByReceiptLO = (TextView) findViewById(R.id.lblEnteredByReceiptLO);
			TextView lblClientReceiptLO = (TextView) findViewById(R.id.lblClientReceiptLO);
			TextView lblSupplierReceiptLO = (TextView) findViewById(R.id.lblSupplierReceiptLO);
			TextView lblStatusReceiptLO = (TextView) findViewById(R.id.lblStatusReceiptLO);

			EditText editTextHmPallets = (EditText) findViewById(R.id.editTextNumberPalletsHTLO);
			EditText editTextRemarks = (EditText) findViewById(R.id.editTextRemarksLO);

			// set data object wh to components
			ModelWhReceipt whReceiptObj = ControlApp.getInstance().getControlWhReceipt().getModelWhReceipt();
			lblPOReceiptLO.setText(whReceiptObj.getPo());
			lblIDWhReceiptLO.setText(whReceiptObj.getWhReceiptNumber());
			lblDateReceiptLO.setText(whReceiptObj.getDateTimeReceived());
			lblEnteredByReceiptLO.setText(whReceiptObj.getNameEmployeeEntered());
			lblClientReceiptLO.setText(whReceiptObj.getNameClient());
			lblSupplierReceiptLO.setText(whReceiptObj.getNameSupplier());
			lblStatusReceiptLO.setText(whReceiptObj.getNameStatus()); // ?????????????

			txtAutoTruckCompany.setText(whReceiptObj.getTruckCompany().getCarrier()); // if carrier is not exits, so put empty array

			if (whReceiptObj.getHtPallets() >= 0) {
				editTextHmPallets.setText(whReceiptObj.getHtPallets());
			}
			editTextRemarks.setText(whReceiptObj.getRemarks());


	}

	private void loadTableItemsWh() {

			ArrayList<ModelItemWhReceipt> listItemsWh = ControlApp
					.getInstance().getControlWhReceipt().getModelWhReceipt()
					.getListItemsWhLO();
			repaintTableItemsWh(getAdapterTableListItemsWhLO(listItemsWh));

	}

	private String[][] getAdapterTableListItemsWhLO(
			ArrayList<ModelItemWhReceipt> listItemsWhLO) {
		try {
			String[][] adapterTable = new String[listItemsWhLO.size()][12];
			for (int i = 0; i < listItemsWhLO.size(); i++) {
				adapterTable[i][0] = (listItemsWhLO.get(i).getHazmat() ? "YES": "NO");
				adapterTable[i][1] = listItemsWhLO.get(i).getnPieces() + "";
				adapterTable[i][2] = listItemsWhLO.get(i).getNameUnitType();
				adapterTable[i][3] = listItemsWhLO.get(i).getRelationIdRMItem()
						+ "";
				adapterTable[i][4] = listItemsWhLO.get(i).getLength() + "";
				adapterTable[i][5] = listItemsWhLO.get(i).getWidth() + "";
				adapterTable[i][6] = listItemsWhLO.get(i).getHeight() + "";
				adapterTable[i][7] = new DecimalFormat("#0.00").format(listItemsWhLO.get(i).getVolume()) + "";
				double weightLB = roundDecimal(listItemsWhLO.get(i).getWeigthLB(), 2);
				double weightKG = roundDecimal(listItemsWhLO.get(i).getWeigthKG(), 2);
				adapterTable[i][8] = weightLB + "";
				adapterTable[i][9] = weightKG + "";
				String location = "";

				/*Validamos si los locations fueron ingresados en el popup del inicio*/

				if (ControlApp.getInstance().getControlWhReceipt().getModelWhReceipt().getIdDep() == 2) {
					if(listItemsWhLO.get(i).getLocations() == null || listItemsWhLO.get(i).getLocations().size()==0){
                        ControlWhReceipt controlWhReceipt = ControlApp.getInstance().getControlWhReceipt();;
                        ModelItemRawMaterials modelItemRawMaterials = controlWhReceipt.getModelWhReceipt().getListItemsRawMaterials().get(listItemsWhLO.get(i).getRelationIdRMItem());
                        listItemsWhLO.get(i).setLocations(modelItemRawMaterials.getListLocations());
                    }
				}
				for (int j = 0; j < listItemsWhLO.get(i).getLocations().size(); j++) {
						location += listItemsWhLO.get(i).getLocations().get(j).getLabel()+ ",";
					}

				adapterTable[i][10] = location;
				adapterTable[i][11] = (listItemsWhLO.get(i).isCloned() ? "YES": "NO");
			}
			return adapterTable;
		} catch (Exception e) {
			Log.e("WhReceiptLOActivity",
					"Error creating in getAdapterTableListItemsWhLO", e);
		}
		return new String[listItemsWhLO.size()][12];
	}

	// repaint table
	private void repaintTableItemsWh(String dataTable[][]) {
		try {

			ModelWhReceipt currentWhReceipt = ControlApp.getInstance()
					.getControlWhReceipt().getModelWhReceipt();//

			String headerTableDataIPLO[] = { "Hazmat", "# Pieces", "Type",
					"Length", "Width", "Height", "Volume", "Weigth[Lb]",
					"Weigth[Kg]", "Location" };
			String headerTableDataRM[] = { "Hazmat", "# Pieces", "Type",
					"P. Code", "Length", "Width", "Height", "Volume",
					"Weigth[Lb]", "Weigth[Kg]", "Location" };

			String[] headerTableData = null;
			if (currentWhReceipt.getIdDep() == 1) { // rm
				headerTableData = headerTableDataRM;
			} else {
				headerTableData = headerTableDataIPLO;
			}

			// reset table
			tableItemsWhLO.removeAllViewsInLayout();

			/* TableLayout design layout */

			// header row table
			TableRow headerTable = new TableRow(this);
			headerTable.setLayoutParams(new TableLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
			tableItemsWhLO.addView(headerTable);


			for (int i = 0; i < headerTableData.length; i++) {
				TextView column = new TextView(this);
				column.setLayoutParams(new TableRow.LayoutParams(
						LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
				column.setText(headerTableData[i]);
				column.setTextSize(TypedValue.COMPLEX_UNIT_SP, 19);
				column.setTypeface(column.getTypeface(), Typeface.BOLD);
				column.setGravity(Gravity.CENTER_HORIZONTAL);
				column.setPadding(5, 5, 5, 5);
				headerTable.addView(column);

				tableItemsWhLO.setColumnStretchable(i, true);
				tableItemsWhLO.setColumnShrinkable(i, true);
			}

			// separator border headerTable in table
			TableRow borderHeaderTable = new TableRow(this);
			borderHeaderTable.setLayoutParams(new TableLayout.LayoutParams(
					LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
			FrameLayout lineHeaderTable = new FrameLayout(this);
			TableRow.LayoutParams lineHeaderTableParams = new TableRow.LayoutParams(
					LayoutParams.MATCH_PARENT, 2);
			lineHeaderTableParams.span = headerTableData.length + 1;
			lineHeaderTable.setBackgroundColor(Color.parseColor("#ff33b5e5"));
			borderHeaderTable.addView(lineHeaderTable, lineHeaderTableParams);
			tableItemsWhLO.addView(borderHeaderTable);

			// fill data table
			for (int i = 0; i < dataTable.length; i++) {
				TableRow eachRow = new TableRow(this);
				eachRow.setLayoutParams(new TableLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
				for (int j = 0; j <= 10 /* 10 fields */; j++) {

					TextView column = new TextView(this);
					column.setLayoutParams(new TableRow.LayoutParams(
							LayoutParams.WRAP_CONTENT,
							LayoutParams.WRAP_CONTENT));
					column.setTextSize(TypedValue.COMPLEX_UNIT_SP, 19);
					column.setGravity(Gravity.CENTER_HORIZONTAL);
					if (j != 3) {
						column.setText(dataTable[i][j]); // set info data table
						eachRow.addView(column);
					} else {
						if (currentWhReceipt.getIdDep() == 1 /* rm */
								&& !dataTable[i][3].equals("-1")) {
							int positionRelation = Integer.parseInt(dataTable[i][3]);
							ModelItemRawMaterials itemRMFather = currentWhReceipt.getListItemsRawMaterials().get(positionRelation);
							String idCodeProduct = itemRMFather.getProductRef();
							column.setText(idCodeProduct); // set info data
							// table
							eachRow.addView(column);
						}
					}
				}

				// check is cloned
				if (dataTable[i][11].equals("YES")) {
					eachRow.setBackgroundColor(Color.parseColor("#97D4FF")); // highligth
					// row
					// cloned
				}

				// create edit button and set event click
				Button buttonEditItem = new Button(this);

				// create listener for edit button
				OnClickListener listenerEdit = new OnClickListener() {
					@Override
					public void onClick(View v) {
						currentIdPostitionEditItemDialog = ((Integer) v
								.getTag()).intValue();
						// change type dialog action
						currentActionDialog = TypeDialog.EDIT;
						// create dialog edit
						createDialogAddEditItem();
					}
				};

				buttonEditItem.setOnClickListener(listenerEdit);
				buttonEditItem.setTag(i);
				buttonEditItem.setText("Edit");
				buttonEditItem.setBackgroundColor(Color.parseColor("#ff33b5e5"));
				buttonEditItem.setTextColor(Color.parseColor("#ffffff"));
				buttonEditItem.setTextSize(TypedValue.COMPLEX_UNIT_SP, 17);
				eachRow.addView(buttonEditItem); // add this button in each row

				// create remove button and set event click
				Button buttonRemoveItem = new Button(this);
				// create listener for remove button
				OnClickListener listenerRemove = new OnClickListener() {
					@Override
					public void onClick(View v) {
						int currentIdItem = ((Integer) v.getTag()).intValue();
						createDialogRemoveItem(currentIdItem);
					}
				};
				buttonRemoveItem.setOnClickListener(listenerRemove);
				buttonRemoveItem.setTag(i);
				buttonRemoveItem.setText("X");
				buttonRemoveItem.setBackgroundColor(Color.parseColor("#ffff4444"));
				buttonRemoveItem.setTextColor(Color.parseColor("#ffffff"));
				buttonRemoveItem.setTextSize(TypedValue.COMPLEX_UNIT_SP, 17);
				eachRow.addView(buttonRemoveItem); // add this button in each
				// row

				// create clone button and set event click
				Button buttonCloneItem = new Button(this);
				// create listener for remove button
				OnClickListener listenerClone = new OnClickListener() {
					@Override
					public void onClick(View v) {
						int currentIdItem = ((Integer) v.getTag()).intValue();
						createDialogCloneItem(currentIdItem);
					}
				};
				buttonCloneItem.setOnClickListener(listenerClone);
				buttonCloneItem.setTag(i);
				buttonCloneItem.setText("C");
				buttonCloneItem.setBackgroundColor(Color.parseColor("#19BE37"));
				buttonCloneItem.setTextColor(Color.parseColor("#ffffff"));
				buttonCloneItem.setTextSize(TypedValue.COMPLEX_UNIT_SP, 17);
				eachRow.addView(buttonCloneItem); // add this button in each row

				// add row in table, important!
				tableItemsWhLO.addView(eachRow);
			}
		} catch (Exception e) {
			Log.e("WhReceiptLOActivity", "err painting table items wh >>>>", e);
		}
	}

	private void initEvents() {
		try {
			Button btnAddWhItem = (Button) findViewById(R.id.btnAddItemWhLO);
			btnAddWhItem.setOnClickListener(this);

			Button btnTakePhotos = (Button) findViewById(R.id.btnTakePhotosLO);
			btnTakePhotos.setOnClickListener(this);

			Button btnTerminateWh = (Button) findViewById(R.id.buttonFinishWhReceiptLO);
			btnTerminateWh.setOnClickListener(this);


			Button partialLabelButton = (Button) findViewById(R.id.Button_partialLabel);
			partialLabelButton.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					createPartialDialogLabels();
				}
			});


			txtAutoTruckCompany = (AutoCompleteTextView) findViewById(R.id.txtAutoTruckCompany);// auto
			Button upsButton = (Button)findViewById(R.id.Button_ups);
			upsButton.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					txtAutoTruckCompany.setText("UPS GROUND");
					ModelCarrier truckObj = ControlApp.getInstance().getControlListCarrier().findTruckCompanyByName("UPS GROUND");
					if (truckObj != null) {
						ControlApp.getInstance().getControlWhReceipt().getModelWhReceipt().setTruckCompany(truckObj);
					}
				}
			});


			Button fedexButton = (Button)findViewById(R.id.Button_fedex);
			fedexButton.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					txtAutoTruckCompany.setText("FEDEX GROUND");
					ModelCarrier truckObj = ControlApp.getInstance()
							.getControlListCarrier()
							.findTruckCompanyByName("FEDEX GROUND");
					if (truckObj != null) {
						ControlApp.getInstance().getControlWhReceipt()
								.getModelWhReceipt()
								.setTruckCompany(truckObj);
					}
				}
			});

		} catch (Exception ex) {
			Log.e("WhReceiptLOActivity", "err in method initEvents ", ex);
		}
	}

	@Override
	public void onClick(View v) {
			switch (v.getId()) {
				case R.id.btnAddItemWhLO:
					// assign event add new item (dialog)
					currentActionDialog = TypeDialog.ADD;
					createDialogAddEditItem();
					break;
				case R.id.btnTakePhotosLO:
					// save state fields unsaved in activity
					String htPalletsStr = (String) ((EditText) findViewById(R.id.editTextNumberPalletsHTLO))
							.getText().toString();
					if (!htPalletsStr.equals("")) { // check empty number
						int htPallets = Integer.parseInt(htPalletsStr);
						ControlApp.getInstance().getControlWhReceipt().getModelWhReceipt().setHtPallets(htPallets);
					}
					String remarks = (String) ((EditText) findViewById(R.id.editTextRemarksLO)).getText().toString();
					ControlApp.getInstance().getControlWhReceipt().getModelWhReceipt().setRemarks(remarks);

					// launch activity take photo
					Intent takePhotoIntent = new Intent().setClass(WReceiptLOActivity.this, TakePhotoActivity.class);
					startActivity(takePhotoIntent);
					break;
				case R.id.buttonFinishWhReceiptLO:
					// finish wh
					// check data completed and items > 0
					if (ControlApp.getInstance().getControlWhReceipt().getModelWhReceipt().getTruckCompany().getIdCarrier() != 0
							&& ControlApp.getInstance().getControlWhReceipt().getModelWhReceipt().getListItemsWhLO().size() > 0
							) {
						// Check department is RM and at least one location to be entered for each item //
						if (ControlApp.getInstance().getControlWhReceipt().getModelWhReceipt().getIdDep() == 2 &&
								!validateLocations(ControlApp.getInstance().getControlWhReceipt().getModelWhReceipt().getListItemsWhLO())
								) {
							Toast toast = Toast.makeText(WReceiptLOActivity.this,
									"Some fields are required to finish warehouse receipt",
									Toast.LENGTH_SHORT);
							toast.setGravity(Gravity.TOP | Gravity.RIGHT, 10, 10);
							toast.show();
						} else {
							// save state fields unsaved in activity
							String htPalletsStrSave = (String) ((EditText) findViewById(R.id.editTextNumberPalletsHTLO)).getText().toString();
							if (!htPalletsStrSave.equals("")) { // check empty number
								int htPallets = Integer.parseInt(htPalletsStrSave);
								ControlApp.getInstance().getControlWhReceipt().getModelWhReceipt().setHtPallets(htPallets);
							}
							String remarksSave = (String) ((EditText) findViewById(R.id.editTextRemarksLO)).getText().toString();
							ControlApp.getInstance().getControlWhReceipt().getModelWhReceipt().setRemarks(remarksSave);

							// create confirm dialog

							createDialogConfirmTerminate(); // create dialog confirm
							// finish whr
						}
					} else {
						Toast toast = Toast
								.makeText(WReceiptLOActivity.this,
										"Some fields are required to finish warehouse receipt",
										Toast.LENGTH_SHORT);
						toast.setGravity(Gravity.TOP | Gravity.RIGHT, 10, 10);
						toast.show();
					}
			}
			}

	private boolean validateLocations(ArrayList<ModelItemWhReceipt> listItemsWhLO) {
		boolean isValid = true;
		for (ModelItemWhReceipt item : listItemsWhLO){
			if(item.getLocations().size() == 0) {
				isValid = false;
				break;
			}
		}
		return isValid;
	}

	// dialog confirm wh info
	private void createDialogConfirmTerminate() {
			// Use the Builder class for convenient dialog construction
			AlertDialog.Builder builder = new AlertDialog.Builder(this); // this
			// activity
			// class
			builder.setMessage(
					"Do you want to complete this Warehouse Receipt?")
					.setTitle("Warehouse Receipt")
					.setPositiveButton("Cancel",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
													int id) {
									// User cancelled the dialog
								}
							})
					.setNegativeButton("OK",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
													int id) {
									terminateProcessWhReceipt();
								}
							});
			// Create the AlertDialog object and return it
			AlertDialog dialogConfirm = builder.create();
			dialogConfirm.show();
	}

	// dialog confirm wh info
	private void createDialogConfirmPartialLabels() {

			// Use the Builder class for convenient dialog construction
			AlertDialog.Builder builder = new AlertDialog.Builder(this); // this
			// activity
			// class
			builder.setMessage(
					"Do you want to print partial labels?")
					.setTitle("Warehouse Labels")
					.setPositiveButton("Cancel",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
													int id) {
									// User cancelled the dialog
								}
							})
					.setNegativeButton("OK",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
													int id) {
									//crear el metdo de creacion de labels en el servidor
									ModelWhReceipt whReceipt = ControlApp.getInstance()
											.getControlWhReceipt().getModelWhReceipt();


									String service = "ServiceWhReceipt";
									String[][] params = {
											{ "operation", "createPartialLabels" },
											{ "idWhNumber", whReceipt.getWhReceiptNumber() },
											{ "idDepartment", whReceipt.getIdDep()+"" },
											{ "numeroLabels", ControlApp.getInstance().getControlWhReceipt().getModelWhReceipt().getNumLabels()+"" },
											{ "supplierName", whReceipt.getNameSupplier() },
											{ "clientName", whReceipt.getNameClient() },
											{ "po", whReceipt.getPo() }};
									String callback = "callbackGetPartialLabels";
									String loadingMessage = "Loading items RM";

									ModelService objService = new ModelService(service,
											params, callback, loadingMessage);

									TaskAsynCallService callServiceTask = new TaskAsynCallService();
									callServiceTask.execute(objService);



								}
							});
			// Create the AlertDialog object and return it
			AlertDialog dialogConfirm = builder.create();
			dialogConfirm.show();

	}

	private void createDialogRemoveItem(final int currentIdItem) {

			// Use the Builder class for convenient dialog construction
			AlertDialog.Builder builder = new AlertDialog.Builder(this); // this
			// activity
			// class
			builder.setMessage("Are you sure to remove this item?")
					.setTitle("Remove Item Warehouse Receipt")
					.setPositiveButton("Yes",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
													int id) {
									ControlApp
											.getInstance()
											.getControlWhReceipt()
											.getModelWhReceipt()
											.removeItemToListWhLo(currentIdItem);
									// repaint items table
									loadTableItemsWh();
								}
							})
					.setNegativeButton("No",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
													int id) {
									// User cancelled the dialog
								}
							});
			// Create the AlertDialog object and return it
			AlertDialog dialogConfirmRemoveItem = builder.create();
			dialogConfirmRemoveItem.show();
	}

	// dialog clone item
	private void createDialogCloneItem(final int currentIdItem) {
			// Use the Builder class for convenient dialog construction
			AlertDialog.Builder builder = new AlertDialog.Builder(this); // this
			// activity
			// class
			builder.setMessage("Are you sure to clone this item?")
					.setTitle("Clone Item Warehouse Receipt")
					.setPositiveButton("Yes",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
													int id) {
									ControlApp.getInstance()
											.getControlWhReceipt()
											.getModelWhReceipt()
											.cloneItemToListWhLo(currentIdItem);
									// repaint items table
									loadTableItemsWh();
								}
							})
					.setNegativeButton("No",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
													int id) {
									// User cancelled the dialog
								}
							});
			// Create the AlertDialog object and return it
			AlertDialog dialogConfirmCloneItem = builder.create();
			dialogConfirmCloneItem.show();

	}

	// dialog add items for RM
	private void createDialogRawMaterials() {
			// init dialog popup
			final Dialog dialog = new Dialog(WReceiptLOActivity.this);
			dialog.setContentView(R.layout.dialog_items_rm);
			dialog.setCanceledOnTouchOutside(false);
			dialog.getWindow().setLayout(dipToPixels(1260),
					LayoutParams.WRAP_CONTENT); //
			dialog.setTitle("Complete Items Raw Materials");
			dialog.show();

			// init table items
			tableItemWhRM = (TableLayout) dialog.findViewById(R.id.tableLayoutItemsListRMDialog);

			/*load info items in table IP*/

			loadTableItemsWhRM();

			// load data RM Dialog
			ModelWhReceipt whreceipt = ControlApp.getInstance().getControlWhReceipt().getModelWhReceipt(); // object wh
			// receipt
			TextView txtPONumberDialog = (TextView) dialog.findViewById(R.id.textViewPORMValueDialog);
			TextView txtSupplierNameDialog = (TextView) dialog.findViewById(R.id.textViewSupplierRMValueDialog);
			txtPONumberDialog.setText(whreceipt.getPo() + "");
			txtSupplierNameDialog.setText(whreceipt.getNameSupplier() + "");

			// save items wh receipt for RM
			Button dialogButtonContinueWhRM = (Button) dialog.findViewById(R.id.ButtonContinueRMReceiptDialog);
			dialogButtonContinueWhRM.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					saveItemsRM(dialog);
				}
			});

			// botton take photos for rm
			Button dialogButtonTakePicturesWhRM = (Button) dialog.findViewById(R.id.buttonTakePhotosRMDialog);
			dialogButtonTakePicturesWhRM.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View v) {
							// launch activity take photo
							Intent takePhotoIntent = new Intent().setClass(WReceiptLOActivity.this,TakePhotoActivity.class);
							startActivity(takePhotoIntent);
						}
					});

			// botton update items for rm
			Button dialogButtonUpdateItemsWhRM = (Button) dialog.findViewById(R.id.buttonUpdateItemsRMDialog);
			dialogButtonUpdateItemsWhRM.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View v) {
							String service = "ServiceWhReceipt";
							String[][] params = {
									{ "operation", "getInfoItemsPODepRM" },
									{
											"idPo",
											ControlApp.getInstance()
													.getControlWhReceipt()
													.getModelWhReceipt()
													.getIdPODepartment()
													+ "" } };
							String callback = "callbackGetInfoItemsPODepRM";
							String loadingMessage = "Loading items RM";

							ModelService objService = new ModelService(service,
									params, callback, loadingMessage);

							TaskAsynCallService callServiceTask = new TaskAsynCallService();
							callServiceTask.execute(objService);
						}
					});


	}

	private void saveItemsRM(Dialog dialog){

		for(int i = 0; i< tableItemWhRM.getChildCount();i++){
			View row = tableItemWhRM.getChildAt(i);
			if(row instanceof TableRow){
				Integer rowIndex = (Integer) row.getTag();
				if(rowIndex!=null) {
					ControlWhReceipt controlWhReceipt = ControlApp.getInstance().getControlWhReceipt();

					ModelItemRawMaterials itemRM = controlWhReceipt.getModelWhReceipt().getListItemsRawMaterials().get(rowIndex);

					Log.e("rowTag","on row index "+rowIndex);
					TextView txtLength = (TextView) row.findViewWithTag("length");
					TextView txtWidth = (TextView) row.findViewWithTag("width");
					TextView txtHeight = (TextView) row.findViewWithTag("height");
					TextView txtWeightLb = (TextView) row.findViewWithTag("weightLb");
					TextView txtWeightKg = (TextView) row.findViewWithTag("weightKg");
					TextView txtRemarks = (TextView) row.findViewWithTag("remarks");
					CheckBox chkHazardous = (CheckBox) row.findViewWithTag("hazardous");
					TextView txtItemId = (TextView) row.findViewWithTag("idItem");

					ArrayList<ModelLocationItemWh> modelLocationItemWhs = recordsLocationList.get(((TableRow) row).getChildAt(18));
					if(modelLocationItemWhs == null){
						modelLocationItemWhs = new ArrayList<ModelLocationItemWh>();
					}


					String strItemId = txtItemId.getText().toString();
					int itemId = 0;
					if(strItemId != ""){
						itemId = Integer.parseInt(strItemId);
					}

					String strLenght = txtLength.getEditableText().toString();
					String strWidth = txtWidth.getEditableText().toString();
					String strHeight = txtHeight.getEditableText().toString();

					int lenght = Integer.valueOf((strLenght.equals("")?"0":strLenght.toString()));
					int width = Integer.valueOf(strWidth.equals("")?"0":strWidth.toString());
					int height = Integer.valueOf(strHeight.equals("")?"0":strHeight.toString());

					double weightLb = 0;
					if(!txtWeightLb.getEditableText().toString().equals("")){
						weightLb = Double.valueOf(txtWeightLb.getEditableText().toString());
					}
					double weightKg = 0;
					if(!txtWeightKg.getEditableText().toString().equals("")){
						weightKg = Double.valueOf(txtWeightKg.getEditableText().toString());
					}

					int pieces = itemRM.getnPieces();
					double qtyArrivedKG = itemRM.getQtyArrivedKG();
					//validateWeightDiscrepancy(weightKg,qtyArrivedKG,pieces);

					String remarks = txtRemarks.getEditableText().toString();

					itemRM.setListTrackings(getTrackingNumbersForRow(row));
					itemRM.setListLocations(getLocationsForRow(row));
					itemRM.setDimensions(lenght,width,height);
					itemRM.setWeigthKG(weightKg);
					itemRM.setWeigthLB(weightLb);
					itemRM.setRemarks(remarks);
					itemRM.setHazmat(chkHazardous.isChecked());
					itemRM.setIdItem(itemId);


					Log.e("table added", String.valueOf(lenght));
					Log.e("table added", String.valueOf(width));
					Log.e("table added", String.valueOf(height));
					Log.e("table added", String.valueOf(weightLb));
					Log.e("table added", String.valueOf(weightKg));
					Log.e("table added", String.valueOf(remarks));
				}

			}

		}

		ArrayList<ModelItemRawMaterials> listItemsRM = ControlApp
				.getInstance().getControlWhReceipt()
				.getModelWhReceipt().getListItemsRawMaterials();
		boolean flat = true;
		for (ModelItemRawMaterials eachItem : listItemsRM) {
			if (eachItem.getQuantity() > 0.0) {
				if (eachItem.getUnit().equalsIgnoreCase("kg")) {
					if (eachItem.getQtyArrivedKG() == eachItem.getQuantity()) {
						eachItem.setIscompleted(1); // set status completed item
					}
				} else { // lb
					if (eachItem.getQtyArrivedLB() == eachItem.getQuantity()) {
						eachItem.setIscompleted(1); // set status
					}
				}
			}
		}
		// if (flat) {
		// create items for wh receipt
		Log.e("Entrando a crear items",
				"Entrando a crear items");
		for (int j = 0; j < listItemsRM.size(); j++) {
			ModelItemRawMaterials eachItem = listItemsRM.get(j);
			if (eachItem.getQtyArrivedKG() != 0.0 && eachItem.getQtyArrivedLB() != 0.0) {
				ModelItemWhReceipt modelItemCreated = new ModelItemWhReceipt(
						eachItem.isHazmat(), eachItem.getnPieces(), eachItem
						.getUnitType().getValueId(),
						eachItem.getUnitType().getValue(),
						eachItem.getPositionUnitType(),
						eachItem.getLength(),   //length
						eachItem.getWidth(),    //width
						eachItem.getHeight(),   //height
						eachItem.getWeigthLB(), // weightLB
						eachItem.getWeigthKG(), // weightKG
						new ArrayList<ModelLocationItemWh>(),
						new ArrayList<String>(), eachItem.getRemarks(),
						eachItem.getIdItem()
				);
				modelItemCreated.setRelationIdRMItem(j); /*
																		 * IMPORTANT
																		 * create object item and add list item per wh receiipt lo
																		 * */
				ControlApp.getInstance().getControlWhReceipt().getModelWhReceipt().addItemToListItemsWhLO(modelItemCreated);
			}
		}
		loadTableItemsWh(); // repaint items!!
		ControlApp.getInstance().getControlWhReceipt()
				.getModelWhReceipt().setSavedRM(true);
		dialog.dismiss(); // close dialog
		hideKeyboard(WReceiptLOActivity.this);
		/*} else {
			Toast toast = Toast.makeText(WReceiptLOActivity.this,
					"Qty entered is not correct, please check",
					Toast.LENGTH_SHORT);
			toast.setGravity(Gravity.TOP | Gravity.RIGHT, 20, 20);
			toast.show();
		}*/
	}

	private ArrayList<String> getTrackingNumbersForRow(View row) {
		ArrayList<String> trackings = new ArrayList<String>();
		RelativeLayout trackingLayout =  (RelativeLayout) row.findViewById(R.id.relativeLayoutBoxTrackingsWhReceipt_inflate);

		TableLayout dataTable = (TableLayout) trackingLayout.findViewById(R.id.tableTrackingsItemWhReceipt_inflate);
		int tableSize = dataTable.getChildCount();

		//Index started at 2 due two no-data elements in the table
		TableRow tableRow;
		for(int i=2;i<tableSize;i++){
			tableRow = (TableRow) dataTable.getChildAt(i);
			String tracking = ((TextView)tableRow.getChildAt(0)).getText().toString();
			trackings.add(tracking);
		}

		return trackings;
	}
	private ArrayList<ModelLocationItemWh> getLocationsForRow(View row) {
		ArrayList<ModelLocationItemWh> locations = new ArrayList<ModelLocationItemWh>();
		RelativeLayout locationLayout =  (RelativeLayout) row.findViewById(R.id.relativeLayoutBoxLocationsWhReceipt_inflate);

		TableLayout tableLocation = (TableLayout) row.findViewById(R.id.tableLocationsItemWhReceipt_inflate);

		int tableSize = tableLocation.getChildCount();

		//Index started at 2 due two no-data elements in the table
		TableRow tableRow;
		for(int i=2;i<tableSize;i++){
			tableRow = (TableRow) tableLocation.getChildAt(i);


			String NumberPiecesLocationDialog = ((TextView) tableRow.getChildAt(1)).getText().toString();

			// place item location
			String PlaceLocationDialog = ((TextView) tableRow.getChildAt(0)).getText().toString();

			// Machetin de busqueda de zonas como string para
			// obtener el id
			ArrayList<ModelLocation> locationList = ControlApp
					.getInstance().getControlListLocations()
					.getlistLocations();
			int id = 0;
			for (ModelLocation modelLocation : locationList) {
				if (modelLocation.getLocation().equals(
						PlaceLocationDialog)) {
					id = modelLocation.getId();
				}
			}

			ModelLocationItemWh modelLocationItemWh = new ModelLocationItemWh(Integer.parseInt(NumberPiecesLocationDialog), PlaceLocationDialog, id,
					PlaceLocationDialog);

			locations.add(modelLocationItemWh);
		}

		return locations;
	}

	private void validateWeightDiscrepancy(double weightKg,double qtyArrivedKG, int pieces) throws WeightException{
		Log.e("weight", String.valueOf(weightKg));
		Log.e("qtyArrivedKG", String.valueOf(qtyArrivedKG));
		Log.e("pieces", String.valueOf(pieces));
		if(qtyArrivedKG != 0.0 && pieces != 0 && weightKg != 0.0){
			double minPercent = 1.05;

			double maxPercent = 1.5;
			double totalWeigthArrived = qtyArrivedKG;

			double maxWeight = totalWeigthArrived*maxPercent;
			double minWeight = totalWeigthArrived*minPercent;
			double weightPieces = weightKg * pieces;

			if(maxWeight>weightPieces && minWeight<weightPieces){
				return;
			}else{
				throw new WeightException(totalWeigthArrived*minPercent,totalWeigthArrived*maxPercent);
			}
		}
	}

	public void callbackGetPartialLabels(JSONObject jsonResponse){
		Log.e("return callback","return callback");
		//llamar nueva activity
		Toast.makeText(WReceiptLOActivity.this, "Imprimir labels", Toast.LENGTH_LONG).show();
		Intent printLabelIntent = new Intent().setClass(
				WReceiptLOActivity.this, PanePrintActivity.class);
		printLabelIntent.putExtra("back", true);
		startActivity(printLabelIntent);
	}

	public void callbackGetInfoItemsPODepRM(JSONObject jsonResponse) {
		try {
			ControlApp.getInstance().getControlWhReceipt().getModelWhReceipt()
					.getListItemsRawMaterials().clear(); // clear items rm array
			// list
			JSONArray dataItemsRM = new JSONArray(
					jsonResponse.getString("result"));
			for (int i = 0; i < dataItemsRM.length(); i++) {
				JSONObject eachItemRawMaerials = dataItemsRM.getJSONObject(i);
				int idItem = eachItemRawMaerials.getInt("idItem");
				int itemNumber = eachItemRawMaerials.getInt("itemNumber");
				double quantity = eachItemRawMaerials.getDouble("quantity");
				String productName = eachItemRawMaerials
						.getString("productName");
				String productCode = eachItemRawMaerials
						.getString("productCode");
				int worksheetId = eachItemRawMaerials.getInt("worksheetId");
				String unit = eachItemRawMaerials.getString("unitType");
				// create new object item rm
				ModelItemRawMaterials itemRawMaterial = new ModelItemRawMaterials(
						idItem, itemNumber, productName, productCode, quantity,
						worksheetId, unit);
				ControlApp.getInstance().getControlWhReceipt()
						.getModelWhReceipt()
						.addItemRawMaterials(itemRawMaterial);
			}
			loadTableItemsWhRM(); /* important repaint data items rm!!! */
		} catch (Exception e) {
			Log.e("WhReceiptLOActivity",
					"err in method callbackGetInfoItemsPODepRM ", e);
		}
	}

	public void callbackGetInfoItemsPODepIP(JSONObject jsonResponse) {
		try {
			ControlApp.getInstance().getControlWhReceipt().getModelWhReceipt()
					.getListItemsIndustrialPurchase().clear(); // clear items rm
			// array list
			JSONArray dataItemsIP = new JSONArray(
					jsonResponse.getString("result"));
			Log.e("aqui esta todo", "Aqui estan los items: " + dataItemsIP);
			for (int i = 0; i < dataItemsIP.length(); i++) {
				JSONObject eachItemPurchaseOrder = dataItemsIP.getJSONObject(i);
				int idItem = eachItemPurchaseOrder.getInt("idItem");
				int itemNumber = eachItemPurchaseOrder.getInt("itemNumber");
				int quantity = eachItemPurchaseOrder.getInt("quantity");
				String productClientDesciption = eachItemPurchaseOrder
						.getString("clientDescription");
				String productClientRef = eachItemPurchaseOrder
						.getString("clientRef");
				String productSupplierDescription = eachItemPurchaseOrder
						.getString("supplierDescription");
				String productSupplierRef = eachItemPurchaseOrder
						.getString("supplierRef");
				int idUnitType = eachItemPurchaseOrder.getInt("idUnitType");
				String unitType = eachItemPurchaseOrder.getString("unitType");
				int idProduct = eachItemPurchaseOrder.getInt("idProduct");
				String productName = eachItemPurchaseOrder
						.getString("productName");
				String manufacturerRef = eachItemPurchaseOrder
						.getString("manufacturerRef");
				int qtyArrived = eachItemPurchaseOrder.getInt("qtyArrived");
				// create each object item ip

				ModelItemIndustrialPurchase itemPurchaseOrder = new ModelItemIndustrialPurchase(
						idItem, itemNumber, quantity, productClientDesciption,
						productClientRef, productSupplierDescription,
						productSupplierRef, idUnitType, unitType, idProduct,
						productName, manufacturerRef, qtyArrived);
				Log.e("aqui es donde se agregan", "aqui es donde se agregan");
				ControlApp.getInstance().getControlWhReceipt()
						.getModelWhReceipt()
						.addItemIndustrialPurchase(itemPurchaseOrder);
			}
			loadTableItemsWhIP(); /* important repaint data items ip!!! */
		} catch (Exception e) {
			Log.e("WhReceiptLOActivity",
					"err in method callbackGetInfoItemsPODepIP ", e);
		}
	}

	// dialog add items for IP
	private void createDialogIndustrialPurcharse() {
		try {

			// init dialog popup
			final Dialog dialog = new Dialog(WReceiptLOActivity.this);
			dialog.setContentView(R.layout.dialog_items_ip);
			dialog.setCanceledOnTouchOutside(false);
			dialog.getWindow().setLayout(dipToPixels(1260),
					LayoutParams.WRAP_CONTENT); //
			dialog.setTitle("Complete Items Industrial Purchase");

			dialog.show();

			// init table items
			tableItemWhIP = (TableLayout) dialog
					.findViewById(R.id.tableLayoutItemsListIPDialog);
			loadTableItemsWhIP(); // load info items in table IP

			// load data IP Dialog
			ModelWhReceipt whreceipt = ControlApp.getInstance()
					.getControlWhReceipt().getModelWhReceipt(); // object wh
			// receipt
			TextView txtPONumberDialog = (TextView) dialog
					.findViewById(R.id.textViewPOIPValueDialog);
			TextView txtSupplierNameDialog = (TextView) dialog
					.findViewById(R.id.textViewSupplierIPValueDialog);
			txtPONumberDialog.setText(whreceipt.getPo() + "");
			txtSupplierNameDialog.setText(whreceipt.getNameSupplier() + "");

			// save items wh receipt for ip
			Button dialogButtonContinueWhIP = (Button) dialog
					.findViewById(R.id.ButtonContinueIPReceiptDialog);
			dialogButtonContinueWhIP.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					ArrayList<ModelItemIndustrialPurchase> listItemsIP = ControlApp
							.getInstance().getControlWhReceipt()
							.getModelWhReceipt()
							.getListItemsIndustrialPurchase();
					boolean flat = true;
					for (ModelItemIndustrialPurchase eachItem : listItemsIP) {
						if (eachItem.getQuantity() < (eachItem.getQtyArrived() + eachItem
								.getQtyEntered())) {
							flat = false;
							break;
						}
					}
					if (flat) {
						ControlApp.getInstance().getControlWhReceipt()
								.getModelWhReceipt().setSavedIP(true);
						dialog.dismiss(); // close dialog
						hideKeyboard(WReceiptLOActivity.this);
					} else {
						Toast toast = Toast.makeText(WReceiptLOActivity.this,
								"Qty arrived is not correct",
								Toast.LENGTH_SHORT);
						toast.setGravity(Gravity.TOP | Gravity.RIGHT, 20, 20);
						toast.show();
					}
				}
			});

			// botton take photos for ip wh
			Button dialogButtonTakePicturesWhIP = (Button) dialog
					.findViewById(R.id.buttonTakePhotosIPDialog);
			dialogButtonTakePicturesWhIP
					.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View v) {
							// launch activity take photo
							Intent takePhotoIntent = new Intent().setClass(
									WReceiptLOActivity.this,
									TakePhotoActivity.class);
							startActivity(takePhotoIntent);
						}
					});

			// botton update items for ip
			Button dialogButtonUpdateItemsWhIP = (Button) dialog
					.findViewById(R.id.buttonUpdateItemsIPDialog);
			dialogButtonUpdateItemsWhIP
					.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View v) {
							String service = "ServiceWhReceipt";
							String[][] params = {
									{ "operation", "getInfoItemsPODepIP" },
									{
											"idPo",
											ControlApp.getInstance()
													.getControlWhReceipt()
													.getModelWhReceipt()
													.getIdPODepartment()
													+ "" } };
							String callback = "callbackGetInfoItemsPODepIP";
							String loadingMessage = "Loading items IP";

							ModelService objService = new ModelService(service,
									params, callback, loadingMessage);

							TaskAsynCallService callServiceTask = new TaskAsynCallService();
							callServiceTask.execute(objService);
						}
					});

			//button create partial label
			Button dialogButtonPartialLabel =  (Button) dialog
					.findViewById(R.id.Button_partialLabel_ip);
			dialogButtonPartialLabel.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View v) {
					createPartialDialogLabels();
				}

			});
		} catch (Exception e) {
			Log.e("WhReceiptLOActivity",
					"err in method createDialogIndustrialPurcharse ", e);
		}
	}

	// dialog add and edit items for wh receipt
	private void createDialogAddEditItem() {
		try {

			// init dialog popup
			final Dialog dialog = new Dialog(WReceiptLOActivity.this);
			dialog.setContentView(R.layout.dialog_item_cargo_lo);
			dialog.setCanceledOnTouchOutside(false);
			dialog.getWindow().setLayout(dipToPixels(1350),
					LayoutParams.WRAP_CONTENT); //

			// init autocomplete locations
			txtAutoLocations = (AutoCompleteTextView) dialog
					.findViewById(R.id.editTextDialogItemLocationPlaceLO);// auto

			// complete
			// component
			txtAutoLocations.setOnFocusChangeListener(new OnFocusChangeListener() {
				@Override
				public void onFocusChange(View v, boolean hasFocus) {
					if (!hasFocus) {
						String textInput = txtAutoLocations.getText().toString();
						ListAdapter adapter = txtAutoLocations.getAdapter();
						boolean textFromList = false;
						if (adapter!=null) {
							int size = adapter.getCount();
							for (int i= 0 ;i<size;i++){
								if(textInput.equals(adapter.getItem(i))){
									textFromList = true;
									break;
								}
							}
						}
						if(!textFromList){
							txtAutoLocations.setText("");
						}
					}
				}
			});

			txtAutoLocations.setOnItemClickListener(new OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> parent, View view,
										int position, long id) {
					String itemSelected = (String) parent
							.getItemAtPosition(position);
					txtAutoLocations.setText(itemSelected);
				}
			});
			loadDataLocations(); // load data locations

			// init spinner type unit
			spinnerTypeUnit = (Spinner) dialog
					.findViewById(R.id.spinnerDialogItemTypePackageLO);
			ArrayList<String> arrayDataTypeUnit = getArrayAdapterDataSpinnerUnitType();

			ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(
					WReceiptLOActivity.this,
					android.R.layout.simple_spinner_item, arrayDataTypeUnit);
			dataAdapter
					.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			spinnerTypeUnit.setAdapter(dataAdapter);

			// events change spinner (it's not used yet)
			spinnerTypeUnit
					.setOnItemSelectedListener(new OnItemSelectedListener() {
						@Override
						public void onItemSelected(AdapterView<?> parentView,
												   View selectItemView, int position, long id) {
							// Toast.makeText(parentView.getContext()," selected "
							// +
							// parentView.getItemAtPosition(position).toString(),
							// Toast.LENGTH_SHORT).show();
						}

						@Override
						public void onNothingSelected(AdapterView<?> parentView) {

						}
					});

			/*
			 * init list and table items per wh receipt
			 */
			// init table locations *
			tableLocationsItemWhLO = (TableLayout) dialog
					.findViewById(R.id.tableLocationsItemWhReceipt);

			// init table trackings
			tableTrackingsItemWhLO = (TableLayout) dialog
					.findViewById(R.id.tableTrackingsItemWhReceipt);


			// repaintTabletTrackingsItemWhInflate(getAdapterTableListTrackingsItemWhLO());

			/*
			 * init all componenents (inputs) dialog
			 */
			// final Switch switchHazmatLO = (Switch)
			// dialog.findViewById(R.id.switchDialogHazmatLO); //no used, and
			// replaced by checkbox
			final CheckBox checkboxHazmatLO = (CheckBox) dialog
					.findViewById(R.id.checkboxDialogHazmatLO);
			final EditText txtNumPiecesDialog = (EditText) dialog
					.findViewById(R.id.editTextDialogItemNPiecesLO);
			final EditText txtLengthDialog = (EditText) dialog
					.findViewById(R.id.editTextDialogItemLengthLO);
			final EditText txtWidthDialog = (EditText) dialog
					.findViewById(R.id.editTextDialogItemWidthLO);
			final EditText txtHeightDialog = (EditText) dialog
					.findViewById(R.id.editTextDialogItemHeightLO);
			final EditText txtWeightLBDialog = (EditText) dialog
					.findViewById(R.id.editTextDialogItemWeightLBLO);
			final EditText txtWeightKGDialog = (EditText) dialog
					.findViewById(R.id.editTextDialogItemWeightKGLO);

			final EditText txtTrackingDialog = (EditText) dialog
					.findViewById(R.id.editTextDialogItemTrackingLO);

			final EditText txtidItemPO = (EditText) dialog.findViewById(R.id.editText_idItemPO);

			txtTrackingDialog.setOnKeyListener(new View.OnKeyListener() {
				@Override
				public boolean onKey(View v, int keyCode, KeyEvent event) {
					if ((event.getAction() == KeyEvent.ACTION_DOWN)
							&& (keyCode == KeyEvent.KEYCODE_ENTER)) { // enter
						// do
						// noting
						return true;
					}
					return false;
				}
			});
			final EditText txtRemarksDialog = (EditText) dialog
					.findViewById(R.id.editTextDialogItemRemarksLO);

			// change title dialog if add or edit
			if (currentActionDialog == TypeDialog.ADD) {
				dialog.setTitle("Create New Item WH Receipt");

				// locations. Empty array
				listLocationsTemp = new ArrayList<ModelLocationItemWh>();
				listTrackingsTemp = new ArrayList<String>();

				dialog.show(); // show dialog
			} else if (currentActionDialog == TypeDialog.EDIT) {

				try {
					dialog.setTitle("Edit Item WH Receipt");

					// load locations current item loaded
					// (currentIdPostitionEditItemDialog)
					ModelItemWhReceipt itemWhEdit = ControlApp.getInstance()
							.getControlWhReceipt().getModelWhReceipt()
							.getListItemsWhLO()
							.get(currentIdPostitionEditItemDialog);

					// set data in fields dialog ...
					listLocationsTemp = itemWhEdit.getLocations();

					ArrayList<ModelItemRawMaterials> listItemsRawMaterials = ControlApp.getInstance()
							.getControlWhReceipt().getModelWhReceipt()
							.getListItemsRawMaterials();
					ModelItemRawMaterials itemRMFather = null;

					if(listItemsRawMaterials.size()>0){
						itemRMFather = ControlApp.getInstance()
								.getControlWhReceipt().getModelWhReceipt()
								.getListItemsRawMaterials().get(itemWhEdit.getRelationIdRMItem());
					}



					ArrayList trackings = null;
					ArrayList locations = null;
					if(itemRMFather!=null){
						trackings = itemRMFather.getListTrackings();
						locations = itemRMFather.getListLocations();
					}

					// Load de trackings from de start pop up, and clean it for keep the actual changes
					if(trackings!=null && trackings.size()>0){
						listTrackingsTemp = trackings;
						itemRMFather.setListTrackings(null);
						itemWhEdit.setListTrackings(listTrackingsTemp);
					}else {
						listTrackingsTemp = itemWhEdit.getListTrackings();
					}
					if(locations!=null && locations.size()>0){
						listLocationsTemp = locations;
						itemRMFather.setListLocations(null);
						itemWhEdit.setListLocations(listLocationsTemp);
					}else {
						listLocationsTemp = itemWhEdit.getListLocations();
					}


					checkboxHazmatLO.setChecked(itemWhEdit.getHazmat()); // set
					// hazmat
					spinnerTypeUnit.setSelection(itemWhEdit
							.getPositionUnitType());
					txtNumPiecesDialog.setText(itemWhEdit.getnPieces() + "");
					txtLengthDialog.setText("");
					if (itemWhEdit.getLength() > 0) {
						txtLengthDialog.setText(itemWhEdit.getLength() + "");
					}
					txtWidthDialog.setText("");
					if (itemWhEdit.getWidth() > 0) {
						txtWidthDialog.setText(itemWhEdit.getWidth() + "");
					}
					txtHeightDialog.setText("");
					if (itemWhEdit.getHeight() > 0) {
						txtHeightDialog.setText(itemWhEdit.getHeight() + "");
					}
					txtWeightLBDialog.setText("");
					if (itemWhEdit.getWeigthLB() > 0) {
						txtWeightLBDialog
								.setText(itemWhEdit.getWeigthLB() + "");
					}
					txtWeightKGDialog.setText("");
					if (itemWhEdit.getWeigthKG() > 0) {
						txtWeightKGDialog
								.setText(itemWhEdit.getWeigthKG() + "");
					}
					txtRemarksDialog.setText(itemWhEdit.getRemarks());

					if(itemWhEdit.getPoItem_id()!=0){
						txtidItemPO.setText(String.valueOf(itemWhEdit.getPoItem_id()));
					}

					// repaint table locations
					loadTableLocationsPerItem();
					loadTableTrackingsPerItem();

					dialog.show(); // show dialog only when data is already set
				} catch (Exception e) {
					Log.e("WhReceiptLOActivity", "*********** error with: ",e);
				}
			}



			// events buttons
			Button dialogButtonSave = (Button) dialog
					.findViewById(R.id.buttonDialogSaveItemLO);
			dialogButtonSave.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					try {

						try {
							// int number pieces
							TextView txtNumberPiecesLocationDialog = (TextView) dialog
									.findViewById(R.id.editTextDialogItemLocationNPiecesLO);
							int cargoPiecesPerLocation = Integer
									.parseInt(txtNumberPiecesLocationDialog
											.getText().toString());

							// place item location
							TextView txtPlaceLocationDialog = (TextView) dialog
									.findViewById(R.id.editTextDialogItemLocationPlaceLO);
							String cargoPlacePerLocation = txtPlaceLocationDialog
									.getText().toString();

							// Machetin de busqueda de zonas como string para
							// obtener el id
							ArrayList<ModelLocation> locationList = ControlApp
									.getInstance().getControlListLocations()
									.getlistLocations();
							int id = 0;
							for (ModelLocation modelLocation : locationList) {
								if (modelLocation.getLocation().equals(
										cargoPlacePerLocation)) {
									id = modelLocation.getId();
								}

							}
							Log.e("cargoPlacePerLocation", "cargoPlacePerLocation: "+cargoPlacePerLocation+" id:"+id);
							// add location to item wh receipt
							listLocationsTemp.add(new ModelLocationItemWh(
									cargoPiecesPerLocation,cargoPlacePerLocation, id,
									cargoPlacePerLocation));
							loadTableLocationsPerItem();
						} catch (NumberFormatException e) {
							Log.e("NumberFormatException","NumberFormatException",e);
						}

						// hazmat
						boolean isHazmatLO = checkboxHazmatLO.isChecked();

						// unit type
						int positionTypeUnitItem = spinnerTypeUnit
								.getSelectedItemPosition();
						ModelMasterValue typeUnit = ControlApp.getInstance()
								.getControlListMasterValues().getListUnitType()
								.get(positionTypeUnitItem);

						// number pieces
						int nPieces = Integer.parseInt(txtNumPiecesDialog
								.getText().toString());

						// length
						double cargoLength = Double.parseDouble(txtLengthDialog
								.getText().toString());

						// width
						double cargoWidth = Double.parseDouble(txtWidthDialog
								.getText().toString());

						// height
						double cargoHeight = Double.parseDouble(txtHeightDialog
								.getText().toString());

						// weight lb
						String cargoWeightLB = txtWeightLBDialog.getText()
								.toString();

						// weight kg
						String cargoWeightKG = txtWeightKGDialog.getText()
								.toString();

						// remarks
						String cargoRemarks = txtRemarksDialog.getText().toString();

						String strIdItem = txtidItemPO.getText().toString();
						int idItem = 0;
						if(!strIdItem.equals("")){
							idItem = Integer.parseInt(strIdItem);
						}

						double valueCargoWeightLb = 0L;
						double valueCargoWeightKG = 0L;
						if (!cargoWeightLB.equals("")) {
							valueCargoWeightLb = Double
									.parseDouble(cargoWeightLB);
							valueCargoWeightKG = valueCargoWeightLb * 0.45359237;
						} else {
							valueCargoWeightKG = Double
									.parseDouble(cargoWeightKG);
							valueCargoWeightLb = valueCargoWeightKG / 0.45359237;
						}

						int numPiecesLocations = 0;
						for (int i = 0; i < listLocationsTemp.size(); i++) {
							numPiecesLocations += listLocationsTemp.get(i).getnPieces();
						}

						if (numPiecesLocations == nPieces) { // check pieces
							// number are
							// equal

							// item wh created
							ModelItemWhReceipt modelItemCreated = new ModelItemWhReceipt(
									isHazmatLO, nPieces, typeUnit.getValueId(),
									typeUnit.getValue(), positionTypeUnitItem,
									cargoLength, cargoWidth, cargoHeight,
									valueCargoWeightLb, valueCargoWeightKG,
									listLocationsTemp, listTrackingsTemp,
									cargoRemarks,idItem);



							if (currentActionDialog == TypeDialog.ADD) {
								// create object item and add list item per wh
								// receiipt lo
								ControlApp
										.getInstance()
										.getControlWhReceipt()
										.getModelWhReceipt()
										.addItemToListItemsWhLO(
												modelItemCreated);
							} else if (currentActionDialog == TypeDialog.EDIT) {
								// update position object item wh receipt
								ControlApp
										.getInstance()
										.getControlWhReceipt()
										.getModelWhReceipt()
										.replaceItemToListWhLo(
												currentIdPostitionEditItemDialog,
												modelItemCreated);

								ControlApp
										.getInstance()
										.getControlWhReceipt()
										.getModelWhReceipt().changeRemarksRM(currentIdPostitionEditItemDialog,cargoRemarks);
							}

							// reload table and hide dialog
							loadTableItemsWh();
							dialog.dismiss(); // hide dialog
							hideKeyboard(WReceiptLOActivity.this); // hide
							// keyboard
						} else {
							// set other default position
							Toast toast = Toast.makeText(
									WReceiptLOActivity.this,
									"The number of pieces is incorrect",
									Toast.LENGTH_SHORT);
							toast.setGravity(Gravity.TOP | Gravity.RIGHT, 10,
									10);
							toast.show();
						}

					} catch (NumberFormatException e) {
						Log.e("numberformat","e",e);
						Toast toast = Toast.makeText(WReceiptLOActivity.this,
								"All Fields are required", Toast.LENGTH_SHORT);
						toast.setGravity(Gravity.TOP | Gravity.RIGHT, 10, 10);
						toast.show();
					}
				}
			});

			// remove item wh receipt
			Button dialogButtonCancel = (Button) dialog
					.findViewById(R.id.buttonDialogRemoveItemLO);
			dialogButtonCancel.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					/*
					 * cancel button
					 */
					// don't repaint items
					// loadTableItemsWh();
					dialog.dismiss(); // close dialog
					hideKeyboard(WReceiptLOActivity.this);
				}
			});

			// add location to wh receipt
			Button dialogButtonAddLocation = (Button) dialog
					.findViewById(R.id.buttonDialogAddEachLocationLO);
			dialogButtonAddLocation.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					try {
						// int number pieces
						TextView txtNumberPiecesLocationDialog = (TextView) dialog
								.findViewById(R.id.editTextDialogItemLocationNPiecesLO);
						int cargoPiecesPerLocation = Integer
								.parseInt(txtNumberPiecesLocationDialog
										.getText().toString());

						// place item location
						TextView txtPlaceLocationDialog = (TextView) dialog
								.findViewById(R.id.editTextDialogItemLocationPlaceLO);
						String cargoPlacePerLocation = txtPlaceLocationDialog
								.getText().toString();

						// Machetin de busqueda de zonas como string para
						// obtener el id
						ArrayList<ModelLocation> locationList = ControlApp
								.getInstance().getControlListLocations()
								.getlistLocations();
						int id = 0;
						for (ModelLocation modelLocation : locationList) {
							if (modelLocation.getLocation().equals(
									cargoPlacePerLocation)) {
								id = modelLocation.getId();
							}

						}
						// add location to item wh receipt
						listLocationsTemp.add(new ModelLocationItemWh(
								cargoPiecesPerLocation, cargoPlacePerLocation,id,
								cargoPlacePerLocation));
						loadTableLocationsPerItem();

						// clean fields
						txtNumberPiecesLocationDialog.setText("");
						txtPlaceLocationDialog.setText("");
						txtPlaceLocationDialog.requestFocus(); // focus to
						// location
						// input

					} catch (NumberFormatException e) {
						Toast.makeText(WReceiptLOActivity.this,
								"N. Pieces and Location are required",
								Toast.LENGTH_SHORT).show();
					}
				}

			});

			// add location to wh receipt
			Button dialogButtonAddTracking = (Button) dialog
					.findViewById(R.id.buttonDialogAddEachTrackingLO);
			dialogButtonAddTracking.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					try {
						// int number pieces
						TextView txtTrackingDialog = (TextView) dialog
								.findViewById(R.id.editTextDialogItemTrackingLO);
						String trackingNumber = txtTrackingDialog.getText()
								.toString();

						// add location to item wh receipt
						listTrackingsTemp.add(trackingNumber);
						loadTableTrackingsPerItem();

						// clean fields
						txtTrackingDialog.setText("");

					} catch (Exception e) {
						// Toast.makeText(WReceiptLOActivity.this,
						// "N. Pieces and Location are required",
						// Toast.LENGTH_SHORT).show();
						Log.e("WReceiptLOActivity",
								"Error in button add tracking" + e);
					}
				}
			});



			/*
			 * End events dialog buttons
			 */

		} catch (Exception e) {
			Log.e("WhReceiptLOActivity", "err creating dialog with data ", e);
		}
	}

	// dialog label partial
	private void createPartialDialogLabels() {
		// button partial label
		final Dialog dialogPartial = new Dialog(WReceiptLOActivity.this);
		dialogPartial.setContentView(R.layout.dialog_partial_labels);
		dialogPartial.setCanceledOnTouchOutside(false);
		dialogPartial.getWindow().setLayout(dipToPixels(550),
				LayoutParams.WRAP_CONTENT); //
		dialogPartial.setTitle("Number labels");
		dialogPartial.show();

		// button print partial labels
		Button partialLabelButtonPrint = (Button) dialogPartial
				.findViewById(R.id.button_partial_labels_print);
		partialLabelButtonPrint.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

				// aqui vamos a agregar los items con valores vacios y sin
				// ubicacion ni track
				EditText textViewDialog = (EditText)dialogPartial.findViewById(R.id.editText_number);

				// create object item and add list item per wh
				// receiipt lo
				ControlApp.getInstance().getControlWhReceipt().getModelWhReceipt().setNumLabels(Integer.parseInt(textViewDialog.getText().toString()));


				// reload table and hide dialog 
				hideKeyboard(WReceiptLOActivity.this); // hide
				// keyboard
				dialogPartial.hide();
				dialogPartial.dismiss();
				createDialogConfirmPartialLabels();
			}
		});
		// button cancel partial labels
		Button partialLabelButtonCancel = (Button) dialogPartial
				.findViewById(R.id.button_dialog_partial_labels_cancel);
		partialLabelButtonCancel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dialogPartial.hide();
				dialogPartial.cancel();
				dialogPartial.dismiss();

			}
		});
	}

	/*
	 * finish process wh receipt, encode items and send data to server
	 */

	private void terminateProcessWhReceipt() {
		try {
			// send data to server
			ModelWhReceipt whReceipt = ControlApp.getInstance()
					.getControlWhReceipt().getModelWhReceipt(); // get object
			// whr

			/*
			 * parse has map items per wh receipt
			 */
			JSONArray listItemsJSON = new JSONArray();
			for (int i = 0; i < whReceipt.getListItemsWhLO().size(); i++) {
				ModelItemWhReceipt eachItem = whReceipt.getListItemsWhLO().get(
						i);
				JSONObject mapDataItem = new JSONObject();
				int isHaz = (eachItem.getHazmat() ? 1 : 0); // yes 1, no 0
				mapDataItem.put("haz", isHaz + "");
				mapDataItem.put("pieces", eachItem.getnPieces() + "");
				mapDataItem.put("idType", eachItem.getIdUnitType() + "");
				mapDataItem.put("nameType", eachItem.getNameUnitType() + "");
				mapDataItem.put("length", eachItem.getLength() + "");
				mapDataItem.put("width", eachItem.getWidth() + "");
				mapDataItem.put("height", eachItem.getHeight() + "");
				mapDataItem.put("weigth", eachItem.getWeigthLB() + "");
				mapDataItem.put("volumeInches", eachItem.getWidth()*eachItem.getHeight()*eachItem.getLength() + "");
				mapDataItem.put("weigthKg", eachItem.getWeigthKG() + "");
				mapDataItem.put("remarks", eachItem.getRemarks() + "");
				mapDataItem.put("poItem_id", eachItem.getPoItem_id() + "");

				/*
				 * add for rm dep, it need codeProduct
				 */
				// for rm
				if (whReceipt.getIdDep() == 1 || whReceipt.getIdDep() == 2) { //is included 2 department, code 1 unknown department
					int idRelationPosition = eachItem.getRelationIdRMItem();
					ModelItemRawMaterials itemRMFather = whReceipt
							.getListItemsRawMaterials().get(idRelationPosition);
					String idCodeProduct = itemRMFather.getProductRef();
					mapDataItem.put("codeProduct", idCodeProduct + "");
					mapDataItem.put("urgent", itemRMFather.isUrgent());
				}

				// locations list
				JSONArray mapDataLocation = new JSONArray();
				for (int j = 0; j < eachItem.getLocations().size(); j++) {
					ModelLocationItemWh eachLocation = eachItem.getLocations()
							.get(j);
					JSONObject eachLocationJSON = new JSONObject();
					eachLocationJSON.put("loc", eachLocation.getId()+ "");
					eachLocationJSON.put("label", eachLocation.getLabel());
					eachLocationJSON.put("pc", eachLocation.getnPieces() + "");
					mapDataLocation.put(eachLocationJSON);
				}
				mapDataItem.put("locations", mapDataLocation);

				// tracking list
				JSONArray mapDataTracking = new JSONArray();
				for (int j = 0; j < eachItem.getListTrackings().size(); j++) {
					String eachTracking = eachItem.getListTrackings().get(j);
					mapDataTracking.put(eachTracking);
				}
				mapDataItem.put("trackings", mapDataTracking);

				listItemsJSON.put(mapDataItem);
			}

			Log.i("**************** Items json to String",listItemsJSON.toString());

			// list items arrived per dep
			JSONArray listItemsIPJSON = new JSONArray();
			JSONArray listItemsRMJSON = new JSONArray();
			if (whReceipt.getIdDep() == 3) { // ip
				for (int i = 0; i < whReceipt.getListItemsIndustrialPurchase()
						.size(); i++) {
					ModelItemIndustrialPurchase eachItem = whReceipt
							.getListItemsIndustrialPurchase().get(i);
					JSONObject mapDataItem = new JSONObject();
					mapDataItem.put("itemId", eachItem.getIdItem() + "");
					mapDataItem.put("quantity", eachItem.getQuantity() + "");
					mapDataItem.put("quantityArrived", eachItem.getQtyArrived()
							+ "");
					mapDataItem.put("quantityEntered", eachItem.getQtyEntered()
							+ "");
					listItemsIPJSON.put(mapDataItem); // add json object
				}
			} else if (whReceipt.getIdDep() == 2) { // rm
				for (int i = 0; i < whReceipt.getListItemsRawMaterials().size(); i++) {
					ModelItemRawMaterials eachItem = whReceipt
							.getListItemsRawMaterials().get(i);
					JSONObject mapDataItem = new JSONObject();
					mapDataItem.put("itemID", eachItem.getIdItem() + "");
					mapDataItem
							.put("productRed", eachItem.getProductRef() + "");
					mapDataItem.put("unit", eachItem.getUnit() + "");
					mapDataItem.put("quantity", eachItem.getQuantity() + "");
					mapDataItem.put("quantityArrivedLB",
							eachItem.getQtyArrivedLB() + "");
					mapDataItem.put("quantityArrivedKG",
							eachItem.getQtyArrivedKG() + "");
					mapDataItem.put("nPieces", eachItem.getnPieces() + "");
					mapDataItem.put("worksheetID", eachItem.getOrderToPlace()
							+ "");
					mapDataItem.put("packageTypeID", eachItem.getUnitType()
							.getValueId() + "");
					mapDataItem.put("packageTypeName", eachItem.getUnitType()
							.getValue() + "");
					mapDataItem.put("isCompleted", eachItem.getIscompleted()+ "");
					mapDataItem.put("remarks", eachItem.getRemarks()+ "");

					listItemsRMJSON.put(mapDataItem); // add json object
				}
			}

			// get data
			String whID = whReceipt.getIdWhReceipt() + "";
			String whNumber = whReceipt.getWhReceiptNumber()+ "";
			String idPo = whReceipt.getIdPODepartment() + "";
			String idDepartment = whReceipt.getIdDep() + "";
			String po = whReceipt.getPo() + "";
			String client = whReceipt.getNameClient() + "";
			String supplier = whReceipt.getNameSupplier() + "";
			String truckID = whReceipt.getTruckCompany().getIdCarrier() + "";
			String truckName = whReceipt.getTruckCompany().getCarrier();
			String numPallets = whReceipt.getHtPallets() + "";
			String remarks = whReceipt.getRemarks();
			String dateReceipt = whReceipt.getDateTimeReceived() + "";
			String employeeEntered = whReceipt.getNameEmployeeEntered() + "";

			String service = "ServiceWhReceipt";
			String[][] params = { { "operation", "saveWarehouseReceipt" },
					{ "idPo", idPo }, { "whID", whID },{ "whNumber", whNumber },
					{ "idDepartment", idDepartment }, { "po", po },
					{ "client", client }, { "supplier", supplier },
					{ "truckID", truckID }, { "truckName", truckName },
					{ "numPallets", numPallets }, { "remarks", remarks },
					{ "employeeEntered", employeeEntered },
					{ "dateReceipt", dateReceipt },
					{ "listItems", listItemsJSON.toString() },
					{ "listItemsIP", listItemsIPJSON.toString() },
					{ "listItemsRM", listItemsRMJSON.toString() } };
			String callback = "callbackSaveWarehouseReceipt";
			String loadingMessage = "Loading Unit Types";

			ModelService objService = new ModelService(service, params,
					callback, loadingMessage);
			TaskAsynCallService callServiceTask = new TaskAsynCallService();
			callServiceTask.execute(objService);
		} catch (Exception e) {
			Log.e("WhReceiptLOActivity",
					"err in method terminateProcessWhReceipt ", e);
		}
	}

	private void callbackSaveWarehouseReceipt(JSONObject jsonResponse) {
		try {
			Log.i("callbackSaveWarehouseReceipt", jsonResponse.toString());
			JSONObject data = new JSONObject(jsonResponse.getString("result"));
			String isCreated = data.getString("isCreated");
			int totalLabels = Integer.parseInt(data.getString("totalLabels"));
			// check if is create is true
			if (isCreated.equals("true")) {
				ControlApp.getInstance().getControlWhReceipt()
						.getModelWhReceipt().setNumLabels(totalLabels);
				// go pane print labels
				Intent printLabelIntent = new Intent().setClass(WReceiptLOActivity.this, PanePrintActivity.class);
				printLabelIntent.putExtra("back", false);
				startActivity(printLabelIntent);
				finish();
			} else {
				Toast.makeText(WReceiptLOActivity.this,
						"Error saving whr and creating labels",
						Toast.LENGTH_LONG).show();
				Log.e("WhReceiptLOActivity", "err is create set in false");
			}
		} catch (Exception ex) {
			Log.e("WhReceiptLOActivity",
					"Error parsing json in method callbackSaveWarehouseReceipt ",
					ex);
		}
	}

	private void loadTableLocationsPerItem() {
		try {
			// adapter is a matrix String String
			repaintTableLocationsItemWh(getAdapterTableListLocationsItemWhLO());
		} catch (Exception e) {
			Log.e("WhReceiptLOActivity",
					"err in method loadTableLocationsPerItem ", e);
		}
	}
	private void loadTableLocationsPerItemInflate(RelativeLayout rowRelativeLayout) {
		try {
			// adapter is a matrix String String
			repaintTableLocationsItemWhInflate(getAdapterTableListLocationsItemWhLOInflate(recordsLocationList.get(rowRelativeLayout)),rowRelativeLayout);
		} catch (Exception e) {
			Log.e("WhReceiptLOActivity",
					"err in method loadTableLocationsPerItem ", e);
		}
	}

	private void loadTableTrackingsPerItem() {
		try {
			// adapter is a matrix String String
			repaintTabletTrackingsItemWh(getAdapterTableListTrackingsItemWhLO());
		} catch (Exception e) {
			Log.e("WhReceiptLOActivity",
					"err in method loadTableTrackingsPerItem ", e);
		}
	}

	private void loadTableTrackingsPerItem_inflate(RelativeLayout rowRelativeLayout) {
		try {
			// adapter is a matrix String String
			repaintTabletTrackingsItemWhInflate(getAdapterTableListTrackingsItemWhLOInflate(recordsTrackingList.get(rowRelativeLayout)),rowRelativeLayout);
		} catch (Exception e) {
			Log.e("WhReceiptLOActivity",
					"err in method loadTableTrackingsPerItem ", e);
		}
	}

	// load data table RM
	private void loadTableItemsWhRM() {
		try {
			// adapter is a matrix String String
			repaintTableItemsWhRM(getAdapterTableListItemsWhRM());
		} catch (Exception e) {
			Log.e("WhReceiptLOActivity",
					"err in method loadTableLocationsPerItem ", e);
		}
	}

	// load data table IP
	private void loadTableItemsWhIP() {
		try {
			// adapter is a matrix String String
			repaintTableItemsWhIP(getAdapterTableListItemsWhIP());
		} catch (Exception e) {
			Log.e("WhReceiptLOActivity",
					"err in method loadTableLocationsPerItem ", e);
		}
	}

	private String[][] getAdapterTableListLocationsItemWhLOInflate(ArrayList<ModelLocationItemWh> locations) {
		try {
			String[][] adapterTable = new String[locations.size()][2];
			for (int i = 0; i < locations.size(); i++) {
				adapterTable[i][0] = locations.get(i)
						.getLocationPlace() + "";
				adapterTable[i][1] = locations.get(i).getnPieces() + "";
			}
			return adapterTable;
		} catch (Exception e) {
			System.err
					.println("Error creating array data table LOCATIONS items wh receipt");
		}
		return new String[locations.size()][2];
	}
	private String[][] getAdapterTableListLocationsItemWhLO() {
		try {
			String[][] adapterTable = new String[listLocationsTemp.size()][2];
			for (int i = 0; i < listLocationsTemp.size(); i++) {
				adapterTable[i][0] = listLocationsTemp.get(i)
						.getLocationPlace() + "";
				adapterTable[i][1] = listLocationsTemp.get(i).getnPieces() + "";
			}
			return adapterTable;
		} catch (Exception e) {
			System.err
					.println("Error creating array data table LOCATIONS items wh receipt");
		}
		return new String[listLocationsTemp.size()][2];
	}

	private String[][] getAdapterTableListTrackingsItemWhLO() {
		try {
			String[][] adapterTable = new String[listTrackingsTemp.size()][1];
			for (int i = 0; i < listTrackingsTemp.size(); i++) {
				adapterTable[i][0] = listTrackingsTemp.get(i);
			}
			return adapterTable;
		} catch (Exception e) {
			System.err
					.println("Error creating array data table TRACKINGS items wh receipt");
		}
		return new String[listTrackingsTemp.size()][1];
	}

	private String[][] getAdapterTableListTrackingsItemWhLOInflate(ArrayList<String> trackings) {
		try {
			String[][] adapterTable = new String[trackings.size()][1];
			for (int i = 0; i < trackings.size(); i++) {
				adapterTable[i][0] = trackings.get(i);
			}
			return adapterTable;
		} catch (Exception e) {
			System.err
					.println("Error creating array data table TRACKINGS items wh receipt");
		}
		return new String[trackings.size()][1];
	}

	private String[][] getAdapterTableListItemsWhRM() {
		try {
			ArrayList<ModelItemRawMaterials> listItemsRM = ControlApp
					.getInstance().getControlWhReceipt().getModelWhReceipt()
					.getListItemsRawMaterials();
			String[][] adapterTable = new String[listItemsRM.size()][11];
			for (int i = 0; i < listItemsRM.size(); i++) {
				adapterTable[i][0] = listItemsRM.get(i).getItemNumber() + "";
				adapterTable[i][1] = listItemsRM.get(i).getProductName() + "";

				adapterTable[i][2] = listItemsRM.get(i).getProductRef() + "";
				adapterTable[i][3] = listItemsRM.get(i).getQuantity() + "";
				adapterTable[i][4] = listItemsRM.get(i).getUnit() + "";
				adapterTable[i][5] = listItemsRM.get(i).getQtyArrivedLB() + "";
				adapterTable[i][6] = listItemsRM.get(i).getQtyArrivedKG() + "";
				adapterTable[i][7] = listItemsRM.get(i).getnPieces() + "";
				adapterTable[i][8] = listItemsRM.get(i).getPositionUnitType()+ "";
				adapterTable[i][9] = listItemsRM.get(i).isHazmat()+ "";
				adapterTable[i][10] = listItemsRM.get(i).getIdItem()+ "";
				/*
				for(String tracking : listItemsRM.get(i).getListTrackings()){
					adapterTable[i][11] +=tracking+"|";
				}
				*/
			}
			return adapterTable;
		} catch (Exception e) {
			Log.e("Error","Error creating array data table IP items wh receipt",e);
			System.err
					.println("Error creating array data table IP items wh receipt");
		}
		return new String[0][0];
	}

	private String[][] getAdapterTableListItemsWhIP() {

		try {
			ArrayList<ModelItemIndustrialPurchase> listItemsIP = ControlApp
					.getInstance().getControlWhReceipt().getModelWhReceipt()
					.getListItemsIndustrialPurchase();
			String[][] adapterTable = new String[listItemsIP.size()][8];
			Log.e("listItemsIP: ", "listItemsIP ======: " + listItemsIP.size());
			for (int i = 0; i < listItemsIP.size(); i++) {
				adapterTable[i][0] = listItemsIP.get(i).getItemNumber() + " ";
				if (!listItemsIP.get(i).getProductSupplierDescription()
						.equalsIgnoreCase("")) {
					adapterTable[i][1] = listItemsIP.get(i)
							.getProductSupplierDescription() + "";
				} else {
					adapterTable[i][1] = listItemsIP.get(i).getProductName()
							+ "";
				}
				if (!listItemsIP.get(i).getProductSupplierRef()
						.equalsIgnoreCase("")) {
					adapterTable[i][2] = listItemsIP.get(i)
							.getProductSupplierRef() + "";
				} else {
					adapterTable[i][2] = listItemsIP.get(i)
							.getManufacturerRef() + "";
				}
				adapterTable[i][3] = listItemsIP.get(i)
						.getProductClientDesciption() + "";
				adapterTable[i][4] = listItemsIP.get(i).getProductClientRef()
						+ "";
				adapterTable[i][5] = listItemsIP.get(i).getQuantity() + "";
				adapterTable[i][6] = listItemsIP.get(i).getUnitType() + "";
				adapterTable[i][7] = listItemsIP.get(i).getQtyEntered() + "";
			}
			return adapterTable;
		} catch (Exception e) {
			System.err
					.println("Error creating array data table IP items wh receipt");
		}
		return new String[0][0];
	}

	// repaint table wh locations
	private void repaintTableLocationsItemWhInflate(String dataTable[][],final RelativeLayout rowRelativeLayout) {
		try {
			String headerTableData[] = { "Loc", "# Pc" };


			TableLayout	tableLocationsItemWhLO_inflate = (TableLayout) rowRelativeLayout.findViewById(R.id.tableLocationsItemWhReceipt_inflate);

			// reset table
			tableLocationsItemWhLO_inflate.removeAllViewsInLayout();

			/* TableLayout design layout */

			// header row table
			TableRow headerTable = new TableRow(this);
			headerTable.setLayoutParams(new TableLayout.LayoutParams(
					LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
			tableLocationsItemWhLO_inflate.addView(headerTable);

			// write header table
			for (int i = 0; i < headerTableData.length; i++) {
				TextView column = new TextView(this);
				column.setLayoutParams(new TableRow.LayoutParams(
						LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
				column.setText(headerTableData[i]);
				column.setTextSize(TypedValue.COMPLEX_UNIT_SP, 17);
				column.setTypeface(column.getTypeface(), Typeface.BOLD);
				column.setGravity(Gravity.CENTER_HORIZONTAL);
				column.setPadding(1, 1, 1, 1);
				headerTable.addView(column);
				// tableLocationsItemWhLO.setColumnStretchable(i, true);
			}

			// separator border headerTable in table
			TableRow borderHeaderTable = new TableRow(this);
			borderHeaderTable.setLayoutParams(new TableLayout.LayoutParams(
					LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
			FrameLayout lineHeaderTable = new FrameLayout(this);
			TableRow.LayoutParams lineHeaderTableParams = new TableRow.LayoutParams(
					LayoutParams.MATCH_PARENT, 2);
			lineHeaderTableParams.span = headerTableData.length + 1;
			lineHeaderTable.setBackgroundColor(Color.parseColor("#ff33b5e5"));
			borderHeaderTable.addView(lineHeaderTable, lineHeaderTableParams);
			tableLocationsItemWhLO_inflate.addView(borderHeaderTable);

			// fill data table
			for (int i = 0; i < dataTable.length; i++) {
				TableRow eachRow = new TableRow(this);
				eachRow.setLayoutParams(new TableLayout.LayoutParams(
						LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
				for (int j = 0; j < dataTable[i].length; j++) {
					TextView column = new TextView(this);
					column.setLayoutParams(new TableRow.LayoutParams(
							LayoutParams.WRAP_CONTENT,
							LayoutParams.WRAP_CONTENT));
					column.setText(dataTable[i][j]); // set info data table
					column.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
					column.setGravity(Gravity.CENTER_HORIZONTAL);
					column.setPadding(3, 3, 3, 3);
					eachRow.addView(column);
				}

				// create button and set event click
				Button button = new Button(this);

				OnClickListener listener = new OnClickListener() {
					@Override
					public void onClick(View v) {
						// Toast.makeText(WReceiptLOActivity.this,"A button was clicked with tag "
						// + v.getTag(), Toast.LENGTH_SHORT).show();
						int positionListLocations = ((Integer) v.getTag())
								.intValue();

						recordsLocationList.get(rowRelativeLayout).remove(positionListLocations);
						//listLocationsTemp.remove(positionListLocations); // remove


						//loadTableLocationsPerItemInflate((RelativeLayout) v.getParent().getParent().getParent()); // repaint
						loadTableLocationsPerItemInflate(rowRelativeLayout); // repaint
					}
				};

				button.setOnClickListener(listener);
				button.setTag(i); // set tag, important for identify position in
				// arraylist locations
				button.setText("X");
				button.setBackgroundColor(Color.parseColor("#ffff4444"));
				button.setTextColor(Color.parseColor("#ffffff"));
				button.setTextSize(TypedValue.COMPLEX_UNIT_SP, 17);

				eachRow.addView(button); // ??

				tableLocationsItemWhLO_inflate.addView(eachRow);
			}
		} catch (Exception e) {
			Log.e("WhReceiptLOActivity",
					"err painting table locations item wh", e);
		}
	}
	// repaint table wh locations
	private void repaintTableLocationsItemWh(String dataTable[][]) {
		try {
			String headerTableData[] = { "Loc", "# Pc" };

			// reset table
			tableLocationsItemWhLO.removeAllViewsInLayout();

			/* TableLayout design layout */

			// header row table
			TableRow headerTable = new TableRow(this);
			headerTable.setLayoutParams(new TableLayout.LayoutParams(
					LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
			tableLocationsItemWhLO.addView(headerTable);

			// write header table
			for (int i = 0; i < headerTableData.length; i++) {
				TextView column = new TextView(this);
				column.setLayoutParams(new TableRow.LayoutParams(
						LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
				column.setText(headerTableData[i]);
				column.setTextSize(TypedValue.COMPLEX_UNIT_SP, 17);
				column.setTypeface(column.getTypeface(), Typeface.BOLD);
				column.setGravity(Gravity.CENTER_HORIZONTAL);
				column.setPadding(1, 1, 1, 1);
				headerTable.addView(column);
				// tableLocationsItemWhLO.setColumnStretchable(i, true);
			}

			// separator border headerTable in table
			TableRow borderHeaderTable = new TableRow(this);
			borderHeaderTable.setLayoutParams(new TableLayout.LayoutParams(
					LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
			FrameLayout lineHeaderTable = new FrameLayout(this);
			TableRow.LayoutParams lineHeaderTableParams = new TableRow.LayoutParams(
					LayoutParams.MATCH_PARENT, 2);
			lineHeaderTableParams.span = headerTableData.length + 1;
			lineHeaderTable.setBackgroundColor(Color.parseColor("#ff33b5e5"));
			borderHeaderTable.addView(lineHeaderTable, lineHeaderTableParams);
			tableLocationsItemWhLO.addView(borderHeaderTable);

			// fill data table
			for (int i = 0; i < dataTable.length; i++) {
				TableRow eachRow = new TableRow(this);
				eachRow.setLayoutParams(new TableLayout.LayoutParams(
						LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
				for (int j = 0; j < dataTable[i].length; j++) {
					TextView column = new TextView(this);
					column.setLayoutParams(new TableRow.LayoutParams(
							LayoutParams.WRAP_CONTENT,
							LayoutParams.WRAP_CONTENT));
					column.setText(dataTable[i][j]); // set info data table
					column.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
					column.setGravity(Gravity.CENTER_HORIZONTAL);
					column.setPadding(3, 3, 3, 3);
					eachRow.addView(column);
				}

				// create button and set event click
				Button button = new Button(this);

				OnClickListener listener = new OnClickListener() {
					@Override
					public void onClick(View v) {
						// Toast.makeText(WReceiptLOActivity.this,"A button was clicked with tag "
						// + v.getTag(), Toast.LENGTH_SHORT).show();
						int positionListLocations = ((Integer) v.getTag())
								.intValue();
						listLocationsTemp.remove(positionListLocations); // remove
						// location
						// per
						// position
						// in
						// arraylist
						loadTableLocationsPerItem(); // repaint
					}
				};

				button.setOnClickListener(listener);
				button.setTag(i); // set tag, important for identify position in
				// arraylist locations
				button.setText("X");
				button.setBackgroundColor(Color.parseColor("#ffff4444"));
				button.setTextColor(Color.parseColor("#ffffff"));
				button.setTextSize(TypedValue.COMPLEX_UNIT_SP, 17);

				eachRow.addView(button); // ??

				tableLocationsItemWhLO.addView(eachRow);
			}
		} catch (Exception e) {
			Log.e("WhReceiptLOActivity",
					"err painting table locations item wh", e);
		}
	}

	// repaint table wh locations
	private void repaintTabletTrackingsItemWh(String dataTable[][]) {
		try {
			String headerTableData[] = { "Tracking" };

			// reset table
			tableTrackingsItemWhLO.removeAllViewsInLayout();
			/* TableLayout design layout */

			// header row table
			TableRow headerTable = new TableRow(this);
			headerTable.setLayoutParams(new TableLayout.LayoutParams(
					LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
			tableTrackingsItemWhLO.addView(headerTable);

			// write header table
			for (int i = 0; i < headerTableData.length; i++) {
				TextView column = new TextView(this);
				column.setLayoutParams(new TableRow.LayoutParams(
						LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
				column.setText(headerTableData[i]);
				column.setTextSize(TypedValue.COMPLEX_UNIT_SP, 17);
				column.setTypeface(column.getTypeface(), Typeface.BOLD);
				column.setGravity(Gravity.CENTER_HORIZONTAL);
				column.setPadding(1, 1, 1, 1);
				headerTable.addView(column);
				// tableLocationsItemWhLO.setColumnStretchable(i, true);
			}

			// separator border headerTable in table
			TableRow borderHeaderTable = new TableRow(this);
			borderHeaderTable.setLayoutParams(new TableLayout.LayoutParams(
					LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
			FrameLayout lineHeaderTable = new FrameLayout(this);
			TableRow.LayoutParams lineHeaderTableParams = new TableRow.LayoutParams(
					LayoutParams.MATCH_PARENT, 2);
			lineHeaderTableParams.span = headerTableData.length + 1;
			lineHeaderTable.setBackgroundColor(Color.parseColor("#ff33b5e5"));
			borderHeaderTable.addView(lineHeaderTable, lineHeaderTableParams);
			tableTrackingsItemWhLO.addView(borderHeaderTable);

			// fill data table
			for (int i = 0; i < dataTable.length; i++) {
				TableRow eachRow = new TableRow(this);
				eachRow.setLayoutParams(new TableLayout.LayoutParams(
						LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
				for (int j = 0; j < dataTable[i].length; j++) {
					TextView column = new TextView(this);
					column.setLayoutParams(new TableRow.LayoutParams(
							LayoutParams.WRAP_CONTENT,
							LayoutParams.WRAP_CONTENT));
					column.setText(dataTable[i][j]); // set info data table
					column.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
					column.setWidth(this.dipToPixels(100));
					column.setGravity(Gravity.CENTER_HORIZONTAL);
					column.setPadding(1, 1, 1, 1);
					eachRow.addView(column);
				}

				// create button and set event click
				Button button = new Button(this);
				// create event listener
				OnClickListener listener = new OnClickListener() {
					@Override
					public void onClick(View v) {
						// Toast.makeText(WReceiptLOActivity.this,"A button was clicked with tag "
						// + v.getTag(), Toast.LENGTH_SHORT).show();
						int positionListTrackings = ((Integer) v.getTag())
								.intValue();
						listTrackingsTemp.remove(positionListTrackings); // remove
						// location
						// per
						// position
						// in
						// arraylist
						loadTableTrackingsPerItem(); // repaint
					}
				};

				button.setOnClickListener(listener);
				button.setTag(i); // set tag, important for identify position in
				// arraylist trackings
				button.setText("X");
				button.setBackgroundColor(Color.parseColor("#ffff4444"));
				button.setTextColor(Color.parseColor("#ffffff"));
				button.setTextSize(TypedValue.COMPLEX_UNIT_SP, 17);

				eachRow.addView(button); //

				tableTrackingsItemWhLO.addView(eachRow);
			}

		} catch (Exception e) {
			Log.e("WhReceiptLOActivity",
					"err painting table trackings item wh", e);
		}
	}

	private void repaintTabletTrackingsItemWhInflate(String dataTable[][],RelativeLayout rowRelativeLayout) {
		try {
			String headerTableData[] = { "Tracking" };

			TableLayout	tableTrackingsItemWhLO_inflate = (TableLayout) rowRelativeLayout.findViewById(R.id.tableTrackingsItemWhReceipt_inflate);
			// reset table
			tableTrackingsItemWhLO_inflate.removeAllViewsInLayout();
			/* TableLayout design layout */

			// header row table
			TableRow headerTable = new TableRow(this);
			headerTable.setLayoutParams(new TableLayout.LayoutParams(
					LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
			tableTrackingsItemWhLO_inflate.addView(headerTable);

			// write header table
			for (int i = 0; i < headerTableData.length; i++) {
				TextView column = new TextView(this);
				column.setLayoutParams(new TableRow.LayoutParams(
						LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
				column.setText(headerTableData[i]);
				column.setTextSize(TypedValue.COMPLEX_UNIT_SP, 17);
				column.setTypeface(column.getTypeface(), Typeface.BOLD);
				column.setGravity(Gravity.CENTER_HORIZONTAL);
				column.setPadding(1, 1, 1, 1);
				headerTable.addView(column);
				// tableLocationsItemWhLO.setColumnStretchable(i, true);
			}

			// separator border headerTable in table
			TableRow borderHeaderTable = new TableRow(this);
			borderHeaderTable.setLayoutParams(new TableLayout.LayoutParams(
					LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
			FrameLayout lineHeaderTable = new FrameLayout(this);
			TableRow.LayoutParams lineHeaderTableParams = new TableRow.LayoutParams(
					LayoutParams.MATCH_PARENT, 2);
			lineHeaderTableParams.span = headerTableData.length + 1;
			lineHeaderTable.setBackgroundColor(Color.parseColor("#ff33b5e5"));
			borderHeaderTable.addView(lineHeaderTable, lineHeaderTableParams);
			tableTrackingsItemWhLO_inflate.addView(borderHeaderTable);

			// fill data table
			for (int i = 0; i < dataTable.length; i++) {
				TableRow eachRow = new TableRow(this);
				eachRow.setLayoutParams(new TableLayout.LayoutParams(
						LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
				for (int j = 0; j < dataTable[i].length; j++) {
					TextView column = new TextView(this);
					column.setLayoutParams(new TableRow.LayoutParams(
							LayoutParams.WRAP_CONTENT,
							LayoutParams.WRAP_CONTENT));
					column.setText(dataTable[i][j]); // set info data table
					column.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
					column.setWidth(this.dipToPixels(100));
					column.setGravity(Gravity.CENTER_HORIZONTAL);
					column.setPadding(1, 1, 1, 1);
					eachRow.addView(column);
				}

				// create button and set event click
				Button button = new Button(this);
				// create event listener
				OnClickListener listener = new OnClickListener() {
					@Override
					public void onClick(View v) {
						// Toast.makeText(WReceiptLOActivity.this,"A button was clicked with tag "
						// + v.getTag(), Toast.LENGTH_SHORT).show();
						RelativeLayout relativeLayout = (RelativeLayout) v.getParent().getParent().getParent();
						int positionListTrackings = ((Integer) v.getTag()).intValue();

						recordsTrackingList.get(relativeLayout).remove(positionListTrackings); // remove
						// location
						// per
						// position
						// in
						// arraylist
						loadTableTrackingsPerItem_inflate(relativeLayout); // repaint
					}
				};

				button.setOnClickListener(listener);
				button.setTag(i); // set tag, important for identify position in
				// arraylist trackings
				button.setText("X");
				button.setBackgroundColor(Color.parseColor("#ffff4444"));
				button.setTextColor(Color.parseColor("#ffffff"));
				button.setTextSize(TypedValue.COMPLEX_UNIT_SP, 17);

				eachRow.addView(button); //

				tableTrackingsItemWhLO_inflate.addView(eachRow);
			}

		} catch (Exception e) {
			Log.e("WhReceiptLOActivity",
					"err painting table trackings item wh", e);
		}
	}



	// rp table RM
	private void repaintTableItemsWhRM(String dataTable[][]) {
		try {
			String headerTableData[] = { "Item", "Description",
					"Code", "Q Ordered", "Unit", "Q Arr[lb]",
					"Q Arr", "Pc", "Type","H", "L[in]","W[in]","H[in]","V[ft3]","W[lb]","W[kg]",
					"Remarks","Tracking","Locations"};

			// reset table
			tableItemWhRM.removeAllViewsInLayout();

			/* TableLayout design layout */

			// header row table
			TableRow headerTable = new TableRow(this);

			headerTable.setLayoutParams(new TableLayout.LayoutParams(
					LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
			tableItemWhRM.addView(headerTable);

			for (int i = 0; i < headerTableData.length; i++) {
				TextView column = new TextView(this);
				column.setLayoutParams(new TableRow.LayoutParams(
						LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
				column.setText(headerTableData[i]);
				column.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
				column.setTypeface(column.getTypeface(), Typeface.BOLD);
				column.setGravity(Gravity.CENTER_HORIZONTAL);
				column.setPadding(3, 3, 3, 3);
				headerTable.addView(column);
			}

			// separator border headerTable in table
			TableRow borderHeaderTable = new TableRow(this);
			borderHeaderTable.setLayoutParams(new TableLayout.LayoutParams(
					LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
			FrameLayout lineHeaderTable = new FrameLayout(this);
			TableRow.LayoutParams lineHeaderTableParams = new TableRow.LayoutParams(
					LayoutParams.MATCH_PARENT, 2);
			lineHeaderTableParams.span = headerTableData.length + 1;
			lineHeaderTable.setBackgroundColor(Color.parseColor("#ff33b5e5"));
			borderHeaderTable.addView(lineHeaderTable, lineHeaderTableParams);
			tableItemWhRM.addView(borderHeaderTable);

			// fill data table
			for (int i = 0; i < dataTable.length; i++) {
				TableRow eachRow = new TableRow(this);
				eachRow.setTag(new Integer(i));
				eachRow.setLayoutParams(new TableLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));


				for (int j = 0; j <= 4; j++) {
					TextView column = new TextView(this);
					column.setLayoutParams(new TableRow.LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT));
					column.setText(dataTable[i][j]); // set info data table
					column.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
					column.setGravity(Gravity.CENTER_HORIZONTAL);
					column.setHeight(dipToPixels(70));


					if (j == 1) {//Item description
						column.setWidth(dipToPixels(150));
						column.setGravity(Gravity.START);
					}
					column.setPadding(1, 1, 1, 1);
					eachRow.addView(column);
				}

				/*
				 * create each textedit
				 */
				final EditText qtyArrivedLBEditText = new EditText(this);
				final EditText qtyArrivedKGEditText = new EditText(this);
				final EditText nPiecesEditText = new EditText(this);

				/**
				 * create textedit for weight/volume
				 */

				final EditText length = new EditText(this);
				final EditText width = new EditText(this);
				final EditText height = new EditText(this);
				final EditText volume = new EditText(this);
				final EditText weightLb = new EditText(this);
				final EditText weightKg = new EditText(this);
				final EditText remarks = new EditText(this);
				final EditText idItem = new EditText(this);
				final RelativeLayout locationTable = getLocationsTableLayout();
				final RelativeLayout trackingTable = getTrackingTableLayout();

				recordsTrackingList.put(trackingTable,new ArrayList<String>());
				recordsLocationList.put(locationTable,new ArrayList());


				Button dialogButtonAddTracking_inflate = (Button) trackingTable
						.findViewById(R.id.buttonDialogAddEachTrackingLO_inflate);
				dialogButtonAddTracking_inflate.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						try {
							// int number pieces
							RelativeLayout rowRelativeLayoutTracking= ((RelativeLayout) v.getParent().getParent().getParent());
							TextView txtTrackingDialog = (TextView) rowRelativeLayoutTracking.findViewById(R.id.editTextDialogItemTrackingLO_inflate);
							String trackingNumber = txtTrackingDialog.getText()
									.toString();

							// add location to item wh receipt

							//listTrackingsTemp.add(trackingNumber);
							recordsTrackingList.get(rowRelativeLayoutTracking).add(trackingNumber);

							loadTableTrackingsPerItem_inflate(rowRelativeLayoutTracking);

							// clean fields
							txtTrackingDialog.setText("");

						} catch (Exception e) {
							Log.e("WReceiptLOActivity",
									"Error in button add tracking" + e);
						}
					}
				});

				// add location to wh receipt
				Button dialogButtonAddLocation = (Button) locationTable
						.findViewById(R.id.buttonDialogAddEachLocationLO_inflate);
				dialogButtonAddLocation.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						try {
							// int number pieces
							RelativeLayout rowRelativeLayoutLocation = ((RelativeLayout) v.getParent().getParent().getParent());

							TextView txtNumberPiecesLocationDialog = (TextView) rowRelativeLayoutLocation
									.findViewById(R.id.editTextDialogItemLocationNPiecesLO_inflate);
							int cargoPiecesPerLocation = Integer
									.parseInt(txtNumberPiecesLocationDialog
											.getText().toString());

							// place item location
							TextView txtPlaceLocationDialog = (TextView) rowRelativeLayoutLocation
									.findViewById(R.id.editTextDialogItemLocationPlaceLO_inflate);
							String cargoPlacePerLocation = txtPlaceLocationDialog
									.getText().toString();

							// Machetin de busqueda de zonas como string para
							// obtener el id
							ArrayList<ModelLocation> locationList = ControlApp
									.getInstance().getControlListLocations()
									.getlistLocations();
							int id = 0;
							for (ModelLocation modelLocation : locationList) {
								if (modelLocation.getLocation().equals(
										cargoPlacePerLocation)) {
									id = modelLocation.getId();
								}

							}
							// add location to item wh receipt
							recordsLocationList.get(rowRelativeLayoutLocation).add(new ModelLocationItemWh(
									cargoPiecesPerLocation, cargoPlacePerLocation,id,
									cargoPlacePerLocation));
							/*listLocationsTemp.add(new ModelLocationItemWh(
									cargoPiecesPerLocation, cargoPlacePerLocation,id,
									cargoPlacePerLocation));*/

							loadTableLocationsPerItemInflate(rowRelativeLayoutLocation);

							// clean fields
							txtNumberPiecesLocationDialog.setText("");
							txtPlaceLocationDialog.setText("");
							txtPlaceLocationDialog.requestFocus(); // focus to
							// location
							// input

						} catch (NumberFormatException e) {
							Toast.makeText(WReceiptLOActivity.this,
									"N. Pieces and Location are required",
									Toast.LENGTH_SHORT).show();
						}
					}

				});




				idItem.setVisibility(View.GONE);
				idItem.setTag("idItem");
				idItem.setText(dataTable[i][10]);

				length.addTextChangedListener(getTextListenerForVolume(width,length,height,volume));
				width.addTextChangedListener(getTextListenerForVolume(width,length,height,volume));
				height.addTextChangedListener(getTextListenerForVolume(width,length,height,volume));

				/*Fix method call*/

				weightLb.setOnFocusChangeListener(getFocusListenerForWeightLb(weightLb,weightKg,qtyArrivedKGEditText,nPiecesEditText));
				weightKg.setOnFocusChangeListener(getFocusListenerForWeightKG(weightKg,weightLb,qtyArrivedKGEditText,nPiecesEditText));

				length.setTag("length");
				width.setTag("width");
				height.setTag("height");
				volume.setTag("volume");
				weightLb.setTag("weightLb");
				weightKg.setTag("weightKg");
				remarks.setTag("remarks");

				remarks.setWidth(dipToPixels(110));

				TextView[] sizeViews = new TextView[]{length,width,height,weightLb,weightKg};

				for(TextView sizeView : sizeViews){
					sizeView.setEnabled(false);
					sizeView.setInputType(InputType.TYPE_CLASS_NUMBER
							| InputType.TYPE_NUMBER_FLAG_DECIMAL
							| InputType.TYPE_NUMBER_FLAG_SIGNED);
					sizeView.setWidth(dipToPixels(80));
				}
				//volume must be calculated, not input
				volume.setEnabled(false);
				volume.setWidth(dipToPixels(80));


				//final EditText nPiecesEditText = new EditText(this);

				/* quantity LB */
				// create button and set event click
				String qtyArrivedLB = dataTable[i][5]; // entered
				if (qtyArrivedLB.equals("0.0")) {
					qtyArrivedLB = "";
				}
				//qtyArrivedLBEditText.setHint("qty");
				qtyArrivedLBEditText.setInputType(InputType.TYPE_CLASS_NUMBER
						| InputType.TYPE_NUMBER_FLAG_DECIMAL
						| InputType.TYPE_NUMBER_FLAG_SIGNED);
				qtyArrivedLBEditText.setWidth(dipToPixels(65));
				qtyArrivedLBEditText.setText(qtyArrivedLB);
				qtyArrivedLBEditText.setTag(i);
				qtyArrivedLBEditText
						.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);

				qtyArrivedLBEditText.addTextChangedListener(getTextChangeListenerForQtyReceiveLb(qtyArrivedLBEditText,qtyArrivedKGEditText));
				// on focus event
				qtyArrivedLBEditText.setOnFocusChangeListener(getFocusListenerForQtyArrivedLB(qtyArrivedLBEditText,qtyArrivedKGEditText));

				/* quantity KG */
				// create button and set event click
				String qtyArrivedKG = dataTable[i][6]; // entered
				if (qtyArrivedKG.equals("0.0")) {
					qtyArrivedKG = "";
				}
				//qtyArrivedKGEditText.setHint("qty");
				qtyArrivedKGEditText.setInputType(InputType.TYPE_CLASS_NUMBER
						| InputType.TYPE_NUMBER_FLAG_DECIMAL
						| InputType.TYPE_NUMBER_FLAG_SIGNED);
				qtyArrivedKGEditText.setWidth(dipToPixels(65));
				qtyArrivedKGEditText.setText(qtyArrivedKG);
				qtyArrivedKGEditText.setTag(i);
				qtyArrivedKGEditText
						.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);

				qtyArrivedKGEditText.addTextChangedListener(getTextChangeListenerForQtyReceiveKG(qtyArrivedKGEditText,qtyArrivedLBEditText));
				// on focus event
				qtyArrivedKGEditText.setOnFocusChangeListener(getFocusListenerForQtyArrivedKG(qtyArrivedKGEditText,qtyArrivedLBEditText));

				/* n pirces arrived */
				// create button and set event click
				String nPieces = dataTable[i][7]; // entered
				if (nPieces.equals("0")) {
					nPieces = "";
				}
				//nPiecesEditText.setHint("pieces");
				nPiecesEditText.setInputType(InputType.TYPE_CLASS_NUMBER);
				nPiecesEditText.setWidth(dipToPixels(50));
				nPiecesEditText.setText(nPieces);
				nPiecesEditText.setTag(i);
				nPiecesEditText.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);

				nPiecesEditText.addTextChangedListener(getTextChangeListenerForPieces(nPiecesEditText,sizeViews));

				/* spinner select unit type */
				// init spinner type unit
				int positionSpinner = Integer.parseInt(dataTable[i][7]); // entered
				final Spinner spinnerTypeUnit = new Spinner(this);
				ArrayList<String> arrayDataTypeUnit = getArrayAdapterDataSpinnerUnitType();

				ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(
						WReceiptLOActivity.this,
						android.R.layout.simple_spinner_item, arrayDataTypeUnit);
				dataAdapter
						.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
				spinnerTypeUnit.setAdapter(dataAdapter);
				spinnerTypeUnit.setSelection(positionSpinner);
				spinnerTypeUnit.setTag(i);
				// events change spinner (it's not used yet)
				spinnerTypeUnit.setOnItemSelectedListener(getItemSelectListenerForTypeUnit(spinnerTypeUnit));

				CheckBox chkHazardous = new CheckBox(this);
				chkHazardous.setTag("hazardous");
				chkHazardous.setChecked(new Boolean(dataTable[i][9]));
				Log.e("is haz enable",(dataTable[i][9]));
				//chkHazardous.setWidth(dipToPixels(50));

				/* add to row textedit */
				eachRow.addView(qtyArrivedLBEditText);
				eachRow.addView(qtyArrivedKGEditText);
				eachRow.addView(nPiecesEditText);
				eachRow.addView(spinnerTypeUnit);

				eachRow.addView(chkHazardous);

				eachRow.addView(length);
				eachRow.addView(width);
				eachRow.addView(height);
				eachRow.addView(volume);
				eachRow.addView(weightLb);
				eachRow.addView(weightKg);
				eachRow.addView(remarks);
				eachRow.addView(trackingTable);
				eachRow.addView(locationTable);




				//Hidden Field, does not paint
				eachRow.addView(idItem);


				// add row in table, important!
				tableItemWhRM.addView(eachRow);

				loadDataLocationsInflate(eachRow);

			}

		} catch (Exception e) {
			Log.e("WhReceiptLOActivity", "err painting table items RM wh", e);
		}
	}

	private RelativeLayout getTrackingTableLayout() {

		RelativeLayout rl = (RelativeLayout) LayoutInflater.from(this).inflate(R.layout.tracking_number_layout, null);;

		return rl;
	}

	private RelativeLayout getLocationsTableLayout() {

		RelativeLayout rl = (RelativeLayout) LayoutInflater.from(this).inflate(R.layout.locations_layout, null);;

		return rl;
	}

	private void calculateVolume(TextView txtWidth,TextView txtlength, TextView txtHeight, TextView txtVolumen){

		if(txtWidth.getEditableText().toString().equals("") ||
				txtHeight.getEditableText().toString().equals("") ||
				txtlength.getEditableText().toString().equals("")){
			txtVolumen.setText("0");
		}else{
			double width = Double.parseDouble(txtWidth.getEditableText().toString());
			double length = Double.parseDouble(txtlength.getEditableText().toString());
			double height = Double.parseDouble(txtHeight.getEditableText().toString());
			txtVolumen.setText(new DecimalFormat("#0.00").format(((width*length*height)/1728)));
		}
	}

	private OnItemSelectedListener getItemSelectListenerForTypeUnit(final Spinner spinnerTypeUnit) {
		return new OnItemSelectedListener() {
			@Override
			public void onItemSelected(
					AdapterView<?> parentView,
					View selectItemView, int position, long id) {
				// Toast.makeText(parentView.getContext()," selected "
				// +
				// parentView.getItemAtPosition(position).toString(),
				// Toast.LENGTH_SHORT).show();
				int positionItemRM = (Integer) spinnerTypeUnit
						.getTag();
				ModelMasterValue typeUnit = ControlApp
						.getInstance()
						.getControlListMasterValues()
						.getListUnitType().get(position);
				// update id position spinner
				ControlApp.getInstance().getControlWhReceipt()
						.getModelWhReceipt()
						.getListItemsRawMaterials()
						.get(positionItemRM)
						.setPositionUnitType(position);
				ControlApp.getInstance().getControlWhReceipt()
						.getModelWhReceipt()
						.getListItemsRawMaterials()
						.get(positionItemRM)
						.setUnitType(typeUnit);
			}

			@Override
			public void onNothingSelected(
					AdapterView<?> parentView) {

			}
		};
	}

	private OnFocusChangeListener getFocusListenerForQtyArrivedKG(final EditText qtyArrivedKGEditText,final EditText qtyArrivedLBEditText){
		return new OnFocusChangeListener() {
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if (hasFocus) {
					int position = (Integer) qtyArrivedKGEditText.getTag();
					ModelItemRawMaterials item = ControlApp
							.getInstance()
							.getControlWhReceipt()
							.getModelWhReceipt()
							.getListItemsRawMaterials()
							.get(position);
					if (item.getQtyArrivedKG() > 0.0) {
						qtyArrivedKGEditText.setText(item
								.getQtyArrivedKG() + ""); // update
						// field
					} else {
						qtyArrivedKGEditText.setText("");
					}

				}else{
					if (!qtyArrivedKGEditText.getText().toString().equals("")) {
						double qtyArrivedKG = Double.parseDouble(qtyArrivedKGEditText.getText().toString());
						double qtyArrivedLB = qtyArrivedKG / 0.45359237; // converter
						qtyArrivedLB = roundDecimal(qtyArrivedLB, 2); // round
						qtyArrivedLBEditText.setText(String.valueOf(qtyArrivedLB));
					}
				}
			}
		};
	}

	private TextWatcher getTextChangeListenerForPieces(final EditText nPiecesEditText,final TextView sizeViews[]) {
		return new TextWatcher() {
			@Override
			public void afterTextChanged(Editable s) {
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start,
										  int count, int after) {
			}

			@Override
			public void onTextChanged(CharSequence s, int start,
									  int before, int count) {
				int position = (Integer) nPiecesEditText.getTag();
				int nPieces = 0;
				if (!((String) s.toString()).equals("")) {
					nPieces = Integer.parseInt((String) s.toString());
				}
				//If zero disabled views for size info
				if(nPieces <= 0){
					for (TextView textView:sizeViews){
						textView.setEnabled(false);
					}
					//else enable views for size info
				}else{
					for (TextView textView:sizeViews){
						textView.setEnabled(true);
					}

				}

				ControlApp.getInstance().getControlWhReceipt()
						.getModelWhReceipt()
						.changeNPiecesArrivedRM(position, nPieces);
			}
		};
	}
	private TextWatcher getTextChangeListenerForRemarks(final EditText txtRemarks) {
		return new TextWatcher() {
			@Override
			public void afterTextChanged(Editable s) {
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start,
										  int count, int after) {
			}

			@Override
			public void onTextChanged(CharSequence s, int start,
									  int before, int count) {
				int position = (Integer) txtRemarks.getTag();
				String remarks = s.toString();


				ControlApp.getInstance().getControlWhReceipt()
						.getModelWhReceipt()
						.changeRemarksRM(position, remarks);
			}
		};
	}

	private TextWatcher getTextChangeListenerForQtyReceiveLb(final EditText sourceLb, final EditText targetKg) {
		return new TextWatcher() {
			@Override
			public void afterTextChanged(Editable s) {
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start,
										  int count, int after) {
			}

			@Override
			public void onTextChanged(CharSequence s, int start,
									  int before, int count) {
				int position = (Integer) sourceLb.getTag();
				double qtyArrivedLB = 0L;
				double qtyArrivedKG = 0L;
				if (!((String) s.toString()).equals("")) {
					try {
						qtyArrivedLB = Double.parseDouble(s.toString());
						qtyArrivedKG = qtyArrivedLB * 0.45359237; // converter
						qtyArrivedKG = roundDecimal(qtyArrivedKG, 2); // round

						// 2
						// dec
						ControlApp
								.getInstance()
								.getControlWhReceipt()
								.getModelWhReceipt()
								.changeQuantityArrivedRM(position,
										qtyArrivedLB, qtyArrivedKG);
					} catch (Exception e) {
						Log.e("WhReceiptLOActivity",
								"************ error fortmat or converter LB to KG",
								e);
					}
				}
			}


		};
	}
	private OnFocusChangeListener getFocusListenerForWeightLb(final EditText editTextSourceLb, final EditText editTextTargetKg,
															  final EditText editTextQtyArrive,final EditText editTextNPieces) {
		return new OnFocusChangeListener() {
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				//if (hasFocus) {

				double weightLB = 0L;
				double weightKG = 0L;
				if (!editTextSourceLb.getText().toString().equals("")) {
					try {
						weightLB = Double.parseDouble(editTextSourceLb.getText().toString());
						weightKG = weightLB * 0.45359237; // converter
						weightKG = roundDecimal(weightKG, 2); // round
						editTextTargetKg.setText(String.valueOf(weightKG));

							/*Validacion del weight discrepancy*/
						if(!editTextQtyArrive.getText().toString().equals("") && !editTextNPieces.getText().toString().equals("")) {
							double qtyArrived = Double.parseDouble(editTextQtyArrive.getText().toString());
							int nPieces = Integer.parseInt(editTextNPieces.getText().toString());
							validateWeightDiscrepancy(weightKG, qtyArrived, nPieces);
						}
					}catch(WeightException e){
						Toast toast = Toast.makeText(WReceiptLOActivity.this,e.getMessage(),Toast.LENGTH_SHORT);
						toast.setGravity(Gravity.TOP | Gravity.RIGHT, 20, 20);
						toast.show();
					} catch (Exception e) {
						Log.e("WhReceiptLOActivity","************ error fortmat or converter LB to KG",e);
					}
				}
			}
			// }
		};
	}
	private TextWatcher getTextChangeListenerForQtyReceiveKG(final EditText sourceKg, final EditText targetLb){
		return new TextWatcher() {
			@Override
			public void afterTextChanged(Editable s) {
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start,
										  int count, int after) {
			}

			@Override
			public void onTextChanged(CharSequence s, int start,
									  int before, int count) {
				int position = (Integer) sourceKg.getTag();
				double qtyArrivedLB = 0;
				double qtyArrivedKG = 0;
				if (!(s.toString()).equals("")) {
					try {
						qtyArrivedKG = Double.parseDouble((String) s
								.toString());
						qtyArrivedLB = qtyArrivedKG / 0.45359237; // converter
						qtyArrivedLB = roundDecimal(qtyArrivedLB, 2); // round
						// 2
						// dec
						ControlApp
								.getInstance()
								.getControlWhReceipt()
								.getModelWhReceipt()
								.changeQuantityArrivedRM(position,
										qtyArrivedLB, qtyArrivedKG);
					} catch (Exception e) {
						Log.e("WhReceiptLOActivity",
								"************ error fortmat or converter KG to LB",
								e);
					}

				}
			}
		};
	}

	private OnFocusChangeListener getFocusListenerForWeightKG(final EditText editTextSourceKg, final EditText editTextTargetLb,
															  final EditText editTextQtyArrive,final EditText editTextNPieces){
		return new OnFocusChangeListener() {
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				//if (hasFocus) {
				double weightLB = 0;
				double weightKG = 0;
				if (!(editTextSourceKg.getText().toString()).equals("")) {
					try {
						weightKG = Double.parseDouble(editTextSourceKg.getText().toString());
						weightLB = weightKG / 0.45359237; // converter
						weightLB = roundDecimal(weightLB, 2); // round
						editTextTargetLb.setText(String.valueOf(weightLB));

							/*Validacion del weight discrepancy*/
						if (!editTextQtyArrive.getText().toString().equals("") && !editTextNPieces.getText().toString().equals("")) {
							double qtyArrived = Double.parseDouble(editTextQtyArrive.getText().toString());
							int nPieces = Integer.parseInt(editTextNPieces.getText().toString());
							validateWeightDiscrepancy(weightKG, qtyArrived, nPieces);
						}
					}catch(WeightException e){
						Toast toast = Toast.makeText(WReceiptLOActivity.this,e.getMessage(),Toast.LENGTH_SHORT);
						toast.setGravity(Gravity.TOP | Gravity.RIGHT, 20, 20);
						toast.show();
					} catch (Exception e) {
						Log.e("WhReceiptLOActivity","************ error fortmat or converter KG to LB",e);
					}

				}
			}
			//}
		};
	}

	private TextWatcher getTextChangeListenerForQtyArriveKG(final EditText sourceKg,final EditText targetLb){
		return new TextWatcher() {
			@Override
			public void afterTextChanged(Editable s) {
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start,
										  int count, int after) {
			}

			@Override
			public void onTextChanged(CharSequence s, int start,
									  int before, int count) {
				int position = (Integer) sourceKg
						.getTag();
				ModelItemRawMaterials item = ControlApp
						.getInstance()
						.getControlWhReceipt()
						.getModelWhReceipt()
						.getListItemsRawMaterials()
						.get(position);
				if (item.getQtyArrivedLB() > 0.0) {
					targetLb.setText(item
							.getQtyArrivedLB() + "");
				} else {
					targetLb.setText("");
				}
			}
		};
	}

	private OnFocusChangeListener getFocusListenerForQtyArrivedLB(final EditText qtyArrivedLBEditText,final EditText qtyArrivedKGEditText){
		return new OnFocusChangeListener() {
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if (hasFocus) {
					int position = (Integer) qtyArrivedLBEditText
							.getTag();
					ModelItemRawMaterials item = ControlApp
							.getInstance()
							.getControlWhReceipt()
							.getModelWhReceipt()
							.getListItemsRawMaterials()
							.get(position);
					if (item.getQtyArrivedLB() > 0.0) {
						qtyArrivedLBEditText.setText(item
								.getQtyArrivedLB() + "");
					} else {
						qtyArrivedLBEditText.setText("");
					}

				}else{
					if (!qtyArrivedKGEditText.getText().toString().equals("")) {
						double qtyArrivedLB = Double.parseDouble(qtyArrivedLBEditText.getText().toString());
						double qtyArrivedKG = qtyArrivedLB * 0.45359237; // converter
						qtyArrivedKG = roundDecimal(qtyArrivedKG, 2); // round
						qtyArrivedKGEditText.setText(String.valueOf(qtyArrivedKG));
					}
				}
			}
		};
	}
	private TextWatcher getTextListenerForWeightLb(final EditText sourceLb,final EditText targetKg) {
		return new TextWatcher() {
			@Override
			public void afterTextChanged(Editable s) {
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start,
										  int count, int after) {
			}

			@Override
			public void onTextChanged(CharSequence s, int start,
									  int before, int count) {
				int position = (Integer) sourceLb.getTag();
				double qtyArrivedLB = 0L;
				double qtyArrivedKG = 0L;
				if (!((String) s.toString()).equals("")) {
					try {
						qtyArrivedLB = Double.parseDouble(s.toString());
						qtyArrivedKG = qtyArrivedLB * 0.45359237; // converter
						qtyArrivedKG = roundDecimal(qtyArrivedKG, 2); // round
						targetKg.setText(String.valueOf(qtyArrivedKG));
						// 2
						// dec
						ControlApp
								.getInstance()
								.getControlWhReceipt()
								.getModelWhReceipt()
								.changeQuantityArrivedRM(position,
										qtyArrivedLB, qtyArrivedKG);
					} catch (Exception e) {
						Log.e("WhReceiptLOActivity",
								"************ error fortmat or converter LB to KG",
								e);
					}
				}
			}


		};
	}
	private TextWatcher getTextListenerForVolume(final TextView txtWidth,final TextView txtlength,final TextView txtHeight,final TextView txtVolumen) {
		return new TextWatcher() {
			@Override
			public void afterTextChanged(Editable s) {
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start,
										  int count, int after) {
			}

			@Override
			public void onTextChanged(CharSequence s, int start,
									  int before, int count) {
				calculateVolume(txtWidth,txtlength,txtHeight,txtVolumen);
			}


		};
	}

	// repaint table IP
	private void repaintTableItemsWhIP(String dataTable[][]) {
		try {
			String headerTableData[] = { "Item", "Supplier desc",
					"Supplier ref", "Client description", "Client reference",
					"Qty Order", "Unit", "Qty Arrived" };

			// reset table
			tableItemWhIP.removeAllViewsInLayout();

			/* TableLayout design layout */

			// header row table
			TableRow headerTable = new TableRow(this);
			headerTable.setLayoutParams(new TableLayout.LayoutParams(
					LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
			tableItemWhIP.addView(headerTable);

			// write header table
			for (int i = 0; i < headerTableData.length; i++) {
				TextView column = new TextView(this);
				column.setLayoutParams(new TableRow.LayoutParams(
						LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
				column.setText(headerTableData[i]);
				column.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
				column.setTypeface(column.getTypeface(), Typeface.BOLD);
				column.setGravity(Gravity.CENTER_HORIZONTAL);
				column.setPadding(3, 3, 3, 3);
				headerTable.addView(column);
			}

			// separator border headerTable in table
			TableRow borderHeaderTable = new TableRow(this);
			borderHeaderTable.setLayoutParams(new TableLayout.LayoutParams(
					LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
			FrameLayout lineHeaderTable = new FrameLayout(this);
			TableRow.LayoutParams lineHeaderTableParams = new TableRow.LayoutParams(
					LayoutParams.MATCH_PARENT, 2);
			lineHeaderTableParams.span = headerTableData.length + 1;
			lineHeaderTable.setBackgroundColor(Color.parseColor("#ff33b5e5"));
			borderHeaderTable.addView(lineHeaderTable, lineHeaderTableParams);
			tableItemWhIP.addView(borderHeaderTable);

			//Log.e("este es el numero de datos", "======numero: "+ dataTable.length);
			// fill data table
			for (int i = 0; i < dataTable.length; i++) {
				TableRow eachRow = new TableRow(this);
				eachRow.setLayoutParams(new TableLayout.LayoutParams(
						LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
				for (int j = 0; j < dataTable[i].length - 1 /*
															 * the last one is
															 * input text
															 */; j++) {
					TextView column = new TextView(this);
					column.setLayoutParams(new TableRow.LayoutParams(
							LayoutParams.WRAP_CONTENT,
							LayoutParams.WRAP_CONTENT));

					column.setText(dataTable[i][j]); // set info data table
					column.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
					column.setGravity(Gravity.CENTER_HORIZONTAL);

					if (j == 1) {
						column.setWidth(dipToPixels(210));
						column.setMinHeight(dipToPixels(200));
						column.setGravity(Gravity.LEFT);
					}
					if (j == 2) {
						column.setWidth(dipToPixels(210));
						column.setMinHeight(dipToPixels(200));
						column.setGravity(Gravity.LEFT);
					}
					if (j == 3) {
						column.setWidth(dipToPixels(210));
						column.setMinHeight(dipToPixels(200));
						column.setGravity(Gravity.LEFT);
					}
					if (j == 4) {
						column.setWidth(dipToPixels(210));
						column.setMinHeight(dipToPixels(200));
						column.setGravity(Gravity.LEFT);
					}
					column.setPadding(1, 1, 1, 1);

					eachRow.addView(column);
				}

				// create button and set event click
				String qtyEntered = dataTable[i][7]; // entered
				if (qtyEntered.equals("0")) {
					qtyEntered = "";
				}
				final EditText qtyArrivedEditText = new EditText(this);
				qtyArrivedEditText.setHint("qty");
				qtyArrivedEditText.setInputType(InputType.TYPE_CLASS_NUMBER);
				qtyArrivedEditText.setWidth(dipToPixels(65));
				qtyArrivedEditText.setText(qtyEntered);
				qtyArrivedEditText.setTag(i);
				qtyArrivedEditText.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);

				qtyArrivedEditText.addTextChangedListener(new TextWatcher() {

					@Override
					public void afterTextChanged(Editable s) {
						// TODO Auto-generated method stub
					}

					@Override
					public void beforeTextChanged(CharSequence s, int start,
												  int count, int after) {
						// TODO Auto-generated method stub
					}

					@Override
					public void onTextChanged(CharSequence s, int start,
											  int before, int count) {
						int position = (Integer) qtyArrivedEditText.getTag();
						int qty = 0;
						if (!((String) s.toString()).equals("")) {
							qty = Integer.parseInt((String) s.toString());
						}
						int response = ControlApp.getInstance()
								.getControlWhReceipt().getModelWhReceipt()
								.changeQuantityItemIP(position, qty);
						if (response == -1) {
							Toast toast = Toast.makeText(
									WReceiptLOActivity.this,
									"Qty arrived is not correct",
									Toast.LENGTH_SHORT);
							toast.setGravity(Gravity.TOP | Gravity.RIGHT, 20,
									20);
							toast.show();
						}
					}
				});

				eachRow.addView(qtyArrivedEditText);

				// add row in table, important!
				tableItemWhIP.addView(eachRow);
			}
		} catch (Exception e) {
			Log.e("WhReceiptLOActivity", "err painting table items IP wh", e);
		}
	}

	private void initComponents() {
		try {

			// init arrays!
			listLocationsTemp = new ArrayList<ModelLocationItemWh>();
			listTrackingsTemp = new ArrayList<String>();

			// init table layout
			tableItemsWhLO = (TableLayout) findViewById(R.id.tblItemListLO);

			// init auto complete truck company
			txtAutoTruckCompany = (AutoCompleteTextView) findViewById(R.id.txtAutoTruckCompany);// auto
			// complete
			// component
			txtAutoTruckCompany
					.setOnItemClickListener(new OnItemClickListener() {
						@Override
						public void onItemClick(AdapterView<?> parent,
												View view, int position, long id) {
							String itemSelected = (String) parent
									.getItemAtPosition(position);
							txtAutoTruckCompany.setText(itemSelected);
							ModelCarrier truckObj = ControlApp.getInstance()
									.getControlListCarrier()
									.findTruckCompanyByName(itemSelected);
							if (truckObj != null) {
								ControlApp.getInstance().getControlWhReceipt()
										.getModelWhReceipt()
										.setTruckCompany(truckObj);
							}
						}
					});

			Button btnAddWhItem = (Button) findViewById(R.id.btnAddItemWhLO);
			// If RM hide the Add Item button
			if(ControlApp.getInstance().getControlWhReceipt().getModelWhReceipt().getIdDep() == 2){
				btnAddWhItem.setVisibility(View.GONE);
			}

		} catch (Exception e) {
			Log.e("WhReceiptLOActivity", "init Components exception ", e);
		}
	}

	/*
	 * load data truck companies
	 */
	private void loadDataTruckCompany() {
		try {
			if (ControlApp.getInstance().getControlListCarrier()
					.getListTruckCompany().size() == 0) {
				String service = "ServiceCarrier";
				String[][] params = { { "operation", "getListTruckCompanies" } };
				String callback = "callbackGetListTruckCompanies";
				String loadingMessage = "Loading Truck Companies";

				ModelService objService = new ModelService(service, params,
						callback, loadingMessage);

				/*TaskAsynCallService callServiceTask = new TaskAsynCallService();
				callServiceTask.execute(objService);*/
				new TruckCompaniesAsyncTask(GeneralServicesImpl.getServicesInstance(),this).execute();
			} else {
				loadDataAutoTruckCompany();
			}
		} catch (Exception ex) {
			Log.e("WhReceiptLOActivity",
					"Error in method loadDataTruckCompany ", ex);
		}
	}

	public void callbackGetListTruckCompanies(List<TruckCompany> truckCompanies) {
			for (TruckCompany truckCompany:truckCompanies) {

				int idCarrier = Integer.parseInt(truckCompany.getIdCarrier());
				String nameCarrier = truckCompany.getName();

				ControlApp
						.getInstance()
						.getControlListCarrier()
						.addTruckCompany(
								new ModelCarrier(idCarrier, nameCarrier));
			}
			loadDataAutoTruckCompany();
	}

	private void loadDataAutoTruckCompany() {
		try {
			ArrayList<String> arrayAdapter = new ArrayList<String>();

			ArrayList<ModelCarrier> listTruckCompany = ControlApp.getInstance()
					.getControlListCarrier().getListTruckCompany();
			for (ModelCarrier eachCarrier : listTruckCompany) {
				arrayAdapter.add(eachCarrier.getCarrier());
			}
			loadAdapterAutoCompleteTruckCompany(arrayAdapter);
		} catch (Exception e) {
			Log.e("WhReceiptLOActivity", "Error get list clients ", e);
		}
	}

	private void loadAdapterAutoCompleteTruckCompany(
			ArrayList<String> arrayAdapter) {
		try {
			ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
					android.R.layout.simple_list_item_1, arrayAdapter);
			txtAutoTruckCompany.setAdapter(adapter);
		} catch (Exception e) {
			Log.e("WhReceiptLOActivity",
					"Error in method loadAdapterAutoCompleteTruckCompany ", e);
		}
	}

	/*
	 * load data locations wh
	 */
	private void loadDataLocations() {
		try {
			if (ControlApp.getInstance().getControlListLocations()
					.getlistLocations().size() == 0) {
				String service = "ServiceLocations";
				String[][] params = { { "operation", "getListLocations" } };
				String callback = "callbackGetListLocations";
				String loadingMessage = "Loading locations";

				ModelService objService = new ModelService(service, params,
						callback, loadingMessage);

				/*TaskAsynCallService callServiceTask = new TaskAsynCallService();
				callServiceTask.execute(objService);*/

				new LocationListAsyncTask(GeneralServicesImpl.getServicesInstance(),this).execute();

			} else {
				loadDataAutoLocations(); // dialog always is destroyed so,
				// should be loaded again
			}
		} catch (Exception ex) {
			Log.e("WhReceiptLOActivity", "Error in method loadDataLocations ",
					ex);
		}
	}
	private void loadDataLocationsInflate(TableRow row) {
		try {
			if (ControlApp.getInstance().getControlListLocations()
					.getlistLocations().size() == 0) {
				String service = "ServiceLocations";
				String[][] params = { { "operation", "getListLocations" } };
				String callback = "callbackGetListLocationsInflate";
				String loadingMessage = "Loading locations";

				ModelService objService = new ModelService(service, params,
						callback, loadingMessage);

				TaskAsynCallService callServiceTask = new TaskAsynCallService();
				callServiceTask.execute(objService);
			} else {
				loadDataAutoLocationsInflate(row); // dialog always is destroyed so,
				// should be loaded again
			}
		} catch (Exception ex) {
			Log.e("WhReceiptLOActivity", "Error in method loadDataLocations ",
					ex);
		}
	}

	public void callbackGetListLocations(List<Location> locations) {
			for (Location location : locations) {

				String strLocation = location.getLoc();
				int id = Integer.parseInt(location.getWhReceiptItemLocationId());
				ControlApp.getInstance().getControlListLocations()
						.addLocation(new ModelLocation(strLocation, id));
			}
			loadDataAutoLocations();
			}
	private void callbackGetListLocationsInflate(JSONObject jsonResponse) {
		try {
			JSONArray data = new JSONArray(jsonResponse.getString("result"));
			for (int i = 0; i < data.length(); i++) {
				JSONObject eachClientResponse = data.getJSONObject(i);
				String location = eachClientResponse.getString("lc");
				int id = eachClientResponse.getInt("id");
				ControlApp.getInstance().getControlListLocations()
						.addLocation(new ModelLocation(location, id));
			}
			loadDataAutoLocationsInflate(null);
		} catch (JSONException ex) {
			Log.e("WhReceiptLOActivity",
					"Error parsing json in method callbackGetListUnitType ", ex);
		}
	}

	private void loadDataAutoLocations() {
		try {
			ArrayList<String> arrayAdapter = new ArrayList<String>();

			ArrayList<ModelLocation> listLocations = ControlApp.getInstance()
					.getControlListLocations().getlistLocations();
			for (ModelLocation eachLocation : listLocations) {
				arrayAdapter.add(eachLocation.getLocation());
			}
			loadAdapterAutoCompleteLocations(arrayAdapter);
		} catch (Exception e) {
			Log.e("WhReceiptLOActivity",
					"Error get list locations in method loadDataAutoLocations ",
					e);
		}
	}
	private void loadDataAutoLocationsInflate(TableRow row) {

		ArrayList<String> arrayAdapter = new ArrayList<String>();

		ArrayList<ModelLocation> listLocations = ControlApp.getInstance()
				.getControlListLocations().getlistLocations();
		for (ModelLocation eachLocation : listLocations) {
			arrayAdapter.add(eachLocation.getLocation());
		}


		// if(row==null){
			int tablerowsCount = tableItemWhRM.getChildCount();
			for(int i=1;i<tablerowsCount;i++){
				row = (TableRow) tableItemWhRM.getChildAt(i);
				loadAdapterAutoCompleteLocationsInflate(row,arrayAdapter);
			}
		// }
		try {


		} catch (Exception e) {
			Log.e("WhReceiptLOActivity",
					"Error get list locations in method loadDataAutoLocations ",
					e);
		}
	}

	private void loadAdapterAutoCompleteLocations(ArrayList<String> arrayAdapter) {
		try {
			ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
					android.R.layout.simple_list_item_1, arrayAdapter);
			txtAutoLocations.setAdapter(adapter);
		} catch (Exception e) {
			Log.e("WhReceiptLOActivity",
					"Error in method loadAdapterAutoCompleteLocations ", e);
		}
	}
	private void loadAdapterAutoCompleteLocationsInflate(TableRow row,ArrayList<String> arrayAdapter) {
		if(row.getChildCount() <= 1){
			return;
		}
		try {
			ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
					android.R.layout.simple_list_item_1, arrayAdapter);
			final AutoCompleteTextView txtAutoLocationsInflate = (AutoCompleteTextView) row.findViewById(R.id.editTextDialogItemLocationPlaceLO_inflate);
			txtAutoLocationsInflate.setAdapter(adapter);

			txtAutoLocationsInflate.setOnFocusChangeListener(new OnFocusChangeListener() {
				@Override
				public void onFocusChange(View v, boolean hasFocus) {
					if (!hasFocus) {
						String textInput = txtAutoLocationsInflate.getText().toString();
						ListAdapter adapter = txtAutoLocationsInflate.getAdapter();
						boolean textFromList = false;
						if (adapter!=null) {
							int size = adapter.getCount();
							for (int i= 0 ;i<size;i++){
								if(textInput.equals(adapter.getItem(i))){
									textFromList = true;
									break;
								}
							}
						}
						if(!textFromList){
							txtAutoLocationsInflate.setText("");
						}
					}
				}
			});

		} catch (Exception e) {
			Log.e("WhReceiptLOActivity","Error in method loadAdapterAutoCompleteLocations ", e);
		}
	}

	/*
	 * load data unit type
	 */
	private void loadDataUnitTypes() {
			if (ControlApp.getInstance().getControlListMasterValues().getListUnitType().size() == 0) {
				new MasterValuesAsyncTask(IConstants.MasterValues.UNITTYPE,GeneralServicesImpl.getServicesInstance(),this).execute();
			} else {
				showGUIDepartment(); /* important!!!!******************* */
			}

	}

	public void callbackGetListUnitType(List<MasterValuesResponse> valuesResponses) {
			for (MasterValuesResponse masterValuesResponse : valuesResponses) {
				int masterId = Integer.parseInt(masterValuesResponse.getMasterId());
				int valueId = Integer.parseInt(masterValuesResponse.getValueId());
				String value = masterValuesResponse.getValue();
				ControlApp
						.getInstance()
						.getControlListMasterValues()
						.addUnitType(
								new ModelMasterValue(masterId, valueId, value));
			}
			Log.d("show gui department", "show in callbackgetlistunitType");
			showGUIDepartment(); /* important!!!!******************* */


	}

	private ArrayList<String> getArrayAdapterDataSpinnerUnitType() {

			ArrayList<String> arrayAdapter = new ArrayList<String>();
			ArrayList<ModelMasterValue> listUnitType = ControlApp.getInstance()
					.getControlListMasterValues().getListUnitType();
			for (ModelMasterValue eachUnitType : listUnitType) {
				arrayAdapter.add(eachUnitType.getValue());
			}
			return arrayAdapter;
	}

	/*
	 * 
	 * Taks web service
	 * 
	 * @author jas
	 */
	// asyn task
	private class TaskAsynCallService extends
			AsyncTask<ModelService, Integer, Boolean> {

		private ProgressDialog Asycdialog = new ProgressDialog(
				WReceiptLOActivity.this);
		private JSONObject respJSON;
		private String serviceCallback;

		@Override
		protected void onPreExecute() {
			// set message of the dialog
			Asycdialog.setMessage("Loading");
			Asycdialog.setCancelable(false);
			Asycdialog.setCanceledOnTouchOutside(false);
			Asycdialog.show();// show dialog
			super.onPreExecute();
		}

		protected Boolean doInBackground(ModelService... listModelService) {
			boolean result = true;
			String response = "";
			try {
				ModelService objService = listModelService[0]; // object service
				this.serviceCallback = objService.getCallback();
				CallWebService callWebService = new CallWebService();
				response = callWebService.callWebServiceExecute(
						objService.getService(), objService.getParams());
				// Log.d("respJSON: ",response);
				respJSON = new JSONObject(response);
				result = true;

			} catch (Exception ex) {
				Log.e("WReceiptLOActivity",
						"Error in (TaskAsynCallService service rest)", ex);
				Log.i("WReceiptLOActivity", response.toString());
				result = false;
				Toast.makeText(WReceiptLOActivity.this,
						"Error connecting web service", Toast.LENGTH_SHORT)
						.show();
			}
			return result;
		}

		protected void onPostExecute(Boolean result) {
			// response async
			if (result) {
				callbackService();
			} else {
				Toast.makeText(WReceiptLOActivity.this,
						"Error calling operation web service",
						Toast.LENGTH_LONG).show();
			}
			Asycdialog.dismiss();
		}

		private void callbackService() {

			if (serviceCallback.equals("callbackGetListTruckCompanies")) {
				// callbackGetListTruckCompanies(respJSON);
			} else if (serviceCallback.equals("callbackGetListUnitType")) {
				//callbackGetListUnitType(respJSON);
			} else if (serviceCallback.equals("callbackGetListLocations")) {
				//callbackGetListLocations(respJSON);

			} else if (serviceCallback.equals("callbackGetListLocationsInflate")) {
				callbackGetListLocationsInflate(respJSON);

			} else if (serviceCallback.equals("callbackSaveWarehouseReceipt")) {
				callbackSaveWarehouseReceipt(respJSON);
			} else if (serviceCallback.equals("callbackGetInfoItemsPODepRM")) {
				callbackGetInfoItemsPODepRM(respJSON);
			} else if (serviceCallback.equals("callbackGetInfoItemsPODepIP")) {
				callbackGetInfoItemsPODepIP(respJSON);
			} else if (serviceCallback.equals("callbackGetPartialLabels")) {
				callbackGetPartialLabels(respJSON);
			}
		}

	}// end asyn task

	// converter dpt to pixels, used for dialog paint
	public int dipToPixels(int dipValue) {
		float pixels = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
				dipValue, getResources().getDisplayMetrics());
		return (int) pixels;
	}

	// hide keyboard
	public static void hideKeyboard(Activity activity) {
		try {
			InputMethodManager inputManager = (InputMethodManager) activity
					.getSystemService(Context.INPUT_METHOD_SERVICE);
			inputManager.hideSoftInputFromWindow(activity.getCurrentFocus()
					.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
		} catch (Exception e) {
			// Ignore exceptions if any
			Log.e("KeyBoardUtil", e.toString(), e);
		}
	}

	private double roundDecimal(double value, int dec) {
		try {
			String val = value + "";
			BigDecimal big = new BigDecimal(val);
			big = big.setScale(dec, RoundingMode.HALF_UP);
			return Double.parseDouble(big + "");
		} catch (Exception e) {
			Log.e("KeyBoardUtil", "err round decimal " + e.toString(), e);
			return value;
		}

	}

}
