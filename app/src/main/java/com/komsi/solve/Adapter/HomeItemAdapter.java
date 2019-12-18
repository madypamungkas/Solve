package com.komsi.solve.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.komsi.solve.Model.MenuHomeModel;
import com.komsi.solve.R;
import com.komsi.solve.TypeChooseActivity;

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
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final MenuHomeModel menu = models.get(position);
        //holder.layoutCard.setBackgroundResource(menu.getImage());
        // holder.params.height = menu.getMinHeight();
        holder.titleMenu.setText(menu.getName());
        holder.layoutCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(mCtx, TypeChooseActivity.class);
                i.putExtra("idCategory", menu.getId());
                i.putExtra("Type", menu.getName());

                mCtx.startActivity(i);

            }
        });
    }

    @Override
    public int getItemCount() {
        return models.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView titleMenu;
        LinearLayout layoutCard;
        FrameLayout.LayoutParams params;

        public ViewHolder(View itemView) {
            super(itemView);
            titleMenu = itemView.findViewById(R.id.titleMenu);
            layoutCard = itemView.findViewById(R.id.layoutCard);
            // layoutCard = new LinearLayout(mCtx);
            params = (FrameLayout.LayoutParams) layoutCard.getLayoutParams();

        }
    }
}
