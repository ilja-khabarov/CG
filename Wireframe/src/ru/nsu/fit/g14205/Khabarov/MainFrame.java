package ru.nsu.fit.g14205.Khabarov;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;

/**
 * Created by ilja on 03.04.2017.
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
    StatusBar statusBar;

    public MainFrame()
    {
        super();
        this.setSize(800,600);
        this.setMinimumSize(new Dimension(800,600));
        this.setLocationRelativeTo(null);
        initFileListener();
        thislink = this;

        JButton fileButton = new JButton("File", new ImageIcon("resources/file.png"));
        fileButton.setToolTipText("Open file");
        JButton clearButton = new JButton();
        clearButton.setToolTipText("Clear the field");
        JButton options = new JButton("Opts");
        options.setToolTipText("Options");
        JButton save = new JButton("Save");
        save.setToolTipText("Save into file");
        JButton about = new JButton();
        about.setToolTipText("About program");

        ImageIcon aboutIcon = new ImageIcon("resources/question.png");
        about.setIcon(aboutIcon);
        ImageIcon saveIcon = new ImageIcon("resources/save.png");
        save.setIcon(saveIcon);
        ImageIcon clearIcon = new ImageIcon("resources/clear.png");
        clearButton.setIcon(clearIcon);
        ImageIcon settingsIcon = new ImageIcon("resources/gear.png");
        options.setIcon(settingsIcon);

        drawPanel = new DrawPanel();
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
            }

        });
        save.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                initChoseFileFrame();
                //chooseFileFrame.setVisible(true);
                chooseFileFrame.setLocationRelativeTo(thislink);
                chooseFileFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
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
        JScrollPane scrollPane = new JScrollPane(drawPanel);
        scrollPane.setPreferredSize(new Dimension(800,500));
        drawPanel.setLayout(new BoxLayout(drawPanel, BoxLayout.Y_AXIS));
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        //scrollPane.setSize(800,500);

        //this.add(drawPanel);
        this.add(scrollPane, BorderLayout.CENTER);

        /*this.addWindowListener(new WindowAdapter() {
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

        });*/


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
                //chooseFileFrame.setVisible(true);
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