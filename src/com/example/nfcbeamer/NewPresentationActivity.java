package com.example.nfcbeamer;

import java.util.ArrayList;
import java.util.Calendar;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;

public class NewPresentationActivity extends Activity {


	ListView listViewPresentations;
	 private Spinner spinnerRooms;
	 ArrayList<String> roomNameList;
	 Button buttonCreatePresentation;
	 Room selectedRoom;

	 
	 Database database;
	 
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_newpresentation);
		
		listViewPresentations = (ListView) findViewById(R.id.listViewPresentations);
		spinnerRooms = (Spinner)findViewById(R.id.spinnerRooms);
		buttonCreatePresentation = (Button) findViewById(R.id.buttonCreatePresentation);
	
		roomNameList = new ArrayList<String>();
		
		
		database = Database.getInstance(this);
		
			
		//Listview wird initial auf die erste Route gesetzt
		selectedRoom = database.getRooms().get(0);
		
				// //Get values of DB
				ArrayList<Presentation> presentationObjectsList = database.getPresentations(selectedRoom.getObjectId());
				
				
				ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
						android.R.layout.simple_list_item_1);
		        // Retrieve object "name" from Parse.com database

				//Vorhandene Einträge werden in den Listview gepackt
						for (Presentation presentation : presentationObjectsList) {
		            //list1Strings22[iii]=(String) country.get("name");
		            adapter.add((String) presentation.getTopic());


		        }

		       listViewPresentations.setAdapter(adapter);
				
		
				
		////////////////////////
		//Dropdown-List wird aufgebaut
		////////////////////        
		        
		    	// //Get values of DB
				ArrayList<Room> roomObjectsList = database.getRooms();
				
				
				//Vorhandene Einträge werden in den Listview gepackt
						for (Room room : roomObjectsList) {
		            roomNameList.add((String) room.getName());
		        }
		        
	        ArrayAdapter<String>adapter2 = new ArrayAdapter<String>(NewPresentationActivity.this,
	                android.R.layout.simple_spinner_item, roomNameList);
	        

	        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	        spinnerRooms.setAdapter(adapter2);
	        spinnerRooms.setOnItemSelectedListener( new OnItemSelectedListener() {

				@Override
				public void onItemSelected(AdapterView<?> arg0, View arg1,
						int index, long arg3) {
				
					
				}

				@Override
				public void onNothingSelected(AdapterView<?> arg0) {
					// TODO Auto-generated method stub
					
				}

				
			});
	        
	        
	        
	        ///////////////////
	        // Button zum erstellen neuer Presentationen
	        //////////////////
	        buttonCreatePresentation.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {


				Intent intent = new Intent(getApplicationContext(),
						CreatePresentationActivity.class);
				intent.putExtra("roomID", selectedRoom.getObjectId());
				startActivity(intent);
				finish();

			}
		});

	

	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
	}

	

}
