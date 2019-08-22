package com.shoppingcart.service.impl;

import java.util.HashMap;
import java.util.Map;
import javax.annotation.PostConstruct;
import org.apache.log4j.Level;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import com.shoppingcart.logger.ILoggerUtil;
import com.shoppingcart.model.ChargeRequest;
import com.shoppingcart.service.PaymentsService;
import com.stripe.Stripe;
import com.stripe.exception.APIConnectionException;
import com.stripe.exception.APIException;
import com.stripe.exception.AuthenticationException;
import com.stripe.exception.CardException;
import com.stripe.exception.InvalidRequestException;
import com.stripe.model.Charge;

@Service
public class PaymentsServiceImpl implements PaymentsService {

    @Autowired
    ILoggerUtil loggerUtil;

    @Value("${STRIPE_SECRET_KEY}")
    String secretKey;

    @PostConstruct
    public void init() {
        Stripe.apiKey = secretKey;
    }

    public Charge charge(ChargeRequest chargeRequest) throws AuthenticationException, InvalidRequestException,
            APIConnectionException, CardException, APIException {
        loggerUtil.log(Level.INFO, "PaymentsServiceImpl", "charge", "start", chargeRequest);
        Map<String, Object> chargeParams = new HashMap<>();
        chargeParams.put("amount", chargeRequest.getAmount());
        chargeParams.put("currency", chargeRequest.getCurrency());
        chargeParams.put("description", chargeRequest.getDescription());
        chargeParams.put("source", chargeRequest.getStripeToken());
        loggerUtil.log(Level.INFO, "PaymentsServiceImpl", "charge", "success");
        return Charge.create(chargeParams);
    }
}
