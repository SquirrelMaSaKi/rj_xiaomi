package com.xiaomi.web.admin;

import com.alibaba.fastjson.JSON;
import com.xiaomi.domain.PageBean;
import com.xiaomi.domain.User;
import com.xiaomi.service.UserService;
import com.xiaomi.service.impl.UserServiceImpl;
import com.xiaomi.utils.StringUtils;
import com.xiaomi.web.servlet.BaseServlet;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "UserServlet", value = "/admin/userservlet")
public class UserServlet extends BaseServlet {
    public String getUserList(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/html;charset=utf-8");

        //判断是否登录
        User admin = (User) request.getSession().getAttribute("admin");
        if (admin == null) {
            return "redirect:admin/login.jsp";
        }

        //2获取（有效）用户列表
        //分页处理
        String pageNum = request.getParameter("pageNum");
        String pageSize = request.getParameter("pageSize");

        String username = request.getParameter("username");
        String gender = request.getParameter("gender");


        int pn = 1;
        int ps = 8;
        if (!StringUtils.isEmpty(pageNum)) {
            pn = Integer.valueOf(pageNum);
            if (pn < 1) {
                pn = 1;
            }
        }
        if (!StringUtils.isEmpty(pageSize)) {
            ps = Integer.valueOf(pageSize);
            if (ps < 1) {
                ps = 8;
            }
        }

        String condition = "flag!=2";

        if (!StringUtils.isEmpty(username)) {
            condition += " and username like '%"+username+"%'";
        }

        if (!StringUtils.isEmpty(gender)) {
            condition += " and gender= '"+gender+"'";
        }



        UserService userService = new UserServiceImpl();

        PageBean<User> pageBean = userService.findByPage(pn,ps,condition);

        //3前端需要的是json字符串，所以这里直接进行Json处理
        //另外，前端还是ajax请求，所以只需要使用write方法
        String string = JSON.toJSONString(pageBean);
        response.getWriter().write(string);
        //request.setAttribute("pageBean", pageBean);

        return null;
    }

    //无效会员列表
    public String invalidUserList(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/html;charset=utf-8");

        //判断是否登录
        User admin = (User) request.getSession().getAttribute("admin");
        if (admin == null) {
            return "redirect:admin/login.jsp";
        }

        //2获取（有效）用户列表
        //分页处理
        String pageNum = request.getParameter("pageNum");
        String pageSize = request.getParameter("pageSize");

        String username = request.getParameter("username");
        String gender = request.getParameter("gender");


        int pn = 1;
        int ps = 8;
        if (!StringUtils.isEmpty(pageNum)) {
            pn = Integer.valueOf(pageNum);
            if (pn < 1) {
                pn = 1;
            }
        }
        if (!StringUtils.isEmpty(pageSize)) {
            ps = Integer.valueOf(pageSize);
            if (ps < 1) {
                ps = 8;
            }
        }

        String condition = "flag=2";

        if (!StringUtils.isEmpty(username)) {
            condition += " and username like '%"+username+"%'";
        }

        if (!StringUtils.isEmpty(gender)) {
            condition += " and gender= '"+gender+"'";
        }



        UserService userService = new UserServiceImpl();

        PageBean<User> pageBean = userService.findByPage(pn,ps,condition);

        //3前端需要的是json字符串，所以这里直接进行Json处理
        //另外，前端还是ajax请求，所以只需要使用write方法
        String string = JSON.toJSONString(pageBean);
        response.getWriter().write(string);
        //request.setAttribute("pageBean", pageBean);

        return null;
    }

    public String deleteUser(HttpServletRequest request, HttpServletResponse response) {
        response.setContentType("text/html;charset=utf-8");

        //判断用户登录
        User admin = (User) request.getSession().getAttribute("admin");
        if (admin == null) {
            return "redirect:admin/login.jsp";
        }


        //获取参数
        String id = request.getParameter("id");
        int chooseId = Integer.valueOf(id);
        if (chooseId != admin.getId()) {
            UserService userService = new UserServiceImpl();
            userService.updateFlag(chooseId,2);
        } else {
            try {
                response.getWriter().write("不能删除正在登陆用户");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public String renewUser(HttpServletRequest request, HttpServletResponse response) {
        response.setContentType("text/html;charset=utf-8");

        //判断用户登录
        User admin = (User) request.getSession().getAttribute("admin");
        if (admin == null) {
            return "redirect:admin/login.jsp";
        }


        //获取参数
        String id = request.getParameter("id");
        int chooseId = Integer.valueOf(id);

        UserService userService = new UserServiceImpl();
        userService.updateFlag(chooseId,1);

        return null;
    }

    public String adminLogin(HttpServletRequest request, HttpServletResponse response) {
        String username = request.getParameter("username");
        String password = request.getParameter("password");

        UserService userService = new UserServiceImpl();
        User user = userService.login(username, password);
        if (user != null) {
            if (user.getRole() != 0) {
                request.setAttribute("msg", "请用管理员账户登录");
                return "/admin/login.jsp";
            }
        } else {
            request.setAttribute("msg", "用户名或密码错误");
            return "/admin/login.jsp";
        }

        request.getSession().setAttribute("admin", user);

        return "redirect:/admin/admin.jsp";
    }

    //退出
    public String loginout(HttpServletRequest request, HttpServletResponse response) {
        //1.把session清楚用户
        //2.session失效
        request.getSession().removeAttribute("admin");
        request.getSession().invalidate();
        return "redirect:/admin/login.jsp";
    }
}
