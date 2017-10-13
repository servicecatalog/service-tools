/*******************************************************************************
 *
 *  Copyright FUJITSU LIMITED 2017                                           
 *
 *  Creation Date: Jun 8, 2017                                                      
 *
 *******************************************************************************/

package org.oscm.lagom.data;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.oscm.lagom.enums.Reason;

import java.util.UUID;

/**
 * Entity class for failures.
 */
public class Failure {

    private static final String FIELD_REASON = "reason";
    private static final String FIELD_REFERENCE_ID = "ref_id";
    private static final String FIELD_CODE = "code";
    private static final String FIELD_PROPERTY = "property";
    private static final String FIELD_MESSAGE = "message";

    private Reason reason;

    private UUID referenceId;

    private Integer code;

    private String property;

    private String message;

    /**
     * Creates a new failure.
     *
     * @param reason      the reason for the failure, null returns null
     * @param referenceId the reference id for the failure, null returns null
     * @param code        the error code for the failure, null returns null
     * @param property    the responsible property for the failure, null returns null
     * @param message     the message for the failure, null returns null
     */
    @JsonCreator
    public Failure(@JsonProperty(FIELD_REASON) Reason reason,
        @JsonProperty(FIELD_REFERENCE_ID) UUID referenceId,
        @JsonProperty(FIELD_CODE) Integer code,
        @JsonProperty(FIELD_PROPERTY) String property,
        @JsonProperty(FIELD_MESSAGE) String message) {
        this.reason = reason;
        this.referenceId = referenceId;
        this.code = code;
        this.property = property;
        this.message = message;
    }

    /**
     * Gets the reason for this failure.
     *
     * @return the reason, null if not set
     */
    @JsonProperty(FIELD_REASON)
    public Reason getReason() {
        return reason;
    }

    /**
     * Gets the unique reference id for this failure.
     *
     * @return the reference id, null if not set
     */
    @JsonProperty(FIELD_REFERENCE_ID)
    public UUID getReferenceId() {
        return referenceId;
    }

    /**
     * Gets the error code for this failure.
     *
     * @return the error code, null if not set
     */
    @JsonProperty(FIELD_CODE)
    public Integer getCode() {
        return code;
    }

    /**
     * Gets the responsible property for this failure.
     *
     * @return the property, null if not set
     */
    @JsonProperty(FIELD_PROPERTY)
    public String getProperty() {
        return property;
    }

    /**
     * Gets the error message for this failure.
     *
     * @return the message, null if not set
     */
    @JsonProperty(FIELD_MESSAGE)
    public String getMessage() {
        return message;
    }

}
