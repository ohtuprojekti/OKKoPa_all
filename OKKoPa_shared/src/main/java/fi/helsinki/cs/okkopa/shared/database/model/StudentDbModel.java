package fi.helsinki.cs.okkopa.shared.database.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Kurki database model for student.
 */
@DatabaseTable(tableName = "OPISKELIJA")
public class StudentDbModel {

    @DatabaseField(columnName = "OPNRO")
    private String studentNumber;

    public StudentDbModel() {
    }

    /**
     * Creates new student model with give student-number.
     * @param studentNumber
     */
    public StudentDbModel(String studentNumber) {
        this.studentNumber = studentNumber;
    }

    public String getStudentNumber() {
        return studentNumber;
    }

    public void setStudentNumber(String studentNumber) {
        this.studentNumber = studentNumber;
    }
}