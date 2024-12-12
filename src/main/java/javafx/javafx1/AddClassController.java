package javafx.javafx1;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.io.File;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;


public class AddClassController implements Initializable
{
    public String className = "";
    public String teacherName = "";

    @FXML
    private TextField inputClass;
    @FXML
    private ComboBox cbTeachersName;

    // Xu ly logic
    public void initComboBox()
    {
        Files file = new Files();
        ResultSet rs = file.getTeachersName();
        try {
            while (rs.next())
            {
                cbTeachersName.getItems().add(rs.getString("name"));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public void storeClass()
    {
        className = inputClass.getText();

        if (cbTeachersName.getValue() != null)
        {
            teacherName = cbTeachersName.getValue().toString();
        } else
        {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setContentText("Vui long chon giao vien!");
            alert.show();
        }

        Files file = new Files();
        int teacherId = file.getTeacherId(teacherName);
        file.storeClass(className, teacherId);
    }
    public void setUpClassForm(String className, String teacherName)
    {

    }

    // Xu ly event
    public void close(ActionEvent event){
        // phat event dong window
        Stage stage = (Stage)((Node) event.getSource()).getScene().getWindow();
        stage.fireEvent(new WindowEvent(stage, WindowEvent.WINDOW_CLOSE_REQUEST));
    }
    public void onActionConfirm(ActionEvent event)
    {
        storeClass();
        close(event);
    }
    public void onActionCancel(ActionEvent event)
    {
        close(event);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initComboBox();
    }
}
