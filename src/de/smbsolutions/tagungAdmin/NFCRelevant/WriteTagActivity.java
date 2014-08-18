package de.smbsolutions.tagungAdmin.NFCRelevant;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.nfc.NdefMessage;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.os.Bundle;
import android.widget.Toast;
import de.smbsolutions.tagungAdmin.R;
import de.smbsolutions.tagungAdmin.Database.Database;

/**
 * Diese Activity wird aufgerufen wenn der Benutzer einen neuen Raum anlegen
 * möchte und dazu schon den Namen eingegeben hat. Dann wird der
 * Foreground-Dispatcher gestartet und auf ein NFC Tag gewartet. Sobal ein Tag
 * erkannt wird, wird ein asychnroner Task gestartet, um den Schreibevorgang
 * durchzuführen
 * 
 * @author Mirko
 * 
 */
public class WriteTagActivity extends Activity {

	NfcAdapter mNfcAdapter;
	String roomName;
	NdefMessage msg;
	Tag tag;
	WriterTask writerTask;
	Database database;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_writetag);

		database = Database.getInstance(this);

		roomName = getIntent().getExtras().getString("roomName");

		// Es wird zunächst überprüft ob NFC überhaupt aktiv ist
		checkForNFC();

	}

	private void checkForNFC() {
		
		
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
	protected void onPause() {

		super.onPause();
		// Wenn die Activity pausiert, muss der Dispatcher deaktiviert werden
		mNfcAdapter.disableForegroundDispatch(this);
	}

	@Override
	protected void onResume() {

		super.onResume();

		// Sobald die Aktivität in den Vordergrund kommt, wird der
		// Foreground-Dispatcher aktiviert.
		// Von nun an werden alle NFC Tags automatisch an diese App
		// weitergeleitet
		Intent i = new Intent(this, this.getClass());
		i.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
		PendingIntent intent = PendingIntent.getActivity(this, 0, i, 0);
		mNfcAdapter.enableForegroundDispatch(this, intent, null, null);

	}

	// Dies wird aufgerufen ein NFCIntent vom Foreground-Dispatcher ausgelöst
	// wird.
	// An dieser Stelle kann dann auf das gleiche Tag zugegriffen werden
	@Override
	public void onNewIntent(Intent intent) {

		// Dann wird ein asynchroner Task gestartet. Dies ist wichtig, da der
		// NFC Schreibevorgang sonst den MainThread blockieren könnte
		tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
		WriterTask tasktest = new WriterTask(roomName, WriteTagActivity.this);
		tasktest.execute(tag);

	}

}
