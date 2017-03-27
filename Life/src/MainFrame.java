import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.net.URL;

/**
 * Created by ilja on 15.02.2017.
 */
public class MainFrame extends JFrame
{
    private JMenuBar menuBar;
    private JToolBar toolBar;
    DrawPanel drawPanel;

    public MainFrame()
    {
        super();
        this.setSize(800,600);
        this.setLocationRelativeTo(null);

        JButton clearButton = new JButton();
        JButton next = new JButton(("Next"));
        JButton xorButton = new JButton("R");
        JButton options = new JButton("Opts");
        JButton start = new JButton("Start");
        start.setToolTipText("Start game!");
        JButton stop = new JButton("Stop");

        ImageIcon startIcon = new ImageIcon("resources/play.png");
        start.setIcon(startIcon);
        ImageIcon nextIcon = new ImageIcon("resources/next.png");
        next.setIcon(nextIcon);
        ImageIcon clearIcon = new ImageIcon("resources/clear.png");
        clearButton.setIcon(clearIcon);
        ImageIcon settingsIcon = new ImageIcon("resources/gear.png");
        options.setIcon(settingsIcon);
        ImageIcon xorIcon = new ImageIcon("resources/replace.png");
        xorButton.setIcon(xorIcon);
        ImageIcon stopIcon = new ImageIcon("resources/stop.png");
        stop.setIcon(stopIcon);
        start.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                drawPanel.start();
            }
        });
        stop.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                drawPanel.stop();
            }
        });

        next.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                drawPanel.initModel();
                drawPanel.lifeModel.bypass();
                drawPanel.drawFieldFromModel();
            }
        });
        options.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFrame optionsFrame = new JFrame("Options");
                optionsFrame.setSize(450,500);
                optionsFrame.setLocationRelativeTo(null);
                optionsFrame.setLayout(new FlowLayout(FlowLayout.LEADING));
                optionsFrame.add(new ControlsPane() );
                optionsFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                optionsFrame.setVisible(true);
            }
        });

        clearButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("Command caught: " + e.getActionCommand() );
                drawPanel.clearSockets();
            }

        });
        xorButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (drawPanel.xormode)
                {
                    drawPanel.setXormode(false);
                    xorButton.setText("R");
                }
                else {
                    drawPanel.setXormode(true);
                    xorButton.setText("X");
                }
                drawPanel.repaint();
                xorButton.repaint();
            }
        });
        menuBar = new JMenuBar();
        toolBar = new JToolBar("MaiNToolbar");
        toolBar.add(clearButton);
        toolBar.add(xorButton);
        toolBar.add(options);
        toolBar.add(next);
        toolBar.add(start);
        toolBar.add(stop);
        menuBar.setVisible(true);

        JMenu menu = new JMenu("File");
        menu.setMnemonic(KeyEvent.VK_A);
        menu.getAccessibleContext().setAccessibleDescription(
                "The only menu in this program that has menu items");
        JMenuItem menuItem = new JMenuItem("Open" );
        menu.add(menuItem);
        JMenu help = new JMenu("Help");
        help.setMnemonic(KeyEvent.VK_B);
        JMenuItem aboutAuthor= new JMenuItem("About author");
        help.add(aboutAuthor);
        aboutAuthor.addMouseListener(new MouseListener() {
            @Override
            public void mousePressed(MouseEvent e) {
                JFrame authorFrame = new JFrame("About author");
                authorFrame.setSize(300, 100 );
                authorFrame.setLayout(new GridLayout(2, 1));
                JLabel textAboutVersion = new JLabel("Dis version is not certain");
                textAboutVersion.setSize(300,50);
                JLabel textAboutAuthor = new JLabel("FIT Khabarov, 13203");
                textAboutAuthor.setSize(300,50);
                authorFrame.add(textAboutVersion);
                authorFrame.add(textAboutAuthor);
                authorFrame.setVisible(true);
                authorFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
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

        drawPanel = new DrawPanel();
        drawPanel.setBackground(Color.white);
        //drawPanel.setSize(800,500);
        JScrollPane scrollPane = new JScrollPane(drawPanel);
        scrollPane.setPreferredSize(new Dimension(800,500));
        drawPanel.setLayout(new BoxLayout(drawPanel, BoxLayout.Y_AXIS));
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        //scrollPane.setSize(800,500);

        //this.add(drawPanel);
        this.add(scrollPane, BorderLayout.CENTER);


        this.setVisible(true);
        this.pack();
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}
