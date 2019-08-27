package com.zw.stm.controller;

import com.zw.stm.pojo.TbItem;
import com.zw.stm.pojo.TbUser;
import com.zw.stm.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
public class OrderController {
    @Autowired
    private CartService cartService;
    @Autowired
    private HttpSession session;

    /**
     * url:/order/order-cart.html
     * 参数：没有参数，但需要用户的id  从cookie中获取token 调用SSO的服务获取用户的ID
     * 返回值：逻辑视图 （订单的确认页面）
     */
    @RequestMapping("/order/order-cart")
    public String showOrder(HttpServletRequest request){
        System.out.println(session.getAttribute("userinfo"));
//		//1.从cookie中获取用户的token
//		String token = CookieUtils.getCookieValue(request, TT_TOKEN_KEY);
//		if(StringUtils.isNotBlank(token)){
//			//2.调用SSO的服务获取用户的信息
//			TaotaoResult result = loginservice.getUserByToken(token);
//			if(result.getStatus()==200){
//				//3.必须是用户登录了才展示
//				//4.展示用户的送货地址   根据用户的ID查询该用户的配送地址。静态数据
//				//5.展示支付方式    从数据库中获取支付的方式。静态数据
//				//6.调用购物车服务从redis数据库中获取购物车的商品的列表
//				TbUser user = (TbUser)result.getData();
//				List<TbItem> cartList = cartservice.getCartList(user.getId());
//				//7.将列表 展示到页面中(传递到页面中通过model)
//				request.setAttribute("cartList", cartList);
//			}
//		}

        TbUser user =(TbUser) session.getAttribute("userinfo");
        List<TbItem> cartList = cartService.getCartList(user.getId());

        //totalPrice
        int totalPrice=0;
        for (TbItem item: cartList){
            totalPrice+=item.getNum()*item.getPrice();
        }
        request.setAttribute("totalPrice", totalPrice);
        //7.将列表 展示到页面中(传递到页面中通过model)
        request.setAttribute("cartList", cartList);
        return "order-cart";
    }
}
