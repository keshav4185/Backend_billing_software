package com.billing.dto;

import java.time.LocalDate;
import java.util.List;

public class InvoiceDTO {
    private Long id;
    private String invoiceNumber;
    private LocalDate invoiceDate;
    private Double subTotal;
    private Double totalAmount;
    private Double totalCgst;
    private Double totalSgst;
    private Double advanceAmount;
    private Double balanceAmount;
    private String paymentStatus;

    private String customerName;
    private String employeeName;

    private List<InvoiceItemDTO> items;

    // getters & setters
}
