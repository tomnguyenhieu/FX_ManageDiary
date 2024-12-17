package javafx.javafx1;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.awt.event.MouseEvent;
import java.io.File;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;


public class AddClassController implements Initializable
{
    public String className = "";
    public String teacherName = "";

    private boolean edit = false;
    public void setEdit(boolean edit) {
        this.edit = edit;
    }

    private int classIdGlobal = 0;
    public void setClassIdGlobal(int classIdGlobal) {
        this.classIdGlobal = classIdGlobal;
    }

    @FXML
    private TextField inputClass;
    @FXML
    private ComboBox cbTeachersName;
//    private String classNameGlobal;
//    private String teacherNameGlobal;

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
    public void updateClassOnEdit()
    {
        className = inputClass.getText();
        teacherName = cbTeachersName.getValue().toString();

        Files file = new Files();
        int teacherId = file.getTeacherId(teacherName);
//        int classId = file.getClassId(className);

        System.out.println(teacherId + " " + classIdGlobal + " " + className);
        file.updateClass(className, teacherId, classIdGlobal);
    }
    public void setUpClassForm(String className, String teacherName)
    {
        inputClass.setText(className);
        cbTeachersName.setValue(teacherName.substring(11));
    }

    // Xu ly event
    public void close(ActionEvent event){
        // phat event dong window
        Stage stage = (Stage)((Node) event.getSource()).getScene().getWindow();
        stage.fireEvent(new WindowEvent(stage, WindowEvent.WINDOW_CLOSE_REQUEST));
    }
    public void onActionConfirm(ActionEvent event)
    {
        if (!edit)
        {
            storeClass();
            close(event);
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setContentText("Them moi thanh cong!");
            alert.show();
        } else
        {
            updateClassOnEdit();
            close(event);
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setContentText("Sua thanh cong!");
            alert.show();
        }
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
