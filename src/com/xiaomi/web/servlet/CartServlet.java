package com.xiaomi.web.servlet;

import com.xiaomi.domain.Cart;
import com.xiaomi.domain.Goods;
import com.xiaomi.domain.User;
import com.xiaomi.service.CartService;
import com.xiaomi.service.GoodsService;
import com.xiaomi.service.impl.CartServiceImpl;
import com.xiaomi.service.impl.GoodsServiceIMpl;
import com.xiaomi.utils.StringUtils;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.util.List;

@WebServlet(name = "CartServlet", value = "/cartservlet")
public class CartServlet extends BaseServlet {
    public String addCart(HttpServletRequest request, HttpServletResponse response) {
        //判断有没有登录
        User user = (User) request.getSession().getAttribute("user");
        if (user == null) {
            return "redirect:/login.jsp";
        }
        String goodsId = request.getParameter("goodsId");
        String number = request.getParameter("number");
        if (StringUtils.isEmpty(goodsId)) {
            return "redirect:/index.jsp";
        }
        //添加购物车（数据库存）
        //查询是否有商品，有则修改，否则添加
        //根据用户id和商品id
        CartService cartService = new CartServiceImpl();
        Cart cart = cartService.findByUidAndPid(user.getId(), Integer.valueOf(goodsId));
        GoodsService goodsService = new GoodsServiceIMpl();
        Goods goods = goodsService.findByID(Integer.valueOf(goodsId));
        int num = Integer.valueOf(number);
        try {
            if (cart == null) {
                //添加
                cart = new Cart(user.getId(), Integer.valueOf(goodsId), num, goods.getPrice().multiply(new BigDecimal(num)));
                cartService.add(cart);
            }else {
                //更新
                cart.setNum(cart.getNum() + num);
                cart.setMoney(goods.getPrice().multiply(new BigDecimal(cart.getNum())));
                cartService.update(cart);
            }
            return "redirect:/cartSuccess.jsp";

        } catch (NumberFormatException e) {
            e.printStackTrace();
            return "redirect:/index.jsp";
        }
    }
    public String getCart(HttpServletRequest request, HttpServletResponse response) {
        User user = (User) request.getSession().getAttribute("user");
        if (user == null) {
            return "redirect:/login.jsp";
        }
        CartService cartService = new CartServiceImpl();
        List<Cart> carts =  cartService.findByUid(user.getId());
        request.setAttribute("carts", carts);
        return "/cart.jsp";
    }
    public String addCartAjax(HttpServletRequest request, HttpServletResponse response) {
        //cartservlet?method=addCartAjax&goodsId="+pid+"&number="+num
        User user = (User) request.getSession().getAttribute("user");
        if (user == null) {
            return "redirect:/login.jsp";
        }

        String goodsId = request.getParameter("goodsId");
        String number = request.getParameter("number"); //商品数量

        CartService cartService = new CartServiceImpl();
        Cart cart = cartService.findByUidAndPid(user.getId(), Integer.valueOf(goodsId));

        //获取商品值
        if (cart != null) {
            if (number.equals("0")) {
                //删除操作，0表示没有该商品，必须删除
                cartService.delete(user.getId(),Integer.valueOf(goodsId));
            } else {
                //更新
                int num = Integer.valueOf(number); //1或者-1，即数量的增删
                //更新数量
                //原来的总价除以原来的数量
                BigDecimal price = cart.getMoney().divide(new BigDecimal(cart.getNum()));
                //更新数量
                cart.setNum(cart.getNum() + num);
                //更新金额
                cart.setMoney(price.multiply(new BigDecimal(cart.getNum())));
                cartService.update(cart);
            }
        }
        return null;
    }
    public String clearCartAjax(HttpServletRequest request, HttpServletResponse response) {
        User user = (User) request.getSession().getAttribute("user");
        if (user == null) {
            return "redirect:/login.jsp";
        }

        CartService cartService = new CartServiceImpl();
        cartService.deleteByUid(user.getId());

        return null;
    }
}
