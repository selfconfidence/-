package com.manyun.common.security.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.manyun.comm.api.model.LoginBusinessUser;
import com.manyun.common.core.enums.UserLoginSource;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.AsyncHandlerInterceptor;
import com.manyun.common.core.constant.SecurityConstants;
import com.manyun.common.core.context.SecurityContextHolder;
import com.manyun.common.core.utils.ServletUtils;
import com.manyun.common.core.utils.StringUtils;
import com.manyun.common.security.auth.AuthUtil;
import com.manyun.common.security.utils.SecurityUtils;
import com.manyun.comm.api.model.LoginUser;

/**
 * 自定义请求头拦截器，将Header数据封装到线程变量中方便获取
 * 注意：此拦截器会同时验证当前用户有效期自动刷新有效期
 *
 * @author ruoyi
 */
public class HeaderInterceptor implements AsyncHandlerInterceptor
{
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception
    {
        if (!(handler instanceof HandlerMethod))
        {
            return true;
        }

        SecurityContextHolder.setUserId(ServletUtils.getHeader(request, SecurityConstants.DETAILS_USER_ID));
        SecurityContextHolder.setUserName(ServletUtils.getHeader(request, SecurityConstants.DETAILS_USERNAME));
        SecurityContextHolder.setUserKey(ServletUtils.getHeader(request, SecurityConstants.USER_KEY));
        SecurityContextHolder.setSource(ServletUtils.getHeader(request, SecurityConstants.DETAILS_LOGIN_SOURCE));

        String token = SecurityUtils.getToken();
        if (StringUtils.isNotEmpty(token))
        {
            // 此处需要适配
            adapterHandle(SecurityContextHolder.getSource(),token);

        }
        return true;
    }

    /**
     * 进行适配 token 来源出在那边
     * @param source
     * @param token
     */
    private void adapterHandle(String source,String  token) {
        execAdminData(source,token);
        execBuiData(source,token);


    }

    private void execAdminData(String source,String token){
        if (!UserLoginSource.PC.getInfo().equals(source))
            return;
        LoginUser loginUser = AuthUtil.getLoginUser(token);
        if (StringUtils.isNotNull(loginUser))
        {
            AuthUtil.verifyLoginUserExpire(loginUser);
            SecurityContextHolder.set(SecurityConstants.LOGIN_USER, loginUser);
        }
    }

    private void execBuiData(String source,String token){
        if (!UserLoginSource.APP.getInfo().equals(source))
            return;
        LoginBusinessUser loginUser = AuthUtil.getLoginBusinessUserUser(token);
        if (StringUtils.isNotNull(loginUser))
        {
            AuthUtil.verifyLoginUserExpire(loginUser);
            SecurityContextHolder.set(SecurityConstants.LOGIN_USER, loginUser);
        }
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
            throws Exception
    {
        SecurityContextHolder.remove();
    }
}
