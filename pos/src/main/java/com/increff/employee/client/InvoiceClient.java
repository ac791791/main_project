package com.increff.employee.client;

import com.increff.employee.model.InvoiceDetails;
import com.increff.employee.model.InvoiceItem;
import com.increff.employee.pojo.DailyReportPojo;
import com.increff.employee.pojo.OrderPojo;
import com.increff.employee.service.DailyReportService;
import com.increff.employee.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletOutputStream;
import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Base64;

@Service
public class InvoiceClient {

    @Autowired
    private OrderService orderService;
    @Autowired
    private DailyReportService dailyReportService;
    @Value("${invoice.url}")
    private String fopUrl;
    @Value("${pdf.path}")
    private String path;
    public void generateInvoice(InvoiceDetails invoiceDetails,HttpServletResponse response1) throws IOException {
        int orderId= invoiceDetails.getOrderId();
        String filePath = path + "invoice" + orderId + ".pdf";
        File file = new File(filePath);
        if(file.exists())
        {
            String response = CheckInvoice(file);
            downloadPDF(orderId,response,response1);
        }
        else {
            RestTemplate restTemplate = new RestTemplate();
            String response = restTemplate.postForObject(fopUrl, invoiceDetails, String.class);
            status(invoiceDetails);

            savePdf(response,orderId);
            downloadPDF(orderId,response,response1);

        }
    }
    public void savePdf(String response,Integer orderId)
    {
        byte[] decodedBytes = Base64.getDecoder().decode(response);
        File file = new File(path + "invoice" + orderId + ".pdf");
        try (FileOutputStream fos = new FileOutputStream(file)) {
            fos.write(decodedBytes);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public String CheckInvoice(File file) throws FileNotFoundException {
        try(FileInputStream fis = new FileInputStream(file);
            BufferedInputStream bis= new BufferedInputStream(fis);
            ByteArrayOutputStream baos = new ByteArrayOutputStream())
        {
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = bis.read(buffer)) != -1) {
                baos.write(buffer, 0, bytesRead);
            }
            byte[] fileBytes = baos.toByteArray();
            return  Base64.getEncoder().encodeToString(fileBytes);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public void downloadPDF(int orderId,String base64, HttpServletResponse response) throws IOException {
        // Step 2: Decode the Base64 string to binary data
        byte[] binaryData = Base64.getDecoder().decode(base64);

        String name=orderId+"_Invoice.pdf";
        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "attachment; filename=" + name);

        ServletOutputStream outputStream = response.getOutputStream();
        outputStream.write(binaryData);
        outputStream.close();
    }


    public void status(InvoiceDetails invoiceDetails){
        int orderId= invoiceDetails.getOrderId();
        OrderPojo orderPojo=orderService.get(orderId);

        if(orderPojo.getInvoiceStatus()==0){
            System.out.println("alpha4");
            LocalDate currentDate = LocalDate.now(); // get the current date
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd"); // create a formatter
            String dateString = currentDate.format(formatter);

            System.out.println("Debug1");
            DailyReportPojo dailyReportPojo= dailyReportService.get(dateString);
            if (dailyReportPojo==null){
                dailyReportService.add(dateString);
                System.out.println("Debug2");
            }
            DailyReportPojo pojo=new DailyReportPojo();
            pojo.setTotalInvoice(1);
            pojo.setTotalRevenue(0);
            pojo.setTotalItems(invoiceDetails.getItems().size());

            for(InvoiceItem item:invoiceDetails.getItems()){
                pojo.setTotalRevenue(pojo.getTotalRevenue()+ item.getPrice());
            }
            dailyReportService.update(dateString,pojo);
        }
        orderService.changeInvoiceStatus(orderId);
    }
}