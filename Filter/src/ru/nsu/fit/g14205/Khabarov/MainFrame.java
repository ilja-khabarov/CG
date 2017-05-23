package ru.nsu.fit.g14205.Khabarov;

import javafx.stage.FileChooser;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;

/**
 * Created by ilja on 15.02.2017.
 */
public class MainFrame extends JFrame
{
    private JMenuBar menuBar;
    private JToolBar toolBar;
    DrawPanel drawPanel;
    JFrame chooseFileFrame;
    JFileChooser chooser;
    ActionListener fileOpener;
    MainFrame thislink;
    MainPane mainPane;

    public MainFrame()
    {
        super();
        this.setSize(1200,600);
        this.setPreferredSize(new Dimension(1150,600));
        this.setMinimumSize(new Dimension(800,600));
        this.setLocationRelativeTo(null);
        initFileListener();
        thislink = this;
        mainPane = new MainPane();

        JButton fileButton = new JButton("File");
        fileButton.setToolTipText("Open file");
        JButton clearButton = new JButton();
        clearButton.setToolTipText("Clear the field");
        JButton options = new JButton("Opts");
        options.setToolTipText("Options");
        JButton save = new JButton("Save");
        save.setToolTipText("Save into file");
        JButton about = new JButton("About");
        about.setToolTipText("About program");
        JButton grayButton = new JButton("Gray");
        JButton sobelButton = new JButton("Sobel");
        JButton x2Button = new JButton("X2");
        JButton negativeButton = new JButton("Neg");
        JButton embossButton = new JButton("Emb");
        JButton rotationButton = new JButton("Rot");
        JButton harshButton = new JButton("Harsh");
        JButton blurButton = new JButton("Blur");
        JButton aquaButton = new JButton("Aqua");
        JButton r2Bmp = new JButton("R2Bmp");
        JButton gammaButton = new JButton("Gamma");
        JButton ditherButton = new JButton("Dith");


        drawPanel = new DrawPanel();

        grayButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mainPane.drawPanelC.setImg(Filters.applyGrayscale(mainPane.drawPanelB.img));
            }
        });
        sobelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //mainPane.drawPanelC.setImg(Filters.applySobel(Filters.applyGrayscale(mainPane.drawPanelB.img), 100));
                SobelFrame sobelFrame = new SobelFrame();
                sobelFrame.setVisible(true);
            }
        });
        x2Button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mainPane.drawPanelC.setImg(Filters.zoom2(mainPane.drawPanelB.img));
            }
        });
        negativeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mainPane.drawPanelC.setImg(Filters.applyNegative(mainPane.drawPanelB.img));
            }
        });
        embossButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mainPane.drawPanelC.setImg(Filters.applyEmboss(Filters.applyGrayscale(mainPane.drawPanelB.img)));
            }
        });
        rotationButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //mainPane.drawPanelC.setImg(Filters.applyRotation(mainPane.drawPanelB.img, 28));
                RotationFrame rotationFrame = new RotationFrame();
                rotationFrame.setVisible(true);
            }
        });
        harshButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int[][] matrix = {{-1,-1,-1},{-1,9,-1},{-1,-1,-1}};
                mainPane.drawPanelC.setImg(Filters.applyMatrix3(mainPane.drawPanelB.img, matrix));

            }
        });
        blurButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int[][] matrix = {{1,1,1},{1,1,1},{1,1,1}};
                mainPane.drawPanelC.setImg(Filters.applyMatrix3(mainPane.drawPanelB.img, matrix));
            }
        });
        aquaButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int[][] matrixBlur = {{0,1,0},{1,2,1},{0,1,0}};
                int[][] matrixHarsh = {{-1,-1,-1},{-1,6,-1},{-1,-1,-1}};
                mainPane.drawPanelC.setImg(Filters.applyMatrix3(Filters.applyMatrix3(mainPane.drawPanelB.img, matrixBlur),matrixHarsh));
            }
        });
        ditherButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mainPane.drawPanelC.setImg(Filters.applyDither(mainPane.drawPanelB.img));

            }
        });
        gammaButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //mainPane.drawPanelC.setImg(Filters.applyGamma(mainPane.drawPanelB.img, 1.5));

                GammaFrame gammaFrame = new GammaFrame();
                gammaFrame.setVisible(true);
            }
        });
        options.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFrame optionsFrame = new JFrame("Options");
                optionsFrame.setSize(450,500);
                optionsFrame.setLocationRelativeTo(thislink);
                optionsFrame.setLayout(new FlowLayout(FlowLayout.LEADING));
                optionsFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                optionsFrame.setVisible(true);
            }
        });



        clearButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("Command caught: " + e.getActionCommand() );
                drawPanel.nullifyImage();
                drawPanel.repaint();
                drawPanel.impactMode = false;
            }

        });
        save.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                initChoseFileFrame();
                chooseFileFrame.setLocationRelativeTo(thislink);
                int returval = chooser.showOpenDialog(chooseFileFrame);
                if ( returval == JFileChooser.APPROVE_OPTION )
                {
                    File file = chooser.getSelectedFile();
                    String[] parsedFilename = file.getPath().split("\\.");
                    try {
                        ImageIO.write(mainPane.drawPanelC.img,
                                parsedFilename[1],
                                file);
                    } catch (IOException ioe)
                    {
                        ioe.printStackTrace();
                    }
                }
            }
        });
        about.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showAuthorFrame();
            }
        });
        r2Bmp.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mainPane.drawPanelB.setImg(mainPane.drawPanelC.img);
            }
        });
        initFileListener();
        fileButton.addActionListener(fileOpener);
        menuBar = new JMenuBar();
        toolBar = new JToolBar("MaiNToolbar");
        toolBar.add(fileButton);
//        toolBar.add(options);
        toolBar.add(save);
        toolBar.add(about);
        toolBar.add(grayButton);
        toolBar.add(sobelButton);
        toolBar.add(x2Button);
        toolBar.add(negativeButton);
        toolBar.add(embossButton);
        toolBar.add(rotationButton);
        toolBar.add(harshButton);
        toolBar.add(blurButton);
        toolBar.add(aquaButton);
        toolBar.add(gammaButton);
        toolBar.add(ditherButton);
        toolBar.add(r2Bmp);
        menuBar.setVisible(true);

        JMenu menu = new JMenu("File");
        menu.setMnemonic(KeyEvent.VK_A);
        menu.getAccessibleContext().setAccessibleDescription(
                "The only menu in this program that has menu items");
        JMenuItem open = new JMenuItem("Open" );
        open.addActionListener(fileOpener);
        menu.add(open);
        JMenu help = new JMenu("Help");
        help.setMnemonic(KeyEvent.VK_B);
        JMenuItem aboutAuthor= new JMenuItem("About author");
        help.add(aboutAuthor);
        aboutAuthor.addMouseListener(new MouseListener() {
            @Override
            public void mousePressed(MouseEvent e) {
                showAuthorFrame();
            }

            @Override
            public void mouseClicked(MouseEvent e) {

            }

            @Override
            public void mouseReleased(MouseEvent e) {

            }

            @Override
            public void mouseEntered(MouseEvent e) {

            }

            @Override
            public void mouseExited(MouseEvent e) {

            }
        });

        menuBar.add(menu);
        menuBar.add(help);

        this.setJMenuBar(menuBar);
        this.add(toolBar, BorderLayout.PAGE_START );

        drawPanel.setBackground(Color.white);
        //drawPanel.setSize(800,500);
        //JScrollPane scrollPane = new JScrollPane(drawPanel);
        JScrollPane scrollPane = new JScrollPane(mainPane);

        scrollPane.setPreferredSize(new Dimension(800,500));
        drawPanel.setLayout(new BoxLayout(drawPanel, BoxLayout.Y_AXIS));
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        //scrollPane.setSize(800,500);

        //this.add(drawPanel);
        this.add(scrollPane, BorderLayout.CENTER);

        /* ON_EXIT ACTION
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                super.windowClosing(e);
                initChoseFileFrame();
                chooseFileFrame.setVisible(true);
                int returval = chooser.showOpenDialog(chooseFileFrame);
                if ( returval == JFileChooser.APPROVE_OPTION )
                {
                    File file = chooser.getSelectedFile();
                    chooseFileFrame.setVisible(false);
                }
            }

        });
*/

        this.setVisible(true);
        this.pack();
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
    void initFileListener()
    {
        fileOpener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                initChoseFileFrame();
                int returval = chooser.showOpenDialog(chooseFileFrame);
                if ( returval == JFileChooser.APPROVE_OPTION )
                {
                    File file = chooser.getSelectedFile();
                    mainPane.setImageA(file);

                }
            }
        };
    }
    void initChoseFileFrame()
    {
        chooseFileFrame = new JFrame("Choose file");
        chooser = new JFileChooser("resources/Data");
        chooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
        chooser.setPreferredSize(new Dimension(400,300));
        chooseFileFrame.setLocationRelativeTo(null);
        chooser.setCurrentDirectory(new File("Data/"));

        /*
        chooseFileFrame.setLayout(new FlowLayout(FlowLayout.LEADING));
        chooseFileFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        chooseFileFrame.setSize(430,350);
        chooseFileFrame.add(chooser);
        */

        //chooseFileFrame.setVisible(true);
    }
    void showAuthorFrame()
    {
        JFrame authorFrame = new JFrame("About author");
        authorFrame.setLocationRelativeTo(thislink);
        authorFrame.setSize(300, 100 );
        authorFrame.setLayout(new GridLayout(2, 1));
        JLabel textAboutVersion = new JLabel("Version 0.1");
        textAboutVersion.setSize(300,50);
        JLabel textAboutAuthor = new JLabel("FIT Khabarov, 13203");
        textAboutAuthor.setSize(300,50);
        authorFrame.add(textAboutVersion);
        authorFrame.add(textAboutAuthor);
        authorFrame.setVisible(true);
        authorFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }

    private class RotationFrame extends JFrame
    {
        SettingsElementPane elementPane;
        JButton okButton;;
        JButton cancelButton;

        void closeRotationFrame()
        {
            this.dispose();
        }
        RotationFrame getRotationFrameExemplar()
        {
            return this;
        }

        RotationFrame()
        {
            super("Rotate");
            this.setPreferredSize(new Dimension(300,200));
            elementPane = new SettingsElementPane("Rotate");
            okButton = new JButton("OK");
            cancelButton = new JButton("Cancel");
            this.setLayout(new FlowLayout(FlowLayout.LEADING));
            JPanel btnPane = new JPanel(new GridLayout(1,2));
            btnPane.add(cancelButton);
            btnPane.add(okButton);
            this.setLocationRelativeTo(null);
            this.add(elementPane);
            this.add(btnPane);
            this.pack();
            this.setVisible(true);
            elementPane.setVisible(true);

            okButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    mainPane.drawPanelC.setImg( Filters.applyRotation(mainPane.drawPanelB.img, Integer.parseInt(elementPane.getVal()))  );
                    closeRotationFrame();
                }
            });
            cancelButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    closeRotationFrame();
                }
            });
        }
    }
    private class SobelFrame extends JFrame
    {
        SettingsElementPane elementPane;
        JButton okButton;;
        JButton cancelButton;

        void closeSobelFrame()
        {
            this.dispose();
        }
        SobelFrame getSobelFrameExemplar()
        {
            return this;
        }

        SobelFrame()
        {
            super("Apply Sobel");
            this.setPreferredSize(new Dimension(300,200));
            elementPane = new SettingsElementPane("Sobel");
            elementPane.setSlider(0,255,25,50,200);
            okButton = new JButton("OK");
            cancelButton = new JButton("Cancel");
            this.setLayout(new FlowLayout(FlowLayout.LEADING));
            JPanel btnPane = new JPanel(new GridLayout(1,2));
            btnPane.add(cancelButton);
            btnPane.add(okButton);
            this.setLocationRelativeTo(null);
            this.add(elementPane);
            this.add(btnPane);
            this.pack();
            this.setVisible(true);
            elementPane.setVisible(true);

            okButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    //mainPane.drawPanelC.setImg( Filters.applySobel(mainPane.drawPanelB.img, Integer.parseInt(elementPane.getVal()))  );
                    mainPane.drawPanelC.setImg(Filters.applySobel(Filters.applyGrayscale(mainPane.drawPanelB.img), Integer.parseInt(elementPane.getVal())));

                    closeSobelFrame();
                }
            });
            cancelButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    closeSobelFrame();
                }
            });
        }
    }

    private class GammaFrame extends JFrame
    {
        JButton okButton;;
        JButton cancelButton;
        JPanel setValuePane;
        JLabel label;
        JTextField field;

        void closeGammaFrame()
        {
            this.dispose();
        }
        GammaFrame getSobelFrameExemplar()
        {
            return this;
        }

        void initSetValuePanel()
        {
            label = new JLabel("Enter gamma parameter");
            field = new JTextField("1");
            field.setHorizontalAlignment(JTextField.CENTER);
            setValuePane = new JPanel();
            setValuePane.setLayout(new GridLayout(1,2));
            setValuePane.add(label);
            setValuePane.add(field);
        }
        GammaFrame()
        {
            super("Apply Gamma");
            this.setPreferredSize(new Dimension(300,200));
            okButton = new JButton("OK");
            cancelButton = new JButton("Cancel");
            this.setLayout(new FlowLayout(FlowLayout.LEADING));
            JPanel btnPane = new JPanel(new GridLayout(1,2));
            btnPane.add(cancelButton);
            btnPane.add(okButton);
            initSetValuePanel();
            this.setLocationRelativeTo(null);
            this.add(setValuePane);
            this.add(btnPane);
            this.pack();
            this.setVisible(true);

            okButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    //mainPane.drawPanelC.setImg( Filters.applySobel(mainPane.drawPanelB.img, Integer.parseInt(elementPane.getVal()))  );
                    mainPane.drawPanelC.setImg(Filters.applyGamma(mainPane.drawPanelB.img, Double.parseDouble(field.getText())));

                    closeGammaFrame();
                }
            });
            cancelButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    closeGammaFrame();
                }
            });
        }
    }

    private class SettingsElementPane extends JPanel
    {
        JSlider slider;
        JTextField textField;
        KeyAdapter keyAdapter;
        ChangeListener changeListener;
        int max, min;

        SettingsElementPane(String name)
        {
            min = -180; max = 180;
            this.setPreferredSize(new Dimension(300,50));
            setLayout(new FlowLayout(FlowLayout.LEFT));
            slider = new JSlider(JSlider.HORIZONTAL, min, max, 0);
            textField = new JTextField("" + 0, 5);
            initChangeListener();
            textField.setSize(50,20);
            slider.setMajorTickSpacing(60);
            slider.setMinorTickSpacing(20);
            slider.setPaintTicks(true);
            slider.setPaintLabels(true);

            slider.addChangeListener(changeListener);
            //this.setPreferredSize(new Dimension(380,50));

            this.add(slider);
            this.add(textField);
            this.add(new JLabel(name));
        }
        void setSlider(int min1, int max1, int minor1, int major1, int value1)
        {
            //slider = new JSlider(JSlider.HORIZONTAL, min1,max1,value1);
            slider.setMaximum(max1);
            slider.setMinimum(min1);
            slider.setMinorTickSpacing(minor1);
            slider.setMajorTickSpacing(major1);
            slider.setValue(value1);


        }
        String getVal()
        {
            return textField.getText();
        }

        void initChangeListener()
        {
            changeListener = new ChangeListener() {
                @Override
                public void stateChanged(ChangeEvent e) {
                    JSlider source = (JSlider)e.getSource();
                    textField.setText(source.getValue() + "");
                    //System.out.println(source.getValue());
                }
            };
        }
    }
}
