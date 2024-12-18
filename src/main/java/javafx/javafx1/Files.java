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
    public ResultSet listClasses()
    {
        String sql = "SELECT * FROM classes WHERE deleted = 1";
        PreparedStatement ps;
        try {
            ps = connect.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            return rs;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public ResultSet getClassInfoByClassId(int classId)
    {
        String sql = "SELECT c.name AS class_name, a_teacher.name AS teacher_name, "
                + "COUNT(a_student.id) AS total_students FROM classes c "
                + "LEFT JOIN accounts a_teacher ON c.teacher_id = a_teacher.id "
                + "AND a_teacher.role = 2 LEFT JOIN accounts a_student "
                + "ON c.id = a_student.class_id AND a_student.role = 4 "
                + "WHERE c.id = "+classId+" AND a_teacher.status = 1 GROUP BY c.name, a_teacher.name";
        PreparedStatement ps;
        try {
            ps = connect.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            return rs;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public boolean storeClass(String className, int teacherId)
    {
        String sql = "INSERT INTO classes(teacher_id, name, deleted) VALUES(?, ?, ?)";
        PreparedStatement ps;
        try {
            ps = connect.prepareStatement(sql);
            ps.setInt(1, teacherId);
            ps.setString(2, className);
            ps.setInt(3, 1);
            ps.executeUpdate();
            return true;
        } catch (Exception e) {
//            throw new RuntimeException(e);
            return false;
        }
    }
    public int getTeacherId(String teacherName)
    {
        String sql = "SELECT * FROM accounts WHERE NAME = '" +teacherName+ "' AND role = 2";
        int teacherId = 0;
        PreparedStatement ps;
        try {
            ps = connect.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next())
            {
                teacherId = rs.getInt("id");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return teacherId;
    }
    public ResultSet getTeachersName()
    {
        String sql = "SELECT * FROM accounts WHERE role = 2 AND status = 1";
        PreparedStatement ps;
        try {
            ps = connect.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            return rs;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public boolean updateClass(String className, int teacherId, int classId)
    {
        String sql = "UPDATE classes SET name = '" +className+ "', teacher_id = '" +teacherId+ "'  WHERE id = '" +classId+ "'";
        PreparedStatement ps;
        try {
            ps = connect.prepareStatement(sql);
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public void deleteClass(int classId)
    {
        String sql = "UPDATE classes SET deleted = 2 WHERE id = " + classId;
        PreparedStatement ps;
        try {
            ps = connect.prepareStatement(sql);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public ResultSet getCommentByLessonId(int lessonId)
    {
        String sql = "SELECT comments.student_id, accounts.name, comments.comment "
                + "FROM comments JOIN accounts "
                + "ON comments.student_id = accounts.id "
                + "WHERE lesson_id = " + lessonId;
        PreparedStatement ps;
        try {
            ps = connect.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            return rs;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public ResultSet getCommentTitleByLessonId(int lessonId)
    {
        String sql = "SELECT * FROM lessons WHERE id = " + lessonId;
        PreparedStatement ps;
        try {
            ps = connect.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            return rs;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public String getClassNameByClassId(int classId)
    {
        String sql = "SELECT name FROM classes WHERE id = " + classId;
        PreparedStatement ps;

        String className = "";
        try {
            ps = connect.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next())
            {
                className = rs.getString("name");
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return className;
    }
}
