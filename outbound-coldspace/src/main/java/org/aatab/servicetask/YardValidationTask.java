package org.aatab.servicetask;

import jakarta.enterprise.context.ApplicationScoped;
import org.aatab.model.Order;
import org.aatab.model.ApiResponse;
import org.jboss.logging.Logger;
import jakarta.inject.Inject;
import org.aatab.service.DatabaseService;

@ApplicationScoped
public class YardValidationTask {
    private static final Logger LOG = Logger.getLogger(YardValidationTask.class);

    @Inject
    DatabaseService databaseService;

    @Inject
    GenericTask generictask;


    public ApiResponse<Order> processOrderValidation(Order order) {
        LOG.info("Processing order validation, order object: " + (order != null ? "present" : "null"));

        if (order == null) {
            LOG.error("Received null order object");
            ApiResponse<Order> response = new ApiResponse<>();
            response.setCode(400);
            response.setStatus("ERROR");
            response.setExternalCode(1);
            response.setExternalDesc("Invalid request: Order data is missing");
            return response;
        }

        LOG.info("Processing order validation for order ID: " + order.getOrderId());

        try {
            // Insert validation records into database
            boolean insertResult = databaseService.updateOrderValidation(order.getOrderId(),"YARD_AVAILABILITY","FAILED","Yard zone/schedule is not available");

            if (insertResult) {
                LOG.info("Successfully inserted validation records for order: " + order.getOrderId());
                return GenericTask.createSuccessResponse(order);
            } else {
                LOG.error("Failed to insert validation records for order: " + order.getOrderId());
                ApiResponse<Order> response = new ApiResponse<>();
                response.setCode(500);
                response.setStatus("ERROR");
                response.setExternalCode(1);
                response.setExternalDesc("Failed to process validation records");
                return response;
            }
        } catch (Exception e) {
            LOG.error("Error processing order validation", e);
            ApiResponse<Order> response = new ApiResponse<>();
            response.setCode(500);
            response.setStatus("ERROR");
            response.setExternalCode(1);
            response.setExternalDesc("Internal server error: " + e.getMessage());
            return response;
        }
    }
}
