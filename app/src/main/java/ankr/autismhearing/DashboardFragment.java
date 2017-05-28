package ankr.autismhearing;


import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridView;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class DashboardFragment extends Fragment {

    GridView gridView;
    Button buttonStartWord;

    List<String> recordedWords = new ArrayList<>();

    public DashboardFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_dashboard, container, false);
        final MainActivity activity = (MainActivity)getActivity();
        gridView = (GridView)v.findViewById(R.id.grid_completed_words);
        buttonStartWord = (Button)v.findViewById(R.id.button_start_word);
        readFromDb();

        buttonStartWord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activity.switchToStringMode();
            }
        });

//        FeedReaderDbHelper mDbHelper = new FeedReaderDbHelper(getContext());
//        WordContract.WordDbHelper mDbHelper = new WordContract.WordDbHelper(getContext());
        WordGridAdapter gridAdapter = new WordGridAdapter(getContext(), recordedWords);
        gridView.setAdapter(gridAdapter);



        return v;
    }

    private void readFromDb() {
        SQLiteDatabase database = new WordDbHelper(getContext()).getReadableDatabase();
        String[] projection = {WordContract.WordEntry.COLUMN_WORD};
        String[] selection = {};
        Cursor cursor = database.query(true, WordContract.WordEntry.TABLE_NAME, projection, null, null, null, null, null, null);
        Log.d("sid","total count: " + String.valueOf(cursor.getCount()));
        recordedWords.clear();
        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                String curr = cursor.getString(cursor.getColumnIndex(WordContract.WordEntry.COLUMN_WORD));
                recordedWords.add(curr);
                Log.d("sid_word", curr);
                cursor.moveToNext();
            }
        }

    }

}
