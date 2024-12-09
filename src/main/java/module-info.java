module javafx.javafx1 {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires org.apache.poi.ooxml;
    requires java.desktop;


    opens javafx.javafx1 to javafx.fxml;
    exports javafx.javafx1;
}