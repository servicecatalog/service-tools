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

import javax.persistence.EntityExistsException;
import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.EntityTransaction;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.oscm.common.hibernate.DataObject;
import org.oscm.common.hibernate.DataPersistence;
import org.oscm.common.hibernate.unit.DataPersistenceTest.DOTest;
import org.oscm.common.interfaces.data.DataType;
import org.oscm.common.interfaces.enums.Operation;
import org.oscm.common.interfaces.event.GenericPublisher;
import org.oscm.common.interfaces.exceptions.CacheException;
import org.oscm.common.interfaces.exceptions.ComponentException;
import org.oscm.common.interfaces.exceptions.InternalException;
import org.oscm.common.interfaces.exceptions.NotFoundException;

/**
 * Unit test for DataPersistence
 * 
 * @author miethaner
 */
public class DataPersistenceTest extends DataPersistence<DOTest> {

    public interface Subdata extends DataType {
    }

    public class DOTest extends DataObject implements Subdata {
    }

    public interface Publisher extends GenericPublisher<Subdata> {
    }

    public class DPublisher implements Publisher {

        @Override
        public void publish(Subdata content,
                org.oscm.common.interfaces.event.GenericPublisher.Callback callback)
                throws ComponentException {
            callback.callback();
        }
    }

    private static EntityManager em;
    private static DOTest dotest;
    private static DPublisher pub;

    @Before
    public void setup() {
        em = getEntityManager(false);

        dotest = new DOTest();
        pub = new DPublisher();

        init(em, DOTest.class, pub);
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
    public void testCreatePositive() throws Exception {

        createData(dotest);
    }

    @Test
    public void testCreateNegativePersist() throws Exception {

        em = getEntityManager(true);

        Mockito.doThrow(new EntityExistsException()).when(em).persist(dotest);

        try {
            init(em, DOTest.class, pub);
            createData(dotest);
            fail();
        } catch (InternalException e) {
        }
    }

    @Test
    public void testReadPositive() throws Exception {

        dotest.setETag(new Long(2L));
        dotest.setLastOperation(Operation.CREATED);
        Mockito.when(em.getReference(DOTest.class, new Long(1L)))
                .thenReturn(dotest);

        DOTest doread = readData(new Long(1L), new Long(1L));

        assertEquals(dotest, doread);
    }

    @Test
    public void testReadNegativeEtag() throws Exception {

        dotest.setETag(new Long(2L));
        dotest.setLastOperation(Operation.CREATED);
        Mockito.when(em.getReference(DOTest.class, new Long(1L)))
                .thenReturn(dotest);

        try {
            readData(new Long(1L), new Long(2L));
            fail();
        } catch (CacheException e) {
        }
    }

    @Test
    public void testReadNegativeNotFound() throws Exception {

        em = getEntityManager(true);

        Mockito.when(em.getReference(DOTest.class, new Long(1L)))
                .thenThrow(new EntityNotFoundException());

        try {
            init(em, DOTest.class, pub);
            readData(new Long(1L), null);
            fail();
        } catch (NotFoundException e) {
        }
    }

    @Test
    public void testReadNegativeDeleted() throws Exception {

        em = getEntityManager(true);

        dotest.setLastOperation(Operation.DELETED);
        Mockito.when(em.getReference(DOTest.class, new Long(1L)))
                .thenReturn(dotest);

        try {
            init(em, DOTest.class, pub);
            readData(new Long(1L), null);
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

        updateData(dotest);

        old.setLastOperation(Operation.CREATED);
        old.setPublished(Boolean.FALSE);

        updateData(dotest);

        dotest.setETag(null);

        updateData(dotest);
    }

    @Test
    public void testUpdateNegativeFind() throws Exception {

        em = getEntityManager(true);

        dotest.setId(new Long(1L));

        Mockito.when(em.getReference(DOTest.class, new Long(1L)))
                .thenThrow(new EntityNotFoundException());

        try {
            init(em, DOTest.class, pub);
            updateData(dotest);
            fail();
        } catch (NotFoundException e) {
        }
    }

    @Test
    public void testUpdateNegativeDeleted() throws Exception {

        em = getEntityManager(true);

        dotest.setId(new Long(1L));
        dotest.setLastOperation(Operation.DELETED);
        dotest.setPublished(Boolean.TRUE);

        Mockito.when(em.getReference(DOTest.class, new Long(1L)))
                .thenReturn(dotest);

        try {
            init(em, DOTest.class, pub);
            updateData(dotest);
            fail();
        } catch (NotFoundException e) {
        }
    }

    @Test
    public void testUpdateNegativeEtag() throws Exception {

        em = getEntityManager(true);

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
            init(em, DOTest.class, pub);
            updateData(dotest);
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

        deleteData(new Long(1L));
    }

    @Test
    public void testDeleteNegativeFind() throws Exception {

        em = getEntityManager(true);

        dotest.setId(new Long(1L));

        Mockito.when(em.getReference(DOTest.class, new Long(1L)))
                .thenThrow(new EntityNotFoundException());

        try {
            init(em, DOTest.class, pub);
            deleteData(new Long(1L));
            fail();
        } catch (NotFoundException e) {
        }
    }

    @Test
    public void testDeleteNegativeDeleted() throws Exception {

        em = getEntityManager(true);

        dotest.setId(new Long(1L));
        dotest.setLastOperation(Operation.DELETED);

        Mockito.when(em.getReference(DOTest.class, new Long(1L)))
                .thenReturn(dotest);

        try {
            init(em, DOTest.class, pub);
            deleteData(new Long(1L));
            fail();
        } catch (NotFoundException e) {
        }
    }

    @Override
    public void publishUnpublished() {
        // TODO Auto-generated method stub

    }

    @Override
    public void publishAll() {
        // TODO Auto-generated method stub

    }
}
