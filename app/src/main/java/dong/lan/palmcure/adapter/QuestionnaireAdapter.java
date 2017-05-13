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
import dong.lan.palmcure.model.Questionnaire;

/**
 */

public class QuestionnaireAdapter extends RecyclerView.Adapter<QuestionnaireAdapter.ViewHolder> {


    private List<Questionnaire> questions;

    public QuestionnaireAdapter(List<Questionnaire> questions) {
        if (questions == null)
            this.questions = new ArrayList<>();
        else
            this.questions = new ArrayList<>(questions);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_questionnaire, null));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Questionnaire question = questions.get(position);
        holder.intro.setText(question.intro);
        holder.res.setText(question.getResultString());
        holder.status.setText(question.getStatus());
    }


    private BaseItemClickListener<Questionnaire> clickListener;

    public void setClickListener(BaseItemClickListener<Questionnaire> clickListener) {
        this.clickListener = clickListener;
    }

    @Override
    public int getItemCount() {
        return questions == null ? 0 : questions.size();
    }

    public void reset(List<Questionnaire> questions) {
        this.questions.clear();
        this.questions.addAll(questions);
        notifyDataSetChanged();
    }


    class ViewHolder extends RecyclerView.ViewHolder {
        TextView intro;
        TextView res;
        LabelTextView status;

        public ViewHolder(View itemView) {
            super(itemView);
            intro = (TextView) itemView.findViewById(R.id.item_questionnaire_intro);
            res = (TextView) itemView.findViewById(R.id.item_questionnaire_res);
            status = (LabelTextView) itemView.findViewById(R.id.item_questionnaire_status);

            if (clickListener != null) {
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int p = getLayoutPosition();
                        clickListener.onClick(questions.get(p), 1, p);
                    }
                });
            }


        }
    }
}
