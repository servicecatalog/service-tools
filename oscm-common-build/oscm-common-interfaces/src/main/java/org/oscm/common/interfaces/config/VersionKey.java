/*******************************************************************************
 *                                                                              
 *  Copyright FUJITSU LIMITED 2016                                           
 *                                                                                                                                 
 *  Creation Date: Dec 21, 2016                                                      
 *                                                                              
 *******************************************************************************/

package org.oscm.common.interfaces.config;

/**
 * Interface for enums that represent version keys.
 * 
 * @author miethaner
 */
public interface VersionKey {

    /**
     * Gets the name of the version key.
     * 
     * @return the version key name
     */
    public String getKeyName();

    /**
     * Gets the major version number.
     * 
     * @return major version number
     */
    public int getMajor();

    /**
     * Gets the minor version number.
     * 
     * @return minor version number
     */
    public int getMinor();

    /**
     * Gets the fix version number.
     * 
     * @return fix version number
     */
    public int getFix();

    /**
     * Gets the version number as single integer with major and minor occupying
     * two digits and fix with three.
     * 
     * @return the compiled number
     */
    default public int getCompiledVersion() {
        return getMajor() * 100000 + getMinor() * 1000 + getFix();
    }

    /**
     * Compares the version number with the given components. Returns negative
     * integer if version is less than the given one, zero if equal and positive
     * if greater.
     * 
     * 
     * @param major
     *            the major version number
     * @param minor
     *            the minor version number
     * @param fix
     *            the fix version number
     * @return -1, 0 or 1
     */
    default public int compareVersion(int major, int minor, int fix) {

        int version = major * 100000 + minor * 1000 + fix;

        if (getCompiledVersion() > version) {
            return 1;
        } else if (getCompiledVersion() == version) {
            return 0;
        } else {
            return -1;
        }
    }

    /**
     * Compares the version number with the given one. Returns negative integer
     * if version is less than the given one, zero if equal and positive if
     * greater.
     * 
     * 
     * @param version
     *            the version to compare with
     * @return -1, 0 or 1
     */
    default public int compareVersion(VersionKey version) {
        return compareVersion(version.getMajor(), version.getMinor(),
                version.getFix());
    }
}
