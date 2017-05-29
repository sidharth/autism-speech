package ankr.autismhearing;


import android.media.Image;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.w3c.dom.Text;


/**
 * A simple {@link Fragment} subclass.
 */
public class StringFragment extends Fragment {
    Button buttonContinue;

    ImageView strings[];
    ImageView instructionHand;

    boolean touched[];

    Animation blink;

    ViewGroup container;
    View v;

    TextView textView, textView2;


    boolean bc=false,be=false,bg=false;

    OnModeChangeListener callback;

    StringMode mode;
    int syllables;

    public StringFragment() {
        // Required empty public constructor
    }

    public StringFragment(StringMode mode) {
        this.mode = mode;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container_in,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_string, container, false);

        final MainActivity activity = (MainActivity)getActivity();

        strings = new ImageView[6];
        touched = new boolean[6];
        strings[0] = (ImageView)v.findViewById(R.id.button_c);
        strings[1] = (ImageView)v.findViewById(R.id.button_d);
        strings[2] = (ImageView)v.findViewById(R.id.button_f);
        strings[3] = (ImageView)v.findViewById(R.id.button_g);
//        strings[4] = (ImageView)v.findViewById(R.id.button_a);
//        strings[5] = (ImageView)v.findViewById(R.id.button_b);

        instructionHand = (ImageView)v.findViewById(R.id.instruction_hand);


        syllables = activity.syllables[activity.wordIndex];

        buttonContinue = (Button)v.findViewById(R.id.button_continue_first_stage);

        container = (LinearLayout)v.findViewById(R.id.container_viewgroup);
        textView = (TextView)v.findViewById(R.id.textview_instruction_title);
        textView2 = (TextView)v.findViewById(R.id.textview_instruction_subtitle);


        blink = new AlphaAnimation((float)1.0, (float)0.3);
        blink.setDuration(1000);
        blink.setRepeatCount(Animation.INFINITE);
        blink.setRepeatMode(Animation.REVERSE);

        if (mode == StringMode.PARENT_TOUCH_ALL) {

            for (int i=0; i<4; i++) {
                strings[i].startAnimation(blink);
            }
            textView.setText("Instruction To Parent");
            textView2.setText("Tap each of the strings below");
        } else if (mode == StringMode.PARENT_TOUCH_ORDERED) {
            instructionHand.setImageResource(R.drawable.instruction_hand_string);
            strings[0].startAnimation(blink);
            for (int i=1; i<4; i++) {
                deactivateButton(strings[i]);
            }
            for (int i=syllables; i<4; i++) {
                strings[i].setVisibility(View.INVISIBLE);
            }
            activateButton(strings[0]);
            textView.setText("Instruction To Parent");
            textView2.setText("Tap on the blinking string and hum the note");
        } else if (mode == StringMode.CHILD_TOUCH_ALL) {
            for (int i=0; i<4; i++) {
                strings[i].startAnimation(blink);
            }
            textView.setText("Instruction To Child");
            textView2.setText("Tap each of the strings below");
        } else if (mode == StringMode.CHILD_TOUCH_ORDERED) {
            instructionHand.setImageResource(R.drawable.instruction_hand_string);
            strings[0].startAnimation(blink);
            for (int i=1; i<4; i++) {
                deactivateButton(strings[i]);
            }
            for (int i=syllables; i<4; i++) {
                strings[i].setVisibility(View.INVISIBLE);
            }
            activateButton(strings[0]);
            textView.setText("Instruction To Child");
            textView2.setText("Tap on the blinking string and hum the note");
        }

        callback = (OnModeChangeListener) getActivity();

        for (int i=0; i<4; i++) {
            strings[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int index = view.getContentDescription().charAt(0) - '0';
//                    activity.stringPlayer[index].start();
                    activity.poolPlay(activity.stringPlayer[index]);
                    view.clearAnimation();
                    if (mode == StringMode.PARENT_TOUCH_ALL || mode == StringMode.CHILD_TOUCH_ALL) {
                        touched[index] = true;
                        allStringsPressed();
                    } else if (mode == StringMode.PARENT_TOUCH_ORDERED || mode == StringMode.CHILD_TOUCH_ORDERED) {
                        deactivateButton(strings[index]);
                        activateButton(strings[(index+1)%syllables]);
                        strings[(index+1)%syllables].startAnimation(blink);
                        if (index == syllables-1) {
                            activateButton(buttonContinue);
//                            if (mode == StringMode.PARENT_TOUCH_ORDERED) {
//                                callback.switchToStringParam(StringMode.CHILD_TOUCH_ORDERED);
//                            } else if (mode == StringMode.CHILD_TOUCH_ORDERED) {
//                                callback.switchToWordTrainMode(StringMode.PARENT_WORD_TRAIN);
//                            }
                        }
                    }

                }
            });
        }

        buttonContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mode == StringMode.PARENT_TOUCH_ALL) {
                    callback.switchToStringParam(StringMode.CHILD_TOUCH_ALL);
                } else if (mode == StringMode.CHILD_TOUCH_ALL) {
                    callback.switchToStringParam(StringMode.PARENT_TOUCH_ORDERED);
                }
                else if (mode == StringMode.PARENT_TOUCH_ORDERED) {
                    callback.switchToStringParam(StringMode.CHILD_TOUCH_ORDERED);
                } else if (mode == StringMode.CHILD_TOUCH_ORDERED) {
                    callback.switchToWordTrainMode(StringMode.PARENT_WORD_TRAIN);
                }
            }
        });


        return v;
    }

    public interface OnModeChangeListener {
        void switchToStringParam(StringMode mode);
        void switchToWordTrainMode(StringMode mode);
    }

    void allStringsPressed() {
        for (int i=0; i<4; i++) {
            if (!touched[i]) {
                return;
            }
        }

        if (mode == StringMode.PARENT_TOUCH_ALL) {
            callback.switchToStringParam(StringMode.CHILD_TOUCH_ALL);
        } else if (mode == StringMode.CHILD_TOUCH_ALL) {
            callback.switchToStringParam(StringMode.PARENT_TOUCH_ORDERED);
        }
        else if (mode == StringMode.PARENT_TOUCH_ORDERED) {
            callback.switchToStringParam(StringMode.CHILD_TOUCH_ORDERED);
        } else if (mode == StringMode.CHILD_TOUCH_ORDERED) {
            callback.switchToWordTrainMode(StringMode.PARENT_WORD_TRAIN);
        }
//        activateButton(buttonContinue);
    }

    void deactivateButton(ImageView b) {
        b.setEnabled(false);
        b.setAlpha((float)0.3);
    }

    void activateButton(ImageView b) {
        b.setEnabled(true);
        b.setAlpha((float)1.0);
    }

    void deactivateButton(Button b) {
        b.setEnabled(false);
        b.setAlpha((float)0.3);
    }

    void activateButton(Button b) {
        b.setEnabled(true);
        b.setAlpha((float)1.0);
    }

}
