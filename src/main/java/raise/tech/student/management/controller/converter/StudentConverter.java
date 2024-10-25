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
   * 受講生に紐づく受講生コース情報をマッピングする。 受講生コース情報は受講生に対して複数存在するのでループを回して受講生詳細情報を組み立てる。
   *
   * @param studentList       　受講生一覧
   * @param studentCourseList 　受講生コース情報のリスト
   * @return 受講生詳細情報のリスト
   */
  public List<StudentDetail> convertStudentDetails(List<Student> studentList,
      List<StudentCourse> studentCourseList, List<ApplicationStatus> applicationStatusList) {

    return studentList.stream()
        .map(student -> createStudentDetail(student, studentCourseList, applicationStatusList))
        .collect(Collectors.toList());

  }

  private StudentDetail createStudentDetail(Student student, List<StudentCourse> studentCourseList,
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

  private List<StudentCourse> filterStudentCourses(Student student,
      List<StudentCourse> studentCourseList) {

    return studentCourseList.stream()
        .filter(course -> course.getStudentsId().equals(student.getId()))
        .collect(Collectors.toList());
  }

  private void setCourseStatus(List<StudentCourse> courseForStudent,
      List<ApplicationStatus> applicationStatusList) {
    courseForStudent.forEach(course ->
        applicationStatusList.stream()
            .filter(status -> status.getCourseId().equals(course.getId()))
            .findFirst()
            .ifPresent(status -> course.setStatus(status.getStatus())));
  }

  private List<ApplicationStatus> getApplicationStatuss(List<StudentCourse> courseForStudent,
      List<ApplicationStatus> applicationStatusList) {

    return applicationStatusList.stream()
        .filter(status -> courseForStudent.stream()
            .anyMatch(course -> course.getId().equals(status.getCourseId())))
        .collect(Collectors.toList());

  }

}