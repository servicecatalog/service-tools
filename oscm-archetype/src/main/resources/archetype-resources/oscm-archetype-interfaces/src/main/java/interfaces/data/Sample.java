#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
/*******************************************************************************
 *                                                                              
 *  Copyright FUJITSU LIMITED 2017                                           
 *                                                                                                                                 
 *  Creation Date: Jun 22, 2017                                                      
 *                                                                              
 *******************************************************************************/

package ${package}.interfaces.data;

import org.oscm.common.interfaces.data.Event;
import org.oscm.common.interfaces.exceptions.ServiceException;
import org.oscm.common.interfaces.keys.ActivityKey;

/**
 * @author miethaner
 *
 */
public class Sample extends Event {

    @Override
    public void validateFor(ActivityKey activity) throws ServiceException {
        // TODO Auto-generated method stub

    }

}
