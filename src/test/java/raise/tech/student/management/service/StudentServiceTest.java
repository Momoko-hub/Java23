package raise.tech.student.management.service;

import static org.assertj.core.api.Assertions.assertThat;
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
import raise.tech.student.management.exception.TestException;
import raise.tech.student.management.repository.StudentRepository;

@ExtendWith(MockitoExtension.class)
class StudentServiceTest {

  @Mock
  private StudentRepository repository;

  @Mock
  private StudentConverter converter;

  private StudentService sut;
  private Student student;
  private List<Student> studentList;
  private List<StudentCourse> studentCourseList;
  private List<ApplicationStatus> applicationStatus;

  @BeforeEach
  void before() {
    sut = new StudentService(repository, converter);

    student = new Student();
    student.setId(12345);
    student.setFullName("山田　太郎");
    student.setFurigana("ヤマダ　タロウ");
    student.setAddress("東京都新宿区");
    student.setAge(30);
    student.setSex("男性");

    StudentCourse course1 = new StudentCourse();
    course1.setId(111);
    course1.setStudentsId(12345);
    course1.setCourseName("Java");

    StudentCourse course2 = new StudentCourse();
    course2.setId(222);
    course2.setStudentsId(12345);
    course2.setCourseName("AWS");

    studentCourseList = Arrays.asList(course1, course2);

    ApplicationStatus
        status1 = new ApplicationStatus();
    status1.setCourseId(111);
    status1.setStatus(Status.仮申込);

    ApplicationStatus status2 = new ApplicationStatus();
    status2.setCourseId(222);
    status2.setStatus(Status.仮申込);

    applicationStatus = Arrays.asList(status1, status2);

  }

  @Test
  void 受講生詳細の一覧検索_リポジトリとコンバーターの処理が適切に呼び出せていること_論理削除含む() {
    List<Student> studentList = Arrays.asList(student);
    List<ApplicationStatus> applicationStatusList1 = Arrays.asList(applicationStatus.get(0));
    List<ApplicationStatus> applicationStatusList2 = Arrays.asList(applicationStatus.get(1));

    when(repository.searchAllStudents()).thenReturn(studentList);
    when(repository.searchStudentCourseByStudentId(12345)).thenReturn(studentCourseList);
    when(repository.searchApplicationStatusByCourseId(111)).thenReturn(applicationStatusList1);
    when(repository.searchApplicationStatusByCourseId(222)).thenReturn(applicationStatusList2);

    List<StudentDetail> expectedStudentDetail = new ArrayList<>();
    when(converter.convertStudentDetails(studentList, studentCourseList,
        applicationStatus))
        .thenReturn(expectedStudentDetail);

    List<StudentDetail> actualStudentDetail = sut.searchAllStudentList();

    verify(repository, times(1)).searchAllStudents();
    verify(repository, times(1)).searchStudentCourseByStudentId(12345);
    verify(repository, times(1)).searchApplicationStatusByCourseId(111);
    verify(repository, times(1)).searchApplicationStatusByCourseId(222);
    verify(converter, times(1)).convertStudentDetails(studentList, studentCourseList,
        applicationStatus);

    assertEquals(expectedStudentDetail, actualStudentDetail);
  }

  @Test
  void 受講生詳細の一覧検索_リポジトリとコンバーターの処理が適切に呼び出せていること＿論理削除除く() {
    List<Student> studentList = Arrays.asList(student);
    List<StudentCourse> studentCourses = studentCourseList;
    List<ApplicationStatus> applicationStatusList1 = Arrays.asList(applicationStatus.get(0));
    List<ApplicationStatus> applicationStatusList2 = Arrays.asList(applicationStatus.get(1));

    when(repository.searchStudents()).thenReturn(studentList);
    when(repository.searchStudentCourseByStudentId(12345)).thenReturn(studentCourseList);
    when(repository.searchApplicationStatusByCourseId(111)).thenReturn(applicationStatusList1);
    when(repository.searchApplicationStatusByCourseId(222)).thenReturn(applicationStatusList2);

    List<StudentDetail> expectedStudentDetail = new ArrayList<>();
    when(converter.convertStudentDetails(studentList, studentCourseList,
        applicationStatus))
        .thenReturn(expectedStudentDetail);

    List<StudentDetail> actualStudentDetail = sut.searchStudentList();

    verify(repository, times(1)).searchStudents();
    verify(repository, times(1)).searchStudentCourseByStudentId(12345);
    verify(repository, times(1)).searchApplicationStatusByCourseId(111);
    verify(repository, times(1)).searchApplicationStatusByCourseId(222);
    verify(converter, times(1)).convertStudentDetails(studentList, studentCourseList,
        applicationStatus);

    assertEquals(expectedStudentDetail, actualStudentDetail);
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
    verify(repository, times(2)).searchApplicationStatusByCourseId(courseId);

    assertEquals(expected.getStudent().getId(),
        result.getStudent().getId());
    assertEquals(expected.getStudentCourseList().size(),
        result.getStudentCourseList().size());
    assertEquals(expected.getApplicationStatus().size(),
        result.getApplicationStatus().size());
  }

  @Test
  void 受講生詳細の名前検索_検索した名前と一致する受講生とコース情報と申込状況が呼び出せていること()
      throws TestException {

    when(
        repository.findStudentsByConditions("山田　太郎", null, null, null, null))
        .thenReturn(studentList);
    when(repository.findCoursesByConditions(null))
        .thenReturn(studentCourseList);
    when(repository.findApplicationStatusByConditions(null))
        .thenReturn(applicationStatus);

    List<StudentDetail> expectedStudentDetails = new ArrayList<>();
    StudentDetail studentDetail = new StudentDetail();
    studentDetail.setStudent(student);
    studentDetail.setStudentCourseList(studentCourseList);
    studentDetail.setApplicationStatus(applicationStatus);
    expectedStudentDetails.add(studentDetail);

    when(converter.convertStudentDetails(studentList, studentCourseList, applicationStatus))
        .thenReturn(expectedStudentDetails);

    List<StudentDetail> resultStudentDetail = sut.searchStudentsByConditions("山田　太郎", null,
        null, null, null, null, null);

    assertThat(resultStudentDetail).isNotEmpty();
    assertThat(resultStudentDetail.get(0).getStudent().getFullName()).isEqualTo("山田　太郎");
    assertThat(resultStudentDetail.get(0).getStudentCourseList().size()).isEqualTo(2);
    assertThat(resultStudentDetail.get(0).getStudentCourseList().get(0).getCourseName()).isEqualTo(
        "Java");
    assertThat(resultStudentDetail.get(0).getApplicationStatus().get(0).getStatus()).isEqualTo(
        Status.仮申込);
    assertThat(resultStudentDetail.get(0).getStudentCourseList().get(1).getCourseName()).isEqualTo(
        "AWS");
    assertThat(resultStudentDetail.get(0).getApplicationStatus().get(1).getStatus()).isEqualTo(
        Status.仮申込);
  }

  @Test
  void 受講生詳細の部分一致検索_検索した名前と一致する受講生とコース情報と申込状況が呼び出せていること()
      throws TestException {
    String partialFullName = "山";

    when(
        repository.findStudentsByConditions(partialFullName, null, null, null, null))
        .thenReturn(studentList);
    when(repository.findCoursesByConditions(null))
        .thenReturn(studentCourseList);
    when(repository.findApplicationStatusByConditions(null))
        .thenReturn(applicationStatus);

    List<StudentDetail> expectedStudentDetails = new ArrayList<>();
    StudentDetail studentDetail = new StudentDetail();
    studentDetail.setStudent(student);
    studentDetail.setStudentCourseList(studentCourseList);
    studentDetail.setApplicationStatus(applicationStatus);
    expectedStudentDetails.add(studentDetail);

    when(converter.convertStudentDetails(studentList, studentCourseList, applicationStatus))
        .thenReturn(expectedStudentDetails);

    List<StudentDetail> resultStudentDetail = sut.searchStudentsByConditions(
        partialFullName, null, null, null, null, null, null);

    assertThat(resultStudentDetail).isNotEmpty();
    assertThat(resultStudentDetail.get(0).getStudent().getFullName()).contains(partialFullName);
  }

  @Test
  void 受講生詳細のコース検索_検索したコースと一致する受講生とコース情報と申込状況が呼び出せていること()
      throws TestException {

    when(
        repository.findStudentsByConditions(null, null, null, null, null))
        .thenReturn(studentList);
    when(repository.findCoursesByConditions("Java"))
        .thenReturn(studentCourseList);
    when(repository.findApplicationStatusByConditions(null))
        .thenReturn(applicationStatus);

    List<StudentDetail> expectedStudentDetails = new ArrayList<>();
    StudentDetail studentDetail = new StudentDetail();
    studentDetail.setStudent(student);
    studentDetail.setStudentCourseList(studentCourseList);
    studentDetail.setApplicationStatus(applicationStatus);
    expectedStudentDetails.add(studentDetail);

    when(converter.convertStudentDetails(studentList, studentCourseList, applicationStatus))
        .thenReturn(expectedStudentDetails);

    List<StudentDetail> resultStudentDetail = sut.searchStudentsByConditions(null, null,
        null, null, null, "Java", null);

    assertThat(resultStudentDetail).isNotEmpty();
    assertThat(resultStudentDetail.get(0).getStudent().getFullName()).isEqualTo("山田　太郎");
    assertThat(resultStudentDetail.get(0).getStudentCourseList().size()).isEqualTo(2);
    assertThat(resultStudentDetail.get(0).getStudentCourseList().get(0).getCourseName()).isEqualTo(
        "Java");
    assertThat(resultStudentDetail.get(0).getApplicationStatus().get(0).getStatus()).isEqualTo(
        Status.仮申込);
    assertThat(resultStudentDetail.get(0).getStudentCourseList().get(1).getCourseName()).isEqualTo(
        "AWS");
    assertThat(resultStudentDetail.get(0).getApplicationStatus().get(1).getStatus()).isEqualTo(
        Status.仮申込);
  }


  @Test
  void 受講生詳細の申込状況検索_検索した申込状況と一致する受講生とコース情報と申込状況が呼び出せていること()
      throws TestException {

    when(
        repository.findStudentsByConditions(null, null, null, null, null))
        .thenReturn(studentList);
    when(repository.findCoursesByConditions(null))
        .thenReturn(studentCourseList);
    when(repository.findApplicationStatusByConditions(Status.仮申込))
        .thenReturn(applicationStatus);

    List<StudentDetail> expectedStudentDetails = new ArrayList<>();
    StudentDetail studentDetail = new StudentDetail();
    studentDetail.setStudent(student);
    studentDetail.setStudentCourseList(studentCourseList);
    studentDetail.setApplicationStatus(applicationStatus);
    expectedStudentDetails.add(studentDetail);

    when(converter.convertStudentDetails(studentList, studentCourseList, applicationStatus))
        .thenReturn(expectedStudentDetails);

    List<StudentDetail> resultStudentDetail = sut.searchStudentsByConditions(null, null,
        null, null, null, null, Status.仮申込);

    assertThat(resultStudentDetail).isNotEmpty();
    assertThat(resultStudentDetail.get(0).getStudent().getFullName()).isEqualTo("山田　太郎");
    assertThat(resultStudentDetail.get(0).getStudentCourseList().size()).isEqualTo(2);
    assertThat(resultStudentDetail.get(0).getStudentCourseList().get(0).getCourseName()).isEqualTo(
        "Java");
    assertThat(resultStudentDetail.get(0).getApplicationStatus().get(0).getStatus()).isEqualTo(
        Status.仮申込);
    assertThat(resultStudentDetail.get(0).getStudentCourseList().get(1).getCourseName()).isEqualTo(
        "AWS");
    assertThat(resultStudentDetail.get(0).getApplicationStatus().get(1).getStatus()).isEqualTo(
        Status.仮申込);
  }

  @Test
  @Transactional
  void 受講生詳細の登録と初期設定が適切に行われているか() {
    StudentDetail studentDetail = new StudentDetail(student, studentCourseList, applicationStatus);

    doNothing().when(repository).insertStudent(student);
    doNothing().when(repository).insertStudentsCourse(any(StudentCourse.class));
    doNothing().when(repository).insertApplicationStatus(any(ApplicationStatus.class));

    StudentDetail result = sut.registerStudent(studentDetail);

    assertNotNull(result);
    assertEquals(student.getId(), result.getStudent().getId());
    assertEquals(2, result.getStudentCourseList().size());

    result.getStudentCourseList().forEach(course -> {
      assertEquals(student.getId(), course.getStudentsId());
      assertNotNull(course.getStartDate());
      assertNotNull(course.getEndDate());
    });

    List<ApplicationStatus> resultStatus = result.getApplicationStatus();
    assertNotNull(resultStatus);
    assertEquals(2, resultStatus.size());

    for (ApplicationStatus status : resultStatus) {
      assertEquals(Status.仮申込, status.getStatus());

      boolean validCourseId = studentCourseList.stream()
          .anyMatch(studentCourse -> studentCourse.getId() == status.getCourseId());
    }

    verify(repository, times(1)).insertStudent(student);
    verify(repository, times(2)).insertStudentsCourse(any(StudentCourse.class));
    verify(repository, times(2)).insertApplicationStatus(any(ApplicationStatus.class));
  }


  @Test
  void 受講生詳細の更新_受講生とコース情報がそれぞれ適切に更新されているか() {
    StudentDetail studentDetail = new StudentDetail();
    studentDetail.setStudent(student);
    studentDetail.setStudentCourseList(studentCourseList);

    sut.updateStudent(studentDetail);

    verify(repository, times(1)).updateStudent(studentDetail.getStudent());

    studentCourseList.forEach(course ->
        verify(repository, times(1)).updateStudentCourse(course));
  }

  @Test
  void 申込状況が更新できているか() {
    applicationStatus.get(0).setStatus(Status.仮申込);
    sut.updateApplicationStatus(applicationStatus, Status.本申込);
    verify(repository, times(1)).updateStatus(applicationStatus.get(0));

    assertEquals(Status.本申込, applicationStatus.get(0).getStatus());

    assertNotNull(applicationStatus.get(0).getUpdatedAt());
  }

  @Test
  void 受講生詳細の申込状況検索_検索した申込状況と一致する受講生詳細が取得できるか() {

    Status status = Status.仮申込;

    StudentCourse course1 = new StudentCourse();
    course1.setId(111);
    course1.setStudentsId(12345);
    course1.setCourseName("Java");

    StudentCourse course2 = new StudentCourse();
    course2.setId(222);
    course2.setStudentsId(12345);
    course2.setCourseName("AWS");

    when(repository.findApplicationStatusByConditions(status))
        .thenReturn(applicationStatus);
    when(repository.searchStudentCourseById(111)).thenReturn(Arrays.asList(course1));
    when(repository.searchStudentCourseById(222)).thenReturn(Arrays.asList(course2));
    when(repository.searchStudentById(12345)).thenReturn(student);

    List<StudentDetail> resultStudentDetail = sut.searchStudentByStatus(status);

    assertThat(resultStudentDetail).hasSize(2);

    StudentDetail detail1 = resultStudentDetail.get(0);
    assertThat(detail1.getStudent().getId()).isEqualTo(12345);
    assertThat(detail1.getStudent().getFullName()).isEqualTo("山田　太郎");
    assertThat(detail1.getStudentCourseList().get(0).getCourseName()).isEqualTo("Java");
    assertThat(detail1.getApplicationStatus().get(0).getStatus()).isEqualTo(Status.仮申込);

    StudentDetail detail2 = resultStudentDetail.get(1);
    assertThat(detail2.getStudent().getId()).isEqualTo(12345);
    assertThat(detail2.getStudent().getFullName()).isEqualTo("山田　太郎");
    assertThat(detail2.getStudentCourseList().get(0).getCourseName()).isEqualTo("AWS");
    assertThat(detail2.getApplicationStatus().get(0).getStatus()).isEqualTo(Status.仮申込);

  }
}
