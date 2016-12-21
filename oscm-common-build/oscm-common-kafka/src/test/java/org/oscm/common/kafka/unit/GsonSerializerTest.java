/*******************************************************************************
 *                                                                              
 *  Copyright FUJITSU LIMITED 2016                                           
 *                                                                                                                                 
 *  Creation Date: Dec 20, 2016                                                      
 *                                                                              
 *******************************************************************************/

package org.oscm.common.kafka.unit;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;
import org.oscm.common.kafka.GsonSerializer;
import org.oscm.common.kafka.Representation;

import com.google.gson.annotations.Since;
import com.google.gson.annotations.Until;

/**
 * Unit test for GsonSerializer
 * 
 * @author miethaner
 */
public class GsonSerializerTest {

    private static final long ID = 1L;
    private static final String OLD_ID = "old";
    private static final String NEW_ID = "new";
    private static final String JSON = "{\"id\": \"" + ID + "\", \"id1\": \""
            + OLD_ID + "\"}";
    private static final int V1 = 1;
    private static final int V2 = 2;

    @SuppressWarnings("unused")
    private class MockRepresentation extends Representation {
        @Until(V2)
        private String id1;
        @Since(V2)
        private String id2;

        public String getId1() {
            return id1;
        }

        public void setId1(String id1) {
            this.id1 = id1;
        }

        public String getId2() {
            return id2;
        }

        public void setId2(String id2) {
            this.id2 = id2;
        }
    }

    @Test
    public void testDeserialize() {

        GsonSerializer<MockRepresentation> gs = new GsonSerializer<>(
                MockRepresentation.class);
        MockRepresentation rep = gs.deserialize("topic",
                JSON.getBytes(GsonSerializer.CHARSET));

        assertNotNull(rep);
    }

}
