package org.aatab.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import io.agroal.api.AgroalDataSource;
import org.aatab.model.Order;
import org.jboss.logging.Logger;

@ApplicationScoped
public class DatabaseService {

    private static final Logger LOG = Logger.getLogger(DatabaseService.class);

    @Inject
    AgroalDataSource dataSource;

    /**
     * Insert order validation records into database
     * 
     * @param order The order data to process
     * @return true if insertion is successful, false otherwise
     */
    public boolean insertOrderValidation(Order order) {
      String sql = "INSERT INTO t_order_validation " +
           "( order_id, order_no, order_type, `type`, `name`, `status`, `message`, " +
           "is_mandatory, validated_at, created_at, updated_at, created_by, updated_by ) " +
           "VALUES " +
           "( ?, ?, 'INBOUND', 'QUOTATION', 'Quotation', 'IN_QUEUE', NULL, 0, NULL, ?, ?, 'kogito', 'kogito' ), " +
           "( ?, ?, 'INBOUND', 'YARD_AVAILABILITY', 'Yard Availability', 'IN_QUEUE', NULL, 0, NULL, ?, ?, 'kogito', 'kogito' ) " +
           "ON DUPLICATE KEY UPDATE " +
           "order_no = VALUES(order_no), " +
           "order_type = VALUES(order_type), " +
           "`name` = VALUES(`name`), " +
           "`status` = VALUES(`status`), " +
           "`message` = VALUES(`message`), " +
           "is_mandatory = VALUES(is_mandatory), " +
           "validated_at = VALUES(validated_at), " +
           "updated_at = VALUES(updated_at), " +
           "updated_by = VALUES(updated_by)";

        // Get current timestamp
        String currentTimestamp = ZonedDateTime.now()
                                      .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

        try (Connection conn = dataSource.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            // Set parameters for first record (QUOTATION)
            pstmt.setString(1, order.getOrderId());
            pstmt.setString(2, order.getOrderNo());
            pstmt.setString(3, currentTimestamp);
            pstmt.setString(4, currentTimestamp);

            // Set parameters for second record (YARD_AVAILABILITY)
            pstmt.setString(5, order.getOrderId());
            pstmt.setString(6, order.getOrderNo());
            pstmt.setString(7, currentTimestamp);
            pstmt.setString(8, currentTimestamp);

            // Execute the query
            int rowsAffected = pstmt.executeUpdate();
            LOG.info("Inserted " + rowsAffected + " order validation records");

            return rowsAffected > 0;

        } catch (SQLException e) {
            LOG.error("Error inserting order validation records", e);
            return false;
        }
    }

    /**
     * Insert outbound order validation records into database
     * 
     * @param order The order data to process
     * @return true if insertion is successful, false otherwise
     */
    public boolean insertOutboundOrderValidation(Order order) {
        String sql = "INSERT INTO t_order_validation " +
             "( order_id, order_no, order_type, `type`, `name`, `status`, `message`, " +
             "is_mandatory, validated_at, created_at, updated_at, created_by, updated_by ) " +
             "VALUES " +
             "( ?, ?, 'OUTBOUND', 'AT_RISK', 'Customer at Risk', 'IN_QUEUE', NULL, 0, NULL, ?, ?, 'kogito', 'kogito' ), " +
             "( ?, ?, 'OUTBOUND', 'AR_OVERDUE', 'AR Overdue', 'IN_QUEUE', NULL, 0, NULL, ?, ?, 'kogito', 'kogito' ), " +
             "( ?, ?, 'OUTBOUND', 'QUOTATION', 'Quotation', 'IN_QUEUE', NULL, 0, NULL, ?, ?, 'kogito', 'kogito' ), " +
             "( ?, ?, 'OUTBOUND', 'YARD_AVAILABILITY', 'Yard Availability', 'IN_QUEUE', NULL, 0, NULL, ?, ?, 'kogito', 'kogito' ) " +
             "ON DUPLICATE KEY UPDATE " +
             "order_no = VALUES(order_no), " +
             "order_type = VALUES(order_type), " +
             "`name` = VALUES(`name`), " +
             "`status` = VALUES(`status`), " +
             "`message` = VALUES(`message`), " +
             "is_mandatory = VALUES(is_mandatory), " +
             "validated_at = VALUES(validated_at), " +
             "updated_at = VALUES(updated_at), " +
             "updated_by = VALUES(updated_by)";

        // Get current timestamp
        String currentTimestamp = ZonedDateTime.now()
                                      .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

        try (Connection conn = dataSource.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            // Set parameters for AT_RISK record
            pstmt.setString(1, order.getOrderId());
            pstmt.setString(2, order.getOrderNo());
            pstmt.setString(3, currentTimestamp);
            pstmt.setString(4, currentTimestamp);

            // Set parameters for AR_OVERDUE record
            pstmt.setString(5, order.getOrderId());
            pstmt.setString(6, order.getOrderNo());
            pstmt.setString(7, currentTimestamp);
            pstmt.setString(8, currentTimestamp);

            // Set parameters for QUOTATION record
            pstmt.setString(9, order.getOrderId());
            pstmt.setString(10, order.getOrderNo());
            pstmt.setString(11, currentTimestamp);
            pstmt.setString(12, currentTimestamp);

            // Set parameters for YARD_AVAILABILITY record
            pstmt.setString(13, order.getOrderId());
            pstmt.setString(14, order.getOrderNo());
            pstmt.setString(15, currentTimestamp);
            pstmt.setString(16, currentTimestamp);

            // Execute the query
            int rowsAffected = pstmt.executeUpdate();
            LOG.info("Inserted " + rowsAffected + " outbound order validation records");

            return rowsAffected > 0;

        } catch (SQLException e) {
            LOG.error("Error inserting outbound order validation records", e);
            return false;
        }
    }

    /**
     * Check if customer is at risk
     * 
     * @param customerCode The customer code to check
     * @return true if customer is at risk (flag=1), false otherwise
     */
    public boolean isCustomerAtRisk(String customerCode) {
        String sql = "SELECT c.customer_alert FROM master_data.cxm_m_customer c WHERE customer_code = ?";
        
        try (Connection conn = dataSource.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, customerCode);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    int customerAlert = rs.getInt("customer_alert");
                    return customerAlert == 1;
                }
                return false; // Customer not found or no alert flag
            }
            
        } catch (SQLException e) {
            LOG.error("Error checking customer alert status", e);
            return false;
        }
    }

    /**
     * Update customer at risk validation status based on customer alert flag
     * 
     * @param orderId The order ID to update
     * @param customerCode The customer code to check
     * @return true if update is successful, false otherwise
     */
    public boolean updateCustomerAtRiskValidation(String orderId, String customerCode) {
        boolean isAtRisk = isCustomerAtRisk(customerCode);
        
        String sql = "UPDATE t_order_validation SET " +
                     "status = ?, message = ?, validated_at = ?, updated_at = ?, updated_by = 'kogito' " +
                     "WHERE order_id = ? AND type = 'AT_RISK'";

        // Get current timestamp
        String currentTimestamp = ZonedDateTime.now()
                                      .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

        try (Connection conn = dataSource.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            if (isAtRisk) {
                pstmt.setString(1, "FAILED");
                pstmt.setString(2, "Customer is at risk");
            } else {
                pstmt.setString(1, "SUCCESS");
                pstmt.setString(2, null);
            }
            
            pstmt.setString(3, currentTimestamp);
            pstmt.setString(4, currentTimestamp);
            pstmt.setString(5, orderId);

            int rowsAffected = pstmt.executeUpdate();
            LOG.info("Updated " + rowsAffected + " customer at risk validation records");

            return rowsAffected > 0;

        } catch (SQLException e) {
            LOG.error("Error updating customer at risk validation", e);
            return false;
        }
    }

    /**
     * Update order validation status
     * 
     * @param orderId The order ID to update
     * @param type The validation type
     * @param status The new status
     * @param message Optional message
     * @return true if update is successful, false otherwise
     */
    public boolean updateOrderValidation(String orderId, String type, String status, String message) {
        String sql = "UPDATE t_order_validation SET status = ?, message = ?, " +
                     "validated_at = ?, updated_at = ?, updated_by = 'kogito' " +
                     "WHERE order_id = ? AND type = ?";

        // Get current timestamp
        String currentTimestamp = ZonedDateTime.now()
                                      .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

        try (Connection conn = dataSource.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, status);
            pstmt.setString(2, message);
            pstmt.setString(3, currentTimestamp);
            pstmt.setString(4, currentTimestamp);
            pstmt.setString(5, orderId);
            pstmt.setString(6, type);

            int rowsAffected = pstmt.executeUpdate();
            LOG.info("Updated " + rowsAffected + " order validation records");

            return rowsAffected > 0;

        } catch (SQLException e) {
            LOG.error("Error updating order validation record", e);
            return false;
        }
    }

    public boolean updateOrderOverdueValidation(String orderId) {
        String sql = "UPDATE t_order_validation SET " +
	"status = 'FAILED', message = 'AR Overdue for this customer', validated_at = NOW(), updated_at = NOW(),"+
	" updated_by = 'kogito' WHERE order_id = ?	AND `type` = 'AR_OVERDUE'";

        // Get current timestamp
        String currentTimestamp = ZonedDateTime.now()
                                      .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

        try (Connection conn = dataSource.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, orderId);

            int rowsAffected = pstmt.executeUpdate();
            LOG.info("Updated " + rowsAffected + " order validation records");

            return rowsAffected > 0;

        } catch (SQLException e) {
            LOG.error("Error updating order validation record", e);
            return false;
        }
    }
}