package raise.tech.student.management.data;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;
import raise.tech.student.management.domain.Status;

@Schema(description = "受講生コース情報")
@Getter
@Setter
public class StudentCourse {

  @Schema(description = "受講生コース情報のID（サーバーで自動採番されます。）", examples = "123456")
  private Integer id;

  @Schema(description = "受講生IDと連携しています。", examples = "123456")
  private Integer studentsId;

  @NotBlank
  private String courseName;

  private Status status;

  private LocalDateTime startDate;

  private LocalDateTime endDate;


}
