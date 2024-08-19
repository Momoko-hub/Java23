package raise.tech.student.management.controller;

import java.util.Arrays;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import raise.tech.student.management.controller.converter.StudentConverter;
import raise.tech.student.management.data.Student;
import raise.tech.student.management.data.StudentsCourses;
import raise.tech.student.management.domain.StudentDetail;
import raise.tech.student.management.service.StudentService;

@Controller
public class StudentController {


  private StudentService service;
  private StudentConverter converter;

  @Autowired
  public StudentController(StudentService service, StudentConverter converter) {
    this.service = service;
    this.converter = converter;
  }

  @GetMapping("/studentsList")
  public String getStudentsList (Model model) {
    List<Student> students = service.searchStudentsList();
    List<StudentsCourses> studentsCourses= service.searchCourseList();

    model.addAttribute("studentList", converter.convertStudentDetails(students, studentsCourses));
    return "studentList";
  }



  @GetMapping("/studentsCoursesList")
  public List<StudentsCourses> getCourseList(){
    return service.searchCourseList();
  }



}
