package raise.tech.student.management.repository;

import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import raise.tech.student.management.data.Student;
import raise.tech.student.management.data.StudentCourse;

/**
 * 受講生テーブルと受講生コース情報テーブルとが紐づくRepositoryです。
 */
@Mapper
public interface StudentRepository {

  /**
   * 受講生の全件検索を行います。
   * <p>
   * * @return 受講生一覧（全件）
   */
  List<Student> searchStudents();

  /**
   * 受講生の検索を行います。
   *
   * @param id 学生ID
   * @return 学生情報
   */
  Student searchStudent(Integer id);

  /**
   * 受講生のコース情報の全件検索を行います。
   *
   * @return 受講生のコース情報（全件）
   */
  List<StudentCourse> searchStudentCourseList();

  /**
   * 受講生IDに紐づく受講生コース情報を検索します。
   *
   * @param studentId 　受講生ID
   * @return 受講生IDに紐づく受講生コース情報
   */
  List<StudentCourse> searchStudentCourse(Integer studentId);

  /**
   * 受講生を新規登録します。IDに関しては自動採番を行う。
   *
   * @param student 　受講生
   */
  void insertStudent(Student student);

  /**
   * 受講生コース情報を新規登録します。IDに関しては自動採番を行う。
   *
   * @param studentCourse 　受講生のコース情報
   */
  void insertStudentsCourse(StudentCourse studentCourse);

  /**
   * 受講生を更新します。
   *
   * @param student 受講生
   */
  void updateStudent(Student student);

  /**
   * 受講生コース情報のコース名を更新します。
   *
   * @param studentCourse 受講生コース情報
   */
  void updateStudentCourse(StudentCourse studentCourse);


}

