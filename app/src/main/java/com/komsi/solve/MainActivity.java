package com.komsi.solve;

import android.animation.ArgbEvaluator;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;

import android.preference.PreferenceManager;
import android.view.MenuItem;
import android.view.View;

import com.google.android.material.navigation.NavigationView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.komsi.solve.Adapter.BannerSliderAdapter;
import com.komsi.solve.Adapter.HomeItemAdapter;
import com.komsi.solve.Adapter.SliderViewAdapter;
import com.komsi.solve.Api.RetrofitClient;
import com.komsi.solve.Model.BannerModel;
import com.komsi.solve.Model.DetailUser;
import com.komsi.solve.Model.MenuHomeModel;
import com.komsi.solve.Model.ResponseBanner;
import com.komsi.solve.Model.ResponseDetails;
import com.komsi.solve.Model.ResponseMenuHome;
import com.komsi.solve.Model.ResponseVersion;
import com.komsi.solve.Model.UserModel;
import com.komsi.solve.Model.VersionModel;
import com.komsi.solve.Storage.SharedPrefManager;
import com.smarteist.autoimageslider.IndicatorAnimations;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;
import com.squareup.picasso.Picasso;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import androidx.viewpager.widget.ViewPager;
import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.view.Window;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener{

    ViewPager viewPager;
    HomeItemAdapter adapter;
    ArrayList<MenuHomeModel> models;
    Integer[] colors = null;
    ArrayList<BannerModel> bannerModel;
    BannerSliderAdapter sliderAdapter;
    ArgbEvaluator argbEvaluator = new ArgbEvaluator();
    private int a = 0;
    private SliderView slider;
    TextView navUsername, navEmail;
    CircleImageView navImage;
    //    User user = SharedPrefManager.getInstance(this).getUser();
//    String token = "Bearer " + user.getToken();
    RecyclerView RVmain;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

       // getDetails();

        LinearLayout btnSearchCode = findViewById(R.id.btnSearchCode);
        
        slider = findViewById(R.id.banner_slider2);
        loadBanner();

        //StatusBarUtil.immersive(this);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        final View viewPagerBackground = findViewById(R.id.view2);

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener((NavigationView.OnNavigationItemSelectedListener) this);

        View header = navigationView.getHeaderView(0);
        //navUsername = (TextView) header.findViewById(R.id.navUsername);
        //navImage = header.findViewById(R.id.navImage);

        String defaultLink = getResources().getString(R.string.link);
        String link = defaultLink + "user/";
        //DetailUser detailUser = SharedPrefManager.getInstance(this).detailUser();

        //navEmail = header.findViewById(R.id.navEmail);

        getListCategory();

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            confirmBackpress();
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_account) {
            Intent intent = new Intent(MainActivity.this, ProfileActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_home) {
            Intent intent = new Intent(MainActivity.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        } else if (id == R.id.nav_leaderboard) {
            Intent intent = new Intent(MainActivity.this, LeaderboardChoose.class);
            startActivity(intent);
        } else if (id == R.id.nav_settings) {
            Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_sign_out) {
            confirmLogOut();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    public void onStart() {
        super.onStart();
        if (!SharedPrefManager.getInstance(this).isLoggedIn()) {
            Intent intent = new Intent(this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }
        loadfoto();
      //  getDetails();
    }

    public void confirmBackpress() {
        final Dialog dialog = new Dialog(MainActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.custom_exit_dialog);

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        FrameLayout mDialogNo = dialog.findViewById(R.id.frmNo);
        mDialogNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        FrameLayout mDialogOk = dialog.findViewById(R.id.frmYes);
        mDialogOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                dialog.dismiss();
            }
        });

        dialog.show();

        Window window = dialog.getWindow();
        window.setLayout(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);
    }

    private void logOut() {
        UserModel user = SharedPrefManager.getInstance(this).getUser();
        String token = "Bearer " + user.getToken();
        Call<ResponseBody> call = RetrofitClient.getInstance().getApi().logout(token);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    SharedPrefManager.getInstance(MainActivity.this).clear();
                    Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                } else {
                    Toast.makeText(MainActivity.this, R.string.something_wrong, Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(MainActivity.this, R.string.something_wrong, Toast.LENGTH_LONG).show();
            }
        });

    }

    public void confirmLogOut() {
        final Dialog dialog = new Dialog(MainActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.custom_logout_dialog);

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        FrameLayout mDialogNo = dialog.findViewById(R.id.frmNo);
        mDialogNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        FrameLayout mDialogOk = dialog.findViewById(R.id.frmYes);
        mDialogOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPrefManager.getInstance(MainActivity.this).clear();
                SharedPreferences sharedPreferences = (getSharedPreferences(SharedPrefManager.SHARED_PREF_NAME, Context.MODE_PRIVATE));
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.clear();
                editor.apply();
                logOut();
                dialog.dismiss();
            }
        });

        dialog.show();

        Window window = dialog.getWindow();
        window.setLayout(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);
    }

   /* private void getDetails() {
        UserModel user = SharedPrefManager.getInstance(this).getUser();
        String token = "Bearer " + user.getToken();
        retrofit2.Call<ResponseDetails> call = RetrofitClient.getInstance().getApi().detail("application/json", token);
        call.enqueue(new Callback<ResponseDetails>() {

            @Override
            public void onResponse(retrofit2.Call<ResponseDetails> call, Response<ResponseDetails> response) {

                if (response.isSuccessful()) {
                    ResponseDetails detailsResponse = response.body();
                    DetailUser detailUser = detailsResponse.getUser();
                    SharedPrefManager.getInstance(MainActivity.this).saveDetail(detailUser);
                    getVersion();
                    NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
                    //navigationView.setNavigationItemSelectedListener(this);


                    View header = navigationView.getHeaderView(0);

                    //navImage = header.findViewById(R.id.navImage);
                    String defaultLink = getResources().getString(R.string.link);
                    String link = defaultLink + "user/";

                    navEmail.setText(response.body().getUser().getEmail());
                    navUsername.setText(response.body().getUser().getUsername());

                    Picasso.get().load(link + response.body().getUser().getPicture()).error(R.drawable.ic_userprofile)
                            .into(navImage);
                } else {
                    Toast.makeText(MainActivity.this, R.string.something_wrong, Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(retrofit2.Call<ResponseDetails> call, Throwable t) {
                Toast.makeText(MainActivity.this, t.toString(), Toast.LENGTH_LONG).show();
            }
        });
    }*/

    public void loadBanner() {
        UserModel user = SharedPrefManager.getInstance(this).getUser();
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
                        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
                        SharedPreferences.Editor editorList = sharedPrefs.edit();
                        Gson gson = new Gson();

                        String json = gson.toJson(bannerModel);

                        editorList.putString("Banner", json);
                        editorList.commit();

                        sliderAdapter = new BannerSliderAdapter(MainActivity.this, bannerModel);
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
                    SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
                    Gson gson = new Gson();
                    String json = sharedPrefs.getString("Banner", "Banner");
                    Type type = new TypeToken<List<BannerModel>>() {
                    }.getType();
                    ArrayList<BannerModel> bannerModel = gson.fromJson(json, type);
                    if (bannerModel.size() != 0) {
                        sliderAdapter = new BannerSliderAdapter(MainActivity.this, bannerModel);
                        slider.startAutoCycle();
                        slider.setIndicatorAnimation(IndicatorAnimations.WORM);
                        slider.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION);
                        slider.setScrollTimeInSec(6);
                        slider.setSliderAdapter(sliderAdapter);
                    } else {
                        Toast.makeText(MainActivity.this, R.string.something_wrong, Toast.LENGTH_LONG).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBanner> call, Throwable t) {
                SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
                Gson gson = new Gson();
                String json = sharedPrefs.getString("Banner", "Banner");
                Type type = new TypeToken<List<BannerModel>>() {
                }.getType();
                ArrayList<BannerModel> bannerModel = gson.fromJson(json, type);
                if (bannerModel.size() != 0) {
                    sliderAdapter = new BannerSliderAdapter(MainActivity.this, bannerModel);
                    slider.startAutoCycle();
                    slider.setIndicatorAnimation(IndicatorAnimations.WORM);
                    slider.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION);
                    slider.setScrollTimeInSec(6);
                    slider.setSliderAdapter(sliderAdapter);
                } else {
                    Toast.makeText(MainActivity.this, R.string.something_wrong, Toast.LENGTH_LONG).show();
                }

            }
        });
    }

    public void getVersion() {
        UserModel user = SharedPrefManager.getInstance(this).getUser();
        String token = "Bearer " + user.getToken();
        Call<ResponseVersion> call = RetrofitClient.getInstance().getApi().version("application/json");
        call.enqueue(new Callback<ResponseVersion>() {
            @Override
            public void onResponse(Call<ResponseVersion> call, Response<ResponseVersion> response) {
                if (response.isSuccessful()) {
                    SharedPrefManager.getInstance(MainActivity.this).saveVersion(response.body().getResult());
                    versionCheck();
                } else {
                    Toast.makeText(MainActivity.this, R.string.something_wrong, Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseVersion> call, Throwable t) {
                Toast.makeText(MainActivity.this, R.string.something_wrong, Toast.LENGTH_LONG).show();

            }
        });
    }

    public void versionCheck() {
        VersionModel versionModel = SharedPrefManager.getInstance(MainActivity.this).versionModel();

        String version = versionModel.getVersion() + "." + versionModel.getSub_version();

        if (!version.equals(BuildConfig.VERSION_NAME)) {
            final Dialog dialog = new Dialog(MainActivity.this);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setCancelable(false);
            dialog.setContentView(R.layout.custom_update_dialog);

            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

            FrameLayout mDialogNo = dialog.findViewById(R.id.frmNo);
            mDialogNo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                    dialog.dismiss();
                }
            });

            FrameLayout mDialogOk = dialog.findViewById(R.id.frmYes);
            mDialogOk.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                    dialog.dismiss();
                    final String appPackageName = getPackageName();
                    try {
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
                    } catch (android.content.ActivityNotFoundException anfe) {
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
                    }

                }
            });

            dialog.show();

            Window window = dialog.getWindow();
            window.setLayout(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        getVersion();
        //getDetails();
        loadfoto();
    }

    public void loadfoto() {

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener((NavigationView.OnNavigationItemSelectedListener) this);

        UserModel user = SharedPrefManager.getInstance(this).getUser();

        View header = navigationView.getHeaderView(0);

        //navImage = header.findViewById(R.id.navImage);

        //   String link = "http://10.33.77.95/master-ruko/ruko/public/storage/user/";
        String defaultLink = getResources().getString(R.string.link);
        String link = defaultLink + "user/";

       /* Picasso.get().load(link + detailUser.getPicture()).error(R.drawable.ic_userprofile)
                .into(navImage);
*/
    }

    public void getListCategory() {
        UserModel user = SharedPrefManager.getInstance(this).getUser();
        String token = "Bearer " + user.getToken();

        Call<ResponseMenuHome> call = RetrofitClient.getInstance().getApi().category(token, "application/json");
        call.enqueue(new Callback<ResponseMenuHome>() {
            @Override
            public void onResponse(Call<ResponseMenuHome> call, Response<ResponseMenuHome> response) {
                ResponseMenuHome model = response.body();
                if (response.isSuccessful()) {
                    int size = model.getResult().size();
                    models = response.body().getResult();
                    RVmain = findViewById(R.id.RVmain);
                    StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL);
                    adapter = new HomeItemAdapter(models, MainActivity.this);
                    RVmain.setLayoutManager(new LinearLayoutManager(MainActivity.this));
                    RVmain.setLayoutManager(staggeredGridLayoutManager);
                    RVmain.setAdapter(adapter);
                } else {
                    Toast.makeText(MainActivity.this, R.string.something_wrong, Toast.LENGTH_SHORT).show();

                }
            }

            @Override
            public void onFailure(Call<ResponseMenuHome> call, Throwable t) {
                Toast.makeText(MainActivity.this, R.string.something_wrong, Toast.LENGTH_SHORT).show();

            }
        });
    }
}
