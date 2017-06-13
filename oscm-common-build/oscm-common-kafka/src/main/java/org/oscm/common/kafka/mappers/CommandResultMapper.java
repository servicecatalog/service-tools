/*******************************************************************************
 *                                                                              
 *  Copyright FUJITSU LIMITED 2017                                           
 *                                                                                                                                 
 *  Creation Date: Jun 12, 2017                                                      
 *                                                                              
 *******************************************************************************/

package org.oscm.common.kafka.mappers;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.kstream.KeyValueMapper;
import org.oscm.common.interfaces.data.Command;
import org.oscm.common.interfaces.data.Event;
import org.oscm.common.interfaces.data.Result;
import org.oscm.common.interfaces.enums.Status;
import org.oscm.common.interfaces.exceptions.ServiceException;
import org.oscm.common.interfaces.services.CommandService;
import org.oscm.common.util.ServiceManager;
import org.oscm.common.util.logger.ServiceLogger;

/**
 * @author miethaner
 *
 */
public class CommandResultMapper
        implements KeyValueMapper<UUID, Command, KeyValue<UUID, Result>> {

    private static final ServiceLogger LOGGER = ServiceLogger
            .getLogger(CommandResultMapper.class);

    private String serviceName;

    public CommandResultMapper(String serviceName) {
        this.serviceName = serviceName;
    }

    @Override
    public KeyValue<UUID, Result> apply(UUID key, Command value) {

        Result result = new Result();
        result.setId(UUID.randomUUID());
        result.setParent(value.getId());
        result.setCommand(value.getCommand());

        try {
            CommandService service = ServiceManager.getInstance()
                    .getService(serviceName);
            List<Event> events = service.process(value);

            result.setEvents(events);
            result.setStatus(Status.SUCCESS);
        } catch (ServiceException e) {
            LOGGER.error(e);
            result.setStatus(Status.FAILURE);
            result.setFailure(e.getAsFailure());
        }

        result.setTimestamp(new Date());

        return KeyValue.pair(result.getId(), result);
    }
}
