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

import org.oscm.common.interfaces.keys.ActivityKey;
import org.oscm.common.interfaces.keys.ApplicationKey;
import org.oscm.common.interfaces.keys.EntityKey;
import org.oscm.common.interfaces.keys.VersionKey;
import ${package}.interfaces.data.Sample;

/**
 * Enum for activity keys. These keys represent all commands and queries that
 * can be called over the frontend. Each key defines an input event and an
 * output event, e.g. the {@link Sample} class. The frontend and backend will
 * use these references for de-/serialization and validation. Also a version
 * window can be defined for filtering at the frontend.
 */
public enum Activity implements ActivityKey {

    SAMPLE_READ("read-sample", Entity.SAMPLE, Entity.SAMPLE, Type.QUERY), //
    SAMPLE_READ_BY_NAME("read-sample-by-name", Entity.SAMPLE, Entity.SAMPLE,
            Type.QUERY), //
    SAMPLE_READ_ALL("read-all-samples", Entity.SAMPLE, Entity.SAMPLE,
            Type.QUERY), //

    SAMPLE_CREATE("create-sample", Entity.SAMPLE, Entity.SAMPLE, Type.COMMAND), //
    SAMPLE_UPDATE("update-sample", Entity.SAMPLE, Entity.SAMPLE, Type.COMMAND), //
    SAMPLE_DELETE("delete-sample", Entity.SAMPLE, Entity.SAMPLE, Type.COMMAND); //

    private String name;
    private EntityKey inputEntity;
    private EntityKey outputEntity;
    private Type type;

    private Activity(String name, EntityKey inputEntity, EntityKey outputEntity,
            Type type) {
        this.name = name;
        this.inputEntity = inputEntity;
        this.outputEntity = outputEntity;
        this.type = type;
    }

    @Override
    public String getActivityName() {
        return name;
    }

    @Override
    public EntityKey getInputEntity() {
        return inputEntity;
    }

    @Override
    public EntityKey getOutputEntity() {
        return outputEntity;
    }

    @Override
    public ApplicationKey getApplication() {
        return Application.SELF;
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
