package de.smbsolutions.tagungAdmin.NFCRelevant;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.Ndef;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import de.smbsolutions.tagungAdmin.R;

/**
 * Diese Klasse liefert alle Informationen eines eingescannten Tags zurück
 * 
 * @author Mirko
 * 
 */
public class ReadTagActivity extends Activity {

	NfcAdapter mNfcAdapter;
	ListView listViewTagInfos;
	TextView textViewReadTag;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_readtag);
		listViewTagInfos = (ListView) findViewById(R.id.listViewTagInfos);
		textViewReadTag = (TextView) findViewById(R.id.textViewReadTag);

		textViewReadTag.setText("Bitte Tag an den Sensor halten.");

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
		// Wenn die Activity pausert, muss der Dispatcher deaktiviert werden
		mNfcAdapter.disableForegroundDispatch(this);
	}

	@Override
	protected void onResume() {

		super.onResume();

		// Sobal die Aktivität in den Vordergrund kommt, wird der
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
		try {
			
			//Der Tag wird ausgelesen
			Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);

		    //Zugriff auf den Inhalt des Tags
			Ndef ndef = Ndef.get(tag);
			
			//Jetzt wird die Message herausgezogen
			NdefMessage message = ndef.getCachedNdefMessage();

			// Die einzelnen NdefRecords werden als Listview angezeigt
			ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
					android.R.layout.simple_list_item_1);

			//Die Records werden durchlaufen und dem Adapter der Liste hinzugefügt
			for (NdefRecord record : message.getRecords()) { 
				
				// Ausgabelisten-Adapter wird gefüllt
				adapter.add(new String(record.getPayload()));

			}

			textViewReadTag
					.setText("Die folgenden Informationen sind auf dem Tag:");
			// Listview wird mit dem Adapter belegt
			listViewTagInfos.setAdapter(adapter);

		} catch (Exception e) {
			Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
		}
	}

}
