package de.alexanderlindhorst.imapmailboxcleaner;

import de.alexanderlindhorst.imapmailboxcleaner.event.FolderStatusEvent;
import de.alexanderlindhorst.imapmailboxcleaner.event.FolderStatusListener;
import de.alexanderlindhorst.imapmailboxcleaner.event.IMAPOperationExceptionEvent;
import de.alexanderlindhorst.imapmailboxcleaner.event.IMAPOperationExceptionListener;
import de.alexanderlindhorst.imapmailboxcleaner.event.MessagesDeletedEvent;
import de.alexanderlindhorst.imapmailboxcleaner.event.MessagesDeletedListener;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import javax.mail.Flags.Flag;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Store;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author alindhorst (original author)
 * @author $Author$ (last editor)
 * @version $Revision$
 */
public class MailBoxCleaner implements Runnable {

    public static final long DEFAULT_DELETABLE_THRESHOLD = 3 * 60 * 60 * 1000;
    private static final int KILO = 1024;
    private static final Logger DEBUG = LoggerFactory.getLogger("DEBUG");
    private static final Logger STDOUT = LoggerFactory.getLogger("STDOUT");
    private static final Logger ERROR = LoggerFactory.getLogger("ERROR");
    private static final int IMAP_PORT_DEFAULT = 143;
    private static final String IMAP_PROTOCOL = "imap";
    private static final int UPDATE_STEP_INTERVAL = 10;
    private static final int WANTED_NUMBER_OF_PARAMS = 5;
    private String login;
    private String password;
    private String imapServerHost;
    private int port;
    private long deletableThreshold;
    private Store store;
    private FolderInformation root;
    private List<FolderStatusListener> folderStatusListeners;
    private List<MessagesDeletedListener> messagesDeletedListeners;
    private List<IMAPOperationExceptionListener> iMAPOperationExceptionListeners;

    public MailBoxCleaner(String login, String password, String imapServerHost, int port, long deletableThreshold) {
        this.login = login;
        this.password = password;
        this.imapServerHost = imapServerHost;
        this.port = port;
        this.deletableThreshold = deletableThreshold;
        folderStatusListeners = new ArrayList<FolderStatusListener>();
        messagesDeletedListeners = new ArrayList<MessagesDeletedListener>();
        iMAPOperationExceptionListeners = new ArrayList<IMAPOperationExceptionListener>();
    }

    public MailBoxCleaner(String login, String password, String imapServerHost, int port) {
        this(login, password, imapServerHost, port, DEFAULT_DELETABLE_THRESHOLD);
    }

    public MailBoxCleaner(String login, String password, String imapServerHost) {
        this(login, password, imapServerHost, IMAP_PORT_DEFAULT);
    }

    public void addFolderStatusListener(FolderStatusListener folderStatusListener) {
        folderStatusListeners.add(folderStatusListener);
    }

    public void removeFolderStatusListener(FolderStatusListener folderStatusListener) {
        folderStatusListeners.add(folderStatusListener);
    }

    private void fireFolderOpened(Folder folder) {
        FolderStatusEvent event = new FolderStatusEvent(folder);
        for (FolderStatusListener folderStatusListener : folderStatusListeners) {
            folderStatusListener.folderOpened(event);
        }
    }

    private void fireFolderClosed(Folder folder) {
        FolderStatusEvent event = new FolderStatusEvent(folder);
        for (FolderStatusListener folderStatusListener : folderStatusListeners) {
            folderStatusListener.folderClosed(event);
        }
    }

    public void addMessagesDeletedListener(MessagesDeletedListener messagesDeletedListener) {
        messagesDeletedListeners.add(messagesDeletedListener);
    }

    public void removeMessagesDeletedListener(MessagesDeletedListener messagesDeletedListener) {
        messagesDeletedListeners.remove(messagesDeletedListener);
    }

    private void fireMessagesDeleted() {
        MessagesDeletedEvent messagesDeletedEvent = new MessagesDeletedEvent(this, root.getDeletedRecursiveCount(),
                root.getTooOldMessageRecursiveCount());
    }

    public void addIMAPOperationExceptionListener(IMAPOperationExceptionListener iMAPOperationExceptionListener) {
        iMAPOperationExceptionListeners.add(iMAPOperationExceptionListener);
    }

    public void removeIMAPOperationExceptionListener(IMAPOperationExceptionListener iMAPOperationExceptionListener) {
        iMAPOperationExceptionListeners.remove(iMAPOperationExceptionListener);
    }

    private void fireIMAPOperationExceptionOccured(Exception exception) {
        IMAPOperationExceptionEvent event = new IMAPOperationExceptionEvent(exception);
        for (IMAPOperationExceptionListener listener : iMAPOperationExceptionListeners) {
            listener.exceptionOccured(event);
        }
    }

    @Override
    public void run() {
        Date thresholdDate = new Date(System.currentTimeMillis() - deletableThreshold);
        try {
            STDOUT.debug("Connecting ....");
            doConnect();
            STDOUT.debug("Cleaning up ....");
            for (FolderInformation folderInfo : root.getChildren()) {
                doCleanupFolder(thresholdDate, folderInfo, true);
            }
            STDOUT.debug("Disconnecting ...");
            doDisconnect();
        } catch (MessagingException messagingException) {
            ERROR.error("Error in cleanup process", messagingException);
            fireIMAPOperationExceptionOccured(messagingException);
        }
        STDOUT.info("Done. {} of {} messages deleted", root.getDeletedRecursiveCount(), root.
                getMessageRecursiveCount());
    }

    public void stopForcibly() {
        try {
            doDisconnect();
        } catch (MessagingException messagingException) {
            ERROR.warn("An exception has occured while the operation was aborted; this can normally be ignored.",
                    messagingException);
            fireIMAPOperationExceptionOccured(messagingException);
        }
    }

    private void doConnect() throws MessagingException {
        STDOUT.debug("Starting session");
        Properties properties = new Properties();
        properties.setProperty("mail.imap.partialfetch", "true");
        properties.setProperty("mail.imap.connectiontimeout", "30000");
        properties.setProperty("mail.imap.timeout", "120000");
        properties.setProperty("mail.imap.connectionpoolsize", "10");
        properties.setProperty("mail.imap.connectionpooltimeout", "20000");
        Session session = Session.getDefaultInstance(new Properties());
        session.setDebug(false);
        store = session.getStore(IMAP_PROTOCOL);
        STDOUT.debug("Opening IMAP account");
        store.connect(imapServerHost, port, login, password);
        Folder rootFolder = store.getDefaultFolder();
        Folder[] children = rootFolder.list();
        List<FolderInformation> folderInformations = new ArrayList<FolderInformation>(children.length);
        for (Folder folder : children) {
            FolderInformation info = buildFolderHierarchy(folder);
            if (info != null) { //null => error occured
                folderInformations.add(info);
            }
        }
        root = new FolderInformation(rootFolder, folderInformations, 0, 0);
    }

    private void doDisconnect() throws MessagingException {
        store.close();
    }

    private void doCleanupFolder(Date thresholdDate, FolderInformation folderInformation, boolean recursive)
            throws MessagingException {
        Message[] messages = getTooOldMessages(thresholdDate, folderInformation.getFolder());
        Folder folder = folderInformation.getFolder();
        if (folder.exists()) {
            folder.open(Folder.READ_WRITE);
            fireFolderOpened(folder);
            for (Message message : messages) {
                try {
                    message.setFlag(Flag.DELETED, true);
                    folderInformation.doIncrementDeletedCount();
                    DEBUG.debug("Deleted message: {} - \"{}\"", folder.getFullName(), message.getSubject());
                    if (folderInformation.getDeletedCount() > 0 && folderInformation.getDeletedCount()
                            % UPDATE_STEP_INTERVAL == 0) { //fire updates in reasonable steps
                        fireMessagesDeleted();
                        folderInformation.getFolder().expunge();

                    }
                } catch (Exception exception) {
                    ERROR.error("Error while deleting message:{} - \"{}\"", folder.getFullName(), message.
                            getSubject());
                    ERROR.error("Error details: ", exception);
                }
            }
            //fire at least once
            fireMessagesDeleted();
            folderInformation.getFolder().expunge();

            if (recursive) {
                for (FolderInformation childFolder : folderInformation.getChildren()) {
                    doCleanupFolder(thresholdDate, childFolder, recursive);
                }
            }
            folder.close(true);
            fireFolderClosed(folder);
        }
    }

    private FolderInformation buildFolderHierarchy(Folder folder) throws MessagingException {
        DEBUG.debug("Building hierarchy for folder {}", folder.getFullName());
        FolderInformation folderInformation = null;
        try {
            List<FolderInformation> children = new ArrayList<FolderInformation>();
            int messages = 0;
            int tooOld = 0;
            if (folder.exists()) {
                messages = folder.getMessageCount();
                STDOUT.info("Found {}  messages in folder '{}'", messages, folder.getName());
                tooOld =
                        getTooOldMessages(new Date(System.currentTimeMillis() - deletableThreshold), folder).length;
                for (Folder childFolder : folder.list("*")) {
                    FolderInformation childFolderInformation = buildFolderHierarchy(childFolder);
                    if (childFolderInformation != null) {
                        children.add(childFolderInformation);
                    }
                }
            }
            folderInformation = new FolderInformation(folder, children, messages, tooOld);
        } catch (MessagingException messagingException) {
            ERROR.error("Couldn't build hierarchy for " + folder.getFullName()
                    + ", will skip it for the remaining parts of the process.", messagingException);
        }
        return folderInformation;
    }

    private Message[] getTooOldMessages(Date thresholdDate, Folder folder) throws MessagingException {
        List<Message> messages = new ArrayList<Message>();
        if (folder.exists()) {
            folder.open(Folder.READ_ONLY);
            fireFolderOpened(folder);
            for (Message message : folder.getMessages()) {
                try {
                    if (message.getSentDate().before(thresholdDate)) {
                        messages.add(message);
                    }
                } catch (Exception exception) {
                    ERROR.error(String.format("Error while trying to fetch too old message \"%s\" - \"%s\"", folder.
                            getFullName(), message.getSubject()), exception);
                    fireIMAPOperationExceptionOccured(exception);
                }
            }
            folder.close(false);
            fireFolderClosed(folder);
        }
        return messages.toArray(new Message[]{});
    }

    private static class FolderInformation {

        private List<FolderInformation> children;
        private Folder folder;
        private int messageCount;
        private int tooOldMessageCount;
        private int deletedCount;

        public FolderInformation(Folder folder, List<FolderInformation> children, int messageCount,
                int tooOldMessageCount) {
            this.children = children;
            this.folder = folder;
            this.messageCount = messageCount;
            this.tooOldMessageCount = tooOldMessageCount;
        }

        public Folder getFolder() {
            return folder;
        }

        public List<FolderInformation> getChildren() {
            return children;
        }

        public int getMessageCount() {
            return messageCount;
        }

        public int getTooOldMessageCount() {
            return tooOldMessageCount;
        }

        public int getDeletedCount() {
            return deletedCount;
        }

        public void doIncrementDeletedCount() {
            deletedCount++;
        }

        public int getTooOldMessageRecursiveCount() {
            int childCount = 0;
            for (FolderInformation folderInformation : children) {
                childCount += folderInformation.getTooOldMessageRecursiveCount();
            }
            return childCount + tooOldMessageCount;
        }

        public int getMessageRecursiveCount() {
            int childCount = 0;
            for (FolderInformation folderInformation : children) {
                childCount += folderInformation.getMessageRecursiveCount();
            }
            return childCount + messageCount;
        }

        public int getDeletedRecursiveCount() {
            int childCount = 0;
            for (FolderInformation folderInformation : children) {
                childCount += folderInformation.getDeletedRecursiveCount();
            }
            return childCount + deletedCount;
        }
    }

    public static void main(String... args) throws IOException {
        if (args == null || args.length < 1) {
            ERROR.warn("No parameters given. At least the -gui parameter is needed.");
        }

        //check for "gui" option
        if (args[0].equals("-gui")) {
            MailBoxCleanerGUI gui = new MailBoxCleanerGUI();
            gui.setVisible(true);
        } else if (args == null || args.length < WANTED_NUMBER_OF_PARAMS) {
            ERROR.warn("Not enough parameters");
            printUsage();
        } else {
            try {
                MailBoxCleaner cleaner = new MailBoxCleaner(args[0], args[1], args[2], Integer.parseInt(args[3]), Long.
                        parseLong(args[4]) * 1000); //NOSONAR
                Thread t = new Thread(cleaner);
                t.start();
            } catch (Exception exception) {
                ERROR.error("Couldn't perform cleanup task", exception);
                printUsage();
            }
        }
    }

    private static void printUsage() throws IOException {
        InputStreamReader inputStreamReader = new InputStreamReader(MailBoxCleaner.class.getResource("/usage.txt").
                openStream());
        char[] buffer = new char[KILO];
        int read = 0;
        StringBuilder stringBuilder = new StringBuilder();
        while ((read = inputStreamReader.read(buffer)) >= 0) {
            stringBuilder.append(buffer, 0, read);
        }
        STDOUT.info(stringBuilder.toString());
    }
}
