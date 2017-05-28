package ankr.autismhearing;


import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class ViewWordRecordingFragment extends Fragment {

    TextView textViewWord;

    public ViewWordRecordingFragment() {
        // Required empty public constructor

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_view_word_recording, container, false);
        final MainActivity activity = (MainActivity)getActivity();
        final String word = getArguments().getString("word");
        textViewWord = (TextView)v.findViewById(R.id.textview_view_word);

        textViewWord.setText(word);

        File[] files = activity.baseFolder.listFiles();
        List<File> filesWithWord = new ArrayList<>();

        for (int i=0; i<files.length; i++) {
            if (files[i].getName().contains(word) && !files[i].getName().contains("parent")) {
                filesWithWord.add(files[i]);
            }
        }

        RecyclerView rv = (RecyclerView)v.findViewById(R.id.recycler_view_recordings);
        ViewRecordingsAdapter adapter = new ViewRecordingsAdapter(getActivity(), filesWithWord);

        rv.setAdapter(adapter);

        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        rv.setLayoutManager(llm);

        return v;
    }

}
