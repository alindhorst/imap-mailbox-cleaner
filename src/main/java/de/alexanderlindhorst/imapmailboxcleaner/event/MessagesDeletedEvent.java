
package de.alexanderlindhorst.imapmailboxcleaner.event;

import java.util.EventObject;

/**
 * @author alindhorst (original author)
 * @author $Author$ (last editor)
 * @version $Revision$
 */
public class MessagesDeletedEvent extends EventObject{
    private int totalDeletableMessages;
    private int messagesDeleted;

    public MessagesDeletedEvent(Object source, int totalDeletableMessages, int messagesDeleted) {
        super(source);
        this.totalDeletableMessages = totalDeletableMessages;
        this.messagesDeleted = messagesDeleted;
    }

    /**
     * Returns the number of messages deleted <b>in total, not since the last call to this method</b>. This will
     * thus return a constantly increasing number per deletion process.
     * @return
     */
    public int getMessagesDeleted() {
        return messagesDeleted;
    }

    public int getTotalDeletableMessages() {
        return totalDeletableMessages;
    }
}
