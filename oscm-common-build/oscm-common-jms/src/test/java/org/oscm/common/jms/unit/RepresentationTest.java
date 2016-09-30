/*******************************************************************************
 *                                                                              
 *  Copyright FUJITSU LIMITED 2016                                           
 *                                                                                                                                 
 *  Creation Date: Aug 19, 2016                                                      
 *                                                                              
 *******************************************************************************/

package org.oscm.common.jms.unit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;
import org.oscm.common.interfaces.data.DataType;
import org.oscm.common.interfaces.enums.Operation;
import org.oscm.common.jms.Representation;

/**
 * Unit test for Representation
 * 
 * @author miethaner
 */
public class RepresentationTest {

    private static final Long ID = new Long(1L);
    private static final Long ETAG = new Long(2L);
    private static final Operation OP = Operation.CREAT;
    private static final Integer VERSION = new Integer(3);

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

    private class RepTest extends Representation {

        public RepTest(DataType data) {
            super(data);
        }

        @Override
        public void update() {
        }
    }

    @Test
    public void testRepresentation() throws Exception {

        RepTest test = new RepTest(new DataTest());

        assertEquals(ID, test.getId());
        assertEquals(ETAG, test.getETag());
        assertEquals(OP, test.getLastOperation());

        test = new RepTest(null);

        assertNull(test.getId());

        test.setId(ID);
        test.setETag(ETAG);
        test.setVersion(VERSION);
        test.setLastOperation(Operation.CREAT);

        assertEquals(ID, test.getId());
        assertEquals(ETAG, test.getETag());
        assertEquals(VERSION, test.getVersion());
        assertEquals(Operation.CREAT, test.getLastOperation());

        test.setId(null);
        test.setETag(null);
        test.setVersion(null);
        test.setLastOperation(null);

        assertNull(test.getId());
        assertNull(test.getETag());
        assertNull(test.getVersion());
        assertNull(test.getLastOperation());
    }
}
