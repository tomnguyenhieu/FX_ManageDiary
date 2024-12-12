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
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

/**
 *
 * @author Admin
 */
public class ManageTeacherController extends App implements Initializable {
  Teacher teacher = null;
    Accounts acc = new Accounts();
    @FXML
    private TableView<Teacher> teachersTable;
    @FXML
    private TableColumn<Teacher, String> idCol;
    @FXML
    private TableColumn<Teacher, String> nameCol;
    @FXML
    private TableColumn<Teacher, String> ageCol;
    @FXML
    private TableColumn<Teacher, String> genderCol;
    @FXML
    private TableColumn<Teacher, String> emailCol;
    @FXML
    private TableColumn<Teacher, String> passCol;
    @FXML
    private TableColumn<Teacher, String> phoneCol;
    @FXML
    private TableColumn<Teacher, String> addressCol;
    @FXML
    private TableColumn<Teacher, String> certificateCol;
    @FXML
    private TableColumn<Teacher, String> salaryCol;
    @FXML
    private TableColumn<Teacher, String> statusCol;

    // Load table Teachers
    @FXML
    public void refreshTableTeachers() {
        final ObservableList<Teacher> TeacherList = FXCollections.observableArrayList();
        ResultSet rs = acc.getTeachersInfo();
        try{
            while(rs.next()) {
                TeacherList.add(new Teacher(
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
                    rs.getInt("status")));
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

        teachersTable.setItems(TeacherList);
        System.out.println("refreshed!");
        //studentsTable.getColumns().addAll(idCol,nameCol,ageCol,genderCol,emailCol,passCol,phoneCol,addressCol,pNameCol,pPhoneCol,pEmailCol,feeCol,classNameCol);
    }
    
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        refreshTableTeachers();
    }
}
