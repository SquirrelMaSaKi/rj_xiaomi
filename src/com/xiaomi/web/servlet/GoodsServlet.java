package com.xiaomi.web.servlet;

import com.xiaomi.domain.Goods;
import com.xiaomi.domain.PageBean;
import com.xiaomi.service.GoodsService;
import com.xiaomi.service.impl.GoodsServiceIMpl;
import com.xiaomi.utils.StringUtils;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(name = "GoodsServlet1", value = "/goodsservlet")
public class GoodsServlet extends BaseServlet {
   public String getGoodsListByTypeId(HttpServletRequest request, HttpServletResponse response) {
       String typeId = request.getParameter("typeId");
       String pageNum = request.getParameter("pageNum");
       String pageSize = request.getParameter("pageSize");
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

       GoodsService goodsService = new GoodsServiceIMpl();
       String condition = "";
       if (!StringUtils.isEmpty(typeId)) {
           condition = "typeId = " + typeId;
       }
       try {
           PageBean<Goods> pageBean = goodsService.findPageBywhere(pn, ps, condition); // typeId = 1;
           request.setAttribute("pageBean", pageBean);
           request.setAttribute("typeId", typeId);
           return "/goodsList.jsp";
       } catch (Exception e) {
           e.printStackTrace();
           return "rediret:/index.jsp";
       }
   }

   public String getGoodsById(HttpServletRequest request, HttpServletResponse response) {
       String id = request.getParameter("id");
       GoodsService goodsService = new GoodsServiceIMpl();
       Goods goods = goodsService.findByID(Integer.valueOf(id));
       if (StringUtils.isEmpty(id)) {
           return "redirect:/index.jsp";
       }
       request.setAttribute("goods", goods);
       return "/goodsDetail.jsp";
   }
}
