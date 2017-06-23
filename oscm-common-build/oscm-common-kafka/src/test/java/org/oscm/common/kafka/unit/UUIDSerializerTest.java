/*******************************************************************************
 *                                                                              
 *  Copyright FUJITSU LIMITED 2017                                           
 *                                                                                                                                 
 *  Creation Date: Jun 23, 2017                                                      
 *                                                                              
 *******************************************************************************/

package org.oscm.common.kafka.unit;

import static org.junit.Assert.assertEquals;

import java.util.Collections;
import java.util.UUID;

import org.junit.Test;
import org.oscm.common.kafka.UUIDSerializer;

/**
 * Unit Test for UUIDSerializer
 * 
 * @author miethaner
 */
public class UUIDSerializerTest {

    @Test
    public void testDeSerialize() {

        UUIDSerializer ser = new UUIDSerializer();
        ser.configure(Collections.emptyMap(), true);

        UUID id = UUID.randomUUID();

        byte[] data = ser.serializer().serialize("", id);

        UUID result = ser.deserialize("", data);

        ser.close();

        assertEquals(id, result);
    }
}
