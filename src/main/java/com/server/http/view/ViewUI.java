package com.server.http.view;

import com.server.http.ServerRun;
import com.server.http.utils.network.NetworkUtil;
import com.server.http.utils.properties.PropertiesService;
import com.server.http.utils.properties.PropertiesValidate;
import com.server.http.utils.properties.Property;
import com.server.http.utils.properties.PropertyName;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;

public class ViewUI extends JFrame {
    private boolean serverIsRunning = false;
    private JPanel contentPane;
    private JPanel pnlActivator = new JPanel();
    private JLabel labelExit;
    private JLabel lblNotify = new JLabel("Server stopped");
    JTextField txtField = new JTextField();

    private JLabel lblActivator = new JLabel("Inactive");

    int xMouse, yMouse;

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    ViewUI frame = new ViewUI();

                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public ViewUI() {
        new PropertiesValidate();
        //setIconImage(Toolkit.getDefaultToolkit().getImage(Welcome.class.getResource("/imagenes/aH-40px.png")));
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setBounds(100, 100, 510, 300);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

        setContentPane(contentPane);
        contentPane.setLayout(null);
        setResizable(false);
        setLocationRelativeTo(null);
        setUndecorated(true);


        Panel panel = new Panel();
        panel.setBackground(SystemColor.darkGray);
        panel.setBounds(0, 0, 510, 300);
        contentPane.add(panel);
        panel.setLayout(null);


        JPanel pnlNotify = new JPanel();
        pnlNotify.setBounds(0, 263, 510, 37);
        pnlNotify.setBackground(new Color(12, 138, 199));
        panel.add(pnlNotify);
        pnlNotify.setLayout(null);


        //lblNotify = new JLabel(message);
        lblNotify.setBounds(10, 10, 450, 19);
        lblNotify.setForeground(new Color(240, 248, 255));
        lblNotify.setFont(new Font("Montserrat", Font.PLAIN, 16));
        pnlNotify.add(lblNotify);


        //Barra para controlar la ventana
        JPanel header = new JPanel();
        header.setBounds(0, 0, 910, 36);
        header.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                headerMouseDragged(e);

            }
        });
        header.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                headerMousePressed(e);
            }
        });
        header.setLayout(null);
        header.setBackground(Color.darkGray);
        panel.add(header);

        //Botón salir
        JPanel btnexit = new JPanel();
        btnexit.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                System.exit(0);
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                btnexit.setBackground(Color.red);
                labelExit.setForeground(Color.white);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                btnexit.setBackground(Color.darkGray);
                labelExit.setForeground(Color.gray);
            }
        });
        btnexit.setLayout(null);
        btnexit.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnexit.setBackground(Color.darkGray);
        btnexit.setBounds(460, 0, 53, 36);
        header.add(btnexit);

        labelExit = new JLabel("X");
        labelExit.setBounds(0, 0, 53, 36);
        btnexit.add(labelExit);
        labelExit.setHorizontalAlignment(SwingConstants.CENTER);
        labelExit.setForeground(Color.GRAY);
        labelExit.setFont(new Font("Roboto", Font.PLAIN, 18));

        // btn activator
        pnlActivator.setBounds(150, 80, 100, 30);
        pnlActivator.setLayout(null);
        pnlActivator.setCursor(new java.awt.Cursor(Cursor.HAND_CURSOR));
        pnlActivator.setBackground(Color.GRAY);
        panel.add(pnlActivator);


        lblActivator.setBounds(0, 0, 100, 30);
        lblActivator.setHorizontalAlignment(SwingConstants.CENTER);
        lblActivator.setFont(new Font("Roboto", Font.PLAIN, 18));
        pnlActivator.add(lblActivator);

        pnlActivator.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                EventQueue.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            if (!serverIsRunning) {
                                serverIsRunning = true;
                                new ServerRun().start();
                                lblNotify.setText("Server is running");
                                lblActivator.setText("Active");
                                pnlActivator.setBackground(Color.green);

                            } else {
                                serverIsRunning = false;
                                new ServerRun().stop();
                                lblNotify.setText("Server stopped");
                                lblActivator.setText("Inactive");
                                pnlActivator.setBackground(Color.lightGray);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
            }


            @Override
            public void mouseEntered(MouseEvent e) {
                //pnlActivator.setBackground(Color.lightGray);
                if (!serverIsRunning) {
                    pnlActivator.setBackground(Color.green);
                } else {
                    pnlActivator.setBackground(Color.GRAY);
                }
            }

            @Override
            public void mouseExited(MouseEvent e) {

                if (!serverIsRunning) {
                    pnlActivator.setBackground(Color.GRAY);
                } else {
                    pnlActivator.setBackground(Color.green);
                }


            }
        });


        // folder selector
        JPanel btnChooser = new JPanel();
        btnChooser.setBounds(150, 120, 100, 30);
        btnChooser.setLayout(null);
        btnChooser.setCursor(new java.awt.Cursor(Cursor.HAND_CURSOR));
        btnChooser.setBackground(Color.GRAY);
        panel.add(btnChooser);


        JLabel lblChosser = new JLabel("Folder save");
        lblChosser.setBounds(0, 0, 100, 30);
        lblChosser.setHorizontalAlignment(SwingConstants.CENTER);
        lblChosser.setFont(new Font("Roboto", Font.PLAIN, 15));
        btnChooser.add(lblChosser);


        btnChooser.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                EventQueue.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            //new PropertiesValidate().assignPathServer(txtField.getText());
                            new PropertiesService().deleteProperty(PropertyName.PATH_SERVER);
                            new PropertiesService().saveProperty(new Property(PropertyName.PATH_SERVER, txtField.getText()));
                            //new JFileChooser();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                btnChooser.setBackground(Color.lightGray);
                lblChosser.setForeground(Color.black);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                btnChooser.setBackground(Color.GRAY);
                lblChosser.setForeground(Color.black);
            }
        });


        JPanel pnlInputText = new JPanel();
        pnlInputText.setBounds(150, 160, 150, 24);
        pnlInputText.setLayout(null);
        pnlInputText.setCursor(new java.awt.Cursor(Cursor.HAND_CURSOR));
        pnlInputText.setBackground(Color.GRAY);
        panel.add(pnlInputText);


        txtField.setBounds(0, 0, 150, 24);
        //String tsxt = new PropertiesRW().read().get(0).split(" ")[1];
        txtField.setText(new PropertiesService().getPropertyByName(PropertyName.PATH_SERVER).getValue());
        txtField.setHorizontalAlignment(SwingConstants.LEFT);
        txtField.setFont(new Font("Roboto Light", Font.PLAIN, 14));
        pnlInputText.add(txtField);


        JLabel lblTitulo = new JLabel("IP Available:");
        lblTitulo.setBounds(10, 50, 150, 24);
        lblTitulo.setBackground(Color.GRAY);
        panel.add(lblTitulo);
        lblTitulo.setHorizontalAlignment(SwingConstants.CENTER);
        lblTitulo.setForeground(SystemColor.textHighlight);
        lblTitulo.setFont(new Font("Roboto Light", Font.PLAIN, 18));


        JPanel pnlIpList = new JPanel();
        pnlIpList.setBounds(30, 80, 110, 70);
        pnlIpList.setBackground(Color.GRAY);
        panel.add(pnlIpList);
        pnlIpList.setLayout(new FlowLayout());

        new NetworkUtil().ipList().forEach((e) -> {
            pnlIpList.add(new JLabel(e));
        });
    }

    private void headerMousePressed(java.awt.event.MouseEvent evt) {
        xMouse = evt.getX();
        yMouse = evt.getY();
    }

    private void headerMouseDragged(java.awt.event.MouseEvent evt) {
        int x = evt.getXOnScreen();
        int y = evt.getYOnScreen();
        this.setLocation(x - xMouse, y - yMouse);
    }
}
