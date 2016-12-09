/*******************************************************************************
 *                                                                              
 *  Copyright FUJITSU LIMITED 2016                                           
 *                                                                                                                                 
 *  Creation Date: Aug 19, 2016                                                      
 *                                                                              
 *******************************************************************************/

package org.oscm.common.hibernate.unit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;
import org.oscm.common.hibernate.DataObject;
import org.oscm.common.interfaces.data.DataType;
import org.oscm.common.interfaces.enums.Operation;

/**
 * Unit test for DataObject
 * 
 * @author miethaner
 */
public class DataObjectTest {

    private static final Long ID = new Long(1L);
    private static final Long ETAG = new Long(2L);
    private static final Operation OP = Operation.CREATED;

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

    private class DOTest extends DataObject {

        public DOTest() {
            super();
        }

        public DOTest(DataType data) {
            super(data);
        }
    }

    @Test
    public void testRepresentation() throws Exception {

        DOTest test = new DOTest(new DataTest());

        assertEquals(ID, test.getId());
        assertEquals(ETAG, test.getETag());
        assertEquals(OP, test.getLastOperation());

        test = new DOTest(null);

        assertNull(test.getId());

        test = new DOTest();

        test.setId(ID);
        test.setETag(ETAG);
        test.setLastOperation(Operation.CREATED);
        test.setPublished(Boolean.TRUE);

        assertEquals(ID, test.getId());
        assertEquals(ETAG, test.getETag());
        assertEquals(Operation.CREATED, test.getLastOperation());
        assertEquals(Boolean.TRUE, test.isPublished());

        test.setId(null);
        test.setETag(null);
        test.setLastOperation(null);
        test.setPublished(null);

        assertNull(test.getId());
        assertNull(test.getETag());
        assertNull(test.getLastOperation());
        assertNull(test.isPublished());
    }
}
