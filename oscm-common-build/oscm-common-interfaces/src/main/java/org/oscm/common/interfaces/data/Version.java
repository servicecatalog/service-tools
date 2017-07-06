/*******************************************************************************
 *                                                                              
 *  Copyright FUJITSU LIMITED 2017                                           
 *                                                                                                                                 
 *  Creation Date: Jul 6, 2017                                                      
 *                                                                              
 *******************************************************************************/

package org.oscm.common.interfaces.data;

/**
 * Class for version numbers of applications.
 * 
 * @author miethaner
 */
public class Version {

    private int major;
    private int minor;
    private int fix;

    /**
     * Creates a new version from the given components.
     * 
     * @param major
     *            the major version number
     * @param minor
     *            the minor/patch version number
     * @param fix
     *            the fix number
     */
    public Version(int major, int minor, int fix) {
        super();
        this.major = major;
        this.minor = minor;
        this.fix = fix;
    }

    /**
     * Creates a new version from a compiled one.
     * 
     * @param compiled
     *            the compiled version number
     */
    public Version(int compiled) {
        this.major = compiled / 10000;
        this.minor = (compiled % 10000) / 1000;
        this.fix = compiled % 1000;
    }

    /**
     * Gets the major version number.
     * 
     * @return major version number
     */
    public int getMajor() {
        return major;
    }

    /**
     * Gets the minor version number.
     * 
     * @return minor version number
     */
    public int getMinor() {
        return minor;
    }

    /**
     * Gets the fix version number.
     * 
     * @return fix version number
     */
    public int getFix() {
        return fix;
    }

    /**
     * Gets the version number as single integer with major and minor occupying
     * two digits and fix with three. Example: 17.2.42 -> 1702042
     * 
     * @return the compiled number
     */
    public int getCompiledVersion() {
        return major * 100000 + minor * 1000 + fix;
    }

    /**
     * Compares the version number with the given components. Returns negative
     * integer if version is less than the given one, zero if equal and positive
     * if greater.
     * 
     * @param major
     *            the major version number
     * @param minor
     *            the minor version number
     * @param fix
     *            the fix version number
     * @return -1, 0 or 1
     */
    public int compare(int major, int minor, int fix) {

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
     * greater. If the given version is null, zero is returned.
     * 
     * @param version
     *            the version to compare with
     * @return -1, 0 or 1
     */
    public int compare(Version version) {
        if (version == null) {
            return 0;
        }

        return compare(version.getMajor(), version.getMinor(),
                version.getFix());
    }

    @Override
    public boolean equals(Object obj) {
        if (obj != null && obj instanceof Version) {
            Version other = Version.class.cast(obj);

            return compare(other) == 0;
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public String toString() {
        return Integer.toString(getCompiledVersion());
    }
}
