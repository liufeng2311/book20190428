package com.beiming.util;

import java.io.Serializable;
import com.beiming.enums.ExceptionEnum;

import lombok.Data;
@Data
public class ResultModel implements Serializable{

  private static final long serialVersionUID = 1L;

  private Integer Code;//0表示成功，非0表示存在异常

  private String message;//各个状态码对应的具体错误信息

  private Object data;//成功时返回数据，失败时置空

  /**
   * 成功时调用的方法
   * @param result
   * @return
   */
  public static ResultModel success(Object result) {
    ResultModel type =new ResultModel();
    type.setCode(ExceptionEnum.SUCCESS.getCode());
    type.setMessage(ExceptionEnum.SUCCESS.getMessage());
    type.setData(result);
    return type;
  }

  /**
   * 失败时调用的方法，并传入失败的原因
   * @param result
   * @param message
   * @param code
   * @return
   */
  public static ResultModel faile(Object result,String message,Integer code) {
    ResultModel type =new ResultModel();
    type.setCode(code);
    type.setMessage(message);
    return type;
  }
}
