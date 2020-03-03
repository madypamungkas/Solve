package id.technow.solve.Storage;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPrefRate {
    public static final String SHARED_PREF_NAME = "rate_shared_pref";
    private static SharedPrefRate mInstance;
    private Context mCtx;

    private SharedPrefRate(Context mCtx) {
        this.mCtx = mCtx;

    }

    public static synchronized SharedPrefRate getInstance(Context mCtx) {
        if (mInstance == null) {
            mInstance = new SharedPrefRate(mCtx);
        }
        return mInstance;
    }
    public void saveRate(String rate){
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("rate", rate);
        editor.commit();
        editor.apply();
    }
    public void saveTry(int tryPlaying){
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("tryPlaying", tryPlaying);
        editor.commit();
        editor.apply();
    }

    public String getRate(){
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString("rate", "no");
    }
    public int getTryPlaying(){
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getInt("tryPlaying", 0);
    }


}
