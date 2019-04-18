package com.example.kafka.streams.poc.domain.entity.order;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;

/**
 * Unit test for CommercialOrderLine domain entity
 */
@SpringBootTest
@RunWith(MockitoJUnitRunner.class)
public class TestCommercialOrderLine {

    @Test
    public void testDefaultConstructor() {
        CommercialOrderLine line = new CommercialOrderLine();

        assertNull(line.getUuid());
        assertNull(line.getCommercialOrderUuid());
        assertNull(line.getProductUuid());
        assertEquals(0f, line.getPrice(), 0.001);
        assertEquals(1, line.getQuantity());
    }

    @Test
    public void testCompleteConstructor() {
        CommercialOrderLine line = new CommercialOrderLine("101", "102", "103", 104f, 105);

        assertEquals("101", line.getUuid());
        assertEquals("102", line.getCommercialOrderUuid());
        assertEquals("103", line.getProductUuid());
        assertEquals(104f, line.getPrice(), 0.001);
        assertEquals(105, line.getQuantity());
    }

    @Test
    public void testTwoLinesAreEqualWhenTheyHaveTheSameUuid() {
        CommercialOrderLine line1 = new CommercialOrderLine("201", "202", "203", 204f, 205);
        CommercialOrderLine line2 = new CommercialOrderLine("201", "212", "213", 214f, 215);

        assertEquals(line1, line2);
        assertNotSame(line1, line2);
    }

    @Test
    public void testTwoCommercialOrderLinesAreDifferentWhenTheyHaveDifferentUuid() {
        CommercialOrderLine line1 = new CommercialOrderLine("301", "302", "303", 304f, 305);
        CommercialOrderLine line2 = new CommercialOrderLine("401", "302", "303", 304f, 305);

        assertNotEquals(line1, line2);
    }

    @Test
    public void testEqualsWithNonCommercialOrderLine() {
        CommercialOrderLine commercialOrderLine = new CommercialOrderLine();

        assertNotEquals(commercialOrderLine, new String());
    }
}
