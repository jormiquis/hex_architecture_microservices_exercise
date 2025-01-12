package edu.uoc.epcsd.notification.domain.service;

import edu.uoc.epcsd.notification.application.kafka.ProductMessage;
import edu.uoc.epcsd.notification.application.rest.dtos.GetProductResponse;
import edu.uoc.epcsd.notification.application.rest.dtos.GetUserResponse;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.util.Arrays;

@Log4j2
@Service
public class NotificationServiceImpl implements NotificationService {

    @Value("${userService.getUsersToAlert.url}")
    private String userServiceUrl;

    @Value("${productService.getProductDetails.url}")
    private String productServiceUrl;

    @Override
    public void notifyProductAvailable(ProductMessage productMessage) {

         // check that the product exists
        new RestTemplate().getForEntity(productServiceUrl, GetProductResponse.class, productMessage.getProductId()).getBody();

        GetUserResponse[] users = new RestTemplate().getForEntity(userServiceUrl, GetUserResponse[].class, productMessage.getProductId(), LocalDate.now()).getBody();

         if (users != null) {
            Arrays.stream(users).forEach(user -> {
                log.info("Sending an email to user " + user.getFullName());
            });
        } else {
            log.warn("No users found with alerts for product " + productMessage.getProductId() + " on date " +  LocalDate.now() );
        }

    }
}
