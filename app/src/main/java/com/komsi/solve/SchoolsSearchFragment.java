package com.komsi.solve;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.komsi.solve.Adapter.NavigationAdapter;
import com.komsi.solve.Adapter.SchoolAdapter;
import com.komsi.solve.Api.RetrofitClient;
import com.komsi.solve.Model.ResponseQuestion;
import com.komsi.solve.Model.ResponseSchools;
import com.komsi.solve.Model.SchoolsModel;
import com.komsi.solve.Model.UserModel;
import com.komsi.solve.Storage.SharedPrefManager;

import java.util.ArrayList;

public class SchoolsSearchFragment extends BottomSheetDialogFragment implements View.OnClickListener {
    RecyclerView schoolsRV;
    SchoolAdapter adapter;
    ArrayList<SchoolsModel> schoolsModels;
    String code;
    TextInputLayout layoutSearch;
    TextInputEditText inputSearch;
    Context mContext;
    ProgressDialog loading;

    public SchoolsSearchFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View fragmentView = inflater.inflate(R.layout.fragment_schools_search, container, false);

        schoolsRV = fragmentView.findViewById(R.id.schoolsRV);

        loadSchools();

        mContext = getActivity().getWindow().getContext();

        layoutSearch = fragmentView.findViewById(R.id.layoutSearch);
        inputSearch = fragmentView.findViewById(R.id.inputSearch);

        inputSearch.setFilters(new InputFilter[]{new InputFilter.AllCaps()});

        inputSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    loadSchoolsTerm();
                    return true;
                }
                return false;
            }
        });

        layoutSearch.setEndIconOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadSchoolsTerm();
            }
        });

        inputSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String code = inputSearch.getText().toString();

                if (code.length() > 0) {
                    layoutSearch.setError(null);
                }
            }
        });

        return fragmentView;
    }

    public void loadSchools() {
        loading = ProgressDialog.show(getActivity(), null, getString(R.string.please_wait), true, false);

        UserModel user = SharedPrefManager.getInstance(getActivity()).getUser();

        String token = "Bearer " + user.getToken();
        Call<ResponseSchools> call = RetrofitClient.getInstance().getApi().schools("application/json", token);
        call.enqueue(new Callback<ResponseSchools>() {
            @Override
            public void onResponse(Call<ResponseSchools> call, final Response<ResponseSchools> response) {
                ResponseSchools questionResponse = response.body();
                loading.dismiss();
                if (response.isSuccessful()) {
                    schoolsModels = questionResponse.getResult();
                    StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(1, LinearLayoutManager.VERTICAL);
                    adapter = new SchoolAdapter(schoolsModels, getActivity(), SchoolsSearchFragment.this, null);
                    schoolsRV.setLayoutManager(new LinearLayoutManager(getActivity()));
                    schoolsRV.setLayoutManager(staggeredGridLayoutManager);
                    schoolsRV.setAdapter(adapter);
                } else {
                    Log.i("debug", "onResponse : FAILED");
                    Toast.makeText(getActivity(), R.string.something_wrong, Toast.LENGTH_LONG).show();

                }
            }

            @Override
            public void onFailure(Call<ResponseSchools> call, Throwable t) {
                loading.dismiss();
                Log.i("debug", "onResponse : FAILED");
                Toast.makeText(getActivity(), t.toString() + R.string.something_wrong, Toast.LENGTH_LONG).show();
            }
        });
    }

    public void loadSchoolsTerm() {
        inputSearch.clearFocus();
        InputMethodManager in = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
        in.hideSoftInputFromWindow(inputSearch.getWindowToken(), 0);
        loading = ProgressDialog.show(mContext, null, getString(R.string.please_wait), true, false);
        String code = inputSearch.getText().toString().trim();

        if (code.isEmpty()) {
            loading.dismiss();
            layoutSearch.setError("Kode Quiz Harus Diisi");
            inputSearch.requestFocus();
            return;
        }
        UserModel user = SharedPrefManager.getInstance(getActivity()).getUser();

        String token = "Bearer " + user.getToken();
        Call<ResponseSchools> call = RetrofitClient.getInstance().getApi().schools2("application/json", token, code);
        call.enqueue(new Callback<ResponseSchools>() {
            @Override
            public void onResponse(Call<ResponseSchools> call, final Response<ResponseSchools> response) {
                ResponseSchools questionResponse = response.body();
                loading.dismiss();
                if (response.isSuccessful()) {
                    schoolsModels = questionResponse.getResult();
                    StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(1, LinearLayoutManager.VERTICAL);
                    adapter = new SchoolAdapter(schoolsModels, getActivity(),SchoolsSearchFragment.this, null);
                    schoolsRV.setLayoutManager(new LinearLayoutManager(getActivity()));
                    schoolsRV.setLayoutManager(staggeredGridLayoutManager);
                    schoolsRV.setAdapter(adapter);
                } else {
                    Log.i("debug", "onResponse : FAILED");
                    Toast.makeText(getActivity(), R.string.something_wrong, Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseSchools> call, Throwable t) {
                loading.dismiss();
                Log.i("debug", "onResponse : FAILED");
                Toast.makeText(getActivity(), t.toString() + R.string.something_wrong, Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void onCancel(@NonNull DialogInterface dialog) {
        super.onCancel(dialog);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {

        }
    }


}
