package com.example.kafka.streams.poc.mongodb.entity;

import com.example.kafka.streams.poc.domain.entity.address.Address;
import com.example.kafka.streams.poc.domain.entity.purchaseorder.PurchaseOrder;
import com.example.kafka.streams.poc.domain.entity.purchaseorder.PurchaseOrderLine;
import com.example.kafka.streams.poc.domain.entity.member.Member;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Unit test for purchase order mongoDB entity
 */
@SpringBootTest
@RunWith(MockitoJUnitRunner.class)
public class TestPurchaseOrderEntity {

    @Test
    public void testEmptyConstructor() {

        PurchaseOrderEntity purchaseOrderEntity = new PurchaseOrderEntity();

        assertNull(purchaseOrderEntity.getKey());
        assertNull(purchaseOrderEntity.getCountry());
        assertNull(purchaseOrderEntity.getDate());
        assertEquals(0f, purchaseOrderEntity.getTotalAmount(), 0.001);
        assertEquals(0, purchaseOrderEntity.getTotalQuantity());
        assertNotNull(purchaseOrderEntity.getLines());
    }

    @Test
    public void testCopyConstructor() {

        List<PurchaseOrderLine> lines = new ArrayList<>();
        PurchaseOrderLine  line = PurchaseOrderLine.newBuilder().setKey("121").build();
        lines.add(line);

        PurchaseOrder purchaseOrder = new PurchaseOrder("101", "102", new Date(103), 104f, 105, lines);
        PurchaseOrderEntity purchaseOrderEntity = new PurchaseOrderEntity(purchaseOrder);

        assertEquals("101", purchaseOrderEntity.getKey());
        assertEquals("102", purchaseOrderEntity.getCountry());
        assertEquals(103, purchaseOrderEntity.getDate().getTime());
        assertEquals(104f, purchaseOrderEntity.getTotalAmount(), 0.001);
        assertEquals(105, purchaseOrderEntity.getTotalQuantity());
        assertEquals("121", purchaseOrderEntity.getLines().get(0).getKey());
    }
}
