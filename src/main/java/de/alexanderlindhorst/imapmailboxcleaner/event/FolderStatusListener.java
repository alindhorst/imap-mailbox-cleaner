package de.alexanderlindhorst.imapmailboxcleaner.event;

/**
 * @author alindhorst (original author)
 * @author $Author$ (last editor)
 * @version $Revision$
 */
public interface FolderStatusListener {

    void folderOpened(FolderStatusEvent event);

    void folderClosed(FolderStatusEvent event);
}
