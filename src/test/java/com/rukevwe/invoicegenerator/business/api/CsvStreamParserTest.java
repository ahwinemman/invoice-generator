package com.rukevwe.invoicegenerator.business.api;

import com.rukevwe.invoicegenerator.model.Company;
import com.rukevwe.invoicegenerator.model.CompanyResult;
import com.rukevwe.invoicegenerator.repository.CsvInvoiceRepository;
import org.apache.poi.util.IOUtils;
import org.apache.velocity.app.VelocityEngine;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

public class CsvStreamParserTest {

    public CsvStreamParser csvStreamParser;

    @Mock
    public CsvInvoiceRepository invoiceRepository;

    @Mock
    public VelocityEngine velocityEngine;
    
    public static String invoiceId = "8fc1a8df-235d-47a2-8c04-f5433f67b256";
    

    @Before
    public void setUp() {
        invoiceRepository = new CsvInvoiceRepository();
        
        csvStreamParser = new CsvStreamParser(velocityEngine, invoiceRepository);
    
    }

    @Test
    public void parseCsv() throws IOException, ParseException {
        
        Company company = new Company();
        company.setId(invoiceId);
        company.setName("Google");
        company.setInvoiceItemList(new ArrayList<>());
        
        File file = new File("src/test/resources/sample.csv");
        FileInputStream input = new FileInputStream(file);
        MultipartFile multipartFile = new MockMultipartFile("file",
                file.getName(), "text/plain", IOUtils.toByteArray(input));

        Assert.assertNotNull(multipartFile);
        List<CompanyResult> companyResults =  csvStreamParser.parseCsv(multipartFile);
        
        
        Assert.assertEquals(3, companyResults.size());
        Assert.assertEquals("Google", companyResults.get(0).getName());
        Assert.assertEquals(Double.valueOf(2400), companyResults.get(0).getTotalAmount());
        Assert.assertEquals("Amazon", companyResults.get(1).getName());
        Assert.assertEquals(Double.valueOf(1600), companyResults.get(1).getTotalAmount());
        Assert.assertEquals("Facebook", companyResults.get(2).getName());
        Assert.assertEquals(Double.valueOf(1000), companyResults.get(2).getTotalAmount());
        
    }
    

}
