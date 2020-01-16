package com.komsi.solve;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.Fragment;
import androidx.appcompat.app.AppCompatDelegate;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.komsi.solve.Api.RetrofitClient;
import com.komsi.solve.Model.ResponseVersion;
import com.komsi.solve.Model.UserModel;
import com.komsi.solve.Model.VersionModel;
import com.komsi.solve.Storage.SharedPrefManager;

import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentActivity;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.preference.PreferenceManager;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.Toast;

public class Main2Activity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private BottomNavigationView bottomNavigationView;
    ProgressDialog loading;



    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment fragment = null;
            switch (item.getItemId()) {
                case R.id.navigationMenu:
                    DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                    drawer.openDrawer(GravityCompat.START);
                    break;
                case R.id.navigationHistory:
                    fragment = new HistoryFragment();
                    break;
                case R.id.navigationHome:
                    fragment = new HomeFragment();
                    break;
                case R.id.navigationSearch:
                    fragment = new SearchFragment();
                    break;
                case R.id.navigationUser:
                    fragment = new UserFragment();
                    break;
            }
            return loadFragment(fragment);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main2);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        bottomNavigationView = findViewById(R.id.navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        CoordinatorLayout.LayoutParams layoutParams = (CoordinatorLayout.LayoutParams) bottomNavigationView.getLayoutParams();
        layoutParams.setBehavior(new BottomNavigationBehavior());

        int fragmentId = getIntent().getIntExtra("FRAGMENT_ID", 2);

        if (fragmentId == 0) {
            drawer.openDrawer(GravityCompat.START);
        } else if (fragmentId == 1) {
            loadFragment(new HistoryFragment());
            bottomNavigationView.setSelectedItemId(R.id.navigationHistory);
        } else if (fragmentId == 2) {
            loadFragment(new SearchFragment());
            bottomNavigationView.setSelectedItemId(R.id.navigationHome);
        } else if (fragmentId == 3) {
            loadFragment(new SearchFragment());
            bottomNavigationView.setSelectedItemId(R.id.navigationSearch);
        } else {
            loadFragment(new UserFragment());
            bottomNavigationView.setSelectedItemId(R.id.navigationUser);
        }
        getVersion();
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


    public void confirmBackpress() {
        final Dialog dialog = new Dialog(Main2Activity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.custom_exit_dialog);

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        FloatingActionButton mDialogNo = dialog.findViewById(R.id.fbNo);
        mDialogNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        FloatingActionButton mDialogOk = dialog.findViewById(R.id.fbYes);
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


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_account) {
            Intent intent = new Intent(Main2Activity.this, ProfileActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_leaderboard) {
            Intent intent = new Intent(Main2Activity.this, LeaderboardChoose.class);
            startActivity(intent);
        } else if (id == R.id.nav_settings) {
            loadFragment(new UserFragment());
            BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
            navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
            navigation.setSelectedItemId(R.id.navigationUser);
        } else if (id == R.id.nav_sign_out) {
            confirmLogOut();
        }


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private boolean loadFragment(Fragment fragment) {
        if (fragment != null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragmentContainer, fragment)
                    .commit();
            return true;
        }
        return false;
    }

    public void confirmLogOut() {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.custom_logout_dialog);

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        FloatingActionButton mDialogNo = dialog.findViewById(R.id.fbNo);
        mDialogNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        FloatingActionButton mDialogOk = dialog.findViewById(R.id.fbYes);
        mDialogOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logOut();
                dialog.dismiss();
            }
        });

        dialog.show();

        Window window = dialog.getWindow();
        window.setLayout(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);
    }

    private void logOut() {
        loading = ProgressDialog.show(Main2Activity.this, null, getString(R.string.please_wait), true, false);

        UserModel user = SharedPrefManager.getInstance(this).getUser();
        String token = "Bearer " + user.getToken();
        Call<ResponseBody> call = RetrofitClient.getInstance().getApi().logout(token);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                loading.dismiss();
                if (response.isSuccessful()) {
                    SharedPrefManager.getInstance(Main2Activity.this).clear();
                    SharedPreferences sharedPreferences = (getSharedPreferences(SharedPrefManager.SHARED_PREF_NAME, Context.MODE_PRIVATE));
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.clear();
                    editor.apply();

                    SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(Main2Activity.this);
                    SharedPreferences.Editor editorList = sharedPrefs.edit();
                    editorList.clear();
                    editor.apply();

                    Intent intent = new Intent(Main2Activity.this, LoginActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                } else {
                    Toast.makeText(Main2Activity.this, R.string.something_wrong, Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                loading.dismiss();
                Toast.makeText(Main2Activity.this, R.string.something_wrong, Toast.LENGTH_LONG).show();
            }
        });

    }

    public void getVersion() {
        Call<ResponseVersion> call = RetrofitClient.getInstance().getApi().version("application/json");
        call.enqueue(new Callback<ResponseVersion>() {
            @Override
            public void onResponse(Call<ResponseVersion> call, Response<ResponseVersion> response) {
                if (response.isSuccessful()) {
                    SharedPrefManager.getInstance(Main2Activity.this).saveVersion(response.body().getResult());
                    versionCheck();
                } else {
                    Toast.makeText(Main2Activity.this, R.string.something_wrong, Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseVersion> call, Throwable t) {
                Toast.makeText(Main2Activity.this, R.string.something_wrong, Toast.LENGTH_LONG).show();

            }
        });
    }

    public void versionCheck() {
        VersionModel versionModel = SharedPrefManager.getInstance(Main2Activity.this).versionModel();

        String version = versionModel.getVersion() + "." + versionModel.getSub_version();

        if (!version.equals(BuildConfig.VERSION_NAME)) {
            final Dialog dialog = new Dialog(Main2Activity.this);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setCancelable(false);
            dialog.setContentView(R.layout.custom_update_dialog);

            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

            FrameLayout mDialogNo = dialog.findViewById(R.id.frmNo);
            FloatingActionButton fbNo = dialog.findViewById(R.id.fbNo);
            fbNo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                    dialog.dismiss();
                }
            });

            FloatingActionButton fbYes = dialog.findViewById(R.id.fbYes);
            fbYes.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //finish();
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

    }
}
