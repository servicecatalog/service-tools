#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
/*******************************************************************************
 *                                                                              
 *  Copyright FUJITSU LIMITED 2016                                           
 *                                                                                                                                 
 *  Creation Date: Aug 18, 2016                                                      
 *                                                                              
 *******************************************************************************/

package ${package}.interfaces.enums;

import org.oscm.common.interfaces.data.Event;
import org.oscm.common.interfaces.keys.ActivityKey;
import org.oscm.common.interfaces.keys.VersionKey;
import ${package}.interfaces.data.Sample;

/**
 * Enum for activity permission keys
 * 
 * @author miethaner
 */
public enum Activity implements ActivityKey {

    ACTION_READ(Sample.class, Sample.class, Type.QUERY), //

    ACTION_CREATE(Sample.class, Sample.class, Type.COMMAND); //

    public static class Constants {
        public static final String ACTION_READ = "ACTION_READ";
        public static final String ACTION_CREATE = "ACTION_WRITE";
    }

    private Class<? extends Event> inputClass;
    private Class<? extends Event> outputClass;
    private Type type;

    private Activity(Class<? extends Event> inputClass,
            Class<? extends Event> outputClass, Type type) {
        this.inputClass = inputClass;
        this.outputClass = outputClass;
        this.type = type;
    }

    @Override
    public String getActivityName() {
        return name();
    }

    @Override
    public Class<? extends Event> getInputClass() {
        return inputClass;
    }

    @Override
    public Class<? extends Event> getOutputClass() {
        return outputClass;
    }

    @Override
    public Type getType() {
        return type;
    }

    @Override
    public VersionKey getSince() {
        return Version.V_1_0_0;
    }

    @Override
    public VersionKey getUntil() {
        return Version.LATEST;
    }
}
