package dong.lan.palmcure.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import dong.lan.base.BaseItemClickListener;
import dong.lan.library.LabelTextView;
import dong.lan.palmcure.R;
import dong.lan.palmcure.model.User;

/**
 * Created by 梁桂栋 on 2017/4/19.
 * Email: 760625325@qq.com
 * Github: github.com/donlan
 */

public class AllDoctorAdapter extends RecyclerView.Adapter<AllDoctorAdapter.ViewHolder> {

    private List<User> contracts;

    private BaseItemClickListener<User> clickListener;

    public AllDoctorAdapter(List<User> Users, BaseItemClickListener<User> clickListener) {
        this.contracts = Users;
        this.clickListener = clickListener;
        if (contracts == null)
            contracts = new ArrayList<>();
    }

    public void addAll(List<User> contracts) {
        this.contracts.addAll(contracts);
        notifyDataSetChanged();
    }

    public void clearAddAll(List<User> contracts) {
        this.contracts.clear();
        this.contracts.addAll(contracts);
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_doctor, null);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        User user = contracts.get(position);
        holder.username.setText(user.username);
        holder.intro.setText(user.phone);
    }

    @Override
    public int getItemCount() {
        return contracts == null ? 0 : contracts.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        TextView username;
        TextView intro;
        LabelTextView status;

        public ViewHolder(View itemView) {
            super(itemView);
            username = (TextView) itemView.findViewById(R.id.doctor_name);
            intro = (TextView) itemView.findViewById(R.id.doctor_intro);
            status = (LabelTextView) itemView.findViewById(R.id.status);
            status.setVisibility(View.GONE);
            if(clickListener!=null) {
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int p = getLayoutPosition();
                        clickListener.onClick(contracts.get(p),0,p);
                    }
                });
            }
        }
    }
}
