package ankr.autismhearing;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class MainActivity extends AppCompatActivity implements StringFragment.OnModeChangeListener,
        NameFragment.OnNameSubmitListener, WordTrainFragment.OnStageChangeListener, DrumFragment.OnDrumModeChangeListener,
        DrumTrainFragment.OnFinishDrumModeListener, RecorderFragment.OnRecordingFinishedListener, WordResultFragment.OnNextWordListener {

    public MediaPlayer[] mp = new MediaPlayer[3];
    public MediaPlayer[] drumPlayer = new MediaPlayer[3];
    public String[] words;
    public int wordIndex = 0;

    RecorderFragment recorderFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_main);

        Util.requestPermission(this, Manifest.permission.RECORD_AUDIO);
        Util.requestPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        mp[0] = MediaPlayer.create(this, R.raw.c);
        mp[1] = MediaPlayer.create(this, R.raw.e);
        mp[2] = MediaPlayer.create(this, R.raw.g);

        drumPlayer[0] = MediaPlayer.create(this, R.raw.d1);
        drumPlayer[1] = MediaPlayer.create(this, R.raw.d2);
        drumPlayer[2] = MediaPlayer.create(this, R.raw.d3);

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
        Random rnd = new Random();
        for (int i = ar.length - 1; i > 0; i--)
        {
            int index = rnd.nextInt(i + 1);
            String a = ar[index];
            ar[index] = ar[i];
            ar[i] = a;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0) {
            if (resultCode == RESULT_OK) {
                // Great! User has recorded and saved the audio file
                recorderFragment.finishCurrentRecording();
            } else if (resultCode == RESULT_CANCELED) {
                // Oops! User has canceled the recording
            }
        }
    }


    @Override
    public void switchToStringMode() {
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

    @Override
    public void switchToDrumParam(StringMode mode) {
        DrumFragment drumFragment = new DrumFragment(mode);
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left);
        fragmentTransaction.replace(R.id.container_fragment, drumFragment)
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void switchToDrumWordTrainMode(StringMode mode) {
        DrumTrainFragment drumTrainFragment = new DrumTrainFragment();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left);
        fragmentTransaction.replace(R.id.container_fragment, drumTrainFragment)
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void switchToRecorder() {
        recorderFragment = new RecorderFragment();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left);
        fragmentTransaction.replace(R.id.container_fragment, recorderFragment)
                .addToBackStack(null)
                .commit();

    }

    @Override
    public void switchToWordResultMode() {
        WordResultFragment resultFragment = new WordResultFragment();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left);
        fragmentTransaction.replace(R.id.container_fragment, resultFragment)
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void switchToNextWord() {
        FragmentManager fm = getSupportFragmentManager();
        int count = fm.getBackStackEntryCount()-3;
        for(int i = 0; i < count; ++i) {
            fm.popBackStackImmediate();
        }
    }
}

