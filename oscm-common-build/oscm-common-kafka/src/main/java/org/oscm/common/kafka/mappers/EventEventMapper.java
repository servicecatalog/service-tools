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
import org.oscm.common.interfaces.exceptions.ServiceException;
import org.oscm.common.interfaces.services.EventService;
import org.oscm.common.util.logger.ServiceLogger;

/**
 * @author miethaner
 *
 */
public class EventEventMapper
        implements KeyValueMapper<UUID, Event, List<KeyValue<UUID, Event>>> {

    private static final ServiceLogger LOGGER = ServiceLogger
            .getLogger(EventEventMapper.class);

    private EventService service;

    public EventEventMapper(EventService service) {
        this.service = service;
    }

    @Override
    public List<KeyValue<UUID, Event>> apply(UUID key, Event value) {

        List<KeyValue<UUID, Event>> eventList = new ArrayList<>();

        try {
            List<Event> events = service.process(value);

            events.forEach((e) -> eventList.add(KeyValue.pair(e.getId(), e)));
        } catch (ServiceException e) {
            LOGGER.error(e);
        }

        return eventList;
    }
}
