package ankr.autismhearing;


import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 */
public class IntermediateFragment extends Fragment {
    int stage;
    Handler handler = new Handler();
    Runnable runnable;

    TextView title, subtitle;

    SwitchOutIntermediateStageListener callback;

    public IntermediateFragment() {
        // Required empty public constructor
    }

    public IntermediateFragment(int stage) {
        this.stage = stage;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_intermediate, container, false);
        final MainActivity activity = (MainActivity)getActivity();
        callback = activity;

        title = (TextView)v.findViewById(R.id.intermediate_title);
        subtitle = (TextView)v.findViewById(R.id.intermediate_subtitle);

        title.setText("STAGE " + String.valueOf(stage));

        switch (stage) {
            case 1:
                subtitle.setText("Let's play the guitar and sing!");
                break;
            case 2:
                subtitle.setText("Now, let's play the drums!");
                break;
            case 3:
                subtitle.setText("Now let's record!");
        }

        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                callback.OnIntermediateFinish(stage);
            }
        };

        handler.postDelayed(runnable, 3000);

        return v;
    }

    public interface SwitchOutIntermediateStageListener {
        void OnIntermediateFinish(int stage);
    }

    @Override
    public void onDestroy() {
        handler.removeCallbacks(runnable);
        super.onDestroy();
    }

}
