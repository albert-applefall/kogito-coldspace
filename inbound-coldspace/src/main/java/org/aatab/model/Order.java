package org.aatab.model.outbound;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonProperty;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)

public class Order {

    @JsonProperty("entity")
    private String entity;

    @JsonProperty("id")
    private String orderId;

    @JsonProperty("customerId")
    private String customerId;

    @JsonProperty("customerCode")
    private String customerCode;

    @JsonProperty("customerName")
    private String customerName;

    @JsonProperty("wmsCustomerId")
    private String wmsCustomerId;

    @JsonProperty("wmsCustomerCode")
    private String wmsCustomerCode;

    @JsonProperty("wmsCustomerName")
    private String wmsCustomerName;

    @JsonProperty("consigneeCode")
    private String consigneeCode;

    @JsonProperty("consigneeName")
    private String consigneeName;

    @JsonProperty("orderNo")
    private String orderNo;

    @JsonProperty("channelName")
    private String channelName;

    @JsonProperty("channelCode")
    private String channelCode;

    @JsonProperty("warehouseLocationCode")
    private String warehouseLocationCode;

    @JsonProperty("warehouseLocationName")
    private String warehouseLocationName;

    @JsonProperty("orderMethodName")
    private String orderMethodName;

    @JsonProperty("orderMethodCode")
    private String orderMethodCode;

    @JsonProperty("orderTypeName")
    private String orderTypeName;

    @JsonProperty("orderTypeCode")
    private String orderTypeCode;

    @JsonProperty("categoryCode")
    private String categoryCode;

    @JsonProperty("categoryName")
    private String categoryName;

    @JsonProperty("orderDate")
    private String orderDate;

    @JsonProperty("paymentDate")
    private String paymentDate;

    @JsonProperty("expectedShipmentTime")
    private String expectedShipmentTime;

    @JsonProperty("invoiceRefNumber")
    private String invoiceRefNumber;

    @JsonProperty("orderMethodConfig")
    private String orderMethodConfig;

    @JsonProperty("orderStatus")
    private String orderStatus;

    @JsonProperty("statusCod")
    private Boolean statusCod;

    @JsonProperty("items")
    private List<OrderItem> items;

    @JsonProperty("recipientData")
    private RecipientData recipientData;

    // Getters and setters
    public String getEntity() {
        return entity;
    }

    public void setEntity(String entity) {
        this.entity = entity;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getCustomerCode() {
        return customerCode;
    }

    public void setCustomerCode(String customerCode) {
        this.customerCode = customerCode;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getWmsCustomerId() {
        return wmsCustomerId;
    }

    public void setWmsCustomerId(String wmsCustomerId) {
        this.wmsCustomerId = wmsCustomerId;
    }

    public String getWmsCustomerCode() {
        return wmsCustomerCode;
    }

    public void setWmsCustomerCode(String wmsCustomerCode) {
        this.wmsCustomerCode = wmsCustomerCode;
    }

    public String getWmsCustomerName() {
        return wmsCustomerName;
    }

    public void setWmsCustomerName(String wmsCustomerName) {
        this.wmsCustomerName = wmsCustomerName;
    }

    public String getConsigneeCode() {
        return consigneeCode;
    }

    public void setConsigneeCode(String consigneeCode) {
        this.consigneeCode = consigneeCode;
    }

    public String getConsigneeName() {
        return consigneeName;
    }

    public void setConsigneeName(String consigneeName) {
        this.consigneeName = consigneeName;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getChannelName() {
        return channelName;
    }

    public void setChannelName(String channelName) {
        this.channelName = channelName;
    }

    public String getChannelCode() {
        return channelCode;
    }

    public void setChannelCode(String channelCode) {
        this.channelCode = channelCode;
    }

    public String getWarehouseLocationCode() {
        return warehouseLocationCode;
    }

    public void setWarehouseLocationCode(String warehouseLocationCode) {
        this.warehouseLocationCode = warehouseLocationCode;
    }

    public String getWarehouseLocationName() {
        return warehouseLocationName;
    }

    public void setWarehouseLocationName(String warehouseLocationName) {
        this.warehouseLocationName = warehouseLocationName;
    }

    public String getOrderMethodName() {
        return orderMethodName;
    }

    public void setOrderMethodName(String orderMethodName) {
        this.orderMethodName = orderMethodName;
    }

    public String getOrderMethodCode() {
        return orderMethodCode;
    }

    public void setOrderMethodCode(String orderMethodCode) {
        this.orderMethodCode = orderMethodCode;
    }

    public String getOrderTypeName() {
        return orderTypeName;
    }

    public void setOrderTypeName(String orderTypeName) {
        this.orderTypeName = orderTypeName;
    }

    public String getOrderTypeCode() {
        return orderTypeCode;
    }

    public void setOrderTypeCode(String orderTypeCode) {
        this.orderTypeCode = orderTypeCode;
    }

    public String getCategoryCode() {
        return categoryCode;
    }

    public void setCategoryCode(String categoryCode) {
        this.categoryCode = categoryCode;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate;
    }

    public String getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(String paymentDate) {
        this.paymentDate = paymentDate;
    }

    public String getExpectedShipmentTime() {
        return expectedShipmentTime;
    }

    public void setExpectedShipmentTime(String expectedShipmentTime) {
        this.expectedShipmentTime = expectedShipmentTime;
    }

    public String getInvoiceRefNumber() {
        return invoiceRefNumber;
    }

    public void setInvoiceRefNumber(String invoiceRefNumber) {
        this.invoiceRefNumber = invoiceRefNumber;
    }

    public String getOrderMethodConfig() {
        return orderMethodConfig;
    }

    public void setOrderMethodConfig(String orderMethodConfig) {
        this.orderMethodConfig = orderMethodConfig;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public Boolean getStatusCod() {
        return statusCod;
    }

    public void setStatusCod(Boolean statusCod) {
        this.statusCod = statusCod;
    }

    public List<OrderItem> getItems() {
        return items;
    }

    public void setItems(List<OrderItem> items) {
        this.items = items;
    }

    public RecipientData getRecipientData() {
        return recipientData;
    }

    public void setRecipientData(RecipientData recipientData) {
        this.recipientData = recipientData;
    }
}