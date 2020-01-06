package com.komsi.solve;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.facebook.shimmer.ShimmerFrameLayout;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import de.hdodenhof.circleimageview.CircleImageView;

public class HistoryFragment extends Fragment implements View.OnClickListener {
    View view;
    Button btnLogout;
    TextView txtUsername, txtEmail, txtVersion;
    LinearLayout btnProfile, btnChangePass, btnTermCondition;
    SwipeRefreshLayout swipeRefresh;
    CircleImageView imgProfile;
    Context mContext;
    private ShimmerFrameLayout shimmerFrameLayout;

    public HistoryFragment() {

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_history, container, false);

        mContext = getActivity().getWindow().getContext();

        PackageInfo pInfo = null;
        try {
            pInfo = mContext.getPackageManager().getPackageInfo(getActivity().getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        /*

        shimmerFrameLayout = view.findViewById(R.id.shimmerContainer);
        swipeRefresh = view.findViewById(R.id.swipeRefresh);
        swipeRefresh.setEnabled(true);
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                checkConnection();
            }
        });

         */

        return view;

    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    /*
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

            Button btnRetry = dialog.findViewById(R.id.btnRetry);
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

    private void logOut() {
        SharedPrefManager.getInstance(getActivity()).clear();
        Intent intent = new Intent(getActivity(), LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
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

        Button btnNo = dialog.findViewById(R.id.btnNo);
        btnNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        Button btnYes = dialog.findViewById(R.id.btnYes);
        btnYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logoutUser();
                logOut();
            }
        });

        dialog.show();
    }

    private void detailUser() {
        shimmerFrameLayout.setVisibility(View.VISIBLE);
        shimmerFrameLayout.startShimmer();
        txtEmail.setVisibility(View.GONE);
        txtUsername.setVisibility(View.GONE);
        imgProfile.setVisibility(View.GONE);
        String accept = "application/json";

        User user = SharedPrefManager.getInstance(getActivity()).getUser();
        String token = user.getToken();
        Call<DetailUserResponse> call = RetrofitClient
                .getInstance()
                .getApi()
                .detailUser(accept, "Bearer " + token);

        call.enqueue(new Callback<DetailUserResponse>() {

            @Override
            public void onResponse(Call<DetailUserResponse> call, Response<DetailUserResponse> response) {
                DetailUserResponse detailUserResponse = response.body();
                if (response.isSuccessful()) {
                    if (detailUserResponse.getStatus().equals("success")) {
                        txtEmail.setVisibility(View.VISIBLE);
                        txtUsername.setVisibility(View.VISIBLE);
                        imgProfile.setVisibility(View.VISIBLE);
                        txtEmail.setText(detailUserResponse.getDetailUser().getEmail());
                        txtUsername.setText(detailUserResponse.getDetailUser().getName());
                        Picasso.get().load(detailUserResponse.getDetailUser().getPic()).error(R.drawable.ic_person).into(imgProfile);
                        shimmerFrameLayout.setVisibility(View.GONE);
                    } else {
                        String emailSend = detailUserResponse.getEmail();
                        Intent intent = new Intent(getActivity(), VerifyEmailActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        intent.putExtra("email", emailSend);
                        startActivity(intent);
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
            public void onFailure(Call<DetailUserResponse> call, Throwable t) {
                shimmerFrameLayout.stopShimmer();
                shimmerFrameLayout.setVisibility(View.GONE);
                swipeRefresh.setRefreshing(false);
                Toast.makeText(getActivity(), "Something wrong. Try again later", Toast.LENGTH_LONG).show();
                Log.d("TAG", "Response = " + t.toString());
            }
        });
    }

    private void logoutUser() {
        User user = SharedPrefManager.getInstance(getActivity()).getUser();
        String token = user.getToken();
        Call<LogoutResponse> call = RetrofitClient
                .getInstance()
                .getApi()
                .logoutUser("Bearer " + token);

        call.enqueue(new Callback<LogoutResponse>() {

            @Override
            public void onResponse(Call<LogoutResponse> call, Response<LogoutResponse> response) {
                if (response.isSuccessful()) {
                    LogoutResponse logoutResponse = response.body();
                    Toast.makeText(getActivity(), "Logout Successfully", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<LogoutResponse> call, Throwable t) {
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
    */
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            /*
            case R.id.btnProfile:
                Intent i = new Intent(getActivity(), SettingsProfileActivity.class);
                startActivity(i);
                break;
            case R.id.btnChangePass:
                Intent store = new Intent(getActivity(), SettingsChangePassActivity.class);
                startActivity(store);
                break;
            case R.id.btnTermCondition:
                Intent toc = new Intent(getActivity(), WebViewActivity.class);
                toc.putExtra("BannerLink", "k-jur.technow.id/privacy");
                startActivity(toc);
                break;
             */
        }
    }

}
