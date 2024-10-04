package raise.tech.student.management.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.transaction.annotation.Transactional;
import raise.tech.student.management.controller.converter.StudentConverter;
import raise.tech.student.management.data.ApplicationStatus;
import raise.tech.student.management.data.Student;
import raise.tech.student.management.data.StudentCourse;
import raise.tech.student.management.domain.Status;
import raise.tech.student.management.domain.StudentDetail;
import raise.tech.student.management.repository.StudentRepository;

@ExtendWith(MockitoExtension.class)
class StudentServiceTest {

  @Mock
  private StudentRepository repository;

  @Mock
  private StudentConverter converter;


  private StudentService sut;
  private Student student;
  private List<StudentCourse> studentCourseList;
  private List<ApplicationStatus> applicationStatus;

  @BeforeEach
  void before() {
    sut = new StudentService(repository, converter);

    student = new Student();
    student.setId(12345);
    student.setFullName("山田　太郎");

    StudentCourse course1 = new StudentCourse();
    course1.setId(111);
    course1.setCourseName("Java");
    StudentCourse course2 = new StudentCourse();
    course2.setId(222);
    course2.setCourseName("AWS");
    studentCourseList = Arrays.asList(course1, course2);

    ApplicationStatus status1 = new ApplicationStatus();
    status1.setCourseId(111);
    status1.setStatus(Status.仮申込);
    ApplicationStatus status2 = new ApplicationStatus();
    status2.setCourseId(222);
    status2.setStatus(Status.仮申込);
    applicationStatus = Arrays.asList(status1, status2);

  }

  @Test
  void 受講生詳細の一覧検索_リポジトリとコンバーターの処理が適切に呼び出せていること() {
    List<Student> studentList = new ArrayList<>();
    studentCourseList = new ArrayList<>();
    when(repository.searchAllStudents()).thenReturn(studentList);
    when(repository.searchStudentCourseList()).thenReturn(studentCourseList);

    sut.searchAllStudentList();

    verify(repository, times(1)).searchAllStudents();
    verify(repository, times(1)).searchStudentCourseList();
    verify(converter, times(1)).convertStudentDetails(studentList, studentCourseList,
        applicationStatus);
  }

  @Test
  void 受講生詳細の検索_IDに紐つく受講生とその受講生のコース情報と申込状況を適切に呼び出せていること() {

    when(repository.searchStudentById(12345)).thenReturn(student);
    when(repository.searchStudentCourseByStudentId(12345)).thenReturn(studentCourseList);

    Integer courseId = 111;
    when(repository.searchApplicationStatusByCourseId(courseId)).thenReturn(applicationStatus);

    StudentDetail expected = new StudentDetail(student, studentCourseList, applicationStatus);
    StudentDetail result = sut.searchStudent(12345);

    verify(repository, times(1)).searchStudentById(12345);
    verify(repository, times(1)).searchStudentCourseByStudentId(12345);
    verify(repository, times(1)).searchApplicationStatusByCourseId(courseId);

    Assertions.assertEquals(expected.getStudent().getId(),
        result.getStudent().getId());
    Assertions.assertEquals(expected.getStudentCourseList().size(),
        result.getStudentCourseList().size());
    Assertions.assertEquals(expected.getApplicationStatus().size(),
        result.getApplicationStatus().size());


  }

  @Test
  @Transactional
  void 受講生詳細の登録と初期設定が適切に行われているか() {
    StudentDetail studentDetail = new StudentDetail(student, studentCourseList, applicationStatus);

    doNothing().when(repository).insertStudent(student);
    doNothing().when(repository).insertStudentsCourse(any(StudentCourse.class));

    StudentDetail result = sut.registerStudent(studentDetail);

    assertNotNull(result);
    assertEquals(student.getId(), result.getStudent().getId());
    assertEquals(2, result.getStudentCourseList().size());

    result.getStudentCourseList().forEach(course -> {
      assertEquals(student.getId(), course.getStudentsId());
      assertNotNull(course.getStartDate());
      assertNotNull(course.getEndDate());
    });

    verify(repository, times(1)).insertStudent(student);
    verify(repository, times(2)).insertStudentsCourse(any(StudentCourse.class));
  }


  @Test
  void 受講生詳細の更新_受講生とコース情報がそれぞれ適切に更新されているか() {
    StudentDetail studentDetail = new StudentDetail();

    sut.updateStudent(studentDetail);

    verify(repository, times(1)).updateStudent(studentDetail.getStudent());

    studentCourseList.forEach(course ->
        verify(repository, times(1)).updateStudentCourse(course));

  }

}