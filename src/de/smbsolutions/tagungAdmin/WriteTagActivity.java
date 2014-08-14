package de.smbsolutions.tagungAdmin;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.nfc.NdefMessage;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.os.Bundle;
import android.widget.Toast;
import de.smbsolutions.tagungAdmin.Database.Database;

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

		tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG); // mach' was
		// mit dem
		// Tag! 
		WriterTask tasktest = new WriterTask(roomName, WriteTagActivity.this);
		tasktest.execute(tag);
       

	}

}
