package ankr.autismhearing;

import android.content.Context;
import android.content.SharedPreferences;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.github.piasy.rxandroidaudio.AudioRecorder;

import java.io.File;


/**
 * A simple {@link Fragment} subclass.
 *
 */
public class RecordFragment extends Fragment {

    Button record;
    TextView textViewTime;
    AudioRecorder mAudioRecorder;
    File mAudioFile;

    int sseconds;
    boolean running;


    public RecordFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_record, container, false);

        record = (Button)v.findViewById(R.id.button_record);
        textViewTime = (TextView)v.findViewById(R.id.textview_time);


        mAudioRecorder = AudioRecorder.getInstance();

        SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
        String pname = sharedPref.getString("name", "none");
        File basePath = new File(Environment.getExternalStorageDirectory().getAbsolutePath()
                + File.separator + "ahrecordings");

        if (!basePath.exists()) {
            basePath.mkdirs();
        }

        mAudioFile = new File(basePath.getPath() + File.separator + System.nanoTime() + "_" + pname + ".m4a");
        mAudioRecorder.prepareRecord(MediaRecorder.AudioSource.MIC,
                MediaRecorder.OutputFormat.MPEG_4, MediaRecorder.AudioEncoder.AAC,
                mAudioFile);

        record.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("sid", "record clicked");
                if (record.getText().toString().equalsIgnoreCase("record")) {
                    record.setText("STOP");
                    mAudioRecorder.startRecord();
                    startTimer();
                }
                else {
                    mAudioRecorder.stopRecord();
                    stopTimer();
                    record.setText("RECORD");
                }
            }
        });
        runTimer();
        return v;
    }

    public void startTimer(){
        running = true;
    }

    public void stopTimer(){
        running = false;
    }

    public void resetTimer(){
        running = true;
        sseconds = 0;
    }

    public void runTimer(){
        final Handler handler = new Handler();
        handler.post(new Runnable() {
            @Override
            public void run() {
//                int hours = sseconds / 3600;
//                int minutes = (sseconds % 3600) / 60;
//                int sec = sseconds % 60;
                int sec = sseconds / 1000;
                int msec = (sseconds / 10) % 100;
                String time = String.format("%02d:%02d", sec, msec);
                textViewTime.setText(time);
                if(running) {
                    sseconds += 100;
                    // update 10 ms
                }
                handler.postDelayed(this, 100);
            }
            // every 100 millisec
            // update 100 millisec

        });

    }

}
