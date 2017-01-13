/*******************************************************************************
 *                                                                              
 *  Copyright FUJITSU LIMITED 2016                                           
 *                                                                                                                                 
 *  Creation Date: Aug 12, 2016                                                      
 *                                                                              
 *******************************************************************************/

package org.oscm.common.kafka.unit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import org.oscm.common.kafka.RequestParameters;

/**
 * Unit test for RequestParameters
 * 
 * @author miethaner
 */
public class RequestParametersTest {

    @Test
    public void testFields() throws Exception {

        Map<String, String> paramMap = new HashMap<>();
        paramMap.put(RequestParameters.PARAM_ID, "2");

        RequestParameters params = new RequestParameters(new Integer(1),
                paramMap) {
        };
        assertEquals(new Integer(1), params.getVersion());
        assertEquals(new Long(2L), params.getId());

        params.setVersion(new Integer(2));
        params.setId(new Long(1L));

        assertEquals(new Integer(2), params.getVersion());
        assertEquals(new Long(1L), params.getId());
        assertNull(params.getETag());
        assertNull(params.getLimit());
        assertNull(params.getOffset());
        assertNotNull(params.getSecurityToken());

    }
}
