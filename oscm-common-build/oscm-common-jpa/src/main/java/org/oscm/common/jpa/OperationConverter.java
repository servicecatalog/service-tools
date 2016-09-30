/*******************************************************************************
 *                                                                              
 *  Copyright FUJITSU LIMITED 2016                                           
 *                                                                                                                                 
 *  Creation Date: Sep 29, 2016                                                      
 *                                                                              
 *******************************************************************************/

package org.oscm.common.jpa;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

import org.oscm.common.interfaces.enums.Operation;

/**
 * Converter for the Operation enumeration.
 * 
 * @author miethaner
 */
@Converter(autoApply = true)
public class OperationConverter implements AttributeConverter<Operation, String> {

    public static final String CREATE_VALUE = "C";
    public static final String UPDATE_VALUE = "U";
    public static final String DELETE_VALUE = "D";

    @Override
    public String convertToDatabaseColumn(Operation operation) {

        String value = "";

        if (operation == null) {
            return value;
        }

        switch (operation) {
        case CREAT:
            value = CREATE_VALUE;
            break;
        case UPDAT:
            value = UPDATE_VALUE;
            break;
        case DELET:
            value = DELETE_VALUE;
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
            operation = Operation.CREAT;
            break;
        case UPDATE_VALUE:
            operation = Operation.UPDAT;
            break;
        case DELETE_VALUE:
            operation = Operation.DELET;
            break;
        }

        return operation;
    }

}
