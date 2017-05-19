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
 */

public class QuestionAdapter extends RecyclerView.Adapter<QuestionAdapter.ViewHolder> {

    public static final int MODE_SELECT = 1;
    public static final int MODE_NONE = 0;
    public static int MODE = MODE_NONE;

    private List<Question> questions;
    private Map<String,Question> map;

    //构造函数，就是初始化列表数据
    public QuestionAdapter(List<Question> questions) {
        this.questions  = new ArrayList<>(questions);
        if(MODE == MODE_SELECT)
            map = new HashMap<>();
    }

    //生成布局文件，也就是控制内容怎么显示的
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_question, null));
    }

    //将列表的数据绑定到页面上
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

    //点击一个内容后，发送给处理者处理
    public void setClickListener(BaseItemClickListener<Question> clickListener) {
        this.clickListener = clickListener;
    }

    //列表有多少内容，控制列表显示的内容个数
    @Override
    public int getItemCount() {
        return questions == null ? 0 : questions.size();
    }

    //重置列表
    public void reset(List<Question> questions) {
        this.questions.clear();
        this.questions.addAll(questions);
        notifyDataSetChanged();
    }

    //显示页面的空间初始化类
    class ViewHolder extends RecyclerView.ViewHolder {
        TextView desc;
        TextView option;
        TextView answer;
        CheckBox select;

        public ViewHolder(View itemView) {
            super(itemView);
            //初始化空间
            desc = (TextView) itemView.findViewById(R.id.item_question_desc);
            option = (TextView) itemView.findViewById(R.id.item_question_key);
            answer = (TextView) itemView.findViewById(R.id.item_question_answer);
            select = (CheckBox) itemView.findViewById(R.id.item_question_select);
            //设置点击事件
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
