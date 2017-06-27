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

package ${package}.services;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import org.oscm.common.interfaces.data.Command;
import org.oscm.common.interfaces.data.Event;
import org.oscm.common.interfaces.data.Token;
import org.oscm.common.interfaces.enums.Messages;
import org.oscm.common.interfaces.enums.Operation;
import org.oscm.common.interfaces.events.EventSource;
import org.oscm.common.interfaces.exceptions.ConflictException;
import org.oscm.common.interfaces.exceptions.NotFoundException;
import org.oscm.common.interfaces.exceptions.ServiceException;
import org.oscm.common.interfaces.services.CommandService;
import org.oscm.common.interfaces.services.QueryService;
import org.oscm.common.util.ServiceManager;
import ${package}.interfaces.data.Sample;

/**
 * Service for viewing and updating samples. Please not that this class does not
 * implement any interface directly but each method follows the functional
 * interfaces {@link QueryService} or {@link CommandService}.
 */
public class SampleService {

    @SuppressWarnings("unused")
    public List<Event> read(Event event, Token token) throws ServiceException {

        Sample sample = Sample.class.cast(event);

        EventSource<Sample> source = ServiceManager.getInstance()
                .getEventSource(Sample.class);

        Sample result = source.get(sample.getId());

        if (result == null || result.getOperation() == Operation.DELETE) {
            throw new NotFoundException(Messages.ERROR_NOT_FOUND);
        }

        return Arrays.asList(result);
    }

    @SuppressWarnings("unused")
    public List<Event> readByName(Event event, Token token)
            throws ServiceException {

        Sample sample = Sample.class.cast(event);

        EventSource<Sample> source = ServiceManager.getInstance()
                .getEventSource(Sample.class);

        List<Event> events = Collections.synchronizedList(new ArrayList<>());

        source.getAll().parallelStream()
                .filter((a) -> a.getName().equals(sample.getName()))
                .forEach((a) -> events.add(a));

        return events;
    }

    @SuppressWarnings("unused")
    public List<Event> readAll(Event event, Token token)
            throws ServiceException {

        EventSource<Sample> source = ServiceManager.getInstance()
                .getEventSource(Sample.class);

        return new ArrayList<>(source.getAll());
    }

    public List<Event> create(Command command) throws ServiceException {

        Sample sample = Sample.class.cast(command.getEvent());

        EventSource<Sample> source = ServiceManager.getInstance()
                .getEventSource(Sample.class);

        Sample old = source.get(sample.getId());

        if (old != null) {
            throw new ConflictException(Messages.ERROR_ALREADY_EXISTS);
        }

        long count = source.getAll().parallelStream()
                .filter((a) -> a.getName().equals(sample.getName())).count();

        if (count > 0) {
            throw new ConflictException(Messages.ERROR_NOT_UNIQUE);
        }

        sample.setETag(UUID.randomUUID());
        sample.setOperation(Operation.UPDATE);

        return Arrays.asList(sample);
    }

    public List<Event> update(Command command) throws ServiceException {

        Sample sample = Sample.class.cast(command.getEvent());

        EventSource<Sample> source = ServiceManager.getInstance()
                .getEventSource(Sample.class);

        Sample old = source.get(sample.getId());

        if (old == null || old.getOperation() == Operation.DELETE) {
            throw new NotFoundException(Messages.ERROR_NOT_FOUND);
        }

        old.setName(sample.getName());
        old.setETag(UUID.randomUUID());
        old.setOperation(Operation.UPDATE);

        return Arrays.asList(old);
    }

    public List<Event> delete(Command command) throws ServiceException {

        Sample sample = Sample.class.cast(command.getEvent());

        EventSource<Sample> source = ServiceManager.getInstance()
                .getEventSource(Sample.class);

        Sample old = source.get(sample.getId());

        if (old == null || old.getOperation() == Operation.DELETE) {
            throw new NotFoundException(Messages.ERROR_NOT_FOUND);
        }

        old.setETag(UUID.randomUUID());
        old.setOperation(Operation.DELETE);

        return Arrays.asList(old);
    }
}
