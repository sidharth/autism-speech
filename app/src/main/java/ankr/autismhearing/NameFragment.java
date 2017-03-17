package ankr.autismhearing;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;


/**
 * A simple {@link Fragment} subclass.
 */
public class NameFragment extends Fragment {

    OnNameSubmitListener callback;
    Button buttonSubmit;
    EditText etName;

    public NameFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_name, container, false);
        buttonSubmit = (Button)v.findViewById(R.id.button_submit_name);
        etName = (EditText)v.findViewById(R.id.edittext_name);

        callback = (OnNameSubmitListener)getActivity();

        buttonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View view = getActivity().getCurrentFocus();
                Context context = getActivity();
                SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putString("name", etName.getText().toString());
                editor.commit();
                if (view != null) {
                    InputMethodManager imm = (InputMethodManager)getActivity()
                            .getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                }
                callback.switchToStringMode();
            }
        });
        return v;
    }

    public interface OnNameSubmitListener {
        void switchToStringMode();
    }

}
