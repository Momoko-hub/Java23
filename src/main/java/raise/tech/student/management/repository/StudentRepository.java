package raise.tech.student.management.repository;

import java.util.List;
import org.apache.ibatis.annotations.Mapper;
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
   * 申込状況のIDを検索します。
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
   * 受講生の検索を行います。
   *
   * @param fullName 学生の名前
   * @return 学生情報
   */
  List<Student> searchStudentByName(String fullName);

  /**
   * 受講生の検索を行います。
   *
   * @param furigana 学生のフリガナ
   * @return 学生情報
   */
  List<Student> searchStudentByFurigana(String furigana);

  /**
   * 受講生の検索を行います。
   *
   * @param address 学生の居住地域
   * @return 学生情報
   */
  List<Student> searchStudentByAddress(String address);

  /**
   * 受講生の検索を行います。
   *
   * @param age 学生の年齢
   * @return 学生情報
   */
  List<Student> searchStudentByAge(Integer age);

  /**
   * 受講生の検索を行います。
   *
   * @param sex 学生の性別
   * @return 学生情報
   */
  List<Student> searchStudentBySex(String sex);

  /**
   * 受講生の検索を行います。
   *
   * @param courseName 受講コース名
   * @return 学生情報
   */
  List<StudentCourse> searchStudentCourseByCourseName(String courseName);

  /**
   * 申込状況の検索を行います。
   *
   * @param status 　申込状況
   * @return 受講生コースIDに紐づく申込状況
   */
  List<ApplicationStatus> searchApplicationStatusByStatus(Status status);

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
