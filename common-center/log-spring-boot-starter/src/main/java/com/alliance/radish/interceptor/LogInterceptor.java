package com.alliance.radish.interceptor;

import com.alliance.radish.constant.TraceConstant;
import com.alliance.radish.utils.SnowflakeIdWorker;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 创建拦截器，在请求达到时生成traceId
 */
@Component
public class LogInterceptor implements HandlerInterceptor {

    /** 雪花ID */
    private static SnowflakeIdWorker idWorker = new SnowflakeIdWorker(0, 0);

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String traceId = request.getHeader(TraceConstant.HTTP_HEADER_TRACE_ID);
        if (StringUtils.isNotEmpty(traceId)) {
            MDC.put(TraceConstant.LOG_TRACE_ID, traceId);
        }else{
            MDC.put(TraceConstant.LOG_TRACE_ID, String.valueOf(idWorker.nextId()));
        }
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        MDC.clear();
    }
}
