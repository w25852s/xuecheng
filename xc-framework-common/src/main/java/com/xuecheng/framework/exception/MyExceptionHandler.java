package com.xuecheng.framework.exception;

import com.google.common.collect.ImmutableMap;
import com.xuecheng.framework.model.response.CommonCode;
import com.xuecheng.framework.model.response.ResponseResult;
import com.xuecheng.framework.model.response.ResultCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;

@ControllerAdvice
@Slf4j
public class MyExceptionHandler {

    private static ImmutableMap<Class<? extends Exception>, ResultCode> EXCEPTION;
    private static ImmutableMap.Builder<Class<? extends Exception>, ResultCode> builder=ImmutableMap.builder();





    @ExceptionHandler(CustomerException.class)
    @ResponseBody

    public ResponseResult customerException(CustomerException exception) {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String now = format.format(new Date());
        ResultCode resultCode = exception.getResultCode();
        log.error(now + "产生错误 ：" + resultCode.message());
        return new ResponseResult(resultCode);

    }


    @ExceptionHandler(Exception.class)
    @ResponseBody
    public ResponseResult exception(Exception exception) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String now = format.format(new Date());
        if (EXCEPTION == null) {
            EXCEPTION=builder.build();
        }

        if(EXCEPTION.get(exception.getClass())!=null){
            return new ResponseResult(EXCEPTION.get(exception.getClass()));
        }
        return new ResponseResult(CommonCode.SERVER_ERROR);

    }

    static{
        builder.put(HttpMessageNotReadableException.class, CommonCode.INVALD_PARAM);
    }


}
