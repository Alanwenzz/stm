package com.zw.stm.controller;

import com.zw.stm.common.pojo.TaotaoResult;
import com.zw.stm.common.util.CookieUtils;
import com.zw.stm.common.util.JsonUtils;
import com.zw.stm.pojo.TbItem;
import com.zw.stm.pojo.TbUser;
import com.zw.stm.service.CartService;
import com.zw.stm.service.ItemService;
import com.zw.stm.service.UserLoginService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

@Controller
public class CartController {
    private final UserLoginService loginService;
    private final ItemService itemService;
    private final CartService cartService;

    @Autowired
    public CartController(UserLoginService loginService, ItemService itemService, CartService cartService) {
        this.loginService = loginService;
        this.itemService = itemService;
        this.cartService = cartService;
    }

    @RequestMapping("/cart/cart")
    public String getCartList(HttpServletRequest request){
        // 3.判断用户是否登录
        // 从cookie中获取用户的token信息
        String token = CookieUtils.getCookieValue(request, "TT_TOKEN");
        // 调用SSO的服务查询用户的信息
        TaotaoResult result = loginService.getUserByToken(token);

        System.out.println(result.getData());
        List<TbItem> cartList ;
        // 获取商品的数据
        if (result.getStatus() == 200) {
            // 4.如果已登录，调用service的方法
            TbUser user = (TbUser) result.getData();
            cartList= cartService.getCartList(user.getId());
            System.out.println(cartList.size());
            request.setAttribute("cartList", cartList);
        } else {
            // 5.如果没有登录 调用cookie的方法 获取商品的列表
            cartList = getCookieCartList(request);
            // 将数据传递到页面中
            request.setAttribute("cartList", cartList);
        }
        int totalPrice=0;
        for (TbItem item: cartList){
            totalPrice+=item.getNum()*item.getPrice();
        }
        request.setAttribute("totalPrice", totalPrice);
        return "cart";
    }

    // 获取购物车的列表
    private List<TbItem> getCookieCartList(HttpServletRequest request) {
        // 从cookie中获取商品的列表
        String jsonstr = CookieUtils.getCookieValue(request, "TT_CART", true);// 商品的列表的JSON
        // 讲商品的列表的JSON转成 对象
        if (StringUtils.isNotBlank(jsonstr)) {
            return JsonUtils.jsonToList(jsonstr, TbItem.class);
        }
        return new ArrayList<>();

    }

    @RequestMapping("/cart/add/{itemId}")
    public String addItemCart(@PathVariable Long itemId, Integer num, HttpServletRequest request,
                              HttpServletResponse response) {
        // 1.引入服务
        // 2.注入服务

        // 3.判断用户是否登录
        // 从cookie中获取用户的token信息
        String token = CookieUtils.getCookieValue(request, "TT_TOKEN");
        // 调用SSO的服务查询用户的信息
        TaotaoResult result = loginService.getUserByToken(token);

        // 获取商品的数据
        TbItem tbItem = itemService.getItemById(itemId);
        if (result.getStatus() == 200) {
            // 4.如果已登录，调用service的方法
            TbUser user = (TbUser) result.getData();
            cartService.addItemCart(tbItem, num, user.getId());
        } else {
            // 5.如果没有登录 调用设置到cookie的方法
            // 先根据cookie获取购物车的列表
            List<TbItem> cartList = getCookieCartList(request);
            boolean flag = false;
            // 判断如果购物车中有包含要添加的商品 商品数量相加
            for (TbItem tbItem2 : cartList) {
                if (tbItem2.getId() == itemId.longValue()) {
                    // 找到列表中的商品 更新数量
                    tbItem2.setNum(tbItem2.getNum() + num);
                    flag = true;
                    break;
                }
            }
            if (flag) {
                // 如果找到对应的商品，更新数量后，还需要设置回cookie中
                CookieUtils.setCookie(request, response, "TT_CART", JsonUtils.objectToJson(cartList), 7 * 24 * 3600,
                        true);
            } else {
                // 如果没有就直接添加到购物车
                // 调用商品服务
                // 设置数量
                tbItem.setNum(num);
                // 设置图片为一张
                if (tbItem.getImage() != null) {
                    tbItem.setImage(tbItem.getImage().split(",")[0]);
                }
                // 添加商品到购物车中
                cartList.add(tbItem);
                // 设置到cookie中
                CookieUtils.setCookie(request, response, "TT_CART", JsonUtils.objectToJson(cartList), 7 * 24 * 3600,
                        true);
            }
        }
        return "cartSuccess";
    }

    @RequestMapping("/cart/update/num/{itemId}/{num}")
    @ResponseBody
    public TaotaoResult updateItemCartByItemId(@PathVariable Long itemId, @PathVariable Integer num,
                                               HttpServletRequest request, HttpServletResponse response) {
        // 1.引入服务
        // 2.注入服务

        // 3.判断用户是否登录
        // 从cookie中获取用户的token信息
        String token = CookieUtils.getCookieValue(request, "TT_TOKEN");
        // 调用SSO的服务查询用户的信息
        TaotaoResult result = loginService.getUserByToken(token);

        // 获取商品的数据
        if (result.getStatus() == 200) {
            // 4.如果已登录，调用service的方法
            TbUser user = (TbUser) result.getData();
            // 更新商品的数量
            cartService.updateItemCartByItemId(user.getId(), itemId, num);
        } else {
            // 5.如果没有登录 调用cookie的方法 更新cookie中的商品的数量的方法
            updateCookieItemCart(itemId, num, request, response);
        }
        return TaotaoResult.ok();
    }

    // 更新商品的数量
    private void updateCookieItemCart(Long itemId, Integer num, HttpServletRequest request,
                                      HttpServletResponse response) {
        // 1.获取cookie中的购物车的商品列表
        List<TbItem> cartList = getCookieCartList(request);
        // 2.判断修改的商品是否在购物车的列表中
        boolean flag = false;
        for (TbItem tbItem : cartList) {
            // 表示找到了要修改的商品
            if (tbItem.getId() == itemId.longValue()) {
                tbItem.setNum(num);
                flag = true;
                break;
            }
        }
        if (flag) {
            // 3.如果存在 更新数量
            CookieUtils.setCookie(request, response, "TT_CART", JsonUtils.objectToJson(cartList), 7 * 24 * 3600,
                    true);
        } else {
            // 4.如果不存在 不管啦。
        }
    }

}
