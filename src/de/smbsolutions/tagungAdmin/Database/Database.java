package de.smbsolutions.tagungAdmin.Database;

import java.util.ArrayList;
import java.util.List;

import android.R.array;
import android.app.Activity;
import android.widget.TextView;

import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.SaveCallback;

import de.smbsolutions.tagungAdmin.R;

/**
 * Diese Klasse repräsentiert die Parse Online Datenbank
 * 
 * @author Mirko
 * 
 */
public class Database {

	public ArrayList<Room> arrayListRooms;
	public ArrayList<Presentation> arrayListPresentation;
	public ArrayList<Subscriber> arrayListSubscriber;
	private static Database db_object = null;
	private Activity context;

	/**
	 * Liefert die Instanz zurück (Singleton)
	 */
	public static Database getInstance(Activity context) {
		if (db_object == null)
			db_object = new Database(context);
		return db_object;
	}

	/**
	 * Privater Konstruktor -> nur ein Singleton kann erzeugt werden
	 */
	private Database(Activity context) {

		this.context = context;

		Parse.initialize(context, "TKlxT9rAYg75PYw19N7zsTqDPkggZuv8HddJdLqR",
				"ekGUAASuWTKDasYzVBWImO9IbFBE38e08WjjkTqx");

		arrayListRooms = new ArrayList<Room>();
		arrayListPresentation = new ArrayList<Presentation>();
		arrayListSubscriber = new ArrayList<Subscriber>();

		// Alle Tabellen werden ausgelesen und in den eben erstellen Arrays
		// gespeichert
		loadRooms();
		loadPresentations();
		loadSubscribers();

	}

	private void loadRooms() {

		// Bisherige DB Einträge werden geladen
		ParseQuery<ParseObject> query = ParseQuery.getQuery("Rooms");

		// Einträge werden gesucht
		query.findInBackground(new FindCallback<ParseObject>() {

			@Override
			public void done(List<ParseObject> objects, ParseException e) {

				for (ParseObject resultEntry : objects) {
					arrayListRooms.add(new Room((String) resultEntry
							.getObjectId(), (String) resultEntry.get("name")));

				}

				// Die MainActivity wird informiert, dass das Laden fertig ist
				notifyActivityOfFinish();

			}
		});

	}

	private void loadPresentations() {

		// Bisherige DB Einträge werden geladen
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

	public void loadSubscribers() {
		// Bisherige DB Einträge werden geladen
		ParseQuery<ParseObject> query = ParseQuery.getQuery("Subscribers");

		// Einträge werden gesucht
		query.findInBackground(new FindCallback<ParseObject>() {

			@Override
			public void done(List<ParseObject> objects, ParseException e) {

				if (e == null) {
					for (ParseObject resultEntry : objects) {

						Subscriber subscriber = new Subscriber(
								(String) resultEntry.getObjectId(),
								(String) resultEntry.get("mail"),
								(String) resultEntry.get("presentation_id"));

						arrayListSubscriber.add(subscriber);

					}
				}

			}
		});

	}

	/**
	 * Liefert zu einer Raum-ID die Liste aller Präsentationen zurück
	 * 
	 * @param roomID
	 * @return
	 */
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

	/**
	 * Liefer zur Präsentations ID alle vorhandenen Subscriber zurück
	 * 
	 * @param presentationId
	 * @return
	 */
	public ArrayList<Subscriber> getSubscribers(String presentationId) {

		ArrayList<Subscriber> subscribersForPresentation = new ArrayList<Subscriber>();

		for (Subscriber subscriber : arrayListSubscriber) {

			if (subscriber.getPresentation_id() != null) {
				if (subscriber.getPresentation_id().equals(presentationId)) {

					subscribersForPresentation.add(subscriber);
				}
			}

		}

		return subscribersForPresentation;

	}

	public ArrayList<Room> getRooms() {

		return arrayListRooms;
	}

	public void createRoom(String name) {

		ParseObject roomObject = new ParseObject("Rooms");
		roomObject.put("name", name);

		// Davor schonmal speichern, damit es im listview aktuell ist!
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

		ParseObject roomObject = new ParseObject("Presentations");
		roomObject.put("Referent", presentation.getReferent());
		roomObject.put("date", presentation.getDate());
		roomObject.put("room_id", presentation.getRoom_id());
		roomObject.put("time_from", presentation.getTime_from());
		roomObject.put("time_to", presentation.getTime_to());
		roomObject.put("topic", presentation.getTopic());

		// Davor schonmal speichern, damit es im listview aktuell ist!
		arrayListPresentation.add(presentation);

		roomObject.saveInBackground(new SaveCallback() {

			@Override
			public void done(ParseException e) {
				if (e == null) {

					// Daten müssen aber zusätzlich neu geladen werden, da die
					// objectID sonst nicht bekannt ist
					arrayListPresentation.clear();
					loadPresentations();
				}

			}
		});
	}

	public void notifyActivityOfFinish() {

		// Alle Buttons werden klickbar gemacht
		context.findViewById(R.id.buttonNewRoom).setEnabled(true);
		context.findViewById(R.id.buttonNewPresentation).setEnabled(true);
		context.findViewById(R.id.buttonReadTag).setEnabled(true);
		context.findViewById(R.id.buttonShowSubscriber).setEnabled(true);

		// Und der Hinweistext entfernt
		TextView text = (TextView) context.findViewById(R.id.textWait);
		text.setText("");

	}

	public void deleteRoom(int id) {

		ParseQuery<ParseObject> query = ParseQuery.getQuery("Rooms");
		List<ParseObject> scoreList = null;

		// Einträge werden gesucht
		try {
			scoreList = query.find();
		} catch (ParseException e) {
			e.printStackTrace();
		}

		try {
			scoreList.get(id).delete();
			arrayListRooms.remove(id);

		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		
		

	}
}
