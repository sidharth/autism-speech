package ankr.autismhearing;


import android.content.Context;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.File;

import cafe.adriel.androidaudiorecorder.AndroidAudioRecorder;
import cafe.adriel.androidaudiorecorder.model.AudioChannel;
import cafe.adriel.androidaudiorecorder.model.AudioSampleRate;
import cafe.adriel.androidaudiorecorder.model.AudioSource;
import mehdi.sakout.fancybuttons.FancyButton;


/**
 * A simple {@link Fragment} subclass.
 */
public class NameFragment extends Fragment {

    OnNameSubmitListener callback;
    Button buttonSubmit;

    FancyButton buttonContinue;

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
        buttonContinue = (FancyButton) v.findViewById(R.id.button_continue_as_prev);

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

                    createNewDbTable();

//                    callback.switchToStringMode();
                    switchToDashboardMode();
                }
            }
        });


        buttonContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String name, gender, birthday, language;
                SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
                name = sharedPref.getString("name", "");
                gender = sharedPref.getString("gender","");
                birthday = sharedPref.getString("birthday","");
                language = sharedPref.getString("language","");

                String folderName = name + "_" +
                        gender + "_" +
                        birthday + "_" +
                        language;

                Toast.makeText(getActivity(), folderName, Toast.LENGTH_SHORT)
                        .show();
                File folder = new File(activity.basePath + File.separator + folderName);
                if (!folder.exists()) {
                    folder.mkdirs();
                }
                activity.baseFolder = folder;
                activity.createSubjectFolder(folderName);

                View view = getActivity().getCurrentFocus();
                if (view != null) {
                    InputMethodManager imm = (InputMethodManager) getActivity()
                            .getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                }


//                callback.switchToStringMode();
                switchToDashboardMode();
            }
        });

        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);


        return v;
    }

    private void createNewDbTable() {
        try {
            SQLiteDatabase database = new WordDbHelper(getContext()).getWritableDatabase();
            database.delete(WordContract.WordEntry.TABLE_NAME, null, null);
        } catch (Exception e) {
            Log.d("sid", "wasn't able to create new table");
        }
    }



    public interface OnNameSubmitListener {
        void switchToStringMode();
    }

    private void switchToDashboardMode() {
        DashboardFragment dashboardFragment = new DashboardFragment();
        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left);
        fragmentTransaction.replace(R.id.container_fragment, dashboardFragment)
                .addToBackStack(null)
                .commit();
    }

}
