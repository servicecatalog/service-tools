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
import org.oscm.common.interfaces.keys.ConsumerKey;

/**
 * Enum for consumer keys. Represents all consumer the application processes.
 * Each key defines an input {@link Entity}. The backend will use these
 * references for de-/serialization and validation.
 */
public enum Consumer implements ConsumerKey {
    CONSUMER("consumer", Entity.SAMPLE);

    private String name;
    private EntityKey inputEntity;

    private Consumer(String name, EntityKey inputEntity) {
        this.name = name;
        this.inputEntity = inputEntity;
    }

    @Override
    public String getConsumerName() {
        return name;
    }

    @Override
    public EntityKey getInputEntity() {
        return inputEntity;
    }
}
