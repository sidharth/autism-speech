package ankr.autismhearing;

import android.media.MediaPlayer;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class MainActivity extends AppCompatActivity implements StringFragment.OnModeChangeListener,
        NameFragment.OnNameSubmitListener, WordTrainFragment.OnStageChangeListener, DrumFragment.OnDrumModeChangeListener{

    public MediaPlayer[] mp = new MediaPlayer[3];
    public String[] words;
    public int wordIndex = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // hide the action bar
//        View decorView = getWindow().getDecorView();
//        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
//        decorView.setSystemUiVisibility(uiOptions);

        setContentView(R.layout.activity_main);

        mp[0] = MediaPlayer.create(this, R.raw.c);
        mp[1] = MediaPlayer.create(this, R.raw.e);
        mp[2] = MediaPlayer.create(this, R.raw.g);

        words = getResources().getStringArray(R.array.wordlist);
        shuffleArray(words);

        NameFragment nameFragment = new NameFragment();
        StringFragment stringFragment = new StringFragment();

        getSupportFragmentManager().beginTransaction()
                .add(R.id.container_fragment, nameFragment)
                .commit();

    }

    // Implementing Fisher–Yates shuffle
    static void shuffleArray(String[] ar)
    {
        // If running on Java 6 or older, use `new Random()` on RHS here
        Random rnd = new Random();
        for (int i = ar.length - 1; i > 0; i--)
        {
            int index = rnd.nextInt(i + 1);
            // Simple swap
            String a = ar[index];
            ar[index] = ar[i];
            ar[i] = a;
        }
    }


    @Override
    public void switchToRecordMode() {
        RecordFragment recordFragment = new RecordFragment();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container_fragment, recordFragment)
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void switchToViewMode() {
        ViewRecordingsFragment fragment = new ViewRecordingsFragment();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container_fragment, fragment)
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void switchToStringMode() {
//        WordResultFragment fragment = new WordResultFragment();
        StringFragment stringFragment = new StringFragment(StringMode.PARENT_TOUCH_ALL);
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left);
        fragmentTransaction.replace(R.id.container_fragment, stringFragment)
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void switchToStringParam(StringMode mode) {
        StringFragment stringFragment = new StringFragment(mode);
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left);
        fragmentTransaction.replace(R.id.container_fragment, stringFragment)
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void switchToWordTrainMode(StringMode mode) {
        WordTrainFragment wordTrainFragment = new WordTrainFragment();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left);
        fragmentTransaction.replace(R.id.container_fragment, wordTrainFragment)
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void switchToStageDrumMode(StringMode mode) {
        DrumFragment drumFragment = new DrumFragment(mode);
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left);
        fragmentTransaction.replace(R.id.container_fragment, drumFragment)
                .addToBackStack(null)
                .commit();
    }
}

