package com.example.nfcbeamer;

import java.io.IOException;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.nfc.FormatException;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.NfcEvent;
import android.nfc.Tag;
import android.nfc.NfcAdapter.CreateNdefMessageCallback;
import android.nfc.tech.Ndef;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

public class ReadTagActivity extends Activity {

	NfcAdapter mNfcAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_readtag);

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

		// Stürzt irgendwie teilweise immer ab
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
		try {
			Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
			Toast.makeText(this, "Read NFC in onNewIntent", Toast.LENGTH_SHORT)
					.show();

			Ndef ndef1 = Ndef.get(tag);

			NdefMessage message = ndef1.getCachedNdefMessage();
			for (NdefRecord record : message.getRecords()) { // work with
				// record.getPayload()
				Toast.makeText(this, new String(record.getPayload()),
						Toast.LENGTH_SHORT).show();
			}

		} catch (Exception e) {
			Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
		}
	}

}
