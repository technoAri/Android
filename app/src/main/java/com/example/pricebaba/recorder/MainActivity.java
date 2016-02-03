package com.example.pricebaba.recorder;

import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    Button startButton,stopButton,playButton;
    MediaRecorder audioRecoder;
    MediaPlayer mediaPlayer;
    String OutputFile;
    GifView gifView;
    SeekBar seekBar;

    TextView textView;
    Handler handler;
    VisualizerView visualizerView;
    int REPEAT_INTERVAL=20;
    Runnable updateVisualiser;
    int maxAmp;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        startButton=(Button)findViewById(R.id.recordButton);
        stopButton=(Button)findViewById(R.id.stopButton);
        playButton=(Button)findViewById(R.id.playButton);
        textView=(TextView)findViewById(R.id.textVisualise);
        textView=(TextView)findViewById(R.id.textVisualise);
        //gifView=(GifView)findViewById(R.id.gifview);
        seekBar=(SeekBar)findViewById(R.id.seekBar);
        visualizerView=(VisualizerView)findViewById(R.id.visualiser);

        stopButton.setEnabled(false);
        //gifView.setVisibility(View.INVISIBLE);
        playButton.setEnabled(false);
        seekBar.setVisibility(View.INVISIBLE);

        OutputFile= Environment.getExternalStorageDirectory().getAbsolutePath() + "/myrecording.3gp";
        audioRecoder=new MediaRecorder();
        audioRecoder.setAudioSource(MediaRecorder.AudioSource.MIC);
        audioRecoder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        audioRecoder.setAudioEncoder(MediaRecorder.OutputFormat.AMR_NB);
        audioRecoder.setOutputFile(OutputFile);

        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    audioRecoder.prepare();
                    audioRecoder.start();
                    audioRecoder.getMaxAmplitude();
                    }
                catch (IllegalStateException e) {
                    e.printStackTrace();
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
                startButton.setEnabled(false);
                stopButton.setEnabled(true);
                //gifView.setVisibility(View.VISIBLE);
                //visualizerView.onDraw(canvas);

                Toast.makeText(getApplicationContext(),"Recording Started",
                        Toast.LENGTH_SHORT).show();

                handler=new Handler();
                handler.post(updateVisualiser);

            }
        });

        stopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                visualizerView.clear();
                try {
                    audioRecoder.stop();
                }
                catch (IllegalStateException e){
                    e.printStackTrace();
                }
                if(audioRecoder!=null) {
                    Toast.makeText(getApplicationContext(), "Maximum Amplitude is:" + audioRecoder.getMaxAmplitude(),
                            Toast.LENGTH_SHORT).show();
                }
                audioRecoder.release();
                audioRecoder  = null;

                stopButton.setEnabled(false);
                playButton.setEnabled(true);
                //gifView.setVisibility(View.INVISIBLE);
                Toast.makeText(getApplicationContext(), "Recording Stopped",
                        Toast.LENGTH_SHORT).show();
            }
        });

        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mediaPlayer=new MediaPlayer();
                try {
                    mediaPlayer.setDataSource(OutputFile);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                try {
                    mediaPlayer.prepare();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                mediaPlayer.start();
                seekBar.setVisibility(View.VISIBLE);
                seekBar.setMax(mediaPlayer.getDuration());

                Toast.makeText(getApplicationContext(),"Playing recorded audio",
                        Toast.LENGTH_SHORT).show();

            }
        });
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            int progress;
            @Override
            public void onProgressChanged(final SeekBar seekBar, int progressValue, boolean fromUser) {
                progress=progressValue;
                int duration=mediaPlayer.getDuration();
                seekBar.setProgress(duration);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                textView.setText(progress + "/" + seekBar.getMax());
            }
        });
        updateVisualiser=new Runnable() {
            @Override
            public void run() {
                //if(maxAmp!=null) {
                    maxAmp = audioRecoder.getMaxAmplitude();
                    visualizerView.addAmplitude(maxAmp);
                    visualizerView.invalidate();

                    handler.postDelayed(this, REPEAT_INTERVAL);
                //}
            }
        };

    }



}