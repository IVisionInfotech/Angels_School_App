package com.ivision.model;

import java.io.Serializable;
import java.util.List;

public class Result implements Serializable {

    String sub_id, subject, type, obtained_marks, total_marks, percentage, date;

    public String getSub_id() {
        return sub_id;
    }

    public void setSub_id(String sub_id) {
        this.sub_id = sub_id;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getObtained_marks() {
        return obtained_marks;
    }

    public void setObtained_marks(String obtained_marks) {
        this.obtained_marks = obtained_marks;
    }

    public String getTotal_marks() {
        return total_marks;
    }

    public void setTotal_marks(String total_marks) {
        this.total_marks = total_marks;
    }

    public String getPercentage() {
        return percentage;
    }

    public void setPercentage(String percentage) {
        this.percentage = percentage;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }



    private String testname,total,trank;
    private List<String> subjectList,mark,subtotal,subrank;

    public String getTestname() {
        return testname;
    }

    public void setTestname(String testname) {
        this.testname = testname;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }


    public String getTrank() {
        return trank;
    }

    public void setTrank(String trank) {
        this.trank = trank;
    }

    public List<String> getListSubject() {
        return subjectList;
    }

    public void setListSubject(List<String> subjectList) {
        this.subjectList = subjectList;
    }

    public List<String> getMark() {
        return mark;
    }

    public void setMark(List<String> mark) {
        this.mark = mark;
    }

    public List<String> getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(List<String> subtotal) {
        this.subtotal = subtotal;
    }

    public List<String> getSubrank() {
        return subrank;
    }

    public void setSubrank(List<String> subrank) {
        this.subrank = subrank;
    }
}
