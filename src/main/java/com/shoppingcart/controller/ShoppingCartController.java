package com.shoppingcart.controller;

import com.shoppingcart.exception.NotEnoughProductsInStockException;
import com.shoppingcart.model.ChargeRequest;
import com.shoppingcart.model.ChargeRequest.Currency;
import com.shoppingcart.service.PaymentsService;
import com.shoppingcart.service.ProductService;
import com.shoppingcart.service.ShoppingCartService;
import com.stripe.exception.APIConnectionException;
import com.stripe.exception.APIException;
import com.stripe.exception.AuthenticationException;
import com.stripe.exception.CardException;
import com.stripe.exception.InvalidRequestException;
import com.stripe.exception.StripeException;
import com.stripe.model.Charge;
import java.math.BigDecimal;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class ShoppingCartController {

    @Autowired
    ShoppingCartService shoppingCartService;

    @Autowired
    ProductService productService;

    @Autowired
    PaymentsService paymentsService;

    private final Logger logger = Logger.getLogger(ShoppingCartController.class);

    @GetMapping("/shoppingCart")
    public ModelAndView shoppingCart() {
        ModelAndView modelAndView = new ModelAndView("/shoppingCart");
        modelAndView.addObject("products", shoppingCartService.getProductsInCart());
        modelAndView.addObject("total", shoppingCartService.getTotal().toString());
        return modelAndView;
    }

    @GetMapping("/shoppingCart/addProduct/{productId}")
    public ModelAndView addProductToCart(@PathVariable("productId") Long productId) {
        productService.findById(productId).ifPresent(shoppingCartService::add);
        return shoppingCart();
    }

    @GetMapping("/shoppingCart/removeProduct/{productId}")
    public ModelAndView removeProductFromCart(@PathVariable("productId") Long productId) {
        productService.findById(productId).ifPresent(shoppingCartService::remove);
        return shoppingCart();
    }

    @PostMapping("/shoppingCart/checkout")
    public ModelAndView checkout() {
        ModelAndView modelAndView = null;
        try {
            BigDecimal amount = shoppingCartService.checkout();
            ChargeRequest chargeRequest = new ChargeRequest();
            chargeRequest.setAmount(amount.doubleValue());
            chargeRequest.setCurrency(Currency.EUR);
            Charge charge = paymentsService.charge(chargeRequest);
            modelAndView = new ModelAndView("/result");
            modelAndView.addObject("id", charge.getId());
            modelAndView.addObject("status", charge.getStatus());
            modelAndView.addObject("chargeId", charge.getId());
            modelAndView.addObject("balance_transaction", charge.getBalanceTransaction());
        } catch (NotEnoughProductsInStockException e) {
            return shoppingCart().addObject("outOfStockMessage", e.getMessage());
        } catch (AuthenticationException | InvalidRequestException | APIConnectionException | CardException
                | APIException e) {
        }
        return modelAndView;
    }

    @ExceptionHandler(StripeException.class)
    public String handleError(Model model, StripeException ex) {
        model.addAttribute("error", ex.getMessage());
        return "result";
    }
}
