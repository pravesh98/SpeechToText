package com.example.com.myapplication;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.speech.RecognizerIntent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    //UI component declaration
    Button recordVoice,audioFile;
    TextView convertedText;

    //Other
    private static final int REQ_CODE_SPEECH_INPUT=100;
    private static final int REQ_CODE_SPEECH_FILE=200;

    //MediaPlayer declaration
    MediaPlayer mediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //UI component initialization
        recordVoice=(Button)findViewById(R.id.takeVoiceInput);
        audioFile=(Button)findViewById(R.id.audioFile);
        convertedText=(TextView)findViewById(R.id.convertedText);

        mediaPlayer=new MediaPlayer();
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);

        recordVoice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startVoiceInput();
            }
        });

        audioFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //selectAudiFile();
                Toast.makeText(getApplicationContext(),"Activity started",Toast.LENGTH_SHORT).show();
                startActivity(new Intent(MainActivity.this,Sample.class));
            }
        });
    }

    private void selectAudiFile() {
        Toast.makeText(getApplicationContext(),"Audio file selection",Toast.LENGTH_SHORT).show();
        Intent intent=new Intent();
        intent.setAction(Intent.ACTION_GET_CONTENT);
        intent.setType("audio/mpeg");
        try {
            startActivityForResult(Intent.createChooser(intent, "select audio"), REQ_CODE_SPEECH_FILE);
        }
        catch (ActivityNotFoundException a)
        {
            Toast.makeText(getApplicationContext(),"cannot select file",Toast.LENGTH_SHORT).show();
        }
    }

    private void startVoiceInput() {
        Toast.makeText(getApplicationContext(),"voice recording started",Toast.LENGTH_SHORT).show();
        Intent intent=new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE,Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT,"Hello how are you  ?");
        try
        {
            startActivityForResult(intent,REQ_CODE_SPEECH_INPUT);
        }
        catch (ActivityNotFoundException e){
            Toast.makeText(getApplicationContext(),"cannot record voice",Toast.LENGTH_SHORT).show();

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode)
        {
            case REQ_CODE_SPEECH_INPUT:{
                if(resultCode==RESULT_OK && null!=data)
                {
                    ArrayList<String> result=data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    convertedText.setText(result.get(0));
                }
                break;
            }
            case REQ_CODE_SPEECH_FILE:{
                if(resultCode==RESULT_OK && null!=data)
                {
                    Uri audioFile=data.getData();
                    try {
                        mediaPlayer.setDataSource(new FileInputStream(new File(audioFile.getPath())).getFD());
                        mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                            @Override
                            public void onPrepared(MediaPlayer mediaPlayer) {
                                mediaPlayer.start();
                            }
                        });

                        mediaPlayer.prepareAsync();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    Toast.makeText(getApplicationContext(),"aa"+String.valueOf(audioFile.toString()),Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
}
