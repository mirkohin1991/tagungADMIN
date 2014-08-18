package de.smbsolutions.tagungAdmin.NonNFCActivities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.nfc.NfcAdapter;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import de.smbsolutions.tagungAdmin.R;
import de.smbsolutions.tagungAdmin.Database.Database;
import de.smbsolutions.tagungAdmin.NFCRelevant.ReadTagActivity;

public class MainActivity extends Activity {

	NfcAdapter mNfcAdapter;
	Button newRoom;
	Button newPresentation;
	Button readTag;
	Button buttonShowSubscriber;
	Database database;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		// Die Buttons werden geladen und vorerst deaktiviert
		// Sobald die Online-Datenbank geladen wurde, werden sie wieder aktiviert
		newRoom = (Button) findViewById(R.id.buttonNewRoom);
		newRoom.setEnabled(false);
		newPresentation = (Button) findViewById(R.id.buttonNewPresentation);
		newPresentation.setEnabled(false);
		readTag = (Button) findViewById(R.id.buttonReadTag);
		readTag.setEnabled(false);
		buttonShowSubscriber = (Button) findViewById(R.id.buttonShowSubscriber);
		buttonShowSubscriber.setEnabled(false);

		// Die Datenbank wird initialisiert
		Database.getInstance(this);
		
		
		//Nur wenn NFC aktiviert ist und eine Internetverbindung besteht, funktioniert die App
		checkForNFCandWIFI();

		newRoom.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getApplicationContext(),
						NewRoomActivity.class);
				startActivity(intent);
			}
		});

		readTag.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getApplicationContext(),
						ReadTagActivity.class);
				startActivity(intent);
			}
		});

		newPresentation.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getApplicationContext(),
						NewPresentationActivity.class);
				startActivity(intent);
			}
		});

		buttonShowSubscriber.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getApplicationContext(),
						ShowSubscribersActivity.class);
				startActivity(intent);
			}
		});

		
		
	
		

	}

	
	@Override
	public void onResume() {
		super.onResume();
	}

	@Override
	public void onPause() {
		super.onPause();

	}
	
	private void checkForNFCandWIFI() {
		
				mNfcAdapter = NfcAdapter.getDefaultAdapter(this);
				if (mNfcAdapter == null) {
					Toast.makeText(this,
							"NFC ist auf Ihrem Gerät leider nicht verfügbar",
							Toast.LENGTH_LONG).show();
					finish();
					return;
				}

				if (!mNfcAdapter.isEnabled()) {
					Toast.makeText(this, "Bitte aktivieren Sie NFC", Toast.LENGTH_LONG)
							.show();
					startActivity(new Intent(
							android.provider.Settings.ACTION_WIRELESS_SETTINGS));
				}

				if (haveNetworkConnection() == false) {
					Toast.makeText(this,
							"Für diese App ist eine Internetverbindung nötig",
							Toast.LENGTH_LONG).show();
					startActivity(new Intent(
							android.provider.Settings.ACTION_WIFI_SETTINGS));
				}
		
	}


	/**
	 * Methode, die überprüft ob Wlan oder Mobiles Netz aktiviert ist
	 */
	private boolean haveNetworkConnection() {
		boolean haveConnectedWifi = false;
		boolean haveConnectedMobile = false;

		ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo[] netInfo = cm.getAllNetworkInfo();
		for (NetworkInfo ni : netInfo) {
			if (ni.getTypeName().equalsIgnoreCase("WIFI"))
				if (ni.isConnected())
					haveConnectedWifi = true;
			if (ni.getTypeName().equalsIgnoreCase("MOBILE"))
				if (ni.isConnected())
					haveConnectedMobile = true;
		}
		return haveConnectedWifi || haveConnectedMobile;
	}

}
