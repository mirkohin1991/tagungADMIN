package com.example.nfcbeamer;

import java.util.Calendar;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;

public class CreatePresentationActivity  extends Activity {
	 EditText txtTitle;
	 EditText txtReferent;
	 DatePicker txtDate;
	 TimePicker txtTimeFrom;
	 TimePicker txtTimeTo;
	 Button buttonSavePresentation;
	 String roomID;


	 
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_createpresentation);
		
		roomID = getIntent().getExtras().getString("roomID");
		
	
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
				
				

				Intent intent = new Intent(getApplicationContext(),
						NewPresentationActivity.class);
		
				startActivity(intent);
				finish();
			}
		});
		

		
			  
//			  txtDate.setOnClickListener(new OnClickListener() {
//
//			        @Override
//			        public void onClick(View v) {
//			            // TODO Auto-generated method stub
//			            //To show current date in the datepicker
//			            Calendar mcurrentDate=Calendar.getInstance();
//			           int mYear= mcurrentDate.get(Calendar.YEAR);
//			            int mMonth=mcurrentDate.get(Calendar.MONTH);
//			            int mDay=mcurrentDate.get(Calendar.DAY_OF_MONTH);
//
//			            DatePickerDialog mDatePicker=new DatePickerDialog(CreatePresentationActivity.this, new OnDateSetListener() {                  
//			                public void onDateSet(DatePicker datepicker, int selectedyear, int selectedmonth, int selectedday) {
//			                    // TODO Auto-generated method stub                      
//			                    /*      Your code   to get date and time    */
//			                }
//			            },mYear, mMonth, mDay);
//			            mDatePicker.setTitle("Select date");         
//			           mDatePicker.di
//			            mDatePicker.show();  }
//			    });
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
