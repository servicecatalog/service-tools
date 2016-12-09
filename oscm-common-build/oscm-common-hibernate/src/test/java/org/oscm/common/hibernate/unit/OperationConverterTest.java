/*******************************************************************************
 *                                                                              
 *  Copyright FUJITSU LIMITED 2016                                           
 *                                                                                                                                 
 *  Creation Date: Sep 30, 2016                                                      
 *                                                                              
 *******************************************************************************/

package org.oscm.common.hibernate.unit;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.oscm.common.hibernate.OperationConverter;
import org.oscm.common.interfaces.enums.Operation;

/**
 * Unit test for OperationConverter
 * 
 * @author miethaner
 */
public class OperationConverterTest {

    @Test
    public void testConvertToValue() {
        OperationConverter oc = new OperationConverter();

        assertEquals(OperationConverter.CREATE_VALUE,
                oc.convertToDatabaseColumn(Operation.CREATED));
        assertEquals(OperationConverter.UPDATE_VALUE,
                oc.convertToDatabaseColumn(Operation.UPDATED));
        assertEquals(OperationConverter.DELETE_VALUE,
                oc.convertToDatabaseColumn(Operation.DELETED));
        assertEquals("", oc.convertToDatabaseColumn(null));
    }

    @Test
    public void testConvertToOperation() {
        OperationConverter oc = new OperationConverter();

        assertEquals(Operation.CREATED,
                oc.convertToEntityAttribute(OperationConverter.CREATE_VALUE));
        assertEquals(Operation.UPDATED,
                oc.convertToEntityAttribute(OperationConverter.UPDATE_VALUE));
        assertEquals(Operation.DELETED,
                oc.convertToEntityAttribute(OperationConverter.DELETE_VALUE));
        assertEquals(null, oc.convertToEntityAttribute(null));
    }

}
