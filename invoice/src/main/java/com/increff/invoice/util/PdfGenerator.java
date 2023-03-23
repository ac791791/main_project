package com.increff.invoice.util;

import org.apache.fop.apps.*;
import javax.xml.transform.Result;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.sax.SAXResult;
import javax.xml.transform.stream.StreamSource;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;


public class PdfGenerator {

    public static void createPDF(
    String xslTemplatePath,
    String invoiceXmlPath,
    String outputPdfPath
    ) throws FOPException, TransformerException, IOException {

        File xsltFile = new File(xslTemplatePath);

//        xml file providing the input
        StreamSource xmlSource = new StreamSource(new File(invoiceXmlPath));
        FopFactory fopFactory = FopFactory.newInstance();
        FOUserAgent foUserAgent = fopFactory.newFOUserAgent();

        try(OutputStream out = Files.newOutputStream(Paths.get(outputPdfPath))){
           Fop fop = fopFactory.newFop(MimeConstants.MIME_PDF,foUserAgent,out);
            TransformerFactory factory = TransformerFactory.newInstance();
            Transformer transformer = factory.newTransformer(new StreamSource(xsltFile));
            Result res = new SAXResult(fop.getDefaultHandler());
            transformer.transform(xmlSource,res);
        }
    }
}
