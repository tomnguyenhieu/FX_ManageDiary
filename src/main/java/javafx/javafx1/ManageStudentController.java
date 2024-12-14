package javafx.javafx1;

import javafx.javafx1.App;
import javafx.javafx1.Accounts;
import javafx.javafx1.Student;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;


import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.util.ResourceBundle;
import java.util.Stack;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ManageStudentController extends App implements Initializable {
    Student student = null;
    Accounts acc = new Accounts();
    double orgSceneX, orgSceneY, orgTranslateX, orgTranslateY;
    @FXML
    private TableView<Student> studentsTable;
    @FXML
    private TableColumn<Student, String> idCol;
    @FXML
    private TableColumn<Student, String> nameCol;
    @FXML
    private TableColumn<Student, String> ageCol;
    @FXML
    private TableColumn<Student, String> genderCol;
    @FXML
    private TableColumn<Student, String> emailCol;
    @FXML
    private TableColumn<Student, String> passCol;
    @FXML
    private TableColumn<Student, String> phoneCol;
    @FXML
    private TableColumn<Student, String> addressCol;
    @FXML
    private TableColumn<Student, String> pNameCol;
    @FXML
    private TableColumn<Student, String> pPhoneCol;
    @FXML
    private TableColumn<Student, String> pEmailCol;
    @FXML
    private TableColumn<Student, String> feeCol;
    @FXML
    private TableColumn<Student, String> classNameCol;
    @FXML
    private TableColumn<Student, String> statusCol;



    @FXML
    public void refreshTable() {
        final ObservableList<Student> StudentList = FXCollections.observableArrayList();
        Accounts acc = new Accounts();
        ResultSet rs = acc.studentsInfo();
        try{
            while(rs.next()) {
                StudentList.add(new Student(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getInt("age"),
                        rs.getString("gender"),
                        rs.getString("email"),
                        rs.getString("password"),
                        rs.getString("phone"),
                        rs.getString("address"),
                        rs.getString("parents_name"),
                        rs.getString("parents_phone"),
                        rs.getString("parents_email"),
                        rs.getInt("fee"),
                        rs.getString("classes.name"),
                        checkStatus(rs.getInt("status"))
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
        pNameCol.setCellValueFactory(new PropertyValueFactory<>("pName"));
        pPhoneCol.setCellValueFactory(new PropertyValueFactory<>("pPhone"));
        pEmailCol.setCellValueFactory(new PropertyValueFactory<>("pEmail"));
        feeCol.setCellValueFactory(new PropertyValueFactory<>("fee"));
        classNameCol.setCellValueFactory(new PropertyValueFactory<>("className"));
        statusCol.setCellValueFactory(new PropertyValueFactory<>("status"));

        studentsTable.setItems(StudentList);
        System.out.println("refresh!!!!!!!!!");
        //studentsTable.getColumns().addAll(idCol,nameCol,ageCol,genderCol,emailCol,passCol,phoneCol,addressCol,pNameCol,pPhoneCol,pEmailCol,feeCol,classNameCol);
    }
    private String checkStatus(int status){
        if(status == 1){
            return "Đang hoạt động";
        }else{
            return "Dừng hoạt động";
        }
    }
    @FXML
    private void onAddStudentClick(){
        // bat stage form them student
        System.out.println("Add!!");
        try {
            FXMLLoader loader = new FXMLLoader(App.class.getResource("add-student.fxml"));
            Parent root = loader.load();
            AddStudentController addStudentController = loader.getController();
            addStudentController.setEdit(false);

            Stage stage = new Stage();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.initStyle(StageStyle.TRANSPARENT);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.show();
            stage.setOnCloseRequest((event) -> {
                refreshTable();
            });
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    @FXML
    public void onEditStudentClick() {
        // Lay thong tin student de hien len form
        student = studentsTable.getSelectionModel().getSelectedItem();
//        TableView.TableViewSelectionModel<Student> studentSelection = studentsTable.getSelectionModel();
        if(student != null) {
            System.out.println(student.getName());
            try {
                FXMLLoader loader = new FXMLLoader(App.class.getResource("add-student.fxml"));
                Parent root = loader.load();
                AddStudentController addStudentController = loader.getController();
                addStudentController.setTextField(student.getName(), student.getAge(), student.getGender(), student.getEmail(), student.getPassword(), student.getPhone(), student.getAddress(), student.getClassName(), student.getPName(), student.getPPhone(), student.getPEmail(), student.getFee());
                addStudentController.setEdit(true);
                addStudentController.setStudentId(student.getId());

                Stage stage = new Stage();
                Scene scene = new Scene(root);
                stage.setScene(scene);
                stage.initStyle(StageStyle.TRANSPARENT);
                stage.initModality(Modality.APPLICATION_MODAL);
                stage.show();
                stage.setOnCloseRequest((event) -> {
                    refreshTable();
//                    studentsTable.setSelectionModel(studentSelection);
                });
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }else {
            // chua chon student
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setHeaderText(null);
            alert.setContentText("Chọn một học sinh để sửa");
            alert.showAndWait();
        }
    }
    @FXML
    public void onDeleteStudentClick(MouseEvent event){
        // Lay id student va bo vao ham xoa
        student = studentsTable.getSelectionModel().getSelectedItem();
        if(student != null){
            int studentId = student.getId();
            acc.deleteStudentById(studentId);
            refreshTable();
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText(null);
            alert.setContentText("Xóa sinh viên thành công");
            alert.showAndWait();
        } else{
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setHeaderText(null);
            alert.setContentText("Chọn vào học sinh để xóa!");
            alert.showAndWait();
        }

    }
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        refreshTable();
    }
    public void onMouseEnter(MouseEvent event){
        HBox hbox = (HBox) event.getSource();
        hbox.setEffect(new ColorAdjust(0,0,0,-0.2));
    }
    public void onMouseExit(MouseEvent event){
        HBox hbox = (HBox) event.getSource();
        hbox.setEffect(new ColorAdjust(0,0,0,0));
    }

    public void onBtnAddPressed(MouseEvent event) {
        orgSceneX = event.getSceneX();
        orgSceneY = event.getSceneY();
        StackPane stkPane = (StackPane)(event.getSource());
        stkPane.setScaleY(1.1);
        stkPane.setScaleX(1.1);
        orgTranslateX = ((StackPane)(event.getSource())).getTranslateX();
        orgTranslateY = ((StackPane)(event.getSource())).getTranslateY();
    }
    public void onBtnAddDragged(MouseEvent event) {
        double offsetX = event.getSceneX() - orgSceneX;
        double offsetY = event.getSceneY() - orgSceneY;
        double newTranslateX = orgTranslateX + offsetX;
        double newTranslateY = orgTranslateY + offsetY;
        ((StackPane)(event.getSource())).setTranslateX(newTranslateX);
        ((StackPane)(event.getSource())).setTranslateY(newTranslateY);
    }
    public void onBtnExit(MouseEvent event){
        StackPane stkPane = (StackPane)(event.getSource());
        stkPane.setScaleY(1);
        stkPane.setScaleX(1);
    }
}
