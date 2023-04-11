package com.prox.sunrisetodolistapp.Models;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class BudgetItem extends RealmObject {

    @PrimaryKey
    int ID;

    int groupID;
    String itemName;
    int itemQty;
    Double itemCost;
    long timeStamp;
    boolean purchased;

    public BudgetItem() {
    }

    public boolean isPurchased() {
        return purchased;
    }

    public void setPurchased(boolean purchased) {
        this.purchased = purchased;
    }

    public int getItemQty() {
        return itemQty;
    }

    public void setItemQty(int itemQty) {
        this.itemQty = itemQty;
    }

    public int getGroupID() {
    return groupID;
    }

    public void setGroupID(int groupID) {
        this.groupID = groupID;
    }

    public void setItemCost(Double itemCost) {
        this.itemCost = itemCost;
    }

    public long getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public double getItemCost() {
        return itemCost;
    }

    public void setItemCost(double itemCost) {
        this.itemCost = itemCost;
    }
}
