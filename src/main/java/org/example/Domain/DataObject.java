package org.example.Domain;

import org.example.VO.Student;

public class DataObject {
    private Student student;
    private Book book;

    public DataObject(Student student, Book book) {
        this.student = student;
        this.book = book;
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }
}
