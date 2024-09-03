package raise.tech.student.management.data;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class Student {

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

  private String sex;

  private String remark;

  private Boolean isDeleted = false;


}

