package org.aatab.servicetask;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.enterprise.context.ApplicationScoped;
import org.aatab.model.outbound.Order;
import org.aatab.model.ApiResponse;
import org.aatab.servicetask.GenericTask;
import org.jboss.logging.Logger;
import jakarta.inject.Inject;
import org.aatab.service.DatabaseService;
import jakarta.ws.rs.core.Response;

@ApplicationScoped
public class QuotationValidationTask {
    private static final Logger LOG = Logger.getLogger(QuotationValidationTask.class);

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
            boolean insertResult = databaseService.updateOrderValidation(order.getOrderId(),"QUOTATION","FAILED","Order not match with any quotation");

            if (insertResult) {
                LOG.info("Successfully inserted Quotation records for order: " + order.getOrderId());
                return generictask.createSuccessResponse(order);
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
