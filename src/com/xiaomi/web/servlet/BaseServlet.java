package com.xiaomi.web.servlet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.Method;

/**
 * 不需要注解，主要是提取方法
 */
public class BaseServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //判断用户行为,http://localhost:8080/rj_xiaomi/userservlet?method=register通过参数传值进行选择调取
        String methodName = request.getParameter("method");

        //利用反射调用
        try {
            Method method = this.getClass().getMethod(methodName, HttpServletRequest.class, HttpServletResponse.class);
            String url =(String) method.invoke(this, request, response);//等同于this.method，方法名调用
            if (url != null && url.trim().length() != 0) {
                //url不是空，而且不是0，说明是要转发或者重定向，定义规则，如果有"redirect:"说明要重定向
                if (url.startsWith("redirect:")) {
                    //重定向
                    response.sendRedirect(request.getContextPath() + url.split(":")[1]);
                } else {
                    //转发
                    request.getRequestDispatcher(url).forward(request, response);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }
}
