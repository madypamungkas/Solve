package id.technow.solve;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import id.technow.solve.Api.RetrofitClient;
import id.technow.solve.Model.QuestionModel;
import id.technow.solve.Model.ResponseQuestion;
import id.technow.solve.Model.UserModel;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.button.MaterialButton;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import id.technow.solve.Adapter.NavigationAdapter;

import id.technow.solve.R;

import id.technow.solve.Storage.SharedPrefManager;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;


public class NavigationFragment extends BottomSheetDialogFragment implements View.OnClickListener {

    RecyclerView optionRV;
    NavigationAdapter adapter;
    MaterialButton btnDone;
    int idSoal;
    String namaSoal;
    List<QuestionModel> questionModels;

    @Override

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View fragmentView = inflater.inflate(R.layout.fragment_navigation, container, false);

        idSoal = getArguments().getInt("idSoal", 1);
        namaSoal = getArguments().getString("namaSoal", "Solve");
        btnDone = fragmentView.findViewById(R.id.btnDone);
        optionRV = fragmentView.findViewById(R.id.optionRV);

        btnDone.setOnClickListener(this);


        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        Gson gson = new Gson();
        String json = sharedPrefs.getString("response", "response");
        //  String json = getArguments().getString("question");
        Type type = new TypeToken<ResponseQuestion>() {
        }.getType();
        ResponseQuestion responseQuestion = gson.fromJson(json, type);
        ArrayList<QuestionModel> questionModels = responseQuestion.getQuestion();


        StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(3, LinearLayoutManager.VERTICAL);
        adapter = new NavigationAdapter(questionModels, getActivity());
        optionRV.setLayoutManager(new LinearLayoutManager(getActivity()));
        optionRV.setLayoutManager(staggeredGridLayoutManager);
        optionRV.setAdapter(adapter);
      //  loadSoal();
        return fragmentView;
    }

    public void loadSoal() {

        UserModel user = SharedPrefManager.getInstance(getActivity()).getUser();

        String token = "Bearer " + user.getToken();
        Call<ResponseQuestion> call = RetrofitClient.getInstance().getApi().question("application/json", token, idSoal);
        call.enqueue(new Callback<ResponseQuestion>() {
            @Override
            public void onResponse(Call<ResponseQuestion> call, final Response<ResponseQuestion> response) {
                ResponseQuestion questionResponse = response.body();
                if (response.isSuccessful()) {
                    if (response.body().getQuestion().size() != 0) {
                        questionModels = questionResponse.getQuestion();
                        StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(3, LinearLayoutManager.VERTICAL);
                        adapter = new NavigationAdapter(questionModels, getActivity());
                        optionRV.setLayoutManager(new LinearLayoutManager(getActivity()));
                        optionRV.setLayoutManager(staggeredGridLayoutManager);
                        optionRV.setAdapter(adapter);
                    } else {
                    }
                } else {
                    Log.i("debug", "onResponse : FAILED");
                    Toast.makeText(getActivity(), R.string.something_wrong, Toast.LENGTH_LONG).show();

                }
            }

            @Override
            public void onFailure(Call<ResponseQuestion> call, Throwable t) {
                Log.i("debug", "onResponse : FAILED");
                Toast.makeText(getActivity(), R.string.something_wrong, Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnDone:
                Intent readyBtn = new Intent(getActivity(), ReviewActivity.class);
                readyBtn.putExtra("idSoal", idSoal);
                readyBtn.putExtra("namaSoal", namaSoal);
                startActivity(readyBtn);
                break;
        }
    }
}
