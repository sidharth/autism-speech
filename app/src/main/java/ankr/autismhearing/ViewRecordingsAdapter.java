package ankr.autismhearing;

import android.media.MediaPlayer;
import android.os.Environment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.io.File;
import java.io.IOException;

public class ViewRecordingsAdapter extends RecyclerView.Adapter<ViewRecordingsAdapter.ViewHolder> {

    private File[] files;
    private File basePath;

    public ViewRecordingsAdapter(File[] files) {
        Log.d("file",String.valueOf(files.length));
        this.files = files;
        basePath = new File(Environment.getExternalStorageDirectory().getAbsolutePath()
                + File.separator + "ahrecordings");
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_recording, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.textView.setText(files[position].getName() );
    }

    @Override
    public int getItemCount() {
        return files.length;
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView textView;
        public ViewHolder(View itemView) {
            super(itemView);
            textView = (TextView)itemView.findViewById(R.id.textview_recording_item);

            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    MediaPlayer mp = new MediaPlayer();
                    try {
                        mp.setDataSource(basePath.getPath() + File.separator + ((TextView)v).getText().toString() );
                        mp.prepare();
                        mp.start();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }

}
