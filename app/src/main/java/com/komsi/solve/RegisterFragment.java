package com.komsi.solve;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.komsi.solve.Adapter.SchoolAdapter;
import com.komsi.solve.Api.RetrofitClient;
import com.komsi.solve.Model.PageViewModel;
import com.komsi.solve.Model.QuestionModel;
import com.komsi.solve.Model.ResponseSignUp;
import com.komsi.solve.Model.SchoolsModel;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Iterator;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProviders;
import in.galaxyofandroid.spinerdialog.OnSpinerItemClick;
import in.galaxyofandroid.spinerdialog.SpinnerDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterFragment extends Fragment  {

    private static final String TAG = "Register";
    MaterialButton btnRegister;
    SpinnerDialog spinnerDialog;
    ArrayList<SchoolsModel> items = new ArrayList<>();
    public TextView txtSchools;
    private EditText etUsername, etEmail, etName, etPassword, etConfirmPassword;
    Context mContext;
    ProgressDialog loading;


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
        etUsername = view.findViewById(R.id.etUsername);
        etEmail = view.findViewById(R.id.etEmail);
        etName = view.findViewById(R.id.etName);
        etPassword = view.findViewById(R.id.etPassword);
        etConfirmPassword = view.findViewById(R.id.etConfirmPassword);

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

    public void setTextSch(){
        final SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String schoolName = sharedPrefs.getString("schoolName", "Asal Sekolah");
        txtSchools.setText(schoolName);


    }
    public void clear(){
        final SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        final Gson gson = new Gson();
        final SharedPreferences.Editor editorList = sharedPrefs.edit();
        editorList.clear();
        editorList.commit();
    }
    public void register(){
        loading = ProgressDialog.show(getActivity(), null, "Please wait...", true, false);
        final SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(getActivity());

        String username = etUsername.getText().toString().trim();
        final String email = etEmail.getText().toString().trim();
        final String name = etName.getText().toString().trim();
        String password = etPassword.getText().toString().trim();
//        String confirmpassword = etConfirmPassword.getText().toString().trim();
        String accept = "application/json";
        String status = "Active";
        String idSchool = sharedPrefs.getString("schoolId", "0001");

        if (username.isEmpty()) {
            loading.dismiss();
            etUsername.setError("Username is required");
            etUsername.requestFocus();
            return;
        }

        if (username.length() < 4) {
            loading.dismiss();
            etUsername.setError("Username should be at least 4 characters long");
            etUsername.requestFocus();
            return;
        }

        if (email.isEmpty()) {
            loading.dismiss();
            etEmail.setError("Email is required");
            etEmail.requestFocus();
            return;
        }
        if (name.isEmpty()) {
            loading.dismiss();
            etName.setError("Name is required");
            etName.requestFocus();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            loading.dismiss();
            etEmail.setError("Enter a valid Email");
            etEmail.requestFocus();
            return;
        }


        if (password.isEmpty()) {
            loading.dismiss();
            etPassword.setError("Password is required");
            etPassword.requestFocus();
            return;
        }

        if (password.length() < 8) {
            loading.dismiss();
            etPassword.setError("Password should be at least 8 characters long");
            etPassword.requestFocus();
            return;
        }

        /*if (confirmpassword.isEmpty()) {
            loading.dismiss();
            etConfirmPassword.setError("Confirm Password is required");
            etConfirmPassword.requestFocus();
            return;
        }

        if (confirmpassword.length() < 8) {
            loading.dismiss();
            etConfirmPassword.setError("Password should be at least 8 characters long");
            etConfirmPassword.requestFocus();
            return;
        }

        if (!password.equals(confirmpassword)) {
            loading.dismiss();
            etConfirmPassword.setError("Password not matching");
            etConfirmPassword.requestFocus();
            return;
        }*/
        Call<ResponseSignUp> call = RetrofitClient
                .getInstance()
                .getApi()
                .registerUser(accept, username, email, name, password, idSchool, idSchool);

        call.enqueue(new Callback<ResponseSignUp>() {
            @Override
            public void onResponse(Call<ResponseSignUp> call, Response<ResponseSignUp> response) {
                ResponseSignUp signUpResponse = response.body();
                if (response.isSuccessful()){
                    if (signUpResponse.getStatus().equals("success")) {
                        Log.i("debug", "onResponse: SUCCESS");
                        loading.dismiss();
                        Toast.makeText(getActivity(), "Registration Success!", Toast.LENGTH_LONG).show();
                       // startActivity(new Intent(mContext, Login.class));
                        clear();
                    }
                } else {
                    loading.dismiss();
                    try {
                        String errorBody = response.errorBody().string();
                        JSONObject jsonObject = new JSONObject(errorBody.trim());
                        jsonObject = jsonObject.getJSONObject("errors");
                        Iterator<String> keys = jsonObject.keys();
                        StringBuilder errors = new StringBuilder();
                        String separator = "";
                        while (keys.hasNext()) {
                            String key = keys.next();
                            JSONArray arr = jsonObject.getJSONArray(key);
                            for (int i = 0; i < arr.length(); i++) {
                                errors.append(separator).append(key).append(" : ").append(arr.getString(i));
                                separator = "\n";
                            }
                        }
                        Toast.makeText(getActivity(), errors.toString(), Toast.LENGTH_LONG).show();
                    } catch (Exception e) {
                        Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseSignUp> call, Throwable t) {
                Log.e("debug", "onFailure: ERROR > " + t.getMessage());
                loading.dismiss();
                Toast.makeText(getActivity(), "Something wrong. Please try again later.", Toast.LENGTH_LONG).show();
            }
        });
    }



}
