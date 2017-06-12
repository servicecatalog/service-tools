/*******************************************************************************
 *                                                                              
 *  Copyright FUJITSU LIMITED 2017                                           
 *                                                                                                                                 
 *  Creation Date: Jun 8, 2017                                                      
 *                                                                              
 *******************************************************************************/

package org.oscm.common.interfaces.data;

import java.util.UUID;

import org.oscm.common.interfaces.enums.Reason;

import com.google.gson.annotations.SerializedName;

/**
 * @author miethaner
 *
 */
public class Failure extends VersionedEntity {

    private static final String FIELD_REASON = "reason";
    private static final String FIELD_REFERENCE_ID = "ref_id";
    private static final String FIELD_CODE = "code";
    private static final String FIELD_PROPERTY = "property";
    private static final String FIELD_MESSAGE = "message";

    @SerializedName(FIELD_REASON)
    private Reason reason;

    @SerializedName(FIELD_REFERENCE_ID)
    private UUID referenceId;

    @SerializedName(FIELD_CODE)
    private Integer code;

    @SerializedName(FIELD_PROPERTY)
    private String property;

    @SerializedName(FIELD_MESSAGE)
    private String message;

    public Reason getReason() {
        return reason;
    }

    public void setReason(Reason reason) {
        this.reason = reason;
    }

    public UUID getReferenceId() {
        return referenceId;
    }

    public void setReferenceId(UUID referenceId) {
        this.referenceId = referenceId;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getProperty() {
        return property;
    }

    public void setProperty(String property) {
        this.property = property;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
