package com.springfullstackcloudapp.backend.service;

import com.stripe.Stripe;
import com.stripe.exception.*;
import com.stripe.model.Customer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import com.stripe.model.Token;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * Created by ranji on 6/19/2017.
 */
@Service
public class StripeService {
    
    /* The Application Logger*/
    private static final Logger LOG = LoggerFactory.getLogger(StripeService.class);

    @Autowired
    private String stripeKey;

    /**
     * Creates a stripe customer and returns the customer id
     * @param tokenParams Credit card details to obtain a token. These will never be stored in DB
     * @param customerParams Parameters which identify customers
     * @return Stripe Customer id used to perform billing operations at a later stage
     * @throws com.springfullstackcloudapp.exceptions.StripeException If an error occurred while interacting with stripe
     */
    public String createCustomer(Map<String, Object> tokenParams, Map<String, Object> customerParams){
        Stripe.apiKey = stripeKey;

        String stripeCustomerId = null;
        try{
            Token token = Token.create(tokenParams);
            customerParams.put("source", token.getId());
            Customer customer = Customer.create(customerParams);
            stripeCustomerId = customer.getId();
        }catch(AuthenticationException e){
            LOG.error("An authentication error occurred while creating a stripe customer", e);
            throw new com.springfullstackcloudapp.exceptions.StripeException();
        }catch(InvalidRequestException e){
            LOG.error("An invalid request exception occurred while creating a stripe customer", e);
            throw new com.springfullstackcloudapp.exceptions.StripeException();
        }catch(APIConnectionException e){
            LOG.error("An API Connection exception occurred while creating a stripe customer", e);
            throw new com.springfullstackcloudapp.exceptions.StripeException();
        }catch(CardException e){
            LOG.error("A credit card exception occurred while creating a stripe customer", e);
            throw new com.springfullstackcloudapp.exceptions.StripeException();
        }catch(APIException e){
            LOG.error("An API exception occurred while creating a stripe customer", e);
            throw new com.springfullstackcloudapp.exceptions.StripeException();
        }
        return stripeCustomerId;
    }
}
