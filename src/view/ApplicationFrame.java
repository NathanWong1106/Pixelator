package view;

import model.Config;
import pipeline.ImageReader;
import pipeline.ImageWriter;
import pipeline.Processor;
import util.ImageSizeUtil;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class ApplicationFrame extends JFrame implements ActionListener, ChangeListener {
    private static final int WIDTH = 1920, HEIGHT = 1080;
    private static final String FONT = "Calibri";

    private JLabel title = new JLabel("Pixelator");
    private JLabel sliderHeader = new JLabel("Detail");
    private JSlider detailSlider = new JSlider();
    private JButton filePickerButton = new JButton("Choose File");
    private JButton convertButton = new JButton("Convert");
    private JLabel originalImg = new JLabel();
    private JLabel convertedImg = new JLabel();

    private JComponent[] frameComponents = {title, sliderHeader, detailSlider, filePickerButton, convertButton, originalImg, convertedImg};

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
        setupSlider();
        addGUIElements();
    }

    private void setupLabels() {
        int mid = WIDTH / 2;
        title.setSize(200, 100);
        title.setLocation(mid - title.getSize().width / 2, 0);
        title.setFont(new Font(FONT, Font.BOLD, 50));
        title.setHorizontalAlignment(JLabel.CENTER);

        sliderHeader.setSize(200, 50);
        sliderHeader.setLocation(mid - sliderHeader.getSize().width / 2, 750);
        sliderHeader.setFont(new Font(FONT, Font.PLAIN, 20));
        sliderHeader.setHorizontalAlignment(JLabel.CENTER);

        originalImg.setLocation(20, 100);
        originalImg.setSize(ImageSizeUtil.MAX_IM_SIZE_WIDTH, ImageSizeUtil.MAX_IM_SIZE_HEIGHT);
        originalImg.setHorizontalAlignment(JLabel.CENTER);
        originalImg.setVerticalAlignment(JLabel.CENTER);

        convertedImg.setLocation(1170, 100);
        convertedImg.setSize(ImageSizeUtil.MAX_IM_SIZE_WIDTH, ImageSizeUtil.MAX_IM_SIZE_HEIGHT);
        convertedImg.setHorizontalAlignment(JLabel.CENTER);
        convertedImg.setVerticalAlignment(JLabel.CENTER);
    }

    private void setupButtons() {
        int mid = WIDTH / 2;
        filePickerButton.setSize(200, 50);
        filePickerButton.setLocation(mid - filePickerButton.getSize().width / 2, 875);
        filePickerButton.addActionListener(this);

        convertButton.setSize(200, 50);
        convertButton.setLocation(mid - convertButton.getSize().width / 2, 950);
        convertButton.addActionListener(this);
    }

    private void setupSlider() {
        int mid = WIDTH / 2;
        detailSlider.setEnabled(false);
        detailSlider.setPaintTicks(true);
        detailSlider.setToolTipText("Choose a file to convert first!");
        detailSlider.setSize(300, 30);
        detailSlider.setLocation(mid - detailSlider.getSize().width / 2, 800);
        detailSlider.addChangeListener(this);
    }

    private void setSliderTicks(int numTicks) {
        detailSlider.setMinimum(0);
        detailSlider.setMaximum(numTicks - 1);
        detailSlider.setMajorTickSpacing(1);
        detailSlider.setEnabled(true);

        detailSlider.setValue(0);
        Config.setPixelSize(0);
    }

    private void addGUIElements() {
        for (JComponent component : frameComponents) {
            add(component);
        }
    }

    private boolean chooseFile() {
        boolean hasChosenFile = false;

        JFileChooser chooser = new JFileChooser();
        chooser.setCurrentDirectory(new File("."));

        while (!hasChosenFile) {
            int ret = chooser.showDialog(this, "Choose an image");

            if (ret == JFileChooser.APPROVE_OPTION) {
                File selected = chooser.getSelectedFile();
                hasChosenFile = true;
                try {
                    ImageReader.readImage(selected);
                    setSliderTicks(Config.pixelSizeOptions.length);
                } catch (IOException e) {
                    JOptionPane.showMessageDialog(this, "Invalid file. Please choose again.");
                    hasChosenFile = false;
                }
            } else if (ret == JFileChooser.CANCEL_OPTION) {
                return false;
            }
        }

        return true;
    }

    private boolean chooseOutputFolder() {
        JFileChooser chooser = new JFileChooser();
        chooser.setCurrentDirectory(new File("."));
        chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        chooser.setAcceptAllFileFilterUsed(false);

        int res = chooser.showDialog(this, "Choose output folder");

        if (res == JOptionPane.NO_OPTION) {
            return false;
        }

        Config.outputFolder = chooser.getSelectedFile().getAbsolutePath();
        return true;
    }

    private boolean chooseOutputName() {
        String name = "";

        while (name.equals("")) {
            name = JOptionPane.showInputDialog(this, "Name of output file", "converted");
            if (name == null) {
                return false;
            } else if (name.equals("")) {
                JOptionPane.showMessageDialog(this, "Names cannot be blank");
            }
        }

        Config.outputFileName = name;

        return true;
    }

    private void updateOriginalImg() {

        //Portrait
        if (Config.bufferedImage.getWidth() < Config.bufferedImage.getHeight()) {
//            originalImg.setLocation(20, 30);
//            originalImg.setSize(600, 888);
            originalImg.setIcon(new ImageIcon(ImageSizeUtil.getDisplayViableImage(Config.bufferedImage)));
        }
        //Landscape
        else {
//            originalImg.setLocation(10, 100);
//            originalImg.setSize(888, 500);
            originalImg.setIcon(new ImageIcon(ImageSizeUtil.getDisplayViableImage(Config.bufferedImage)));
        }
    }

    private void updateConvertedImg() {
        //Portrait
        if (Config.convertedImage.getWidth() < Config.convertedImage.getHeight()) {
//            convertedImg.setLocation(1250, 30);
//            convertedImg.setSize(600, 888);
            convertedImg.setIcon(new ImageIcon(ImageSizeUtil.getDisplayViableImage(Config.convertedImage)));
        }
        //Landscape
        else {
//            convertedImg.setLocation(900, 100);
//            convertedImg.setSize(888, 500);
            convertedImg.setIcon(new ImageIcon(ImageSizeUtil.getDisplayViableImage(Config.convertedImage)));
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == filePickerButton) {
            if (chooseFile()) {
                updateOriginalImg();
            }
        } else if (e.getSource() == convertButton) {
            try {
                if (Config.bufferedImage != null && chooseOutputFolder() && chooseOutputName()) {
                    ImageWriter.writeImageFromArray(Processor.processImage());
                    updateConvertedImg();
                }
            } catch (IOException err) {
                System.err.println(err);
            }

        }
    }

    @Override
    public void stateChanged(ChangeEvent e) {
        if (e.getSource() == detailSlider) {
            Config.setPixelSize(detailSlider.getValue());
            detailSlider.setToolTipText(String.format("Pixel Size: %d x %d", Config.pixelSize, Config.pixelSize));
        }
    }
}
