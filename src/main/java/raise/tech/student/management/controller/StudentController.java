package raise.tech.student.management.controller;


import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
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

/**
 * 受講生の検索や登録、更新などを行うREST APIとして実行されるControllerです。
 */
@RestController
public class StudentController {


  private final StudentService service;
  private final StudentConverter converter;
  private StudentRepository repository;


  @Autowired
  public StudentController(StudentService service, StudentConverter converter,
      StudentRepository repository) {
    this.service = service;
    this.converter = converter;
    this.repository = repository;

  }

  /**
   * 受講生一覧検索です。
   * 全件検索を行うので、条件指定は行わないものになります。
   *
   * @return 受講生一覧（全件）
   */
  @GetMapping("/studentsList")
  public List<StudentDetail> getStudentsList() {
    List<Student> students = service.searchStudentList();
    List<StudentsCourses> studentsCourses = service.searchCourseList();
    return converter.convertStudentDetails(students, studentsCourses);
  }

  /**
   * 受講生検索です。
   * IDに紐づく任意の受講生の情報を取得します。
   * @param id　受講生ID
   * @return 受講生
   */
  //受講生情報の更新
  @GetMapping("/student/{id}")
  public StudentDetail getStudent(@PathVariable Long id) {
    return service.searchStudentById(id);
  }

  //新規受講生の登録処理
  @PostMapping("/registerStudent")
  public ResponseEntity<StudentDetail> registerStudent(@RequestBody StudentDetail studentDetail) {
    StudentDetail responseStudentDetail = service.saveStudent(studentDetail);
    return ResponseEntity.ok(responseStudentDetail);
  }


  //受講生情報の更新登録処理
  @PostMapping("/updateStudent")
  public ResponseEntity<String> updateStudent(@RequestBody StudentDetail studentDetail) {
    service.updateStudent(studentDetail);
    return ResponseEntity.ok("更新処理がで成功しました。");
  }
}


