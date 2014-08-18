package de.smbsolutions.tagungAdmin.NonNFCActivities;

import java.util.ArrayList;

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
import de.smbsolutions.tagungAdmin.R;
import de.smbsolutions.tagungAdmin.Database.Database;
import de.smbsolutions.tagungAdmin.Database.Presentation;
import de.smbsolutions.tagungAdmin.Database.Room;

/**
 * Diese Klasse stellt die Seite mit der Liste aller Präsentationen zu einem
 * Raum sowie einem Button zum Anlegen einer neuen Präsentation dar.
 * 
 * @author Mirko
 * 
 */
public class NewPresentationActivity extends Activity {

	ListView listViewPresentations;
	private Spinner spinnerRooms;
	ArrayList<String> roomNameList;
	Button buttonCreatePresentation;
	Room selectedRoom;
	int listID;

	Database database;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_newpresentation);

		listViewPresentations = (ListView) findViewById(R.id.listViewPresentations);
		spinnerRooms = (Spinner) findViewById(R.id.spinnerRooms);
		buttonCreatePresentation = (Button) findViewById(R.id.buttonCreatePresentation);
		roomNameList = new ArrayList<String>();
		database = Database.getInstance(this);

		// Wenn der User gerade eine neue Präsentation erstellt hat, ist dieser
		// Parameter gesetzt um zu identifizieren für welchen Raum der Eintrag
		// erstellt wurde

		listID = -1;
		try {
			int listIDold = getIntent().getExtras().getInt("listID");

			if (listIDold != 0)
				listID = listIDold;
		} catch (Exception e) {
		}

		// Keine ListID wurde gespeichert
		if (listID == -1) {
			// Listview wird initial auf die erste Route gesetzt
			if (database.getRooms().size() != 0)
				selectedRoom = database.getRooms().get(0);

			// Es wurde ein Raum aus der Liste vermerkt
		} else {
			selectedRoom = database.getRooms().get(listID);
		}

		// //////////////////////
		// Dropdown-List wird aufgebaut
		// //////////////////

		// Die in der Datenbank vorhandenen Räume werde geladen
		ArrayList<Room> roomObjectsList = database.getRooms();

		// Vorhandene Einträge werden in ein String-Array für die Dropdown-Liste
		// gespeichert.
		for (Room room : roomObjectsList) {
			roomNameList.add((String) room.getName());
		}

		// Jetzt wird der entsprechende Adapter angelegt
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(
				NewPresentationActivity.this,
				android.R.layout.simple_spinner_item, roomNameList);

		// Final wird die Dropdown-Liste aller verfügbaren Räume angelegt
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinnerRooms.setAdapter(adapter);
		spinnerRooms.setSelection(listID);
		spinnerRooms.setOnItemSelectedListener(new OnItemSelectedListener() {

			// Wenn der Benutzer einen anderen Eintrag der Dropdown ausgewählt
			// hat
			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int index, long arg3) {

				// Zur Auswahl der Räume-Dropdownlist wird die entsprechende
				// ListView aller Präsentation erstellt
				// Bemerkung: Diese Methode wird auch angesprungen wenn die
				// Dropdown-Liste initial erstellt wird
				selectedRoom = database.getRooms().get(index);
				buildPresentationlist(selectedRoom.getObjectId());

			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// Nichts wird getan
			}

		});

		// /////////////////
		// Button zum Erstellen neuer Presentationen
		// ////////////////
		buttonCreatePresentation.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				Intent intent = new Intent(getApplicationContext(),
						CreatePresentationActivity.class);
				intent.putExtra("roomID", selectedRoom.getObjectId());
				intent.putExtra("listID",
						spinnerRooms.getSelectedItemPosition());
				startActivity(intent);
				finish();

			}
		});

	}


	/**
	 * Diese Methode erstellt den ListView mit allen zum ausgewählten Raum
	 * gehörenden Präsentationen
	 */
	private void buildPresentationlist(String roomID) {
		// In der DB vorhandene Präsenationen werden ausgelesen
		ArrayList<Presentation> presentationObjectsList = database
				.getPresentations(roomID);

		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1);


		// Vorhandene Einträge werden in den Listview gepackt
		for (Presentation presentation : presentationObjectsList) {

			adapter.add((String) presentation.getTopic());
		}

		listViewPresentations.setAdapter(adapter);
	}

}
