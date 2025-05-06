package com.mosuuuutech.CRUD.API;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpServer;
import com.mosuuuutech.CRUD.API.beans.*;
import com.mosuuuutech.CRUD.API.http.MoneyspaceWebhookCallBack;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;
import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.concurrent.Executors;

/**
 * Money Space Java SDK API Buildder class
 * @author Money space company limited
 * @version 2.0.0
 *
 */
public class MoneyspaceAPIBuilderImpl extends MoneyspaceAPIBuilder {

    private static final Logger logger = LoggerFactory.getLogger(MoneyspaceAPIBuilderImpl.class);

    private static final int PARAM_NAME_IDX = 0;
    private static final int PARAM_VALUE_IDX = 1;

    private static final int HTTP_OK_STATUS = 200;

    private static final String AND_DELIMITER = "&";
    private static final String EQUAL_DELIMITER = "=";

    private HttpServer server;

    // Local
//  private final String basedURL = "http://localhost:8089";
    // Staging
//    private final String basedURL = "https://a.stage.moneyspace.net";
    // Production
  private final String basedURL = "https://a.moneyspace.net";
    private final String secretId;
    private final String secretKey;

    private final String successUrl;
    private final String failUrl;
    private final String cancelUrl;

    /**
     * Initial Moneyspace API builder
     * @param secretId String of Payment Gateway secret ID E
     * @param secretKey String of Payment Gateway secret Key
     * @param successUrl String of Success Call Back URL
     * @param failUrl String of Fail Call Back URL
     * @param cancelUrl String of Cancel Call Back URL
     */
    public MoneyspaceAPIBuilderImpl(String secretId
            , String secretKey
            , String successUrl
            , String failUrl
            , String cancelUrl) throws Exception {
        // Validate cancel URL
        if (!MoneyspaceUtils.isURL(successUrl)) {
            throw new Exception("System could not initial without define a success call back URL for payment gateway!");
        }
        // Validate cancel URL
        if (!MoneyspaceUtils.isURL(failUrl)) {
            throw new Exception("System could not initial without define a fail call back URL for payment gateway!");
        }
        // Validate cancel URL
        if (!MoneyspaceUtils.isURL(cancelUrl)) {
            throw new Exception("System could not initial without define a cancel call back URL for payment gateway!");
        }
        this.secretId = secretId;
        this.secretKey = secretKey;
        this.successUrl = successUrl;
        this.failUrl = failUrl;
        this.cancelUrl = cancelUrl;
    }

    /**
     * Start HTTP Webhook Handle Server
     * @param hostname string represent server name
     * @param port int represent HTTP port
     * @param callBack MoneyspaceWebhookCallBack inherit class
     * @throws Exception all potential exception
     */
    public void startWebhook(final String hostname, final int port, final MoneyspaceWebhookCallBack callBack)
            throws Exception {
        stopWebhook();
        server = HttpServer.create(new InetSocketAddress(hostname, port), 0);
        server.createContext("/", (exchange) -> {
            // Initial data transfer object
            try {
        /*
         Parsing HTTP request
        */
                URI uri = exchange.getRequestURI();
                String httpMethod = exchange.getRequestMethod();
                logger.info("HTTP[{}] request URI: {}", httpMethod, uri.getPath());
                Headers headers = exchange.getRequestHeaders();
                headers.forEach((k, v) -> logger.info("HTTP[{}] request header ({}, {})", httpMethod, k, v));
                // Process HTTP parameters
                callBack.handle(createDtoFromQueryParams(uri));
                // Set HTTP response
                String text = "OK";
                byte[] response = text.getBytes();
                exchange.sendResponseHeaders(HTTP_OK_STATUS, response.length);
                exchange.getResponseBody().write(response);
                exchange.close();
            } catch (Exception ex) {
                logger.error("Exception occurred while parsing call back HTTP reqeust, caused by -- {}", ex.getMessage(), ex);
            }
        });
        server.setExecutor(Executors.newCachedThreadPool());
        server.start();
    }

    /**
     * Stop HTTP Webhook Handle Server
     */
    public void stopWebhook() {
        // Validate HTTP server instance is valid
        if (server != null) {
            try {
                // Stop HTTP server instance with 0 delay
                server.stop(0);
            } catch (Exception exp) {
                logger.warn("System could not stop HTTP server instance, caused by -- {}", exp.getMessage());
            }
        }
    }

    /**
     * Create Transaction (link payment)
     * @param request CreateTransactionRequest class stored all data fields to create payment.
     * @param timeout (Integer) HTTP request timeout specification, Default is 60 seconds.
     * @return CreateTransactionResponse class stored all data fields from the response.
     * @throws Exception all potential exception
     */
    public CreateTransactionResponse createTransaction(CreateTransactionRequest request, Integer timeout)
            throws Exception {
        // Initial HTTP entity payload data builder
        MultipartEntityBuilder entityBuilder = MultipartEntityBuilder.create();
        entityBuilder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
        /* Authentication */
        entityBuilder.addTextBody("secret_id", this.secretId, ContentType.MULTIPART_FORM_DATA);
        entityBuilder.addTextBody("secret_key", this.secretKey, ContentType.MULTIPART_FORM_DATA);
        /* Data attributes */
        if (request.getFirstname() != null) {
            entityBuilder.addTextBody("firstname", request.getFirstname(), ContentType.MULTIPART_FORM_DATA);
        }
        if (request.getLastname() != null) {
            entityBuilder.addTextBody("lastname", request.getLastname(), ContentType.MULTIPART_FORM_DATA);
        }
        if (request.getEmail() != null) {
            entityBuilder.addTextBody("email", request.getEmail(), ContentType.MULTIPART_FORM_DATA);
        }
        if (request.getPhone() != null) {
            entityBuilder.addTextBody("phone", request.getPhone(), ContentType.MULTIPART_FORM_DATA);
        }
        if (request.getAmount() != null) {
            entityBuilder.addTextBody("amount", request.getAmount().toString(), ContentType.MULTIPART_FORM_DATA);
        }
        if (request.getDescription() != null) {
            entityBuilder.addTextBody("description", request.getDescription(), ContentType.MULTIPART_FORM_DATA);
        }
        if (request.getAddress() != null) {
            entityBuilder.addTextBody("address", request.getAddress(), ContentType.MULTIPART_FORM_DATA);
        }
        if (request.getMessage() != null) {
            entityBuilder.addTextBody("message", request.getMessage(), ContentType.MULTIPART_FORM_DATA);
        }
        if (request.getFeeType() != null) {
            entityBuilder.addTextBody("feeType", request.getFeeType().value(), ContentType.MULTIPART_FORM_DATA);
        }
        if (request.getOrderId() != null) {
            entityBuilder.addTextBody("order_id", request.getOrderId(), ContentType.MULTIPART_FORM_DATA);
        }
        if (request.getPaymentType() != null) {
            entityBuilder.addTextBody("payment_type", request.getPaymentType().value(), ContentType.MULTIPART_FORM_DATA);
        }
        if (request.getAgreement() != null) {
            entityBuilder.addTextBody("agreement", String.valueOf(request.getAgreement().value()), ContentType.MULTIPART_FORM_DATA);
        }

        entityBuilder.addTextBody("success_Url", this.successUrl, ContentType.MULTIPART_FORM_DATA);
        entityBuilder.addTextBody("fail_Url", this.failUrl, ContentType.MULTIPART_FORM_DATA);
        entityBuilder.addTextBody("cancel_Url", this.cancelUrl, ContentType.MULTIPART_FORM_DATA);
        // Processing HTTP response
        CreateTransactionResponse response = null;
        // Perform HTTP POST method request and retrieve HTTP response payload in string format.
        String responseString = doPost(basedURL + "/payment/CreateTransaction", entityBuilder.build(), timeout);
        logger.info("HTTP response has body content: {}", responseString);
        ObjectMapper mapper = new ObjectMapper().configure(com.fasterxml.jackson.core.JsonParser.Feature.ALLOW_TRAILING_COMMA, true);
        JsonNode it = mapper.readTree(responseString);
        if (it.isArray()) {
            for (final JsonNode node : it) {
                response = new CreateTransactionResponse(node.get(CreateTransactionResponse.PARAM_STATUS).asText());
                // Collect a transaction ID if only is the value valid in the response payload.
                if (node.has(CreateTransactionResponse.PARAM_TRANSACTION_ID)) {
                    response.setTransactionId(node.get(CreateTransactionResponse.PARAM_TRANSACTION_ID).asText());
                }
                // Collect a qr prompt if only is the value valid in the response payload.
                if (node.has(CreateTransactionResponse.PARAM_IMAGE_QRPROM)) {
                    response.setImageQrprom(node.get(CreateTransactionResponse.PARAM_IMAGE_QRPROM).asText());
                }
                // Collect a link payment if only is the value valid in the response payload.
                if (node.has(CreateTransactionResponse.PARAM_LINK_PAYMENT)) {
                    response.setLinkPayment(node.get(CreateTransactionResponse.PARAM_LINK_PAYMENT).asText());
                }
            }
        } else {
            logger.warn("System has found unexpected response to proceed, -- {}", responseString);
        }
        return response;
    }

    /**
     * Create Transaction (mskey)
     * @param request CreateTransactionMskeyRequest class stored all data fields to create payment.
     * @param timeout (Integer) HTTP request timeout specification, Default is 60 seconds.
     * @return CreateTransactionMskeyResponse class stored all data fields from the response.
     * @throws Exception all potential exception
     */
    public CreateTransactionIDResponse createTransaction(CreateTransactionIDRequest request, Integer timeout)
            throws Exception {
        // Initial HTTP entity data payload builder
        MultipartEntityBuilder entityBuilder = MultipartEntityBuilder.create();
        entityBuilder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
        /* Authentication */
        entityBuilder.addTextBody("secret_id", this.secretId, ContentType.MULTIPART_FORM_DATA);
        entityBuilder.addTextBody("secret_key", this.secretKey, ContentType.MULTIPART_FORM_DATA);

        /* Data attributes */
        if (request.getFirstname() != null) {
            entityBuilder.addTextBody("firstname", request.getFirstname(), ContentType.MULTIPART_FORM_DATA);
        }
        if (request.getLastname() != null) {
            entityBuilder.addTextBody("lastname", request.getLastname(), ContentType.MULTIPART_FORM_DATA);
        }
        if (request.getEmail() != null) {
            entityBuilder.addTextBody("email", request.getEmail(), ContentType.MULTIPART_FORM_DATA);
        }
        if (request.getPhone() != null) {
            entityBuilder.addTextBody("phone", request.getPhone(), ContentType.MULTIPART_FORM_DATA);
        }
        if (request.getAmount() != null) {
            entityBuilder.addTextBody("amount", request.getAmount().toString(), ContentType.MULTIPART_FORM_DATA);
        }
        if (request.getDescription() != null) {
            entityBuilder.addTextBody("description", request.getDescription(), ContentType.MULTIPART_FORM_DATA);
        }
        if (request.getAddress() != null) {
            entityBuilder.addTextBody("address", request.getAddress(), ContentType.MULTIPART_FORM_DATA);
        }
        if (request.getMessage() != null) {
            entityBuilder.addTextBody("message", request.getMessage(), ContentType.MULTIPART_FORM_DATA);
        }
        if (request.getFeeType() != null) {
            entityBuilder.addTextBody("feeType", request.getFeeType().value(), ContentType.MULTIPART_FORM_DATA);
        }
        if (request.getOrderId() != null) {
            entityBuilder.addTextBody("order_id", request.getOrderId(), ContentType.MULTIPART_FORM_DATA);
        }
        if (request.getPaymentType() != null) {
            entityBuilder.addTextBody("payment_type", request.getPaymentType().value(), ContentType.MULTIPART_FORM_DATA);
        }
        if (request.getAgreement() != null) {
            entityBuilder.addTextBody("agreement", String.valueOf(request.getAgreement().value()), ContentType.MULTIPART_FORM_DATA);
        }

        entityBuilder.addTextBody("success_Url", this.successUrl, ContentType.MULTIPART_FORM_DATA);
        entityBuilder.addTextBody("fail_Url", this.failUrl, ContentType.MULTIPART_FORM_DATA);
        entityBuilder.addTextBody("cancel_Url", this.cancelUrl, ContentType.MULTIPART_FORM_DATA);

        if (request.getBankType() != null) {
            entityBuilder.addTextBody("bankType", String.valueOf(request.getBankType().value()), ContentType.MULTIPART_FORM_DATA);
        }
        if (request.getBankType() != null && request.getStartTerm() != null) {
            // Validate start term number (a number to represent a month) by particular bank type.
            switch (request.getBankType()) {
                case BAY:
                    if (!Arrays.asList(BankType.BAYStartTerm).contains(request.getStartTerm())) {
                        throw new Exception("Bank type '"+request.getBankType().value()+"' only support start terms ["+Arrays.toString(BankType.BAYStartTerm)+"]");
                    }
                    break;
                case KTC:
                    if (!Arrays.asList(BankType.KTCStartTerm).contains(request.getStartTerm())) {
                        throw new Exception("Bank type '"+request.getBankType().value()+"' only support start terms ["+Arrays.toString(BankType.KTCStartTerm)+"]");
                    }
                    break;
                case FCY:
                    if (!Arrays.asList(BankType.FCYStartTerm).contains(request.getStartTerm())) {
                        throw new Exception("Bank type '"+request.getBankType().value()+"' only support start terms ["+Arrays.toString(BankType.FCYStartTerm)+"]");
                    }
                    break;
            }
            entityBuilder.addTextBody("startTerm", String.valueOf(request.getStartTerm()), ContentType.MULTIPART_FORM_DATA);
        }
        if (request.getBankType() != null && request.getEndTerm() != null) {
            entityBuilder.addTextBody("endTerm", String.valueOf(request.getEndTerm()), ContentType.MULTIPART_FORM_DATA);
        }
        // Processing HTTP response
        CreateTransactionIDResponse response = null;
        // Perform HTTP POST method request and retrieve HTTP response payload in string format.
        String responseString = doPost(basedURL + "/CreateTransactionID", entityBuilder.build(), timeout);
        logger.info("HTTP response has body content: {}", responseString);
        ObjectMapper mapper = new ObjectMapper().configure(com.fasterxml.jackson.core.JsonParser.Feature.ALLOW_TRAILING_COMMA, true);
        JsonNode it = mapper.readTree(responseString);
        if (it.isArray()) {
            for (final JsonNode node : it) {
                response = new CreateTransactionIDResponse(node.get(CreateTransactionIDResponse.PARAM_STATUS).asText());
                // Collect a transaction ID if only is the value valid in the response payload.
                if (node.has(CreateTransactionIDResponse.PARAM_TRANSACTION_ID)) {
                    response.setTransactionId(node.get(CreateTransactionIDResponse.PARAM_TRANSACTION_ID).asText());
                }
                // Collect a transaction ID if only is the value valid in the response payload.
                if (node.has(CreateTransactionIDResponse.PARAM_MSKEY)) {
                    response.setMskey(node.get(CreateTransactionIDResponse.PARAM_MSKEY).asText());
                }
            }
        } else {
            logger.warn("System has found unexpected response to proceed, -- {}", responseString);
        }
        return response;
    }

    /**
     * Generate Payment Link by Transaction ID
     * @param transactionId String represented Transaction ID for checking Pyament status
     * @return The generated payment link with transaction ID.
     */
    public String getPaymentLink(String transactionId) {
        return basedURL + "/makepayment/linkpaymentcard?secreteID=" + this.secretId + "&transactionID=" + transactionId;
    }

    /**
     * Create Transaction Installment
     * @param request CreateTransactionIDRequest class stored all data fields to create installment payment.
     * @param timeout (Integer) HTTP request timeout specification, Default is 60 seconds.
     * @return CreateTransactionResponse class stored all data fields from the response.
     * @throws Exception all potential exception
     */
    public CreateTransactionResponse createTransactionInstallment(CreateTransactionIDRequest request, Integer timeout)
            throws Exception {
        // Initial HTTP entity data payload builder
        MultipartEntityBuilder entityBuilder = MultipartEntityBuilder.create();
        entityBuilder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
        /* Authentication */
        entityBuilder.addTextBody("secret_id", this.secretId, ContentType.MULTIPART_FORM_DATA);
        entityBuilder.addTextBody("secret_key", this.secretKey, ContentType.MULTIPART_FORM_DATA);

        /* Data attributes */
        if (request.getFirstname() != null) {
            entityBuilder.addTextBody("firstname", request.getFirstname(), ContentType.MULTIPART_FORM_DATA);
        }
        if (request.getLastname() != null) {
            entityBuilder.addTextBody("lastname", request.getLastname(), ContentType.MULTIPART_FORM_DATA);
        }
        if (request.getEmail() != null) {
            entityBuilder.addTextBody("email", request.getEmail(), ContentType.MULTIPART_FORM_DATA);
        }
        if (request.getPhone() != null) {
            entityBuilder.addTextBody("phone", request.getPhone(), ContentType.MULTIPART_FORM_DATA);
        }
        if (request.getAmount() != null) {
            entityBuilder.addTextBody("amount", request.getAmount().toString(), ContentType.MULTIPART_FORM_DATA);
        }
        if (request.getDescription() != null) {
            entityBuilder.addTextBody("description", request.getDescription(), ContentType.MULTIPART_FORM_DATA);
        }
        if (request.getAddress() != null) {
            entityBuilder.addTextBody("address", request.getAddress(), ContentType.MULTIPART_FORM_DATA);
        }
        if (request.getMessage() != null) {
            entityBuilder.addTextBody("message", request.getMessage(), ContentType.MULTIPART_FORM_DATA);
        }
        if (request.getFeeType() != null) {
            entityBuilder.addTextBody("feeType", request.getFeeType().value(), ContentType.MULTIPART_FORM_DATA);
        }
        if (request.getOrderId() != null) {
            entityBuilder.addTextBody("order_id", request.getOrderId(), ContentType.MULTIPART_FORM_DATA);
        }
        if (request.getPaymentType() != null) {
            entityBuilder.addTextBody("payment_type", request.getPaymentType().value(), ContentType.MULTIPART_FORM_DATA);
        }
        if (request.getAgreement() != null) {
            entityBuilder.addTextBody("agreement", String.valueOf(request.getAgreement().value()), ContentType.MULTIPART_FORM_DATA);
        }

        entityBuilder.addTextBody("success_Url", this.successUrl, ContentType.MULTIPART_FORM_DATA);
        entityBuilder.addTextBody("fail_Url", this.failUrl, ContentType.MULTIPART_FORM_DATA);
        entityBuilder.addTextBody("cancel_Url", this.cancelUrl, ContentType.MULTIPART_FORM_DATA);

        if (request.getBankType() != null) {
            entityBuilder.addTextBody("bankType", String.valueOf(request.getBankType().value()), ContentType.MULTIPART_FORM_DATA);
        }
        if (request.getBankType() != null && request.getStartTerm() != null) {
            // Validate start term number (a number to represent a month) by particular bank type.
            switch (request.getBankType()) {
                case BAY:
                    if (!Arrays.asList(BankType.BAYStartTerm).contains(request.getStartTerm())) {
                        throw new Exception("Bank type '"+request.getBankType().value()+"' only support start terms ["+Arrays.toString(BankType.BAYStartTerm)+"]");
                    }
                    break;
                case KTC:
                    if (!Arrays.asList(BankType.KTCStartTerm).contains(request.getStartTerm())) {
                        throw new Exception("Bank type '"+request.getBankType().value()+"' only support start terms ["+Arrays.toString(BankType.KTCStartTerm)+"]");
                    }
                    break;
                case FCY:
                    if (!Arrays.asList(BankType.FCYStartTerm).contains(request.getStartTerm())) {
                        throw new Exception("Bank type '"+request.getBankType().value()+"' only support start terms ["+Arrays.toString(BankType.FCYStartTerm)+"]");
                    }
                    break;
            }
            entityBuilder.addTextBody("startTerm", String.valueOf(request.getStartTerm()), ContentType.MULTIPART_FORM_DATA);
        }
        if (request.getBankType() != null && request.getEndTerm() != null) {
            entityBuilder.addTextBody("endTerm", String.valueOf(request.getEndTerm()), ContentType.MULTIPART_FORM_DATA);
        }

        // Processing HTTP response
        CreateTransactionResponse response = null;
        // Perform HTTP POST method request and retrieve HTTP response payload in string format.
        String responseString = doPost(basedURL + "/payment/Createinstallment", entityBuilder.build(), timeout);
        logger.info("HTTP response has body content: {}", responseString);
        ObjectMapper mapper = new ObjectMapper().configure(com.fasterxml.jackson.core.JsonParser.Feature.ALLOW_TRAILING_COMMA, true);
        JsonNode it = mapper.readTree(responseString);
        if (it.isArray()) {
            for (final JsonNode node : it) {
                response = new CreateTransactionResponse(node.get(CreateTransactionResponse.PARAM_STATUS).asText());
                // Collect a transaction ID if only is the value valid in the response payload.
                if (node.has(CreateTransactionResponse.PARAM_TRANSACTION_ID)) {
                    response.setTransactionId(node.get(CreateTransactionResponse.PARAM_TRANSACTION_ID).asText());
                }
                // Collect a link payment if only is the value valid in the response payload.
                if (node.has(CreateTransactionResponse.PARAM_LINK_PAYMENT)) {
                    response.setLinkPayment(node.get(CreateTransactionResponse.PARAM_LINK_PAYMENT).asText());
                }
            }
        } else {
            logger.warn("System has found unexpected response to proceed, -- {}", responseString);
        }
        return response;
    }

    /**
     * Check Payment Status by Transaction ID
     * @param transactionId String represented Transaction ID for checking Pyament status
     * @param timeout (Integer) HTTP request timeout specification, Default is 60 seconds.
     * @return TransactionStatusResponse class stored all data fields from the response.
     * @throws Exception all potential exceptions
     */
    public TransactionStatusResponse checkPaymentStatusByTransaction(String transactionId, Integer timeout)
            throws Exception {
        // Initial HTTP entity data payload builder
        MultipartEntityBuilder entityBuilder = MultipartEntityBuilder.create();
        entityBuilder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
        /* Authentication */
        entityBuilder.addTextBody("secret_id", this.secretId, ContentType.MULTIPART_FORM_DATA);
        entityBuilder.addTextBody("secret_key", this.secretKey, ContentType.MULTIPART_FORM_DATA);
        /* Data attributes */
        entityBuilder.addTextBody("transaction_ID", transactionId, ContentType.MULTIPART_FORM_DATA);
        // Processing HTTP response
        TransactionStatusResponse response = null;
        // Perform HTTP POST method request and retrieve HTTP response payload in string format.
        String responseString = doPost(basedURL + "/payment/transactionID", entityBuilder.build(), timeout);
        logger.info("HTTP response has body content: {}", responseString);
        ObjectMapper mapper = new ObjectMapper().configure(com.fasterxml.jackson.core.JsonParser.Feature.ALLOW_TRAILING_COMMA, true);
        JsonNode it = mapper.readTree(responseString);
        if (it.isArray()) {
            for (final JsonNode node : it) {
                response = new TransactionStatusResponse(node.get(TransactionStatusResponse.PARAM_TRANSACTION_ID).asText());
                // Collect an amount value if only is the value valid in the response payload.
                if (node.has(TransactionStatusResponse.PARAM_AMOUNT)) {
                    try {
                        response.setAmount(node.get(TransactionStatusResponse.PARAM_AMOUNT).asDouble());
                    } catch (NumberFormatException exp) {
                        logger.warn("Amount `{}` value could not cast to Double object!", node.get(TransactionStatusResponse.PARAM_AMOUNT));
                    }
                }
                // Collect a description value if only is the value valid in the response payload.
                if (node.has(TransactionStatusResponse.PARAM_DESCRIPTION)) {
                    response.setDescription(node.get(TransactionStatusResponse.PARAM_DESCRIPTION).asText());
                }
                // Collect a payment status value if only is the value valid in the response payload.
                if (node.has(TransactionStatusResponse.PARAM_STATUS_PAYMENT)) {
                    response.setStatusPayment(node.get(TransactionStatusResponse.PARAM_STATUS_PAYMENT).asText());
                }
            }
        } else {
            logger.warn("System has found unexpected response to proceed, -- {}", responseString);
        }
        return response;
    }

    /**
     * Check Payment Status by Order ID
     * @param orderId String represented Order ID for checking Pyament status
     * @param timeout (Integer) HTTP request timeout specification, Default is 60 seconds.
     * @return OrderStatusResponse class stored all data fields from the response.
     * @throws Exception all potential exceptions
     */
    public OrderStatusResponse checkPaymentStatusByOrder(String orderId, Integer timeout) throws Exception {
        // Initial HTTP entity data payload builder
        MultipartEntityBuilder entityBuilder = MultipartEntityBuilder.create();
        entityBuilder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
        /* Authentication */
        entityBuilder.addTextBody("secret_id", this.secretId, ContentType.MULTIPART_FORM_DATA);
        entityBuilder.addTextBody("secret_key", this.secretKey, ContentType.MULTIPART_FORM_DATA);
        /* Data attributes */
        entityBuilder.addTextBody("order_id", orderId, ContentType.MULTIPART_FORM_DATA);
        // Processing HTTP response
        OrderStatusResponse response = null;
        // Perform HTTP POST method request and retrieve HTTP response payload in string format.
        String responseString = doPost(basedURL + "/payment/OrderID", entityBuilder.build(), timeout);
        logger.info("HTTP response has body content: {}", responseString);
        ObjectMapper mapper = new ObjectMapper().configure(com.fasterxml.jackson.core.JsonParser.Feature.ALLOW_TRAILING_COMMA, true);
        JsonNode it = mapper.readTree(responseString);
        if (it.isArray()) {
            for (final JsonNode node : it) {
                response = new OrderStatusResponse(node.get(OrderStatusResponse.PARAM_ORDER_ID).asText());
                // Collect an amount value if only is the value valid in the response payload.
                if (node.has(OrderStatusResponse.PARAM_AMOUNT)) {
                    try {
                        response.setAmount(node.get(OrderStatusResponse.PARAM_AMOUNT).asDouble());
                    } catch (NumberFormatException exp) {
                        logger.warn("Amount `{}` value could not cast to Double object!", node.get(OrderStatusResponse.PARAM_AMOUNT));
                    }
                }
                // Collect a description value if only is the value valid in the response payload.
                if (node.has(OrderStatusResponse.PARAM_DESCRIPTION)) {
                    response.setDescription(node.get(OrderStatusResponse.PARAM_DESCRIPTION).asText());
                }
                // Collect a payment status value if only is the value valid in the response payload.
                if (node.has(OrderStatusResponse.PARAM_STATUS_PAYMENT)) {
                    response.setStatusPayment(node.get(OrderStatusResponse.PARAM_STATUS_PAYMENT).asText());
                }
            }
        } else {
            logger.warn("System has found unexpected response to proceed, -- {}", responseString);
        }
        return response;
    }

    /**
     * Check Order by Order ID API
     * @param orderId String represented Order ID for checking Order
     * @param timeout (Integer) HTTP request timeout specification, Default is 60 seconds.
     * @return CheckOrderIDResponse class stored all data fields from the response.
     * @throws Exception all potential exceptions
     */
    public CheckOrderIDResponse checkOrderID(String orderId, Integer timeout) throws Exception {
        // Initial HTTP entity data payload builder
        MultipartEntityBuilder entityBuilder = MultipartEntityBuilder.create();
        entityBuilder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
        /* Authentication */
        entityBuilder.addTextBody("secret_id", this.secretId, ContentType.MULTIPART_FORM_DATA);
        entityBuilder.addTextBody("secret_key", this.secretKey, ContentType.MULTIPART_FORM_DATA);
        /* Data attributes */
        entityBuilder.addTextBody("order_id", orderId, ContentType.MULTIPART_FORM_DATA);
        // Processing HTTP response
        CheckOrderIDResponse response = null;
        // Perform HTTP POST method request and retrieve HTTP response payload in string format.
        String responseString = doPost(basedURL + "/CheckOrderID", entityBuilder.build(), timeout);
        logger.info("HTTP response has body content: {}", responseString);
        ObjectMapper mapper = new ObjectMapper().configure(com.fasterxml.jackson.core.JsonParser.Feature.ALLOW_TRAILING_COMMA, true);
        JsonNode it = mapper.readTree(responseString);
        if (it.isArray()) {
            // Parsing JSON array intp object
            for (final JsonNode node : it) {
                // Initial object to store response payload
                response = new CheckOrderIDResponse();
                if (node.has(CheckOrderIDResponse.PARAM_ORDER_ID)) {
                    // Parsing JSON object
                    JsonNode order = node.get(CheckOrderIDResponse.PARAM_ORDER_ID);
                    // Collect an amount value if only is the value valid in the response payload.
                    if (order.has(CheckOrderIDResponse.PARAM_AMOUNT)) {
                        try {
                            response.setAmount(order.get(CheckOrderIDResponse.PARAM_AMOUNT).asDouble());
                        } catch (NumberFormatException exp) {
                            logger.warn("Amount `{}` value could not cast to Double object!", order.get(CheckOrderIDResponse.PARAM_AMOUNT));
                        }
                    }
                    // Collect a description value if only is the value valid in the response payload.
                    if (order.has(CheckOrderIDResponse.PARAM_DESCRIPTION)) {
                        response.setDescription(order.get(CheckOrderIDResponse.PARAM_DESCRIPTION).asText());
                    }
                    // Collect a payment status value if only is the value valid in the response payload.
                    if (order.has(CheckOrderIDResponse.PARAM_STATUS)) {
                        response.setStatus(order.get(CheckOrderIDResponse.PARAM_STATUS).asText());
                    }
                } else {
                    // Collect a description value if only is the value valid in the response payload.
                    if (node.has(CheckOrderIDResponse.PARAM_DESCRIPTION)) {
                        response.setDescription(node.get(CheckOrderIDResponse.PARAM_DESCRIPTION).asText());
                    }
                    // Collect a payment status value if only is the value valid in the response payload.
                    if (node.has(CheckOrderIDResponse.PARAM_STATUS)) {
                        response.setStatus(node.get(CheckOrderIDResponse.PARAM_STATUS).asText());
                    }
                }
            }
        } else {
            logger.warn("System has found unexpected response to proceed, -- {}", responseString);
        }

        return response;
    }

    /**
     * Check Payment by Transaction ID API
     * @param transactionId String represented Transaction ID for checking Payment
     * @param timeout (Integer) HTTP request timeout specification, Default is 60 seconds.
     * @return CheckPaymentResponse class stored all data fields from the response.
     * @throws Exception all potential exceptions
     */
    public CheckPaymentResponse checkPayment(String transactionId, Integer timeout) throws Exception {
        // Initial HTTP entity data payload builder
        MultipartEntityBuilder entityBuilder = MultipartEntityBuilder.create();
        entityBuilder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
        /* Authentication */
        entityBuilder.addTextBody("secret_id", this.secretId, ContentType.MULTIPART_FORM_DATA);
        entityBuilder.addTextBody("secret_key", this.secretKey, ContentType.MULTIPART_FORM_DATA);
        /* Data attributes */
        entityBuilder.addTextBody("transaction_ID", transactionId, ContentType.MULTIPART_FORM_DATA);
        // Processing HTTP response
        CheckPaymentResponse response = null;
        // Perform HTTP POST method request and retrieve HTTP response payload in string format.
        String responseString = doPost(basedURL + "/CheckPayment", entityBuilder.build(), timeout);
        logger.info("HTTP response has body content: {}", responseString);
        ObjectMapper mapper = new ObjectMapper().configure(com.fasterxml.jackson.core.JsonParser.Feature.ALLOW_TRAILING_COMMA, true);
        JsonNode it = mapper.readTree(responseString);
        if (it.isArray()) {
            // Parsing JSON array intp object
            for (final JsonNode node : it) {
                // Initial object to store response payload
                response = new CheckPaymentResponse();
                if (node.has(CheckPaymentResponse.PARAM_TRANSACTION_ID)) {
                    // Parsing JSON object
                    JsonNode order = node.get(CheckPaymentResponse.PARAM_TRANSACTION_ID);
                    // Collect an amount value if only is the value valid in the response payload.
                    if (order.has(CheckPaymentResponse.PARAM_AMOUNT)) {
                        try {
                            response.setAmount(order.get(CheckPaymentResponse.PARAM_AMOUNT).asDouble());
                        } catch (NumberFormatException exp) {
                            logger.warn("Amount `{}` value could not cast to Double object!", order.get(CheckPaymentResponse.PARAM_AMOUNT));
                        }
                    }
                    // Collect a description value if only is the value valid in the response payload.
                    if (order.has(CheckPaymentResponse.PARAM_DESCRIPTION)) {
                        response.setDescription(order.get(CheckPaymentResponse.PARAM_DESCRIPTION).asText());
                    }
                    // Collect a payment status value if only is the value valid in the response payload.
                    if (order.has(CheckPaymentResponse.PARAM_STATUS)) {
                        response.setStatus(order.get(CheckPaymentResponse.PARAM_STATUS).asText());
                    }
                } else {
                    // Collect a description value if only is the value valid in the response payload.
                    if (node.has(CheckPaymentResponse.PARAM_DESCRIPTION)) {
                        response.setDescription(node.get(CheckPaymentResponse.PARAM_DESCRIPTION).asText());
                    }
                    // Collect a payment status value if only is the value valid in the response payload.
                    if (node.has(CheckPaymentResponse.PARAM_STATUS)) {
                        response.setStatus(node.get(CheckPaymentResponse.PARAM_STATUS).asText());
                    }
                }
            }
        } else {
            logger.warn("System has found unexpected response to proceed, -- {}", responseString);
        }
        return response;
    }

    /**
     * Cancel Payment by Transaction ID API
     * @param transactionId String represented Transaction ID for cancel the payment
     * @param timeout (Integer) HTTP request timeout specification, Default is 60 seconds.
     * @return CancelPaymentResponse class stored all data fields from the response.
     * @throws Exception all potential exceptions
     */
    public CancelPaymentResponse cancelPayment(String transactionId, Integer timeout) throws Exception {
        // Initial HTTP entity data payload builder
        MultipartEntityBuilder entityBuilder = MultipartEntityBuilder.create();
        entityBuilder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
        /* Authentication */
        entityBuilder.addTextBody("secret_id", this.secretId, ContentType.MULTIPART_FORM_DATA);
        entityBuilder.addTextBody("secret_key", this.secretKey, ContentType.MULTIPART_FORM_DATA);
        /* Data attributes */
        entityBuilder.addTextBody("transaction_ID", transactionId, ContentType.MULTIPART_FORM_DATA);
        // Processing HTTP response
        CancelPaymentResponse response = null;
        // Perform HTTP POST method request and retrieve HTTP response payload in string format.
        String responseString = doPost(basedURL + "/merchantapi/cancelpayment", entityBuilder.build(), timeout);
        logger.info("HTTP response has body content: {}", responseString);
        ObjectMapper mapper = new ObjectMapper().configure(com.fasterxml.jackson.core.JsonParser.Feature.ALLOW_TRAILING_COMMA, true);
        JsonNode it = mapper.readTree(responseString);
        if (it.isArray()) {
            // Parsing JSON array intp object
            for (final JsonNode node : it) {
                // Initial object to store response payload
                response = new CancelPaymentResponse();
                // Collect a description value if only is the value valid in the response payload.
                if (node.has(CancelPaymentResponse.PARAM_MESSAGE)) {
                    response.setMessage(node.get(CancelPaymentResponse.PARAM_MESSAGE).asText());
                }
                // Collect a description value if only is the value valid in the response payload.
                if (node.has(CancelPaymentResponse.PARAM_DESCRIPTION)) {
                    response.setDescription(node.get(CancelPaymentResponse.PARAM_DESCRIPTION).asText());
                }
                // Collect a payment status value if only is the value valid in the response payload.
                if (node.has(CancelPaymentResponse.PARAM_STATUS)) {
                    response.setStatus(node.get(CancelPaymentResponse.PARAM_STATUS).asText());
                }
            }
        } else {
            logger.warn("System has found unexpected response to proceed, -- {}", responseString);
        }
        return response;
    }

    /**
     * HTTP request QueryString parsing method
     * @param uri HTTP request
     * @return MoneyspaceWebhookCallbackDto class stored data fields from HTTP request
     */
    private MoneyspaceWebhookDto createDtoFromQueryParams(URI uri) {
        MoneyspaceWebhookDto dto = new MoneyspaceWebhookDto();
        //Get the request query
        String query = uri.getQuery();
        if (query != null) {
            logger.info("HTTP Call Back QueryString - {}", query);
            String[] queryParams = query.split(AND_DELIMITER);
            if (queryParams.length > 0) {
                String date = "";
                String time = "";
                // Loop for each HTTP QueryString params
                for (String qParam : queryParams) {
                    String[] param = qParam.split(EQUAL_DELIMITER);
                    if (param.length > 0) {
                        for (int i = 0; i < param.length; i++) {
                            switch (param[PARAM_NAME_IDX]) {
                                case MoneyspaceWebhookDto.PARAM_LOCALE:
                                    dto.setLocale(param[PARAM_VALUE_IDX]);
                                    break;
                                case MoneyspaceWebhookDto.PARAM_STORE_NAME:
                                    dto.setStoreName(param[PARAM_VALUE_IDX]);
                                    break;
                                case MoneyspaceWebhookDto.PARAM_STORE_LOGO:
                                    dto.setStoreLogo(param[PARAM_VALUE_IDX]);
                                    break;
                                case MoneyspaceWebhookDto.PARAM_STORE_PHONE:
                                    dto.setStorePhone(param[PARAM_VALUE_IDX]);
                                    break;
                                case MoneyspaceWebhookDto.PARAM_NAME_CUSTOMER:
                                    dto.setNameCustomer(param[PARAM_VALUE_IDX]);
                                    break;
                                case MoneyspaceWebhookDto.PARAM_DATETIME:
                                    try {
                                        if (!MoneyspaceUtils.isEmpty(param[PARAM_VALUE_IDX])) {
                                            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy hh:mm");
                                            dto.setDatetime(sdf.parse(param[PARAM_VALUE_IDX]));
                                        }
                                    } catch (Exception ex) {
                                        logger.warn("System could not parsing a datetime value[{}] by format 'dd-MM-yyyy hh:mm'", param[PARAM_VALUE_IDX]);
                                    }
                                    break;
                                case MoneyspaceWebhookDto.PARAM_IDPAY:
                                    dto.setIdpay(param[PARAM_VALUE_IDX]);
                                    break;
                                case MoneyspaceWebhookDto.PARAM_NAME_MERCHANT:
                                    dto.setNameMerchant(param[PARAM_VALUE_IDX]);
                                    break;
                                case MoneyspaceWebhookDto.PARAM_AGREEMENT:
                                    if (param[PARAM_VALUE_IDX].matches("^[1-5]$")) {
                                        dto.setAgreement(Integer.parseInt(param[PARAM_VALUE_IDX]));
                                    }
                                    break;
                                case MoneyspaceWebhookDto.PARAM_DESCRIPTION:
                                    dto.setDescription(param[PARAM_VALUE_IDX]);
                                    break;
                                case MoneyspaceWebhookDto.PARAM_AMOUNT:
                                    if (param[PARAM_VALUE_IDX].matches("(?=.*?\\d)^\\$?(([1-9]\\d{0,2}(,\\d{3})*)|\\d+)?(\\.\\d{1,2})?$")) {
                                        dto.setAmount(Double.parseDouble(param[PARAM_VALUE_IDX]));
                                    }
                                    break;
                                case MoneyspaceWebhookDto.PARAM_DATE:
                                    try {
                                        if (!MoneyspaceUtils.isEmpty(param[PARAM_VALUE_IDX])) {
                                            if (!MoneyspaceUtils.isEmpty(time)) {
                                                SimpleDateFormat sdf = new SimpleDateFormat("dd/MMM/yyyy hh:mm");
                                                dto.setDatetime(sdf.parse(param[PARAM_VALUE_IDX] + " " + time));
                                            } else {
                                                date = param[PARAM_VALUE_IDX];
                                            }
                                        }
                                    } catch (Exception ex) {
                                        logger.warn("System could not parsing a datetime value[{} {}] by format 'dd/MMM/yyyy hh:mm'", param[PARAM_VALUE_IDX], time);
                                    }
                                    break;
                                case MoneyspaceWebhookDto.PARAM_TIME:
                                    try {
                                        if (!MoneyspaceUtils.isEmpty(param[PARAM_VALUE_IDX])) {
                                            if (!MoneyspaceUtils.isEmpty(date)) {
                                                SimpleDateFormat sdf = new SimpleDateFormat("dd/MMM/yyyy hh:mm");
                                                dto.setDatetime(sdf.parse(date + " " + param[PARAM_VALUE_IDX]));
                                            } else {
                                                time = param[PARAM_VALUE_IDX];
                                            }
                                        }
                                    } catch (Exception ex) {
                                        logger.warn("System could not parsing a datetime value[{} {}] by format 'dd/MMM/yyyy hh:mm'", date, param[PARAM_VALUE_IDX]);
                                    }
                                    break;
                                case MoneyspaceWebhookDto.PARAM_TYPE:
                                    dto.setType(param[PARAM_VALUE_IDX]);
                                    break;
                                case MoneyspaceWebhookDto.PARAM_TRANSACTION_ID:
                                    dto.setTransactionId(param[PARAM_VALUE_IDX]);
                                    break;
                            }
                        }
                    }
                }
            }
        }

        return dto;
    }
}