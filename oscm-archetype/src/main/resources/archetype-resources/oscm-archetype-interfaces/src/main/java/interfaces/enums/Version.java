#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
/*******************************************************************************
 *                                                                              
 *  Copyright FUJITSU LIMITED 2016                                           
 *                                                                                                                                 
 *  Creation Date: Dec 21, 2016                                                      
 *                                                                              
 *******************************************************************************/

package ${package}.interfaces.enums;

import org.oscm.common.interfaces.keys.VersionKey;

/**
 * Enum for the versions of this service.
 * 
 * @author miethaner
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
