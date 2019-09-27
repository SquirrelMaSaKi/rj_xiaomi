package com.xiaomi.web.servlet;

import com.xiaomi.domain.Order;
import com.xiaomi.service.OrderService;
import com.xiaomi.service.impl.OrderServiceImpl;
import com.xiaomi.utils.PaymentUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ResourceBundle;

/**
 * 从易宝支付回调，不需要继承BaseServlet
 */
@WebServlet(name = "CallbackServlet", value = "/callback")
public class CallbackServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //接收回调结果
        String p1_MerId = request.getParameter("p1_MerId");
        String r0_Cmd = request.getParameter("r0_Cmd");
        String r1_Code = request.getParameter("r1_Code");
        String r2_TrxId = request.getParameter("r2_TrxId");
        String r3_Amt = request.getParameter("r3_Amt");
        String r4_Cur = request.getParameter("r4_Cur");
        String r5_Pid = request.getParameter("r5_Pid");
        String r6_Order = request.getParameter("r6_Order");
        String r7_Uid = request.getParameter("r7_Uid");
        String r8_MP = request.getParameter("r8_MP");
        String r9_BType = request.getParameter("r9_BType");
        String rb_BankId = request.getParameter("rb_BankId");
        String ro_BankOrderId = request.getParameter("ro_BankOrderId");
        String rp_PayDate = request.getParameter("rp_PayDate");
        String rq_CardNo = request.getParameter("rq_CardNo");
        String ru_Trxtime = request.getParameter("ru_Trxtime");
        // 身份校验 --- 判断是不是支付公司通知你
        String hmac = request.getParameter("hmac");
        String keyValue = ResourceBundle.getBundle("merchantInfo").getString(
                "keyValue");

        // 自己对上面数据进行加密 --- 比较支付公司发过来hamc
        boolean isValid = PaymentUtil.verifyCallback(hmac, p1_MerId, r0_Cmd,
                r1_Code, r2_TrxId, r3_Amt, r4_Cur, r5_Pid, r6_Order, r7_Uid,
                r8_MP, r9_BType, keyValue);
        if (isValid) {
            // 响应数据有效
            if (r9_BType.equals("1")) {
                // 浏览器重定向
                System.out.println("111");
                request.setAttribute("msg", "您的订单号为:"+r6_Order+",金额为:"+r3_Amt+"已经支付成功,等待发货~~");

            } else if (r9_BType.equals("2")) {
                // 服务器点对点 --- 支付公司通知你
                System.out.println("付款成功！222");
                // 修改订单状态 为已付款
                // 回复支付公司
                response.getWriter().print("success");
            }

            //修改订单状态，支付成功
            OrderService orderService = new OrderServiceImpl();
            orderService.updateStatus(r6_Order, "2"); //表示支付成功
            request.getRequestDispatcher("/message.jsp").forward(request, response);

        } else {
            // 数据无效
            System.out.println("数据被篡改！");
            request.setAttribute("msg", "订单支付失败");
            request.getRequestDispatcher("/message.jsp").forward(request, response);
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }
}
