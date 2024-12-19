package javafx.javafx1;

import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Callback;
import org.kordamp.ikonli.javafx.FontIcon;


import java.net.URL;
import java.sql.ResultSet;
import java.util.ResourceBundle;

public class ManageStudentController extends App implements Initializable {
    Person student = null;
    Accounts acc = new Accounts();
    private static Timeline gameLoop;
    double orgSceneX, orgSceneY, orgTranslateX, orgTranslateY;
    @FXML
    private TableView<Person> studentsTable;
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
    private TableColumn<Person, String> pNameCol;
    @FXML
    private TableColumn<Person, String> pPhoneCol;
    @FXML
    private TableColumn<Person, String> pEmailCol;
    @FXML
    private TableColumn<Person, String> feeCol;
    @FXML
    private TableColumn<Person, String> classNameCol;
    @FXML
    private TableColumn<Person, String> statusCol;

    @FXML
    private TableView<Bill> billTable;
    @FXML
    private TableColumn<Bill, String> bNameCol;
    @FXML
    private TableColumn<Bill, String> bClassCol;
    @FXML
    private TableColumn<Bill, String> bDateCol;
    @FXML
    private TableColumn<Bill, String> bPriceCol;
    @FXML
    private TableColumn<Bill, String> bStatusCol;
    @FXML
    private TableColumn<Bill, String> bUpdateCol;
    @FXML
    private StackPane billStkPane;

    public void refreshBillTable(){
        final ObservableList<Bill> BillList = FXCollections.observableArrayList();
        Accounts acc = new Accounts();
        ResultSet rs = acc.getBillInfoByAccountId(student.getId());
        try{
            while (rs.next()){
                BillList.add(new Bill(
                        rs.getInt("id"),
                        student.getName(),
                        student.getClassName(),
                        rs.getString("time"),
                        student.getFee(),
                        rs.getInt("status") == 1 ? "Đã đóng" : "Chưa đóng"
                ));
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        bNameCol.setCellValueFactory(new PropertyValueFactory<>("bName"));
        bClassCol.setCellValueFactory(new PropertyValueFactory<>("bClass"));
        bDateCol.setCellValueFactory(new PropertyValueFactory<>("bDate"));
        bPriceCol.setCellValueFactory(new PropertyValueFactory<>("bPrice"));
        bStatusCol.setCellValueFactory(new PropertyValueFactory<>("bStatus"));
        billTable.setItems(BillList);

        Callback<TableColumn<Bill, String>, TableCell<Bill, String>> cellFoctory = (TableColumn<Bill, String> param) -> {
            final TableCell<Bill, String> cell = new TableCell<>() {
                @Override
                public void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty) {
                        setGraphic(null);
                        setText(null);
                    } else {
                        Bill bill = getTableView().getItems().get(getIndex());
                        if (!bill.getBStatus().equals("Đã đóng")){
                            Label lb = new Label("Cập nhật");
                            lb.setTextFill(Color.WHITE);

                            HBox hbox = new HBox(lb);
                            hbox.setMaxWidth(100);
                            hbox.setStyle("-fx-alignment:center; -fx-cursor: hand; -fx-background-color:  #30475E; -fx-background-radius: 8;");

                            hbox.setOnMouseClicked((MouseEvent event) -> {
//                                acc.updateBillStatus(billTable.getSelectionModel().getSelectedItem().getBId());
//                                refreshBillTable();

                                System.out.println(bill.getBId());
                            });
                            hbox.setOnMouseEntered((MouseEvent event) -> {
                                HBox myHbox = (HBox) event.getSource();
                                myHbox.setEffect(new ColorAdjust(0,0,0,-0.2));
                            });
                            hbox.setOnMouseExited((MouseEvent event) -> {
                                HBox myHbox = (HBox) event.getSource();
                                myHbox.setEffect(new ColorAdjust(0,0,0,0));
                            });

                            setGraphic(hbox);
                            setText(null);
                        } else{
                            FontIcon font = new FontIcon("fas-check-circle");
                            font.setIconSize(12);
                            HBox hb = new HBox(font);
                            hb.setMaxWidth(100);
                            hb.setStyle("-fx-alignment:center;-fx-background-color:  #FFFFFF;");
                            setGraphic(hb);
                            setText(null);
                        }


                    }
                }
            };
            return cell;
        };
        bUpdateCol.setCellFactory(cellFoctory);

    }
    @FXML
    public void refreshStudentTable() {
        final ObservableList<Person> StudentList = FXCollections.observableArrayList();
        Accounts acc = new Accounts();
        ResultSet rs = acc.studentsInfo();
        try{
            while(rs.next()) {
                StudentList.add(new Person(
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
        pNameCol.setCellValueFactory(new PropertyValueFactory<>("pName"));
        pPhoneCol.setCellValueFactory(new PropertyValueFactory<>("pPhone"));
        pEmailCol.setCellValueFactory(new PropertyValueFactory<>("pEmail"));
        feeCol.setCellValueFactory(new PropertyValueFactory<>("fee"));
        classNameCol.setCellValueFactory(new PropertyValueFactory<>("className"));
        statusCol.setCellValueFactory(new PropertyValueFactory<>("status"));

        studentsTable.setItems(StudentList);
        System.out.println("refresh!!!!!!!!!");
    }

    @FXML
    private void onAddStudentClick(MouseEvent event){
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
            stage.centerOnScreen();
            stage.show();
            stage.setOnCloseRequest((event1) -> {
                refreshStudentTable();
            });
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    @FXML
    public void onEditStudentClick() {
        billStkPane.setVisible(false);
        // Lay thong tin student de hien len form
        student = studentsTable.getSelectionModel().getSelectedItem();
//        TableView.TableViewSelectionModel<Student> studentSelection = studentsTable.getSelectionModel();
        if(student != null) {
            System.out.println(student.getName());
            try {
                FXMLLoader loader = new FXMLLoader(App.class.getResource("add-student.fxml"));
                Parent root = loader.load();
                AddStudentController addStudentController = loader.getController();
                addStudentController.setTextField(student.getName(), student.getAge(), student.getGender(), student.getEmail(), student.getPassword(), student.getPhone(), student.getAddress(), student.getClassName(), student.getPName(), student.getPPhone(), student.getPEmail(), student.getFee(), student.getStatus());
                addStudentController.setEdit(true);
                addStudentController.setStudentId(student.getId());

                Stage stage = new Stage();
                Scene scene = new Scene(root);
                stage.setScene(scene);
                stage.initStyle(StageStyle.TRANSPARENT);
                stage.initModality(Modality.APPLICATION_MODAL);
                stage.show();
                stage.setOnCloseRequest((event) -> {
                    refreshStudentTable();
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
    public void onDeleteStudentClick(){
        // Lay id student va bo vao ham xoa
        student = studentsTable.getSelectionModel().getSelectedItem();
        if(student != null){
            int studentId = student.getId();
            acc.deleteStudentById(studentId);
            refreshStudentTable();
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
        refreshStudentTable();
    }
    boolean firstClick = true;
    public void onStudentTableClick(MouseEvent event){
        student = studentsTable.getSelectionModel().getSelectedItem();
        if(student != null){
            billStkPane.setVisible(firstClick);
            double posX = event.getX();
            double posY = event.getY();

            if(posX + 23 < 1080 - 526 && posY + 107 < 594-120){
                billStkPane.setLayoutX(posX + 30);
                billStkPane.setLayoutY(posY + 120);
            } else if (posX + 23 >= 1080 - 526){
                if(posY + 107 >= 594 -100){
                    billStkPane.setLayoutX(1080-526);
                    billStkPane.setLayoutY(594-100);
                } else {
                    billStkPane.setLayoutX(1080-526);
                    billStkPane.setLayoutY(posY + 120);
                }
            } else{
                billStkPane.setLayoutX(posX + 40);
                billStkPane.setLayoutY(594 - 100);
            }
//            System.out.println(posX + " " + posY);
            firstClick = !firstClick;
            refreshBillTable();
        }
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
