package com.komsi.solve.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.komsi.solve.Api.RetrofitClient;
import com.komsi.solve.Model.CategoryModel;
import com.komsi.solve.Model.ListSoalModel;
import com.komsi.solve.Model.ResponseListSoal;
import com.komsi.solve.Model.UserModel;
import com.komsi.solve.QuizChooseActivity;
import com.komsi.solve.R;
import com.komsi.solve.Storage.SharedPrefManager;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TypeGameAdapter extends RecyclerView.Adapter<TypeGameAdapter.TypeVH> {
    Context mCtx;
    ArrayList<CategoryModel> categories;

    public TypeGameAdapter(Context mCtx, ArrayList<CategoryModel> categories) {
        this.mCtx = mCtx;
        this.categories = categories;
    }

    @NonNull
    @Override
    public TypeVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.quiz_type_list_layout, parent, false);
        TypeVH holder = new TypeVH(view);
        return new TypeVH(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final TypeVH holder, int position) {
        final CategoryModel type = categories.get(position);
        holder.typeGame.setText(type.getName());
        holder.desc.setText(type.getDescription());

        String link = "http://solve.technow.id/storage/quiz_type/";
        Picasso.get().load(link + type.getId()).into(new Target() {
            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                holder.imgGame.setImageDrawable(new BitmapDrawable(bitmap));
            }

            @Override
            public void onBitmapFailed(Exception e, Drawable errorDrawable) {

            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {

            }
        });

        Picasso.get().load(link + type.getPic_url()).into(new Target() {
            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                holder.imgCategory.setImageDrawable(new BitmapDrawable(bitmap));
            }

            @Override
            public void onBitmapFailed(Exception e, Drawable errorDrawable) {

            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {

            }
        });

        holder.btnQuiz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UserModel user = SharedPrefManager.getInstance(mCtx).getUser();
                String token = "Bearer " + user.getToken();

                Call<ResponseListSoal> call = RetrofitClient.getInstance().getApi().quizList("application/json", token, type.getId());
                call.enqueue(new Callback<ResponseListSoal>() {
                    @Override
                    public void onResponse(Call<ResponseListSoal> call, Response<ResponseListSoal> response) {
                        if (response.isSuccessful()) {
                            //Toast.makeText(Hangeul_Start.this, response.code()+"", Toast.LENGTH_SHORT).show();

                            ArrayList<ListSoalModel> listSoal = response.body().getResult();
                            if (response.body().getResult() != null) {
                               /* ListSoalModel model = listSoal.get(0);
                                int idSoal = model.getId();*/
                                Intent intent = new Intent(mCtx, QuizChooseActivity.class);
                                intent.putExtra("category", type.getQuiz_category_id() + "");
                                intent.putExtra("idType", type.getId());
                                intent.putExtra("namaSoal", type.getName());
                                mCtx.startActivity(intent);
                            } else {
                                Toast.makeText(mCtx, "Game Is Unavailable", Toast.LENGTH_SHORT).show();

                            }
                        } else {
                            Toast.makeText(mCtx, response.errorBody() + "-", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseListSoal> call, Throwable t) {
                        Toast.makeText(mCtx, t.toString(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    @Override
    public int getItemCount() {
        return categories.size();
    }

    class TypeVH extends RecyclerView.ViewHolder {
        TextView typeGame, desc;
        LinearLayout btnQuiz;
        CardView cardGame;
        ImageView imgGame, imgCategory;

        public TypeVH(@NonNull View itemView) {
            super(itemView);
            cardGame = itemView.findViewById(R.id.cardGame);
            typeGame = itemView.findViewById(R.id.typeGame);
            desc = itemView.findViewById(R.id.desc);
            btnQuiz = itemView.findViewById(R.id.btnQuiz);
            imgGame = itemView.findViewById(R.id.imgGame);
            imgCategory = itemView.findViewById(R.id.imgCategory);
        }
    }
}
