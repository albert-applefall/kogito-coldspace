package org.aatab.servicetask;

  import jakarta.enterprise.context.ApplicationScoped;
  import jakarta.inject.Inject;

  import org.aatab.model.Order;
  import org.aatab.model.ApiResponse;
  import org.aatab.service.DatabaseService;
  import org.jboss.logging.Logger;

@ApplicationScoped
  public class BusinessValidationTask {

      private static final Logger LOG = Logger.getLogger(BusinessValidationTask.class);

      @Inject
      DatabaseService databaseService;

      @Inject
    GenericTask generictask;
       

      // Update this method to handle null order
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
              boolean insertResult = databaseService.insertOutboundOrderValidation(order);

              if (insertResult) {
                  LOG.info("Successfully inserted validation records for order: " + order.getOrderId());
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