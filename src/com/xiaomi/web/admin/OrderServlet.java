package com.xiaomi.web.admin;

import com.xiaomi.domain.Order;
import com.xiaomi.domain.PageBean;
import com.xiaomi.domain.User;
import com.xiaomi.service.OrderService;
import com.xiaomi.service.impl.OrderServiceImpl;
import com.xiaomi.utils.StringUtils;
import com.xiaomi.web.servlet.BaseServlet;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(name = "orderservlet", value = "/admin/orderservlet")
public class OrderServlet extends BaseServlet {
    public String getAllOrder(HttpServletRequest request, HttpServletResponse response) {
        User admin =(User) request.getSession().getAttribute("admin");
        if (admin == null) {
            return "redirect:/admin/login.jsp";
        }

        //获取数据
        String pageNum = request.getParameter("pageNum");
        String pageSize = request.getParameter("pageSize");
        String username = request.getParameter("username");
        String orderStatus = request.getParameter("orderStatus");

        int pn = 1;
        int ps = 6;
        if (!StringUtils.isEmpty(pageNum)) {
            pn = Integer.valueOf(pageNum);
            if (pn < 1) {
                pn = 1;
            }
        }

        if (!StringUtils.isEmpty(pageSize)) {
            ps = Integer.valueOf(pageSize);
            if (ps < 1) {
                ps = 6;
            }
        }

        String condition = " where 1=1 ";
        if (!StringUtils.isEmpty(username)) {
            condition += " and u.username like '%"+username+"%' ";
        }
        if (!StringUtils.isEmpty(orderStatus)) {
            condition += " and o.status = '"+orderStatus+"' ";
        }

        OrderService orderService = new OrderServiceImpl();
        PageBean<Order> pageBean = orderService.orderByPage(pn, ps, condition);

        request.setAttribute("pageBean", pageBean);
        request.setAttribute("username", username);
        request.setAttribute("status", orderStatus);

        return "/admin/showAllOrder.jsp";
    }
    public String sendOrder(HttpServletRequest request, HttpServletResponse response) {
        User admin =(User) request.getSession().getAttribute("admin");
        if (admin == null) {
            return "redirect:/admin/login.jsp";
        }

        String oid = request.getParameter("oid");
        OrderService orderService = new OrderServiceImpl();
        orderService.updateStatus(oid, "3");//3标示已发货

        getAllOrder(request, response);

        return null;

    }
}
