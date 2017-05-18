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
import dong.lan.palmcure.model.Contract;

/**
 */

public class PatientContractAdapter extends RecyclerView.Adapter<PatientContractAdapter.ViewHolder> {

    private List<Contract> contracts;

    private BaseItemClickListener<Contract> clickListener;

    public PatientContractAdapter(List<Contract> Users, BaseItemClickListener<Contract> clickListener) {
        this.contracts = Users;
        this.clickListener = clickListener;
        if(contracts == null)
            contracts = new ArrayList<>();
    }


    public void addAll(List<Contract> contracts){
        this.contracts.addAll(contracts);
        notifyDataSetChanged();
    }

    public void clearAddAll(List<Contract> contracts){
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
        Contract contract = contracts.get(position);
        holder.username.setText(contract.user.username);
        holder.intro.setText(contract.user.phone);
        holder.status.setText(contract.getStatusDisplay());
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
