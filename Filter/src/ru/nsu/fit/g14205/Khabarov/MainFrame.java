package ru.nsu.fit.g14205.Khabarov;

import javafx.stage.FileChooser;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
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

        JButton fileButton = new JButton("File", new ImageIcon("resources/file.png"));
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

        ImageIcon aboutIcon = new ImageIcon("resources/question.png");
        about.setIcon(aboutIcon);
        ImageIcon saveIcon = new ImageIcon("resources/save.png");
        save.setIcon(saveIcon);
        ImageIcon clearIcon = new ImageIcon("resources/clear.png");
        clearButton.setIcon(clearIcon);
        ImageIcon settingsIcon = new ImageIcon("resources/gear.png");
        options.setIcon(settingsIcon);

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
                mainPane.drawPanelC.setImg(Filters.applySobel(Filters.applyGrayscale(mainPane.drawPanelB.img)));
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
                mainPane.drawPanelC.setImg(Filters.applyRotation(mainPane.drawPanelB.img, 28));
            }
        });
        options.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFrame optionsFrame = new JFrame("Options");
                optionsFrame.setSize(450,500);
                optionsFrame.setLocationRelativeTo(thislink);
                optionsFrame.setLayout(new FlowLayout(FlowLayout.LEADING));
                optionsFrame.add(new ControlsPane(drawPanel) );
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
                chooseFileFrame.setVisible(true);
                chooseFileFrame.setLocationRelativeTo(thislink);
                int returval = chooser.showOpenDialog(chooseFileFrame);
                if ( returval == JFileChooser.APPROVE_OPTION )
                {
                    File file = chooser.getSelectedFile();
                    chooseFileFrame.setVisible(false);
                }
            }
        });
        about.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showAuthorFrame();
            }
        });
        fileButton.addActionListener(fileOpener);
        menuBar = new JMenuBar();
        toolBar = new JToolBar("MaiNToolbar");
        toolBar.add(fileButton);
        toolBar.add(clearButton);
        toolBar.add(options);
        toolBar.add(save);
        toolBar.add(about);
        toolBar.add(grayButton);
        toolBar.add(sobelButton);
        toolBar.add(x2Button);
        toolBar.add(negativeButton);
        toolBar.add(embossButton);
        toolBar.add(rotationButton);
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
                chooseFileFrame.setVisible(true);
                int returval = chooser.showOpenDialog(chooseFileFrame);
                if ( returval == JFileChooser.APPROVE_OPTION )
                {
                    File file = chooser.getSelectedFile();
                    chooseFileFrame.setVisible(false);
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
        chooseFileFrame.setLocationRelativeTo(this);
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
}
