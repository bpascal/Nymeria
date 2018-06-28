package com.codido.nymeria.bean.vo;

import java.io.Serializable;
import java.util.List;

/**
 * 作者：Junjie.Lai on 2017/3/23 16:15
 * 邮箱：laijj@yzmm365.cn
 */
public class StudentInfo implements Serializable {
    private String studentid;
    private String studentname;    //学生姓名（非空）
    private String sex;             //性别
    private String birthday;        //生日
    private String state;           //状态 0:正常，1:毕业，2:退学
    private String number;          //学号
    private String byname;          //别名
    private String cards;           // 学生卡号（多张卡时 为 “逗号”隔开）
    private String classid;         //班级ID  (如果班级ID 为空，这表示当前学生还未分配班级)
    private String registerdate;    //入学日期
    private String classname;       //班级名称
    private List<ParentInfo> userList;      //家长列表
    private String imageUrl;             //学生照片地址

    /**
     * 是否在车上的标识
     * 默认为false，不在车上
     */
    private boolean isOnTheBus = false;

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getStudentid() {
        return studentid;
    }

    public void setStudentid(String studentid) {
        this.studentid = studentid;
    }

    public String getStudentname() {
        return studentname;
    }

    public void setStudentname(String studentname) {
        this.studentname = studentname;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getByname() {
        return byname;
    }

    public void setByname(String byname) {
        this.byname = byname;
    }

    public String getCards() {
        return cards;
    }

    public void setCards(String cards) {
        this.cards = cards;
    }

    public String getClassid() {
        return classid;
    }

    public void setClassid(String classid) {
        this.classid = classid;
    }

    public String getRegisterdate() {
        return registerdate;
    }

    public void setRegisterdate(String registerdate) {
        this.registerdate = registerdate;
    }

    public String getClassname() {
        return classname;
    }

    public void setClassname(String classname) {
        this.classname = classname;
    }

    public List<ParentInfo> getUserList() {
        return userList;
    }

    public void setUserList(List<ParentInfo> userList) {
        this.userList = userList;
    }

    public boolean isOnTheBus() {
        return isOnTheBus;
    }

    public void setOnTheBus(boolean onTheBus) {
        isOnTheBus = onTheBus;
    }

    @Override
    public String toString() {
        return "StudentInfo{" +
                "studentid='" + studentid + '\'' +
                ", studentname='" + studentname + '\'' +
                ", sex='" + sex + '\'' +
                ", birthday='" + birthday + '\'' +
                ", state='" + state + '\'' +
                ", number='" + number + '\'' +
                ", byname='" + byname + '\'' +
                ", cards='" + cards + '\'' +
                ", classid='" + classid + '\'' +
                ", registerdate='" + registerdate + '\'' +
                ", classname='" + classname + '\'' +
                ", userList=" + userList +
                ", imageUrl='" + imageUrl + '\'' +
                ", isOnTheBus=" + isOnTheBus +
                '}';
    }
}
