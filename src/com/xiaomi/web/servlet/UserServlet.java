package com.xiaomi.web.servlet;


import cn.dsna.util.images.ValidateCode;
import com.xiaomi.domain.Address;
import com.xiaomi.domain.User;
import com.xiaomi.service.AddressService;
import com.xiaomi.service.UserService;
import com.xiaomi.service.impl.AddressServiceImpl;
import com.xiaomi.service.impl.UserServiceImpl;
import com.xiaomi.utils.Base64Utils;
import com.xiaomi.utils.MD5Utils;
import com.xiaomi.utils.RandomUtils;
import com.xiaomi.utils.StringUtils;
import org.apache.commons.beanutils.BeanUtils;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet(name = "UserServlet1", value = "/userservlet")
public class UserServlet extends BaseServlet {

    public String register(HttpServletRequest request, HttpServletResponse response) {
        try {
            String repassword = request.getParameter("repassword");
            User user = new User();
            BeanUtils.populate(user, request.getParameterMap());

            //校验数据
            if (StringUtils.isEmpty(user.getUsername())) {
                request.setAttribute("registerMsg", "用户名不能为空");
                return "/register.jsp";
            }
            if (StringUtils.isEmpty(user.getPassword())) {
                request.setAttribute("registerMsg", "密码不能为空");
                return "/register.jsp";
            }
            if (user.getPassword().length() < 6 || user.getPassword().length() > 11) {
                request.setAttribute("registerMsg", "密码必须在6-11位");
                return "/register.jsp";
            }
            if (!user.getPassword().equals(repassword)) {
                request.setAttribute("registerMsg", "两次密码不一致");
                return "/register.jsp";
            }
            if (StringUtils.isEmpty(user.getEmail())) {
                request.setAttribute("registerMsg", "邮箱不能为空");
                return "/register.jsp";
            }
            //调用业务
            UserService userService = new UserServiceImpl();
            //有flag role code 没有赋值
            user.setFlag(0);
            user.setRole(1);
            user.setCode(RandomUtils.createActive());
            userService.register(user);
        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println("用户注册");
        return "/index.jsp";
    }

    public String checkUserName(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String username = request.getParameter("username");
        if (username == null || username.trim().length() == 0) {
            return null;
        }

        UserService userService = new UserServiceImpl();
        User user = userService.checkUserName(username);

        if (user != null) {
            response.getWriter().write("1");
        } else {
            response.getWriter().write("0");
        }
        return null;
    }

    //验证码
    public String code(HttpServletRequest request, HttpServletResponse response) {
        ValidateCode validateCode = new ValidateCode(100, 40, 4,20 );
        String code = validateCode.getCode();
        request.getSession().setAttribute("vcode", code);
        try {
            validateCode.write(response.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    //验证输入的验证码是否正确
    public String checkCode(HttpServletRequest request, HttpServletResponse response) {
        String clientcode = request.getParameter("code");
        String servercode = (String) request.getSession().getAttribute("vcode");

        if (StringUtils.isEmpty(clientcode)) {
            return null;
        }
        try {
            if (clientcode.equalsIgnoreCase(servercode)) {
                response.getWriter().write("0");
            } else {
                response.getWriter().write("1");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String login(HttpServletRequest request, HttpServletResponse response) {
        //获取用户名和密码
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String auto = request.getParameter("auto");
        String valcode = request.getParameter("valcode");
        String vcode = (String) request.getSession().getAttribute("vcode");

        if (StringUtils.isEmpty(valcode)) {
            request.setAttribute("msg", "验证码不能为空");
            return "/login.jsp";
        }

        //验证码要忽略大小写判断
        if (!valcode.equalsIgnoreCase(vcode)) {
            System.out.println(valcode);
            System.out.println(vcode);
            request.setAttribute("msg", "验证码输入不正确");
            return "/login.jsp";
        }


        if (StringUtils.isEmpty(username)) {
            request.setAttribute("msg", "用户名不能为空");
            return "/login.jsp";
        }
        if (StringUtils.isEmpty(password)) {
            request.setAttribute("msg", "密码不能为空");
            return "/login.jsp";
        }

        //验证用户名和密码是否正确
        UserService userService = new UserServiceImpl();
        User user = userService.login(username, password);
        if (user == null) {
            request.setAttribute("msg", "用户名和密码有误");
            return "/login.jsp";
        } else {
            //用户有没有激活
            if (user.getFlag() != 1) {
                request.setAttribute("msg", "用户尚未激活或禁用");
                return "/login.jsp";
            }
            //有没有权限
            if (user.getRole() != 1) {
                request.setAttribute("msg", "用户没有权限");
                return "/login.jsp";
            }

            //登录成功
            request.getSession().setAttribute("user", user);

            //自动登录
            if (!StringUtils.isEmpty(auto)) {
                String userinfo = username + "#" + password;
                Cookie cookie = new Cookie("userinfo", Base64Utils.encode(userinfo));
                cookie.setMaxAge(60*60*24*14);
                cookie.setPath("/");
                cookie.setHttpOnly(true);
                response.addCookie(cookie);
            }

            //重定向到首页
            return "redirect:/index.jsp";
        }
    }

    //退出登录
    public String logOut(HttpServletRequest request, HttpServletResponse response) {
        //删除session中的数据
        request.getSession().removeAttribute("user");
        //删除session
        request.getSession().invalidate();
        //删除cookie
        Cookie cookie = new Cookie("userinfo", "");
        cookie.setMaxAge(0);
        cookie.setPath("/");
        cookie.setHttpOnly(true);
        response.addCookie(cookie);
        return "redirect:/index.jsp";
    }

    //获取收货地址
    public String getAddress(HttpServletRequest request, HttpServletResponse response) {
       User user = (User) request.getSession().getAttribute("user");
       if (user == null) {
           return "redirect:/login.jsp";
       }

        AddressService addressService = new AddressServiceImpl();
        List<Address> addresses = addressService.findByUid(user.getId());
        request.setAttribute("addList", addresses);
        return "/self_info.jsp";
    }

    //添加收获地址
    public String addAddress(HttpServletRequest request, HttpServletResponse response) {
        User user = (User) request.getSession().getAttribute("user");
        if (user == null) {
            return "redirect:/login.jsp";
        }

        //name phone detail
        String name = request.getParameter("name");
        String phone = request.getParameter("phone");
        String detail = request.getParameter("detail");//详细地址

        if (StringUtils.isEmpty(name)) {
            request.setAttribute("msg", "收件人不能为空");
            return getAddress(request, response); //再次执行这个方法
        }

        if (StringUtils.isEmpty(phone)) {
            request.setAttribute("msg", "电话不能为空");
            return "/userservlet?method=getAddress";
        }

        if (StringUtils.isEmpty(detail)) {
            request.setAttribute("msg", "收件地址不能为空");
            return "/userservlet?method=getAddress";
        }
        Address address = new Address(null, detail, name, phone, user.getId(), 0);
        AddressService addressService = new AddressServiceImpl();
        addressService.add(address);
        return getAddress(request, response);
    }

    public String defaultAddress(HttpServletRequest request, HttpServletResponse response) {
        User user = (User) request.getSession().getAttribute("user");
        if (user == null) {
            return "redirect:/login.jsp";
        }

        String id = request.getParameter("id");
        AddressService addressService = new AddressServiceImpl();
        addressService.updateDefault(Integer.valueOf(id), user.getId());

        //直接走上面的方法，重新获取地址列表
        return getAddress(request, response);
    }

    public String deleteAddress(HttpServletRequest request, HttpServletResponse response) {
        User user = (User) request.getSession().getAttribute("user");
        if (user == null) {
            return "redirect:/login.jsp";
        }

        String id = request.getParameter("id");
        AddressService addressService = new AddressServiceImpl();
        addressService.delete(Integer.valueOf(id));

        return getAddress(request, response);
    }

    public String updateAddress(HttpServletRequest request, HttpServletResponse response) throws Exception {
        User user = (User) request.getSession().getAttribute("user");
        response.setContentType("text/html;charset=utf-8");
        if (user == null) {
            return "redirect:/login.jsp";
        }

        String id = request.getParameter("id");
        String level = request.getParameter("level");
        String name = request.getParameter("name");
        String phone = request.getParameter("phone");
        String detail = request.getParameter("detail");

        if (StringUtils.isEmpty(name)) {
            response.getWriter().write("<script type='text/javascript'>alert('收件人不能为空');window.location='userservlet?method=getAddress'</script>");
        }
        if (StringUtils.isEmpty(phone)) {
            response.getWriter().write("<script type='text/javascript'>alert('电话不能为空');window.location='userservlet?method=getAddress'</script>");
        }
        if (StringUtils.isEmpty(detail)) {
            response.getWriter().write("<script type='text/javascript'>alert('详细地址不能为空');window.location='userservlet?method=getAddress'</script>");
        }

        //更新
        Address address = new Address(Integer.valueOf(id), detail, name, phone, user.getId(), Integer.valueOf(level));
        AddressService addressService = new AddressServiceImpl();
        addressService.update(address);

        return getAddress(request, response);
    }

    public String updatePassword(HttpServletRequest request, HttpServletResponse response) {
        response.setContentType("text/html;charset=utf-8");

        User user = (User) request.getSession().getAttribute("user");
        if (user == null) {
            return "redirect:/login.jsp";
        }

        String oldPassword = user.getPassword();
        String clientoldpassword = request.getParameter("password");
        String md5password = MD5Utils.md5(clientoldpassword);
        if (StringUtils.isEmpty(oldPassword)) {
            request.setAttribute("msg", "原始密码不能为空");
            return "/myself_info.jsp";
        }
        if (!oldPassword.equals(md5password)) {
            request.setAttribute("msg", "原始密码输入有误");
            return "/myself_info.jsp";
        }

        //获取新密码
        String newpassword = request.getParameter("newpassword");
        String newpassword2 = request.getParameter("newpassword2");
        if (StringUtils.isEmpty(newpassword)) {
            request.setAttribute("msg", "新密码不能为空");
            return "/myself_info.jsp";
        }
        if (!newpassword.equals(newpassword2)) {
            request.setAttribute("msg", "两次密码输入不一致");
            return "/myself_info.jsp";
        }
        try {
            UserService userService = new UserServiceImpl();
            userService.modifyPassword(user.getId(),newpassword);
            //修改成功，将cookie和session中的东西全部删除
            request.getSession().removeAttribute("user");
            Cookie cookie = new Cookie("userinfo", "");
            cookie.setMaxAge(0);
            cookie.setPath("/");
            cookie.setHttpOnly(true);
            response.addCookie(cookie);
            response.getWriter().write("<script type='text/javascript'>alert('密码修改成功，请重新登录');window.location.href='login.jsp'</script>");
        } catch (IOException e) {
            e.printStackTrace();
            request.setAttribute("msg", "密码修改失败");
            return "/myself_info.jsp";
        }
        return null;
    }
}
