package id.technow.solve.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import id.technow.solve.Model.BannerModel;
import com.technow.solve.R;
import id.technow.solve.WebViewsActivity;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class SliderViewAdapter extends com.smarteist.autoimageslider.SliderViewAdapter<SliderViewAdapter.SliderVH> {
    private Context mCtx;
    private ArrayList<BannerModel> bannerModels;

    public SliderViewAdapter(Context mCtx, ArrayList<BannerModel> bannerModels) {
        this.mCtx = mCtx;
        this.bannerModels = bannerModels;
    }

    @Override
    public SliderVH onCreateViewHolder(ViewGroup parent) {
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.image_slider_layout_item, null);
        return new SliderVH(inflate);}

    @Override
    public void onBindViewHolder(SliderVH viewHolder, int position) {
        final BannerModel banner = bannerModels.get(position);
        String link = "https://solve.technow.id/storage/";
        Picasso.get().load(link+"banner/"+banner.getId()).error(R.color.colorPrimary)
                .into(viewHolder.imageViewBackground);
        String linkTo = banner.getLinkTo();
        if(linkTo.length() > 5){
            viewHolder.mainBannerLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //                   Intent i = new Intent(Intent.ACTION_VIEW);
//                    i.setData(Uri.parse("http://"+banner.getLinkTo()));
                    Intent i = new Intent(mCtx, WebViewsActivity.class);
                    i.putExtra("BannerLink","https://"+banner.getLinkTo());
                    mCtx.startActivity(i);
                }
            });
        }
        else{

        }
    }




    @Override
    public int getCount() {
        return bannerModels.size();
    }

    class SliderVH extends com.smarteist.autoimageslider.SliderViewAdapter.ViewHolder{
        View itemView;
        ImageView imageViewBackground;
        FrameLayout mainBannerLayout;
        // TextView textViewDescription;
        public SliderVH(View itemView) {
            super(itemView);
            imageViewBackground = itemView.findViewById(R.id.iv_auto_image_slider);
            //textViewDescription = itemView.findViewById(R.id.tv_auto_image_slider);
            this.itemView = itemView;
            mainBannerLayout = itemView.findViewById(R.id.mainBannerLayout);
        }
    }
}
