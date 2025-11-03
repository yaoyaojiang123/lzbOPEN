package com.ch.personmis.entity;

import java.io.Serializable;

/**
 * 课程信息实体类
 */
public class Course implements Serializable {
    private Integer id;
    private String courseName;
    private String teacherName;
    private Integer credit;
    private Integer totalHours;
    private String courseType;
    private String suitableGrade;
    private String description;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getTeacherName() {
        return teacherName;
    }

    public void setTeacherName(String teacherName) {
        this.teacherName = teacherName;
    }

    public Integer getCredit() {
        return credit;
    }

    public void setCredit(Integer credit) {
        this.credit = credit;
    }

    public Integer getTotalHours() {
        return totalHours;
    }

    public void setTotalHours(Integer totalHours) {
        this.totalHours = totalHours;
    }

    public String getCourseType() {
        return courseType;
    }

    public void setCourseType(String courseType) {
        this.courseType = courseType;
    }

    public String getSuitableGrade() {
        return suitableGrade;
    }

    public void setSuitableGrade(String suitableGrade) {
        this.suitableGrade = suitableGrade;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}