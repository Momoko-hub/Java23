package raise.tech.student.management.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.beans.factory.annotation.Autowired;
import raise.tech.student.management.data.Student;
import raise.tech.student.management.data.StudentCourse;

@MybatisTest
class StudentRepositoryTest {

  @Autowired
  private StudentRepository sut;

  @Test
  void 受講生の全件検索が行われること() {
    List<Student> actual = sut.searchStudents();
    assertThat(actual.size()).isEqualTo(5);
  }

  @Test
  void 受講生のID検索が行えること() {
    Integer id = 197555;
    Student student = sut.searchStudent(id);
    assertThat(student.getId()).isEqualTo(id);
    assertThat(student.getFullName()).isEqualTo("森田恵麻");
  }

  @Test
  void 受講生IDに紐づく受講生コース情報の検索が行えること() {
    Integer studentId = 197555;
    List<StudentCourse> actual = sut.searchStudentCourse(studentId);

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

    List<Student> actual = sut.searchStudents();

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
  void 受講生の更新が行えること() {
    Integer id = 197555;

    Student student = sut.searchStudent(id);
    sut.updateStudent(student);

    student.setFullName("盛田　恵麻");

    sut.updateStudent(student);
    Student updatedStudent = sut.searchStudent(id);

    assertThat(updatedStudent.getFullName()).isEqualTo("盛田　恵麻");

  }

  @Test
  void 受講生コース情報の更新が行われていること() {
    Integer studentId = 197555;
    List<StudentCourse> studentCourses = sut.searchStudentCourse(studentId);

    studentCourses.forEach(course -> {
      if (course.getCourseName().equals("AWS")) {
        course.setCourseName("Java");
      }
    });

    List<StudentCourse> updatedStudentCourse = sut.searchStudentCourse(studentId);

    assertThat(updatedStudentCourse).anyMatch(course -> course.getCourseName().equals("Java"));

  }
}

