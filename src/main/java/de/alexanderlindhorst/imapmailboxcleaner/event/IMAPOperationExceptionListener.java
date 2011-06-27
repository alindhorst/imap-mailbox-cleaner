
package de.alexanderlindhorst.imapmailboxcleaner.event;

/**
  * @author alindhorst (original author)
  * @author $Author$ (last editor)
  * @version $Revision$ */
public interface IMAPOperationExceptionListener {
    void exceptionOccured(IMAPOperationExceptionEvent event);
}
