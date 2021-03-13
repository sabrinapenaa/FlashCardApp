package com.example.flashcardapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class AddCardActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_card);
        //now i want to dismiss the AddActivityCard activity so will be using finish
        findViewById(R.id.cancelButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent inte = new Intent(AddCardActivity.this, MainActivity.class);
                AddCardActivity.this.startActivity(inte); //going to the starting screen of MainActivity
                finish(); //closing AddCardActivity and opening MainActivity
                //need to send previous data

            }
        });

        findViewById(R.id.saveButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //setting the string I will be passing to the intent
                String qString = ((EditText) findViewById(R.id.inputQuestion)).getText().toString();
                String aString = ((EditText) findViewById(R.id.inputAnswer)).getText().toString();

                //passing data to intent
                Intent data = new Intent(AddCardActivity.this,MainActivity.class);
                data.putExtra("QSTRING", qString);
                data.putExtra("ASTRING", aString);
                setResult(RESULT_OK, data); //sets results that activity will return to caller aka checks if operation succeeded
                finish(); //close this screen and go to the next
            }
        });

    }
}