/*******************************************************************************
 *                                                                              
 *  Copyright FUJITSU LIMITED 2016                                           
 *                                                                                                                                 
 *  Creation Date: Aug 19, 2016                                                      
 *                                                                              
 *******************************************************************************/

package org.oscm.common.jpa.unit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.EntityTransaction;

import org.junit.Test;
import org.mockito.Mockito;
import org.oscm.common.interfaces.data.DataType;
import org.oscm.common.interfaces.enums.Operation;
import org.oscm.common.interfaces.exceptions.NotFoundException;
import org.oscm.common.jpa.ProxyObject;
import org.oscm.common.jpa.ProxyPersistence;
import org.oscm.common.jpa.unit.ProxyPersistenceTest.POTest;

/**
 * Unit test for ProxyPersistence
 * 
 * @author miethaner
 */
public class ProxyPersistenceTest extends ProxyPersistence<POTest> {

    public interface Subdata extends DataType {
    }

    public class POTest extends ProxyObject implements Subdata {
    }

    @SuppressWarnings("boxing")
    private EntityManager getEntityManager(boolean exception) {
        EntityManager em = Mockito.mock(EntityManager.class);
        EntityTransaction et = Mockito.mock(EntityTransaction.class);

        Mockito.when(em.getTransaction()).thenReturn(et);

        if (exception) {
            Mockito.when(et.isActive()).thenReturn(true);
        }

        return em;
    }

    @Test
    public void testReadProxyPositive() throws Exception {

        EntityManager em = getEntityManager(false);

        POTest potest = new POTest();
        potest.setLastOperation(Operation.CREATE);
        Mockito.when(em.getReference(POTest.class, new Long(1L))).thenReturn(potest);

        init(em, POTest.class);
        POTest poread = readProxy(new Long(1L));

        assertEquals(potest, poread);
    }

    @Test
    public void testReadProxyNegativeNotFound() throws Exception {

        EntityManager em = getEntityManager(true);

        Mockito.when(em.getReference(POTest.class, new Long(1L))).thenThrow(new EntityNotFoundException());

        try {
            init(em, POTest.class);
            readProxy(new Long(1L));
            fail();
        } catch (NotFoundException e) {
        }
    }

    @Test
    public void testReadProxyNegativeDeleted() throws Exception {

        EntityManager em = getEntityManager(true);

        POTest potest = new POTest();
        potest.setLastOperation(Operation.DELETE);
        Mockito.when(em.getReference(POTest.class, new Long(1L))).thenReturn(potest);

        try {
            init(em, POTest.class);
            readProxy(new Long(1L));
            fail();
        } catch (NotFoundException e) {
        }
    }

    @Test
    public void testMergePositive() throws Exception {

        EntityManager em = getEntityManager(false);

        POTest dotest = new POTest();
        dotest.setId(new Long(1L));
        dotest.setLastOperation(Operation.CREATE);

        init(em, POTest.class);
        mergeProxy(dotest);

        Mockito.verify(em).merge(dotest);

    }
}