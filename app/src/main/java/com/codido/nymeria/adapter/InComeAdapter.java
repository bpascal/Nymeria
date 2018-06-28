package com.codido.nymeria.adapter;

import android.animation.ValueAnimator;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.BaseExpandableListAdapter;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.codido.nymeria.R;
import com.codido.nymeria.activity.InComeByDayActivity;
import com.codido.nymeria.bean.vo.DayEarnVo;
import com.codido.nymeria.bean.vo.MonthEarnVo;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by liukaifu on 2017/11/9.
 */
public class InComeAdapter extends BaseExpandableListAdapter {


    private Context context;

    private LayoutInflater layoutInflater;


    /**
     * 收益列表
     */
    private List<MonthEarnVo> monthEarnVoList;

    /**
     * 构造方法
     *
     * @param context
     */
    public InComeAdapter(Context context) {
        this.context = context;
        this.layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getGroupCount() {
        if (monthEarnVoList != null && monthEarnVoList.size() > 0) {
            return monthEarnVoList.size();
        } else {
            return 0;
        }
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        if (monthEarnVoList != null && monthEarnVoList.size() > 0) {
            MonthEarnVo monthEarnVo = monthEarnVoList.get(groupPosition);
            if (monthEarnVo != null) {
                List<DayEarnVo> dayEarnVoList = monthEarnVo.getDayEarnList();
                if (dayEarnVoList != null && dayEarnVoList.size() > 0) {
                    return dayEarnVoList.size();
                } else {
                    return 0;
                }
            } else {
                return 0;
            }
        } else {
            return 0;
        }
    }

    @Override
    public Object getGroup(int groupPosition) {
        if (monthEarnVoList != null && monthEarnVoList.size() > 0) {
            return monthEarnVoList.get(groupPosition);
        } else {
            return null;
        }
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        if (monthEarnVoList != null && monthEarnVoList.size() > 0) {
            MonthEarnVo monthEarnVo = monthEarnVoList.get(groupPosition);
            if (monthEarnVo != null) {
                List<DayEarnVo> dayEarnVoList = monthEarnVo.getDayEarnList();
                if (dayEarnVoList != null && dayEarnVoList.size() > 0) {
                    return dayEarnVoList.get(childPosition);
                } else {
                    return null;
                }
            } else {
                return null;
            }
        } else {
            return null;
        }
    }

    @Override
    public long getGroupId(int groupPosition) {
        if (monthEarnVoList != null && monthEarnVoList.size() > 0) {
            return groupPosition;
        } else {
            return -1;
        }
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        if (monthEarnVoList != null && monthEarnVoList.size() > 0) {
            MonthEarnVo monthEarnVo = monthEarnVoList.get(groupPosition);
            if (monthEarnVo != null) {
                List<DayEarnVo> dayEarnVoList = monthEarnVo.getDayEarnList();
                if (dayEarnVoList != null && dayEarnVoList.size() > 0) {
                    return childPosition;
                } else {
                    return -1;
                }
            } else {
                return -1;
            }
        } else {
            return -1;
        }
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean sExpanded, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.income_item_by_month, null);
        }
        convertView.setTag(R.layout.income_item_by_month, groupPosition);
        convertView.setTag(R.layout.income_item_by_day, -1);
        //展现数字
        TextView textViewAmt = (TextView) convertView.findViewById(R.id.textViewAmt);
        textViewAmt.setText(monthEarnVoList.get(groupPosition).getMonthTotalEarn() + "元");
        TextView textViewMonth = (TextView) convertView.findViewById(R.id.textViewMonth);
        textViewMonth.setText(monthEarnVoList.get(groupPosition).getMonth().substring(4) + "月");
        TextView textViewYear = (TextView) convertView.findViewById(R.id.textViewYear);
        textViewYear.setText(monthEarnVoList.get(groupPosition).getMonth().substring(0, 4) + "年");

        return convertView;
    }

    class DayViewHolder {

        View view;

        DayEarnVo dayEarnVo;

        @BindView(R.id.textViewDayAmt)
        TextView textViewDayAmt;
        @BindView(R.id.textViewDay)
        TextView textViewDay;
        @BindView(R.id.progressBarContent)
        ProgressBar progressBar;

        ValueAnimator animator;

        public DayViewHolder(View view) {
            this.view = view;
            this.view.setTag(this);
            ButterKnife.bind(this, view);
        }

        public void update() {
            textViewDayAmt.setText(dayEarnVo.getDayTotalEarn());
            textViewDay.setText(dayEarnVo.getDay().substring(6, 8));
            progressBar.setProgress((int) dayEarnVo.getProgressNum());

            if (animator != null && animator.isRunning()) {
                animator.end();
            }

            animator = ValueAnimator.ofInt(0, (int) dayEarnVo.getProgressNum());

            animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    int curValue = (int) animation.getAnimatedValue();
                    progressBar.setProgress(curValue);
                }
            });
            animator.setDuration(1000);
            animator.setInterpolator(new AccelerateDecelerateInterpolator());
            animator.start();
        }

    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            DayViewHolder holder = (DayViewHolder) v.getTag();

            InComeByDayActivity.start(context, holder.dayEarnVo);
        }
    };

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {

        DayViewHolder holder;
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.income_item_by_day, null);
            holder = new DayViewHolder(convertView);
            convertView.setOnClickListener(onClickListener);
        } else {
            holder = (DayViewHolder) convertView.getTag();
        }

        holder.dayEarnVo = monthEarnVoList.get(groupPosition).getDayEarnList().get(childPosition);
        holder.update();

        return convertView;
    }

    public List<MonthEarnVo> getMonthEarnVoList() {
        return monthEarnVoList;
    }

    public void setMonthEarnVoList(List<MonthEarnVo> monthEarnVoList) {
        this.monthEarnVoList = monthEarnVoList;
    }
}
