package de.smbsolutions.tagungAdmin.NonNFCActivities;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import de.smbsolutions.tagungAdmin.R;
import de.smbsolutions.tagungAdmin.Database.Database;
import de.smbsolutions.tagungAdmin.Database.Room;
import de.smbsolutions.tagungAdmin.NFCRelevant.WriteTagActivity;

/**
 * Auf dieser Seite können bestehende Räume angeschaut und ein Button für neue
 * Räume erstellt werden. 
 * 
 * @author Mirko
 * 
 */
public class NewRoomActivity extends Activity {

	EditText roomText;
	Button saveRoom;
	ListView roomNameList;
	Database database;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_newroom);

		roomText = (EditText) findViewById(R.id.editRoomName);
		saveRoom = (Button) findViewById(R.id.buttonCreateRoom);
		roomNameList = (ListView) findViewById(R.id.listViewRooms);

		database = Database.getInstance(this);

		//Vorhandene Räume werden ausgelesen
		ArrayList<Room> roomObjectsList = database.getRooms();

		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1);

		// Vorhandene Einträge werden in den Listview gepackt
		for (Room room : roomObjectsList) {
			// list1Strings22[iii]=(String) country.get("name");
			adapter.add((String) room.getName());

		}

		roomNameList.setAdapter(adapter);

		
		//On LongClick lassen sich Räume löschen (BETA)
		// --> Sollte mittels eines Popups realisiert werden
		roomNameList.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
					int index, long arg3) {
			
				//Raum wird gelöscht
				database.deleteRoom(index);

				return false;
			}
		});

		saveRoom.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				String roomName = NewRoomActivity.this.roomText.getText()
						.toString();

				if (roomName.isEmpty()) {
					roomText.setHint("Bitte Namen eingeben");
					roomText.setHintTextColor(Color.RED);

				} else {

					Intent intent = new Intent(getApplicationContext(),
							WriteTagActivity.class);
					intent.putExtra("roomName", roomName);
					startActivity(intent);
					finish();
				}

			}
		});

	}
	
	
	


}
