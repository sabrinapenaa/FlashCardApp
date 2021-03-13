package com.example.flashcardapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TextView flashcardAnswer = findViewById(R.id.flashcard_answer);
        TextView flashcardQuestion= findViewById(R.id.flashcard_question);
        //now want to set on click listener for when click question
        flashcardQuestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //need to set to visible
                flashcardQuestion.setVisibility(View.INVISIBLE);
                flashcardAnswer.setVisibility(View.VISIBLE);
            }
        });

        //going to set onCLick listener to allow addButtom to be pressed
        //this will allow me to set an intent in order to navigate to new activity
        findViewById(R.id.addButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,AddCardActivity.class);
                MainActivity.this.startActivityForResult(intent,100);

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && data != null) { // this 100 needs to match the 100 we used when we called startActivityForResult!
            TextView qString = (TextView) findViewById(R.id.flashcard_question);
            TextView aString = (TextView) findViewById(R.id.flashcard_answer);
            String string1 = data.getExtras().getString("QSTRING");
            String string2 = data.getExtras().getString("ASTRING");

            qString.setText(string1);
            aString.setText(string2);
        }
    }
}