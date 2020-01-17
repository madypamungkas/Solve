package id.technow.solve.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import id.technow.solve.Api.RetrofitClient;
import id.technow.solve.LeaderboardChoose;
import id.technow.solve.LeaderboardChooseType;
import id.technow.solve.Model.CategoryModel;
import id.technow.solve.Model.ListSoalModel;
import id.technow.solve.Model.MenuHomeModel;
import id.technow.solve.Model.ResponseListSoal;
import id.technow.solve.Model.UserModel;
import id.technow.solve.R;
import id.technow.solve.Storage.SharedPrefManager;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LeadChooseCategoryAdapter extends RecyclerView.Adapter<LeadChooseCategoryAdapter.LeaCategory >{

    Context mCtx;
    ArrayList<MenuHomeModel> categories;

    public LeadChooseCategoryAdapter(Context mCtx, ArrayList<MenuHomeModel> categories) {
        this.mCtx = mCtx;
        this.categories = categories;
    }

    @NonNull
    @Override
    public LeaCategory onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.quiz_type_list_layout, parent, false);
        LeaCategory holder = new LeaCategory(view);
        return new LeaCategory(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final LeaCategory holder, int position) {
        final MenuHomeModel type = categories.get(position);
        holder.typeGame.setText(type.getName());
        holder.desc.setText(" ");
        String link = "http://solve.technow.id/storage/quiz_category2/";
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
                Intent intent = new Intent(mCtx, LeaderboardChooseType.class);
                intent.putExtra("idCategory", type.getId());
                intent.putExtra("namaSoal", type.getName());
                mCtx.startActivity(intent);
                /*Call<ResponseListSoal> call = RetrofitClient.getInstance().getApi().quizList("application/json", token, type.getId());
                call.enqueue(new Callback<ResponseListSoal>() {
                    @Override
                    public void onResponse(Call<ResponseListSoal> call, Response<ResponseListSoal> response) {
                        if (response.isSuccessful()) {
                            if (response.body().getResult() != null) {

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
                });*/
            }
        });
    }

    @Override
    public int getItemCount() {
        return categories.size();
    }

    class LeaCategory extends RecyclerView.ViewHolder {
        TextView typeGame, desc;
        LinearLayout btnQuiz;
        CardView cardGame;
        ImageView imgGame, imgCategory;
        public LeaCategory(@NonNull View itemView) {
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
