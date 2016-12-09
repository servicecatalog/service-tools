/*******************************************************************************
 *                                                                              
 *  Copyright FUJITSU LIMITED 2016                                           
 *                                                                                                                                 
 *  Creation Date: Sep 29, 2016                                                      
 *                                                                              
 *******************************************************************************/

package org.oscm.common.hibernate;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

import org.oscm.common.interfaces.enums.Operation;

/**
 * Converter for the Operation enumeration.
 * 
 * @author miethaner
 */
@Converter(autoApply = true)
public class OperationConverter
        implements AttributeConverter<Operation, String> {

    public static final String CREATE_VALUE = "C";
    public static final String UPDATE_VALUE = "U";
    public static final String DELETE_VALUE = "D";
    public static final String SUSPEND_VALUE = "S";

    @Override
    public String convertToDatabaseColumn(Operation operation) {

        String value = "";

        if (operation == null) {
            return value;
        }

        switch (operation) {
        case CREATED:
            value = CREATE_VALUE;
            break;
        case UPDATED:
            value = UPDATE_VALUE;
            break;
        case DELETED:
            value = DELETE_VALUE;
            break;
        case SUSPENDED:
            value = SUSPEND_VALUE;
            break;
        }

        return value;
    }

    @Override
    public Operation convertToEntityAttribute(String value) {

        Operation operation = null;

        if (value == null) {
            return operation;
        }

        switch (value) {
        case CREATE_VALUE:
            operation = Operation.CREATED;
            break;
        case UPDATE_VALUE:
            operation = Operation.UPDATED;
            break;
        case DELETE_VALUE:
            operation = Operation.DELETED;
            break;
        case SUSPEND_VALUE:
            operation = Operation.SUSPENDED;
            break;
        }

        return operation;
    }

}
