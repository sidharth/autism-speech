package ankr.autismhearing;


import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.media.SoundPool;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;


/**
 * A simple {@link Fragment} subclass.
 */
public class WordProcessingFragment extends Fragment {

    public LinearLayout view_container;
    public TextView textViewSkip;
    Button buttonRetry;
    String word;

    public WordProcessingFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_word_processing, container, false);
        final MainActivity activity = (MainActivity)getActivity();

        activity.createMediaPlayers();
        view_container = (LinearLayout)v.findViewById(R.id.container_word_processing);
        textViewSkip = (TextView)v.findViewById(R.id.textview_skip_word);
        buttonRetry = (Button)v.findViewById(R.id.button_retry);

        word = activity.words[activity.wordIndex];
        saveToDb();

        textViewSkip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                WordResultFragment resultFragment = new WordResultFragment();
                FragmentTransaction fragmentTransaction = activity.getSupportFragmentManager().beginTransaction();
                fragmentTransaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left);
                fragmentTransaction.replace(R.id.container_fragment, resultFragment)
                        .commit();
            }
        });

        buttonRetry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().onBackPressed();
            }
        });

//        final MaterialDialog dialog = new MaterialDialog.Builder(getActivity())
//                .title("Processing audio")
//                .content("Please wait")
//                .progress(true, 0)
//                .widgetColorRes(R.color.wallet_holo_blue_light)
//                .cancelable(false)
//                .show();

//        final Handler handler = new Handler();
//        handler.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                //Do something after 100ms
//                dialog.dismiss();
//                view_container.setVisibility(View.VISIBLE);
////                activity.rain.start();
//                activity.poolPlay(activity.rain);
//            }
//        }, 5000);

        view_container.setVisibility(View.VISIBLE);
//        activity.poolPlay(activity.rain);

        return v;
    }

    @Override
    public void onResume(){
        super.onResume();
        final MainActivity activity = (MainActivity) getActivity();
        Log.d("DEBUG","playing rain");
//        activity.poolPlay(activity.rain);
        activity.rain = activity.pool.load(activity,R.raw.rain,0);
        activity.pool.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener() {
            @Override
            public void onLoadComplete(SoundPool soundPool, int sampleId, int status) {
                if(sampleId == activity.rain) {
                    soundPool.play(sampleId, 1, 1, 0, 0, 1);
                    soundPool.unload(sampleId);
                }
            }
        });
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
