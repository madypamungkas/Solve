package id.technow.solve.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import id.technow.solve.Model.LeaderboarModel;
import com.technow.solve.R;
import com.squareup.picasso.Picasso;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import de.hdodenhof.circleimageview.CircleImageView;


public class LeaderboardAdapter  extends RecyclerView.Adapter<LeaderboardAdapter.LeaderVH>{
    private Context mCtx;
    private List<LeaderboarModel> leaderboardModel;

    public LeaderboardAdapter(Context mCtx, List<LeaderboarModel> leaderboardModel) {
        this.mCtx = mCtx;
        this.leaderboardModel = leaderboardModel;
    }

    @NonNull
    @Override
    public LeaderVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mCtx).inflate(R.layout.list_leaderboard, parent, false);
        return new LeaderboardAdapter.LeaderVH(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LeaderVH holder, int position) {
        final LeaderboarModel leader = leaderboardModel.get(position);

        holder.number.setText((position+4)+"");
        holder.score.setText(""+leader.getTotal_score());
        holder.name.setText(""+leader.getUsername());
        String link = "https://solve.technow.id/storage/"+"user/"+ leader.getPicture();
        Picasso.get().load(link).error(R.drawable.ic_userprofile)
                .into(holder.avatar);

    }

    @Override
    public int getItemCount() {
        return  leaderboardModel.size();
    }

    public class LeaderVH extends RecyclerView.ViewHolder {
        TextView number, score, name;
        CircleImageView avatar;
        String string;
        public LeaderVH(View itemView) {
            super(itemView);
            string = itemView.getResources().getString(R.string.link);
            number = itemView.findViewById(R.id.number);
            score = itemView.findViewById(R.id.score);
            name = itemView.findViewById(R.id.name);
            avatar = itemView.findViewById(R.id.avatar);
        }
    }
}
