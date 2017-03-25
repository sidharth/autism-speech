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
    String name, gender, dob, language, word;
    String childPath, parentPath;
    String childFileName, parentFileName;

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
        callback = activity;

        recordParent = (Button)v.findViewById(R.id.button_record_parent);
        recordChild = (Button)v.findViewById(R.id.button_record_child);

        SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
        name = sharedPref.getString("name","n");
        gender = sharedPref.getString("gender", "a");
        dob = sharedPref.getString("birthday", "00000000");
        language = sharedPref.getString("language","lang");
        word = activity.words[activity.wordIndex];

        Log.d("folderyay", activity.baseFolder.toString());

        childFileName = name + "_" +
                gender + "_"+
                dob + "_" +
                word;

        parentFileName = childFileName + "_parent";

        childPath = activity.baseFolder.toString() + File.separator +
                childFileName;
        parentPath = childPath + "_parent";

        recordParent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int color = getResources().getColor(R.color.colorPrimary);
                int requestCode = 0;
                AndroidAudioRecorder.with(getActivity())
                        // Required
                        .setFilePath(parentPath)
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
                int color = getResources().getColor(R.color.colorPrimaryDark);
                int requestCode = 0;
                AndroidAudioRecorder.with(getActivity())
                        // Required
                        .setFilePath(childPath)
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
        final MainActivity activity = (MainActivity)getActivity();
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
            Log.d("filename", childFileName);
            Log.d("filename", parentFileName);
            activity.uploadChildFilesToSubjectFolder(childFileName);
            activity.uploadParentFilesToSubjectFolder(parentFileName);
            callback.switchToWordResultMode();
        }
    }

    public interface OnRecordingFinishedListener {
        void switchToWordResultMode();
    }

}
