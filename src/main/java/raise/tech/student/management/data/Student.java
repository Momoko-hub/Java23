package raise.tech.student.management.data;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Schema(description = "受講生")
@Getter
@Setter

public class Student {

  @Schema(description = "受講生のID（サーバーで自動採番されます。）", examples = "123456")
  private Integer id;

  @NotBlank
  private String fullName;

  @NotBlank
  private String furigana;

  private String nickname;

  @NotBlank
  @Email
  private String emailAddress;

  @NotBlank
  private String address;

  private int age;

  @NotBlank
  private String sex;

  private String remark;

  private boolean isDeleted = false;


}

