package view;

import model.Command;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;

/**
 * A threaded modal that lets the user cancel the processing operation if needed.
 * Without this, the screen would freeze up - locking out user control
 */
public class Modal extends JDialog implements ActionListener {
    private SwingWorker worker;
    private JPanel panel = new JPanel(new BorderLayout());
    private JLabel messageLabel;
    private JButton cancelButton = new JButton("Cancel");

    /**
     * Constructor for the modal. Once created, the modal will run the command in a swing worker thread.
     * Once the task is done, the modal will automatically dispose itself.
     *
     * @param parent Parent component of this modal
     * @param message Message to display on the modal
     * @param runnable Command to run in the swing worker thread
     * @throws ExecutionException
     * @throws InterruptedException
     */
    public Modal(Frame parent, String message, Command runnable) throws ExecutionException, InterruptedException {
        //Setup modal GUI
        super(parent);
        messageLabel = new JLabel(message);
        cancelButton.setSize(100, 30);
        cancelButton.addActionListener(this);
        panel.add(messageLabel, BorderLayout.CENTER);
        panel.add(cancelButton, BorderLayout.SOUTH);

        getContentPane().add(panel);
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        setLocationRelativeTo(parent);
        setModal(true);
        setTitle(message);
        setSize(300, 100);

        //Create a SwingWorker that runs the Command on a thread and destroys itself upon completion
        worker = new SwingWorker<>() {
            @Override
            protected String doInBackground() {
                runnable.run();
                return "Finished";
            }

            @Override
            protected void done() {
                super.done();
                dispose();
            }
        };

        //Start the worker thread
        worker.execute();

        //setVisible must be called after execute to keep the Worker thread alive
        setVisible(true);

        //Blocking code - will wait until done (and throw any errors that occur)
        worker.get();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        //Interrupt the worker if the cancel button is pressed
        if (e.getSource() == cancelButton) {
            boolean successCancel = worker.cancel(true);
            if(!successCancel){
                JOptionPane.showMessageDialog(getParent(), "Could not cancel task");
            }
        }
    }
}
