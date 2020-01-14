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

import com.komsi.solve.LeaderboardActivity;
import com.komsi.solve.LeaderboardChooseType;
import com.komsi.solve.Model.MenuHomeModel;
import com.komsi.solve.Model.TypeListModel;
import com.komsi.solve.R;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

public class LeaderboardChooseAdapter extends RecyclerView.Adapter<LeaderboardChooseAdapter.ViewHolder> {
    Context mCtx;
    ArrayList<TypeListModel> categories;
    List<String> colors;

    public LeaderboardChooseAdapter(Context mCtx, ArrayList<TypeListModel> categories) {
        this.mCtx = mCtx;
        this.categories = categories;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.quiz_type_list_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        final TypeListModel category = categories.get(position);

        holder.typeGame.setText(category.getName());
        holder.desc.setText(category.getDescription());

        String link = "https://solve.technow.id/storage/quiz_category/";
        Picasso.get().load(link + category.getId()).into(new Target() {
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

        colors = new ArrayList<String>();

        colors.add("#d32f2f");
        colors.add("#d81b60");
        colors.add("#ab47bc");
        colors.add("#7e57c2");
        colors.add("#5c6bc0");
        colors.add("#1976d2");
        colors.add("#00796b");
        colors.add("#2e7d32");
        colors.add("#ff6f00");
        colors.add("#ff6d00");
        colors.add("#8d6e63");
        colors.add("#757575");
        colors.add("#546e7a");
        colors.add("#c0ca33");

        Random r = new Random();


        int i1 = r.nextInt(13 - 1) + 1;
        holder.cardGame.setCardBackgroundColor(Color.parseColor(colors.get(i1)));

        holder.btnQuiz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(mCtx, LeaderboardActivity.class);
                intent.putExtra("idtype", 1);
                mCtx.startActivity(intent);

            }
        });
    }

    @Override
    public int getItemCount() {
        return categories.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        TextView typeGame, desc;
        LinearLayout btnQuiz;
        CardView cardGame;
        ImageView imgGame;

        public ViewHolder(View itemView) {
            super(itemView);
            cardGame = itemView.findViewById(R.id.cardGame);
            typeGame = itemView.findViewById(R.id.typeGame);
            desc = itemView.findViewById(R.id.desc);
            btnQuiz = itemView.findViewById(R.id.btnQuiz);
            imgGame = itemView.findViewById(R.id.imgGame);

        }
    }
}
