package id.technow.solve.Storage;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.google.gson.Gson;
import id.technow.solve.Model.DetailUser;
import id.technow.solve.Model.UserModel;
import id.technow.solve.Model.VersionModel;

public class SharedPrefManager {  public static final String SHARED_PREF_NAME = "user_shared_pref";
    private static SharedPrefManager mInstance;
    private Context mCtx;

    private SharedPrefManager(Context mCtx) {
        this.mCtx = mCtx;

    }

    public static synchronized SharedPrefManager getInstance(Context mCtx) {
        if (mInstance == null) {
            mInstance = new SharedPrefManager(mCtx);
        }
        return mInstance;
    }


    public void saveUser(UserModel user){
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("id", user.getId());
        editor.putString("name", user.getName());
        editor.putString("username", user.getUsername());
        editor.putString("email", user.getEmail());
        editor.putString("email_verified_at", user.getEmail_verified_at());
        editor.putString("picture", user.getPicture());
        editor.putString("created_at", user.getCreated_at());
        editor.putString("updated_at", user.getUpdated_at());
        editor.putString("deleted_at", user.getDeleted_at());
        editor.putString("school_id", user.getSchool_id());
        editor.putString("phone_number", user.getPhone_number());
        editor.putString("token", user.getToken());
        editor.commit();
        editor.apply();
    }
    public void saveAnswerChance(int chance){
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("chance", chance);
        editor.commit();
        editor.apply();
    }

    public void saveDetail(DetailUser detailUser) {
        Gson gson = new Gson();
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();


        editor.putString("id", detailUser.getId());
        editor.putString("name", detailUser.getName());
        editor.putString("username", detailUser.getUsername());
        editor.putString("email", detailUser.getEmail());
        editor.putString("email_verified_at", detailUser.getEmail_verified_at());
        editor.putString("picture", detailUser.getPicture());
        editor.putString("created_at", detailUser.getCreated_at());
        editor.putString("updated_at", detailUser.getUpdated_at());
        editor.putString("deleted_at", detailUser.getDeleted_at());
        editor.putString("school_id", detailUser.getSchool_id());
        editor.putString("count_played", detailUser.getCount_played());
        editor.putString("high_score", detailUser.getHigh_score());
        String collager = gson.toJson(detailUser.getCollager());
        editor.putString("collager", collager);
        String school = gson.toJson(detailUser.getSchool());
        editor.putString("school", school);


        editor.apply();
    }

    /*public DetailUser detailUser(){
        Gson gson = new Gson();

        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return new DetailUser(
                sharedPreferences.getString("id", null),
                sharedPreferences.getString("name", null),
                sharedPreferences.getString("username", null),
                sharedPreferences.getString("email", null),
                sharedPreferences.getString("email_verified_at", null),
                sharedPreferences.getString("picture", null),
                sharedPreferences.getString("created_at", null),
                sharedPreferences.getString("updated_at", null),
                sharedPreferences.getString("deleted_at", null),
                sharedPreferences.getString("school_id", null),
                sharedPreferences.getString("count_played", null),
                sharedPreferences.getString("high_score", null),
                sharedPreferences.getClass< CollagerModel >,
        sharedPreferences.getClass< DetailSchoolModel >

        );
    }*/

    public void saveVersion(VersionModel version){
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString("id_ver", version.getId());
        editor.putString("version", version.getVersion());
        editor.putString("sub_version", version.getSub_version());
        editor.putString("year", version.getYear());
        editor.putString("created_at", version.getCreated_at());
        editor.putString("updated_at", version.getUpdated_at());
        editor.apply();

    }

    public VersionModel versionModel(){
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return new VersionModel(
                sharedPreferences.getString("id_ver", null),
                sharedPreferences.getString("version", null),
                sharedPreferences.getString("sub_version", null),
                sharedPreferences.getString("year", null),
                sharedPreferences.getString("created_at", null),
                sharedPreferences.getString("updated_at", null)

        );
    }
    public boolean isLoggedIn(){
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString("id", null) != null;
    }

    public UserModel getUser() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return new UserModel(
                sharedPreferences.getString("id", null),
                sharedPreferences.getString("name", null),
                sharedPreferences.getString("username", null),
                sharedPreferences.getString("email", null),
                sharedPreferences.getString("email_verified_at", null),
                sharedPreferences.getString("picture", null),
                sharedPreferences.getString("created_at", null),
                sharedPreferences.getString("updated_at", null),
                sharedPreferences.getString("deleted_at", null),
                sharedPreferences.getString("school_id", null),
                sharedPreferences.getString("phone_number", null),
                sharedPreferences.getString("token", null)
        );
    }
    public int getChance(){
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getInt("chance", 3);
    }


    public void clear() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.commit();
        editor.apply();

        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(mCtx);
        SharedPreferences.Editor editorList = sharedPrefs.edit();
        editorList.clear();
        editor.apply();
    }
}
