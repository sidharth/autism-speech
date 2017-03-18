package ankr.autismhearing;


import android.content.Context;
import android.content.SharedPreferences;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.github.piasy.rxandroidaudio.AudioRecorder;

import java.io.File;

import ankr.autismhearing.visualizer.RecordingSampler;
import ankr.autismhearing.visualizer.VisualizerView;
import cafe.adriel.androidaudiorecorder.AndroidAudioRecorder;
import cafe.adriel.androidaudiorecorder.model.AudioChannel;
import cafe.adriel.androidaudiorecorder.model.AudioSampleRate;
import cafe.adriel.androidaudiorecorder.model.AudioSource;


/**
 * A simple {@link Fragment} subclass.
 */
public class RecorderFragment extends Fragment {

//    Button recordButton;

    Button recordChild, recordParent;

    int lastPressed = 0;

    OnRecordingFinishedListener callback;

    public RecorderFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_recorder, container, false);

        final MainActivity activity = (MainActivity)getActivity();
        callback = (OnRecordingFinishedListener)activity;

        recordParent = (Button)v.findViewById(R.id.button_record_parent);
        recordChild = (Button)v.findViewById(R.id.button_record_child);

        recordParent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String filePath = Environment.getExternalStorageDirectory() + "/recorded_audio.wav";
                int color = getResources().getColor(R.color.colorPrimary);
                int requestCode = 0;
                AndroidAudioRecorder.with(getActivity())
                        // Required
                        .setFilePath(filePath)
                        .setColor(color)
                        .setRequestCode(requestCode)

                        // Optional
                        .setSource(AudioSource.MIC)
                        .setChannel(AudioChannel.STEREO)
                        .setSampleRate(AudioSampleRate.HZ_44100)
                        .setAutoStart(false)
                        .setKeepDisplayOn(true)

                        // Start recording
                        .record();

                lastPressed = 1;
            }
        });

        recordChild.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String filePath = Environment.getExternalStorageDirectory() + "/recorded_audio.wav";
                int color = getResources().getColor(R.color.colorPrimaryDark);
                int requestCode = 0;
                AndroidAudioRecorder.with(getActivity())
                        // Required
                        .setFilePath(filePath)
                        .setColor(color)
                        .setRequestCode(requestCode)

                        // Optional
                        .setSource(AudioSource.MIC)
                        .setChannel(AudioChannel.STEREO)
                        .setSampleRate(AudioSampleRate.HZ_44100)
                        .setAutoStart(false)
                        .setKeepDisplayOn(true)

                        // Start recording
                        .record();
                lastPressed = 2;
            }
        });

        return v;
    }

    public void finishCurrentRecording() {
        Log.d("lastpressed", "something");

        if (lastPressed == 1) {
            Log.d("lastpressed", "1");
            recordParent.setEnabled(false);
            recordParent.setText("✓");
        } else if (lastPressed == 2) {
            Log.d("lastpressed", "2");
            recordChild.setEnabled(false);
            recordChild.setText("✓");
        }

        if (recordParent.getText().toString().contains("✓") &&
                recordChild.getText().toString().contains("✓")) {
            callback.switchToWordResultMode();
        }
    }

    public interface OnRecordingFinishedListener {
        void switchToWordResultMode();
    }

}
