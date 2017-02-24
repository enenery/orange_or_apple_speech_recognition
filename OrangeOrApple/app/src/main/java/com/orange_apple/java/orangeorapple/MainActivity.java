package com.orange_apple.java.orangeorapple;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements TextToSpeech.OnInitListener{

    private final int SPEECH_RECOGNITION_CODE = 1;
    private TextView nav;
    private TextToSpeech textToSpeech;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textToSpeech = new TextToSpeech(MainActivity.this, MainActivity.this);
    }

    public void startQuestion(View view){
        nav = (TextView) findViewById(R.id.navigation);
        String question = "Okay, do you like orange or apple?";
        nav.setText("Okay, do you like orange or apple?");
        //TextToSpeechFunction(question);
        startSpeechToText(question);
    }

    /**
     * Start speech to text intent. This opens up Google Speech Recognition API dialog box to listen the speech input.
     * */
    private void startSpeechToText(String speech) {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        TextToSpeechFunction(speech);
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT,
                speech);
        try {
            startActivityForResult(intent, SPEECH_RECOGNITION_CODE);
        } catch (ActivityNotFoundException a) {
            Toast.makeText(getApplicationContext(),
                    "Sorry! Speech recognition is not supported in this device.",
                    Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Callback for speech recognition activity
     * */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case SPEECH_RECOGNITION_CODE: {
                if (resultCode == RESULT_OK && null != data) {

                    ArrayList<String> result = data
                            .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    String text = result.get(0);

                    if(result.contains("Orange") || result.contains("Apple")){
                        String message = "You like " + text + "? Me too!";
                        TextToSpeechFunction(message);}
                    else {
                        String notReconginized = "Please say either orange or apple. \nTry again.";
                        nav.setText("Please say either orange or apple. \nTry again.");
                        startSpeechToText(notReconginized);
                    }

                }
                break;
            }

        }
    }

    @Override
    public void onDestroy() {

        textToSpeech.shutdown();

        super.onDestroy();
    }

    public void TextToSpeechFunction(String content)
    {

        textToSpeech.speak(content, TextToSpeech.QUEUE_FLUSH, null);

    }

    @Override
    public void onInit(int Text2SpeechCurrentStatus) {

        if (Text2SpeechCurrentStatus == TextToSpeech.SUCCESS) {

            textToSpeech.setLanguage(Locale.US);

            TextToSpeechFunction("Tap to start");
        }

    }
}
