package raise.tech.student.management.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import raise.tech.student.management.controller.converter.StudentConverter;
import raise.tech.student.management.data.Student;
import raise.tech.student.management.data.StudentsCourses;
import raise.tech.student.management.domain.StudentDetail;
import raise.tech.student.management.repository.StudentRepository;
import raise.tech.student.management.service.StudentService;

@RestController
public class StudentController {


  private final StudentService service;
  private final StudentConverter converter;
  private Student student;
  private StudentRepository repository;


  @Autowired
  public StudentController(StudentService service, StudentConverter converter,
      StudentRepository repository) {
    this.service = service;
    this.converter = converter;
    this.repository = repository;

  }

  //受講生一覧を表示
  @GetMapping("/studentsList")
  public List<StudentDetail> getStudentsList() {
    List<Student> students = service.getActiveStudents();
    List<StudentsCourses> studentsCourses = service.searchCourseList();
    return converter.convertStudentDetails(students, studentsCourses);
  }

  //新規受講生の登録処理
  @PostMapping("/registerStudent")
  public ResponseEntity<StudentDetail> registerStudent(@RequestBody StudentDetail studentDetail) {
    StudentDetail responseStudentDetail = service.saveStudent(studentDetail);
    return ResponseEntity.ok(responseStudentDetail);
  }

  //受講生情報の更新
  @GetMapping("/student/{id}")
  public StudentDetail getStudent(@PathVariable Long id) {
    return service.searchStudentById(id);
  }

  //受講生情報の更新登録処理
  @PostMapping("/updateStudent")
  public ResponseEntity<String> updateStudent(@RequestBody StudentDetail studentDetail) {
    service.updateStudent(studentDetail);
    return ResponseEntity.ok("更新処理がで成功しました。");
  }
}


