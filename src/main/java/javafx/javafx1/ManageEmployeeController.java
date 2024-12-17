/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package javafx.javafx1;

import java.net.URL;
import java.sql.ResultSet;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 *
 * @author Admin
 */
public class ManageEmployeeController extends App implements Initializable {
    Person employee = null;
    Accounts acc = new Accounts();
    @FXML
    private TableView<Person> employeeTable;
    @FXML
    private TableColumn<Person, String> idCol;
    @FXML
    private TableColumn<Person, String> nameCol;
    @FXML
    private TableColumn<Person, String> ageCol;
    @FXML
    private TableColumn<Person, String> genderCol;
    @FXML
    private TableColumn<Person, String> emailCol;
    @FXML
    private TableColumn<Person, String> passCol;
    @FXML
    private TableColumn<Person, String> phoneCol;
    @FXML
    private TableColumn<Person, String> addressCol;
    @FXML
    private TableColumn<Person, String> certificateCol;
    @FXML
    private TableColumn<Person, String> salaryCol;
    @FXML
    private TableColumn<Person, String> statusCol;

    // Load table Teachers
    @FXML
    public void refreshTableEmployee() {
        final ObservableList<Person> EmployeeList = FXCollections.observableArrayList();
        ResultSet rs = acc.getTeachersInfo(3);
        try{
            while(rs.next()) {
                EmployeeList.add(new Person(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getInt("age"),
                        rs.getString("gender"),
                        rs.getString("email"),
                        rs.getString("password"),
                        rs.getString("phone"),
                        rs.getString("address"),
                        rs.getString("certificates"),
                        rs.getInt("salary"),
                        rs.getInt("status") == 1 ? "Đang hoạt động" : "Dừng hoạt động"
                ));
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        ageCol.setCellValueFactory(new PropertyValueFactory<>("age"));
        genderCol.setCellValueFactory(new PropertyValueFactory<>("gender"));
        emailCol.setCellValueFactory(new PropertyValueFactory<>("email"));
        passCol.setCellValueFactory(new PropertyValueFactory<>("password"));
        phoneCol.setCellValueFactory(new PropertyValueFactory<>("phone"));
        addressCol.setCellValueFactory(new PropertyValueFactory<>("address"));
        certificateCol.setCellValueFactory(new PropertyValueFactory<>("certificate"));
        salaryCol.setCellValueFactory(new PropertyValueFactory<>("salary"));
        statusCol.setCellValueFactory(new PropertyValueFactory<>("status"));

        employeeTable.setItems(EmployeeList);
        System.out.println("refreshed!");
    }
    // Add new Employee
    @FXML
    public void onAddEmployeeClick() {
        try {
            FXMLLoader loader = new FXMLLoader(App.class.getResource("addEmployee.fxml"));
            Parent root = loader.load();
            AddEmployeeController addEmployeeController = loader.getController();
            addEmployeeController.setEdit(false);

            Stage stage = new Stage();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.initStyle(StageStyle.TRANSPARENT);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.centerOnScreen();
            stage.show();
            stage.setOnCloseRequest((event) -> {
                refreshTableEmployee();
            });
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void onEditEmployeeClick() {
        // Lay thong tin nhan vien de hien len form
        employee = employeeTable.getSelectionModel().getSelectedItem();
        if(employee != null) {
            System.out.println(employee.getName());
            try {
                FXMLLoader loader = new FXMLLoader(App.class.getResource("addEmployee.fxml"));
                Parent root = loader.load();
                AddEmployeeController addEmployeeController = loader.getController();
                addEmployeeController.setTextField(employee.getName(), employee.getAge(), employee.getGender(), employee.getEmail(), employee.getPassword(), employee.getPhone(), employee.getAddress(),employee.getStatus(), employee.getCertificate(), employee.getSalary());
                addEmployeeController.setEdit(true);
                addEmployeeController.setEmployeeId(employee.getId());


                Stage stage = new Stage();
                Scene scene = new Scene(root);
                stage.setScene(scene);
                stage.initStyle(StageStyle.TRANSPARENT);
                stage.initModality(Modality.APPLICATION_MODAL);
                stage.show();
                stage.setOnCloseRequest((event) -> {
                    refreshTableEmployee();
                });
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }else {
            // chua chon student
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setHeaderText(null);
            alert.setContentText("Chọn một nhân viên để sửa");
            alert.showAndWait();
        }
    }

    public void onDeleteEmployeeClick(MouseEvent event){
        // Lay id student va bo vao ham xoa
        employee = employeeTable.getSelectionModel().getSelectedItem();
        if(employee != null){
            int employeeId = employee.getId();
            acc.deleteTeacherById(employeeId);
            refreshTableEmployee();
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText(null);
            alert.setContentText("Xóa nhân viên thành công");
            alert.showAndWait();
        } else{
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setHeaderText(null);
            alert.setContentText("Chọn vào nhân viên để xóa!");
            alert.showAndWait();
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        refreshTableEmployee();
    }
}

