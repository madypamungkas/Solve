package id.technow.solve;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Toast;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.material.button.MaterialButton;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import id.technow.solve.Adapter.BannerSliderAdapter;
import id.technow.solve.Adapter.HomeItemAdapter;
import id.technow.solve.Api.RetrofitClient;
import id.technow.solve.Model.BannerModel;
import id.technow.solve.Model.MenuHomeModel;
import id.technow.solve.Model.ResponseBanner;
import id.technow.solve.Model.ResponseMenuHome;
import id.technow.solve.Model.UserModel;

import com.technow.solve.R;

import id.technow.solve.Storage.SharedPrefManager;
import com.smarteist.autoimageslider.IndicatorAnimations;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends Fragment {
    View view;
    SwipeRefreshLayout swipeRefresh;
    Context mContext;
    private ShimmerFrameLayout shimmerFrameLayout;
    HomeItemAdapter adapter;
    ArrayList<MenuHomeModel> models;
    ArrayList<BannerModel> bannerModel;
    BannerSliderAdapter sliderAdapter;
    private int a = 0;
    private SliderView slider;
    RecyclerView listCategory;

    public HomeFragment() {

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_home, container, false);

        mContext = getActivity().getWindow().getContext();
        slider = view.findViewById(R.id.banner_slider2);
        listCategory = view.findViewById(R.id.listCategory);
        checkConnection();
        //getVersion();

        return view;

    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }


    public void getListCategory() {
        UserModel user = SharedPrefManager.getInstance(mContext).getUser();
        String token = "Bearer " + user.getToken();

        Call<ResponseMenuHome> call = RetrofitClient.getInstance().getApi().category(token, "application/json");
        call.enqueue(new Callback<ResponseMenuHome>() {
            @Override
            public void onResponse(Call<ResponseMenuHome> call, Response<ResponseMenuHome> response) {
                ResponseMenuHome model = response.body();
                if (response.isSuccessful()) {
                    int size = model.getResult().size();
                    models = response.body().getResult();
                    StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL);
                    adapter = new HomeItemAdapter(models, mContext);
                    listCategory.setLayoutManager(new LinearLayoutManager(mContext));
                    listCategory.setLayoutManager(staggeredGridLayoutManager);
                    listCategory.setAdapter(adapter);
                } else {
                    Toast.makeText(mContext, R.string.something_wrong, Toast.LENGTH_SHORT).show();

                }
            }

            @Override
            public void onFailure(Call<ResponseMenuHome> call, Throwable t) {
                Toast.makeText(mContext, R.string.something_wrong, Toast.LENGTH_SHORT).show();

            }
        });
    }

    private void checkConnection() {
        if (isNetworkAvailable()) {
            loadBanner();
            getListCategory();
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

    private void loadBanner() {
        UserModel user = SharedPrefManager.getInstance(getActivity()).getUser();
        String token = "Bearer " + user.getToken();

        Call<ResponseBanner> call = RetrofitClient.getInstance().getApi().banner(token, "application/json");
        call.enqueue(new Callback<ResponseBanner>() {
            @Override
            public void onResponse(Call<ResponseBanner> call, Response<ResponseBanner> response) {
                if (response.isSuccessful()) {
                    bannerModel = response.body().getResult();
                    if (bannerModel.size() == 0) {
                        bannerModel = new ArrayList<>();
                        bannerModel.add(new BannerModel(1, "5d440cdd72347.jpg", "Banner 2", "1", "https://ruko.technow.id", "2019-08-02 10:13:49", "2019-08-02 10:13:49"));
                    } else {
                        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
                        SharedPreferences.Editor editorList = sharedPrefs.edit();
                        Gson gson = new Gson();

                        String json = gson.toJson(bannerModel);

                        editorList.putString("Banner", json);
                        editorList.apply();

                        sliderAdapter = new BannerSliderAdapter(getActivity(), bannerModel);
                        slider.startAutoCycle();
                        slider.setIndicatorAnimation(IndicatorAnimations.WORM);
                        slider.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION);
                        slider.setScrollTimeInSec(6);
                        slider.setSliderAdapter(sliderAdapter);
                    }

                } else {
                /*    bannerModel = new ArrayList<>();
                    bannerModel.add(new BannerModel(1, "5d440cdd72347.jpg", "Banner 2", "1", "https://ruko.technow.id", "2019-08-02 10:13:49", "2019-08-02 10:13:49"));
*/
                    SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
                    Gson gson = new Gson();
                    String json = sharedPrefs.getString("Banner", "Banner");
                    Type type = new TypeToken<List<BannerModel>>() {
                    }.getType();
                    ArrayList<BannerModel> bannerModel = gson.fromJson(json, type);
                    if (bannerModel.size() != 0) {
                        sliderAdapter = new BannerSliderAdapter(getActivity(), bannerModel);
                        slider.startAutoCycle();
                        slider.setIndicatorAnimation(IndicatorAnimations.WORM);
                        slider.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION);
                        slider.setScrollTimeInSec(6);
                        slider.setSliderAdapter(sliderAdapter);
                    } else {
                        Toast.makeText(getActivity(), R.string.something_wrong, Toast.LENGTH_LONG).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBanner> call, Throwable t) {
                SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
                Gson gson = new Gson();
                String json = sharedPrefs.getString("Banner", "Banner");
                Type type = new TypeToken<List<BannerModel>>() {
                }.getType();
                ArrayList<BannerModel> bannerModel = gson.fromJson(json, type);
                if (bannerModel.size() != 0) {
                    sliderAdapter = new BannerSliderAdapter(getActivity(), bannerModel);
                    slider.startAutoCycle();
                    slider.setIndicatorAnimation(IndicatorAnimations.WORM);
                    slider.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION);
                    slider.setScrollTimeInSec(6);
                    slider.setSliderAdapter(sliderAdapter);
                } else {
                    Toast.makeText(getActivity(), R.string.something_wrong, Toast.LENGTH_LONG).show();
                }

            }
        });
    }

}
