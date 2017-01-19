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

import javax.persistence.EntityExistsException;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityNotFoundException;
import javax.persistence.EntityTransaction;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.oscm.common.hibernate.HibernateManager;
import org.oscm.common.hibernate.DataObject;
import org.oscm.common.hibernate.DataPersistence;
import org.oscm.common.hibernate.ProxyObject;
import org.oscm.common.interfaces.data.Callback;
import org.oscm.common.interfaces.data.DataType;
import org.oscm.common.interfaces.enums.Operation;
import org.oscm.common.interfaces.events.GenericPublisher;
import org.oscm.common.interfaces.exceptions.CacheException;
import org.oscm.common.interfaces.exceptions.ConflictException;
import org.oscm.common.interfaces.exceptions.NotFoundException;
import org.oscm.common.interfaces.exceptions.ServiceException;

/**
 * Unit test for DataPersistence
 * 
 * @author miethaner
 */
public class DataPersistenceTest {

    public interface Subdata extends DataType {
    }

    public class DataTest extends DataPersistence<DOTest> {

        public DataTest() {
            super();
        }

        public DataTest(GenericPublisher<? super DOTest> publisher) {
            super(publisher);
        }

        @Override
        public DOTest createData(DOTest entity, boolean publish)
                throws ServiceException {
            return super.createData(entity, publish);
        }

        @Override
        public DOTest readData(Long id, Long etag) throws ServiceException {
            return super.readData(id, etag);
        }

        @Override
        public List<DOTest> readAllData(String namedQuery,
                Map<String, Object> params, Long limit, Long offset) {
            return super.readAllData(namedQuery, params, limit, offset);
        }

        @Override
        public DOTest readSingleData(String namedQuery,
                Map<String, Object> parameters) throws ServiceException {
            return super.readSingleData(namedQuery, parameters);
        }

        @Override
        public DOTest updateData(DOTest entity, Long etag, boolean publish)
                throws ServiceException {
            return super.updateData(entity, etag, publish);
        }

        @Override
        public DOTest deleteData(Long id, boolean publish)
                throws ServiceException {
            return super.deleteData(id, publish);
        }

        @Override
        public void confirm(Long id) throws ServiceException {
            super.confirm(id);
        }

        @Override
        public <F extends DataObject> F readForeignData(Class<F> foreign,
                Long id) throws ServiceException {
            return super.readForeignData(foreign, id);
        }

        @Override
        public <F extends ProxyObject> F readForeignProxy(Class<F> foreign,
                Long id) throws ServiceException {
            return super.readForeignProxy(foreign, id);
        }

    }

    public class DOTest extends DataObject implements Subdata {
    }

    public interface Publisher extends GenericPublisher<Subdata> {
    }

    public class DPublisher implements Publisher {

        @Override
        public void publish(Subdata content, Callback callback)
                throws ServiceException {
            callback.callback();
        }
    }

    private static EntityManager em;
    private static DataTest test;
    private static DOTest dotest;
    private static DPublisher pub;

    @Before
    public void setup() {
        em = getEntityManager(false);

        dotest = new DOTest();
        pub = new DPublisher();

        test = new DataTest(pub);
    }

    @SuppressWarnings("boxing")
    private EntityManager getEntityManager(boolean exception) {
        EntityManagerFactory emf = Mockito.mock(EntityManagerFactory.class);
        HibernateManager.init(emf);
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
    public void testCreatePositive() throws Exception {

        Mockito.when(em.getReference(Mockito.eq(DOTest.class), Mockito.any()))
                .thenReturn(dotest);

        test.createData(dotest, true);
    }

    @Test
    public void testCreateNegativePersist() throws Exception {

        em = getEntityManager(true);
        test = new DataTest();

        Mockito.doThrow(new EntityExistsException()).when(em).persist(dotest);

        try {
            test.createData(dotest, true);
            fail();
        } catch (ConflictException e) {
        }
    }

    @Test
    public void testReadPositive() throws Exception {

        dotest.setETag(new Long(2L));
        dotest.setLastOperation(Operation.CREATED);
        Mockito.when(em.getReference(DOTest.class, new Long(1L)))
                .thenReturn(dotest);

        DOTest doread = test.readData(new Long(1L), new Long(1L));

        assertEquals(dotest, doread);
    }

    @Test
    public void testReadNegativeEtag() throws Exception {

        dotest.setETag(new Long(2L));
        dotest.setLastOperation(Operation.CREATED);
        Mockito.when(em.getReference(DOTest.class, new Long(1L)))
                .thenReturn(dotest);

        try {
            test.readData(new Long(1L), new Long(2L));
            fail();
        } catch (CacheException e) {
        }
    }

    @Test
    public void testReadNegativeNotFound() throws Exception {

        em = getEntityManager(true);
        test = new DataTest();

        Mockito.when(em.getReference(DOTest.class, new Long(1L)))
                .thenThrow(new EntityNotFoundException());

        try {
            test.readData(new Long(1L), null);
            fail();
        } catch (NotFoundException e) {
        }
    }

    @Test
    public void testReadNegativeDeleted() throws Exception {

        em = getEntityManager(true);
        test = new DataTest();

        dotest.setLastOperation(Operation.DELETED);
        Mockito.when(em.getReference(DOTest.class, new Long(1L)))
                .thenReturn(dotest);

        try {
            test.readData(new Long(1L), null);
            fail();
        } catch (NotFoundException e) {
        }
    }

    @Test
    public void testUpdatePositive() throws Exception {

        dotest.setId(new Long(1L));
        dotest.setETag(new Long(1L));

        DOTest old = new DOTest();
        old.setId(new Long(1L));
        old.setETag(new Long(1L));
        old.setLastOperation(Operation.UPDATED);
        old.setPublished(Boolean.TRUE);

        Mockito.when(em.getReference(DOTest.class, new Long(1L)))
                .thenReturn(old);
        Mockito.when(em.merge(dotest)).thenReturn(dotest);

        test.updateData(dotest, null, true);

        old.setLastOperation(Operation.CREATED);
        old.setPublished(Boolean.FALSE);

        test.updateData(dotest, null, true);

        dotest.setETag(null);

        test.updateData(dotest, null, true);
    }

    @Test
    public void testUpdateNegativeFind() throws Exception {

        em = getEntityManager(true);
        test = new DataTest(pub);

        dotest.setId(new Long(1L));

        Mockito.when(em.getReference(DOTest.class, new Long(1L)))
                .thenThrow(new EntityNotFoundException());

        try {
            test.updateData(dotest, null, true);
            fail();
        } catch (NotFoundException e) {
        }
    }

    @Test
    public void testUpdateNegativeDeleted() throws Exception {

        em = getEntityManager(true);
        test = new DataTest(pub);

        dotest.setId(new Long(1L));
        dotest.setLastOperation(Operation.DELETED);
        dotest.setPublished(Boolean.TRUE);

        Mockito.when(em.getReference(DOTest.class, new Long(1L)))
                .thenReturn(dotest);

        try {
            test.updateData(dotest, null, true);
            fail();
        } catch (NotFoundException e) {
        }
    }

    @Test
    public void testUpdateNegativeEtag() throws Exception {

        em = getEntityManager(true);
        test = new DataTest(pub);

        dotest.setId(new Long(1L));
        dotest.setETag(new Long(1L));

        DOTest old = new DOTest();
        old.setId(new Long(1L));
        old.setETag(new Long(2L));
        old.setLastOperation(Operation.UPDATED);
        old.setPublished(Boolean.TRUE);

        Mockito.when(em.getReference(DOTest.class, new Long(1L)))
                .thenReturn(old);

        try {
            test.updateData(dotest, new Long(1), true);
            fail();
        } catch (CacheException e) {
        }
    }

    @Test
    public void testDeletePositive() throws Exception {

        DOTest old = new DOTest();
        old.setId(new Long(1L));

        Mockito.when(em.getReference(DOTest.class, new Long(1L)))
                .thenReturn(old);

        test.deleteData(new Long(1L), true);
    }

    @Test
    public void testDeleteNegativeFind() throws Exception {

        em = getEntityManager(true);
        test = new DataTest(pub);

        dotest.setId(new Long(1L));

        Mockito.when(em.getReference(DOTest.class, new Long(1L)))
                .thenThrow(new EntityNotFoundException());

        try {
            test.deleteData(new Long(1L), true);
            fail();
        } catch (NotFoundException e) {
        }
    }

    @Test
    public void testDeleteNegativeDeleted() throws Exception {

        em = getEntityManager(true);
        test = new DataTest(pub);

        dotest.setId(new Long(1L));
        dotest.setLastOperation(Operation.DELETED);

        Mockito.when(em.getReference(DOTest.class, new Long(1L)))
                .thenReturn(dotest);

        try {
            test.deleteData(new Long(1L), true);
            fail();
        } catch (NotFoundException e) {
        }
    }
}
