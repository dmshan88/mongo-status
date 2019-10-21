package com.example.controller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.ErrorAttributes;
import org.springframework.boot.autoconfigure.web.ErrorController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.util.UriComponentsBuilder;

import com.example.common.CustomResponse;
import com.example.common.ErrorCode;


/**自定义错误页面*/
@RestController
public class CustomErrorController implements ErrorController {

    private static final String PATH = "/error";
    private static final String PARAM_MESSAGE = "message";
    private static final String PARAM_CODE = "code";
    
    @Autowired
    private ErrorAttributes errorAttributes;

    @Override
    public String getErrorPath() {
        return PATH;
    }
    
    /**自定义错误转发路径*/
    static public String getCustomErrorPath(ErrorCode errorCode, String message) {
        return UriComponentsBuilder.fromUriString(CustomErrorController.PATH)
                .queryParam(CustomErrorController.PARAM_CODE, errorCode.getCode())
                .queryParam(CustomErrorController.PARAM_MESSAGE, message).toUriString();
    }
    
    @RequestMapping(value = PATH)
    CustomResponse<Object> error(HttpServletRequest request, HttpServletResponse response,
            @RequestParam(value = PARAM_MESSAGE, required = false) String message,
            @RequestParam(value = PARAM_CODE, required = false) Integer code) {
        RequestAttributes requestAttributes = new ServletRequestAttributes(request);
        Map<String, Object> errorMap = this.errorAttributes.getErrorAttributes(requestAttributes, false);
        CustomResponse<Object> responseData = new CustomResponse<Object>(null);
        ErrorCode errorCode = ErrorCode.ERROR;
        if (message != null && code != null) { //处理转发的错误信息
            errorCode = ErrorCode.getByCode(code);
        } else {
            switch (response.getStatus()) {
            //对不能捕获的404等异常手动赋值
            case 404:
                errorCode = ErrorCode.ERROR;
                message = "404 路径不存在";
                break;
            default:
                //获取错误信息
                message = errorMap.get("message").toString();
            }
        }
        responseData.setStatus(response.getStatus());
        responseData.setErrorCode(errorCode, message);
        response.setStatus(HttpServletResponse.SC_OK);
        return responseData;
    }
}