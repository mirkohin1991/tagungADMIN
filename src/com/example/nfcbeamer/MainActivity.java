package com.example.nfcbeamer;

import java.io.IOException;
import java.util.List;

import javax.xml.datatype.Duration;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.nfc.FormatException;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.NfcAdapter.CreateNdefMessageCallback;
import android.nfc.NfcEvent;
import android.nfc.Tag;
import android.nfc.tech.Ndef;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.Parse;
import com.parse.ParseAnalytics;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import android.widget.EditText;

public class MainActivity extends Activity {

	NfcAdapter mNfcAdapter;
	Button newRoom;
	Button newPresentation;
	Button readTag;
	Database database;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		newRoom = (Button) findViewById(R.id.buttonNewRoom);
		newPresentation = (Button) findViewById(R.id.buttonNewPresentation);
		readTag = (Button) findViewById(R.id.buttonReadTag);
		
		
		
		Database.getInstance(this);

		newRoom.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getApplicationContext(),
						NewRoomActivity.class);
				String message = "value";
				intent.putExtra("key", message);
				startActivity(intent);
			}
		});

		readTag.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getApplicationContext(),
						ReadTagActivity.class);
				String message = "value";
				intent.putExtra("key", message);
				startActivity(intent);
			}
		});
		
		
		
		newPresentation.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getApplicationContext(),
						NewPresentationActivity.class);
				String message = "value";
				intent.putExtra("key", message);
				startActivity(intent);
			}
		});
		
		
	
		

		// // Check for available NFC Adapter
		// mNfcAdapter = NfcAdapter.getDefaultAdapter(this);
		// if (mNfcAdapter == null) {
		// Toast.makeText(this, "NFC is not available", Toast.LENGTH_LONG)
		// .show();
		// finish();
		// return;
		// }
		//
		// if (!mNfcAdapter.isEnabled()) {
		// Toast.makeText(this, "NFC nicht aktiviert", Toast.LENGTH_SHORT)
		// .show();
		// startActivity(new Intent(
		// android.provider.Settings.ACTION_WIRELESS_SETTINGS));
		// }
		//
		// if
		// (getIntent().getAction().equals(NfcAdapter.ACTION_NDEF_DISCOVERED)) {
		// Tag tag = getIntent().getExtras().getParcelable(
		// NfcAdapter.EXTRA_TAG);
		// Ndef ndef = Ndef.get(tag);
		// Toast.makeText(this, "Read NFC in onCreate",
		// Toast.LENGTH_SHORT).show();
		// NdefMessage message = ndef.getCachedNdefMessage();
		// for (NdefRecord record : message.getRecords()) { // work with
		// // record.getPayload()
		// Toast.makeText(this, new String(record.getPayload()),
		// Toast.LENGTH_SHORT).show();
		// }
		// }
		// BEGIN DB STUFF

		//
		// // //Saving presentations for rooms
		// ParseObject presentations = new ParseObject("Presentations");
		// presentations.put("topic", "Technologien der Zukunft");
		// presentations.put("Referent", "Herr Müller");
		// presentations.put("date", "21.05.2014");
		// presentations.put("time_from", "12:00");
		// presentations.put("time_to", "14:00");
		// // presentations.put("room_id", roomID);
		// presentations.saveInBackground();
		//
		//

		// //Get values of DB
		// ParseQuery<ParseObject> query = ParseQuery.getQuery("Rooms");
		// query.whereEqualTo("name", "Test");
		// query.findInBackground(new FindCallback<ParseObject>() {
		// public void done(List<ParseObject> scoreList, ParseException e) {
		// if (e == null) {
		// // //Saving presentations for rooms
		// ParseObject presentations = new
		// ParseObject("Presentations");
		// presentations.put("topic", "Technologien der Zukunft");
		// presentations.put("Referent", "Herr Müller");
		// presentations.put("date", "21.05.2014");
		// presentations.put("time_from", "12:00");
		// presentations.put("time_to", "14:00");
		// presentations.put("room_id", scoreList.get(0).getObjectId());
		// presentations.saveInBackground();
		// } else {
		// Log.d("score", "Error: " + e.getMessage());
		// }
		// }
		// });
		//
		//

		// //Saving e-mail reserverations for slides
		// ParseObject slideSubscribe = new ParseObject("SlideSubscribe");

	}

	@Override
	public void onResume() {
		super.onResume();
	}

	@Override
	public void onPause() {
		super.onPause();

	}

}
