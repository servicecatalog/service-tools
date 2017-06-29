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

import org.oscm.common.interfaces.keys.ApplicationKey;

/**
 * 
 * 
 * @author miethaner
 */
public enum Application implements ApplicationKey {
    SAMPLE("sample");

    public static final Application SELF = SAMPLE;

    private String name;

    private Application(String name) {
        this.name = name;
    }

    @Override
    public String getApplicationName() {
        return name;
    }

}
