package ankr.autismhearing;


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
import android.widget.LinearLayout;
import android.widget.TextView;

import org.w3c.dom.Text;


/**
 * A simple {@link Fragment} subclass.
 */
public class StringFragment extends Fragment {

    Button buttonC,
            buttonE,
            buttonG,
            buttonRecNext,
            buttonViewRecordings,
            buttonContinue;

    Animation blink;

    ViewGroup container, stringC, stringE, stringG;
    View v;

    MediaPlayer c, e, g;

    TextView textView, textView2;


    boolean bc=false,be=false,bg=false;

    OnModeChangeListener callback;

    StringMode mode;
    int stage;

    public StringFragment() {
        // Required empty public constructor
    }

    public StringFragment(StringMode mode) {
        this.mode = mode;
        this.stage = stage;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container_in,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_string, container, false);

        final MainActivity activity = (MainActivity)getActivity();


        buttonC = (Button)v.findViewById(R.id.button_c);
        buttonE = (Button)v.findViewById(R.id.button_e);
        buttonG = (Button)v.findViewById(R.id.button_g);
        buttonContinue = (Button)v.findViewById(R.id.button_continue_first_stage);

        container = (LinearLayout)v.findViewById(R.id.container_viewgroup);
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
            textView2.setText("Tap each of the strings below");
        } else if (mode == StringMode.PARENT_TOUCH_ORDERED) {
            buttonC.startAnimation(blink);
            deactivateButton(buttonE);
            deactivateButton(buttonG);
            textView.setText("Instruction To Parent");
            textView2.setText("Tap on the blinking string and hum the note");
        }

        c = MediaPlayer.create(getContext(), R.raw.c);
        e = MediaPlayer.create(getContext(), R.raw.e);
        g = MediaPlayer.create(getContext(), R.raw.g);

        callback = (OnModeChangeListener) getActivity();

        buttonC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.mp[0].start();
                buttonC.clearAnimation();
                if (mode == StringMode.PARENT_TOUCH_ALL) {
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
                activity.mp[1].start();
                buttonE.clearAnimation();
                if (mode == StringMode.PARENT_TOUCH_ALL) {
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
                activity.mp[2].start();
                buttonG.clearAnimation();
                if (mode == StringMode.PARENT_TOUCH_ALL) {
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
                    callback.switchToStringParam(StringMode.PARENT_TOUCH_ORDERED);
                } else if (mode == StringMode.PARENT_TOUCH_ORDERED) {
                    callback.switchToWordTrainMode(StringMode.PARENT_WORD_TRAIN);
                }
            }
        });

        return v;
    }

    public interface OnModeChangeListener {
        void switchToRecordMode();
        void switchToViewMode();
        void switchToStringParam(StringMode mode);
        void switchToWordTrainMode(StringMode mode);
    }

    void allStringsPressed() {
        if (bc && be && bg) {
            activateButton(buttonContinue);
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

}
