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

public class ManageTeacherController extends App implements Initializable {
    Person teacher = null;
    Accounts acc = new Accounts();
    double orgSceneX, orgSceneY, orgTranslateX, orgTranslateY;

    @FXML
    private TableView<Person> teachersTable;
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

    @FXML
    private TableView<Bill> teacherDetailTable;
    @FXML
    private TableColumn<Bill, String> detailIdCol;
    @FXML
    private TableColumn<Bill, String> detailNameCol;
    @FXML
    private TableColumn<Bill, String> monthCol;
    @FXML
    private TableColumn<Bill, String> lessonQtyCol;
    @FXML
    private TableColumn<Bill, String> monthSalaryCol;
    @FXML
    private TableColumn<Bill, String> detailStatusCol;
    @FXML
    private TableColumn<Bill, String> updateSalarySttCol;

    @FXML
    private StackPane detailPane;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        refreshTableTeachers();
        detailPane.setVisible(false); // Ẩn detailPane khi khởi tạo
    }

    // Làm mới bảng giáo viên
    @FXML
    public void refreshTableTeachers() {
        final ObservableList<Person> TeacherList = FXCollections.observableArrayList();
        ResultSet rs = acc.getTeachersInfo(2);
        try {
            while (rs.next()) {
                TeacherList.add(new Person(
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
                        rs.getString("status")
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

        teachersTable.setItems(TeacherList);
    }

    // Làm mới bảng chi tiết giáo viên
    private void updateTeacherDetails(boolean isSelectionChange) {
        if (teacher != null || isSelectionChange) {
            if (isSelectionChange) {
                teacher = teachersTable.getSelectionModel().getSelectedItem();
            }

            ObservableList<Bill> billList = FXCollections.observableArrayList();
            ResultSet rs = acc.getTeacherInfoByAccountId(teacher.getId());

            try {
                while (rs.next()) {
                    billList.add(new Bill(
                            rs.getInt("bill_id"),
                            teacher.getName(),
                            rs.getString("month_taught"),
                            rs.getInt("lessons_count"),
                            rs.getInt("monthly_salary"),
                            rs.getInt("salary_status") == 1 ? "Chưa trả" : "Đã trả"
                    ));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            detailIdCol.setCellValueFactory(new PropertyValueFactory<>("id"));
            detailNameCol.setCellValueFactory(new PropertyValueFactory<>("bName"));
            monthCol.setCellValueFactory(new PropertyValueFactory<>("month"));
            lessonQtyCol.setCellValueFactory(new PropertyValueFactory<>("lessonQty"));
            monthSalaryCol.setCellValueFactory(new PropertyValueFactory<>("monthlySalary"));
            detailStatusCol.setCellValueFactory(new PropertyValueFactory<>("bStatus"));

            Callback<TableColumn<Bill, String>, TableCell<Bill, String>> cellFactory = (TableColumn<Bill, String> param) -> {
                final TableCell<Bill, String> cell = new TableCell<>() {
                    @Override
                    public void updateItem(String item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                            setText(null);
                        } else {
                            Bill bill = getTableView().getItems().get(getIndex());
                            if ("Đã trả".equals(bill.getBStatus())) {
                                setGraphic(null);
                                setText(null);
                            } else {
                                Label lb = new Label("Cập nhật");
                                lb.setTextFill(Color.WHITE);

                                HBox hbox = new HBox(lb);
                                hbox.setMaxWidth(100);
                                hbox.setStyle("-fx-alignment:center; -fx-cursor: hand; -fx-background-color:  #30475E; -fx-background-radius: 8;");
                                hbox.setOnMouseClicked(event -> {
                                    acc.updateSalaryStatus(bill.getId());
                                    updateTeacherDetails(false);
                                    System.out.println(bill.getId());
                                });

                                setGraphic(hbox);
                                setText(null);
                            }
                        }
                    }
                };
                return cell;
            };
            updateSalarySttCol.setCellFactory(cellFactory);

            teacherDetailTable.setItems(billList);
        }
    }

    // Sự kiện khi chọn một hàng trong bảng teachersTable

    /**
     *
     * @param event
     */

    boolean firstClick = true;
    @FXML
    public void onTeacherSelected(MouseEvent event) {
        teacher = teachersTable.getSelectionModel().getSelectedItem();
        if (teacher != null) {
            detailPane.setVisible(true); // Hiển thị detailPane
            detailPane.setVisible(firstClick);
            double posX = event.getX();
            double posY = event.getY();

            if(posX + 23 < 1080 - 526 && posY + 107 < 594-120){
                detailPane.setLayoutX(posX + 30);
                detailPane.setLayoutY(posY + 120);
            } else if (posX + 23 >= 1080 - 526){
                if(posY + 107 >= 594 -100){
                    detailPane.setLayoutX(1080-526);
                    detailPane.setLayoutY(594-100);
                } else {
                    detailPane.setLayoutX(1080-526);
                    detailPane.setLayoutY(posY + 120);
                }
            } else{
                detailPane.setLayoutX(posX + 40);
                detailPane.setLayoutY(594 - 100);
            }
//            System.out.println(posX + " " + posY);
            firstClick = !firstClick;
            updateTeacherDetails(true); // Làm mới bảng teacherDetailTable
        } else {
            detailPane.setVisible(false); // Ẩn detailPane nếu không chọn hàng nào
        }
    }

    // Thêm giáo viên mới
    @FXML
    public void onAddTeacherClick() {
        try {
            FXMLLoader loader = new FXMLLoader(App.class.getResource("addTeacher.fxml"));
            Parent root = loader.load();
            AddTeacherController addTeacherController = loader.getController();
            addTeacherController.setEdit(false);

            Stage stage = new Stage();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.initStyle(StageStyle.TRANSPARENT);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.show();
            stage.setOnCloseRequest((event) -> refreshTableTeachers());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    // Chỉnh sửa thông tin giáo viên
    @FXML
    public void onEditTeacherClick() {
        teacher = teachersTable.getSelectionModel().getSelectedItem();
        if (teacher != null) {
            try {
                FXMLLoader loader = new FXMLLoader(App.class.getResource("addTeacher.fxml"));
                Parent root = loader.load();
                AddTeacherController addTeacherController = loader.getController();
                addTeacherController.setTeacherId(teacher.getId());
                addTeacherController.setTextField(
                        teacher.getName(),
                        teacher.getAge(),
                        teacher.getGender(),
                        teacher.getEmail(),
                        teacher.getPassword(),
                        teacher.getPhone(),
                        teacher.getAddress(),
                        teacher.getStatus(),
                        teacher.getCertificate(),
                        teacher.getSalary()
                );
                addTeacherController.setEdit(true);

                Stage stage = new Stage();
                Scene scene = new Scene(root);
                stage.setScene(scene);
                stage.initStyle(StageStyle.TRANSPARENT);
                stage.initModality(Modality.APPLICATION_MODAL);
                stage.show();
                stage.setOnCloseRequest((event) -> refreshTableTeachers());
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setHeaderText(null);
            alert.setContentText("Chọn một giáo viên để sửa");
            alert.showAndWait();
        }
    }

    // Xóa giáo viên
    @FXML
    public void onDeleteTeacherClick(MouseEvent event) {
        teacher = teachersTable.getSelectionModel().getSelectedItem();
        if (teacher != null) {
            int teacherId = teacher.getId();
            acc.deleteTeacherById(teacherId);
            refreshTableTeachers();
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText(null);
            alert.setContentText("Xóa giáo viên thành công");
            alert.showAndWait();
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setHeaderText(null);
            alert.setContentText("Chọn vào giáo viên để xóa!");
            alert.showAndWait();
        }
    }

    // Hiệu ứng nút (ví dụ: thay đổi kích thước khi nhấn vào)
    @FXML
    public void onBtnAddPressed(MouseEvent event) {
        orgSceneX = event.getSceneX();
        orgSceneY = event.getSceneY();
        StackPane stkPane = (StackPane) event.getSource();
        stkPane.setScaleY(1.1);
        stkPane.setScaleX(1.1);
        orgTranslateX = stkPane.getTranslateX();
        orgTranslateY = stkPane.getTranslateY();
    }

    @FXML
    public void onBtnAddDragged(MouseEvent event) {
        double offsetX = event.getSceneX() - orgSceneX;
        double offsetY = event.getSceneY() - orgSceneY;
        double newTranslateX = orgTranslateX + offsetX;
        double newTranslateY = orgTranslateY + offsetY;
        ((StackPane) event.getSource()).setTranslateX(newTranslateX);
        ((StackPane) event.getSource()).setTranslateY(newTranslateY);
    }

    @FXML
    public void onBtnExit(MouseEvent event) {
        StackPane stkPane = (StackPane) event.getSource();
        stkPane.setScaleY(1);
        stkPane.setScaleX(1);
    }
}