package ankr.autismhearing;

import android.content.Context;
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
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class ViewRecordingsAdapter extends RecyclerView.Adapter<ViewRecordingsAdapter.ViewHolder> {

    private final List<File> files;
    private File basePath;
    private final MainActivity activity;

    public ViewRecordingsAdapter(Context context, List<File> files) {
        this.files = files;
        activity = (MainActivity)context;
        basePath = activity.baseFolder;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_recording, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Date date = new Date(files.get(position).lastModified());
        Format formatter = new SimpleDateFormat("EEEE, dd MMMM yyyy");

        String display = " @ " + formatter.format(date);
        holder.textView.setText(files.get(position).getName());
        holder.textViewDate.setText(display);
    }

    @Override
    public int getItemCount() {
        return files.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView textView;
        public TextView textViewDate;
        public ViewHolder(View itemView) {
            super(itemView);
            textView = (TextView)itemView.findViewById(R.id.textview_recording_item);
            textViewDate = (TextView)itemView.findViewById(R.id.textview_recording_item_date);


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
