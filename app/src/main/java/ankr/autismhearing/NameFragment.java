package ankr.autismhearing;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.File;


/**
 * A simple {@link Fragment} subclass.
 */
public class NameFragment extends Fragment {

    OnNameSubmitListener callback;
    Button buttonSubmit;
    EditText etName, etGender, etBirthday, etNativeLang;
    MainActivity activity;

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
        etGender = (EditText)v.findViewById(R.id.edittext_gender);
        etBirthday = (EditText)v.findViewById(R.id.edittext_birthday);
        etNativeLang = (EditText)v.findViewById(R.id.edittext_nativelang);

        activity = (MainActivity)getActivity();
        callback = (OnNameSubmitListener)getActivity();

        buttonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (etName.getText().toString().length()==0 ||
                        etGender.getText().toString().length() == 0 ||
                        etBirthday.getText().toString().length() == 0 ||
                        etNativeLang.getText().toString().length() == 0) {
                    Toast.makeText(getActivity(), "Fill in all the fields",Toast.LENGTH_SHORT)
                            .show();
                }
                else {
                    String modDob = etBirthday.getText().toString();
                    modDob = modDob.replaceAll("[/]","-");
                    etBirthday.setText(modDob);

                    String folderName = etName.getText().toString() + "_" +
                            etGender.getText().toString() + "_" +
                            etBirthday.getText().toString() + "_" +
                            etNativeLang.getText().toString();
                    Log.d("foldername", folderName);
                    File folder = new File(activity.basePath + File.separator + folderName);
                    if (!folder.exists()) {
                        folder.mkdirs();
                    }
                    activity.baseFolder = folder;
                    activity.createSubjectFolder(folderName);

                    View view = getActivity().getCurrentFocus();
                    SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPref.edit();
                    editor.putString("name", etName.getText().toString());
                    editor.putString("gender", etGender.getText().toString());
                    editor.putString("birthday", etBirthday.getText().toString());
                    editor.putString("language", etNativeLang.getText().toString());
                    editor.commit();
                    if (view != null) {
                        InputMethodManager imm = (InputMethodManager) getActivity()
                                .getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                    }
                    callback.switchToStringMode();
                }
            }
        });
        return v;
    }

    public interface OnNameSubmitListener {
        void switchToStringMode();
    }

}
