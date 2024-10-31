package raise.tech.student.management.repository;

import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import raise.tech.student.management.data.ApplicationStatus;
import raise.tech.student.management.data.Student;
import raise.tech.student.management.data.StudentCourse;
import raise.tech.student.management.domain.Status;

/**
 * 受講生テーブルと受講生コース情報テーブルとが紐づくRepositoryです。
 */
@Mapper
public interface StudentRepository {

  /**
   * 受講生の全件検索を行います。（論理削除含む）
   *
   * @return 受講生一覧（全件）
   */
  List<Student> searchAllStudents();

  /**
   * 登録中の受講生の全件検索を行います。 論理削除された受講生は表示されません。
   *
   * @return 受講生一覧（全件）
   */
  List<Student> searchStudents();

  /**
   * 受講生の検索を行います。
   *
   * @param id 学生ID
   * @return 学生情報
   */
  Student searchStudentById(Integer id);

  /**
   * 複数の受講生の検索を行います。
   *
   * @param id 学生ID
   * @return 学生情報
   */
  List<Student> searchStudentsById(Integer id);


  /**
   * 受講生のコース情報の全件検索を行います。
   *
   * @return 受講生のコース情報（全件）
   */
  List<StudentCourse> searchStudentCourseList();

  /**
   * 申込状況の全件検索を行います。
   *
   * @return 受講生の申込状況（全件）
   */
  List<ApplicationStatus> searchApplicationStatuses();


  /**
   * 受講生IDに紐づく受講生コース情報を検索します。
   *
   * @param studentId 　受講生ID
   * @return 受講生IDに紐づく受講生コース情報
   */
  List<StudentCourse> searchStudentCourseByStudentId(Integer studentId);

  /**
   * 受講生コース情報のIDを検索します。
   *
   * @param id 　受講生コース情報のID
   */
  List<StudentCourse> searchStudentCourseById(Integer id);

  /**
   * 申込状況のIDで検索します。
   *
   * @param id 　申込状況のID
   */
  List<ApplicationStatus> searchApplicationStatusById(Integer id);

  /**
   * 受講生コースIDに紐づく申込状況を検索します。
   *
   * @param courseId 　受講生コースID
   * @return 受講生コースIDに紐づく申込状況
   */
  List<ApplicationStatus> searchApplicationStatusByCourseId(Integer courseId);

  /**
   * 条件に該当する受講生の検索を行います。 検索条件（名前、フリガナ、居住地域、年齢、性別）
   *
   * @param fullName,furigana,address,age,sex
   * @return 該当する受講生リスト
   */
  List<Student> findStudentsByConditions(
      @Param("fullName") String fullName,
      @Param("furigana") String furigana,
      @Param("address") String address,
      @Param("age") Integer age,
      @Param("sex") String sex);

  /**
   * コース名が一致する受講生コースの検索を行います。
   *
   * @param courseName
   * @return 該当する受講生コースのリスト
   */
  List<StudentCourse> findCoursesByConditions(@Param("courseName") String courseName);

  /**
   * ステータスが一致する申込状況の検索を行います。
   *
   * @param status
   * @return 該当する申込状況のリスト
   */
  List<ApplicationStatus> findApplicationStatusByConditions(@Param("status") Status status);


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
   * 申込状況を新規登録します。IDに関しては自動採番を行う。
   *
   * @param applicationStatus 　申込状況
   */
  void insertApplicationStatus(ApplicationStatus applicationStatus);

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

  /**
   * 申込状況を更新します。
   *
   * @param applicationStatus 申込状況
   */
  void updateStatus(ApplicationStatus applicationStatus);

}
