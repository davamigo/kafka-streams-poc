package com.example.kafka.streams.poc.service.api;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * Unit test for FakeLegacyProductIdsApi service
 */
@SpringBootTest
@RunWith(MockitoJUnitRunner.class)
public class FakeLegacyProductIdsApiTest {

    @Test
    public void testGetLegacyIdWhenFound() {

        FakeLegacyProductIdsApi api = new FakeLegacyProductIdsApi(0, 0);

        Integer result = api.getLegacyId("101").orElse(null);

        Assert.assertNotNull(result);
        Assert.assertTrue(result >= 100000);
        Assert.assertTrue(result <= 999999);
    }

    @Test
    public void testGetLegacyIdWhenNotFound() {

        FakeLegacyProductIdsApi api = new FakeLegacyProductIdsApi(0, 100);

        Integer result = api.getLegacyId("201").orElse(null);

        Assert.assertNull(result);
    }

    @Test
    public void testGetLegacyIdSecondCallReturnsSameNum() {

        FakeLegacyProductIdsApi api = new FakeLegacyProductIdsApi(0, 0);

        Integer result1 = api.getLegacyId("201").orElse(null);
        Integer result2 = api.getLegacyId("201").orElse(null);

        Assert.assertNotNull(result1);
        Assert.assertEquals(result1, result2);
    }
}
