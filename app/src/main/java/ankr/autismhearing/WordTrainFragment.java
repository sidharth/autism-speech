package ankr.autismhearing;


import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Hashtable;


/**
 * A simple {@link Fragment} subclass.
 */
public class WordTrainFragment extends Fragment {

    private static String TAG = "word-train-fragment";

    StringMode mode;
    Button nextStageButton;
    ImageView image;
    int syllables = 1;
    String word;
    String wordResource;

    TextView textViewWord;
    ImageView strings[] = new ImageView[6];
    ImageView instructionImage;

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
        nextStageButton = (Button)v.findViewById(R.id.button_next_stage);
        image = (ImageView)v.findViewById(R.id.imageview_word_train);
        strings[0] = (ImageView)v.findViewById(R.id.button_c);
        strings[1] = (ImageView)v.findViewById(R.id.button_d);
        strings[2] = (ImageView)v.findViewById(R.id.button_f);
        strings[3] = (ImageView)v.findViewById(R.id.button_g);
//        strings[4] = (ImageView)v.findViewById(R.id.button_a);
//        strings[5] = (ImageView)v.findViewById(R.id.button_b);

        instructionImage = (ImageView)v.findViewById(R.id.instruction_hand_string);

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
        syllables = activity.syllables[activity.wordIndex];
        wordResource = word.toLowerCase().replaceAll("[^a-z]","");
        final Animation vibrate_animation = AnimationUtils.loadAnimation(getActivity(),R.anim.vibrate);
        Log.d(TAG, wordResource);

        Picasso.with(getActivity())
                .load(imageRes.get(wordResource))
                .resize(500, 375)
                .centerInside()
                .into(image);
//        image.setImageDrawable(getResources().getDrawable(imageRes.get(wordResource)));

        Picasso.with(getActivity())
                .load(R.drawable.instruction_hand_string)
                .resize(150, 150)
                .centerInside()
                .into(instructionImage);

        if (mode == StringMode.CHILD_TOUCH_ALL) {
            textViewInstructionTitle.setText("Instruction to Child");
        }

        for (int i=syllables; i<4; i++) {
            strings[i].setVisibility(View.INVISIBLE);
        }

        for (int i=0; i<syllables; i++) {
            strings[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int index = view.getContentDescription().charAt(0)-'0';
//                    activity.stringPlayer[index].start();
                    view.clearAnimation();
                    view.startAnimation(vibrate_animation);
                    activity.poolPlay(activity.stringPlayer[index]);

                }
            });
        }

        textViewWord.setText(word);

        nextStageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mode == StringMode.CHILD_TOUCH_ALL) {
                    image.setVisibility(View.GONE);
                    callback.switchToStageDrumMode(StringMode.PARENT_TOUCH_ALL);
                } else {
                    image.setVisibility(View.GONE);
                    callback.switchToStringWordTrainModeParam(StringMode.CHILD_TOUCH_ALL);
                }
            }
        });


        return v;
    }

}
