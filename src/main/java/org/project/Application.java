package org.project;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Application extends JFrame {
    private final JPanel panel = new JPanel();
    private final JButton button1 = new JButton("SQL"), button2 = new JButton("Exit");
    public Application() {
        this.setTitle("New Application");
        this.setSize(430, 220);
        this.setLocation(500, 250);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);

        Dimension buttonsSize = new Dimension(100, 50);
        button1.setPreferredSize(buttonsSize);
        button1.setFocusable(false);
        button2.setPreferredSize(buttonsSize);
        button2.setFocusable(false);
        panel.setLayout(new FlowLayout(FlowLayout.CENTER));
        panel.add(button1);
        panel.add(button2);
        settingButtons();

        Container container = this.getContentPane();
        container.add(panel, BorderLayout.CENTER);
    }
    public void start() {
        this.setVisible(true);
    }
    public void exit() {
        this.dispose();
        System.exit(0);
    }
    private void settingButtons() {
        button1.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                final JDialog dialog = new JDialog();
                final JTextField url = new JTextField(), name = new JTextField();
                final JPasswordField password = new JPasswordField();
                JLabel urlLabel = new JLabel("URL"), nameLabel = new JLabel("Name"), passwordLabel = new JLabel("Password");
                JButton con = new JButton("Connect");
                SpringLayout layout = new SpringLayout();
                con.setFocusable(false);
                Dimension size = new Dimension(70, 35);
                url.setPreferredSize(size);
                name.setPreferredSize(size);
                password.setPreferredSize(size);

                Container container = dialog.getContentPane();
                container.setLayout(layout);
                container.add(url);
                container.add(name);
                container.add(password);
                container.add(con);
                container.add(urlLabel);
                container.add(nameLabel);
                container.add(passwordLabel);

                layout.putConstraint(SpringLayout.WEST , url, 10,
                        SpringLayout.WEST , container);
                layout.putConstraint(SpringLayout.NORTH, url, 20,
                        SpringLayout.NORTH, container);
                layout.putConstraint(SpringLayout.WEST , urlLabel, 35,
                        SpringLayout.WEST , container);
                layout.putConstraint(SpringLayout.NORTH, urlLabel, 55,
                        SpringLayout.NORTH, container);
                layout.putConstraint(SpringLayout.NORTH, name, 20,
                        SpringLayout.NORTH, container);
                layout.putConstraint(SpringLayout.WEST , name, 125,
                        SpringLayout.WEST , container);
                layout.putConstraint(SpringLayout.WEST , nameLabel, 145,
                        SpringLayout.WEST , container);
                layout.putConstraint(SpringLayout.NORTH, nameLabel, 55,
                        SpringLayout.NORTH, container);
                layout.putConstraint(SpringLayout.NORTH, password, 20,
                        SpringLayout.NORTH, container);
                layout.putConstraint(SpringLayout.WEST , password, 235,
                        SpringLayout.WEST , container);
                layout.putConstraint(SpringLayout.WEST , passwordLabel, 240,
                        SpringLayout.WEST , container);
                layout.putConstraint(SpringLayout.NORTH, passwordLabel, 55,
                        SpringLayout.NORTH, container);
                layout.putConstraint(SpringLayout.NORTH, con, 100,
                        SpringLayout.NORTH, container);
                layout.putConstraint(SpringLayout.WEST , con, 115,
                        SpringLayout.WEST , container);

                dialog.setFocusable(false);
                dialog.setAlwaysOnTop(true);
                dialog.setResizable(false);
                dialog.setLocation(700, 300);
                dialog.setSize(330, 200);
                dialog.setTitle("Подключиться к базе данных");
                dialog.setVisible(true);

                con.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        try {
                            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
                        } catch (ClassNotFoundException ex) {
                            throw new RuntimeException(ex);
                        }
                        String urlText = "jdbc:sqlserver://" + url.getText() + ";Database=NewDataBase;encrypt=true;trustServerCertificate=true";
                        String nameText = name.getText();
                        char[] passwordText = password.getPassword();
                        try(Connection connection = DriverManager.getConnection(urlText, nameText, String.valueOf(passwordText))) {
                            dialog.dispose();
                            ResultSet result = connection.prepareStatement("select Products.product_name, " +
                                    "Categories.category_name from Products join Categories on product_category_id=category_id").executeQuery();

                            String[] table1 = new String[6];
                            String[] table2 = new String[6];
                            int temp = 0;
                            while(result.next()) {
                                table1[temp++] = (result.getString("product_name"));
                                table2[temp++] = (result.getString("category_name"));
                            }
                            JDialog listDialog = new JDialog();
                            listDialog.setLayout(new FlowLayout(FlowLayout.LEADING));
                            JList<String> listTable1 = new JList<>(table1);
                            JList<String> listTable2 = new JList<>(table2);

                            listDialog.setSize(200, 200);
                            listDialog.setLocation(700, 300);
                            listDialog.add(listTable1);
                            listDialog.add(listTable2);
                            listDialog.setVisible(true);

                            result.close();
                        } catch (SQLException ex) {
                            throw new RuntimeException(ex);
                        }
                    }
                });
            }
        });
        button2.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                exit();
            }
        });
    }
}
