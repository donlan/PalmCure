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
import dong.lan.palmcure.model.Appointment;

/**
 * Created by 梁桂栋 on 2017/4/19.
 * Email: 760625325@qq.com
 * Github: github.com/donlan
 */

public class PatientAppointmentAdapter extends RecyclerView.Adapter<PatientAppointmentAdapter.ViewHolder> {

    private List<Appointment> contracts;

    private BaseItemClickListener<Appointment> clickListener;

    public PatientAppointmentAdapter(List<Appointment> Users, BaseItemClickListener<Appointment> clickListener) {
        this.contracts = Users;
        this.clickListener = clickListener;
        if (contracts == null)
            contracts = new ArrayList<>();
    }


    public void addAll(List<Appointment> contracts) {
        this.contracts.addAll(contracts);
        notifyDataSetChanged();
    }

    public void clearAddAll(List<Appointment> contracts) {
        this.contracts.clear();
        this.contracts.addAll(contracts);
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_appoint, null);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Appointment appointment = contracts.get(position);
        holder.username.setText(appointment.user.getDisplayName());
        holder.intro.setText(appointment.user.phone);
        holder.status.setText(appointment.getStatusDisplay());
        holder.time.setText("预约时间：" + appointment.booktime);
    }

    @Override
    public int getItemCount() {
        return contracts == null ? 0 : contracts.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        TextView username;
        TextView intro;
        LabelTextView status;
        LabelTextView time;

        public ViewHolder(View itemView) {
            super(itemView);
            username = (TextView) itemView.findViewById(R.id.name);
            intro = (TextView) itemView.findViewById(R.id.reason);
            status = (LabelTextView) itemView.findViewById(R.id.status);
            time = (LabelTextView) itemView.findViewById(R.id.time);
            if(clickListener!=null) {
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int p = getLayoutPosition();
                        clickListener.onClick(contracts.get(p),0,p);
                    }
                });
                time.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int p = getLayoutPosition();
                        clickListener.onClick(contracts.get(p),1,p);
                    }
                });
            }
        }
    }
}
