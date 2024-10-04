package raise.tech.student.management.data;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;
import raise.tech.student.management.domain.Status;

@Schema(description = "申込状況")
@Getter
@Setter
public class ApplicationStatus {

  private Integer id;

  private Integer courseId;

  private Status status;

  private LocalDateTime createdAt;

  private LocalDateTime updatedAt;
}
