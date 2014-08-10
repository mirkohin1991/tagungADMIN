package com.example.nfcbeamer;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
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

public class NewRoomActivity extends Activity {

	EditText roomText;
	Button saveRoom;
	ListView roomNameList;
	 Database database;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_newroom);

		roomText = (EditText) findViewById(R.id.editRoomName);
		saveRoom = (Button) findViewById(R.id.buttonCreateRoom);
		roomNameList = (ListView) findViewById(R.id.listViewRooms);
		
		database = Database.getInstance(this);

		// //Get values of DB
		ArrayList<Room> roomObjectsList = database.getRooms();
		
		
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1);
        // Retrieve object "name" from Parse.com database

		//Vorhandene Einträge werden in den Listview gepackt
				for (Room room : roomObjectsList) {
            //list1Strings22[iii]=(String) country.get("name");
            adapter.add((String) room.getName());


        }

        roomNameList.setAdapter(adapter);
        
        
        roomNameList.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
					int index, long arg3) {
			
				// //Get values of DB
				ParseQuery<ParseObject> query = ParseQuery.getQuery("Rooms");
				List<ParseObject> scoreList = null;
				
				//Einträge werden gesucht
				try {
					scoreList = query.find();
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				try {
					scoreList.get(index).delete();
				  
					
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			
				
				return false;
			}
		});
        
		
	

		
		saveRoom.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				String roomName = NewRoomActivity.this.roomText.getText()
						.toString();

				Intent intent = new Intent(getApplicationContext(),
						WriteTagActivity.class);
				intent.putExtra("roomName", roomName);
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
