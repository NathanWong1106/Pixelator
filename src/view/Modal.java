package view;

import util.Command;

import javax.swing.*;
import java.awt.*;
import java.util.concurrent.ExecutionException;

public class Modal extends JDialog {
    private SwingWorker worker;

    public Modal(Frame parent, String message, Command runnable) {
        super(parent);

        JPanel p1 = new JPanel(new BorderLayout());
        p1.add(new JLabel(message), BorderLayout.CENTER);

        getContentPane().add(p1);
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        setLocationRelativeTo(parent);
        setModal(true);
        setTitle(message);
        setSize(300,100);

        worker = new SwingWorker<>() {
            @Override
            protected String doInBackground() throws Exception {
                runnable.run();
                return "Finished";
            }

            @Override
            protected void done() {
                super.done();
                dispose();
            }
        };

        worker.execute();

        //setVisible must be called after execute to keep the Worker thread alive
        setVisible(true);

        //Blocking code - will wait until done
        try{
            worker.get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
