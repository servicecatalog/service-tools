#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
/*******************************************************************************
 *                                                                              
 *  Copyright FUJITSU LIMITED 2017                                           
 *                                                                                                                                 
 *  Creation Date: Jun 28, 2017                                                      
 *                                                                              
 *******************************************************************************/

package ${package}.interfaces.enums;

import org.oscm.common.interfaces.data.Event;
import org.oscm.common.interfaces.keys.ApplicationKey;
import org.oscm.common.interfaces.keys.EntityKey;
import ${package}.interfaces.data.Sample;

/**
 * Enum for entity keys. Represents entities that this application interacts with.
 */
public enum Entity implements EntityKey {
    SAMPLE("sample", Sample.class);

    private String name;
    private Class<? extends Event> clazz;

    private Entity(String name, Class<? extends Event> clazz) {
        this.name = name;
        this.clazz = clazz;
    }

    @Override
    public String getEntityName() {
        return name;
    }

    @Override
    public Class<? extends Event> getEntityClass() {
        return clazz;
    }

    @Override
    public ApplicationKey getApplication() {
        return Application.SELF;
    }

}
