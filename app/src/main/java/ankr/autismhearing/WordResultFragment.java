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
    TextView textViewPercent;
    Button buttonNextWord;
    OnNextWordListener callback;

    public WordResultFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_word_result, container, false);
        final MainActivity activity = (MainActivity)getActivity();
        callback = (OnNextWordListener)activity;
        textViewPercent = (TextView)v.findViewById(R.id.textview_percent_done);
        buttonNextWord = (Button)v.findViewById(R.id.button_next_word);

        activity.wordIndex++;
        textViewPercent.setText(String.valueOf(activity.wordIndex) + "%");

        buttonNextWord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callback.switchToNextWord();
            }
        });



        return v;
    }

    public interface OnNextWordListener {
        void switchToNextWord();
    }

}
