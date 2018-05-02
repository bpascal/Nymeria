package com.codido.nymeria.adapter.holder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.codido.nymeria.R;
import com.codido.nymeria.adapter.BaseItemHolder;
import com.codido.nymeria.bean.vo.StudentInfo;
import com.codido.nymeria.util.YUtils;

/**
 * 学生信息holder
 * 作者：Junjie.Lai on 2017/4/13 17:28
 * 邮箱：laijj@yzmm365.cn
 */
public class StudentItemHolder extends BaseItemHolder<StudentInfo> {

    /**
     * 用户image
     */
    private ImageView studentPhotoImage;

    /**
     * 学生姓名
     */
    private TextView textViewStudentName;

    /**
     * 蒙层
     */
    private View linearLayoutMeng;


    @Override
    protected void initViews(View convertView) {
        studentPhotoImage = (ImageView) convertView.findViewById(R.id.imageViewStudent);
        textViewStudentName = (TextView) convertView.findViewById(R.id.textViewStudentName);
        linearLayoutMeng = convertView.findViewById(R.id.linearLayoutMeng);
    }

    @Override
    protected void setHolderView(StudentInfo item) {
        if (!YUtils.isEmpty(item.getImageUrl())) {
            Glide.with(this.mActivity).load(item.getImageUrl()).into(studentPhotoImage);
        } else {
            Glide.with(this.mActivity).load(R.mipmap.babby_default).into(studentPhotoImage);
        }
        textViewStudentName.setText(item.getStudentname());
        //上下车状况
        if (item.isOnTheBus()) {
            linearLayoutMeng.setVisibility(View.GONE);
        } else {
            linearLayoutMeng.setVisibility(View.VISIBLE);
        }
    }
}
