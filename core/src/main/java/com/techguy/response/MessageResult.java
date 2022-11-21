package com.techguy.response;

import com.techguy.constant.CommonConstant;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
public class MessageResult<T> implements Serializable {
  private static final long serialVersionUID =1L;

    private boolean success = true;
    private String message = "operation success";
    private Integer code = CommonConstant.OK_200;
    private T result;
    private Object obj;

    public Object setResult(Object obj){
      this.obj =  obj;
      return obj;
    }


    private long timestamp = System.currentTimeMillis();

    public MessageResult<T> success(String message){
            this.message=message;
            this.code= CommonConstant.OK_200;
            this.success=true;
            return this;
    }
  public static<T> MessageResult<T> OK() {
    MessageResult<T> result = new MessageResult<T>();
    result.setSuccess(true);
    result.setCode(CommonConstant.OK_200);
    result.setMessage("success");
    return result;
  }
  public static<T> MessageResult<T> error(String msg, T data) {
    MessageResult<T> result = new MessageResult<T>();
    result.setSuccess(false);
    result.setCode(CommonConstant.INTERNAL_SERVER_ERROR_500);
    result.setMessage(msg);
    result.setResult(data);
    return result;
  }
  public static MessageResult<Object> error(String msg) {
    return error(CommonConstant.INTERNAL_SERVER_ERROR_500, msg);
  }
  public static MessageResult<Object> error(int code, String msg) {
    MessageResult<Object> result = new MessageResult<>();
    result.setCode(code);
    result.setMessage(msg);
    result.setSuccess(false);
    return result;
  }

  public MessageResult<T> error500(String message) {
    this.message = message;
    this.code = CommonConstant.INTERNAL_SERVER_ERROR_500;
    this.success = false;
    return this;
  }
  public static MessageResult<Object> noauth(String msg) {
    return error(CommonConstant.NO_AUTHZ, msg);
  }
}
