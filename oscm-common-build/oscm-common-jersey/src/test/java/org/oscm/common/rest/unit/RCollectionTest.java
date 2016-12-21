/*******************************************************************************
 *                                                                              
 *  Copyright FUJITSU LIMITED 2016                                           
 *                                                                                                                                 
 *  Creation Date: May 19, 2016                                                      
 *                                                                              
 *******************************************************************************/

package org.oscm.common.rest.unit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;

import javax.ws.rs.WebApplicationException;

import org.junit.Test;
import org.mockito.Mockito;
import org.oscm.common.interfaces.exceptions.ServiceException;
import org.oscm.common.rest.RCollection;
import org.oscm.common.rest.Representation;

/**
 * Unit test for RCollection
 * 
 * @author miethaner
 */
public class RCollectionTest {

    private class RepTest extends Representation {

        @Override
        public void validateCreate() throws WebApplicationException {
        }

        @Override
        public void validateUpdate() throws ServiceException {
        }

    }

    @Test
    public void testCreate() throws Exception {

        RepTest rep = Mockito.spy(new RepTest());

        RCollection<RepTest> coll = new RCollection<>();
        coll.setItems(Arrays.asList(rep));

        coll.setVersion(new Integer(1));

        assertEquals(1, rep.getVersion().intValue());

        coll.convert();

        Mockito.verify(rep).convert();

        coll.update();

        Mockito.verify(rep).update();

        coll.validateCreate();

        Mockito.verify(rep).validateCreate();

        coll = new RCollection<>(Arrays.asList(rep));

        assertTrue(coll.getItems().contains(rep));

    }

}
