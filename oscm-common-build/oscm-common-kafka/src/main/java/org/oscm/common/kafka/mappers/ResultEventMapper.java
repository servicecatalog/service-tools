/*******************************************************************************
 *                                                                              
 *  Copyright FUJITSU LIMITED 2017                                           
 *                                                                                                                                 
 *  Creation Date: Jun 12, 2017                                                      
 *                                                                              
 *******************************************************************************/

package org.oscm.common.kafka.mappers;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.kstream.KeyValueMapper;
import org.oscm.common.interfaces.data.Event;
import org.oscm.common.interfaces.data.Result;

/**
 * Mapper class for kafka stream pipelines. Extracts the events from a result
 * and returns them.
 * 
 * @author miethaner
 */
public class ResultEventMapper
        implements KeyValueMapper<UUID, Result, List<KeyValue<UUID, Event>>> {

    @Override
    public List<KeyValue<UUID, Event>> apply(UUID key, Result value) {

        List<KeyValue<UUID, Event>> events = new ArrayList<>();

        value.getEvents()
                .forEach((e) -> events.add(KeyValue.pair(e.getId(), e)));

        return events;
    }

}
