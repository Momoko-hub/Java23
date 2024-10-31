package raise.tech.student.management.controller.converter;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import raise.tech.student.management.data.ApplicationStatus;
import raise.tech.student.management.data.Student;
import raise.tech.student.management.data.StudentCourse;
import raise.tech.student.management.domain.Status;
import raise.tech.student.management.domain.StudentDetail;

class StudentConverterTest {

  StudentConverter sut;
  List<Student> studentList;
  List<StudentCourse> studentCourseList;
  List<ApplicationStatus> applicationStatusList;

  @BeforeEach
  void before() {
    sut = new StudentConverter();

    Student student1 = new Student();
    student1.setId(123);
    student1.setFullName("テスト太郎");

    Student student2 = new Student();
    student2.setId(456);
    student2.setFullName("テスト花子");

    studentList = Arrays.asList(student1, student2);

    StudentCourse studentCourse1 = new StudentCourse();
    studentCourse1.setId(111);
    studentCourse1.setStudentsId(123);
    studentCourse1.setCourseName("Java");

    StudentCourse studentCourse2 = new StudentCourse();
    studentCourse2.setId(222);
    studentCourse2.setStudentsId(123);
    studentCourse2.setCourseName("AWS");

    StudentCourse studentCourse3 = new StudentCourse();
    studentCourse3.setId(333);
    studentCourse3.setStudentsId(456);
    studentCourse3.setCourseName("Webデザイン");

    studentCourseList = Arrays.asList(studentCourse1, studentCourse2,
        studentCourse3);

    ApplicationStatus status1 = new ApplicationStatus();
    status1.setCourseId(111);
    status1.setStatus(Status.仮申込);

    ApplicationStatus status2 = new ApplicationStatus();
    status2.setCourseId(222);
    status2.setStatus(Status.仮申込);

    ApplicationStatus status3 = new ApplicationStatus();
    status3.setCourseId(333);
    status3.setStatus(Status.仮申込);

    applicationStatusList = Arrays.asList(status1, status2, status3);

  }

  @Test
  void converterStudentDetail_正しいStudentDetailリストが生成される() {
    List<StudentDetail> result = sut.convertStudentDetails(studentList, studentCourseList,
        applicationStatusList);

    assertEquals(2, result.size());
    assertEquals("テスト太郎", result.get(0).getStudent().getFullName());
    assertEquals("テスト花子", result.get(1).getStudent().getFullName());
  }

  @Test
  void createStudentDetail_正しいStudentDetailが生成される() {
    StudentDetail result = sut.createStudentDetail(studentList.get(0), studentCourseList,
        applicationStatusList);

    assertEquals("テスト太郎", result.getStudent().getFullName());
    assertEquals(2, result.getStudentCourseList().size());
    assertEquals(Status.仮申込, result.getApplicationStatus().get(0).getStatus());
  }

  @Test
  void filterStudentCourses_学生に関するコースのみ取得される() {
    List<StudentCourse> result = sut.filterStudentCourses(studentList.get(0), studentCourseList);

    assertEquals(2, result.size());
    assertEquals("Java", result.get(0).getCourseName());
    assertEquals("AWS", result.get(1).getCourseName());
  }

  @Test
  void setCourseStatus_正しいステータスが設定される() {
    List<StudentCourse> courseForStudent = sut.filterStudentCourses(studentList.get(0),
        studentCourseList);
    sut.setCourseStatus(courseForStudent, applicationStatusList);

    assertEquals(Status.仮申込, courseForStudent.get(0).getStatus());
    assertEquals(Status.仮申込, courseForStudent.get(1).getStatus());
  }

  @Test
  void getApplicationStatus_学生に関する申込状況が取得される() {
    List<StudentCourse> courseForStudent = sut.filterStudentCourses(studentList.get(0),
        studentCourseList);
    List<ApplicationStatus> result = sut.getApplicationStatuss(courseForStudent,
        applicationStatusList);

    assertEquals(2, result.size());
    assertEquals(Status.仮申込, result.get(0).getStatus());
    assertEquals(Status.仮申込, result.get(1).getStatus());
  }
}