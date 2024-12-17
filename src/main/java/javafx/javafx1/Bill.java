package javafx.javafx1;

import javafx.beans.property.SimpleStringProperty;

public class Bill {
    private SimpleStringProperty bName;
    private SimpleStringProperty bClass;
    private SimpleStringProperty bDate;
    private int bPrice;
    private SimpleStringProperty bStatus;

    public Bill(String bName, String bClass, String bDate, int bPrice, String bStatus) {
        this.bName = new SimpleStringProperty(bName);
        this.bClass = new SimpleStringProperty(bClass);
        this.bDate = new SimpleStringProperty(bDate);
        this.bPrice = bPrice;
        this.bStatus = new SimpleStringProperty(bStatus);
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
