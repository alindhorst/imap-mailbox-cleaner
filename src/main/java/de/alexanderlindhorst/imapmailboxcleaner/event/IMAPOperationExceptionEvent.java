
package de.alexanderlindhorst.imapmailboxcleaner.event;

import java.util.EventObject;

/**
 * @author alindhorst (original author)
 * @author $Author$ (last editor)
 * @version $Revision$
 */
public class IMAPOperationExceptionEvent extends EventObject{

    public IMAPOperationExceptionEvent(Exception source) {
        super(source);
    }

    @Override
    public Exception getSource() {
        return (Exception) super.getSource();
    }
}
