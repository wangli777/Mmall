package com.mmall.common;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import java.io.Serializable;

/**
 * @Author: WangLi
 * @Date: 2018/9/13 13:08
 * @Package: com.mmall.common
 * @Description: 这是一个通用的消息响应类
 */
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)//保证序列化Json时，如果value是null，则不保存该条
public class ServerResponse<T> implements Serializable {
    private int status;
    private String msg;
    private T data;

    public ServerResponse(int status, String msg, T data) {
        this.status = status;
        this.msg = msg;
        this.data = data;
    }

    public ServerResponse(int status, T data) {

        this.status = status;
        this.data = data;
    }

    public ServerResponse(int status, String msg) {

        this.status = status;
        this.msg = msg;
    }

    public ServerResponse(int status) {

        this.status = status;
    }

    /**
     * Getter
     * @return
     */
    public int getStatus() {
        return status;
    }

    public String getMsg() {
        return msg;
    }

    public T getData() {
        return data;
    }

    /**
     * @JsonIgnore 注解使之不在Json序列化结果当中
     * @return
     */
    @JsonIgnore
    public boolean isSuccess(){
        return this.status == ResponseCode.SUCCESS.getCode();
    }

    public static <T> ServerResponse<T> createBySuccess(){
        return new ServerResponse<T>(ResponseCode.SUCCESS.getCode());
    }
    public static <T> ServerResponse<T> createBySuccessMessage(String msg){
        return new ServerResponse<T>(ResponseCode.SUCCESS.getCode(),msg);
    }
    public static <T> ServerResponse<T> createBySuccessData(T data){
        return new ServerResponse<T>(ResponseCode.SUCCESS.getCode(),data);
    }
    public static <T> ServerResponse<T> createBySuccess(String msg,T data){
        return new ServerResponse<T>(ResponseCode.SUCCESS.getCode(),msg,data);
    }

    public static <T> ServerResponse<T> createByError(){
        return new ServerResponse<T>(ResponseCode.ERROR.getCode(),ResponseCode.ERROR.getDesc());
    }
    public static <T> ServerResponse<T> createByErrorMessage(String msg){
        return new ServerResponse<T>(ResponseCode.ERROR.getCode(),msg);
    }
    public static <T> ServerResponse<T> createByErrorCodeMessage(int code,String msg){
        return new ServerResponse<T>(code,msg);
    }
}
