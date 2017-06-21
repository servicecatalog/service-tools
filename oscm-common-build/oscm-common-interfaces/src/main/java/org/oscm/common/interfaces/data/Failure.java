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
 * Entity class for failures.
 * 
 * @author miethaner
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

    /**
     * Gets the reason for this failure. Returns null if not set.
     * 
     * @return the reason or null
     */
    public Reason getReason() {
        return reason;
    }

    /**
     * Sets the reason for this failure.
     * 
     * @param reason
     *            the reason
     */
    public void setReason(Reason reason) {
        this.reason = reason;
    }

    /**
     * Gets the unique reference id for this failure. Returns null if not set.
     * 
     * @return the reference id or null
     */
    public UUID getReferenceId() {
        return referenceId;
    }

    /**
     * Sets the unique reference id for this failure.
     * 
     * @param referenceId
     *            the reference id
     */
    public void setReferenceId(UUID referenceId) {
        this.referenceId = referenceId;
    }

    /**
     * Gets the error code for this failure. Returns null if not set.
     * 
     * @return the error code or null
     */
    public Integer getCode() {
        return code;
    }

    /**
     * Sets the error code for this failure.
     * 
     * @param code
     *            the error code
     */
    public void setCode(Integer code) {
        this.code = code;
    }

    /**
     * Gets the responsible property for this failure. Returns null if not set.
     * 
     * @return the property or null
     */
    public String getProperty() {
        return property;
    }

    /**
     * Sets the responsible property for this failure.
     * 
     * @param property
     *            the property
     */
    public void setProperty(String property) {
        this.property = property;
    }

    /**
     * Gets the error message for this failure. Returns null if not set.
     * 
     * @return the message or null
     */
    public String getMessage() {
        return message;
    }

    /**
     * Sets the error message for this failure.
     * 
     * @param message
     *            the error message
     */
    public void setMessage(String message) {
        this.message = message;
    }
}
