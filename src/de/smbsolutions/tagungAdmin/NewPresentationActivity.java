package de.smbsolutions.tagungAdmin;

import java.util.ArrayList;

import de.smbsolutions.tagungAdmin.Database.Database;
import de.smbsolutions.tagungAdmin.Database.Presentation;
import de.smbsolutions.tagungAdmin.Database.Room;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;

public class NewPresentationActivity extends Activity {


	ListView listViewPresentations;
	 private Spinner spinnerRooms;
	 ArrayList<String> roomNameList;
	 Button buttonCreatePresentation;
	 Room selectedRoom;
	 int listID = -1;

	 
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
		
		//Wenn der User gerade eine neue Präsentation erstellt hat, ist dieser Parameter gesetzt um zu identifizieren für welchen Raum der Eintrag erstellt wurde
		
		try {
			listID = getIntent().getExtras().getInt("listID");
		} catch (Exception e) {
			// TODO: handle exception
		}
		
		
		//Keine ListID wurde gespeichert	
	   if (listID == -1) {
		 //Listview wird initial auf die erste Route gesetzt
			selectedRoom = database.getRooms().get(0);	
		   
		//Es wurde ein Raum aus der Liste vermerkt	
	   } else {
		   selectedRoom = database.getRooms().get(listID);	
	   }
			
		
		
				
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
	        

	        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	        spinnerRooms.setAdapter(adapter2);
	        spinnerRooms.setSelection(listID);
	        spinnerRooms.setOnItemSelectedListener( new OnItemSelectedListener() {

				@Override
				public void onItemSelected(AdapterView<?> arg0, View arg1,
						int index, long arg3) {
					
					//Zur Auswahl der Dropdownlist wird die entsprechende ListView erstellt
					//Bemerkung: Diese Methode wird auch angesprungen wenn die Dropdown-Liste initial erstellt wird
					selectedRoom = database.getRooms().get(index);
					buildPresentationlist(selectedRoom.getObjectId());
				
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
				intent.putExtra("listID", spinnerRooms.getSelectedItemPosition());
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

	@Override
	protected void onResume(){
		super.onResume();
		
		
	}
	
	private void buildPresentationlist(String roomID) {
		// //Get values of DB
		ArrayList<Presentation> presentationObjectsList = database.getPresentations(roomID);
		
		
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1);
		// Retrieve object "name" from Parse.com database

		//Vorhandene Einträge werden in den Listview gepackt
				for (Presentation presentation : presentationObjectsList) {
		    //list1Strings22[iii]=(String) country.get("name");
		    adapter.add((String) presentation.getTopic());
		}

      listViewPresentations.setAdapter(adapter);
	}
	

}
