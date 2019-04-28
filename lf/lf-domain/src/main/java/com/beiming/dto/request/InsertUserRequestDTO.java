package com.beiming.dto.request;

import javax.validation.constraints.NotNull;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel(value = "UserRequestDTO" , description = "新增用户DTO")
@Data
public class InsertUserRequestDTO {

  @ApiModelProperty(value = "用户名", required = true)
  @NotNull(message = "用户名不能为空")
  private String username;
  
  @ApiModelProperty(value = "密码", required = true)
  @NotNull(message = "密码不能为空")
  private String password;
}
