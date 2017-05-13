package dong.lan.palmcure.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import dong.lan.base.BaseItemClickListener;
import dong.lan.palmcure.R;
import dong.lan.palmcure.model.Question;

/**
 * Created by 梁桂栋 on 2017/5/10.
 * Email: 760625325@qq.com
 * Github: github.com/donlan
 */

public class QuestionAdapter extends RecyclerView.Adapter<QuestionAdapter.ViewHolder> {

    public static final int MODE_SELECT = 1;
    public static final int MODE_NONE = 0;
    public static int MODE = MODE_NONE;

    private List<Question> questions;
    private Map<String,Question> map;

    public QuestionAdapter(List<Question> questions) {
        this.questions  = new ArrayList<>(questions);
        if(MODE == MODE_SELECT)
            map = new HashMap<>();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_question, null));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Question question = questions.get(position);
        holder.desc.setText(question.qdescribe);
        holder.option.setText(question.getOptionString());
        holder.answer.setText(question.answer);
    }


    public List<Question> getSelect(){
        if(map == null)
            return null;
        List<Question> questionList = new ArrayList<>();
        Set<String> keys = map.keySet();
        for (String k : keys){
            questionList.add(map.get(k));
        }
        return questionList;
    }


    private BaseItemClickListener<Question> clickListener;

    public void setClickListener(BaseItemClickListener<Question> clickListener) {
        this.clickListener = clickListener;
    }

    @Override
    public int getItemCount() {
        return questions == null ? 0 : questions.size();
    }

    public void reset(List<Question> questions) {
        this.questions.clear();
        this.questions.addAll(questions);
        notifyDataSetChanged();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView desc;
        TextView option;
        TextView answer;
        CheckBox select;

        public ViewHolder(View itemView) {
            super(itemView);
            desc = (TextView) itemView.findViewById(R.id.item_question_desc);
            option = (TextView) itemView.findViewById(R.id.item_question_key);
            answer = (TextView) itemView.findViewById(R.id.item_question_answer);
            select = (CheckBox) itemView.findViewById(R.id.item_question_select);
            if(clickListener!=null) {
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int p = getLayoutPosition();
                        clickListener.onClick(questions.get(p),1,p);
                    }
                });
            }

            if(MODE == MODE_NONE){

            }else{
                select.setVisibility(View.VISIBLE);
                select = (CheckBox) itemView.findViewById(R.id.item_question_select);
                select.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        int p = getLayoutPosition();
                        Question question = questions.get(p);
                        if(isChecked){
                            map.put(question.id,question);
                        }else {
                            map.remove(question.id);
                        }
                    }
                });
            }
        }
    }
}
