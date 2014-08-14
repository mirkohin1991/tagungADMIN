package de.smbsolutions.tagungAdmin;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import de.smbsolutions.tagungAdmin.Database.Database;
import de.smbsolutions.tagungAdmin.Database.Presentation;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;

public class CreatePresentationActivity extends Activity {
	EditText txtTitle;
	EditText txtReferent;
	DatePicker txtDate;
	TimePicker txtTimeFrom;
	TimePicker txtTimeTo;
	Button buttonSavePresentation;
	String roomID;
	int listID;

	Database database;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_createpresentation);

		roomID = getIntent().getExtras().getString("roomID");
		listID = getIntent().getExtras().getInt("listID");
		database = Database.getInstance(this);

		txtTitle = (EditText) findViewById(R.id.editPresentationTitle);
		txtReferent = (EditText) findViewById(R.id.editPresentationReferent);
		txtDate = (DatePicker) findViewById(R.id.editPresentationDate);
		txtTimeFrom = (TimePicker) findViewById(R.id.editPresentationTimeFrom);
		txtTimeTo = (TimePicker) findViewById(R.id.editPresentationTimeTo);
		buttonSavePresentation = (Button) findViewById(R.id.buttonSavePresentation);

		buttonSavePresentation.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				boolean flag_error = false;

				if (txtTitle.getText().toString().isEmpty()) {
					txtTitle.setHint("Bitte Thema eingeben");
					txtTitle.setHintTextColor(Color.RED);
					flag_error = true;
				}

				if (txtReferent.getText().toString().isEmpty()) {
					txtReferent.setHint("Bitte Referent eingeben");
					txtReferent.setHintTextColor(Color.RED);
					flag_error = true;
				}

				if (flag_error == false) {
				
			
					
					Calendar cal = Calendar.getInstance();
					cal.set(Calendar.YEAR, txtDate.getYear());
					cal.set(Calendar.MONTH, txtDate.getMonth());
					cal.set(Calendar.DAY_OF_MONTH, txtDate.getDayOfMonth());
					cal.set(Calendar.HOUR_OF_DAY, txtTimeFrom.getCurrentHour());
					cal.set(Calendar.MINUTE, txtTimeFrom.getCurrentMinute());
					cal.set(Calendar.SECOND, 0);
					cal.set(Calendar.MILLISECOND, 0);
					Date date = cal.getTime();
					
					 //Das Datum wir gespeichert
					SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy"); //like "HH:mm" or just "mm", whatever you want
					String dateString = sdf.format(date);
					
					//Und die Uhrzeit extra 
					sdf = new SimpleDateFormat("HH:mm");
					String timeFrom = sdf.format(date);
					
					//Die zweite Uhrzeit (Time_TO) wird formatiert und gespeicher
					cal.set(Calendar.HOUR_OF_DAY, txtTimeTo.getCurrentHour());
					cal.set(Calendar.MINUTE, txtTimeTo.getCurrentMinute());
					date = cal.getTime();		
					String timeTo = sdf.format(date);

					//Und mit allem zusammen das das PräsentationsObjekt erstellt
					Presentation newPresentation = new Presentation(null,
							txtReferent.getText().toString(), String
									.valueOf(dateString), roomID, timeFrom, timeTo,
							txtTitle.getText().toString());

					database.createPresentation(newPresentation);

					Intent intent = new Intent(getApplicationContext(),
							NewPresentationActivity.class);
					intent.putExtra("listID", listID);

					startActivity(intent);
					finish();
				}
			}
		});

		//
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
	}

}
