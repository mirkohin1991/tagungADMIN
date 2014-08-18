package de.smbsolutions.tagungAdmin.NFCRelevant;

import java.io.IOException;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.nfc.FormatException;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.Tag;
import android.nfc.TagLostException;
import android.nfc.tech.Ndef;
import android.nfc.tech.NdefFormatable;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;
import de.smbsolutions.tagungAdmin.Database.Database;
import de.smbsolutions.tagungAdmin.NonNFCActivities.NewRoomActivity;

/**
 * In diesem Task findet der eigentliche Schreibvorgang statt
 * 
 * @author Mirko
 * 
 */
public class WriterTask extends

AsyncTask<Tag, Void, String> {

	String roomName;
	Context context;
	boolean successfull = false;
	Database database;

	public WriterTask(String roomName, Context context) {
		this.roomName = roomName;
		this.context = context;

		database = Database.getInstance(null);

	}

	// Wird angestoßen wenn Task gestartet
	@Override
	protected String doInBackground(Tag... arg0) {

		Tag tag = arg0[0];

		boolean containsNdef = false;
		boolean containsNdefFormatable = false;
		NdefMessage msg;

		// ///////////
		// Es werden drei Records erstellt
		// //////////

		// Nur die Besucher App darf den Tag lesen
		// --> Android Appication Record mit dem Paket-Namen
		NdefRecord packageRecord = NdefRecord
				.createApplicationRecord("de.smbsolutions.tagungVisitor");

		// Zusätzlich wird ein URI erstellt
		NdefRecord uriRecord = NdefRecord.createUri("http://smbsolutions.de");

		// Abschließend der hautpsächlich interessante Name des eben angelegten
		// Raums
		NdefRecord roomNameRecord = NdefRecord.createMime("text/plain",
				roomName.getBytes());

		// Die Message wird mit den drei Records erstellt
		msg = new NdefMessage(new NdefRecord[] { uriRecord, packageRecord,
				roomNameRecord });

		// Zuerst muss herausgefunden werden um welchen Typ Tag es sich handelt.
		// Unsere App kann die beiden gängigsten Formate NDEF und NDEFFORMATABLE
		// behandeln) Letzters wird durch erstmaliges Beschreiben in ein NDEF
		// Tag umgewandelt

		// Überprüfen um welche Art es sich handelt:
		String techs[] = tag.getTechList();
		for (String tech : techs) {

			if (tech.equals("android.nfc.tech.Ndef")) {
				containsNdef = true;
			}
			if (tech.equals("android.nfc.tech.NdefFormatable")) {
				containsNdefFormatable = true;
			}
		}

		// Es handelt sich um ein NDefFormatable
		if (containsNdefFormatable == true) {
			NdefFormatable ndefFormatable = NdefFormatable.get(tag);
			try {

				// Der Tag wird formatiert und die Message darauf geschrieben
				ndefFormatable.connect();
				ndefFormatable.format(msg);
				successfull = true;

			} catch (FormatException e) {

				Toast.makeText(context,
						"Unable to write to tag due to FormatException.",
						Toast.LENGTH_SHORT).show();

			} catch (IOException e) {
				Log.e("demo", "Unable to write to tag.", e);
				Toast.makeText(context, "Unable to write to tag.",
						Toast.LENGTH_SHORT).show();

			} finally {
				try {
					ndefFormatable.close();

				} catch (IOException e) {
				}
			}

		}

		// Es handelt sich um einen regulären Ndef-Tag
		if (containsNdef == true) {

			Ndef ndef = Ndef.get(tag);
			try {

				// Nur wenn es kein readOnly-Tag ist und die neue Tag message
				// auch auf den Tag passt, kann der Schreibevorgang beginnen
				if (ndef.isWritable()
						&& ndef.getMaxSize() > msg.toByteArray().length) {
					ndef.connect();
					//Die Message wird auf den Tag geschrieben
					ndef.writeNdefMessage(msg);

					successfull = true;

				}

			} catch (FormatException e) {

				Toast.makeText(context,
						"Unable to write to tag due to FormatException.",
						Toast.LENGTH_SHORT).show();
			} catch (TagLostException e) {

			} catch (IOException e) {

				Toast.makeText(context,
						"Unable to write to tag." + e.toString(),
						Toast.LENGTH_LONG).show();

			} finally {
				try {
					ndef.close();

				} catch (IOException e) {
				}
			}

		}

		// Das beschreiben des Tags hat geklappt
		if (successfull == true) {

			return "success";
		} else {
			return null;
		}
	}

	
	/**
	 * Diese Methode wird angesprungen wenn die vorherige Methode erfolgreich war
	 */
	@Override
	protected void onPostExecute(String result) {
		// TODO Auto-generated method stub
		super.onPostExecute(result);

		// Das beschreiben des Tags hat geklappt, jetzt kann der Raum auch in
		// die DB geschrieben werden
		if (result.equals("success")) {

		
			database.createRoom(roomName);

			// Die nächste Aktivity wird aufgerufen
			Intent nextIntent = new Intent(context, NewRoomActivity.class);
			nextIntent.putExtra("roomName", roomName);
			context.startActivity(nextIntent);
			((Activity) context).finish();
			
		} else {
			Toast.makeText(
					context,
					"Da ist leider etwas schief gelaufen. Bitte nochmal probieren",
					Toast.LENGTH_SHORT).show();
		}

	}

	@Override
	protected void onProgressUpdate(Void... values) {

		super.onProgressUpdate(values);

	}

}
