package com.example.nfcbeamer;

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

				if (e != null) {
				for (ParseObject resultEntry : objects) {
					arrayListPresentation.add(new Presentation(
							(String) resultEntry.getObjectId(),
							(String) resultEntry.get("Referent"),
							(String) resultEntry.get("date"),
							(String) resultEntry.get("room_id"),
							(String) resultEntry.get("time_from"),
							(String) resultEntry.get("time_to"),
							(String) resultEntry.get("topic")));

				}
				}

			}
		});

	}

	public ArrayList<Presentation> getPresentations(String roomID) {

		ArrayList<Presentation> presentationsForRoom = new ArrayList<Presentation>();

		for (Presentation presentation : arrayListPresentation) {

			if (presentation.getObjectId().equals(roomID)) {

				presentationsForRoom.add(presentation);
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
		roomObject.saveInBackground(new SaveCallback() {
			
			@Override
			public void done(ParseException e) {
				if (e == null) {
					
					//Daten werden refresht
					arrayListRooms.clear();
					loadRooms();
				}
				
			}
		});
	}

}
