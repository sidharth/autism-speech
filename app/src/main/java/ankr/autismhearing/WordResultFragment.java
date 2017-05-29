package ankr.autismhearing;


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
public class WordResultFragment extends Fragment {
    TextView textViewPercent, textViewTitle, textViewSubtitle;
    Button buttonNextWord;
    OnNextWordListener callback;
    String percent;

    public WordResultFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_word_result, container, false);
        final MainActivity activity = (MainActivity)getActivity();

//        activity.firework.start();
        activity.poolPlay(activity.firework);


        callback = (OnNextWordListener)activity;

        textViewPercent = (TextView)v.findViewById(R.id.textview_percent_done);
        buttonNextWord = (Button)v.findViewById(R.id.button_next_word);
        textViewTitle = (TextView)v.findViewById(R.id.textview_result_title);
        textViewSubtitle = (TextView)v.findViewById(R.id.textview_result_subtitle);
//
        percent = String.format("%.2f",((float)(activity.wordIndex+1) * 100) / activity.words.length);
        activity.wordIndex++;
        activity.wordIndex %= 15;

        textViewPercent.setText(percent + "%");

        if (activity.wordIndex  == 0) {
            textViewTitle.setText("CONGRATULATIONS");
            textViewSubtitle.setText("You're all done :-)");
            buttonNextWord.setText("+ New Participant");
        }

        buttonNextWord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (activity.wordIndex != 0) {
                    callback.switchToNextWord();
                }
                else {
                    callback.switchToNextParticipant();
                }
            }
        });

        return v;
    }

    public interface OnNextWordListener {
        void switchToNextWord();
        void switchToNextParticipant();
    }

}
