package org.aatab.tools;

  import java.io.ByteArrayInputStream;
  import java.io.IOException;
  import java.io.InputStream;
  import java.nio.charset.StandardCharsets;
  import java.util.Scanner;
  import jakarta.enterprise.context.ApplicationScoped;

  import jakarta.ws.rs.container.ContainerRequestContext;
  import jakarta.ws.rs.container.ContainerRequestFilter;
  import jakarta.ws.rs.core.MediaType;
  import jakarta.ws.rs.ext.Provider;
  import jakarta.ws.rs.Priorities;
  import jakarta.annotation.Priority;
  import jakarta.ws.rs.Path;

  import com.fasterxml.jackson.databind.JsonNode;
  import com.fasterxml.jackson.databind.ObjectMapper;
  import com.fasterxml.jackson.databind.node.ObjectNode;
  import org.aatab.model.ApiResponse;
  import org.jboss.logging.Logger;


  @Provider
@ApplicationScoped
@Priority(Priorities.ENTITY_CODER + 100)
public class KogitoRequestFilter implements ContainerRequestFilter {

    private static final Logger LOG = Logger.getLogger(KogitoRequestFilter.class);
    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
        // Only process JSON requests that are POST or PUT
        if (requestContext.getMediaType() == null ||
            !requestContext.getMediaType().equals(MediaType.APPLICATION_JSON_TYPE) ||
            (!requestContext.getMethod().equals("POST") && !requestContext.getMethod().equals("PUT"))) {
            return;
        }

        // Check path to make sure we're working with kogito process endpoints
        String path = requestContext.getUriInfo().getPath();
        if (!path.contains("inbound_order/business_validation") &&
            !path.contains("outbound_order/business_validation") &&
            !path.contains("transport_order/business_validation") &&
            !path.contains("orderval")) {
            return;
        }

        LOG.debug("Processing request for path: " + path);

        // Get the original input stream
        InputStream inputStream = requestContext.getEntityStream();

        // Convert the input stream to a String
        String requestBody = convertInputStreamToString(inputStream);
        LOG.debug("Original request body: " + requestBody);

        try {
            // Parse the JSON
            JsonNode jsonNode = MAPPER.readTree(requestBody);

            // Check if the JSON already has an 'order' field at the root level
            if (jsonNode.has("order")) {
                // Already wrapped, no need to transform
                LOG.debug("Request already has 'order' field, no transformation needed");

                // Reset the input stream
                requestContext.setEntityStream(new ByteArrayInputStream(
                    requestBody.getBytes(StandardCharsets.UTF_8)));
                return;
            }

            // Create a new object that wraps the original JSON in an 'order' field
            ObjectNode wrapperNode = MAPPER.createObjectNode();
            wrapperNode.set("order", jsonNode);

            String transformedJson = MAPPER.writeValueAsString(wrapperNode);
            LOG.info("Transformed request payload to include 'order' wrapper");
            LOG.debug("Transformed payload: " + transformedJson);

            // Replace the entity stream with our transformed JSON
            requestContext.setEntityStream(new ByteArrayInputStream(
                transformedJson.getBytes(StandardCharsets.UTF_8)));

        } catch (Exception e) {
            LOG.error("Error transforming request payload", e);
            // In case of error, reset the input stream to its original state
            requestContext.setEntityStream(new ByteArrayInputStream(
                requestBody.getBytes(StandardCharsets.UTF_8)));
        }
    }

    private String convertInputStreamToString(InputStream inputStream) {
        try (Scanner scanner = new Scanner(inputStream, StandardCharsets.UTF_8)) {
            if (scanner.hasNext()) {
                return scanner.useDelimiter("\\A").next();
            }
            return "{}";
        } catch (Exception e) {
            LOG.error("Error reading input stream", e);
            return "{}";
        }
    }
}