package com.example.goalfitness.Model;

public class ToDo {

    private String task;
    private String priority;
    private String Date_or_Time;

    public ToDo() {
    }

    public ToDo(String task, String priority, String Date_or_Time) {
        this.task = task;
        this.priority = priority;
        this.Date_or_Time = Date_or_Time;
    }

    public String getTask() {
        return task;
    }

    public void setTask(String task) {
        this.task = task;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public String getDate_or_Time() {
        return Date_or_Time;
    }

    public void setDate_or_Time(String date_or_Time) {
        Date_or_Time = date_or_Time;
    }
}
