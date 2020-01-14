package com.komsi.solve;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.material.button.MaterialButton;
import com.komsi.solve.Adapter.HistoryAdapter;
import com.komsi.solve.Api.RetrofitClient;
import com.komsi.solve.Model.HistoryModel;
import com.komsi.solve.Model.ResponseHistory;
import com.komsi.solve.Model.ResponseMenuHome;
import com.komsi.solve.Model.UserModel;
import com.komsi.solve.Storage.SharedPrefManager;

import java.util.ArrayList;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HistoryFragment extends Fragment {
    View view;
    SwipeRefreshLayout swipeRefresh;
    RecyclerView rvHistory;
    LinearLayout noDataLayout;
    RelativeLayout rvLayout;
    Context mContext;
    HistoryAdapter adapter;
    private ShimmerFrameLayout shimmerFrameLayout;

    public HistoryFragment() {

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_history, container, false);

        mContext = getActivity().getWindow().getContext();

        rvHistory = view.findViewById(R.id.rvHistory);
        rvLayout = view.findViewById(R.id.rvLayout);
        noDataLayout = view.findViewById(R.id.noDataLayout);
        shimmerFrameLayout = view.findViewById(R.id.shimmerContainer);

        swipeRefresh = view.findViewById(R.id.swipeRefresh);
        swipeRefresh.setEnabled(true);
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                checkConnection();
            }
        });

        checkConnection();

        return view;

    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    private void loadData() {
        rvLayout.setVisibility(View.VISIBLE);
        noDataLayout.setVisibility(View.GONE);
        shimmerFrameLayout.setVisibility(View.VISIBLE);
        shimmerFrameLayout.startShimmer();

        UserModel user = SharedPrefManager.getInstance(mContext).getUser();
        String token = "Bearer " + user.getToken();

        Call<ResponseHistory> call = RetrofitClient.getInstance().getApi().history("application/json", token);
        call.enqueue(new Callback<ResponseHistory>() {
            @Override
            public void onResponse(Call<ResponseHistory> call, Response<ResponseHistory> response) {
                if (response.isSuccessful()) {
                    adapter = new HistoryAdapter(response.body().getResult(), mContext);
                    RecyclerView.LayoutManager eLayoutManager = new LinearLayoutManager(mContext);
                    rvHistory.setLayoutManager(eLayoutManager);
                    rvHistory.setItemAnimator(new DefaultItemAnimator());
                    rvHistory.setAdapter(adapter);
                    if (eLayoutManager.getItemCount() == 0) {
                        shimmerFrameLayout.setVisibility(View.GONE);
                        rvLayout.setVisibility(View.GONE);
                        noDataLayout.setVisibility(View.VISIBLE);
                    } else {
                        noDataLayout.setVisibility(View.GONE);
                        shimmerFrameLayout.setVisibility(View.GONE);
                        rvLayout.setVisibility(View.VISIBLE);
                    }
                    //  Toast.makeText(mContext, response.code()+" ", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(mContext, "Coba Beberapa Saat Lagi", Toast.LENGTH_LONG).show();

                }
                swipeRefresh.setRefreshing(false);
                shimmerFrameLayout.stopShimmer();
            }

            @Override
            public void onFailure(Call<ResponseHistory> call, Throwable t) {
                shimmerFrameLayout.stopShimmer();
                shimmerFrameLayout.setVisibility(View.GONE);
                rvLayout.setVisibility(View.GONE);
                noDataLayout.setVisibility(View.VISIBLE);
                swipeRefresh.setRefreshing(false);
                Toast.makeText(mContext, t.toString() + " ", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void checkConnection() {
        if (isNetworkAvailable()) {
            loadData();
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


    @Override
    public void onResume() {
        super.onResume();
        checkConnection();
    }

}
