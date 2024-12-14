/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package javafx.javafx1;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

/**
 *
 * @author Admin
 */
public class AddTeacherController extends App implements Initializable {
  private Accounts acc = new Accounts();
    private boolean edit;
    public void setEdit(boolean edit) {
        this.edit = edit;
    }
    private int teacherId;
    public void setTeacherId(int teacherId) {
        this.teacherId = teacherId;
    }
    @FXML
    Button cancelBtn;
    @FXML
    Button confirmBtn;

    @FXML
    TextField nameFld;
    @FXML
    ComboBox ageCb;
    @FXML
    ComboBox genderCb;
    @FXML
    TextField emailFld;
    @FXML
    TextField passFld;
    @FXML
    TextField phoneFld;
    @FXML
    TextField addressFld;
    @FXML
    ComboBox statusCb;
    @FXML
    TextField certificateFld;
    @FXML
    TextField salaryFld;

    @FXML
    public void close(ActionEvent event){
        // phat event dong window
        Stage stage = (Stage)((Node) event.getSource()).getScene().getWindow();
        stage.fireEvent(new WindowEvent(stage, WindowEvent.WINDOW_CLOSE_REQUEST));
    }
    private void setUpForm() throws SQLException {
        // Khoi tao cac comboBox
        genderCb.getItems().add("Nam");
        genderCb.getItems().add("Nữ");
        
        for(int i = 1; i < 100; i++){
            ageCb.getItems().add(i);
        }
        
        statusCb.getItems().add("1");
        statusCb.getItems().add("2");
    }
    public void setTextField(String name, int age, String gender, String email, String password, String phone, String address, int status, String certificate, int salary){
        // khoi tao thong tin dien san trong form
        nameFld.setText(name);
        ageCb.setValue(age);
        genderCb.setValue(gender);
        emailFld.setText(email);
        passFld.setText(password);
        phoneFld.setText(phone);
        addressFld.setText(address);
        statusCb.setValue(status);
        certificateFld.setText(certificate);
        salaryFld.setText(String.valueOf(salary));
    }
    public void onConfirmClick(ActionEvent event){
        if(ageCb.getValue() != null && genderCb.getValue() != null){
            //comboBox khong null
            String name = nameFld.getText();
            String age = ageCb.getValue().toString();
            String gender = genderCb.getValue().toString();
            String email = emailFld.getText();
            String pass = passFld.getText();
            String phone = phoneFld.getText();
            String address = addressFld.getText();
            int status = Integer.parseInt(statusCb.getValue().toString());
            String certificate = certificateFld.getText();
            int salary = Integer.parseInt(salaryFld.getText());
            if(name.isEmpty() || gender.isEmpty() || email.isEmpty() || pass.isEmpty() || address.isEmpty()){
                // Nhap thieu thong tin
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setHeaderText(null);
                alert.setContentText("Vui lòng nhập đầy đủ thông tin");
                alert.showAndWait();
            } else {
                if(!edit){
                    // tao moi student
                    acc.addTeacher(name, Integer.parseInt(age), gender, email, pass, phone, address, status, certificate, salary);
                    close(event);
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setHeaderText(null);
                    alert.setContentText("Thêm giaó viên thành công");
                    alert.showAndWait();
                } else{
                    // chinh sua student
                    acc.editTeacher(name, Integer.parseInt(age), gender, email, pass, phone, address, status, certificate, salary);
                    close(event);
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setHeaderText(null);
                    alert.setContentText("Sửa thông tin giáo viên thành công");
                    alert.showAndWait();
                }
            }
        }else {
            // Nhap thieu thong tin
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setHeaderText(null);
            alert.setContentText("Vui lòng nhập đầy đủ thông tin");
            alert.showAndWait();
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            setUpForm();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
