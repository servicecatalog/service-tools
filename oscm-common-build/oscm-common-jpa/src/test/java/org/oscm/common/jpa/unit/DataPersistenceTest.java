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

import javax.persistence.EntityExistsException;
import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.EntityTransaction;

import org.junit.Test;
import org.mockito.Mockito;
import org.oscm.common.interfaces.data.DataType;
import org.oscm.common.interfaces.enums.Operation;
import org.oscm.common.interfaces.exceptions.CacheException;
import org.oscm.common.interfaces.exceptions.InternalException;
import org.oscm.common.interfaces.exceptions.NotFoundException;
import org.oscm.common.jpa.DataObject;
import org.oscm.common.jpa.DataPersistence;
import org.oscm.common.jpa.ProxyObject;
import org.oscm.common.jpa.unit.DataPersistenceTest.DOTest;

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
    public void testCreatePositive() throws Exception {

        EntityManager em = getEntityManager(false);

        DOTest dotest = new DOTest();

        init(em, DOTest.class);
        createData(dotest);
    }

    @Test
    public void testCreateNegativePersist() throws Exception {

        EntityManager em = getEntityManager(true);

        DOTest dotest = new DOTest();

        Mockito.doThrow(new EntityExistsException()).when(em).persist(dotest);

        try {
            init(em, DOTest.class);
            createData(dotest);
            fail();
        } catch (InternalException e) {
        }
    }

    @Test
    public void testReadPositive() throws Exception {

        EntityManager em = getEntityManager(false);

        DOTest dotest = new DOTest();
        dotest.setETag(new Long(2L));
        dotest.setLastOperation(Operation.CREATE);
        Mockito.when(em.getReference(DOTest.class, new Long(1L))).thenReturn(dotest);

        init(em, DOTest.class);
        DOTest doread = readData(new Long(1L), new Long(1L));

        assertEquals(dotest, doread);
    }

    @Test
    public void testReadNegativeEtag() throws Exception {

        EntityManager em = getEntityManager(false);

        DOTest dotest = new DOTest();
        dotest.setETag(new Long(2L));
        dotest.setLastOperation(Operation.CREATE);
        Mockito.when(em.getReference(DOTest.class, new Long(1L))).thenReturn(dotest);

        try {
            init(em, DOTest.class);
            readData(new Long(1L), new Long(2L));
            fail();
        } catch (CacheException e) {
        }
    }

    @Test
    public void testReadNegativeNotFound() throws Exception {

        EntityManager em = getEntityManager(true);

        Mockito.when(em.getReference(DOTest.class, new Long(1L))).thenThrow(new EntityNotFoundException());

        try {
            init(em, DOTest.class);
            readData(new Long(1L), null);
            fail();
        } catch (NotFoundException e) {
        }
    }

    @Test
    public void testReadNegativeDeleted() throws Exception {

        EntityManager em = getEntityManager(true);

        DOTest dotest = new DOTest();
        dotest.setLastOperation(Operation.DELETE);
        Mockito.when(em.getReference(DOTest.class, new Long(1L))).thenReturn(dotest);

        try {
            init(em, DOTest.class);
            readData(new Long(1L), null);
            fail();
        } catch (NotFoundException e) {
        }
    }

    @Test
    public void testUpdatePositive() throws Exception {

        EntityManager em = getEntityManager(false);

        DOTest dotest = new DOTest();
        dotest.setId(new Long(1L));
        dotest.setETag(new Long(1L));

        DOTest old = new DOTest();
        old.setId(new Long(1L));
        old.setETag(new Long(1L));
        old.setLastOperation(Operation.UPDATE);
        old.setPublished(Boolean.TRUE);

        Mockito.when(em.getReference(DOTest.class, new Long(1L))).thenReturn(old);

        init(em, DOTest.class);
        updateData(dotest);

        old.setLastOperation(Operation.CREATE);
        old.setPublished(Boolean.FALSE);

        updateData(dotest);

        dotest.setETag(null);

        updateData(dotest);
    }

    @Test
    public void testUpdateNegativeFind() throws Exception {

        EntityManager em = getEntityManager(true);

        DOTest dotest = new DOTest();
        dotest.setId(new Long(1L));

        Mockito.when(em.getReference(DOTest.class, new Long(1L))).thenThrow(new EntityNotFoundException());

        try {
            init(em, DOTest.class);
            updateData(dotest);
            fail();
        } catch (NotFoundException e) {
        }
    }

    @Test
    public void testUpdateNegativeDeleted() throws Exception {

        EntityManager em = getEntityManager(true);

        DOTest dotest = new DOTest();
        dotest.setId(new Long(1L));
        dotest.setLastOperation(Operation.DELETE);
        dotest.setPublished(Boolean.TRUE);

        Mockito.when(em.getReference(DOTest.class, new Long(1L))).thenReturn(dotest);

        try {
            init(em, DOTest.class);
            updateData(dotest);
            fail();
        } catch (NotFoundException e) {
        }
    }

    @Test
    public void testUpdateNegativeEtag() throws Exception {

        EntityManager em = getEntityManager(true);

        DOTest dotest = new DOTest();
        dotest.setId(new Long(1L));
        dotest.setETag(new Long(1L));

        DOTest old = new DOTest();
        old.setId(new Long(1L));
        old.setETag(new Long(2L));
        old.setLastOperation(Operation.UPDATE);
        old.setPublished(Boolean.TRUE);

        Mockito.when(em.getReference(DOTest.class, new Long(1L))).thenReturn(old);

        try {
            init(em, DOTest.class);
            updateData(dotest);
            fail();
        } catch (CacheException e) {
        }
    }

    @Test
    public void testDeletePositive() throws Exception {

        EntityManager em = getEntityManager(false);

        DOTest old = new DOTest();
        old.setId(new Long(1L));

        Mockito.when(em.getReference(DOTest.class, new Long(1L))).thenReturn(old);

        init(em, DOTest.class);
        deleteData(new Long(1L));
    }

    @Test
    public void testDeleteNegativeFind() throws Exception {

        EntityManager em = getEntityManager(true);

        DOTest dotest = new DOTest();
        dotest.setId(new Long(1L));

        Mockito.when(em.getReference(DOTest.class, new Long(1L))).thenThrow(new EntityNotFoundException());

        try {
            init(em, DOTest.class);
            deleteData(new Long(1L));
            fail();
        } catch (NotFoundException e) {
        }
    }

    @Test
    public void testDeleteNegativeDeleted() throws Exception {

        EntityManager em = getEntityManager(true);

        DOTest dotest = new DOTest();
        dotest.setId(new Long(1L));
        dotest.setLastOperation(Operation.DELETE);

        Mockito.when(em.getReference(DOTest.class, new Long(1L))).thenReturn(dotest);

        try {
            init(em, DOTest.class);
            deleteData(new Long(1L));
            fail();
        } catch (NotFoundException e) {
        }
    }

    @Test
    public void testConfirmPositive() throws Exception {

        EntityManager em = getEntityManager(false);

        DOTest dotest = new DOTest();
        dotest.setId(new Long(1L));
        dotest.setLastOperation(Operation.UPDATE);

        Mockito.when(em.getReference(DOTest.class, new Long(1L))).thenReturn(dotest);

        init(em, DOTest.class);
        confirm(new Long(1L));

        assertEquals(Boolean.TRUE, dotest.isPublished());

        dotest.setLastOperation(Operation.DELETE);

        confirm(new Long(1L));

        Mockito.verify(em).remove(dotest);
    }

    @Test
    public void testConfirmNegativeFind() throws Exception {

        EntityManager em = getEntityManager(true);

        DOTest dotest = new DOTest();
        dotest.setId(new Long(1L));

        Mockito.when(em.getReference(DOTest.class, new Long(1L))).thenThrow(new EntityNotFoundException());

        try {
            init(em, DOTest.class);
            confirm(new Long(1L));
            fail();
        } catch (NotFoundException e) {
        }
    }
}
