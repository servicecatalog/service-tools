/*******************************************************************************
 *                                                                              
 *  Copyright FUJITSU LIMITED 2016                                           
 *                                                                                                                                 
 *  Creation Date: Aug 19, 2016                                                      
 *                                                                              
 *******************************************************************************/

package org.oscm.common.jpa.unit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;
import org.oscm.common.interfaces.data.DataType;
import org.oscm.common.interfaces.enums.Operation;
import org.oscm.common.jpa.ProxyObject;

/**
 * Unit test for ProxyObject
 * 
 * @author miethaner
 */
public class ProxyObjectTest {
    private static final Long ID = new Long(1L);
    private static final Long ETAG = new Long(2L);
    private static final Operation OP = Operation.CREAT;

    private class DataTest implements DataType {

        @Override
        public Long getId() {
            return ID;
        }

        @Override
        public Long getETag() {
            return ETAG;
        }

        @Override
        public Operation getLastOperation() {
            return OP;
        }
    }

    private class POTest extends ProxyObject {

        public POTest() {
            super();
        }

        public POTest(DataType data) {
            super(data);
        }
    }

    @Test
    public void testRepresentation() throws Exception {

        POTest test = new POTest(new DataTest());

        assertEquals(ID, test.getId());
        assertEquals(ETAG, test.getETag());
        assertEquals(OP, test.getLastOperation());

        test = new POTest(null);

        assertNull(test.getId());

        test = new POTest();

        test.setId(ID);
        test.setETag(ETAG);
        test.setLastOperation(Operation.CREAT);

        assertEquals(ID, test.getId());
        assertEquals(ETAG, test.getETag());
        assertEquals(Operation.CREAT, test.getLastOperation());

        test.setId(null);
        test.setETag(null);
        test.setLastOperation(null);

        assertNull(test.getId());
        assertNull(test.getETag());
        assertNull(test.getLastOperation());
    }
}
