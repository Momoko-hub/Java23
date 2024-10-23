package raise.tech.student.management.controller.converter;

import java.util.ArrayList;
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

    List<StudentDetail> studentDetails = new ArrayList<>();

    studentList.forEach(student -> {
      StudentDetail studentDetail = new StudentDetail();
      studentDetail.setStudent(student);

      List<StudentCourse> convertStudentCourseList = studentCourseList.stream()
          .filter(studentCourse -> student.getId().equals(studentCourse.getStudentsId()))
          .collect(Collectors.toList());

      convertStudentCourseList.forEach(studentCourse -> {
        applicationStatusList.stream()
            .filter(status ->
                status.getCourseId().equals(studentCourse.getId()))
            .findFirst()
            .ifPresent(status -> studentCourse.setStatus(status.getStatus()));
      });

      studentDetail.setStudentCourseList(convertStudentCourseList);

      studentDetail.setApplicationStatus(applicationStatusList.stream()
          .filter(status ->
              convertStudentCourseList.stream()
                  .anyMatch(studentCourse ->
                      status.getCourseId().equals(studentCourse.getId())))
          .collect(Collectors.toList()));

      studentDetails.add(studentDetail);
    });

    return studentDetails;
  }

}
