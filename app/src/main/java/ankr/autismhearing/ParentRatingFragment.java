package ankr.autismhearing;


import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;


/**
 * A simple {@link Fragment} subclass.
 */
public class ParentRatingFragment extends Fragment {

    ImageView[] emo = new ImageView[3];
    String word;


    public ParentRatingFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_parent_rating, container, false);
        final MainActivity activity = (MainActivity)getActivity();

        word = activity.words[activity.wordIndex];
        saveToDb();


        emo[0] = (ImageView)v.findViewById(R.id.emoticon_rating_1);
        emo[1] = (ImageView)v.findViewById(R.id.emoticon_rating_2);
        emo[2] = (ImageView)v.findViewById(R.id.emoticon_rating_3);

        for (int i=0; i<3; i++) {
            emo[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int score = Integer.valueOf(((ImageView)view).getContentDescription().toString());
                    if (score <= 1) {
                        switchToNegativeResultMode();
                    }
                    else {
                        switchToPositiveResultMode();
                    }
                }
            });
        }

        return v;
    }

    private void switchToPositiveResultMode() {
        WordResultFragment wordResultFragment = new WordResultFragment();
        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left);
        fragmentTransaction.replace(R.id.container_fragment, wordResultFragment)
                .addToBackStack(null)
                .commit();
    }

    private void switchToNegativeResultMode() {
        WordProcessingFragment processingFragment = new WordProcessingFragment();
        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left);
        fragmentTransaction.replace(R.id.container_fragment, processingFragment)
                .addToBackStack(null)
                .commit();
    }

    private void saveToDb() {
        SQLiteDatabase database = new WordDbHelper(getContext()).getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(WordContract.WordEntry.COLUMN_WORD, word);
        values.put(WordContract.WordEntry.COLUMN_COUNT, 1);
        long row = database.insert(WordContract.WordEntry.TABLE_NAME, null, values);
        Toast.makeText(getContext(), "Added to database"+ String.valueOf(row), Toast.LENGTH_SHORT).show();
    }

}
