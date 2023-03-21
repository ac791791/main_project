package com.increff.invoice.model;


import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class InvoiceDetails {


    private Integer orderId;

    private LocalDateTime time;

    private List<InvoiceItem> items;

}
