/*******************************************************************************
 *                                                                              
 *  Copyright FUJITSU LIMITED 2016                                           
 *                                                                                                                                 
 *  Creation Date: Sep 30, 2016                                                      
 *                                                                              
 *******************************************************************************/

package org.oscm.common.jpa.unit;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.oscm.common.interfaces.enums.Operation;
import org.oscm.common.jpa.OperationConverter;

/**
 * Unit test for OperationConverter
 * 
 * @author miethaner
 */
public class OperationConverterTest {

    @Test
    public void testConvertToValue() {
        OperationConverter oc = new OperationConverter();

        assertEquals(OperationConverter.CREATE_VALUE, oc.convertToDatabaseColumn(Operation.CREAT));
        assertEquals(OperationConverter.UPDATE_VALUE, oc.convertToDatabaseColumn(Operation.UPDAT));
        assertEquals(OperationConverter.DELETE_VALUE, oc.convertToDatabaseColumn(Operation.DELET));
        assertEquals("", oc.convertToDatabaseColumn(null));
    }

    @Test
    public void testConvertToOperation() {
        OperationConverter oc = new OperationConverter();

        assertEquals(Operation.CREAT, oc.convertToEntityAttribute(OperationConverter.CREATE_VALUE));
        assertEquals(Operation.UPDAT, oc.convertToEntityAttribute(OperationConverter.UPDATE_VALUE));
        assertEquals(Operation.DELET, oc.convertToEntityAttribute(OperationConverter.DELETE_VALUE));
        assertEquals(null, oc.convertToEntityAttribute(null));
    }

}
