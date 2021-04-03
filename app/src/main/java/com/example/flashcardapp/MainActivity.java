package com.example.flashcardapp;

import android.animation.Animator;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.Snackbar;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    FlashcardDatabase flashcardDatabase; //database variable
    List<Flashcard> allFlashcards; //variable that will be holding all flashcard objects
    int currentCardDisplayedIndex = 0; //will be keeping track of index


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        flashcardDatabase = new FlashcardDatabase(getApplicationContext()); //initialize database var
        allFlashcards = flashcardDatabase.getAllCards(); //read from the database when the app is launched


     //if when app is opened and it has a database already set, get values from database else it will output xml q/a
        if( allFlashcards != null && allFlashcards.size() > 0){
            ((TextView) findViewById(R.id.flashcard_question)).setText(allFlashcards.get(0).getQuestion());
            ((TextView) findViewById(R.id.flashcard_answer)).setText(allFlashcards.get(0).getAnswer());
        }
        //now want to set on click listener for when click question
        TextView flashcardAnswer = findViewById(R.id.flashcard_answer);
        TextView flashcardQuestion = findViewById(R.id.flashcard_question);
        flashcardQuestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // get the center for the clipping circle
                flashcardQuestion.setVisibility(View.INVISIBLE);
                flashcardAnswer.setVisibility(View.VISIBLE);
                    int cx = flashcardAnswer.getWidth() / 2;
                    int cy = flashcardAnswer.getHeight() / 2;

                    // get the final radius for the clipping circle
                    float finalRadius = (float) Math.hypot(cx, cy);

                    // create the animator for this view (the start radius is zero)
                    Animator anim = ViewAnimationUtils.createCircularReveal(flashcardAnswer, cx, cy, 0f, finalRadius);

                    // hide the question and show the answer to prepare for playing the animation!
                    flashcardQuestion.setVisibility(View.INVISIBLE);
                    flashcardAnswer.setVisibility(View.VISIBLE);

                    anim.setDuration(600);
                    anim.start();

            }
        });
        flashcardAnswer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                    flashcardAnswer.setVisibility(View.INVISIBLE);
                    flashcardQuestion.setVisibility(View.VISIBLE);

            }
        });
        //going to set onCLick listener to allow addButtom to be pressed
        //this will allow me to set an intent in order to navigate to new activity
        findViewById(R.id.addButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,AddCardActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.right_in, R.anim.left_out);

            }
        });

        findViewById(R.id.nextButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //if there are no cards we just return
                if(allFlashcards.size() == 0){
                    return;
                }
                currentCardDisplayedIndex++;
                //Need to check if there is next

                if(currentCardDisplayedIndex >= allFlashcards.size()) {
                    //setting snackbar message to show on whatever view is currently displayed
                    Snackbar.make(flashcardQuestion,
                            "You've reached the end of the cards, going back to start.",
                            Snackbar.LENGTH_SHORT)
                            .show();
                    currentCardDisplayedIndex = 0; //need to reset the card index
                }
                //else we can show the next card
                allFlashcards = flashcardDatabase.getAllCards();
                Flashcard flashcard = allFlashcards.get(currentCardDisplayedIndex);

                final Animation leftOutAnim = AnimationUtils.loadAnimation(v.getContext(), R.anim.left_out);
                final Animation rightInAnim = AnimationUtils.loadAnimation(v.getContext(), R.anim.right_in);


                leftOutAnim.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {
                        // this method is called when the animation first starts
                        ((TextView) findViewById(R.id.flashcard_question)).setVisibility(View.INVISIBLE);
                        ((TextView) findViewById(R.id.flashcard_answer)).setVisibility(View.INVISIBLE);

                    }
                    @Override
                    public void onAnimationEnd(Animation animation) {
                        // this method is called when the animation is finished playing

                        findViewById(R.id.flashcard_question).startAnimation(rightInAnim);
                        ((TextView) findViewById(R.id.flashcard_question)).setText(flashcard.getQuestion());
                        ((TextView) findViewById(R.id.flashcard_answer)).setText(flashcard.getAnswer());
                        ((TextView) findViewById(R.id.flashcard_question)).setVisibility(View.VISIBLE);
                    }
                    @Override
                    public void onAnimationRepeat(Animation animation) {
                        // we don't need to worry about this method
                    }
                });
                if( flashcardQuestion.getVisibility() == View.VISIBLE){
                    findViewById(R.id.flashcard_question).startAnimation(leftOutAnim);
                }
                else if( flashcardAnswer.getVisibility() == View.VISIBLE) findViewById(R.id.flashcard_answer).startAnimation(leftOutAnim);

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
            flashcardDatabase.insertCard(new Flashcard(string1, string2));
            allFlashcards = flashcardDatabase.getAllCards(); //local variable holding the lsit is updated
        }
    }
}