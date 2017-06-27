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

import org.oscm.common.interfaces.data.Event;
import org.oscm.common.interfaces.keys.ActivityKey;
import org.oscm.common.interfaces.keys.VersionKey;import ${package}.interfaces.data.Sample;

/**
 * Enum for activity keys. These keys represent all commands and queries that
 * can be called over the frontend. Each key defines an input event and an
 * output event, e.g. the {@link Sample} class. The frontend and backend will
 * use these references for de-/serialization and validation. Also a version
 * window can be defined for filtering at the frontend.
 */
public enum Activity implements ActivityKey {

    SAMPLE_READ(Sample.class, Sample.class, Type.QUERY), //
    SAMPLE_READ_BY_NAME(Sample.class, Sample.class, Type.QUERY), //
    SAMPLE_READ_ALL(Sample.class, Sample.class, Type.QUERY), //

    SAMPLE_CREATE(Sample.class, Sample.class, Type.COMMAND), //
    SAMPLE_UPDATE(Sample.class, Sample.class, Type.COMMAND), //
    SAMPLE_DELETE(Sample.class, Sample.class, Type.COMMAND); //

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
