package raise.tech.student.management.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import raise.tech.student.management.controller.converter.StudentConverter;
import raise.tech.student.management.data.Student;
import raise.tech.student.management.data.StudentsCourses;
import raise.tech.student.management.domain.StudentDetail;
import raise.tech.student.management.repository.StudentRepository;
import raise.tech.student.management.service.StudentService;

@Controller
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
  public String getStudentsList(Model model) {
    List<Student> students = service.getActiveStudents();
    List<StudentsCourses> studentsCourses = service.searchCourseList();

    model.addAttribute("studentList", converter.convertStudentDetails(students, studentsCourses));
    return "studentList";
  }


  //新規受講生登録の画面表示
  @GetMapping("/newStudent")
  public String newStudent(Model model) {
    StudentDetail studentDetail = new StudentDetail();
    studentDetail.setStudentsCourses(Arrays.asList(new StudentsCourses()));
    model.addAttribute("studentDetail", studentDetail);
    return "registerStudent";
  }


  //新規受講生の登録処理
  @PostMapping("/registerStudent")
  public String registerStudent(@ModelAttribute StudentDetail studentDetail, BindingResult result) {
    if (result.hasErrors()) {
      return "registerStudent";
    }
    service.saveStudent(studentDetail);
    return "redirect:/studentsList";
  }

  //受講生情報の更新
  @GetMapping("/student/{id}")
  public String getStudent(@PathVariable Long id, Model model) {
    StudentDetail studentDetail = service.searchStudentById(id);

    if (studentDetail.getStudentsCourses() == null){
        studentDetail.setStudentsCourses(new ArrayList<>());
    }
    model.addAttribute("studentDetail", studentDetail);
    return "updateStudent";
  }


  //受講生情報の更新登録処理
  @PostMapping("/updateStudent/{id}")
  public String updateStudent(@ModelAttribute StudentDetail studentDetail, BindingResult result) {
    if (result.hasErrors()) {
      return "updateStudent";
    }
    service.updateStudent(studentDetail);
    return "redirect:/studentsList";
  }

}


