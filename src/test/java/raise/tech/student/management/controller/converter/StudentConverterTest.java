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

  @BeforeEach
  void before() {
    sut = new StudentConverter();
  }

  @Test
  void 受講生に紐づく受講生コース情報が取得できているか() {
    Student student1 = new Student();
    student1.setId(123);
    student1.setFullName("テスト太郎");

    Student student2 = new Student();
    student2.setId(456);
    student2.setFullName("テスト花子");

    List<Student> studentList = Arrays.asList(student1, student2);

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

    List<StudentCourse> studentCourseList = Arrays.asList(studentCourse1, studentCourse2,
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

    List<ApplicationStatus> applicationStatusList = Arrays.asList(status1, status2, status3);

    List<StudentDetail> result = sut.convertStudentDetails(studentList, studentCourseList,
        applicationStatusList);

    assertEquals(2, result.size());

    StudentDetail studentDetail1 = result.getFirst();
    assertEquals(123, studentDetail1.getStudent().getId());
    assertEquals(2, studentDetail1.getStudentCourseList().size());
    assertEquals("Java", studentDetail1.getStudentCourseList().get(0).getCourseName());
    assertEquals("AWS", studentDetail1.getStudentCourseList().get(1).getCourseName());
    assertEquals(2, studentDetail1.getApplicationStatus().size());
    assertEquals(Status.仮申込, studentDetail1.getApplicationStatus().get(0).getStatus());
    assertEquals(Status.仮申込, studentDetail1.getApplicationStatus().get(1).getStatus());

    StudentDetail studentDetail2 = result.get(1);
    assertEquals(456, studentDetail2.getStudent().getId());
    assertEquals(1, studentDetail2.getStudentCourseList().size());
    assertEquals("Webデザイン", studentDetail2.getStudentCourseList().getFirst().getCourseName());
    assertEquals(1, studentDetail2.getApplicationStatus().size());
    assertEquals(Status.仮申込, studentDetail2.getApplicationStatus().get(0).getStatus());


  }
}