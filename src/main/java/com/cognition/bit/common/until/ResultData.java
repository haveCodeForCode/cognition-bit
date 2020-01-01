package com.cognition.bit.common.until;


import com.cognition.bit.common.config.Constant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 返回功能函数
 * @author Worry
 * @version 2019/3/10
 */
public class ResultData {

    protected Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * 是否成功
     */
    private boolean success;

    /**
     * 状态编码
     */
    private int code;

    /**
     * 服务器返回消息
     */
    private String msg;

    /**
     * 状态码 1：成功 0：失败
     */
    private int state;

    /**
     * 回传数据对象
     */
    private Object data;

    /**
     * 回传状态
     * @param result
     * @return
     */
    public static ResultData result(boolean result) {
        return new ResultData().setSuccess(result).setState(result ? 1 : 0)
                .setCode(result ? Constant.SERVER_SUCCESS : Constant.SERVER_ERROR)
                .setMsg(result ? Constant.DEFAULT_SUCCESS_MSG : Constant.SERVER_ERROR_MSG);
    }

    /**
     * 回传带消息
     * @param result
     * @param msg
     * @return
     */
    public static ResultData result(boolean result, String msg) {
        return new ResultData().setSuccess(result).setState(result ? 1 : 0)
                .setCode(result ? Constant.SERVER_SUCCESS : Constant.SERVER_ERROR)
                .setMsg(msg);
    }


    /**
     * 回传带数据
     * @param result
     * @param object
     * @return
     */
    public static ResultData result(boolean result, Object object) {
        return new ResultData().setSuccess(result).setState(result ? 1 : 0)
                .setCode(result ? Constant.SERVER_SUCCESS : Constant.SERVER_ERROR)
                .setMsg(result ? Constant.DEFAULT_SUCCESS_MSG : Constant.SERVER_ERROR_MSG)
                .setData(object);
    }



    public boolean isSuccess() {
        return success;
    }

    private ResultData setSuccess(boolean success) {
        this.success = success;
        return this;
    }

    public int getCode() {
        return code;
    }

    private ResultData setCode(int code) {
        this.code = code;
        return this;
    }

    public String getMsg() {
        return msg;
    }

    public ResultData setMsg(String msg) {
        this.msg = msg;
        return this;
    }

    public int getState() {
        return state;
    }

    public ResultData setState(int state) {
        this.state = state;
        return this;
    }

    public Object getData() {
        return data;
    }

    public ResultData setData(Object data) {
        this.data = data;
        return this;
    }
}
