package com.komsi.solve;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.komsi.solve.Api.RetrofitClient;
import com.komsi.solve.Model.ResponseDetails;
import com.komsi.solve.Model.UserModel;
import com.komsi.solve.Storage.SharedPrefManager;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserFragment extends Fragment implements View.OnClickListener {
    View view;
    Button btnLogout;
    TextView txtUsername, txtEmail, txtVersion;
    LinearLayout btnProfile, btnChangePass, btnTermCondition;
    SwipeRefreshLayout swipeRefresh;
    CircleImageView imgProfile;
    Context mContext;
    ProgressDialog loading;

    String link = "https://solve.technow.id/storage/user/";
    private ShimmerFrameLayout shimmerFrameLayout;

    public UserFragment() {

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_user, container, false);
        UserModel user = SharedPrefManager.getInstance(getActivity()).getUser();

        mContext = getActivity().getWindow().getContext();
        btnLogout = view.findViewById(R.id.btnLogout);
        txtUsername = view.findViewById(R.id.txtUsername);
        txtEmail = view.findViewById(R.id.txtEmail);
        txtVersion = view.findViewById(R.id.txtVersion);
        PackageInfo pInfo = null;
        try {
            pInfo = mContext.getPackageManager().getPackageInfo(getActivity().getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        String version = pInfo.versionName;
        txtVersion.setText(version);

        btnChangePass = view.findViewById(R.id.btnChangePass);
        btnTermCondition = view.findViewById(R.id.btnTermCondition);
        btnProfile = view.findViewById(R.id.btnProfile);
        imgProfile = view.findViewById(R.id.imgProfile);
        btnTermCondition.setOnClickListener(this);
        btnChangePass.setOnClickListener(this);
        btnProfile.setOnClickListener(this);
        btnLogout.setOnClickListener(this);
        shimmerFrameLayout = view.findViewById(R.id.shimmerContainer);
        swipeRefresh = view.findViewById(R.id.swipeRefresh);
        swipeRefresh.setEnabled(true);
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                checkConnection();
            }
        });
        Picasso.get().load("https://solve.technow.id/storage/user/"+user.getId()).error(R.drawable.ic_user).into(imgProfile);

        return view;

    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    private void detailUser() {
        shimmerFrameLayout.setVisibility(View.VISIBLE);
        shimmerFrameLayout.startShimmer();
        txtEmail.setVisibility(View.GONE);
        txtUsername.setVisibility(View.GONE);
        //imgProfile.setVisibility(View.GONE);
        String accept = "application/json";

        UserModel user = SharedPrefManager.getInstance(getActivity()).getUser();
        String token = user.getToken();
        Call<ResponseDetails> call = RetrofitClient
                .getInstance()
                .getApi()
                .detail(accept, "Bearer " + token);

        call.enqueue(new Callback<ResponseDetails>() {

            @Override
            public void onResponse(Call<ResponseDetails> call, Response<ResponseDetails> response) {
                ResponseDetails responseDetails = response.body();
                if (response.isSuccessful()) {
                    if (responseDetails.getStatus().equals("success")) {
                        txtEmail.setVisibility(View.VISIBLE);
                        txtUsername.setVisibility(View.VISIBLE);
                        imgProfile.setVisibility(View.VISIBLE);
                        txtEmail.setText(responseDetails.getUser().getEmail());
                        txtUsername.setText(responseDetails.getUser().getName());
                        Picasso.get().load(link+response.body().getUser().getId()).error(R.drawable.ic_user).into(imgProfile);
                        shimmerFrameLayout.setVisibility(View.GONE);
                        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
                        SharedPreferences.Editor editorList = sharedPrefs.edit();
                        Gson gson = new Gson();

                        String detailUser = gson.toJson(response.body());
                        editorList.putString("DetailUser", detailUser);
                        editorList.commit();
                    }
                } else {
                    try {
                        JSONObject jObjError = new JSONObject(response.errorBody().string());
                        Toast.makeText(getActivity(), jObjError.getString("message"), Toast.LENGTH_LONG).show();
                        if (jObjError.getString("message").equals("Unauthenticated.")) {
                            SharedPrefManager.getInstance(getActivity()).clear();
                            Intent intent = new Intent(getActivity(), LoginActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                        }
                    } catch (Exception e) {
                        Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
                shimmerFrameLayout.stopShimmer();
                swipeRefresh.setRefreshing(false);
            }

            @Override
            public void onFailure(Call<ResponseDetails> call, Throwable t) {
                shimmerFrameLayout.stopShimmer();
                shimmerFrameLayout.setVisibility(View.GONE);
                swipeRefresh.setRefreshing(false);
                Toast.makeText(getActivity(), "Something wrong. Try again later", Toast.LENGTH_LONG).show();
                Log.d("TAG", "Response = " + t.toString());
            }
        });
    }

    private void checkConnection() {
        if (isNetworkAvailable()) {
            detailUser();
        } else {
            final Dialog dialog = new Dialog(mContext);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setCancelable(false);
            dialog.setContentView(R.layout.dialog_no_internet);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

            DisplayMetrics metrics = getResources().getDisplayMetrics();
            int width = metrics.widthPixels;
            int height = metrics.heightPixels;

            dialog.getWindow().setLayout((9 * width) / 10, height);

            MaterialButton btnRetry = dialog.findViewById(R.id.btnRetry);
            btnRetry.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                    checkConnection();
                }
            });
            dialog.show();
        }
    }

    private void confirmLogOut() {
        final Dialog dialog = new Dialog(mContext);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.dialog_logout);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        DisplayMetrics metrics = getResources().getDisplayMetrics();
        int width = metrics.widthPixels;
        int height = metrics.heightPixels;

        dialog.getWindow().setLayout((9 * width) / 10, (2 * height) / 5);

        MaterialButton btnNo = dialog.findViewById(R.id.btnNo);
        btnNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        MaterialButton btnYes = dialog.findViewById(R.id.btnYes);
        btnYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logoutUser();
                logOut();
            }
        });

        dialog.show();
    }

    private void logoutUser() {
        UserModel user = SharedPrefManager.getInstance(getActivity()).getUser();
        String token = user.getToken();
        Call<ResponseBody> call = RetrofitClient
                .getInstance()
                .getApi()
                .logout("Bearer " + token);

        call.enqueue(new Callback<ResponseBody>() {

            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(getActivity(), "Logout Successfully", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(getActivity(), "Something wrong. Try again later", Toast.LENGTH_LONG).show();
                Log.d("TAG", "Response = " + t.toString());
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        checkConnection();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnProfile:
                Intent i = new Intent(getActivity(), ChangeProfile.class);
                startActivity(i);
                break;
            case R.id.btnChangePass:
                Intent i2 = new Intent(getActivity(), ChangePassword.class);
                startActivity(i2);
                break;
            case R.id.btnLogout:
                confirmLogOut();
                break;
        }
    }

    private void logOut() {
        loading = ProgressDialog.show(mContext, null, getString(R.string.please_wait), true, false);

        UserModel user = SharedPrefManager.getInstance(getActivity()).getUser();
        String token = "Bearer " + user.getToken();
        Call<ResponseBody> call = RetrofitClient.getInstance().getApi().logout(token);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                loading.dismiss();
                if (response.isSuccessful()) {
                    SharedPrefManager.getInstance(getActivity()).clear();
                    SharedPreferences sharedPreferences = (getActivity().getSharedPreferences(SharedPrefManager.SHARED_PREF_NAME, Context.MODE_PRIVATE));
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.clear();
                    editor.apply();

                    SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
                    SharedPreferences.Editor editorList = sharedPrefs.edit();
                    editorList.clear();
                    editor.apply();

                    Intent intent = new Intent(getActivity(), LoginActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                } else {
                    Toast.makeText(getActivity(), R.string.something_wrong, Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                loading.dismiss();
                Toast.makeText(getActivity(), R.string.something_wrong, Toast.LENGTH_LONG).show();
            }
        });

    }


}
