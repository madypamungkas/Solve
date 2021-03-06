package id.technow.solve;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.Fragment;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.navigation.NavigationView;

import id.technow.solve.Api.RetrofitClient;
import id.technow.solve.Model.UserModel;

import id.technow.solve.R;

import id.technow.solve.Storage.SharedPrefManager;

import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import id.technow.solve.Storage.SharedPrefRate;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.os.CountDownTimer;
import android.preference.PreferenceManager;
import android.util.DisplayMetrics;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Locale;
import java.util.TimeZone;

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
        checkRating();
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
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.dialog_confirm_exit_app);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        DisplayMetrics metrics = getResources().getDisplayMetrics();
        int width = metrics.widthPixels;
        int height = metrics.heightPixels;

        dialog.getWindow().setLayout((9 * width) / 10, (3 * height) / 5);

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
                finish();
                dialog.dismiss();
            }
        });

        dialog.show();
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        // Handle navigation view item clicks here.
        int id = item.getItemId();
        if (id == R.id.nav_leaderboard) {
            Intent intent = new Intent(Main2Activity.this, LeaderboardChooseCategory.class);
            startActivity(intent);
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
        final Dialog dialog = new Dialog(Main2Activity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.dialog_logout);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        DisplayMetrics metrics = getResources().getDisplayMetrics();
        int width = metrics.widthPixels;
        int height = metrics.heightPixels;

        dialog.getWindow().setLayout((9 * width) / 10, (3 * height) / 5);

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
                logOut();
                dialog.dismiss();
            }
        });


        dialog.show();
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

    public void checkRating() {
        String spr = SharedPrefRate.getInstance(this).getRate();
        int play = SharedPrefRate.getInstance(this).getTryPlaying();
        //Toast.makeText(this, spr +" "+ play, Toast.LENGTH_SHORT).show();
        if (spr == "no") {
            if (play > 2) {
                dialogRate();
            }
        } else {

        }
    }

    private void dialogRate() {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setContentView(R.layout.dialog_rating);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        DisplayMetrics metrics = getResources().getDisplayMetrics();
        int width = metrics.widthPixels;
        int height = metrics.heightPixels;

        dialog.getWindow().setLayout((9 * width) / 10, height);

        MaterialButton btnUpgrade = dialog.findViewById(R.id.btnUpgrade);
        btnUpgrade.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                SharedPrefRate.getInstance(Main2Activity.this).saveRate("yes");
                 final String appPackageName = getPackageName();
                try {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
                } catch (android.content.ActivityNotFoundException anfe) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
                }

            }
        });

        TextView showIklan = dialog.findViewById(R.id.showAd);
        showIklan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                                  dialog.dismiss();
            }
        });
        dialog.show();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}
