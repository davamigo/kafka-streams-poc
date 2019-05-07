package com.example.kafka.streams.poc.domain.entity.product;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.Assert.*;

/**
 * Unit test for Product.AvroExporter class
 */
@SpringBootTest
@RunWith(MockitoJUnitRunner.class)
public class TestProductExporter {

    @Test
    public void testExportProductToAvro() {

        Product sourceProduct = new Product("101", "102", "103", "104", 105.0f);

        com.example.kafka.streams.poc.schemas.product.Product avroProduct
                = Product.newAvroExporter(sourceProduct).export();

        assertNotNull(avroProduct);
        assertEquals("101", avroProduct.getUuid());
        assertEquals("102", avroProduct.getName());
        assertEquals("103", avroProduct.getType());
        assertEquals("104", avroProduct.getBarCode());
        assertEquals(105f, avroProduct.getPrice(), 0.001);
    }
}
