
package de.alexanderlindhorst.imapmailboxcleaner.event;

import java.util.EventObject;
import javax.mail.Folder;

/**
 * @author alindhorst (original author)
 * @author $Author$ (last editor)
 * @version $Revision$
 */
public class FolderStatusEvent extends EventObject{

    public FolderStatusEvent(Folder source) {
        super(source);
    }

    @Override
    public Folder getSource() {
        return (Folder) super.getSource();
    }

}
