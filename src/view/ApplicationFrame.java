package view;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class ApplicationFrame extends JFrame implements ActionListener {
    private int WIDTH = 1920, HEIGHT = 1080;
    private String FONT = "Calibri";
    private JLabel title = new JLabel("Pixelator");
    private JLabel sliderHeader = new JLabel("Detail");
    private JLabel fileNameHeader = new JLabel("File Name");
    private JSlider detailSlider = new JSlider();
    private JButton filePickerButton = new JButton("Choose File");
    private JButton convertButton = new JButton("Convert");
    private JLabel originalImg = new JLabel();
    private JLabel convertedImg = new JLabel();
    private JTextField fileNameInput = new JTextField();

    private JComponent[] frameComponents = {title, sliderHeader, fileNameHeader, detailSlider, filePickerButton, convertButton, originalImg, convertedImg, fileNameInput};

    public ApplicationFrame() {
        setSize(WIDTH, HEIGHT);
        setTitle("Pixelator");
        setLayout(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setupGUI();
        setVisible(true);
    }

    private void setupGUI() {
        setupLabels();
        setupButtons();
        addGUIElements();
    }

    private void setupLabels() {
        int mid = WIDTH / 2;
        title.setSize(200, 100);
        title.setLocation(mid - title.getSize().width / 2, 0);
        title.setFont(new Font(FONT, Font.BOLD, 50));
        title.setHorizontalAlignment(JLabel.CENTER);

        sliderHeader.setSize(200, 50);
        sliderHeader.setLocation(100, 800);
        sliderHeader.setFont(new Font(FONT, Font.PLAIN, 30));
        sliderHeader.setHorizontalAlignment(JLabel.CENTER);

        fileNameHeader.setSize(200, 50);
        fileNameHeader.setLocation(mid + 600, 800);
        fileNameHeader.setFont(new Font(FONT, Font.PLAIN, 30));
        fileNameHeader.setHorizontalAlignment(JLabel.CENTER);

        //TODO setup image labels
    }

    private void setupButtons() {
        int mid = WIDTH / 2;
        filePickerButton.setSize(200, 100);
        filePickerButton.setLocation(mid - filePickerButton.getSize().width / 2, 800);
        filePickerButton.addActionListener(this);

        convertButton.setSize(200, 100);
        convertButton.setLocation(mid - convertButton.getSize().width / 2, 910);
        convertButton.addActionListener(this);
    }

    private void addGUIElements() {
        for (JComponent component : frameComponents) {
            add(component);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {

    }
}
