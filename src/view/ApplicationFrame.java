package view;

import model.Config;
import model.ProcessOption;
import pipeline.ImageReader;
import pipeline.Runner;
import model.Command;
import util.ImageSizeUtil;
import util.Util;

import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.util.Hashtable;
import java.util.concurrent.CancellationException;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 * Main Frame that is shown to the user containing all functionality of the application
 */
public class ApplicationFrame extends JFrame implements ActionListener, ChangeListener, ItemListener {
    private static final int WIDTH = 1920, HEIGHT = 1080;
    private static final String FONT = "Calibri";
    private static final String DESCRIPTION_PREPEND = "<b>Algorithm Description:</b> ";

    private JLabel title = new JLabel("Pixelator");
    private JLabel sliderHeader = new JLabel("Detail | Pixel Size");
    private JSlider detailSlider = new JSlider();
    private JButton filePickerButton = new JButton("Choose File");
    private JButton convertButton = new JButton("Convert");
    private JLabel originalImg = new JLabel();
    private JLabel convertedImg = new JLabel();
    private JLabel processOptionsHeader = new JLabel("Processing Type");
    private JLabel algorithmDescription = new JLabel(Util.getHTMLWrappedString(DESCRIPTION_PREPEND + Config.processOption.getDescription()));
    private JComboBox processingOptions = new JComboBox(ProcessOption.values());

    private Command runner = new Runner();

    private JComponent[] frameComponents = {title, sliderHeader, detailSlider, filePickerButton,
            convertButton, originalImg, convertedImg, processOptionsHeader,
            algorithmDescription, processingOptions};

    public ApplicationFrame() {
        setSize(WIDTH, HEIGHT);
        setTitle("Pixelator");
        setLayout(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setupGUI();
        setVisible(true);
    }

    /**
     * Calls all gui setup methods in order
     */
    private void setupGUI() {
        setupOptions();
        setupLabels();
        setupButtons();
        setupSlider();
        addGUIElements();
    }

    /**
     * Sets up the processing options combo box that allows the user to select an algorithm to process their image
     */
    private void setupOptions() {
        int mid = WIDTH / 2;
        processingOptions.setSize(300, 30);
        processingOptions.setLocation(mid - processingOptions.getWidth() / 2, 800);
        processingOptions.addItemListener(this);
    }

    /**
     * Sets up all labels/headings in the frame
     */
    private void setupLabels() {
        int mid = WIDTH / 2;
        title.setSize(200, 100);
        title.setLocation(mid - title.getSize().width / 2, 0);
        title.setFont(new Font(FONT, Font.BOLD, 50));
        title.setHorizontalAlignment(JLabel.CENTER);

        sliderHeader.setSize(200, 50);
        sliderHeader.setLocation(mid - sliderHeader.getSize().width / 2, 650);
        sliderHeader.setFont(new Font(FONT, Font.PLAIN, 20));
        sliderHeader.setHorizontalAlignment(JLabel.CENTER);
        sliderHeader.setToolTipText("Lower values preserve more detail and result in smaller pixels");

        processOptionsHeader.setSize(200, 50);
        processOptionsHeader.setLocation(mid - sliderHeader.getSize().width / 2, 750);
        processOptionsHeader.setFont(new Font(FONT, Font.PLAIN, 20));
        processOptionsHeader.setHorizontalAlignment(JLabel.CENTER);

        originalImg.setLocation(20, 100);
        originalImg.setSize(ImageSizeUtil.MAX_IM_SIZE_WIDTH, ImageSizeUtil.MAX_IM_SIZE_HEIGHT);
        originalImg.setHorizontalAlignment(JLabel.CENTER);
        originalImg.setVerticalAlignment(JLabel.CENTER);

        convertedImg.setLocation(1170, 100);
        convertedImg.setSize(ImageSizeUtil.MAX_IM_SIZE_WIDTH, ImageSizeUtil.MAX_IM_SIZE_HEIGHT);
        convertedImg.setHorizontalAlignment(JLabel.CENTER);
        convertedImg.setVerticalAlignment(JLabel.CENTER);

        algorithmDescription.setSize(300,60);
        algorithmDescription.setLocation(mid - algorithmDescription.getSize().width / 2,830);
        algorithmDescription.setFont(new Font (FONT, Font.PLAIN, 14));
        algorithmDescription.setVerticalAlignment(JLabel.TOP);
        algorithmDescription.setHorizontalAlignment(JLabel.CENTER);
    }

    /**
     * Sets up all buttons in the frame and binds this as an action listener.
     * The convert button is initially disabled without an input image
     */
    private void setupButtons() {
        int mid = WIDTH / 2;
        filePickerButton.setSize(200, 50);
        filePickerButton.setLocation(mid - filePickerButton.getSize().width / 2, 900);
        filePickerButton.addActionListener(this);

        convertButton.setSize(200, 50);
        convertButton.setLocation(mid - convertButton.getSize().width / 2, 975);
        convertButton.addActionListener(this);
        convertButton.setEnabled(false);
    }

    /**
     * Sets up the detail slider (initially disabled without an input image)
     */
    private void setupSlider() {
        int mid = WIDTH / 2;
        detailSlider.setEnabled(false);
        detailSlider.setPaintTicks(true);
        detailSlider.setPaintLabels(true);
        detailSlider.setToolTipText("Choose a file to convert first!");
        detailSlider.setSize(300, 50);
        detailSlider.setLocation(mid - detailSlider.getSize().width / 2, 700);
        detailSlider.addChangeListener(this);
    }

    /**
     * Based on the number of ticks needed, update the slider ticks and reset the selected index to 0
     */
    private void setSliderTicks(int numTicks) {
        //Prepare a hashtable to map the pixel size to the appropriate slider value
        Hashtable<Integer, JLabel> labels = new Hashtable<>();
        for(int i = 0; i < numTicks; i++){
            labels.put(i, new JLabel(Integer.toString(Config.pixelSizeOptions[i])));
        }

        //Update slider ticks
        detailSlider.setMinimum(0);
        detailSlider.setMaximum(numTicks - 1);
        detailSlider.setMajorTickSpacing(1);
        detailSlider.setEnabled(true);
        detailSlider.setLabelTable(labels);

        //Set to least amount of detail initially
        detailSlider.setValue(0);
        Config.setPixelSize(0);
    }

    /**
     * Adds all GUI components contained in the frameComponents array to the frame
     */
    private void addGUIElements() {
        for (JComponent component : frameComponents) {
            add(component);
        }
    }

    /**
     * Opens up a file chooser and allows the user to choose a file
     * @return true if a file was chosen, false if cancelled
     */
    private boolean chooseFile() {
        boolean hasChosenFile = false;

        //Create a file chooser and start it at the project's root directory
        JFileChooser chooser = new JFileChooser();
        chooser.setCurrentDirectory(new File("."));

        while (!hasChosenFile) {
            //Prompt the user for a file
            int ret = chooser.showDialog(this, "Choose an image");

            if (ret == JFileChooser.APPROVE_OPTION) {
                //If the user has selected a file then try to read it as an image and set the pixel size options
                File selected = chooser.getSelectedFile();
                hasChosenFile = true;
                try {
                    ImageReader.readImage(selected);
                    setSliderTicks(Config.pixelSizeOptions.length);
                } catch (IOException e) {
                    //If the file is incompatible then prompt then repeat the process again
                    JOptionPane.showMessageDialog(this, "Invalid file. Please choose again.");
                    hasChosenFile = false;
                }
            } else if (ret == JFileChooser.CANCEL_OPTION) {
                //Stop the process if the user has cancelled
                return false;
            }
        }

        //Enable the convert button once a file as been selected
        convertButton.setEnabled(true);
        return true;
    }

    /**
     * Opens a file chooser and allows the user to choose an output folder
     * @return true if a folder has been selected, false if cancelled
     */
    private boolean chooseOutputFolder() {
        //Create a file chooser that only accepts folders as input
        JFileChooser chooser = new JFileChooser();
        chooser.setCurrentDirectory(new File("."));
        chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        chooser.setAcceptAllFileFilterUsed(false);

        //Prompt the user to select a folder
        int res = chooser.showDialog(this, "Choose output folder");

        //If they cancel then stop the process
        if (res == JOptionPane.NO_OPTION) {
            return false;
        }

        //Update the config
        Config.outputFolder = chooser.getSelectedFile().getAbsolutePath();
        return true;
    }

    /**
     * Shows an input dialog to the user asking for input of an output file name (e.g. "converted")
     * @return true if a name was given, false if cancelled
     */
    private boolean chooseOutputName() {
        String name = "";

        //Continue while the name is a blank string
        while (name.equals("")) {
            //Get the user input for the name
            name = JOptionPane.showInputDialog(this, "Name of output file", "converted");

            //If the name is null then the user has cancelled, if the name is "" then re-prompt the user
            if (name == null) {
                return false;
            } else if (name.equals("")) {
                JOptionPane.showMessageDialog(this, "Names cannot be blank");
            }
        }

        //Update the config
        Config.outputFileName = name;
        return true;
    }

    /**
     * Updates the original image with the buffered image stored in config
     */
    private void updateOriginalImg() {
        originalImg.setIcon(new ImageIcon(ImageSizeUtil.getDisplayViableImage(Config.bufferedImage)));
    }

    /**
     * Updates the converted image with the converted buffered image stored in config
     */
    private void updateConvertedImg() {
        convertedImg.setIcon(new ImageIcon(ImageSizeUtil.getDisplayViableImage(Config.convertedImage)));
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        //Update the original image label if a new image is picked
        if (e.getSource() == filePickerButton) {
            if (chooseFile()) {
                updateOriginalImg();
            }
        } else if (e.getSource() == convertButton) {
            //Allow the user to choose their output folder and file name then start the processing pipeline to generate the new image
            if (Config.bufferedImage != null && chooseOutputFolder() && chooseOutputName()) {
                boolean processDone = true;
                try {
                    //The modal starts a new thread with the processes described in runner
                    //This is still a blocking piece of code and will not run asynchronously
                    //Once finished, the modal will destroy itself
                    new Modal(this, "Processing Image... please wait", runner);
                } catch (Exception err) {
                    //Catch any errors or cancellations from the process/user
                    if (err instanceof CancellationException) {
                        System.out.println("Cancelled Processing");
                    } else {
                        err.printStackTrace();
                    }
                    processDone = false;
                }

                //If a new image is successfully processed then update the converted image label with the new image
                if (processDone) {
                    updateConvertedImg();
                }
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

    @Override
    public void itemStateChanged(ItemEvent e) {
        if(e.getStateChange() == ItemEvent.SELECTED) {
            Config.processOption = (ProcessOption) processingOptions.getSelectedItem();
            algorithmDescription.setText(Util.getHTMLWrappedString(DESCRIPTION_PREPEND + Config.processOption.getDescription()));
        }
    }
}
