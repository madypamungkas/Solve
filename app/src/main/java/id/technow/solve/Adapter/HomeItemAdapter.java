package id.technow.solve.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import id.technow.solve.Model.MenuHomeModel;

import id.technow.solve.R;
import id.technow.solve.TypeChooseActivity;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class HomeItemAdapter extends RecyclerView.Adapter<HomeItemAdapter.ViewHolder> {
    private List<MenuHomeModel> models;
    private Context mCtx;

    public HomeItemAdapter(List<MenuHomeModel> models, Context mCtx) {
        this.models = models;
        this.mCtx = mCtx;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_home, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        final MenuHomeModel menu = models.get(position);

        holder.titleMenu.setText(menu.getName());
        holder.layoutCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(mCtx, TypeChooseActivity.class);
                i.putExtra("idCategory", menu.getId());
                i.putExtra("picture", menu.getPic_url_2());
                i.putExtra("desc", menu.getDescription());
                i.putExtra("Type", menu.getName());
                mCtx.startActivity(i);

            }
        });
        String link = "https://solve.technow.id/storage/quiz_category/";
        Picasso.get().load(link + menu.getId()).into(new Target() {
            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                holder.layoutCard.setBackground(new BitmapDrawable(bitmap));
            }

            @Override
            public void onBitmapFailed(Exception e, Drawable errorDrawable) {

            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {

            }
        });

        holder.textTitle = models.get(position).getName();
        if (holder.textTitle.length() > 7) {
            holder.titleMenu.setTextSize(TypedValue.COMPLEX_UNIT_PX , mCtx.getResources().getDimension(R.dimen._12ssp));
        } else if (holder.textTitle.length() > 8) {
            holder.titleMenu.setTextSize(TypedValue.COMPLEX_UNIT_PX , mCtx.getResources().getDimension(R.dimen._11ssp));
        }
    }

    @Override
    public int getItemCount() {
        return models.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView titleMenu;
        LinearLayout layoutCard;
        FrameLayout.LayoutParams params;
        String textTitle;

        public ViewHolder(View itemView) {
            super(itemView);
            titleMenu = itemView.findViewById(R.id.titleMenu);
            layoutCard = itemView.findViewById(R.id.layoutCard);
            // layoutCard = new LinearLayout(mCtx);
            params = (FrameLayout.LayoutParams) layoutCard.getLayoutParams();
        }
    }
}
