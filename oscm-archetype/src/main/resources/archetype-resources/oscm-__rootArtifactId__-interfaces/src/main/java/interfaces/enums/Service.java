#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
/*******************************************************************************
 *                                                                              
 *  Copyright FUJITSU LIMITED 2017                                           
 *                                                                                                                                 
 *  Creation Date: Jun 27, 2017                                                      
 *                                                                              
 *******************************************************************************/

package ${package}.interfaces.enums;

import org.oscm.common.interfaces.keys.ServiceKey;

/**
 * Enum for service keys. These keys are used to describe microservices that
 * this service interacts with directly via commands and queries.
 */
public enum Service implements ServiceKey {
    SAMPLE;

    public static final Service SELF = SAMPLE;

    @Override
    public String getServiceName() {
        return name();
    }

}
