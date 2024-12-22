package javafx.javafx1;

//import com.test.mytestjavafx.HelloApplication;
//import com.test.mytestjavafx.model.Accounts;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

public class AddStudentController extends App implements Initializable {
    private Accounts acc = new Accounts();
    private boolean edit;
    public void setEdit(boolean edit) {
        this.edit = edit;
    }
    private int studentId;
    public void setStudentId(int studentId) {
        this.studentId = studentId;
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
    ComboBox addressCb;
    @FXML
    ComboBox classCb;
    @FXML
    TextField pNameFld;
    @FXML
    TextField pPhoneFld;
    @FXML
    TextField pEmailFld;
    @FXML
    TextField pFeeFld;
    @FXML
    ComboBox statusCb;




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
        genderCb.getItems().add("Hiếu");
        for(int i = 1; i < 100; i++){
            ageCb.getItems().add(i);
        }
        ResultSet rsClass = acc.getAllClass();
        while (rsClass.next()){
            classCb.getItems().add(rsClass.getString("name"));
        }
        statusCb.getItems().add("Đang hoạt động");
        statusCb.getItems().add("Dừng hoạt động");
        List<String> cityLst = Arrays.asList(
                "Hà Nội", "Hải Phòng", "Hồ Chí Minh", "Đà Nẵng", "Cần Thơ",
                "Bắc Ninh", "Hà Nam", "Hải Dương", "Hưng Yên", "Nam Định",
                "Ninh Bình", "Thái Bình", "Vĩnh Phúc", "Quảng Ninh", "Lào Cai",
                "Yên Bái", "Điện Biên", "Lai Châu", "Sơn La", "Hòa Bình",
                "Thái Nguyên", "Tuyên Quang", "Phú Thọ", "Bắc Giang", "Bắc Kạn",
                "Cao Bằng", "Lạng Sơn", "Hà Giang", "Thanh Hóa", "Nghệ An",
                "Hà Tĩnh", "Quảng Bình", "Quảng Trị", "Thừa Thiên Huế", "Quảng Nam",
                "Quảng Ngãi", "Bình Định", "Phú Yên", "Khánh Hòa", "Ninh Thuận",
                "Bình Thuận", "Kon Tum", "Gia Lai", "Đắk Lắk", "Đắk Nông",
                "Lâm Đồng", "Bình Phước", "Tây Ninh", "Bình Dương", "Đồng Nai",
                "Bà Rịa - Vũng Tàu", "Long An", "Tiền Giang", "Bến Tre", "Trà Vinh",
                "Vĩnh Long", "Đồng Tháp", "An Giang", "Kiên Giang", "Hậu Giang",
                "Sóc Trăng", "Bạc Liêu", "Cà Mau"
        );
        addressCb.getItems().addAll(cityLst);
    }
    public void setTextField(String name, int age, String gender, String email, String pass, String phone, String address, String className, String pName, String pPhone, String pEmail, int fee, String status){
        // khoi tao thong tin dien san trong form
        nameFld.setText(name);
        ageCb.setValue(age);
        genderCb.setValue(gender);
        emailFld.setText(email);
        passFld.setText(pass);
        phoneFld.setText(phone);
        addressCb.setValue(address);
        classCb.setValue(className);
        pNameFld.setText(pName);
        pPhoneFld.setText(pPhone);
        pEmailFld.setText(pEmail);
        pFeeFld.setText(String.valueOf(fee));
        statusCb.setValue(status);
    }
    public void onConfirmClick(ActionEvent event){
        if(ageCb.getValue() != null && genderCb.getValue() != null && classCb.getValue() != null && addressCb.getValue() != null && statusCb.getValue() != null && pFeeFld.getText() != ""){
            //comboBox khong null
            String name = nameFld.getText();
            String age = ageCb.getValue().toString();
            String gender = genderCb.getValue().toString();
            String email = emailFld.getText();
            String pass = passFld.getText();
            String phone = phoneFld.getText();
            String address = addressCb.getValue().toString();
            String className = classCb.getValue().toString();
            String pName = pNameFld.getText();
            String pPhone = pPhoneFld.getText();
            String pEmail = pEmailFld.getText();
            int fee = Integer.parseInt(pFeeFld.getText());
            int status = (statusCb.getValue().toString().equals("Đang hoạt động")) ? 1 : 2;
            if(name.isEmpty() || age.isEmpty() || gender.isEmpty() || email.isEmpty() || pass.isEmpty() || address.isEmpty() || pName.isEmpty() || pPhone.isEmpty() || pEmail.isEmpty()){
                // Nhap thieu thong tin
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setHeaderText(null);
                alert.setContentText("Vui lòng nhập đầy đủ thông tin");
                alert.showAndWait();
            } else {
                if(!edit){
                    // tao moi student
                    acc.addStudent(name,Integer.parseInt(age),gender,email,pass,phone,address,pName,pPhone,pEmail,fee,className,status);
                    close(event);
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setHeaderText(null);
                    alert.setContentText("Thêm sinh viên thành công");
                    alert.showAndWait();
                } else{
                    // chinh sua student
                    acc.editStudent(name,Integer.parseInt(age),gender,email,pass,phone,address,pName,pPhone,pEmail,fee,className, status, studentId);
                    close(event);
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setHeaderText(null);
                    alert.setContentText("Sửa thông tin sinh viên thành công");
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
