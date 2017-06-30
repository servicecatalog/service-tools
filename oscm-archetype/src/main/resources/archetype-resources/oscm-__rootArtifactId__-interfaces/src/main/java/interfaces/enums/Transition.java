#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
/*******************************************************************************
 *                                                                              
 *  Copyright FUJITSU LIMITED 2017                                           
 *                                                                                                                                 
 *  Creation Date: Jun 29, 2017                                                      
 *                                                                              
 *******************************************************************************/

package ${package}.interfaces.enums;

import org.oscm.common.interfaces.keys.EntityKey;
import org.oscm.common.interfaces.keys.TransitionKey;

/**
 * Enum for transition keys. Represents all transitions the application
 * processes. Each key defines an input {@link Entity} and an output entity. The
 * backend will use these references for de-/serialization and validation.
 */
public enum Transition implements TransitionKey {
    TRANSITION("transition", Entity.SAMPLE, Entity.SAMPLE);

    private String name;
    private EntityKey inputEntity;
    private EntityKey outputEntity;

    private Transition(String name, EntityKey inputEntity,
            EntityKey outputEntity) {
        this.name = name;
        this.inputEntity = inputEntity;
        this.outputEntity = outputEntity;
    }

    @Override
    public String getTransitionName() {
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

}
