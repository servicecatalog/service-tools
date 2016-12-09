/*******************************************************************************
 *                                                                              
 *  Copyright FUJITSU LIMITED 2016                                           
 *                                                                                                                                 
 *  Creation Date: Dec 9, 2016                                                      
 *                                                                              
 *******************************************************************************/

package org.oscm.common.hibernate.unit;

import org.junit.Test;
import org.oscm.common.hibernate.ConnectionManager;

/**
 * Unit test for ConnectionManager
 * 
 * @author miethaner
 */
public class ConnectionManagerTest {

    @Test(expected = RuntimeException.class)
    public void testWithoutInit() {

        ConnectionManager.getInstance().getEntityManager();
    }
}
