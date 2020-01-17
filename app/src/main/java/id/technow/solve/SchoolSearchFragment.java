package id.technow.solve;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import id.technow.solve.Adapter.SchoolAdapter;
import id.technow.solve.Api.RetrofitClient;
import id.technow.solve.Model.ResponseSchools;
import id.technow.solve.Model.SchoolsModel;
import id.technow.solve.Model.UserModel;

import id.technow.solve.R;

import id.technow.solve.Storage.SharedPrefManager;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SchoolSearchFragment extends BottomSheetDialogFragment {
    RecyclerView schoolsRV;
    SchoolAdapter adapter;
    ArrayList<SchoolsModel> schoolsModels;
    String code;
    TextInputLayout layoutSearch;
    TextInputEditText inputSearch;
    Context mContext;
    private BottomSheetBehavior mBottomSheetBehavior;

    @Override
    public void onCancel(DialogInterface dialog) {
        super.onCancel(dialog);
        //handleUserExit();
    }

    @SuppressLint("RestrictedApi")
    @Override
    public void setupDialog(Dialog dialog, int style) {
        super.setupDialog(dialog, style);

        View fragmentView = LayoutInflater.from(getContext()).inflate(R.layout.fragment_schools_search, null);
        dialog.setContentView(fragmentView);

        mContext = getActivity();

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

        CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams) ((View) fragmentView.getParent()).getLayoutParams();
        CoordinatorLayout.Behavior behavior = params.getBehavior();

        if (behavior != null && behavior instanceof BottomSheetBehavior) {
            mBottomSheetBehavior = (BottomSheetBehavior) behavior;
            ((BottomSheetBehavior) behavior).setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
                @Override
                public void onStateChanged(@NonNull View bottomSheet, int newState) {
                    if (newState == BottomSheetBehavior.STATE_HIDDEN) {
                        dismiss();
                    }
                }

                @Override
                public void onSlide(@NonNull View bottomSheet, float slideOffset) {
                }
            });
        }
    }

    public void hideState() {
        mBottomSheetBehavior.setHideable(true);
        mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
    }

    private void loadSchools() {
        UserModel user = SharedPrefManager.getInstance(getActivity()).getUser();

        String token = "Bearer " + user.getToken();
        Call<ResponseSchools> call = RetrofitClient.getInstance().getApi().schools("application/json", token);
        call.enqueue(new Callback<ResponseSchools>() {
            @Override
            public void onResponse(Call<ResponseSchools> call, final Response<ResponseSchools> response) {
                ResponseSchools responseSchools = response.body();
                if (response.isSuccessful()) {
                    schoolsModels = responseSchools.getResult();
                    StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(1, LinearLayoutManager.VERTICAL);
                    adapter = new SchoolAdapter(schoolsModels, getActivity(), SchoolSearchFragment.this);
                    schoolsRV.setLayoutManager(new LinearLayoutManager(getActivity()));
                    schoolsRV.setLayoutManager(staggeredGridLayoutManager);
                    schoolsRV.setAdapter(adapter);
                } else {
                    Log.i("debug", "onResponse : FAILED");
                    Toast.makeText(getActivity(), response.code() + " ", Toast.LENGTH_LONG).show();

                }
            }

            @Override
            public void onFailure(Call<ResponseSchools> call, Throwable t) {
                Log.i("debug", "onResponse : FAILED");
                Toast.makeText(getActivity(), t.toString() + R.string.something_wrong + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void loadSchoolsTerm() {
        inputSearch.clearFocus();
        InputMethodManager in = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        assert in != null;
        in.hideSoftInputFromWindow(inputSearch.getWindowToken(), 0);
        String school = inputSearch.getText().toString().trim();

        if (school.isEmpty()) {
            layoutSearch.setError("Sekolah harus diisi");
            inputSearch.requestFocus();
            return;
        }

        UserModel user = SharedPrefManager.getInstance(getActivity()).getUser();

        String token = "Bearer " + user.getToken();
        Call<ResponseSchools> call = RetrofitClient.getInstance().getApi().schools2("application/json", token, school);
        call.enqueue(new Callback<ResponseSchools>() {
            @Override
            public void onResponse(Call<ResponseSchools> call, final Response<ResponseSchools> response) {
                ResponseSchools questionResponse = response.body();
                if (response.isSuccessful()) {
                    schoolsModels = questionResponse.getResult();
                    StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(1, LinearLayoutManager.VERTICAL);
                    adapter = new SchoolAdapter(schoolsModels, getActivity(), SchoolSearchFragment.this);
                    schoolsRV.setLayoutManager(new LinearLayoutManager(getActivity()));
                    schoolsRV.setLayoutManager(staggeredGridLayoutManager);
                    schoolsRV.setAdapter(adapter);

                } else {
                    Log.i("debug", "onResponse : FAILED");
                    Toast.makeText(getActivity(), R.string.something_wrong + response.code(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseSchools> call, Throwable t) {
                Log.i("debug", "onResponse : FAILED");
                Toast.makeText(getActivity(), t.toString() + R.string.something_wrong + t.toString(), Toast.LENGTH_LONG).show();
            }
        });
    }
}
