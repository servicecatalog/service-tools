/*******************************************************************************
 *                                                                              
 *  Copyright FUJITSU LIMITED 2016                                           
 *                                                                                                                                 
 *  Creation Date: Jun 29, 2016                                                      
 *                                                                              
 *******************************************************************************/

package org.oscm.common.hibernate;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.SequenceGenerator;
import javax.persistence.Version;

import org.oscm.common.interfaces.data.DataType;
import org.oscm.common.interfaces.enums.Operation;

/**
 * Super class for all Data Objects
 * 
 * @author miethaner
 */
@MappedSuperclass
public abstract class DataObject implements DataType {

    public static final String FIELD_ID = "id";
    public static final String FIELD_ETAG = "etag";
    public static final String FIELD_LAST_OPERATION = "last_operation";
    public static final String FIELD_PUBLISHED = "published";

    public static final String CLASS_LAST_OPERATION = "org.oscm.common.interfaces.enums.Operation";

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "do_seq")
    @SequenceGenerator(name = "do_seq", allocationSize = 1000)
    @Column(name = FIELD_ID)
    private Long id;

    @Version
    @Column(name = FIELD_ETAG, nullable = false)
    private Long etag;

    @Column(name = FIELD_LAST_OPERATION, nullable = false, length = OperationConverter.LENGTH)
    private Operation lastOperation;

    @Column(name = FIELD_PUBLISHED, nullable = false)
    private Boolean published;

    public DataObject() {
    }

    public DataObject(DataType data) {
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

    public Boolean isPublished() {
        return published;
    }

    public void setPublished(Boolean published) {
        this.published = published;
    }

}
