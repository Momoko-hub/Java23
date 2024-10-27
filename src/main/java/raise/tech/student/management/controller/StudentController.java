package raise.tech.student.management.controller;


import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import raise.tech.student.management.data.ApplicationStatus;
import raise.tech.student.management.domain.Status;
import raise.tech.student.management.domain.StudentDetail;
import raise.tech.student.management.exception.TestException;
import raise.tech.student.management.service.StudentService;

/**
 * 受講生の検索や登録、更新などを行うREST APIとして実行されるControllerです。
 */
@Validated
@RestController
public class StudentController {

  private final StudentService service;

  @Autowired
  public StudentController(StudentService service) {
    this.service = service;

  }

  /**
   * 受講生詳細の一覧検索です。 全件検索を行うので、条件指定は行わないものになります。デフォルトでは論理削除されたデータは含みません。
   *
   * @return 受講生詳細一覧（全件）
   */
  @Operation(summary = "一覧検索（論理削除除く）", description = "すべての受講生の詳細情報を取得します。デフォルトでは論理削除されたデータは含みません。")
  @GetMapping("/students")
  public List<StudentDetail> getStudentsList(
      @RequestParam(required = false, defaultValue = "false") boolean includeDeleted) {
    return includeDeleted ? service.searchAllStudentList() : service.searchStudentList();
  }

  /**
   * 受講生詳細の検索です。 IDに紐づく任意の受講生の情報を取得します。
   *
   * @param id 　受講生ID
   * @return 受講生詳細
   */
  @Operation(summary = "IDによる受講生検索", description = "指定したIDの受講生詳細情報を取得します。")
  @GetMapping("/students/{id}")
  public StudentDetail getStudent(@PathVariable @NotNull Integer id) {
    return service.searchStudent(id);
  }

  /**
   * 受講生詳細の条件検索です。 検索した名前と一致する任意の受講生の情報を取得します。
   *
   * @param fullName 　受講生の名前
   * @return 受講生詳細
   */
  @Operation(summary = "名前による受講生検索", description = "検索した条件の受講生詳細情報を取得します。")
  @GetMapping("/students/search")
  public List<StudentDetail> searchStudents(
      @RequestParam(required = false) String fullName,
      @RequestParam(required = false) String furigana,
      @RequestParam(required = false) String address,
      @RequestParam(required = false) Integer age,
      @RequestParam(required = false) String sex,
      @RequestParam(required = false) String courseName,
      @RequestParam(required = false) Status status
  ) {
    return service.searchStudentsByConditions(fullName, furigana, address, age, sex, courseName,
        status);
  }

  /**
   * 受講生詳細の登録を行います。
   *
   * @param studentDetail 受講生詳細
   * @return 実行結果
   */
  @Operation(summary = "受講生登録", description = "新規受講生の登録を行います。IDは自動生成され、クライアント側からは指定しません。")
  @PostMapping("/students")
  public ResponseEntity<StudentDetail> registerStudent(
      @RequestBody @Valid StudentDetail studentDetail) {
    StudentDetail responseStudentDetail = service.registerStudent(studentDetail);
    return ResponseEntity.ok(responseStudentDetail);
  }

  /**
   * 受講生詳細の更新を行います。キャンセルフラグの更新もここで行います。（論理削除）
   *
   * @param studentDetail 受講生詳細
   * @return 実行結果
   */
  @Operation(summary = "受講生更新", description = "受講生詳細の更新を行います。キャンセルフラグの更新もここで行います。（論理削除）")
  @PutMapping("/students")
  public ResponseEntity<String> updateStudent(@RequestBody StudentDetail studentDetail) {
    service.updateStudent(studentDetail);
    return ResponseEntity.ok("更新処理が成功しました。");
  }

  /**
   * 申込状況を仮申込から本申込に変更します。
   *
   * @param id 申込状況ID
   * @return 実行結果
   */
  @Operation(summary = "受講生更新", description = "申込状況を更新します")
  @PutMapping("/students/request-status")
  public ResponseEntity<String> updateStatus(@RequestParam Integer id,
      @RequestParam Status status) {
    List<ApplicationStatus> applicationStatus = service.findStatusById(id);

    switch (status) {
      case Status.仮申込 -> service.updateStatusToMainApplication(applicationStatus);

      case Status.本申込 -> service.updateStatusToTakingTheCourse(applicationStatus);

      case Status.受講中 -> service.updateStatusToCourseCompleted(applicationStatus);

      default -> {
        return ResponseEntity.badRequest().body("ステータスが更新できませんでした：" + status);
      }
    }
    String updatedStudentDetail = applicationStatus.stream()
        .map(as -> "ID:" + as.getId() + ", 申込状況:" + as.getStatus()
            + ", 更新日時:" + as.getUpdatedAt())
        .collect(Collectors.joining("\n"));

    return ResponseEntity.ok("申込状況が本申込に更新されました。\n" + updatedStudentDetail);
  }

  /**
   * 例外処理用
   */
  @Operation(summary = "例外処理", description = "例外処理を実行し、現在このAPIが使用できないことを通知します。")
  @GetMapping("/students/descriptions")
  public List<StudentDetail> getStudentDetail() throws TestException {
    throw new TestException(
        "現在このAPIは使用できません。URLは「students』ではなく「studentsList」を利用してください。");
  }
}




