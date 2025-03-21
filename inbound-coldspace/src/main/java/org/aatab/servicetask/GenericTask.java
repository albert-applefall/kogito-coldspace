package org.aatab.servicetask;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.aatab.model.ApiResponse;
import org.aatab.service.DatabaseService;
import org.jboss.logging.Logger;
import jakarta.ws.rs.core.Response;

@ApplicationScoped
public class GenericTask {
    private static final Logger LOG = Logger.getLogger(GenericTask.class);

    public static <T> ApiResponse<T> createValidationNotSupportedResponse(String type) {
        ApiResponse<T> response = new ApiResponse<>();
        response.setCode(400);
        response.setStatus("OK");
        response.setExternalCode(0);
        response.setExternalDesc("Error: Validation '" + type + "' is not supported");
         response.setContent(null);
         return response;
    }

          // Method to create dummy response with specific content
          public static <T> ApiResponse<T> createSuccessResponse(T content) {
            ApiResponse<T> response = new ApiResponse<>();
            response.setCode(200);
            response.setStatus("OK");
            response.setExternalCode(0);
            response.setExternalDesc("Success");
            response.setContent(null);
            //response.setContent(content);
  
            return response;
        }
}
