package raise.tech.student.management.service;

import java.time.LocalDateTime;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import raise.tech.student.management.data.Student;
import raise.tech.student.management.data.StudentsCourses;
import raise.tech.student.management.domain.StudentDetail;
import raise.tech.student.management.repository.StudentRepository;

/**
 * 受講生情報を取り扱うサービスです。
 * 受講生の検索や登録・更新処理を行います。
 */
@Service
public class StudentService {

  private final StudentRepository repository;

  @Autowired
  public StudentService(StudentRepository repository) {
    this.repository = repository;
  }

  /**
   * 受講生一覧検索です。
   * 全件検索を行うので、条件指定は行わないものになります。
   *
   * @return 受講生一覧（全件）
   */
  public List<Student> searchStudentList() {
    return repository.searchStudents();
  }

  /**
   * 受講生検索です。
   * IDに紐づく受講生情報を取得した後、その受講生に紐づく受講生コースを取得して設定します。
   * @param id　受講生ID
   * @return 受講生
   */
  public StudentDetail searchStudentList(Long id) {
    Student student = repository.searchStudent(id);
    List<StudentsCourses> studentsCourses = repository.searchStudentCourses(student.getId());
    StudentDetail studentDetail = new StudentDetail();
    studentDetail.setStudent(student);
    studentDetail.setStudentsCourses(studentsCourses);
    return studentDetail;
  }

  public List<StudentsCourses> searchCourseList() {
    return repository.searchStudentCoursesList();
  }

  @Transactional
  public StudentDetail saveStudent(StudentDetail studentDetail) {
    repository.insertStudent(studentDetail.getStudent());
    //コース情報の登録
    for (StudentsCourses studentsCourses : studentDetail.getStudentsCourses()) {
      studentsCourses.setStudentsId(studentDetail.getStudent().getId());
      studentsCourses.setStartDate(LocalDateTime.now());
      studentsCourses.setEndDate(LocalDateTime.now().plusYears(1));

      repository.insertStudentsCourses(studentsCourses);
    }
    return studentDetail;
  }

  @Transactional
  public void updateStudent(StudentDetail studentDetail) {
    repository.updateStudent(studentDetail.getStudent());
    //現在のコース情報を登録
    List<StudentsCourses> currentCourses = repository.searchStudentCourses(
        studentDetail.getStudent()
            .getId());
    //新しいコース情報と比較して、更新、追加、削除を行う
    for (StudentsCourses newCourse : studentDetail.getStudentsCourses()) {
      boolean exists = false;
      for (StudentsCourses currentCourse : currentCourses) {
        if (currentCourse.getId().equals(newCourse.getId())) {
          //既存のコースを更新
          repository.updateStudentsCourses(newCourse);
          exists = true;
          break;
        }
      }
      if (!exists) {
        //新しいコースの追加
        newCourse.setStudentsId(studentDetail.getStudent().getId());
        newCourse.setStartDate(LocalDateTime.now());
        newCourse.setEndDate(LocalDateTime.now().plusYears(1));
        repository.insertStudentsCourses(newCourse);

      }
    }
  }
    @Transactional
    public StudentDetail searchStudentById (Long id){
      Student student = repository.searchStudent(id);
      List<StudentsCourses> studentsCourses = repository.searchStudentCourses(student.getId());
      StudentDetail studentDetail = new StudentDetail();
      studentDetail.setStudent(student);
      studentDetail.setStudentsCourses(studentsCourses);
      return studentDetail;
    }

    @Transactional
  public void deleteStudent(Long id){
    repository.logicallyDeleteStudent(id);
    }
  }


