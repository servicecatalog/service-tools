/*******************************************************************************
 *                                                                              
 *  Copyright FUJITSU LIMITED 2016                                           
 *                                                                                                                                 
 *  Creation Date: Aug 19, 2016                                                      
 *                                                                              
 *******************************************************************************/

package org.oscm.common.hibernate.unit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityNotFoundException;
import javax.persistence.EntityTransaction;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.oscm.common.hibernate.ConnectionManager;
import org.oscm.common.hibernate.ProxyObject;
import org.oscm.common.hibernate.ProxyPersistence;
import org.oscm.common.interfaces.data.DataType;
import org.oscm.common.interfaces.enums.Operation;
import org.oscm.common.interfaces.exceptions.NotFoundException;
import org.oscm.common.interfaces.exceptions.ServiceException;

/**
 * Unit test for ProxyPersistence
 * 
 * @author miethaner
 */
public class ProxyPersistenceTest {

    public class ProxyTest extends ProxyPersistence<POTest> {

        @Override
        public POTest readProxy(Long id, Long etag) throws ServiceException {
            return super.readProxy(id, etag);
        }

        @Override
        public List<POTest> readAllProxy(String namedQuery,
                Map<String, Object> params, Long limit, Long offset) {
            return super.readAllProxy(namedQuery, params, limit, offset);
        }

        @Override
        public POTest readSingleProxy(String namedQuery,
                Map<String, Object> parameters) throws ServiceException {
            return super.readSingleProxy(namedQuery, parameters);
        }

        @Override
        public void mergeProxy(POTest entity) throws ServiceException {
            super.mergeProxy(entity);
        }
    }

    public interface Subdata extends DataType {
    }

    public class POTest extends ProxyObject implements Subdata {
    }

    private static EntityManager em;
    private static ProxyTest test;
    private static POTest potest;

    @Before
    public void setup() {
        em = getEntityManager(false);

        potest = new POTest();

        test = new ProxyTest();
    }

    @SuppressWarnings("boxing")
    private EntityManager getEntityManager(boolean exception) {
        EntityManagerFactory emf = Mockito.mock(EntityManagerFactory.class);
        ConnectionManager.init(emf);
        EntityManager em = Mockito.mock(EntityManager.class);
        EntityTransaction et = Mockito.mock(EntityTransaction.class);

        Mockito.when(emf.createEntityManager()).thenReturn(em);
        Mockito.when(em.getTransaction()).thenReturn(et);

        if (exception) {
            Mockito.when(et.isActive()).thenReturn(true);
        }

        return em;
    }

    @Test
    public void testReadProxyPositive() throws Exception {

        potest.setLastOperation(Operation.CREATED);
        Mockito.when(em.getReference(POTest.class, new Long(1L)))
                .thenReturn(potest);

        POTest poread = test.readProxy(new Long(1L), null);

        assertEquals(potest, poread);
    }

    @Test
    public void testReadProxyNegativeNotFound() throws Exception {

        EntityManager em = getEntityManager(true);
        test = new ProxyTest();

        Mockito.when(em.getReference(POTest.class, new Long(1L)))
                .thenThrow(new EntityNotFoundException());

        try {
            test.readProxy(new Long(1L), null);
            fail();
        } catch (NotFoundException e) {
        }
    }

    @Test
    public void testReadProxyNegativeDeleted() throws Exception {

        EntityManager em = getEntityManager(true);
        test = new ProxyTest();

        potest.setLastOperation(Operation.DELETED);
        Mockito.when(em.getReference(POTest.class, new Long(1L)))
                .thenReturn(potest);

        try {
            test.readProxy(new Long(1L), null);
            fail();
        } catch (NotFoundException e) {
        }
    }

    @Test
    public void testMergePositive() throws Exception {

        potest.setId(new Long(1L));
        potest.setLastOperation(Operation.CREATED);

        test.mergeProxy(potest);

        Mockito.verify(em).merge(potest);

    }
}