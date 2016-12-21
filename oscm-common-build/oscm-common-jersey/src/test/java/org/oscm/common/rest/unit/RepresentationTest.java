/*******************************************************************************
 *                                                                              
 *  Copyright FUJITSU LIMITED 2016                                           
 *                                                                                                                                 
 *  Creation Date: Aug 19, 2016                                                      
 *                                                                              
 *******************************************************************************/

package org.oscm.common.rest.unit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;
import org.oscm.common.interfaces.data.DataType;
import org.oscm.common.interfaces.enums.Operation;
import org.oscm.common.interfaces.exceptions.ServiceException;
import org.oscm.common.rest.Representation;

/**
 * Unit test for Representation
 * 
 * @author miethaner
 */
public class RepresentationTest {

    private static final Long ID = new Long(1L);
    private static final Long ETAG = new Long(2L);
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
            return null;
        }
    }

    private class RepTest extends Representation {

        public RepTest(DataType data) {
            super(data);
        }

        @Override
        public void validateCreate() throws ServiceException {
        }

        @Override
        public void update() {
        }

        @Override
        public void convert() {
        }

        @Override
        public void validateUpdate() throws ServiceException {
        }
    }

    @Test
    public void testRepresentation() throws Exception {

        RepTest test = new RepTest(new DataTest());

        assertEquals(ID, test.getId());
        assertEquals(ETAG, test.getETag());

        test = new RepTest(null);

        assertNull(test.getId());

        test.setId(ID);
        test.setETag(ETAG);
        test.setVersion(VERSION);

        assertEquals(ID, test.getId());
        assertEquals(ETAG, test.getETag());
        assertEquals(VERSION, test.getVersion());

        test.setId(null);
        test.setETag(null);
        test.setVersion(null);

        assertNull(test.getId());
        assertNull(test.getETag());
        assertNull(test.getVersion());
    }
}
