/*******************************************************************************
 *                                                                              
 *  Copyright FUJITSU LIMITED 2016                                           
 *                                                                                                                                 
 *  Creation Date: Aug 30, 2016                                                      
 *                                                                              
 *******************************************************************************/

package org.oscm.common.util.data;

import org.oscm.common.interfaces.data.DataType;
import org.oscm.common.interfaces.enums.Operation;

/**
 * Superclass for value objects.
 * 
 * @author miethaner
 */
public class ValueObject implements DataType {

    private Long id;
    private Long etag;
    private Operation lastOperation;

    public ValueObject() {
    }

    public ValueObject(DataType data) {
        if (data == null) {
            return;
        }

        this.id = data.getId();
        this.etag = data.getETag();
        this.lastOperation = data.getLastOperation();
    }

    @Override
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public Long getETag() {
        return etag;
    }

    public void setETag(Long etag) {
        this.etag = etag;
    }

    @Override
    public Operation getLastOperation() {
        return lastOperation;
    }

    public void setLastOperation(Operation lastOperation) {
        this.lastOperation = lastOperation;
    }
}
