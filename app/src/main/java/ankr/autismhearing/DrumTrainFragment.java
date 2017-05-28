package ankr.autismhearing;


import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.HashMap;

import cafe.adriel.androidaudiorecorder.AndroidAudioRecorder;
import cafe.adriel.androidaudiorecorder.model.AudioChannel;
import cafe.adriel.androidaudiorecorder.model.AudioSampleRate;
import cafe.adriel.androidaudiorecorder.model.AudioSource;


/**
 * A simple {@link Fragment} subclass.
 */
public class DrumTrainFragment extends Fragment {
    StringMode mode;
    ImageView image;
    ImageView buttonC, buttonE, buttonG;
    Button nextStageButton;
    TextView textViewWord, textViewInstruction;

    String wordResource, word;

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
        callback = (OnFinishDrumModeListener)activity;

        if (!activity.isPlayerSet) {
            activity.createMediaPlayers();
        }

        HashMap<String, Integer> imageRes = new HashMap<>();
        imageRes.put("balloon", R.drawable.balloon);
        imageRes.put("chair", R.drawable.chair);
        imageRes.put("didi", R.drawable.didi);
        imageRes.put("chocolate", R.drawable.chocolate);
        imageRes.put("elephant", R.drawable.elephant);
        imageRes.put("finish", R.drawable.finish);
        imageRes.put("flower", R.drawable.flower);
        imageRes.put("give", R.drawable.give);
        imageRes.put("hello", R.drawable.hello);
        imageRes.put("hugpapa", R.drawable.hugpapa);
        imageRes.put("kissmumma", R.drawable.kissmumma);
        imageRes.put("lunch", R.drawable.lunch);
        imageRes.put("mouth", R.drawable.mouth);
        imageRes.put("sun", R.drawable.sun);
        imageRes.put("train", R.drawable.train);
        imageRes.put("tummy", R.drawable.tummy);

        image = (ImageView)v.findViewById(R.id.imageview_word_train_drum);
        buttonC = (ImageView)v.findViewById(R.id.button_c);
        buttonE = (ImageView)v.findViewById(R.id.button_e);
        buttonG = (ImageView)v.findViewById(R.id.button_g);
        nextStageButton = (Button)v.findViewById(R.id.button_next_stage);
        textViewWord = (TextView)v.findViewById(R.id.textview_word);
        textViewInstruction = (TextView)v.findViewById(R.id.textview_instruction_title);

        word = activity.words[activity.wordIndex];
        wordResource = word.toLowerCase().replaceAll("[^a-z]","");

        Log.d("DrumTrainFragment", wordResource);
        Picasso.with(getActivity()).load(imageRes.get(wordResource)).resize(384, 288).into(image);
//        image.setImageDrawable(getResources().getDrawable(imageRes.get(wordResource)));

        if (mode == StringMode.CHILD_WORD_TRAIN) {
            textViewInstruction.setText("Instruction to Child");
        }

        textViewWord.setText(word);

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
                if (mode == StringMode.PARENT_WORD_TRAIN) {
//                    image.setVisibility(View.GONE);
                    callback.switchToDrumTrainModeParam(StringMode.CHILD_WORD_TRAIN);
                } else if (mode == StringMode.CHILD_WORD_TRAIN) {
//                    image.setVisibility(View.GONE);
                    callback.switchToRecorder();
                }
            }
        });

        return v;
    }

    public interface OnFinishDrumModeListener {
        void switchToDrumTrainModeParam(StringMode mode);
        void switchToRecorder();
    }

}
