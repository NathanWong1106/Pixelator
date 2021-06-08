package view;

import model.Command;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;

public class Modal extends JDialog implements ActionListener {
    private SwingWorker worker;
    private JPanel panel = new JPanel(new BorderLayout());
    private JLabel messageLabel;
    private JButton cancelButton = new JButton("Cancel");

    public Modal(Frame parent, String message, Command runnable) throws ExecutionException, InterruptedException {
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
        if (e.getSource() == cancelButton) {
            boolean successCancel = worker.cancel(true);
            if(!successCancel){
                JOptionPane.showMessageDialog(getParent(), "Could not cancel task");
            }
        }
    }
}
