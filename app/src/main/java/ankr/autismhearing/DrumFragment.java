package ankr.autismhearing;


import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import cafe.adriel.androidaudiorecorder.AndroidAudioRecorder;
import cafe.adriel.androidaudiorecorder.model.AudioChannel;
import cafe.adriel.androidaudiorecorder.model.AudioSampleRate;
import cafe.adriel.androidaudiorecorder.model.AudioSource;


/**
 * A simple {@link Fragment} subclass.
 */
public class DrumFragment extends Fragment {

    ImageView buttonC,
            buttonE,
            buttonG;

    Button buttonContinue;

    Animation blink;
    View v;

    TextView textView, textView2;


    boolean bc=false, be=false, bg=false;

    OnDrumModeChangeListener callback;


    StringMode mode;
    int stage;

    public DrumFragment() {
        // Required empty public constructor
    }

    public DrumFragment(StringMode mode) {
        this.mode = mode;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_drum, container, false);

        final MainActivity activity = (MainActivity)getActivity();


        buttonC = (ImageView)v.findViewById(R.id.button_c);
        buttonE = (ImageView) v.findViewById(R.id.button_e);
        buttonG = (ImageView) v.findViewById(R.id.button_g);
        buttonContinue = (Button)v.findViewById(R.id.button_continue_first_stage);

        textView = (TextView)v.findViewById(R.id.textview_instruction_title);
        textView2 = (TextView)v.findViewById(R.id.textview_instruction_subtitle);


        blink = new AlphaAnimation((float)1.0, (float)0.3);
        blink.setDuration(1000);
        blink.setRepeatCount(Animation.INFINITE);
        blink.setRepeatMode(Animation.REVERSE);

        if (mode == StringMode.PARENT_TOUCH_ALL) {
            buttonC.startAnimation(blink);
            buttonE.startAnimation(blink);
            buttonG.startAnimation(blink);
            textView.setText("Instruction To Parent");
            textView2.setText("Beat the blinking drums below");
        } else if (mode == StringMode.CHILD_TOUCH_ALL) {
            buttonC.startAnimation(blink);
            buttonE.startAnimation(blink);
            buttonG.startAnimation(blink);
            textView.setText("Instruction To Child");
            textView2.setText("Beat the blinking drums below");
        }

        callback = (OnDrumModeChangeListener) getActivity();

        buttonC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                activity.drumPlayer[0].start();
                activity.poolPlay(activity.drumPlayer[0]);
                buttonC.clearAnimation();
                if (mode == StringMode.PARENT_TOUCH_ALL || mode == StringMode.CHILD_TOUCH_ALL) {
                    bc = true;
                    allStringsPressed();
                } else if (mode == StringMode.PARENT_TOUCH_ORDERED) {
                    deactivateButton(buttonC);
                    deactivateButton(buttonG);
                    activateButton(buttonE);
                    buttonE.startAnimation(blink);
                }
            }
        });

        buttonE.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                activity.drumPlayer[1].start();
                activity.poolPlay(activity.drumPlayer[1]);
                buttonE.clearAnimation();
                if (mode == StringMode.PARENT_TOUCH_ALL || mode == StringMode.CHILD_TOUCH_ALL) {
                    be = true;
                    allStringsPressed();
                } else if (mode == StringMode.PARENT_TOUCH_ORDERED) {
                    deactivateButton(buttonE);
                    activateButton(buttonG);
                    buttonG.startAnimation(blink);
                }
            }
        });

        buttonG.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                activity.drumPlayer[2].start();
                activity.poolPlay(activity.drumPlayer[2]);
                buttonG.clearAnimation();
                if (mode == StringMode.PARENT_TOUCH_ALL || mode == StringMode.CHILD_TOUCH_ALL) {
                    bg = true;
                    allStringsPressed();
                } else if (mode == StringMode.PARENT_TOUCH_ORDERED) {
                    activateButton(buttonC);
                    buttonC.startAnimation(blink);
                    deactivateButton(buttonE);
                    deactivateButton(buttonG);
                    activateButton(buttonContinue);
                }
            }
        });

        buttonContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mode == StringMode.PARENT_TOUCH_ALL) {
                    callback.switchToDrumParam(StringMode.CHILD_TOUCH_ALL);
                } else if (mode == StringMode.CHILD_TOUCH_ALL) {
                    callback.switchToDrumWordTrainMode(StringMode.PARENT_WORD_TRAIN);
                }
            }
        });

        return v;
    }

    public interface OnDrumModeChangeListener {
        void switchToDrumParam(StringMode mode);
        void switchToDrumWordTrainMode(StringMode mode);
    }

    void allStringsPressed() {
        if (bc && be && bg) {
//            activateButton(buttonContinue);
            if (mode == StringMode.PARENT_TOUCH_ALL) {
                callback.switchToDrumParam(StringMode.CHILD_TOUCH_ALL);
            } else if (mode == StringMode.CHILD_TOUCH_ALL) {
                callback.switchToDrumWordTrainMode(StringMode.PARENT_WORD_TRAIN);
            }
        }
    }

    void deactivateButton(Button b) {
        b.setEnabled(false);
        b.setAlpha((float)0.3);
    }

    void activateButton(Button b) {
        b.setEnabled(true);
        b.setAlpha(1);
    }

    void deactivateButton(ImageView b) {
        b.setEnabled(false);
        b.setAlpha((float)0.3);
    }

    void activateButton(ImageView b) {
        b.setEnabled(true);
        b.setAlpha(1);
    }

}
