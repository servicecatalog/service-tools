/*******************************************************************************
 *                                                                              
 *  Copyright FUJITSU LIMITED 2016                                           
 *                                                                                                                                 
 *  Creation Date: Jul 4, 2016                                                      
 *                                                                              
 *******************************************************************************/

package org.oscm.common.jpa;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

import org.oscm.common.interfaces.data.DataType;
import org.oscm.common.interfaces.enums.Operation;

/**
 * Super class for all Proxy Objects
 * 
 * @author miethaner
 */
@MappedSuperclass
public abstract class ProxyObject implements DataType {

    public static final String FIELD_ID = "id";
    public static final String FIELD_ETAG = "etag";
    public static final String FIELD_LAST_OPERATION = "last_operation";

    public static final String CLASS_LAST_OPERATION = "org.oscm.common.interfaces.enums.Operation";

    @Id
    @Column(name = FIELD_ID)
    private Long id;

    @Column(name = FIELD_ETAG, nullable = false)
    private Long etag;

    @Column(name = FIELD_LAST_OPERATION, nullable = false)
    private Operation lastOperation;

    public ProxyObject() {
    }

    public ProxyObject(DataType proxy) {
        if (proxy == null) {
            return;
        }

        this.id = proxy.getId();
        this.etag = proxy.getETag();
        this.lastOperation = proxy.getLastOperation();
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
