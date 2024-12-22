package javafx.javafx1;

import javafx.animation.*;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Side;
import javafx.scene.chart.*;
import javafx.scene.control.Label;
import javafx.scene.effect.BlurType;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.Effect;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.util.Duration;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class DashboardStudentController extends App implements Initializable {
    Accounts acc = new Accounts();
    double maxLesson = 10;
    double maxStudent = 10;
//    int count = 0;

    @FXML
    PieChart agePChart;
    @FXML
    Label avgStudentAgeLb;
    @FXML
    Label boyLb;
    @FXML
    Label girlLb;
    @FXML
    CategoryAxis ctgAxis;
    @FXML
    NumberAxis numAxis;
    @FXML
    LineChart totalStudentLChart;
    @FXML
    VBox topStudentVb;
    @FXML
    VBox classInfoVb;

    private void numberAnim(Label lb, int num){
        final int[] count = {0};
        int milliDuration;
        if (num <= 10) {
            milliDuration = 100;
        } else{
            milliDuration = 30;
        }

        Timeline timeline = new Timeline(
                new KeyFrame(Duration.millis(milliDuration), event -> {
                    if (count[0] < num) {
                        count[0]++;
                        lb.setText(Integer.toString(count[0]));
                    }
                })
        );
        timeline.setCycleCount(num);
        timeline.play();
    }

    private HBox addStudentInTop(int top, String name, String className, String address, int score){
        HBox hbox = new HBox();
        hbox.setPrefWidth(287);
        hbox.setPrefHeight(66);

        Label topLb = new Label(Integer.toString(top));
        topLb.setAlignment(Pos.CENTER);
        topLb.setFont(new Font(22));
        topLb.setTextFill(Color.BLACK);
        topLb.setPrefHeight(66);
        topLb.setPrefWidth(66);
        topLb.setStyle("-fx-border-color: #8A3030;-fx-font-weight: bold; -fx-border-width: 0 0 1 0;");

        VBox vBox = new VBox();
        vBox.setPrefHeight(66);
        vBox.setPrefWidth(179);
        vBox.setPadding(new Insets(0,0,0,10));

        Label scoreLb = new Label(Integer.toString(score) + "đ");
        scoreLb.setPrefHeight(66);
        scoreLb.setPrefWidth(66);
        scoreLb.setFont(new Font(22));
        scoreLb.setAlignment(Pos.CENTER);
        scoreLb.setStyle("-fx-font-weight: bold; -fx-background-color:  #DCDCDC; -fx-background-radius: 90;-fx-background-insets: 7;");

        Label nameLb = new Label(name);
        nameLb.setPrefWidth(240);
        nameLb.setPrefHeight(44);
        nameLb.setFont(new Font(24));
        nameLb.setTextFill(Paint.valueOf("#30475e"));
        nameLb.setStyle("-fx-font-weight: bold;");
        nameLb.setAlignment(Pos.BOTTOM_LEFT);


        Label infoStudentLb = new Label(className + ", " + address);
        infoStudentLb.setPrefWidth(240);
        infoStudentLb.setPrefHeight(22);
        infoStudentLb.setFont(new Font(12));
        infoStudentLb.setAlignment(Pos.TOP_LEFT);


        vBox.getChildren().addAll(nameLb, infoStudentLb);
        hbox.getChildren().addAll(topLb, vBox, scoreLb);

        return hbox;
    }
    private VBox addClassBox(String name, int total_lesson, int total_student){

        double maxWidth = 220;

        DropShadow ds = new DropShadow();
        ds.setBlurType(BlurType.THREE_PASS_BOX);
        ds.setWidth(8);
        ds.setHeight(8);
        ds.setRadius(3.5);


        VBox vBox = new VBox();
        vBox.setPrefHeight(70);
        vBox.setPrefWidth(250);
        vBox.setPadding(new Insets(0,0,0,15));
        vBox.setStyle("-fx-background-color:  #FFFFFF; -fx-background-radius: 12;");
        vBox.setEffect(ds);

        Label lb = new Label(name);
        lb.setPrefHeight(32);
        lb.setPrefWidth(90);
        lb.setFont(new Font(14));
        lb.setStyle("-fx-font-weight: bold;");
        lb.setAlignment(Pos.BOTTOM_LEFT);

        Rectangle lessonRec = new Rectangle();
        lessonRec.setWidth(maxWidth * (total_lesson / maxLesson) + 4);
        lessonRec.setHeight(12);
        lessonRec.setStrokeWidth(0);
        lessonRec.setFill(Paint.valueOf("#F05454"));

        Rectangle studentRec = new Rectangle();
        studentRec.setWidth(maxWidth * (total_student / maxStudent) + 4);
        studentRec.setHeight(12);
        studentRec.setStrokeWidth(0);
        studentRec.setFill(Paint.valueOf("#30475E"));

        vBox.getChildren().addAll(lb, lessonRec, studentRec);
        return vBox;
    }

    private void topStudentSetup(){
        ResultSet rs = acc.getTopStudent();
        int top = 1;
        try{
            while(rs.next()){
                String name = rs.getString("student_name");
                int score = rs.getInt("score");
                String className = rs.getString("class_name");
                String address = rs.getString("address");
                topStudentVb.getChildren().add(addStudentInTop(top,name,className,address,score));
                top++;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    private void pChartSetup(){
        ResultSet rs = acc.getStudentAgeData();
        int under_12 = 0;
        int under_22 = 0;
        int over_22 = 0;
        try{
            while(rs.next()){
                under_12 = rs.getInt("under_12");
                under_22 = rs.getInt("under_22");
                over_22 = rs.getInt("over_22");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        PieChart.Data under12 = new PieChart.Data("Dưới 12 tuổi",under_12);
        PieChart.Data under22 = new PieChart.Data("Dưới 22 tuổi",under_22);
        PieChart.Data over22 = new PieChart.Data("Trên 22 tuổi",over_22);
        agePChart.getData().clear();
        agePChart.getData().addAll(under12, under22, over22);
        agePChart.setLabelsVisible(true);
        agePChart.setLabelLineLength(1);
        agePChart.setLegendVisible(false);
        agePChart.setStartAngle(0);
        agePChart.getData().get(0).getNode().setStyle("-fx-background-color: #F05454;");
        agePChart.getData().get(1).getNode().setStyle("-fx-background-color: #8A3030;");
        agePChart.getData().get(2).getNode().setStyle("-fx-background-color: #30475E;");

//        SequentialTransition sequentialTransition = new SequentialTransition();
//        for (PieChart.Data data : agePChart.getData()) {
//            RotateTransition rotateTransition = new RotateTransition(Duration.seconds(0.3), data.getNode());
//            rotateTransition.setByAngle(60);
//            rotateTransition.setCycleCount(1);
//            rotateTransition.setAutoReverse(false);
//
//            sequentialTransition.getChildren().add(rotateTransition);
//        }
//        sequentialTransition.play();

    }
    private void avgAgeSetup(){
        int avgAge = acc.avgStudentAge();
        numberAnim(avgStudentAgeLb, avgAge);
    }
    private void genderSetup(){
        ResultSet rs = acc.getStudentGender();
        try{
            while(rs.next()){
                numberAnim(boyLb, rs.getInt("Nam"));
                numberAnim(girlLb, rs.getInt("Nữ"));

//                boyLb.setText(Integer.toString(rs.getInt("Nam")));
//                girlLb.setText(Integer.toString(rs.getInt("Nữ")));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    private void lChartSetup(){
        totalStudentLChart.setTitle("Tổng số học viên theo tháng");
        totalStudentLChart.setLegendVisible(false);
        numAxis.setLabel("Số học viên");
        XYChart.Series<String, Number> dataSeries = new XYChart.Series<>();
        ResultSet rs = acc.getStudentStatistical();

        try{
            while (rs.next()){
                dataSeries.getData().add(new XYChart.Data<>(rs.getString("month_year"), rs.getInt("total_students")));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        totalStudentLChart.getData().add(dataSeries);
    }
    private void classInfoSetup(){
        ResultSet rs = acc.getClassStudentLesson();
        try {
            while (rs.next()){
                String name = rs.getString("class_name");
                int lesson = rs.getInt("total_lessons");
                int student = rs.getInt("total_students");
                classInfoVb.getChildren().add(addClassBox(name, lesson, student));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        pChartSetup();
        avgAgeSetup();
        genderSetup();
        lChartSetup();
        topStudentSetup();
        classInfoSetup();
    }
}
