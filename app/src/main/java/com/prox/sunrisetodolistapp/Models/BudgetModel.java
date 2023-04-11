package com.prox.sunrisetodolistapp.Models;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class BudgetModel extends RealmObject {

    @PrimaryKey
    int ID;

    String budgetName;
    Double cost;
    Double budget;
    Double remainder;
    int archived;

    final public static int PINNED = 1;
    final public static int UNPINNED = 0;
    final public static int ARCHIVED = 1;
    final public static int UNARCHIVED = 0;

    public BudgetModel() {
    }

    public int getArchived() {
        return archived;
    }

    public void setArchived(int archived) {
        this.archived = archived;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getBudgetName() {
        return budgetName;
    }

    public void setBudgetName(String budgetName) {
        this.budgetName = budgetName;
    }

    public Double getCost() {
        return cost;
    }

    public void setCost(Double cost) {
        this.cost = cost;
    }

    public Double getBudget() {
        return budget;
    }

    public void setBudget(Double budget) {
        this.budget = budget;
    }

    public Double getRemainder() {
        return remainder;
    }

    public void setRemainder(Double remainder) {
        this.remainder = remainder;
    }
}
