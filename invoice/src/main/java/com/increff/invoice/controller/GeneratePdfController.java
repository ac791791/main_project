package com.increff.invoice.controller;

import com.increff.invoice.model.InvoiceDetails;
import com.increff.invoice.model.InvoiceItem;
import com.increff.invoice.service.GenerateInvoiceService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.fop.apps.FOPException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Api
@RestController
public class GeneratePdfController {

    @Autowired
    private  GenerateInvoiceService generateInvoiceService;


    @ApiOperation(value = "Returns base64-encoded string representing the PDF invoice")
    @RequestMapping(path = "/api/generate", method = RequestMethod.POST)
    public String getEncodedPdf(@RequestBody InvoiceDetails invoiceDetails)
            throws IOException, FOPException, TransformerException, ParserConfigurationException {

        return generateInvoiceService.getEncodedPdf(invoiceDetails);

    }

}