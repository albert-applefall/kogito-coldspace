package org.aatab.tools;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Map;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerResponseContext;
import jakarta.ws.rs.container.ContainerResponseFilter;
import jakarta.ws.rs.ext.Provider;
import jakarta.ws.rs.Priorities;
import jakarta.annotation.Priority;
import jakarta.ws.rs.core.MediaType;
import org.aatab.model.ApiResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Provider
@ApplicationScoped
@Priority(Priorities.ENTITY_CODER + 100)
public class KogitoResponseFilter implements ContainerResponseFilter {
    
    private static final Logger logger = LoggerFactory.getLogger(KogitoResponseFilter.class);

    @Override
    public void filter(ContainerRequestContext requestContext, 
                      ContainerResponseContext responseContext) throws IOException {
        
        String path = requestContext.getUriInfo().getPath();
        logger.debug("Response filter triggered for: {}", path);
        
        if (responseContext.getMediaType() != null 
            && responseContext.getMediaType().isCompatible(MediaType.APPLICATION_JSON_TYPE)) {
            
            Object entity = responseContext.getEntity();
            
            if (entity == null) {
                return;
            }
            
            logger.debug("Entity class: {}", entity.getClass().getName());
            
            // Check if this is likely a Kogito model output class
            if (entity.getClass().getName().contains("ModelOutput")) {
                try {
                    // Look for an ApiResponse field in the class
                    Field apiResponseField = findApiResponseField(entity);
                    
                    if (apiResponseField != null) {
                        apiResponseField.setAccessible(true);
                        Object apiResponse = apiResponseField.get(entity);
                        
                        if (apiResponse != null) {
                            logger.debug("Found and extracted field: {} of type: {}", 
                                       apiResponseField.getName(), apiResponse.getClass().getName());
                            
                            // Set the status code from apiResponse if possible
                            setStatusCodeFromApiResponse(responseContext, apiResponse);
                            
                            // Replace the entity with just the ApiResponse
                            responseContext.setEntity(apiResponse);
                            logger.debug("Response entity replaced successfully");
                        }
                    } else {
                        logger.debug("No ApiResponse field found in entity of type: {}", entity.getClass().getName());
                    }
                } catch (Exception e) {
                    logger.error("Error processing response entity", e);
                }
            } else if (entity instanceof Map) {
                // Fallback to Map approach
                @SuppressWarnings("unchecked")
                Map<String, Object> responseMap = (Map<String, Object>) entity;
                
                // Look for any response field in the map that might be an ApiResponse
                for (Map.Entry<String, Object> entry : responseMap.entrySet()) {
                    if (entry.getValue() != null && 
                        (entry.getValue().getClass().equals(ApiResponse.class) || 
                         entry.getKey().toLowerCase().contains("response"))) {
                        
                        logger.debug("Found response field in map: {}", entry.getKey());
                        
                        // Set the status code from the response value if possible
                        setStatusCodeFromApiResponse(responseContext, entry.getValue());
                        
                        responseContext.setEntity(entry.getValue());
                        logger.debug("Response entity replaced with field from map");
                        break;
                    }
                }
                
                // Check if the map itself is an ApiResponse-like structure
                if (responseMap.containsKey("code") && responseMap.get("code") instanceof Number) {
                    int statusCode = ((Number) responseMap.get("code")).intValue();
                    responseContext.setStatus(statusCode);
                    logger.debug("Set HTTP status code to {} from response map", statusCode);
                }
            } else if (entity instanceof ApiResponse) {
                // If the entity is already an ApiResponse
                setStatusCodeFromApiResponse(responseContext, entity);
            }
        }
    }
    
    private void setStatusCodeFromApiResponse(ContainerResponseContext responseContext, Object apiResponse) {
        try {
            // Try to get the code field through reflection
            Method getCodeMethod = findGetterMethod(apiResponse.getClass(), "getCode", "code");
            if (getCodeMethod != null) {
                Object codeValue = getCodeMethod.invoke(apiResponse);
                if (codeValue instanceof Number) {
                    int statusCode = ((Number) codeValue).intValue();
                    responseContext.setStatus(statusCode);
                    logger.debug("Set HTTP status code to {}", statusCode);
                }
            } else {
                // Fallback to direct field access
                Field codeField = findField(apiResponse.getClass(), "code");
                if (codeField != null) {
                    codeField.setAccessible(true);
                    Object codeValue = codeField.get(apiResponse);
                    if (codeValue instanceof Number) {
                        int statusCode = ((Number) codeValue).intValue();
                        responseContext.setStatus(statusCode);
                        logger.debug("Set HTTP status code to {}", statusCode);
                    }
                }
            }
        } catch (Exception e) {
            logger.error("Error setting status code from ApiResponse", e);
        }
    }
    
    private Method findGetterMethod(Class<?> clazz, String... methodNames) {
        for (String methodName : methodNames) {
            try {
                return clazz.getMethod(methodName);
            } catch (NoSuchMethodException e) {
                // Method not found, try next one
            }
        }
        return null;
    }
    
    private Field findField(Class<?> clazz, String fieldName) {
        try {
            return clazz.getDeclaredField(fieldName);
        } catch (NoSuchFieldException e) {
            return null;
        }
    }
    
    private Field findApiResponseField(Object entity) {
        // Try to find a field that could be an ApiResponse
        for (Field field : entity.getClass().getDeclaredFields()) {
            String fieldName = field.getName().toLowerCase();
            Class<?> fieldType = field.getType();
            
            // Check if field is ApiResponse or has a name suggesting it's a response
            if (fieldType.equals(ApiResponse.class) || 
                fieldName.contains("apiresponse") || 
                fieldName.contains("response") || 
                fieldName.contains("result")) {
                
                return field;
            }
        }
        return null;
    }
}