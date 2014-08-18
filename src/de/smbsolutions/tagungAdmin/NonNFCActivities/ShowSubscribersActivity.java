package de.smbsolutions.tagungAdmin.NonNFCActivities;

import java.util.ArrayList;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
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
import de.smbsolutions.tagungAdmin.Database.Subscriber;

/**
 * In dieser Klassen werden zu einer Präsentation in einem Raum alle
 * eingetragenen Mail-Adressen angezeigt
 * 
 * @author Mirko
 * 
 */
public class ShowSubscribersActivity extends Activity {

	ListView listViewSubscribers;
	private Spinner spinnerRooms;
	private Spinner spinnerPresentations;
	ArrayList<String> roomNameList;

	Button buttonCreatePresentation;
	Room selectedRoom;
	Presentation selectedPresentation;

	Database database;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_showsubscribers);

		listViewSubscribers = (ListView) findViewById(R.id.listViewSubscribers);
		spinnerRooms = (Spinner) findViewById(R.id.spinnerRoomsShow);
		spinnerPresentations = (Spinner) findViewById(R.id.spinnerPresentationShow);

		roomNameList = new ArrayList<String>();

		database = Database.getInstance(this);

		// //////////////////////
		// Dropdown-List wird aufgebaut
		// //////////////////

		// RÄume werden aus der DB gelesen
		ArrayList<Room> roomObjectsList = database.getRooms();

		// Vorhandene Einträge werden in ein String-Array für die Dropdown
		// geladen
		for (Room room : roomObjectsList) {
			roomNameList.add((String) room.getName());
		}

		ArrayAdapter<String> adapter = new ArrayAdapter<String>(
				ShowSubscribersActivity.this,
				android.R.layout.simple_spinner_item, roomNameList);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinnerRooms.setAdapter(adapter);
		spinnerRooms.setSelection(0);

		// Event wenn der Benutzer einen anderen Raum auswählt
		spinnerRooms.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int index, long arg3) {

				// Die Liste der Subscriber-Mailadressen wird jedes mal refresht
				ArrayAdapter<String> adapter = (ArrayAdapter) listViewSubscribers
						.getAdapter();
				if (adapter != null) {
					adapter.clear();
				}

				// Zur Auswahl der Dropdownlist wird eine weitere Dropdown
				// mit den Präsentationen erstellt
				selectedRoom = database.getRooms().get(index);
				buildPresentationlist(selectedRoom.getObjectId());

			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {

			}

		});

	}

	/**
	 * Die zum Raum gehörenden Präsentationen werden geladen
	 * 
	 * @param roomID
	 */
	private void buildPresentationlist(String roomID) {

		ArrayList<String> presentationNameList;
		presentationNameList = new ArrayList<String>();
		boolean flag_no_entry = false;

		// Die Präsentationen werden geladen
		ArrayList<Presentation> presenationObjectsList = database
				.getPresentations(roomID);

		// Vorhandene Einträge werden in ein String-Array für den ListView
		// geladen
		for (Presentation presenation : presenationObjectsList) {
			presentationNameList.add((String) presenation.getTopic());
		}

		// Logik wird unterbrochen, wenn es keinen Vortrag zum ausgewählten Raum
		// gibt
		if (presentationNameList.size() == 0) {
			presentationNameList.add("Noch kein Vortrag vorhanden");
			flag_no_entry = true;
		}

		ArrayAdapter<String> adapter = new ArrayAdapter<String>(
				ShowSubscribersActivity.this,
				android.R.layout.simple_spinner_item, presentationNameList);

		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinnerPresentations.setAdapter(adapter);

		// Kein Action-Listener, wenn es keinen Eintrag gibt
		if (flag_no_entry == false) {

			spinnerPresentations.setSelection(0);
			spinnerPresentations
					.setOnItemSelectedListener(new OnItemSelectedListener() {
						@Override
						public void onItemSelected(AdapterView<?> arg0,
								View arg1, int index, long arg3) {

							// Zur Auswahl der Dropdownlist wird die
							// entsprechende ListView
							// erstellt
							ArrayList<Presentation> presentations = database
									.getPresentations(selectedRoom
											.getObjectId());

							// Wenn es eine Präsentation gibt, wird zur Auswahl
							// die E-Mail Liste erstellt
							if (presentations.size() != 0) {
								selectedPresentation = presentations.get(index);
								buildSubscriberList(selectedPresentation
										.getObjectId());
							}

						}

						@Override
						public void onNothingSelected(AdapterView<?> arg0) {

						}

					});

		}

	}

	/**
	 * Die zur Präsentation gehörenden Subscriber werden geladen
	 * @param presentationID
	 */
	private void buildSubscriberList(String presentationID) {

		// Die E-Mail-Liste wird ausgelesen
		ArrayList<Subscriber> subscriberObjectsList = database
				.getSubscribers(presentationID);

		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1);

		if (subscriberObjectsList.size() == 0) {
			adapter.add("Noch keine E-Mail Adresse vorhanden");
		} else {

			// Vorhandene Einträge werden in den Listview gepackt
			for (Subscriber subscriber : subscriberObjectsList) {

				adapter.add((String) subscriber.getMail());
			}

		}

		listViewSubscribers.setAdapter(adapter);

	}

}
