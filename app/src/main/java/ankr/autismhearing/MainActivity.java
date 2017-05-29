package ankr.autismhearing;

import android.Manifest;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.drive.Drive;
import com.google.android.gms.drive.DriveApi;
import com.google.android.gms.drive.DriveContents;
import com.google.android.gms.drive.DriveFolder;
import com.google.android.gms.drive.DriveId;
import com.google.android.gms.drive.MetadataBuffer;
import com.google.android.gms.drive.MetadataChangeSet;
import com.google.android.gms.drive.query.Filters;
import com.google.android.gms.drive.query.Query;
import com.google.android.gms.drive.query.SearchableField;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Writer;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class MainActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, StringFragment.OnModeChangeListener,
        NameFragment.OnNameSubmitListener, WordTrainFragment.OnStageChangeListener, DrumFragment.OnDrumModeChangeListener,
        DrumTrainFragment.OnFinishDrumModeListener, RecorderFragment.OnRecordingFinishedListener, WordResultFragment.OnNextWordListener,
        IntermediateFragment.SwitchOutIntermediateStageListener {


    private static final String TAG = "drive-quickstart";
    private static final int REQUEST_CODE_CAPTURE_IMAGE = 11;
    private static final int REQUEST_CODE_CREATOR = 12;
    private static final int REQUEST_CODE_RESOLUTION = 13;

    public static final String DRIVE_ROOT = "ahsounds";
//
//    public MediaPlayer[] drumPlayer = new MediaPlayer[3];
//    public MediaPlayer[] stringPlayer = new MediaPlayer[6];
//    public MediaPlayer firework = new MediaPlayer();
//    public MediaPlayer rain = new MediaPlayer();

    public int[] drumPlayer = new int[3];
    public int[] stringPlayer = new int[6];
    public int firework = 0;
    public int rain = 0;

    public SoundPool pool;
    public String[] words;
    public int wordIndex = 0;
    public int syllables[];
    public File basePath;
    public File baseFolder;

    public DriveId driveRootFolderId;
    public DriveId subjectFolderId;

    public String childFilename, parentFilename;
    public boolean isPlayerSet = false;

    GoogleApiClient googleApiClient;


    RecorderFragment recorderFragment;

    final ResultCallback<DriveFolder.DriveFolderResult> newFolderCallback = new ResultCallback<DriveFolder.DriveFolderResult>() {
        @Override
        public void onResult(DriveFolder.DriveFolderResult result) {
            if (!result.getStatus().isSuccess()) {
                Log.d("test","Error while trying to create the folder");
                return;
            }
            Log.d("test","Created a folder: " + result.getDriveFolder().getDriveId());
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        basePath = new File(Environment.getExternalStorageDirectory().getAbsolutePath()
                + File.separator + "ahsounds");
        if (!basePath.exists()) {
            basePath.mkdirs();
        }

        googleApiClient = new GoogleApiClient.Builder(this)
                .addApi(Drive.API)
                .addScope(Drive.SCOPE_FILE)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();


        setContentView(R.layout.activity_main);

        Util.requestPermission(this, Manifest.permission.RECORD_AUDIO);
        Util.requestPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        createMediaPlayers();

        words = getResources().getStringArray(R.array.wordlist);
        shuffleArray(words);
        countSyllables();


        NameFragment nameFragment = new NameFragment(); // use this
//        StringFragment stringFragment = new StringFragment();
//        WordProcessingFragment wordProcessingFragment = new WordProcessingFragment();
        ParentRatingFragment parentRatingFragment = new ParentRatingFragment();
//        DashboardFragment dashboardFragment = new DashboardFragment();

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

    void countSyllables() {
        syllables = new int[words.length];

        for (int i = 0; i < words.length; i++) {
            String w = words[i];
            syllables[i] = 1;
            for (int j = 0; j < w.length(); j++) {
                if (w.charAt(j) == '-' || w.charAt(j) == ' ') {
                    syllables[i]++;
                }
            }
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

        else if (requestCode == REQUEST_CODE_RESOLUTION) {
            Log.d("TAG","Request Code Resolution called");
            if(resultCode == RESULT_OK) {
                googleApiClient.connect();
            } else {
                Log.d("TAG","Request Code Resolution Failed");
            }
        }


    }


    @Override
    public void switchToStringMode() {
        IntermediateFragment fragment = new IntermediateFragment(1);
//        StringFragment stringFragment = new StringFragment(StringMode.PARENT_TOUCH_ALL);
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left);
        fragmentTransaction.replace(R.id.container_fragment, fragment)
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
        WordTrainFragment wordTrainFragment = new WordTrainFragment(mode);
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left);
        fragmentTransaction.replace(R.id.container_fragment, wordTrainFragment)
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void switchToStringWordTrainModeParam(StringMode mode) {
        WordTrainFragment wordTrainFragment = new WordTrainFragment(mode);
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left);
        fragmentTransaction.replace(R.id.container_fragment, wordTrainFragment)
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void switchToStageDrumMode(StringMode mode) {
        IntermediateFragment fragment = new IntermediateFragment(2);
//        DrumFragment drumFragment = new DrumFragment(mode);
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left);
        fragmentTransaction.replace(R.id.container_fragment, fragment)
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
        DrumTrainFragment drumTrainFragment = new DrumTrainFragment(mode);
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left);
        fragmentTransaction.replace(R.id.container_fragment, drumTrainFragment)
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void switchToDrumTrainModeParam(StringMode mode) {
        DrumTrainFragment drumTrainFragment = new DrumTrainFragment(mode);
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left);
        fragmentTransaction.replace(R.id.container_fragment, drumTrainFragment)
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void switchToRecorder() {
//        recorderFragment = new RecorderFragment();
        IntermediateFragment fragment = new IntermediateFragment(3);
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left);
        fragmentTransaction.replace(R.id.container_fragment, fragment)
                .addToBackStack(null)
                .commit();

    }

    @Override
    public void switchToWordResultMode() {
        WordProcessingFragment processingFragment = new WordProcessingFragment();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left);
        fragmentTransaction.replace(R.id.container_fragment, processingFragment)
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void switchToNextWord() {
        FragmentManager fm = getSupportFragmentManager();
        int count = fm.getBackStackEntryCount();
        Log.d("sid_stacks", String.valueOf(count));
        for(int i = 0; i < count-1; ++i) {
            fm.popBackStackImmediate();
        }
        Log.d("sid_stacks", "remaining: " + String.valueOf(fm.getBackStackEntryCount()));
    }

    @Override
    public void switchToNextParticipant() {
        FragmentManager fm = getSupportFragmentManager();
        int count = fm.getBackStackEntryCount();
        for(int i = 0; i < count; ++i) {
            fm.popBackStackImmediate();
        }
    }

    @Override
    public void OnIntermediateFinish(int stage) {
        final FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left);

        switch (stage) {
            case 1:
                ft.replace(R.id.container_fragment, new StringFragment(StringMode.PARENT_TOUCH_ALL));
                break;
            case 2:
                ft.replace(R.id.container_fragment, new DrumFragment(StringMode.PARENT_TOUCH_ALL));
                break;
            case 3:
                recorderFragment = new RecorderFragment();
                ft.replace(R.id.container_fragment, recorderFragment);
        }
        ft.commit();
    }




    // GOOGLE DRIVE API

    @Override
    protected void onStart() {
        super.onStart();
        googleApiClient.connect();
    }

    @Override
    public void onConnectionFailed(ConnectionResult result) {
        // Called whenever the API client fails to connect.
        Log.i("TAG", "GoogleApiClient connection failed: " + result.toString());
        if (!result.hasResolution()) {
            // show the localized error dialog.
            Log.d("TAG","No resolution for google api sign in");
            GoogleApiAvailability.getInstance().getErrorDialog(this, result.getErrorCode(), 0).show();
            return;
        }
        // The failure has a resolution. Resolve it.
        // Called typically when the app is not yet authorized, and an
        // authorization
        // dialog is displayed to the user.
        try {
            Log.d("TAG","got here");
            result.startResolutionForResult(this, REQUEST_CODE_RESOLUTION);
        } catch (IntentSender.SendIntentException e) {
            Log.e("TAG", "Exception while starting resolution activity", e);
        }
    }

    final ResultCallback<DriveApi.MetadataBufferResult> resultCallback = new ResultCallback<DriveApi.MetadataBufferResult>() {
        @Override
        public void onResult(@NonNull DriveApi.MetadataBufferResult metadataBufferResult) {
            MetadataBuffer buffer = metadataBufferResult.getMetadataBuffer();
            boolean exists = false;
            for (int i=0; i<buffer.getCount(); i++) {
                if (buffer.get(i).getTitle().equalsIgnoreCase(DRIVE_ROOT)) {
                    driveRootFolderId = buffer.get(i).getDriveId();
                    Log.d("drive debug",driveRootFolderId.toString());
                    exists = true;
                }
            }

            if (!exists) {
                Log.d("sid", "must create new folder");
                createNewFolder();
            } else {
                Log.d("trashed", String.valueOf(metadataBufferResult.getMetadataBuffer().get(0).isTrashed()));
                Log.d("sid","already exists some folder");

            }

            buffer.close();

            return;
        }
    };


    @Override
    public void onConnected(Bundle connectionHint) {
        Log.i(TAG, "API client connected.");
        Drive.DriveApi.getRootFolder(googleApiClient).listChildren(googleApiClient).setResultCallback(resultCallback);


    }

    @Override
    public void onConnectionSuspended(int cause) {
        Log.i(TAG, "GoogleApiClient connection suspended");
    }


    public void createNewFolder() {
        Log.d("sid","creating new folder");
        MetadataChangeSet changeSet = new MetadataChangeSet.Builder()
                .setTitle(DRIVE_ROOT).build();
        Drive.DriveApi.getRootFolder(googleApiClient).createFolder(
                googleApiClient, changeSet).setResultCallback(newFolderCallback);
    }

    final ResultCallback<DriveFolder.DriveFolderResult> subjectFolderCallback = new ResultCallback<DriveFolder.DriveFolderResult>() {
        @Override
        public void onResult(DriveFolder.DriveFolderResult result) {
            if (!result.getStatus().isSuccess()) {
                Log.d("test","Error while trying to create the folder");
                return;
            }
            Log.d("test","Created a folder: " + result.getDriveFolder().getDriveId());
            subjectFolderId = result.getDriveFolder().getDriveId();
        }
    };


    public void createSubjectFolder(String folderName) {
        DriveFolder folder = Drive.DriveApi.getFolder(googleApiClient, driveRootFolderId);
        MetadataChangeSet changeSet = new MetadataChangeSet.Builder()
                .setTitle(folderName).build();
        folder.createFolder(googleApiClient, changeSet).setResultCallback(subjectFolderCallback);
    }


    public void uploadChildFilesToSubjectFolder(String filename) {
        Log.d("child", "initiating upload code");
        childFilename = filename;
        Drive.DriveApi.newDriveContents(googleApiClient)
                .setResultCallback(childDriveContentsCallback);
    }

    public void uploadParentFilesToSubjectFolder(String filename) {
        Log.d("parent", "initiating upload code");
        parentFilename = filename;
        Drive.DriveApi.newDriveContents(googleApiClient)
                .setResultCallback(parentDriveContentsCallback);
    }




    final private ResultCallback<DriveApi.DriveContentsResult> childDriveContentsCallback = new
            ResultCallback<DriveApi.DriveContentsResult>() {
                @Override
                public void onResult(DriveApi.DriveContentsResult result) {
                    if (!result.getStatus().isSuccess()) {
                        Log.i(TAG, "Error while trying to create new file contents");
                        return;
                    }
                    final DriveContents driveContents = result.getDriveContents();

                    // Perform I/O off the UI thread.
                    new Thread() {
                        @Override
                        public void run() {
                            // write content to DriveContents
                            OutputStream outputStream = driveContents.getOutputStream();

                            Writer writer = new OutputStreamWriter(outputStream);
//                            Writer writer = new PrintWriter(outputStream);
                            try {
//                                writer.write("Hello World!");
//                                writer.close();
                                File f = new File(baseFolder.toString() + File.separator + childFilename);
                                InputStream in = new FileInputStream(f);
                                byte[] buf = new byte[1024];
                                int len;
                                while ((len = in.read(buf)) > 0) {
                                    outputStream.write(buf, 0, len);
                                }
                                in.close();

                            } catch (IOException e) {
                                Log.d("sid", "something went wrong");
                                Log.e(TAG, e.getMessage());
                            }


                            MetadataChangeSet changeSet = new MetadataChangeSet.Builder()
                                    .setTitle(childFilename)
                                    .setMimeType("audio/wav")
                                    .setStarred(true).build();

                            // create a file on root folder
                            Drive.DriveApi.getFolder(googleApiClient, subjectFolderId)
                                    .createFile(googleApiClient, changeSet, driveContents)
                                    .setResultCallback(fileCallback);
                        }
                    }.start();
                }
            };

    final private ResultCallback<DriveApi.DriveContentsResult> parentDriveContentsCallback = new
            ResultCallback<DriveApi.DriveContentsResult>() {
                @Override
                public void onResult(DriveApi.DriveContentsResult result) {
                    if (!result.getStatus().isSuccess()) {
                        Log.i(TAG, "Error while trying to create new file contents");
                        return;
                    }
                    final DriveContents driveContents = result.getDriveContents();

                    // Perform I/O off the UI thread.
                    new Thread() {
                        @Override
                        public void run() {
                            // write content to DriveContents
                            OutputStream outputStream = driveContents.getOutputStream();

                            Writer writer = new OutputStreamWriter(outputStream);
//                            Writer writer = new PrintWriter(outputStream);
                            try {
//                                writer.write("Hello World!");
//                                writer.close();
                                File f = new File(baseFolder.toString() + File.separator + parentFilename);
                                InputStream in = new FileInputStream(f);
                                byte[] buf = new byte[1024];
                                int len;
                                while ((len = in.read(buf)) > 0) {
                                    outputStream.write(buf, 0, len);
                                }
                                in.close();

                            } catch (IOException e) {
                                Log.d("sid", "something went wrong");
                                Log.e(TAG, e.getMessage());
                            }


                            MetadataChangeSet changeSet = new MetadataChangeSet.Builder()
                                    .setTitle(parentFilename)
                                    .setMimeType("audio/wav")
                                    .setStarred(true).build();

                            // create a file on root folder
                            Drive.DriveApi.getFolder(googleApiClient, subjectFolderId)
                                    .createFile(googleApiClient, changeSet, driveContents)
                                    .setResultCallback(fileCallback);
                        }
                    }.start();
                }
            };

    final private ResultCallback<DriveFolder.DriveFileResult> fileCallback = new
            ResultCallback<DriveFolder.DriveFileResult>() {
                @Override
                public void onResult(DriveFolder.DriveFileResult result) {
                    if (!result.getStatus().isSuccess()) {
                        Log.d(TAG, "Error while trying to create the file");
                        return;
                    }
                    Log.i(TAG, "Created a file with content: " + result.getDriveFile().getDriveId());
                }
            };

    void releaseMediaPlayers() {
        for (int i=0; i<6; i++) {
//            stringPlayer[i].release();
            pool.unload(stringPlayer[i]);
        }

        for (int i=0; i<3; i++) {
//            drumPlayer[i].release();
            pool.unload(drumPlayer[i]);
        }

        pool.unload(rain);
        pool.unload(firework);
//        rain.release();
//        firework.release();

        isPlayerSet = false;
    }

    void createMediaPlayers() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            pool = (new SoundPool.Builder()).setMaxStreams(20).build();
        } else {
            pool = new SoundPool(20, AudioManager.STREAM_MUSIC,0);
        }

        drumPlayer[0] = pool.load(this, R.raw.d1,0);
        drumPlayer[1] = pool.load(this, R.raw.d2,0);
        drumPlayer[2] = pool.load(this, R.raw.d3,0);

        stringPlayer[0] = pool.load(this, R.raw.t0,0);
        stringPlayer[1] = pool.load(this, R.raw.t1,0);
        stringPlayer[2] = pool.load(this, R.raw.t2,0);
        stringPlayer[3] = pool.load(this, R.raw.t3,0);
        stringPlayer[4] = pool.load(this, R.raw.a,0);
        stringPlayer[5] = pool.load(this, R.raw.b,0);

        rain = pool.load(this, R.raw.rain,0);
        firework = pool.load(this, R.raw.firework,0);



//        drumPlayer[0] = MediaPlayer.create(this, R.raw.d1);
//        drumPlayer[1] = MediaPlayer.create(this, R.raw.d2);
//        drumPlayer[2] = MediaPlayer.create(this, R.raw.d3);
//
//        stringPlayer[0] = MediaPlayer.create(this, R.raw.t0);
//        stringPlayer[1] = MediaPlayer.create(this, R.raw.t1);
//        stringPlayer[2] = MediaPlayer.create(this, R.raw.t2);
//        stringPlayer[3] = MediaPlayer.create(this, R.raw.t3);
//        stringPlayer[4] = MediaPlayer.create(this, R.raw.a);
//        stringPlayer[5] = MediaPlayer.create(this, R.raw.b);
//
//        rain = MediaPlayer.create(this, R.raw.rain);
//        firework = MediaPlayer.create(this, R.raw.firework);
        isPlayerSet = true;
    }



    public void poolPlay(int id) {
        pool.play(id,1,1,0,0,1);
    }
}

