package com.xiaomi.web.admin;

import com.alibaba.fastjson.JSON;
import com.xiaomi.domain.*;
import com.xiaomi.service.CartService;
import com.xiaomi.service.GoodsService;
import com.xiaomi.service.GoodsTypeService;
import com.xiaomi.service.OrderDetailService;
import com.xiaomi.service.impl.CartServiceImpl;
import com.xiaomi.service.impl.GoodsServiceIMpl;
import com.xiaomi.service.impl.GoodsTypeServiceImpl;
import com.xiaomi.service.impl.OrderDetailServiceImpl;
import com.xiaomi.utils.StringUtils;
import com.xiaomi.web.servlet.BaseServlet;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet(name="GoodsServlet",value = "/admin/goodsservlet")
public class GoodsServlet extends BaseServlet {
    public String getGoodsList(HttpServletRequest request, HttpServletResponse response) {
        User admin = (User) request.getSession().getAttribute("admin");
        if (admin == null) {
            return "redirect:/admin/login.jsp";
        }

        String pageNum = request.getParameter("pageNum");
        String pageSize = request.getParameter("pageSize");
        String name = request.getParameter("name");
        String pubdate = request.getParameter("pubdate");

        int pn = 1;
        int ps = 8;
        if (!StringUtils.isEmpty(pageNum)) {
            pn = Integer.valueOf(pageNum);
            if (pn < 1) {
                pn = 1;
            }
        }

        String condition = "1=1";
        if (!StringUtils.isEmpty(name)) {
            condition += " and name like '%"+ name+"%'";
            request.setAttribute("name", name);
        }
        if (!StringUtils.isEmpty(pubdate)) {
            condition += " and pubdate = '"+ pubdate +"'";
            request.setAttribute("pubdate", pubdate);
        }

        if (!StringUtils.isEmpty(pageSize)) {
            ps = Integer.valueOf(pageSize);
            if (ps < 1) {
                ps = 6;
            }
        }

        GoodsService goodsService = new GoodsServiceIMpl();
        PageBean<Goods> pageBean = goodsService.findPageBywhere(pn, ps, condition);

        request.setAttribute("pageBean", pageBean);

        return "/admin/showGoods.jsp";
    }
    public String getGoodsTypeJson(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json;charset=utf-8");

        User admin = (User) request.getSession().getAttribute("admin");
        if (admin == null) {
            return "redirect:/admin/login.jsp";
        }

        GoodsTypeService goodsTypeService = new GoodsTypeServiceImpl();
        List<GoodsType> typeList = goodsTypeService.findTypeByLevel(1);
        String string = JSON.toJSONString(typeList);
        response.getWriter().write(string);

        return null;
    }

    public String takeGoods(HttpServletRequest request, HttpServletResponse response) {
        String id = request.getParameter("id");
        if (!StringUtils.isEmpty(id)) {
            GoodsService goodsService = new GoodsServiceIMpl();
            Goods goods = goodsService.findByID(Integer.valueOf(id));
            request.setAttribute("goods", goods);
        }

        return "/admin/modifyGoods.jsp";
    }

    public String deleteGoods(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/html;charset=utf-8");
        String id = request.getParameter("id");
        int pid = Integer.valueOf(id);
        System.out.println(id);

        //判断购物车和订单详情中是否有物品，没有的话不能删除
        GoodsService goodsService = new GoodsServiceIMpl();
        CartService cartService = new CartServiceImpl();
        OrderDetailService orderDetailService = new OrderDetailServiceImpl();
        List<Cart> cartList = cartService.findByPid(pid);
        List<OrderDetail> orderDetailList = orderDetailService.findByPid(pid);

        if ((cartList==null || cartList.size()==0) && (orderDetailList==null || orderDetailList.size()==0)) {
            goodsService.deleteByPid(pid);
            return getGoodsList(request, response);
        } else {
            response.getWriter().write("<script type='text/javascript'>alert('该商品正在流通，不能删除，请等待交易完毕后处理');window.location.href='goodsservlet?method=getGoodsList'</script>");
        }
        return null;
    }
}
