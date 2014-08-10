package com.example.nfcbeamer;

import java.io.IOException;

import com.parse.Parse;
import com.parse.ParseObject;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.nfc.FormatException;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.TagLostException;
import android.nfc.tech.Ndef;
import android.nfc.tech.NdefFormatable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

public class WriteTagActivity extends Activity {

	NfcAdapter mNfcAdapter;
	String roomName;
	NdefMessage msg;
	Tag tag;
	WriterTask  writerTask;
	Database database;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_writetag);
		
		database = Database.getInstance(this);

		roomName = getIntent().getExtras().getString("roomName");

		// Check for available NFC Adapter
		mNfcAdapter = NfcAdapter.getDefaultAdapter(this);
		if (mNfcAdapter == null) {
			Toast.makeText(this, "NFC is not available", Toast.LENGTH_LONG)
					.show();
			finish();
			return;
		}

		if (!mNfcAdapter.isEnabled()) {
			Toast.makeText(this, "NFC nicht aktiviert", Toast.LENGTH_SHORT)
					.show();
			startActivity(new Intent(
					android.provider.Settings.ACTION_WIRELESS_SETTINGS));
		}
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		// onResume gets called after this to handle the intent
		mNfcAdapter.disableForegroundDispatch(this);
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();

		// Aufruf in onResume
		Intent i = new Intent(this, this.getClass());
		i.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
		PendingIntent intent = PendingIntent.getActivity(this, 0, i, 0);
		mNfcAdapter.enableForegroundDispatch(this, intent, null, null);

	}

	// Dies wird aufgerufen wenn Aktivität resumed (Ausgelöst, durch das
	// Intent.FLAG_ACTIVITY_SINGLE_TOP
	// An dieser Stelle kann dann auf das gleiche Tag zugegriffen werden
	@Override
	public void onNewIntent(Intent intent) {

		boolean containsNdef = false;
		boolean containsNdefFormatable = false;
		boolean successfull = false;

		tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG); // mach' was
																	// mit dem
																	// Tag! }

		Toast.makeText(this, "Read NFC in onNewIntent", Toast.LENGTH_SHORT)
				.show();

		NdefRecord packageRecord = NdefRecord
		// Nur diese App darf den Intent behandeln -> Sorgt dafür, dass direkt
		// die App gestartet wird
				.createApplicationRecord("com.example.nfcbeamer");
		NdefRecord uriRecord = NdefRecord
				.createUri("http://smbsolutions.de/index.html");

		NdefRecord roomNameRecord = NdefRecord.createMime("text/plain",
				roomName.getBytes());
		msg = new NdefMessage(new NdefRecord[] { uriRecord,
				packageRecord, roomNameRecord });

		// Zuerst muss herausgefunden werden um welchen Typ Tag es sich handelt.
		// Unsere App kann
		// die beiden gängigsten Formate NDEF und NDEFFORMATABLE behandeln)
		String techs[] = tag.getTechList();
		for (String tech : techs) {

			if (tech.equals("android.nfc.tech.Ndef")) {
				containsNdef = true;
			}
			if (tech.equals("android.nfc.tech.NdefFormatable")) {
				containsNdefFormatable = true;
			}
		}

		if (containsNdefFormatable == true) {
			NdefFormatable ndefFormatable = NdefFormatable.get(tag);
			try {
				ndefFormatable.connect();
				ndefFormatable.format(msg);
				
				successfull = true;

			} catch (FormatException e) {
				Log.e("demo", "Unable to write to tag due to FormatException",
						e);
				Toast.makeText(this,
						"Unable to write to tag due to FormatException.",
						Toast.LENGTH_SHORT).show();
			} catch (TagLostException e) {
				Toast.makeText(this, "Tag wurde beim Beschreiben entfernt",
						Toast.LENGTH_SHORT).show();

			} catch (IOException e) {
				Log.e("demo", "Unable to write to tag.", e);
				Toast.makeText(this, "Unable to write to tag.",
						Toast.LENGTH_SHORT).show();

			} finally {
				try {
					ndefFormatable.close();
					
					
				} catch (IOException e) {
				}
			}

		}

		if (containsNdef == true) {
		 
			Ndef ndef = Ndef.get(tag);
			try {
				
				// Nur wenn es kein readOnly-Tag ist und die neue Tag message
				// auch auf das Tag passt,
				// kann der Schreibevorgang beginnen
				if (ndef.isWritable()
						&& ndef.getMaxSize() > msg.toByteArray().length) {
					ndef.connect();
					ndef.writeNdefMessage(msg); // msg ist die NdefMessage
					Toast.makeText(getApplicationContext(),
							"Message has been written to Ndef tag",
							Toast.LENGTH_LONG).show();
				}
				
				successfull = true;

			} catch (FormatException e) {
				Log.e("demo", "Unable to write to tag due to FormatException",
						e);
				Toast.makeText(getApplicationContext(),
						"Unable to write to tag due to FormatException.",
						Toast.LENGTH_SHORT).show();
			} catch (TagLostException e) {
				Toast.makeText(getApplicationContext(), "Tag wurde beim Beschreiben entfernt",
						Toast.LENGTH_SHORT).show();

			} catch (IOException e) {
				Log.e("demo", "Unable to write to tag.", e);
				Toast.makeText(getApplicationContext(), "Unable to write to tag." + e.toString(),
						Toast.LENGTH_LONG).show();

			} finally {
				try {
					ndef.close();
					
				} catch (IOException e) {
				}
			}
		

			
		}
		
		//Das beschreiben des Tags hat geklappt
		if (successfull == true) {
			
			// save Room to db
			database.createRoom(roomName);
			
			Intent nextIntent = new Intent(getApplicationContext(),
					NewRoomActivity.class);
			nextIntent.putExtra("roomName", roomName);
			startActivity(nextIntent);
			WriteTagActivity.this.finish();
			
			Toast.makeText(this, "Raum " + roomName + " erfolgreich gespeichert" , Toast.LENGTH_LONG).show();
		}

	}

}
