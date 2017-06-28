/*******************************************************************************
 *                                                                              
 *  Copyright FUJITSU LIMITED 2017                                           
 *                                                                                                                                 
 *  Creation Date: Jun 12, 2017                                                      
 *                                                                              
 *******************************************************************************/

package org.oscm.common.kafka.mappers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.kstream.KeyValueMapper;
import org.oscm.common.interfaces.data.Command;
import org.oscm.common.interfaces.data.Event;
import org.oscm.common.interfaces.data.Result;
import org.oscm.common.interfaces.enums.Status;
import org.oscm.common.interfaces.events.CommandPublisher;
import org.oscm.common.interfaces.exceptions.ServiceException;
import org.oscm.common.interfaces.keys.ActivityKey;
import org.oscm.common.interfaces.services.CommandService;
import org.oscm.common.util.ConfigurationManager;
import org.oscm.common.util.ServiceManager;
import org.oscm.common.util.logger.ServiceLogger;

/**
 * Mapper class for kafka stream pipelines. Processes commands according to
 * their keys via services and returns the result.
 * 
 * @author miethaner
 */
public class CommandResultMapper
        implements KeyValueMapper<UUID, Command, List<KeyValue<UUID, Result>>> {

    private static final ServiceLogger LOGGER = ServiceLogger
            .getLogger(CommandResultMapper.class);

    private ActivityKey command;
    private CommandService service;
    private CommandPublisher publisher;

    public CommandResultMapper(ActivityKey command) {
        this.command = command;

        ServiceManager sm = ServiceManager.getInstance();
        this.service = sm.getCommandService(command);
        this.publisher = sm
                .getPublisher(ConfigurationManager.getInstance().getSelf());
    }

    @Override
    public List<KeyValue<UUID, Result>> apply(UUID key, Command value) {

        if (value == null || !command.equals(value.getCommand())
                || publisher.getResult(key) != null) {
            return new ArrayList<>();
        }

        Result result = new Result();
        result.setId(key);
        result.setCommand(value.getCommand());

        try {
            List<Event> events = service.execute(value);

            result.setEvents(events);
            result.setStatus(Status.SUCCESS);
        } catch (ServiceException e) {
            LOGGER.error(e);
            result.setStatus(Status.FAILURE);
            result.setFailure(e.getAsFailure());
        }

        result.setTimestamp(new Date());

        return Arrays.asList(KeyValue.pair(result.getId(), result));
    }
}
