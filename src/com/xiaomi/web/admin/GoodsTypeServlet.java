package com.xiaomi.web.admin;

import com.alibaba.fastjson.JSON;
import com.xiaomi.domain.Goods;
import com.xiaomi.domain.GoodsType;
import com.xiaomi.domain.PageBean;
import com.xiaomi.domain.User;
import com.xiaomi.service.GoodsService;
import com.xiaomi.service.GoodsTypeService;
import com.xiaomi.service.impl.GoodsServiceIMpl;
import com.xiaomi.service.impl.GoodsTypeServiceImpl;
import com.xiaomi.utils.StringUtils;
import com.xiaomi.web.servlet.BaseServlet;
import org.apache.commons.beanutils.BeanUtils;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

//http://localhost:8080/rj_xiaomi/admin/addGoodsType.jsp
//http://localhost:8080/rj_xiaomi/admin/goodstypeservlet?method=showGoodsType

@WebServlet(name = "GoodsTypeServlet", value = "/admin/goodstypeservlet")
public class GoodsTypeServlet extends BaseServlet {

    public String showGoodsType(HttpServletRequest request, HttpServletResponse response) throws IOException {
        User admin = (User) request.getSession().getAttribute("admin");
        if (admin == null) {
            return "redirect:/admin/login.jsp";
        }


        String pageNum = request.getParameter("pageNum");
        String pageSize = request.getParameter("pageSize");
        String name = request.getParameter("name");
        String level = request.getParameter("level");
        GoodsTypeService goodsTypeService = new GoodsTypeServiceImpl();
        int pn = 1;
        int ps = 5;
        if (!StringUtils.isEmpty(pageNum)) {
            pn = Integer.valueOf(pageNum);
            if (pn < 1) {
                pn = 1;
            }
        }

        if (!StringUtils.isEmpty(pageSize)) {
            ps = Integer.valueOf(ps);
            if (ps < 1) {
                ps = 5;
            }
        }

        String condition = "";

        if (!StringUtils.isEmpty(name)) {
            condition += " and name='"+ name +"' ";
        }
        if (!StringUtils.isEmpty(level)) {
            int lev = Integer.valueOf(level);
            condition += " and level="+lev+" ";
        }

        List<GoodsType> goodsTypeList = goodsTypeService.findTypeAll(pn,ps,condition);
        long totalSize = goodsTypeService.getCount(condition);

        List<GoodsType> goodsTypes = goodsTypeService.findGoodsTypes();

        PageBean<GoodsType> pageBean = new PageBean<>(pn, ps, totalSize, goodsTypeList);

        //一定要返回结果name和level，让前端的
        request.setAttribute("pageBean", pageBean);
        request.setAttribute("name", name);
        request.setAttribute("level", level);
        request.setAttribute("types", goodsTypes);


        return "/admin/showGoodsType.jsp";
    }

    public String modifyGoodsType(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String id = request.getParameter("id");
        String name = request.getParameter("name");
        String level = request.getParameter("level");
        String parent = request.getParameter("parent");


        if (StringUtils.isEmpty(name)) {
            request.setAttribute("msg", "类型名称不能为空");
            //response.getWriter().write("<script type='text/javascript'> alert('类型名称不能为空');</script>");
            return "/admin/goodstypeservlet?method=showGoodsType";
        }
        if (StringUtils.isEmpty(level)) {
            //response.getWriter().write("<script type='text/javascript'> alert('类型名称不能为空');</script>");
            request.setAttribute("msg", "品类等级不能为空");
            return "/admin/goodstypeservlet?method=showGoodsType";
        }



        GoodsTypeService goodsTypeService = new GoodsTypeServiceImpl();
        GoodsType goodsType = goodsTypeService.findTypeById(Integer.valueOf(id));
        goodsType.setName(name);
        goodsType.setLevel(Integer.valueOf(level));
        goodsType.setParent(Integer.valueOf(parent));
        GoodsType parentType = goodsTypeService.findTypeById(Integer.valueOf(parent));
        goodsType.setParentName(parentType.getName());

        goodsTypeService.modifyById(goodsType);

        return "/admin/goodstypeservlet?method=showGoodsType";

    }

    public String deleteGoodsType(HttpServletRequest request, HttpServletResponse response) {
        String id = request.getParameter("id");
        if (StringUtils.isEmpty(id)) {
            request.setAttribute("msg", "未找到对应类型");
            return "/admin/goodstypeservlet?method=showGoodsType";
        }
        GoodsTypeService goodsTypeService = new GoodsTypeServiceImpl();
        GoodsService goodsService = new GoodsServiceIMpl();
        GoodsType goodsType = goodsTypeService.findTypeById(Integer.valueOf(id));
        List<Goods> goodsList = goodsService.findByTypeId(goodsType.getId());

        if (goodsList != null && goodsList.size() > 0) {
            request.setAttribute("msg", "该类型下还有对应的商品，请先删除商品");
            return "/admin/goodstypeservlet?method=showGoodsType";
        } else {
            goodsTypeService.deleteByID(Integer.valueOf(id));
            return "/admin/goodstypeservlet?method=showGoodsType";
        }
    }

    public String addGoodsType(HttpServletRequest request, HttpServletResponse response) throws InvocationTargetException, IllegalAccessException, IOException {
        response.setContentType("text/html;charset=utf-8");

        User admin = (User) request.getSession().getAttribute("admin");
        if (admin == null) {
            return "redirect:/admin/login.jsp";
        }

        try {
            GoodsType goodsType = new GoodsType();
            BeanUtils.populate(goodsType, request.getParameterMap());
            if (StringUtils.isEmpty(goodsType.getName())) {
                response.getWriter().write("<script type='text/javascript'>alert('种类名称不能为空');window.location.href='addGoodsType.jsp'</script>");
                return null;
            }
            GoodsTypeService goodsTypeService = new GoodsTypeServiceImpl();
            goodsTypeService.add(goodsType);
            return showGoodsType(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            response.getWriter().write("<script type='text/javascript'>alert('添加失败');window.location.href='addGoodsType.jsp'</script>");
            return null;
        }
    }

    public String showAllGoodsType(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json;charset=utf-8");
        GoodsTypeService goodsTypeService = new GoodsTypeServiceImpl();
        List<GoodsType> goodsTypes = goodsTypeService.findGoodsTypes();
        response.getWriter().write(JSON.toJSONString(goodsTypes));
        return null;
    }

}
