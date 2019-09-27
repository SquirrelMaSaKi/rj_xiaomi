package com.xiaomi.web.servlet;

import com.alibaba.fastjson.JSON;
import com.xiaomi.service.OrderService;
import com.xiaomi.service.impl.OrderServiceImpl;
import com.xiaomi.utils.WeiXinResult;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "WXCallbackServlet", value = "/wxcallback")
public class WXCallbackServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //接收微信回调的数据,将json数据改为javaBean
        String result = request.getParameter("result");
        WeiXinResult wxresult = JSON.parseObject(result, WeiXinResult.class);

        //获取对象属性进行判断
        /**
         * "out_trade_no":"1221ea762d54496e83a33c9dab72f320",//订单编号
         * "result_code":"SUCCESS",//支付结果
         * "total_fee":"1",//总支付价格
         * "type":0//类型 0为页面重定向,1为点对点服务器通信,因为是 localhost 地址,无法使用1
         */
        String result_code = wxresult.getResult().getResult_code();
        if (result_code.equals("SUCCESS")) {
            //支付成功
            //判断重定向
            if (wxresult.getType() == 0) {
                request.setAttribute("msg", "您的订单号为:"+wxresult.getResult().getOut_trade_no()+",金额为:"+wxresult.getResult().getCash_fee()+"已经支付成功,等待发货~~");
            } else {
                //socket..
                response.getWriter().write("success");
            }
            OrderService orderService = new OrderServiceImpl();
            orderService.updateStatus(wxresult.getResult().getOut_trade_no(), "2");
        } else {
            request.setAttribute("msg", "您的订单号为:"+wxresult.getResult().getOut_trade_no()+"支付失败");
        }
        request.getRequestDispatcher("/message.jsp").forward(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }
}
