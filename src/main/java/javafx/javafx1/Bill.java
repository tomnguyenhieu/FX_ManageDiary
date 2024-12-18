package javafx.javafx1;

import javafx.beans.property.SimpleStringProperty;

public class Bill {
    private int id;
    private SimpleStringProperty month;
    private int lessonQty;
    private int monthlySalary;

    private int bId;
    private SimpleStringProperty bName;
    private SimpleStringProperty bClass;
    private SimpleStringProperty bDate;
    private int bPrice;
    private SimpleStringProperty bStatus;

    public Bill(int id, String bName, String month, int lessonQty, int monthlySalary, String bStatus) {
        this.id = id;
        this.month = new SimpleStringProperty(month);
        this.lessonQty = lessonQty;
        this.monthlySalary = monthlySalary;
        this.bName = new SimpleStringProperty(bName);
        this.bStatus = new SimpleStringProperty(bStatus);
    }

    public Bill(int bId, String bName, String bClass, String bDate, int bPrice, String bStatus) {
        this.bId = bId;
        this.bName = new SimpleStringProperty(bName);
        this.bClass = new SimpleStringProperty(bClass);
        this.bDate = new SimpleStringProperty(bDate);
        this.bPrice = bPrice;
        this.bStatus = new SimpleStringProperty(bStatus);
    }

    public int getId() {
        return id;
    }

    public int getLessonQty() {
        return lessonQty;
    }

    public int getMonthlySalary() {
        return monthlySalary;
    }

    public int getBId(){
        return bId;
    }
    public String getBName() {
        return bName.get();
    }

    public String getBClass() {
        return bClass.get();
    }

    public String getBDate() {
        return bDate.get();
    }

    public int getBPrice() {
        return bPrice;
    }

    public String getBStatus() {
        return bStatus.get();
    }

}
