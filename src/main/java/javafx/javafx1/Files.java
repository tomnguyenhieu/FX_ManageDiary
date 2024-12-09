package javafx.javafx1;

import javafx.fxml.FXML;
import javafx.scene.control.TableView;

import java.sql.*;

public class Files
{
    private Connection connect;

    public Files() {
        this.connect = Connector.getConnection();
    }
    public int getClassId(String className)
    {
        String sql = "SELECT * FROM classes WHERE name = '" +className+"'";
        PreparedStatement ps;
        try {
            ps = connect.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            int classId = 0;
            while (rs.next())
            {
                classId = rs.getInt("id");
            }
            return classId;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public int findLessonByTitle(String title){
        String sql = "SELECT * FROM lessons WHERE title = '"+title+"'";
        try {
            PreparedStatement ps = connect.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            int idLesson = 0;
            while(rs.next())
            {
                idLesson = rs.getInt("id");
            }
            return idLesson;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    public boolean storeExcelInfo(int classId, String titleValue, String contentValue)
    {
        String sql = "INSERT INTO lessons("
            + "class_id, title, content)"
            + " VALUES(?,?,?)";
        PreparedStatement ps;
        try {
            ps = connect.prepareStatement(sql);
            ps.setInt(1, classId);
            ps.setString(2, titleValue);
            ps.setString(3, contentValue);
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
//            throw new RuntimeException(e);
            return false;
        }
    }
    public boolean storeExcelComment(int studentId, String lessonTitle, String studentComment) {
        String sql = "INSERT INTO comments("
                + "student_id, lesson_id, comment)"
                + " VALUES(?,?,?)";
        PreparedStatement ps;
        try {
            ps = connect.prepareStatement(sql);
            ps.setInt(1, studentId);
            ps.setInt(2, findLessonByTitle(lessonTitle));
            ps.setString(3, studentComment);
            ps.executeUpdate();
            return true;
        } catch (Exception e) {
//            throw new RuntimeException(e);
            return false;
        }
    }
    public ResultSet getClassInfo(int lessonId)
    {
        String sql = "SELECT classes.id, accounts.name AS teacher_name, classes.name AS class_name, lessons.title, \n" +
                "lessons.content FROM classes JOIN accounts ON classes.teacher_id = accounts.id\n" +
                "JOIN lessons ON classes.id = lessons.class_id WHERE lessons.id = '" +lessonId+ "'";
        PreparedStatement ps;
        try {
            ps = connect.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            return rs;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public ResultSet getClassComments(int lessonId)
    {
        String sql = "SELECT lessons.id AS lesson_id, accounts.name AS student_name, comments.comment "
                +"FROM lessons JOIN comments ON lessons.id = comments.lesson_id "
                + "JOIN accounts ON accounts.id = comments.student_id "
                + "WHERE lessons.id = '" +lessonId+ "'";
        PreparedStatement ps;
        try {
            ps = connect.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            return rs;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public ResultSet listLessons()
    {
        String sql = "SELECT * FROM lessons";
        PreparedStatement ps;
        try {
            ps = connect.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            return rs;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public ResultSet listLessonsByClassId(int classId)
    {
        String sql = "SELECT * FROM lessons WHERE class_id = " +classId;
        PreparedStatement ps;
        try {
            ps = connect.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            return rs;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
