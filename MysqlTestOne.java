package DatabaseConnectivity;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Date;


class MysqlTestOne extends JFrame implements ActionListener {

    double amount;

    Banking b;
    Panel panel = new Panel(null);
    Font font = new Font("Comic Sans MS", Font.BOLD, 20);
    Font font1 = new Font("Arial Black", Font.BOLD, 14);

    JLabel lbl1 = new JLabel("__*********Welcome********__");


    JButton btn1 = new JButton("Deposit Money");
    JButton btn2 = new JButton("Withdraw Money");
    JButton btn3 = new JButton("Show StateMent");
    JButton btn4 = new JButton("Logout");

    public MysqlTestOne() {
        add(panel);

        setSize(700, 700);
        setTitle("Welcome To The Bank");
        setVisible(true);
        setDefaultCloseOperation(HIDE_ON_CLOSE);

        panel.add(lbl1);
        lbl1.setFont(font);
        panel.add(btn1);
        panel.add(btn2);
        panel.add(btn3);
        panel.add(btn4);

        lbl1.setBounds(540, 40, 700, 30);
        btn1.setBounds(550, 100, 300, 50);
        btn2.setBounds(550, 170, 300, 50);
        btn3.setBounds(550, 240, 300, 50);
        btn4.setBounds(1200, 10, 150, 20);

        btn1.addActionListener(this);
        btn2.addActionListener(this);
        btn3.addActionListener(this);
        btn4.addActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String mes = e.getActionCommand();

        String DRIVERCLASS = "com.mysql.jdbc.Driver";

        String DATABASE = "account";
        String URL = "jdbc:mysql://localhost:3306/";
        String USER = "root";
        String PASSWORD = "qwertyuiop";

        Connection connection;
        PreparedStatement pStatement;
        ResultSet result;

        Date d = new Date();
        String date = d.toString();

        double bal;

        if (mes.equalsIgnoreCase("Deposit Money")) {

            try {

                Class.forName(DRIVERCLASS);
                connection = DriverManager.getConnection(URL + DATABASE, USER, PASSWORD);

                String a;
                a = JOptionPane.showInputDialog("Enter Amount you Want to Deposit");


                amount = Double.parseDouble(a);
                JOptionPane.showConfirmDialog(this, "Are You sure to enter this amount " + a);
                //	if(JOptionPane.showConfirmDialog(null, "true"))
                //	{


                //	}


                pStatement = connection.prepareStatement("select balance from bankaccount where account_number=? and pass=?");

                pStatement.setString(1, Banking.acc_num);
                pStatement.setString(2, Banking.password);

                result = pStatement.executeQuery();

                while (result.next()) {
                    bal = result.getDouble("balance");

                    bal = bal + amount;

                    pStatement = connection.prepareStatement("update bankaccount set balance=? where account_number=? and pass=?");
                    pStatement.setDouble(1, bal);
                    pStatement.setString(2, Banking.acc_num);
                    pStatement.setString(3, Banking.password);

                    pStatement.executeUpdate();

                    pStatement = connection.prepareStatement("update bankaccount set last_amount_deposited=? where account_number=? and pass=?");
                    pStatement.setDouble(1, amount);
                    pStatement.setString(2, Banking.acc_num);
                    pStatement.setString(3, Banking.password);

                    pStatement.executeUpdate();

                    JOptionPane.showMessageDialog(this, "Amount Deposited : " + amount);

                    pStatement = connection.prepareStatement("insert into statement (account_number,deposited,balance,date) values (?,?,?,?)");
                    pStatement.setString(1, Banking.acc_num);
                    pStatement.setDouble(2, amount);
                    pStatement.setDouble(3, bal);
                    pStatement.setString(4, date);

                    pStatement.executeUpdate();

                }

            } catch (Exception e3) {
                e3.printStackTrace();
            }
        } else if (mes.equalsIgnoreCase("Withdraw Money")) {
            System.out.println(Banking.acc_num + Banking.password);
            try {

                Class.forName(DRIVERCLASS);
                connection = DriverManager.getConnection(URL + DATABASE, USER, PASSWORD);

                String a;
                a = JOptionPane.showInputDialog("Enter Amount you Want to Withdraw");

                amount = Double.parseDouble(a);

                pStatement = connection.prepareStatement("select balance from bankaccount where account_number=? and pass=?");

                pStatement.setString(1, Banking.acc_num);
                pStatement.setString(2, Banking.password);

                result = pStatement.executeQuery();

                while (result.next()) {
                    bal = result.getDouble("balance");

                    bal = bal - amount;

                    pStatement = connection.prepareStatement("update bankaccount set balance=? where account_number=? and pass=?");
                    pStatement.setDouble(1, bal);
                    pStatement.setString(2, Banking.acc_num);
                    pStatement.setString(3, Banking.password);

                    pStatement.executeUpdate();

                    pStatement = connection.prepareStatement("update bankaccount set last_amount_withdrawed=? where name=? and pass=?");
                    pStatement.setDouble(1, amount);
                    pStatement.setString(2, Banking.acc_num);
                    pStatement.setString(3, Banking.password);

                    pStatement.executeUpdate();

                    JOptionPane.showMessageDialog(this, "Amount Withdrawed : " + amount);

                    pStatement = connection.prepareStatement("insert into statement (account_number,withdrawed,balance,date) values (?,?,?,?)");
                    pStatement.setString(1, Banking.acc_num);
                    pStatement.setDouble(2, amount);
                    pStatement.setDouble(3, bal);
                    pStatement.setString(4, date);

                    pStatement.executeUpdate();

                }

            } catch (Exception e4) {
                e4.printStackTrace();
            }

        } else if (mes.equalsIgnoreCase("Show Statement")) {

            try {

                Class.forName(DRIVERCLASS);
                connection = DriverManager.getConnection(URL + DATABASE, USER, PASSWORD);

                JFrame frame = new JFrame("Final Account");

                Object columnNames[] = {"Account Number", "Withdrawed Money", "Deposited Money", "Balance Remained", "Date"};

                DefaultTableModel model = new DefaultTableModel();
                model.setColumnIdentifiers(columnNames);

                String textValue = Banking.text1.getText();

                int account_number;
                double withdraw;
                double deposit;
                double balance;
                String date1;

                pStatement = connection.prepareStatement("select * from statement where account_number=?");
                pStatement.setString(1, textValue);
                result = pStatement.executeQuery();

                if (result.next()) {
                    result.previous();

                    while (result.next()) {

                        account_number = result.getInt(1);
                        withdraw = result.getDouble(5);
                        deposit = result.getDouble(2);
                        balance = result.getDouble(3);
                        date1 = result.getString(4);


                        Object rowData[] = {account_number, withdraw, deposit, balance, date1};

                        model.addRow(rowData);

                        JTable table = new JTable(model);

                        JScrollPane scrollPane = new JScrollPane(table);
                        frame.add(scrollPane, BorderLayout.CENTER);


                    }

                }

                frame.setSize(1000, 600);
                frame.setVisible(true);

            } catch (Exception e5) {
                e5.printStackTrace();
            }

        } else if (mes.equalsIgnoreCase("Logout")) {
            System.exit(0);
        }
    }

}





