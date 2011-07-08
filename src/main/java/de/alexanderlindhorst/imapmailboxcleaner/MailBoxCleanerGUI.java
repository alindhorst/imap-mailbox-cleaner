
/*
 * MailBoxCleanerGUI.java
 *
 * Created on 17.03.2011, 16:02:09
 */
package de.alexanderlindhorst.imapmailboxcleaner;

import de.alexanderlindhorst.imapmailboxcleaner.event.FolderStatusEvent;
import de.alexanderlindhorst.imapmailboxcleaner.event.FolderStatusListener;
import de.alexanderlindhorst.imapmailboxcleaner.event.IMAPOperationExceptionEvent;
import de.alexanderlindhorst.imapmailboxcleaner.event.IMAPOperationExceptionListener;
import de.alexanderlindhorst.imapmailboxcleaner.event.MessagesDeletedEvent;
import de.alexanderlindhorst.imapmailboxcleaner.event.MessagesDeletedListener;
import javax.mail.Folder;
import javax.swing.SwingUtilities;
import org.apache.log4j.Logger;

/**
 *
 * @author alindhorst
 */
public class MailBoxCleanerGUI extends javax.swing.JFrame implements FolderStatusListener, MessagesDeletedListener,
        IMAPOperationExceptionListener {

    private static final String PROGRESS_STRING_TEMPLATE = "%d of %d messages deleted";
    private static final Logger LOGGER = Logger.getLogger(MailBoxCleanerGUI.class);
    private Worker worker;

    /** Creates new form MailBoxCleanerGUI */
    public MailBoxCleanerGUI() {
        initComponents();
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        javax.swing.JPanel titlePane = new javax.swing.JPanel();
        javax.swing.JLabel titelLabel = new javax.swing.JLabel();
        javax.swing.JPanel progressPane = new javax.swing.JPanel();
        progressBarLabel = new javax.swing.JLabel();
        progressBar = new javax.swing.JProgressBar();
        javax.swing.JPanel connectionPane = new javax.swing.JPanel();
        javax.swing.JLabel hostFieldLabel = new javax.swing.JLabel();
        hostField = new javax.swing.JTextField();
        javax.swing.JLabel portFieldLabel = new javax.swing.JLabel();
        portField = new javax.swing.JTextField();
        javax.swing.JLabel usernameFieldLabel = new javax.swing.JLabel();
        usernameField = new javax.swing.JTextField();
        javax.swing.JLabel passwordFieldLabel = new javax.swing.JLabel();
        passwordField = new javax.swing.JPasswordField();
        javax.swing.JLabel thresholdFiledLabel = new javax.swing.JLabel();
        thresholdField = new javax.swing.JTextField();
        javax.swing.JPanel actionPane = new javax.swing.JPanel();
        javax.swing.JPanel innerActionPane = new javax.swing.JPanel();
        cleanUpButton = new javax.swing.JButton();
        stopButton = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Mail Box Cleaner");

        titlePane.setBorder(javax.swing.BorderFactory.createEmptyBorder(20, 20, 10, 20));

        titelLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        titelLabel.setText("<html><body><h1>Mailbox Cleaner</h1></body></html>");
        titlePane.add(titelLabel);

        getContentPane().add(titlePane, java.awt.BorderLayout.NORTH);

        progressPane.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.LOWERED), javax.swing.BorderFactory.createEmptyBorder(20, 20, 20, 20)));
        progressPane.setLayout(new java.awt.GridBagLayout());

        progressBarLabel.setText("Progress:");
        progressBarLabel.setEnabled(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.ipadx = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        progressPane.add(progressBarLabel, gridBagConstraints);

        progressBar.setEnabled(false);
        progressBar.setStringPainted(true);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.gridheight = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.weightx = 2.0;
        progressPane.add(progressBar, gridBagConstraints);

        getContentPane().add(progressPane, java.awt.BorderLayout.PAGE_END);

        connectionPane.setBorder(javax.swing.BorderFactory.createEmptyBorder(10, 20, 20, 20));
        connectionPane.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        connectionPane.setLayout(new java.awt.GridBagLayout());

        hostFieldLabel.setText("Host:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.ipadx = 1;
        gridBagConstraints.ipady = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        connectionPane.add(hostFieldLabel, gridBagConstraints);

        hostField.setName("hostField"); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.ipadx = 1;
        gridBagConstraints.ipady = 1;
        gridBagConstraints.weightx = 2.0;
        connectionPane.add(hostField, gridBagConstraints);

        portFieldLabel.setText("Port:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.ipadx = 1;
        gridBagConstraints.ipady = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        connectionPane.add(portFieldLabel, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.ipadx = 1;
        gridBagConstraints.ipady = 1;
        gridBagConstraints.weightx = 2.0;
        connectionPane.add(portField, gridBagConstraints);

        usernameFieldLabel.setText("Username:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.ipadx = 1;
        gridBagConstraints.ipady = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        connectionPane.add(usernameFieldLabel, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.ipadx = 1;
        gridBagConstraints.ipady = 1;
        gridBagConstraints.weightx = 2.0;
        connectionPane.add(usernameField, gridBagConstraints);

        passwordFieldLabel.setText("Password:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.ipadx = 1;
        gridBagConstraints.ipady = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        connectionPane.add(passwordFieldLabel, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.ipadx = 1;
        gridBagConstraints.ipady = 1;
        gridBagConstraints.weightx = 2.0;
        connectionPane.add(passwordField, gridBagConstraints);

        thresholdFiledLabel.setText("Threshold in sec.:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.ipadx = 1;
        gridBagConstraints.ipady = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        connectionPane.add(thresholdFiledLabel, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.ipadx = 1;
        gridBagConstraints.ipady = 1;
        connectionPane.add(thresholdField, gridBagConstraints);

        actionPane.setBorder(javax.swing.BorderFactory.createEmptyBorder(10, 10, 10, 10));

        innerActionPane.setLayout(new java.awt.GridLayout(1, 0));

        cleanUpButton.setMnemonic('c');
        cleanUpButton.setText("Clean Up Mailbox");
        cleanUpButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cleanUpButtonActionPerformed(evt);
            }
        });
        innerActionPane.add(cleanUpButton);

        stopButton.setText("Stop");
        stopButton.setEnabled(false);
        stopButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                stopButtonActionPerformed(evt);
            }
        });
        innerActionPane.add(stopButton);

        actionPane.add(innerActionPane);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.ipadx = 1;
        gridBagConstraints.ipady = 1;
        connectionPane.add(actionPane, gridBagConstraints);

        getContentPane().add(connectionPane, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    @Override
    public void folderOpened(FolderStatusEvent event) {
        //handled in a separate thread, so GUI actions have to be sent to Event Dispatcher again
        try {
            SwingUtilities.invokeAndWait(new FolderOpenedDelegate(event.getSource()));
        } catch (Exception ex) {
            LOGGER.error("Couldn't digest event", ex);
        }
    }

    @Override
    public void folderClosed(FolderStatusEvent event) {
        //handled in a separate thread, so GUI actions have to be sent to Event Dispatcher again
        try {
            SwingUtilities.invokeAndWait(new FolderClosedDelegate(event.getSource()));
        } catch (Exception ex) {
            LOGGER.error("Couldn't digest event", ex);
        }
    }

    @Override
    public void messagesDeleted(MessagesDeletedEvent event) {
        //handled in a separate thread, so GUI actions have to be sent to Event Dispatcher again
        try {
            SwingUtilities.invokeAndWait(new MessagesDeletedDelegate(event.getTotalDeletableMessages(), event.
                    getMessagesDeleted()));
        } catch (Exception ex) {
            LOGGER.error("Couldn't digest event", ex);
        }

    }

    @Override
    public void exceptionOccured(IMAPOperationExceptionEvent event) {
        //handled in a separate thread, so GUI actions have to be sent to Event Dispatcher again
        try {
            SwingUtilities.invokeAndWait(new IMAPOperationExceptionOccuredDelegate(event.getSource()));
        } catch (Exception ex) {
            LOGGER.error("Couldn't digest event", ex);
        }
    }

    private void cleanUpButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cleanUpButtonActionPerformed
        cleanUpButton.setEnabled(false);
        stopButton.setEnabled(true);
        MailBoxCleaner mailBoxCleaner = null;
        try {
            if (worker != null && worker.isAlive()) {
                throw new IllegalStateException("Another task is still being executed.");
            }
            String host = hostField.getText();
            String port = portField.getText();
            String username = usernameField.getText();
            String password = String.valueOf(passwordField.getPassword());
            long threshold = thresholdField.getText() != null ? 
                Long.parseLong(thresholdField.getText()) * 1000 : MailBoxCleaner.DEFAULT_DELETABLE_THRESHOLD;
            mailBoxCleaner = new MailBoxCleaner(username, password, host, Integer.parseInt(port), threshold);
            progressBar.setEnabled(true);
            progressBar.setString("Getting message count ...");
            progressBar.setEnabled(true);
            mailBoxCleaner.addFolderStatusListener(this);
            mailBoxCleaner.addMessagesDeletedListener(this);
            mailBoxCleaner.addIMAPOperationExceptionListener(this);
            //spawn off a thread
            worker = new Worker(mailBoxCleaner);
            worker.start();
        } catch (Exception exception) {
            handleError(exception);
        }
        progressBar.setEnabled(false);
    }//GEN-LAST:event_cleanUpButtonActionPerformed

    private void handleError(Exception exception) {
        //TODO show error message box
        progressBar.setString("Error");
        LOGGER.error("Couldn't cleanup mailbox", exception);
        stopAction();
    }

    private void stopAction() {
        stopButton.setEnabled(false);
        cleanUpButton.setEnabled(true);
        if (worker != null && worker.isAlive()) {
            worker.kill();
            try {
                worker.join(5000); //NOSONAR
            } catch (InterruptedException interruptedException) { //NOSONAR
                //disregard this
            }
            if (worker.isAlive()) {
                worker.interrupt();
            }
            try {
                worker.join(1000); //NOSONAR
            } catch (InterruptedException interruptedException) { //NOSONAR
                //disregard this
            }
            if (worker.isAlive()) {
                worker.stop();
            }
            worker = null;
        }
    }

    @SuppressWarnings({"deprecation", "empty-statement"})
    private void stopButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_stopButtonActionPerformed
        stopAction();
    }//GEN-LAST:event_stopButtonActionPerformed
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton cleanUpButton;
    private javax.swing.JTextField hostField;
    private javax.swing.JPasswordField passwordField;
    private javax.swing.JTextField portField;
    private javax.swing.JProgressBar progressBar;
    private javax.swing.JLabel progressBarLabel;
    private javax.swing.JButton stopButton;
    private javax.swing.JTextField thresholdField;
    private javax.swing.JTextField usernameField;
    // End of variables declaration//GEN-END:variables

    private final class Worker extends Thread {

        private MailBoxCleaner mailBoxCleaner;

        private Worker(MailBoxCleaner cleaner) {
            this.mailBoxCleaner = cleaner;
        }

        @Override
        public void run() {
            mailBoxCleaner.run();
            progressBar.setString("Done.");
            stopAction();
        }

        private void kill() {
            mailBoxCleaner.stopForcibly();
        }
    }

    private final class FolderOpenedDelegate implements Runnable {

        private Folder folder;

        private FolderOpenedDelegate(Folder folder) {
            this.folder = folder;
        }

        @Override
        public void run() {
            LOGGER.info(folder.getName() + " opened");
        }
    }

    private final class FolderClosedDelegate implements Runnable {

        private Folder folder;

        private FolderClosedDelegate(Folder folder) {
            this.folder = folder;
        }

        @Override
        public void run() {
            LOGGER.info(folder.getName() + " closed");
        }
    }

    private final class MessagesDeletedDelegate implements Runnable {

        private int totalDeletableMessages;
        private int messagesDeleted;

        private MessagesDeletedDelegate(int totalDeletableMessages, int messagesDeleted) {
            this.totalDeletableMessages = totalDeletableMessages;
            this.messagesDeleted = messagesDeleted;
        }

        @Override
        public void run() {
            progressBar.setMaximum(totalDeletableMessages);
            progressBar.setValue(messagesDeleted);
            progressBar.setString(String.format(PROGRESS_STRING_TEMPLATE, messagesDeleted, totalDeletableMessages));
        }
    }

    private final class IMAPOperationExceptionOccuredDelegate implements Runnable {

        private Exception exception;

        private IMAPOperationExceptionOccuredDelegate(Exception exception) {
            this.exception = exception;
        }

        @Override
        public void run() {
            handleError(exception);
        }
    }
}
