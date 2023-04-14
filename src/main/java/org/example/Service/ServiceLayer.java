package org.example.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.Domain.DataObject;
import org.example.Repository.BookRepository;
import org.example.Domain.Book;
import org.example.VO.Student;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class ServiceLayer {
        @Autowired
        private BookRepository bookRepository;
        @Autowired

        private final RestTemplate restTemplate;
        private static final String BASE_URL = "http://localhost:8081";

        @Autowired
        public ServiceLayer(RestTemplate restTemplate) {
            this.restTemplate = restTemplate;
        }

        public List<Student> consumeAPI(Student student) {
            String url = BASE_URL + "/get";
            return restTemplate.getForObject(url, List.class, student);
        }

    public Student addStudent(Student student) {
        String url = BASE_URL + "/post";
        try {
            ResponseEntity<Student> responseEntity = restTemplate.postForEntity(url, student, Student.class);
            return responseEntity.getBody();
        } catch (Exception e) {
            return null;
        }
    }

    public List<Student> getStudentsBetweenDates(LocalDateTime startDate, LocalDateTime endDate) {
        String url = BASE_URL + "/studentsBetweenDates";
        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(url)
                .queryParam("startDate", startDate)
                .queryParam("endDate", endDate);
        URI uri = builder.build().toUri();
        return restTemplate.getForObject(uri, List.class);
    }
    public void updateStudent(Student student, Long id){
        String url = BASE_URL + "/update/{id}";
        RestTemplate restTemplate = new RestTemplate();
        URI uri = UriComponentsBuilder.fromUriString(url)
                .buildAndExpand(id)
                .toUri();
        HttpEntity<Student> requestEntity = new HttpEntity<>(student);
        restTemplate.put(uri, requestEntity);
    }
    public Student getStudentById(Long id) {
        String url = BASE_URL + "/students/{id}";
        try {
            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<Student> responseEntity = restTemplate.getForEntity(url, Student.class, id);
            return responseEntity.getBody();
        } catch (HttpClientErrorException.NotFound ex) {
            throw new IllegalArgumentException("Student with id " + id + " not found.", ex);
        }
    }

    public Student  getStudentByName(String name){
        String url = BASE_URL + "/getByName?name={name}";
            try{
                RestTemplate restTemplate = new RestTemplate();
                ResponseEntity<Student> responseEntity = restTemplate.getForEntity(url, Student.class, name);
                return responseEntity.getBody();
            }catch(HttpClientErrorException.NotFound ex){
                throw new IllegalStateException("Student with name" +name+ "not found");
            }
    }
    public Book getBookByName(String name){
        Optional<Book> optionalBook = (Optional<Book>) bookRepository.findByName(name);
        if(optionalBook.isPresent()) {
            return optionalBook.get();
        }
        return null;
    }

    public DataObject getDataByName(String name) {
        Student student = getStudentByName(name);
        Optional<Book> optionalBook = bookRepository.findByName(name);
        if (student != null && optionalBook.isPresent()) {
            Book book = optionalBook.get();
            return new DataObject(student, book);
        } else {
            throw new IllegalArgumentException("Data not found for name: " + name);
        }
    }

    public void deleteStudent(Long id) {
        String url = BASE_URL + "/delete/{id}";
        RestTemplate restTemplate = new RestTemplate();
        URI uri = UriComponentsBuilder.fromUriString(url)
                .buildAndExpand(id)
                .toUri();
        restTemplate.delete(uri);
    }
    public Book addBook(Book book){
           return bookRepository.save(book);
    }

    public void deleteBook(Long id){
            bookRepository.deleteById(id);
    }
    public void updateBook(Book book, Long id) {
        Book existingBook = bookRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Book not found with id " + id));
        existingBook.setName(book.getName());
        existingBook.setAuthor(book.getAuthor());
        bookRepository.save(existingBook);
    }

    public Book getBookById(Long id){
            return bookRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Book not found with id :"+ id));
    }

    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }
    public Book getStudentBookById(Long studentId) {
        String url = BASE_URL + "/students/{id}/book";
        try {
            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<Book> responseEntity = restTemplate.getForEntity(url, Book.class, studentId);
            return responseEntity.getBody();
        } catch (HttpClientErrorException.NotFound ex) {
            throw new IllegalArgumentException("Book for student with id " + studentId + " not found.", ex);
        }
    }



}


