package de.smbsolutions.tagungAdmin;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.LinkedHashMap;

import de.smbsolutions.tagungAdmin.Database.Database;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
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

public class WriterTask extends

		AsyncTask<Tag, Void, String> {
	


	String roomName;
	Context context;
	boolean successfull = false;
	Database database;
	 
		public WriterTask(String roomName, Context context){
			this.roomName = roomName;
			this.context = context;
			
			database = Database.getInstance(context);
			
	 }



	@Override
	protected String doInBackground(Tag... arg0) {
		
		  Tag tag = arg0[0];
	        
	        boolean containsNdef = false;
			boolean containsNdefFormatable = false;
			NdefMessage msg;




			NdefRecord packageRecord = NdefRecord
			// Nur diese App darf den Intent behandeln -> Sorgt dafür, dass direkt
			// die App gestartet wird
					.createApplicationRecord("de.smbsolutions.tagungVisitor");
			NdefRecord uriRecord = NdefRecord
					.createUri("http://smbsolutions.de/index.html");

			NdefRecord roomNameRecord = NdefRecord.createMime("text/plain",
					roomName.getBytes());
			msg = new NdefMessage(new NdefRecord[] { packageRecord, roomNameRecord, uriRecord
					 });

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
					Toast.makeText(context,
							"Unable to write to tag due to FormatException.",
							Toast.LENGTH_SHORT).show();
				} catch (TagLostException e) {
					

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
						
						successfull = true;

					}
					
					

				} catch (FormatException e) {
					

					Toast.makeText(context,
							"Unable to write to tag due to FormatException.",
							Toast.LENGTH_SHORT).show();
				} catch (TagLostException e) {
					
			} catch (IOException e) {
				
					Toast.makeText(context, "Unable to write to tag." + e.toString(),
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
    		
    			return "success";
    		} else {
    			return null;
    		}
	}



	@Override
	protected void onPostExecute(String result) {
		// TODO Auto-generated method stub
		super.onPostExecute(result);
		
		//Das beschreiben des Tags hat geklappt, jetzt kann der Raum auch in die DB geschrieben werden 
		if (result.equals("success")) {
	
			// save Room to db
			database.createRoom(roomName);
			
			//Die nächste Aktivity wird aufgerufen
			Intent nextIntent = new Intent(context,
					NewRoomActivity.class);
			nextIntent.putExtra("roomName", roomName);
			context.startActivity(nextIntent);
			((Activity) context).finish();
		} else {
			Toast.makeText(context, "Da ist leider etwas schief gelaufen. Bitte nochmal probieren", Toast.LENGTH_SHORT).show();
		}
		
		
	}
	
	

}
