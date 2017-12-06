package com.example.com.myapplication;

import android.content.Intent;
import android.graphics.Color;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.speech.RecognitionListener;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.util.ArrayList;

public class Sample extends AppCompatActivity implements RecognitionListener {

    //UI Component declaration
    private TextView returnedText;
    private ToggleButton toggleButton;
    private ProgressBar progressBar;
    private SpeechRecognizer speechRecognizer=null;
    private Intent recognizerIntent;
    private Button getReport;
    private String LOF_TAG="VoiceRecognizingIntent";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sample);

        returnedText=(TextView)findViewById(R.id.textView1);
        progressBar=(ProgressBar)findViewById(R.id.progressBar1);
        toggleButton=(ToggleButton)findViewById(R.id.toggleButton1);
        getReport=(Button)findViewById(R.id.getReport);

        progressBar.setVisibility(View.VISIBLE);
        speechRecognizer=SpeechRecognizer.createSpeechRecognizer(this);
        speechRecognizer.setRecognitionListener(this);

        recognizerIntent=new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_PREFERENCE,"en");
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE,this.getPackageName());
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,RecognizerIntent.LANGUAGE_MODEL_WEB_SEARCH);
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS,3);

        toggleButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b)
                {
                    progressBar.setVisibility(View.VISIBLE);
                    progressBar.setIndeterminate(true);
                    speechRecognizer.startListening(recognizerIntent);
                }
                else
                {
                    progressBar.setIndeterminate(false);
                    progressBar.setVisibility(View.INVISIBLE);
                    speechRecognizer.stopListening();
                }
            }
        });

        if(returnedText.getText().toString().trim().length()>3)
        {
            getReport.setTextColor(Color.BLACK);
            getReport.setClickable(true);
            Toast.makeText(getApplicationContext(),"clicking enabled",Toast.LENGTH_SHORT).show();
        }
        else
        {
            getReport.setTextColor(Color.GRAY);
            getReport.setClickable(false);
            Toast.makeText(getApplicationContext(),"clicking disabled",Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(speechRecognizer!=null)
        {
            speechRecognizer.destroy();
            Toast.makeText(getApplicationContext(),"destroyed",Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onReadyForSpeech(Bundle bundle) {
    }

    @Override
    public void onBeginningOfSpeech() {
        progressBar.setIndeterminate(false);
        progressBar.setMax(10);
    }

    @Override
    public void onRmsChanged(float v) {
        progressBar.setProgress((int)v);
    }

    @Override
    public void onBufferReceived(byte[] bytes) {

    }

    @Override
    public void onEndOfSpeech() {
        progressBar.setIndeterminate(true);
        toggleButton.setChecked(false);
    }

    @Override
    public void onError(int i) {
        String errorMessage= getErrorText(i);
        returnedText.setText(errorMessage);
        toggleButton.setChecked(false);
    }

    private String getErrorText(int i) {
        String msg="";
        switch (i){
            case SpeechRecognizer.ERROR_AUDIO:msg="Audio recording error";break;
            case SpeechRecognizer.ERROR_CLIENT:msg="Client side error";break;
            case SpeechRecognizer.ERROR_INSUFFICIENT_PERMISSIONS:msg="Insufficient permission";break;
            case SpeechRecognizer.ERROR_NETWORK:msg="network error";break;
            case SpeechRecognizer.ERROR_NETWORK_TIMEOUT:msg="network timeout";break;
            case SpeechRecognizer.ERROR_NO_MATCH:msg="no match";break;
            case SpeechRecognizer.ERROR_RECOGNIZER_BUSY:msg="recognizer busy";break;
            case SpeechRecognizer.ERROR_SERVER:msg="server error";break;
            case SpeechRecognizer.ERROR_SPEECH_TIMEOUT:msg="speech timeout";break;
        }
        return msg;
    }

    @Override
    public void onResults(Bundle bundle) {
        Toast.makeText(getApplicationContext(),"completed",Toast.LENGTH_SHORT).show();
        ArrayList<String> matches=bundle.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
        String string="";
        for(String r:matches)
            string=string+r+"\n";

        returnedText.setText(matches.get(0));
    }

    @Override
    public void onPartialResults(Bundle bundle) {

    }

    @Override
    public void onEvent(int i, Bundle bundle) {

    }
}