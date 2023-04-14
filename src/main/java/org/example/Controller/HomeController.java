package org.example.Controller;

import org.example.Domain.DataObject;
import org.example.Service.ServiceLayer;
import org.example.Domain.Book;
import org.example.VO.Student;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping
public class HomeController {

    private  final ServiceLayer serviceLayer;
    @Autowired
    public HomeController(ServiceLayer serviceLayer){
        this.serviceLayer=serviceLayer;
    }



    @GetMapping("/get")
    public List<Student> getStudents(Student student){
      return serviceLayer.consumeAPI(student);
    }
    @PostMapping("/post")
    public ResponseEntity<Student> registerNewStudent(@RequestBody Student student) {
        Student addedStudent = serviceLayer.addStudent(student);
        return ResponseEntity.status(HttpStatus.CREATED).body(addedStudent);
    }
    @GetMapping("/studentsBetweenDates")
    public ResponseEntity<List<Student>> getStudentsBetweenDates(
            @RequestParam("startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam("endDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        List<Student> students = serviceLayer.getStudentsBetweenDates(startDate, endDate);
        return ResponseEntity.ok(students);
    }
    @PutMapping("/students/{id}")
    public ResponseEntity<Student> updateStudent(@PathVariable Long id, @RequestBody Student student) {
        serviceLayer.updateStudent(student, id);
        return ResponseEntity.ok(student);
    }
    @GetMapping("/students/{id}")
    public ResponseEntity<Student> getStudentById(@PathVariable Long id) {
        Student student = serviceLayer.getStudentById(id);
        return ResponseEntity.ok(student);
    }

    @DeleteMapping("/students/{id}")
    public ResponseEntity<String> deleteStudent(@PathVariable Long id) {
        serviceLayer.deleteStudent(id);
        return ResponseEntity.ok("Student with id " + id + " deleted successfully.");
    }

    @GetMapping("get/book")
    public List<Book> getAllBooks() {
        return serviceLayer.getAllBooks();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Book> getBookById(@PathVariable Long id) {
        Optional<Book> bookOptional = Optional.ofNullable(serviceLayer.getBookById(id));
        if (bookOptional.isPresent()) {
            Book book = bookOptional.get();
            return ResponseEntity.ok(book);
        } else {
            return ResponseEntity.notFound().build();
        }
    }


    @PostMapping("add/book")
    public Book addBook(@RequestBody Book book) {
        return serviceLayer.addBook(book);
    }

    @PutMapping("/{id}")
    public void updateBook(@RequestBody Book book, @PathVariable Long id) {
        serviceLayer.updateBook(book, id);
    }


    @DeleteMapping("/{id}")
    public void deleteBook(@PathVariable Long id) {
        serviceLayer.deleteBook(id);
    }

    @GetMapping("/students/{id}/book")
    public ResponseEntity<Book> getStudentBookById(@PathVariable Long id) {
        try {
            Book book = serviceLayer.getStudentBookById(id);
            return ResponseEntity.ok(book);
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.notFound().build();
        }
    }
    @GetMapping("/getByName")
    public ResponseEntity<Student> getStudentByName(@RequestParam("name") String name){
        try{
            Student student = serviceLayer.getStudentByName(name);
            return ResponseEntity.ok(student);
        }catch(IllegalArgumentException ex){
            return ResponseEntity.notFound().build();
        }
    }
    @GetMapping("/getByBookName")
    public ResponseEntity<Book> getBookByName(@RequestParam("name") String name) {
        Optional<Book> optionalBook = Optional.ofNullable(serviceLayer.getBookByName(name));
        if (optionalBook.isPresent()) {
            Book book = optionalBook.get();
            return ResponseEntity.ok(book);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    @GetMapping("/dataByName")
    public ResponseEntity<DataObject> getDataByName(@RequestParam String name) {
        try {
            DataObject dataObject = serviceLayer.getDataByName(name);
            return ResponseEntity.ok(dataObject);
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.notFound().build();
        }
    }

}

