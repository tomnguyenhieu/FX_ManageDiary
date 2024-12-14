package javafx.javafx1;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;


import java.io.File;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;

public class ConfirmUploadController
{
    private Stage stage;
    private int countGlobal = 0;
    private VBox lessonsContainerGlobal;
    private boolean isConfirm = false;

    @FXML
    private Label nameLabel;

    // Xu ly logic
    public void parseFileName(String fileName)
    {
        nameLabel.setText("Xác nhận tải lên file " + fileName + " ?");
    }
    public void parseCountGlobal(int count)
    {
        this.countGlobal = count;
    }
    public void parseLessonsContainerGlobal(VBox lessonsContainer)
    {
        this.lessonsContainerGlobal = lessonsContainer;
    }
    public boolean getConfirm() {
        return isConfirm;
    }

    // Xy ly event
    public void close(ActionEvent event){
        // phat event dong window
        stage = (Stage)((Node) event.getSource()).getScene().getWindow();
        stage.fireEvent(new WindowEvent(stage, WindowEvent.WINDOW_CLOSE_REQUEST));
    }
    public void onActionConfirm(ActionEvent event)
    {
        isConfirm = true;

        close(event);

        Button btn = (Button)lessonsContainerGlobal.getChildren().getLast();
        btn.setTextFill(Color.WHITE);
        btn.setStyle("-fx-background-color: #F05454; -fx-font-size: 20");

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setContentText("Upload thanh cong");
        alert.show();
    }
    public void onActionCancel(ActionEvent event)
    {
        isConfirm = false;

        close(event);
    }
}
