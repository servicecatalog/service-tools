/*******************************************************************************
 *                                                                              
 *  Copyright FUJITSU LIMITED 2016                                           
 *                                                                                                                                 
 *  Creation Date: Aug 19, 2016                                                      
 *                                                                              
 *******************************************************************************/

package org.oscm.common.util.unit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;
import org.oscm.common.interfaces.data.DataType;
import org.oscm.common.interfaces.enums.Operation;
import org.oscm.common.util.ValueObject;

/**
 * Unit test for DataObject
 * 
 * @author miethaner
 */
public class ValueObjectTest {

    private static final Long ID = new Long(1L);
    private static final Long ETAG = new Long(2L);
    private static final Operation OP = Operation.CREATE;

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

    private class VOTest extends ValueObject {

        public VOTest() {
            super();
        }

        public VOTest(DataType data) {
            super(data);
        }
    }

    @Test
    public void testRepresentation() throws Exception {

        VOTest test = new VOTest(new DataTest());

        assertEquals(ID, test.getId());
        assertEquals(ETAG, test.getETag());
        assertEquals(OP, test.getLastOperation());

        test = new VOTest(null);

        assertNull(test.getId());

        test = new VOTest();

        test.setId(ID);
        test.setETag(ETAG);
        test.setLastOperation(OP);

        assertEquals(ID, test.getId());
        assertEquals(ETAG, test.getETag());
        assertEquals(OP, test.getLastOperation());

        test.setId(null);
        test.setETag(null);
        test.setLastOperation(null);

        assertNull(test.getId());
        assertNull(test.getETag());
        assertNull(test.getLastOperation());
    }
}
