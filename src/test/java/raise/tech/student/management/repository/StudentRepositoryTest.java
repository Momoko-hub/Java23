package raise.tech.student.management.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.beans.factory.annotation.Autowired;
import raise.tech.student.management.data.ApplicationStatus;
import raise.tech.student.management.data.Student;
import raise.tech.student.management.data.StudentCourse;
import raise.tech.student.management.domain.Status;

@MybatisTest
class StudentRepositoryTest {

  @Autowired
  private StudentRepository sut;

  @Test
  void 受講生の全件検索が行われること＿論理削除含む() {
    List<Student> actual = sut.searchAllStudents();
    assertThat(actual.size()).isEqualTo(5);
  }

  @Test
  void 登録された受講生の全件検索が行われること＿論理削除除く() {
    List<Student> actual = sut.searchStudents();
    assertThat(actual.size()).isEqualTo(4);
  }

  @Test
  void 受講生のID検索が行えること() {
    Integer id = 197555;
    Student student = sut.searchStudentById(id);
    assertThat(student.getId()).isEqualTo(id);
    assertThat(student.getFullName()).isEqualTo("森田恵麻");
  }

  @Test
  void 複数の受講生のID検索が行えること() {
    Integer id = 197555;
    List<Student> student = sut.searchStudentsById(id);
    assertThat(student.get(0).getId()).isEqualTo(id);
    assertThat(student.get(0).getFullName()).isEqualTo("森田恵麻");
  }

  @Test
  void 受講生コースの全件検索が行えること() {
    List<StudentCourse> actual = sut.searchStudentCourseList();
    assertThat(actual.size()).isEqualTo(8);
  }

  @Test
  void 受講生IDに紐づく受講生コース情報の検索が行えること() {
    Integer studentId = 197555;
    List<StudentCourse> actual = sut.searchStudentCourseByStudentId(studentId);

    StudentCourse testCourse = actual.get(0);
    assertThat(actual.size()).isEqualTo(2);

    StudentCourse firstCourse = actual.get(0);
    assertThat(firstCourse.getId()).isEqualTo(996284);
    assertThat(firstCourse.getCourseName()).isEqualTo("Webデザイン");

    StudentCourse secondCourse = actual.get(1);
    assertThat(secondCourse.getId()).isEqualTo(996285);
    assertThat(secondCourse.getCourseName()).isEqualTo("AWS");
  }

  @Test
  void 受講生コース情報のID検索が行えること() {
    Integer id = 996284;
    List<StudentCourse> studentCourses = sut.searchStudentCourseById(id);
    assertThat(studentCourses.get(0).getId()).isEqualTo(id);
    assertThat(studentCourses.get(0).getCourseName()).isEqualTo("デザイン");
  }

  @Test
  void 申込状況のID検索が行えること() {
    Integer id = 1;
    List<ApplicationStatus> applicationStatuses = sut.searchApplicationStatusById(id);
    assertThat(applicationStatuses.get(0).getId()).isEqualTo(id);
    assertThat(applicationStatuses.get(0).getStatus()).isEqualTo(Status.仮申込);
  }

  @Test
  void 申込状況のコースID検索が行えること() {
    Integer courseId = 996284;
    List<ApplicationStatus> applicationStatuses = sut.searchApplicationStatusByCourseId(courseId);
    assertThat(applicationStatuses.get(0).getCourseId()).isEqualTo(courseId);
    assertThat(applicationStatuses.get(0).getStatus()).isEqualTo(Status.仮申込);
  }

  @Test
  void 受講生の名前検索が行えること() {
    String fullName = "森田恵麻";
    List<Student> student = sut.searchStudentByName(fullName);
    assertThat(student.get(0).getFullName()).isEqualTo(fullName);
  }

  @Test
  void 受講生のフリガナ検索が行えること() {
    String furigana = "モリタエマ";
    List<Student> student = sut.searchStudentByFurigana(furigana);
    assertThat(student.get(0).getFurigana()).isEqualTo(furigana);
  }

  @Test
  void 受講生の居住地域検索が行えること() {
    String address = "北海道札幌市";
    List<Student> student = sut.searchStudentByAddress(address);
    assertThat(student.get(0).getAddress()).isEqualTo(address);
  }

  @Test
  void 受講生の年齢検索が行えること() {
    Integer age = 30;
    List<Student> student = sut.searchStudentByAge(age);
    assertThat(student.get(0).getAge()).isEqualTo(age);

  }

  @Test
  void 受講生の性別検索が行えること() {
    String sex = "男";
    List<Student> student = sut.searchStudentBySex(sex);
    assertThat(student.get(0).getSex()).isEqualTo(sex);

  }

  @Test
  void 受講コースの名前検索が行えること() {
    String courseName = "Java";
    List<StudentCourse> studentCourses = sut.searchStudentCourseByCourseName(courseName);
    assertThat(studentCourses.size()).isEqualTo(3);
  }

  @Test
  void 申込状況検索が行えること() {
    Status status = Status.仮申込;
    List<ApplicationStatus> statuses = sut.searchApplicationStatusByStatus(status);
    assertThat(statuses.size()).isEqualTo(8);
  }


  @Test
  void 受講生の登録が行えること() {
    Student student = new Student();

    student.setFullName("大田　桃子");
    student.setFurigana("オオタ　モモコ");
    student.setNickname("もも");
    student.setEmailAddress("momo@email.com");
    student.setAddress("東京都");
    student.setAge(29);
    student.setSex("女性");

    sut.insertStudent(student);

    List<Student> actual = sut.searchAllStudents();

    assertThat(actual.size()).isEqualTo(6);

  }

  @Test
  void 受講生コース情報の登録が行えること() {

    StudentCourse studentCourse = new StudentCourse();
    studentCourse.setCourseName("Java");

    sut.insertStudentsCourse(studentCourse);

    List<StudentCourse> actual = sut.searchStudentCourseList();
    assertThat(actual.size()).isEqualTo(9);

  }

  @Test
  void 申込状況の登録が行えること() {

    ApplicationStatus applicationStatus = new ApplicationStatus();
    applicationStatus.setStatus(Status.仮申込);

    sut.insertApplicationStatus(applicationStatus);

    List<ApplicationStatus> actual = sut.searchApplicationStatuses();
    assertThat(actual.size()).isEqualTo(9);

  }

  @Test
  void 受講生の更新が行えること() {
    Integer id = 197555;

    Student student = sut.searchStudentById(id);
    sut.updateStudent(student);

    student.setFullName("盛田　恵麻");

    sut.updateStudent(student);
    Student updatedStudent = sut.searchStudentById(id);

    assertThat(updatedStudent.getFullName()).isEqualTo("盛田　恵麻");

  }

  @Test
  void 受講生コース情報の更新が行われていること() {
    Integer studentId = 197555;
    List<StudentCourse> studentCourses = sut.searchStudentCourseByStudentId(studentId);

    studentCourses.forEach(course -> {
      if (course.getCourseName().equals("AWS")) {
        course.setCourseName("Java");
      }
    });

    List<StudentCourse> updatedStudentCourse = sut.searchStudentCourseByStudentId(studentId);

    assertThat(updatedStudentCourse).anyMatch(course -> course.getCourseName().equals("Java"));
  }

  @Test
  void 申込状況の更新が行われていること() {

    Integer courseId = 996284;

    ApplicationStatus applicationStatus = new ApplicationStatus();
    applicationStatus.setCourseId(996384);
    applicationStatus.setStatus(Status.仮申込);

    List<ApplicationStatus> applicationStatuses = new ArrayList<>();
    applicationStatuses.add(applicationStatus);

    List<ApplicationStatus> foundStatus = sut.searchApplicationStatusByCourseId(courseId);
    assertThat(foundStatus.get(0).getStatus()).isEqualTo(Status.仮申込);

    foundStatus.get(0).setStatus(Status.本申込);
    sut.updateStatus(foundStatus.get(0));

    List<ApplicationStatus> updateStatus = sut.searchApplicationStatusByCourseId(courseId);
    assertThat(foundStatus.get(0).getStatus()).isEqualTo(Status.本申込);

  }
}

