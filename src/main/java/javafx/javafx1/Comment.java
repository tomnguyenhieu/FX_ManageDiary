package javafx.javafx1;

import javafx.beans.property.SimpleStringProperty;

public class Comment {
    // Table 1
    private SimpleStringProperty commentTitle;
    private SimpleStringProperty commentContent;
    private SimpleStringProperty commentClassName;
    private SimpleStringProperty commentTeacherName;

    // Table 2
    private SimpleStringProperty commentStudentName;
    private SimpleStringProperty commentStudentComment;
    private int commentStudentScore;

    public Comment() {};

    public Comment(String cStudentName, String cStudentComment, int cStudentScore) {
        this.commentStudentName = new SimpleStringProperty(cStudentName);
        this.commentStudentComment = new SimpleStringProperty(cStudentComment);
        this.commentStudentScore = cStudentScore;
    }

    public Comment(String cTitle, String cContent, String cClassName, String cTeacherName) {
        this.commentTitle = new SimpleStringProperty(cTitle);
        this.commentContent = new SimpleStringProperty(cContent);
        this.commentClassName = new SimpleStringProperty(cClassName);
        this.commentTeacherName = new SimpleStringProperty(cTeacherName);
    }

    // Getter Table 1
    public String getCommentTitle() {
        return commentTitle.get();
    }

    public String getCommentContent() {
        return commentContent.get();
    }

    public String getCommentClassName() {
        return commentClassName.get();
    }

    public String getCommentTeacherName() {
        return commentTeacherName.get();
    }

    // Getter Table 2
    public String getCommentStudentName() {
        return commentStudentName.get();
    }

    // Getter for commentStudentComment
    public String getCommentStudentComment() {

        return commentStudentComment.get();
    }

    public int getCommentStudentScore() {
        return commentStudentScore;
    }
}

