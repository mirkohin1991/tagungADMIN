package de.smbsolutions.tagungAdmin.Database;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.YuvImage;

import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.SaveCallback;


public class Database {

	public ArrayList<Room> arrayListRooms;
	public ArrayList<Presentation> arrayListPresentation;
	private static Database db_object = null;

	/**
	 * Liefert die Instanz zurück (Singleton)
	 */
	public static Database getInstance(Context context) {
		if (db_object == null)
			db_object = new Database(context);
		return db_object;
	}

	/**
	 * Privater Konstruktor -> nur ein Singleton kann erzeugt werden
	 */
	private Database(Context context) {

		Parse.initialize(context, "TKlxT9rAYg75PYw19N7zsTqDPkggZuv8HddJdLqR",
				"ekGUAASuWTKDasYzVBWImO9IbFBE38e08WjjkTqx");

		arrayListRooms = new ArrayList<Room>();
		arrayListPresentation = new ArrayList<Presentation>();

		loadRooms();
		loadPresentations();

	}

	private void loadRooms() {

		// Bisherige DB Einträge werden geladen

		// //Get values of DB
		ParseQuery<ParseObject> query = ParseQuery.getQuery("Rooms");

		// Einträge werden gesucht

		query.findInBackground(new FindCallback<ParseObject>() {

			@Override
			public void done(List<ParseObject> objects, ParseException e) {

				for (ParseObject resultEntry : objects) {
					arrayListRooms.add(new Room((String) resultEntry
							.getObjectId(), (String) resultEntry.get("name")));

				}

			}
		});

	}

	private void loadPresentations() {

		// Bisherige DB Einträge werden geladen

		// //Get values of DB
		ParseQuery<ParseObject> query = ParseQuery.getQuery("Presentations");

		// Einträge werden gesucht

		query.findInBackground(new FindCallback<ParseObject>() {

			@Override
			public void done(List<ParseObject> objects, ParseException e) {

				if (e == null) {
					for (ParseObject resultEntry : objects) {

						Presentation presentation = new Presentation(
								(String) resultEntry.getObjectId(),
								(String) resultEntry.get("Referent"),
								(String) resultEntry.get("date"),
								(String) resultEntry.get("room_id"),
								(String) resultEntry.get("time_from"),
								(String) resultEntry.get("time_to"),
								(String) resultEntry.get("topic"));

						arrayListPresentation.add(presentation);

					}
				}

			}
		});

	}

	public ArrayList<Presentation> getPresentations(String roomID) {

		ArrayList<Presentation> presentationsForRoom = new ArrayList<Presentation>();

		for (Presentation presentation : arrayListPresentation) {

			if (presentation.getRoom_id() != null) {
				if (presentation.getRoom_id().equals(roomID)) {

					presentationsForRoom.add(presentation);
				}
			}

		}

		return presentationsForRoom;

	}

	public ArrayList<Room> getRooms() {

		return arrayListRooms;
	}

	public void createRoom(String name) {
		// Saving a room
		ParseObject roomObject = new ParseObject("Rooms");
		roomObject.put("name", name);
		
		//Davor schonmal speichern, damit es im listview aktuell ist!
		arrayListRooms.add(new Room(null, name));
		
		
		roomObject.saveInBackground(new SaveCallback() {
			@Override
			public void done(ParseException e) {
				if (e == null) {

					// Daten werden refresht
					arrayListRooms.clear();
					loadRooms();
				}

			}
		});
	}
	
	

	public void createPresentation(final Presentation presentation) {
		// Saving a room
		ParseObject roomObject = new ParseObject("Presentations");
		roomObject.put("Referent", presentation.getReferent());
		roomObject.put("date", presentation.getDate());
		roomObject.put("room_id", presentation.getRoom_id());
		roomObject.put("time_from", presentation.getTime_from());
		roomObject.put("time_to", presentation.getTime_to());
		roomObject.put("topic", presentation.getTopic());
		
		
		//Davor schonmal speichern, damit es im listview aktuell ist!
		arrayListPresentation.add(presentation);

		roomObject.saveInBackground(new SaveCallback() {

			@Override
			public void done(ParseException e) {
				if (e == null) {
					
			

					// Daten müssen aber zusätzlich neu geladen werden, da die objectID nicht bekannt ist
					arrayListPresentation.clear();
					loadPresentations();
				}

			}
		});
	}

}
