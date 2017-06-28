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

import org.oscm.common.interfaces.keys.VersionKey;

/**
 * Enum for the versions keys. Describes the history of versions of this service.
 */
public enum Version implements VersionKey {

    V_1_0_0(1, 0, 0);

    public static final Version LATEST = V_1_0_0;

    private int major;
    private int minor;
    private int fix;

    private Version(int major, int minor, int fix) {
        this.major = major;
        this.minor = minor;
        this.fix = fix;
    }

    @Override
    public int getMajor() {
        return major;
    }

    @Override
    public int getMinor() {
        return minor;
    }

    @Override
    public int getFix() {
        return fix;
    }
}