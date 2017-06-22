#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
/*******************************************************************************
 *                                                                              
 *  Copyright FUJITSU LIMITED 2017                                           
 *                                                                                                                                 
 *  Creation Date: Jun 21, 2017                                                      
 *                                                                              
 *******************************************************************************/

package ${package}.interfaces.enums;

import org.oscm.common.interfaces.keys.ServiceKey;

/**
 * @author miethaner
 *
 */
public enum Service implements ServiceKey {
    SAMPLE;

    public static final Service SELF = SAMPLE;

    @Override
    public String getServiceName() {
        return name();
    }

}
