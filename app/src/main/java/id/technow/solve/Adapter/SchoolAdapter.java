package id.technow.solve.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.TextView;

import id.technow.solve.ChangeProfile;
import id.technow.solve.Model.SchoolsModel;
import com.technow.solve.R;
import id.technow.solve.RegisterActivity;
import id.technow.solve.SchoolSearchFragment;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

public class SchoolAdapter extends RecyclerView.Adapter<SchoolAdapter.SchoolVH> {
    private int mSelectedItem = -1;
    private List<SchoolsModel> schoolsModels;
    private Context mCtx;
    SchoolSearchFragment fragment;

    public SchoolAdapter(List<SchoolsModel> schoolsModels, Context mCtx, SchoolSearchFragment fragment) {
        this.schoolsModels = schoolsModels;
        this.mCtx = mCtx;
        this.fragment = fragment;
    }

    @NonNull
    @Override
    public SchoolVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_schools, parent, false);
        SchoolVH holder = new SchoolVH(view);
        return new SchoolVH(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final SchoolVH holder, final int position) {
        final SchoolsModel schools = schoolsModels.get(position);

        holder.tvSchool.setText(schools.getText());
        if (position == mSelectedItem) {
            holder.placeA.setCardBackgroundColor(Color.parseColor("#64b5f6"));
            if (mCtx instanceof RegisterActivity) {
                ((RegisterActivity) mCtx).setTextSch(schools.getText(), schools.getId());
                fragment.hideState();
            }
            if (mCtx instanceof ChangeProfile) {
                ((ChangeProfile) mCtx).setTextSch(schools.getText(), schools.getId());
                fragment.hideState();
            }
        } else {

        }

    }

    @Override
    public int getItemCount() {
        return schoolsModels.size();
    }

    public class SchoolVH extends RecyclerView.ViewHolder {
        TextView tvSchool;
        RadioButton rbChoose;
        public int id;
        public String school;
        CardView placeA;

        public SchoolVH(@NonNull View itemView) {
            super(itemView);
            tvSchool = itemView.findViewById(R.id.tvSchool);
            rbChoose = itemView.findViewById(R.id.rbChoose);
            placeA = itemView.findViewById(R.id.placeA);

            View.OnClickListener l = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mSelectedItem = getAdapterPosition();
                    notifyItemRangeChanged(0, schoolsModels.size());
                    notifyDataSetChanged();

                }
            };

            rbChoose.setOnClickListener(l);
            itemView.setOnClickListener(l);
            placeA.setOnClickListener(l);
        }
    }


}
