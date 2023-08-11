package com.server.http.view;

import com.server.http.ServerRun;
import com.server.http.view.util.NetworkUtil;
import com.server.http.view.util.PropertiesRW;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.io.*;
import java.util.List;
import java.util.stream.Collectors;

public class Welcome extends JFrame {
    private boolean serverIsRunning = false;
    private JPanel contentPane;
    private JLabel labelExit;
    private JLabel lblNotify = new JLabel("Server stopped");
    JTextField txtField = new JTextField();

    int xMouse, yMouse;

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    Welcome frame = new Welcome();
                    frame.setVisible(true);
                    //new PropertiesRW().write("Esto es una prueba");



                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

    }

    public Welcome() {
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
        lblNotify.setBounds(110, 10, 310, 19);
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
        header.setBackground(Color.WHITE);
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
                btnexit.setBackground(Color.white);
                labelExit.setForeground(Color.black);
            }
        });
        btnexit.setLayout(null);
        btnexit.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnexit.setBackground(Color.WHITE);
        btnexit.setBounds(460, 0, 53, 36);
        header.add(btnexit);

        labelExit = new JLabel("X");
        labelExit.setBounds(0, 0, 53, 36);
        btnexit.add(labelExit);
        labelExit.setHorizontalAlignment(SwingConstants.CENTER);
        labelExit.setFont(new Font("Roboto", Font.PLAIN, 18));

        //Botón Login
        JPanel btnLogin = new JPanel();
        btnLogin.setBounds(754, 300, 83, 70);
        btnLogin.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                //Login login = new Login();
                //login.setVisible(true);
                dispose();
            }
        });
        btnLogin.setLayout(null);
        btnLogin.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnLogin.setBackground(SystemColor.window);
        panel.add(btnLogin);

        // btn activator
        JPanel btnActivator = new JPanel();
        btnActivator.setBounds(200, 80, 100, 30);
        btnActivator.setLayout(null);
        btnActivator.setCursor(new java.awt.Cursor(Cursor.HAND_CURSOR));
        btnActivator.setBackground(Color.GRAY);
        panel.add(btnActivator);


        JLabel lblActivator = new JLabel("Activate");
        lblActivator.setBounds(0, 0, 100, 30);
        lblActivator.setHorizontalAlignment(SwingConstants.CENTER);
        lblActivator.setFont(new Font("Roboto", Font.PLAIN, 18));
        btnActivator.add(lblActivator);

        btnActivator.addMouseListener(new MouseAdapter() {
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

                            } else {
                                serverIsRunning = false;
                                new ServerRun().stop();
                                lblNotify.setText("Server stopped");

                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                btnActivator.setBackground(Color.lightGray);
                lblActivator.setForeground(Color.black);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                btnActivator.setBackground(Color.GRAY);
                lblActivator.setForeground(Color.black);
            }
        });


        // folder selector
        JPanel btnChooser = new JPanel();
        btnChooser.setBounds(200, 120, 100, 30);
        btnChooser.setLayout(null);
        btnChooser.setCursor(new java.awt.Cursor(Cursor.HAND_CURSOR));
        btnChooser.setBackground(Color.GRAY);
        panel.add(btnChooser);


        JLabel lblChosser = new JLabel("Folder Serve");
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
                            System.out.println("hello");


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
        pnlInputText.setBounds(200, 160, 150, 24);
        pnlInputText.setLayout(null);
        pnlInputText.setCursor(new java.awt.Cursor(Cursor.HAND_CURSOR));
        pnlInputText.setBackground(Color.GRAY);
        panel.add(pnlInputText);


        txtField.setBounds(0, 0, 150, 24);
        //String tsxt = new PropertiesRW().read().get(0).split(" ")[1];
        //txtField.setText(tsxt);
        txtField.setHorizontalAlignment(SwingConstants.LEFT);
        txtField.setFont(new Font("Roboto Light", Font.PLAIN, 14));
        pnlInputText.add(txtField);


        JLabel lblTitulo = new JLabel("IP Available:");
        lblTitulo.setBounds(10, 50, 150, 24);
        lblTitulo.setBackground(Color.GRAY);
        panel.add(lblTitulo);
        lblTitulo.setHorizontalAlignment(SwingConstants.CENTER);
        lblTitulo.setForeground(SystemColor.textHighlight);
        lblTitulo.setFont(new Font("Roboto Light", Font.PLAIN, 20));


        JPanel panelList = new JPanel();
        panelList.setBounds(30, 80, 150, 120);
        panelList.setBackground(Color.GRAY);
        panel.add(panelList);
        panelList.setLayout(new FlowLayout());

        new NetworkUtil().ipList().forEach((e) -> {
            panelList.add(new JLabel(e));
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
