package raise.tech.student.management.controller;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import raise.tech.student.management.controller.converter.StudentConverter;
import raise.tech.student.management.domain.StudentDetail;
import raise.tech.student.management.exception.TestException;
import raise.tech.student.management.repository.StudentRepository;
import raise.tech.student.management.service.StudentService;

/**
 * 受講生の検索や登録、更新などを行うREST APIとして実行されるControllerです。
 */
@Validated
@RestController
public class StudentController {


  private final StudentService service;
  private StudentRepository repository;


  @Autowired
  public StudentController(StudentService service, StudentConverter converter,
      StudentRepository repository) {
    this.service = service;
    this.repository = repository;

  }

  /**
   * 受講生詳細の一覧検索です。 全件検索を行うので、条件指定は行わないものになります。
   *
   * @return 受講生詳細一覧（全件）
   */
  @Operation(summary = "一覧検索", description = "すべての受講生の詳細情報を取得します。")
  @ApiResponses(value = {
      @ApiResponse(
          responseCode = "200",
          description = "成功時は受講生一覧が返されます。",
          content = @Content(mediaType = "application/json")),
      @ApiResponse(
          responseCode = "500",
          description = "サーバーでエラーが発生しました。")
  })
  @GetMapping("/studentsList")
  public List<StudentDetail> getStudentsList() {
    return service.searchStudentList();
  }

  /**
   * 受講生詳細の検索です。 IDに紐づく任意の受講生の情報を取得します。
   *
   * @param id 　受講生ID
   * @return 受講生
   */
  //受講生情報の更新
  @Operation(summary = "IDによる受講生検索", description = "指定したIDの受講生詳細情報を取得します。")
  @ApiResponses(value = {
      @ApiResponse(
          responseCode = "200",
          description = "成功時は受講生の詳細情報が返されます。",
          content = @Content(mediaType = "application/json")),
      @ApiResponse(
          responseCode = "404",
          description = "指定したIDの受講生が見つかりません。"),
      @ApiResponse(
          responseCode = "400",
          description = "無効なIDが提供されました。")

  })
  @GetMapping("/student/{id}")
  public StudentDetail getStudent(@PathVariable @NotNull Integer id) {
    return service.searchStudent(id);
  }


  /**
   * 受講生詳細の登録を行います。
   *
   * @param studentDetail 受講生詳細
   * @return 実行結果
   */
  @Operation(summary = "受講生登録", description = "新規受講生の登録を行います。IDは自動生成され、クライアント側からは指定しません。")
  @ApiResponses(value = {
      @ApiResponse(
          responseCode = "200",
          description = "登録された受講生の情報を返します。"),
      @ApiResponse(
          responseCode = "400",
          description = "リクエストにエラーがあります。（バリデーションエラー）"),
      @ApiResponse(
          responseCode = "500",
          description = "サーバーでエラーが発生しました。")})

  @PostMapping("/registerStudent")
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
  @ApiResponses(value = {
      @ApiResponse(
          responseCode = "200",
          description = "更新処理が成功しました。"),
      @ApiResponse(
          responseCode = "400",
          description = "リクエストにエラーがあります。（バリデーションエラー）"),
      @ApiResponse(
          responseCode = "500",
          description = "サーバーでエラーが発生しました。")})
  @PutMapping("/updateStudent")
  public ResponseEntity<String> updateStudent(@RequestBody StudentDetail studentDetail) {
    service.updateStudent(studentDetail);
    return ResponseEntity.ok("更新処理が成功しました。");
  }

  /**
   * 例外処理用
   */
  @Operation(summary = "例外処理", description = "例外処理を実行し、現在このAPIが使用できないことを通知します。")
  @ApiResponses(value = {
      @ApiResponse(
          responseCode = "400",
          description = "リクエストにエラーがあります。",
          content = @Content(
              mediaType = "application/json",
              schema = @Schema(
                  description = "エラーレスポンス",
                  example = "{ \"error\": \"現在このAPIは使用できません。URLは「students』ではなく「studentsList」を利用してください。\"}"))),
      @ApiResponse(
          responseCode = "500",
          description = "サーバーでエラーが発生しました。")})
  @GetMapping("/students")
  public List<StudentDetail> exceptionHandler() throws TestException {
    throw new TestException(
        "現在このAPIは使用できません。URLは「students』ではなく「studentsList」を利用してください。");
  }
}


