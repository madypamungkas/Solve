package com.komsi.solve;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.komsi.solve.Model.PageViewModel;
import com.komsi.solve.Model.QuestionModel;
import com.komsi.solve.Model.SchoolsModel;

import java.lang.reflect.Type;
import java.util.ArrayList;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProviders;
import in.galaxyofandroid.spinerdialog.OnSpinerItemClick;
import in.galaxyofandroid.spinerdialog.SpinnerDialog;

public class RegisterFragment extends Fragment implements View.OnClickListener {

    private static final String TAG = "Register";
    MaterialButton btnRegister;
    SpinnerDialog spinnerDialog;
    ArrayList<SchoolsModel> items = new ArrayList<>();
    public TextView txtSchools;


    private PageViewModel pageViewModel;

    public RegisterFragment() {

    }

    /**
     * @return A new instance of fragment SpeedDialFragment.
     */
    public static RegisterFragment newInstance() {
        return new RegisterFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pageViewModel = ViewModelProviders.of(this).get(PageViewModel.class);
        pageViewModel.setIndex(TAG);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_register, container, false);

        final SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        final Gson gson = new Gson();
        final SharedPreferences.Editor editorList = sharedPrefs.edit();

        String schoolName = sharedPrefs.getString("schoolName", "Asal Sekolah");
        String schoolId = sharedPrefs.getString("schoolId", "0");

        btnRegister = view.findViewById(R.id.btnRegister);
        txtSchools = view.findViewById(R.id.txtSchools);
        txtSchools.setText(schoolName);


        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              register();
            }
        });


        txtSchools.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                SchoolsSearchFragment fragment = new SchoolsSearchFragment();
                fragment.setArguments(bundle);
                fragment.show(((FragmentActivity) getActivity()).getSupportFragmentManager(), TAG);
            }
        });

       /* ArrayList<String> names = android:entries="@array/labels";

        spinnerDialog=new SpinnerDialog(getActivity(),items ,"Select or Search City","Close Button Text");// With No Animation
        spinnerDialog=new SpinnerDialog(getActivity(),,"Select or Search City",R.style.DialogAnimations_SmileWindow,"Close Button Text");// With 	Animation

        spinnerDialog.setCancellable(true); // for cancellable
        spinnerDialog.setShowKeyboard(false);// for open keyboard by default


        spinnerDialog.bindOnSpinerListener(new OnSpinerItemClick() {
            @Override
            public void onClick(String item, int position) {
                Toast.makeText(getActivity(), item + "  " + position+"", Toast.LENGTH_SHORT).show();
                selectedItems.setText(item + " Position: " + position);
            }
        });
        view.findViewById(R.id.show).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                spinnerDialog.showSpinerDialog();
            }
        });*/


        return view;
    }
    public void register(){
        final SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        final Gson gson = new Gson();
        final SharedPreferences.Editor editorList = sharedPrefs.edit();
        editorList.clear();
        editorList.commit();
    }

    @Override
    public void onClick(View view) {


    }
}
