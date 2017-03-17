package ankr.autismhearing;


import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 */
public class WordTrainFragment extends Fragment {

    StringMode mode;
    Button circleButton, nextStageButton;
    int counter = 0;
    int syllables = 1;
    String word;

    TextView textViewWord;

    OnStageChangeListener callback;


    public WordTrainFragment() {
        // Required empty public constructor
    }

    public WordTrainFragment(StringMode mode) {
        this.mode = mode;
    }

    public interface OnStageChangeListener {
        void switchToStageDrumMode(StringMode mode);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_word_train, container, false);
        final MainActivity activity = (MainActivity)getActivity();
        callback = (OnStageChangeListener)activity;

        textViewWord = (TextView)v.findViewById(R.id.textview_word);
        circleButton = (Button)v.findViewById(R.id.button_circle);
        nextStageButton = (Button)v.findViewById(R.id.button_next_stage);

        word = activity.words[activity.wordIndex];

        for (int i=0; i<word.length(); i++) {
            if (word.charAt(i) =='-' || word.charAt(i) ==' ') {
                syllables++;
            }
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
                callback.switchToStageDrumMode(StringMode.PARENT_TOUCH_ALL);
            }
        });

        return v;
    }

}
