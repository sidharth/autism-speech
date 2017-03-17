package ankr.autismhearing;


import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.io.File;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class ViewRecordingsFragment extends Fragment {


    public ViewRecordingsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_view_recordings, container, false);

        File basePath = new File(Environment.getExternalStorageDirectory().getAbsolutePath()
                + File.separator + "ahrecordings");

        File[] files = basePath.listFiles();

        RecyclerView rv = (RecyclerView)v.findViewById(R.id.recycler_view_recordings);
        ViewRecordingsAdapter adapter = new ViewRecordingsAdapter(files);

        rv.setAdapter(adapter);

        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        rv.setLayoutManager(llm);

        return v;
    }

}
