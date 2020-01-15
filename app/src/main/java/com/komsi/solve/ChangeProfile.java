package com.komsi.solve;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.fragment.app.FragmentActivity;
import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.komsi.solve.Api.RetrofitClient;
import com.komsi.solve.Model.DetailUser;
import com.komsi.solve.Model.ResponseDetails;
import com.komsi.solve.Model.ResponseProfile;
import com.komsi.solve.Model.UserModel;
import com.komsi.solve.Storage.SharedPrefManager;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Iterator;

public class ChangeProfile extends AppCompatActivity {
    private static final String TAG = "Register";

    private AppCompatEditText etSName, etSEmail;
    private AppCompatTextView etSUsername;
    MaterialButton btnConfirm;
    UserModel user;
    private static final int IMG_REQUEST = 777;
    ProgressDialog loading;
    public TextView txtSchools,btnEditPic;
    String idSchool, schoolName;
    CircleImageView imgProfile;
    private Bitmap bitmap, dstBmp;
    ProgressDialog progress;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_profile);
        etSName = findViewById(R.id.etSName);
        etSEmail = findViewById(R.id.etSEmail);
        etSUsername = findViewById(R.id.etSUsername);
        user = SharedPrefManager.getInstance(this).getUser();
        btnConfirm = findViewById(R.id.btnConfirm);
        txtSchools = findViewById(R.id.txtSchools);
        btnEditPic = findViewById(R.id.btnEditPic);
        imgProfile = findViewById(R.id.imgProfile);

        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(ChangeProfile.this);
        Gson gson = new Gson();
        String json = sharedPrefs.getString("DetailUser", "DetailUser");
        Type type = new TypeToken<ResponseDetails>() {
        }.getType();
        ResponseDetails responseDetails = gson.fromJson(json, type);

        DetailUser detail = responseDetails.getUser();

        Picasso.get().load("https://solve.technow.id/storage/user/"+user.getId()).error(R.drawable.ic_user).into(imgProfile);

        etSName.setText(user.getName());
        etSEmail.setText(user.getEmail());
        etSUsername.setText(user.getUsername());
        txtSchools.setText(detail.getSchool().getName());
        //loadData();
        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateProfil();
            }
        });
        btnEditPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectImage();
            }
        });

        txtSchools.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                SchoolsSearchFragment fragment = new SchoolsSearchFragment();
                fragment.setArguments(bundle);
                fragment.show(((FragmentActivity) ChangeProfile.this).getSupportFragmentManager(), TAG);
            }
        });
        ImageButton btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }
    public void setTextSch(String schoolName, String schoolId) {
        txtSchools.setText(schoolName);
        idSchool = schoolId;
    }

    public void updateProfil() {
        loading = ProgressDialog.show(ChangeProfile.this, null, "Please wait...", true, false);

        String token = "Bearer " + user.getToken();
        String name = etSName.getText().toString().trim();
        String email = etSEmail.getText().toString().trim();
        String username = etSUsername.getText().toString().trim();
        retrofit2.Call<ResponseProfile> call = RetrofitClient.getInstance().getApi().updateProfil(token, name, email, username, idSchool);
        call.enqueue(new Callback<ResponseProfile>() {
            @Override
            public void onResponse(retrofit2.Call<ResponseProfile> call, Response<ResponseProfile> response) {
                ResponseProfile profileResponse = response.body();
                if (response.isSuccessful()) {
                    if (profileResponse.getStatus().equals("success")) {
                        Log.i("debug", "onResponse: SUCCESS");
                        loading.dismiss();
                        loadData();
                        Toast.makeText(ChangeProfile.this, "Setting Success!", Toast.LENGTH_LONG).show();
                        startActivity(new Intent(ChangeProfile.this, Main2Activity.class));
                    } else {
                        Toast.makeText(ChangeProfile.this, response.body().getMessage() + "", Toast.LENGTH_LONG).show();
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
                        Toast.makeText(ChangeProfile.this, errors.toString(), Toast.LENGTH_LONG).show();
                    } catch (Exception e) {
                        Toast.makeText(ChangeProfile.this, e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
            }
            @Override
            public void onFailure(retrofit2.Call<ResponseProfile> call, Throwable t) {
                Log.e("debug", "onFailure: ERROR > " + t.getMessage());
                loading.dismiss();
                Toast.makeText(ChangeProfile.this, R.string.something_wrong, Toast.LENGTH_LONG).show();
            }
        });
    }

    public void loadData() {
        String token = "Bearer " + user.getToken();
        Call<ResponseDetails> call = RetrofitClient.getInstance().getApi().detail("application/json", token);
        call.enqueue(new Callback<ResponseDetails>() {
            @Override
            public void onResponse(Call<ResponseDetails> call, Response<ResponseDetails> response) {
                if (response.isSuccessful()) {
                    //   SharedPrefManager.getInstance(ChangeProfile.this).saveUser(ResponseBody.getUser());

                    SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(ChangeProfile.this);
                    SharedPreferences.Editor editorList = sharedPrefs.edit();
                    Gson gson = new Gson();

                    String detailUser = gson.toJson(response.body());
                    editorList.putString("DetailUser", detailUser);
                    editorList.commit();

                    Intent intent = new Intent(ChangeProfile.this, Main2Activity.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(ChangeProfile.this, R.string.something_wrong, Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseDetails> call, Throwable t) {
                Toast.makeText(ChangeProfile.this, R.string.something_wrong, Toast.LENGTH_LONG).show();
            }
        });
    }
    private void selectImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, IMG_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == IMG_REQUEST && resultCode == RESULT_OK && data != null) {
            Uri path = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), path);

                if (bitmap.getWidth() >= bitmap.getHeight()) {

                    dstBmp = Bitmap.createBitmap(
                            bitmap,
                            bitmap.getWidth() / 2 - bitmap.getHeight() / 2,
                            0,
                            bitmap.getHeight(),
                            bitmap.getHeight()
                    );

                } else {
                    dstBmp = Bitmap.createBitmap(
                            bitmap,
                            0,
                            bitmap.getHeight() / 2 - bitmap.getWidth() / 2,
                            bitmap.getWidth(),
                            bitmap.getWidth()
                    );
                }
                imgProfile.setImageDrawable(new BitmapDrawable(bitmap));
                // uploadProfile.setVisibility(View.VISIBLE);
                uploadFoto();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public String imgToString() {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        dstBmp.compress(Bitmap.CompressFormat.JPEG, 50, byteArrayOutputStream);
        byte[] imgByte = byteArrayOutputStream.toByteArray();
        //    bt_uploadFoto.setEnabled(true);
        return Base64.encodeToString(imgByte, Base64.DEFAULT);
    }

    public void uploadFoto() {
        UserModel user = SharedPrefManager.getInstance(this).getUser();
        String token = "Bearer " + user.getToken();

        progress = ProgressDialog.show(ChangeProfile.this, null, "Loading ...", true, false);

        String image = imgToString();
        retrofit2.Call<ResponseProfile> call = RetrofitClient.getInstance().getApi().updateAvatar(token, image);
        call.enqueue(new Callback<ResponseProfile>() {
            @Override
            public void onResponse(retrofit2.Call<ResponseProfile> call, Response<ResponseProfile> response) {
                if (response.isSuccessful()) {
                    //getDetails();
                    progress.dismiss();
                    updateData();
                    Toast.makeText(ChangeProfile.this, "Foto Profil Berganti", Toast.LENGTH_LONG).show();

                } else {
                    progress.dismiss();
                    Toast.makeText(ChangeProfile.this, response.code() + "Ukuran Foto Terlalu Besar", Toast.LENGTH_LONG).show();

                    //        Toast.makeText(ChangeProfile.this, response.errorBody().toString(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(retrofit2.Call<ResponseProfile> call, Throwable t) {
                progress.dismiss();
                Toast.makeText(ChangeProfile.this, t.toString(), Toast.LENGTH_LONG).show();
                Toast.makeText(ChangeProfile.this, "Ukuran Foto Terlalu Besar 2", Toast.LENGTH_LONG).show();

            }
        });
    }
    public void updateData() {
        UserModel user = SharedPrefManager.getInstance(this).getUser();
        if (user.getToken() != null) {
            String token = "Bearer " + user.getToken();

            retrofit2.Call<ResponseDetails> call = RetrofitClient.getInstance().getApi().detail("application/json", token);
            call.enqueue(new Callback<ResponseDetails>() {
                @Override
                public void onResponse(retrofit2.Call<ResponseDetails> call, Response<ResponseDetails> response) {
                    if (response.isSuccessful()) {

                        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(ChangeProfile.this);
                        SharedPreferences.Editor editorList = sharedPrefs.edit();
                        Gson gson = new Gson();

                        String detailUser = gson.toJson(response.body());
                        editorList.putString("DetailUser", detailUser);
                        editorList.commit();

                        SharedPrefManager.getInstance(ChangeProfile.this).saveDetail(response.body().getUser());
                        Intent intent = new Intent(ChangeProfile.this, Main2Activity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                    } else {
                        Toast.makeText(ChangeProfile.this, R.string.something_wrong, Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onFailure(retrofit2.Call<ResponseDetails> call, Throwable t) {
                    Toast.makeText(ChangeProfile.this, R.string.something_wrong, Toast.LENGTH_LONG).show();

                }
            });
        }
    }


}