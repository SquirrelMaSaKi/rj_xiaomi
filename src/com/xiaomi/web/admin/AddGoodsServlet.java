package com.xiaomi.web.admin;

import com.xiaomi.domain.Goods;
import com.xiaomi.domain.User;
import com.xiaomi.service.GoodsService;
import com.xiaomi.service.impl.GoodsServiceIMpl;
import com.xiaomi.utils.StringUtils;
import com.xiaomi.utils.UploadUtils;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.beanutils.Converter;
import org.apache.commons.beanutils.converters.BigDecimalConverter;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;

@WebServlet(name = "AddGoodsServlet", value = "/admin/addgoods")
@MultipartConfig(maxFileSize = 1024*1024*5, maxRequestSize = 1024*1024*100)
public class AddGoodsServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        User admin = (User) request.getSession().getAttribute("admin");
        ServletContext application = this.getServletConfig().getServletContext();
        String realPath = application.getRealPath("WEB-INF/upload");
        File file = new File(realPath);
        if (!file.exists()) {
            file.mkdir();
        }

        if (admin == null) {
            response.sendRedirect(request.getContextPath() + "/admin/login.jsp");
            return;
        }

        ConvertUtils.register(new Converter() {
            @Override
            public <T> T convert(Class<T> aClass, Object o) {
                if (o == null) {
                    return null;
                }

                String str = (String)o;
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                try {
                    Date date = format.parse(str);
                    return (T)date;
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                return null;
            }
        }, Date.class);

        ConvertUtils.register(new BigDecimalConverter(null), BigDecimal.class);

        //处理数据
        Collection<Part> parts = request.getParts();
        if (parts != null && parts.size() > 0) {
            Goods goods = new Goods();
            try {
                BeanUtils.populate(goods, request.getParameterMap());
            } catch (Exception e) {
                e.printStackTrace();
            }
            for (Part part : parts) {
                String fileName = part.getSubmittedFileName();
                if (!StringUtils.isEmpty(fileName)) {//文件
                    String header = part.getHeader("content-disposition");
                    fileName = header.substring(header.indexOf("filename=")+10, header.length()-1).substring(header.substring(header.indexOf("filename=")+10, header.length()-1).lastIndexOf("\\") + 1).replace("_", "");
                    String finalPath = UploadUtils.createNewPath(realPath, fileName) + "/" + UploadUtils.createNewFileName(fileName);
                    goods.setPicture(finalPath);
                    part.write(finalPath);
                    part.delete();
                }
            }
            GoodsService goodsService = new GoodsServiceIMpl();
            goodsService.add(goods);
            response.sendRedirect(request.getContextPath() + "/admin/goodsservlet?method=getGoodsList");
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }
}
