package com.example.kafka.streams.poc.mongodb.entity;

import com.example.kafka.streams.poc.domain.entity.warehouse.WarehouseOrderLine;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Date;

/**
 * Unit test for warehouse order line matched mongoDB entity
 */
@SpringBootTest
@RunWith(MockitoJUnitRunner.class)
public class TestWarehouseOrderLineMatchedEntity {
    @Test
    public void testEmptyConstructor() {

        WarehouseOrderLineEntity source = new WarehouseOrderLineEntity();
        WarehouseOrderLineMatchedEntity target = new WarehouseOrderLineMatchedEntity();

        Assert.assertEquals(source.getUuid(), target.getUuid());
        Assert.assertEquals(source.getCountry(), target.getCountry());
        Assert.assertEquals(source.getDate(), target.getDate());
        Assert.assertEquals(source.getProductUuid(), target.getProductUuid());
        Assert.assertEquals(source.getProductLegacyId(), target.getProductLegacyId());
        Assert.assertEquals(source.getProductName(), target.getProductName());
        Assert.assertEquals(source.getProductBarCode(), target.getProductBarCode());
        Assert.assertEquals(source.getQuantity(), target.getQuantity());
    }

    @Test
    public void testCopyConstructor() {

        WarehouseOrderLine line = new WarehouseOrderLine(
                "101",
                "102",
                new Date(103L),
                "104",
                105,
                "106",
                "107",
                108
        );

        WarehouseOrderLineEntity source = new WarehouseOrderLineEntity(line);
        WarehouseOrderLineMatchedEntity target = new WarehouseOrderLineMatchedEntity(line);

        Assert.assertEquals(source.getUuid(), target.getUuid());
        Assert.assertEquals(source.getCountry(), target.getCountry());
        Assert.assertEquals(source.getDate(), target.getDate());
        Assert.assertEquals(source.getProductUuid(), target.getProductUuid());
        Assert.assertEquals(source.getProductLegacyId(), target.getProductLegacyId());
        Assert.assertEquals(source.getProductName(), target.getProductName());
        Assert.assertEquals(source.getProductBarCode(), target.getProductBarCode());
        Assert.assertEquals(source.getQuantity(), target.getQuantity());
    }
}
