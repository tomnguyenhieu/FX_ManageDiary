package javafx.javafx1;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.*;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;


import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.time.LocalDate;
import java.util.SequencedSet;

public class DBTeachersStaffsController implements Initializable
{
    private LocalDate today = LocalDate.now();
    private Integer currentMonth = today.getMonthValue();
    private Integer currentYear = today.getYear();
    private String time = currentMonth.toString() + "/" + currentYear.toString();
    private int year = 0;

    @FXML
    private LineChart<String, Number> lineChart;
    @FXML
    private CategoryAxis xAxis;
    @FXML
    private NumberAxis yAxis;
    @FXML
    private Label totalTeachersLabel;
    @FXML
    private Label averageTeacherAgesLabel;
    @FXML
    private Label totalStaffsLabel;
    @FXML
    private Label averageStaffAgesLabel;
    @FXML
    private ComboBox<Integer> cbYears;
    @FXML
    private TableView<Person> tblTeachers;
    @FXML
    private TableColumn<Person, Integer> idCol;
    @FXML
    private TableColumn<Person, String> nameCol;
    @FXML
    private BarChart<String, Number> barChart;
    @FXML
    private CategoryAxis xAxis2;
    @FXML
    private NumberAxis yAxis2;

    // Xu ly logic
    public void renderLineChart()
    {
        Files files = new Files();
        yAxis.setLabel("Số lượng");

        // Giáo viên
        XYChart.Series<String, Number> dataSeriesTeachers = new XYChart.Series<>();
        ResultSet rs = files.getTeachersStatistical();
        try {
            while (rs.next())
            {
                dataSeriesTeachers.getData().add(new XYChart.Data<>(rs.getString("month"), rs.getInt("total_teachers")));
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        // Nhân viên
        XYChart.Series<String, Number> dataSeriesStaffs = new XYChart.Series<>();
        ResultSet rs1 = files.getStaffsStatistical();
        try {
            while (rs1.next())
            {
                dataSeriesStaffs.getData().add(new XYChart.Data<>(rs1.getString("month"), rs1.getInt("total_staffs")));
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        dataSeriesTeachers.setName("Giáo viên");
        dataSeriesStaffs.setName("Nhân viên");

        lineChart.getData().add(dataSeriesTeachers);
        lineChart.getData().add(dataSeriesStaffs);

    }
    public void updateTeacherBox()
    {
        Files files = new Files();
        ResultSet rs = files.getTeachersStatistical();
        try {
            while (rs.next())
            {
                String month = rs.getString("month");
                if (month.equals(time))
                {
                    Integer _totalTeachers = rs.getInt("total_teachers");
                    String strTotalTeachers = _totalTeachers.toString();
                    totalTeachersLabel.setText(strTotalTeachers);

                    Integer _averageAges = rs.getInt("avg_age");
                    String strAverageAges = _averageAges.toString();
                    averageTeacherAgesLabel.setText(strAverageAges);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }
    public void updateStaffBox()
    {
        Files files = new Files();
        ResultSet rs = files.getStaffsStatistical();
        try {
            while (rs.next())
            {
                String month = rs.getString("month");
                if (month.equals(time))
                {
                    Integer _totalStaffs = rs.getInt("total_staffs");
                    String strTotalTeachers = _totalStaffs.toString();
                    totalStaffsLabel.setText(strTotalTeachers);

                    Integer _averageAges = rs.getInt("avg_age");
                    String strAverageAges = _averageAges.toString();
                    averageStaffAgesLabel.setText(strAverageAges);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }
    public void initComboBoxYears()
    {
        Files files = new Files();
        ResultSet rs = files.getYears();
        try {
            while (rs.next())
            {
                String time = rs.getString("year");
                int year = Integer.parseInt(time);
                cbYears.getItems().add(year);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public void renderBarChart(ResultSet rs)
    {
        yAxis2.setLabel("Số lượng");

        XYChart.Series<String, Number> dataSeries = new XYChart.Series<>();
        try {
            while (rs.next())
            {
                dataSeries.getData().add(new XYChart.Data<>(rs.getString("month"), rs.getInt("total_lessons")));
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        barChart.getData().clear();
        barChart.getData().add(dataSeries);
    }

    // Xu ly event
    public void onActionChooseYear(ActionEvent event)
    {
        final ObservableList<Person> data = FXCollections.observableArrayList();
        Files files = new Files();
        year = cbYears.getValue();
        ResultSet rs = files.getTeachersListByYear(year);

        try {
            while (rs.next())
            {
                int teacherId = rs.getInt("id");
                String teacherName = rs.getString("teacher_name");

                data.add(new Person(teacherId, teacherName));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));

        tblTeachers.setItems(data);
    }
    public void onMouseClickGetTeacher()
    {
        Person teacher = tblTeachers.getSelectionModel().getSelectedItem();
        if (teacher != null)
        {
            int id = teacher.getId();

            Files files = new Files();
            ResultSet rs = files.getTeacherJobsPerMonth(id, year);
            renderBarChart(rs);
        }

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        renderLineChart();
        updateTeacherBox();
        updateStaffBox();
        initComboBoxYears();
    }
}
