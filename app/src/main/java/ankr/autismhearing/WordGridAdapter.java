package ankr.autismhearing;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by home on 27/05/17.
 */
public class WordGridAdapter extends BaseAdapter {

    private final Context mContext;
    private final MainActivity mActivity;
    private final List<String> words;
    public WordGridAdapter(Context context, List<String> words) {
        mContext = context;
        mActivity = (MainActivity)mContext;
        this.words = words;
    }

    @Override
    public int getCount() {
        return words.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int pos, View view, ViewGroup viewGroup) {
        View v = view;
        if (v == null) {
            LayoutInflater li = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = li.inflate(R.layout.gridview_cell, null);
            TextView tv = (TextView)v.findViewById(R.id.gridcell_textview);
            tv.setText(words.get(pos));

            tv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                        ViewWordRecordingFragment fragment = new ViewWordRecordingFragment();
                        Bundle args = new Bundle();
                        args.putString("word", ((TextView)view).getText().toString());
                        fragment.setArguments(args);
                        FragmentTransaction fragmentTransaction = mActivity.getSupportFragmentManager().beginTransaction();
                        fragmentTransaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left);
                        fragmentTransaction.replace(R.id.container_fragment, fragment)
                                .addToBackStack(null)
                                .commit();
                }
            });


        }
        return v;
    }
}
