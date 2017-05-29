package dong.lan.palmcure.adapter;

import android.support.annotation.IdRes;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import dong.lan.base.BaseItemClickListener;
import dong.lan.base.ui.base.Config;
import dong.lan.library.LabelTextView;
import dong.lan.palmcure.R;
import dong.lan.palmcure.UserManager;
import dong.lan.palmcure.model.Question;
import dong.lan.palmcure.model.Questionnaire;
import dong.lan.palmcure.model.Record;

/**
 */

public class QuestionHandlerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    private static final int TYPE_QUESTIONNAIRE = 0;
    private static final int TYPE_QUESTION = 1;
    private int count;
    private int score;
    private Questionnaire questionnaire;
    private List<Record> questions;
    private TextView scoreTv;

    public QuestionHandlerAdapter(Questionnaire questionnaire, List<Record> questions) {
        this.questions = new ArrayList<>(questions);
        this.questionnaire = questionnaire;
        calScore();
    }

    public int getLevel() {
        return ((int) score * 10 / count);
    }

    public void calScore() {
        count = 0;
        score = 0;
        for (Record record : questions) {
            record.questionObj.answer = "A";
            count += record.questionObj.getScore();
            record.questionObj.answer = record.answer;
            score += record.questionObj.getScore();
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_QUESTIONNAIRE) {
            return new QNHolder(LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_qn_handler, null));
        }
        return new ViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_question_handler, null));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        if (getItemViewType(position) == TYPE_QUESTIONNAIRE) {
            QNHolder holder = (QNHolder) viewHolder;
            holder.intro.setText(questionnaire.intro);

            holder.action.setText("查看问题");

            if (TextUtils.isEmpty(questionnaire.result)) {
                holder.result.setFocusable(true);
            } else {

                if (questionnaire.status == 0) {
                    holder.action.setText("等待审查");
                } else if (questionnaire.status == 1) {
                    holder.action.setText("开始答题");
                } else if (questionnaire.status == 2) {
                    holder.result.setFocusable(false);
                    int level = ((int) score * 10 / count);
                    if (questionnaire.level < level) {
                        holder.action.setText("问卷不合格");
                    } else {
                        holder.action.setText("问卷答题合格");
                    }
                    updateScore();
                }
                holder.result.setText(questionnaire.getResultString());
            }
        } else {
            ViewHolder holder = (ViewHolder) viewHolder;
            Record record = questions.get(position - 1);
            Question question = record.questionObj;
            holder.desc.setText(question.qdescribe);

            if (position == getItemCount() - 1) {
                if (UserManager.get().currentUser().type == Config.TYPE_PATIENT && questionnaire.status != Questionnaire.STATUS_DONE) {
                    holder.action.setText("提交");
                } else {
                    holder.action.setText("没有更多题目了");
                }
            }

            if (TextUtils.isEmpty(record.answer)) {

            } else {
                switch (record.answer) {
                    case "A":
                        holder.opA.setChecked(true);
                        break;
                    case "B":
                        holder.opB.setChecked(true);
                        break;
                    case "C":
                        holder.opC.setChecked(true);
                        break;
                }
            }
            holder.opA.setText(question.options.get("A"));
            holder.opB.setText(question.options.get("B"));
            holder.opC.setText(question.options.get("C"));
        }

    }


    private BaseItemClickListener<Record> clickListener;

    public void setClickListener(BaseItemClickListener<Record> clickListener) {
        this.clickListener = clickListener;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0)
            return TYPE_QUESTIONNAIRE;
        return TYPE_QUESTION;
    }

    @Override
    public int getItemCount() {
        return questions == null ? 1 : questions.size() + 1;
    }

    public void reset(List<Record> questions) {
        this.questions.clear();
        this.questions.addAll(questions);
        notifyDataSetChanged();
    }

    public void updateScore() {

        int level = ((int) score * 10 / count);
        scoreTv.setText("问卷总分：" + count + " 分，得分："
                + score + " 分，评分等级：" + level
                + "\n 设定的评分等级是：" + questionnaire.level
                + "\n " + ((questionnaire.level < level) ? "通过评测" : "未通过评测"));
    }


    class QNHolder extends RecyclerView.ViewHolder {
        TextView intro;
        EditText result;
        LabelTextView action;
        TextView scoreRes;

        public QNHolder(View itemView) {
            super(itemView);
            intro = (TextView) itemView.findViewById(R.id.item_qn_intro);
            result = (EditText) itemView.findViewById(R.id.item_qn_result);
            action = (LabelTextView) itemView.findViewById(R.id.item_pn_action);
            scoreRes = (TextView) itemView.findViewById(R.id.item_qn_score_res);
            scoreTv = scoreRes;
            if (clickListener != null) {
                action.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        clickListener.onClick(null, 1, 0);
                    }
                });
            }
        }
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView desc;
        RadioButton opA;
        RadioButton opB;
        RadioButton opC;
        RadioGroup optionGroup;
        LabelTextView action;

        public ViewHolder(View itemView) {
            super(itemView);
            desc = (TextView) itemView.findViewById(R.id.item_question_intro);
            opA = (RadioButton) itemView.findViewById(R.id.item_option_a);
            opB = (RadioButton) itemView.findViewById(R.id.item_option_b);
            opC = (RadioButton) itemView.findViewById(R.id.item_option_c);
            action = (LabelTextView) itemView.findViewById(R.id.item_question_action);

            optionGroup = (RadioGroup) itemView.findViewById(R.id.item_option_group);

            optionGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                    int p = getLayoutPosition() - 1;
                    Record record = questions.get(p);
                    if (checkedId == R.id.item_option_a) {
                        record.answer = "A";
                    } else if (checkedId == R.id.item_option_b) {
                        record.answer = "B";
                    } else if (checkedId == R.id.item_option_c) {
                        record.answer = "C";
                    }
                }
            });

            if (clickListener != null) {
                action.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int p = getLayoutPosition() - 1;
                        clickListener.onClick(questions.get(p), 2, p + 1);
                    }
                });
            }

        }
    }
}
