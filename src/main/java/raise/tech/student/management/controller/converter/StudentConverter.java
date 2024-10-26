package raise.tech.student.management.controller.converter;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;
import raise.tech.student.management.data.ApplicationStatus;
import raise.tech.student.management.data.Student;
import raise.tech.student.management.data.StudentCourse;
import raise.tech.student.management.domain.StudentDetail;

/**
 * 受講生詳細を受講生や受講生コース情報、もしくはその逆の変換を行うコンバーターです。
 */
@Component
public class StudentConverter {

  /**
   * 学生リストを受け取り、それぞれの学生についてStudentDetailオブジェクトを生成し、そのリストを返します。
   *
   * @param studentList           　受講生一覧
   * @param studentCourseList     　受講生コース情報のリスト
   * @param applicationStatusList 申込状況リスト
   * @return 受講生詳細情報のリスト
   */
  public List<StudentDetail> convertStudentDetails(List<Student> studentList,
      List<StudentCourse> studentCourseList, List<ApplicationStatus> applicationStatusList) {

    return studentList.stream()
        .map(student -> createStudentDetail(student, studentCourseList, applicationStatusList))
        .collect(Collectors.toList());

  }

  /**
   * 特定の学生に対する詳細情報を生成し、StudentDetailオブジェクトを作成します。
   *
   * @param student               　受講生
   * @param studentCourseList     　受講生コース情報のリスト
   * @param applicationStatusList 申込状況リスト
   * @return 受講生詳細情報
   */
  public StudentDetail createStudentDetail(Student student, List<StudentCourse> studentCourseList,
      List<ApplicationStatus> applicationStatusList) {

    StudentDetail studentDetail = new StudentDetail();
    studentDetail.setStudent(student);

    List<StudentCourse> courseForStudent = filterStudentCourses(student, studentCourseList);
    setCourseStatus(courseForStudent, applicationStatusList);

    studentDetail.setStudentCourseList(courseForStudent);

    studentDetail.setApplicationStatus(
        getApplicationStatuss(courseForStudent, applicationStatusList));

    return studentDetail;
  }

  /**
   * 指定された学生に関するコースをフィルタリングして返します。
   *
   * @param student           受講生
   * @param studentCourseList 　受講生コース情報のリスト
   * @return 受講生コースリスト
   */
  public List<StudentCourse> filterStudentCourses(Student student,
      List<StudentCourse> studentCourseList) {

    return studentCourseList.stream()
        .filter(course -> course.getStudentsId().equals(student.getId()))
        .collect(Collectors.toList());
  }

  /**
   * 学生に関するコースのステータスを設定します。
   *
   * @param courseForStudent      　受講生コース情報のリスト
   * @param applicationStatusList 申込状況リスト
   */
  public void setCourseStatus(List<StudentCourse> courseForStudent,
      List<ApplicationStatus> applicationStatusList) {
    courseForStudent.forEach(course ->
        applicationStatusList.stream()
            .filter(status -> status.getCourseId().equals(course.getId()))
            .findFirst()
            .ifPresent(status -> course.setStatus(status.getStatus())));
  }

  /**
   * 学生に関連するコースに基づいて、申込状況のリストを取得します。
   *
   * @param courseForStudent      　受講生コース情報のリスト
   * @param applicationStatusList 申込状況リスト
   * @return 条件一致した申込状況のリスト
   */
  public List<ApplicationStatus> getApplicationStatuss(List<StudentCourse> courseForStudent,
      List<ApplicationStatus> applicationStatusList) {

    return applicationStatusList.stream()
        .filter(status -> courseForStudent.stream()
            .anyMatch(course -> course.getId().equals(status.getCourseId())))
        .collect(Collectors.toList());

  }

}