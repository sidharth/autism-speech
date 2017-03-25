package ankr.autismhearing;


import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.HashMap;
import java.util.Hashtable;


/**
 * A simple {@link Fragment} subclass.
 */
public class WordTrainFragment extends Fragment {

    private static String TAG = "word-train-fragment";

    StringMode mode;
    Button circleButton, nextStageButton;
    ImageView image;
    int counter = 0;
    int syllables = 1;
    String word;
    String wordResource;

    TextView textViewWord;

    OnStageChangeListener callback;

    TextView textViewInstructionTitle;


    public WordTrainFragment() {
        // Required empty public constructor
    }

    public WordTrainFragment(StringMode mode) {
        this.mode = mode;
    }

    public interface OnStageChangeListener {
        void switchToStageDrumMode(StringMode mode);
        void switchToStringWordTrainModeParam(StringMode mode);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_word_train, container, false);
        final MainActivity activity = (MainActivity)getActivity();
        callback = (OnStageChangeListener)activity;

        textViewWord = (TextView)v.findViewById(R.id.textview_word);
        textViewInstructionTitle = (TextView)v.findViewById(R.id.textview_instruction_title);
        circleButton = (Button)v.findViewById(R.id.button_circle);
        nextStageButton = (Button)v.findViewById(R.id.button_next_stage);
        image = (ImageView)v.findViewById(R.id.imageview_word_train);

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



        word = activity.words[activity.wordIndex];
        wordResource = word.toLowerCase().replaceAll("[^a-z]","");

        Log.d(TAG, wordResource);

//        image.setImageResource(getContext().getResources().getIdentifier(word, "drawable", getContext().getPackageName()));
        image.setImageResource(imageRes.get(wordResource));

        for (int i=0; i<word.length(); i++) {
            if (word.charAt(i) =='-' || word.charAt(i) ==' ') {
                syllables++;
            }
        }

        if (mode == StringMode.CHILD_TOUCH_ALL) {
            textViewInstructionTitle.setText("Instruction to Child");
        }

        textViewWord.setText(word);

        circleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.mp[counter].start();
                counter = (counter+1)%syllables;
            }
        });

        nextStageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mode == StringMode.CHILD_TOUCH_ALL) {
                    callback.switchToStageDrumMode(StringMode.PARENT_TOUCH_ALL);
                } else {
                    callback.switchToStringWordTrainModeParam(StringMode.CHILD_TOUCH_ALL);
                }
            }
        });


        return v;
    }

}
