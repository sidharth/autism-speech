package ankr.autismhearing;


import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import cafe.adriel.androidaudiorecorder.AndroidAudioRecorder;
import cafe.adriel.androidaudiorecorder.model.AudioChannel;
import cafe.adriel.androidaudiorecorder.model.AudioSampleRate;
import cafe.adriel.androidaudiorecorder.model.AudioSource;


/**
 * A simple {@link Fragment} subclass.
 */
public class DrumTrainFragment extends Fragment {
    StringMode mode;
    Button buttonC, buttonE, buttonG;
    Button nextStageButton;
    TextView textViewWord;

    OnFinishDrumModeListener callback;

    public DrumTrainFragment() {
        // Required empty public constructor
    }

    public DrumTrainFragment(StringMode mode) {
        this.mode = mode;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_drum_train, container, false);

        final MainActivity activity = (MainActivity)getActivity();

        buttonC = (Button)v.findViewById(R.id.button_c);
        buttonE = (Button)v.findViewById(R.id.button_e);
        buttonG = (Button)v.findViewById(R.id.button_g);
        nextStageButton = (Button)v.findViewById(R.id.button_next_stage);
        textViewWord = (TextView)v.findViewById(R.id.textview_word);

        callback = (OnFinishDrumModeListener)activity;

        textViewWord.setText(activity.words[activity.wordIndex]);

        buttonC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.drumPlayer[0].start();
            }
        });

        buttonE.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.drumPlayer[1].start();
            }
        });

        buttonG.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.drumPlayer[2].start();
            }
        });

        nextStageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callback.switchToRecorder();
            }
        });

        return v;
    }


    public interface OnFinishDrumModeListener {
        void switchToRecorder();
    }



}
