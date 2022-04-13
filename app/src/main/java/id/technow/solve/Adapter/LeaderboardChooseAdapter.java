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

import id.technow.solve.LeaderboardActivity;
import id.technow.solve.Model.TypeListModel;
import id.technow.solve.R;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

public class LeaderboardChooseAdapter extends RecyclerView.Adapter<LeaderboardChooseAdapter.ViewHolder> {
    Context mCtx;
    ArrayList<TypeListModel> categories;

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

        holder.typeGame.setText(category.getTitle());
        holder.desc.setText(category.getDescription());

        String link = "http://185.210.144.115:8080/storage/quiz_category/";
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

        holder.btnQuiz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mCtx, LeaderboardActivity.class);
                intent.putExtra("idsoal", category.getId());
                intent.putExtra("namasoal", category.getTitle());
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
