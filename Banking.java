package DatabaseConnectivity;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Date;

class Banking extends JFrame implements ActionListener {

    Panel panel = new Panel(null);
    Font font = new Font("Comic Sans MS", Font.BOLD, 20);
    Font font1 = new Font("Arial Black", Font.BOLD, 14);

    private JLabel lbl1 = new JLabel("Acc. No.");
    private JLabel lbl2 = new JLabel("Password");
    static JTextField text1 = new JTextField();
    private JPasswordField text2 = new JPasswordField();
    private JLabel lbl3 = new JLabel("------To Create New Account Fill the following Form------");

    private JLabel lbl5 = new JLabel("Name");
    private JLabel lbl6 = new JLabel("Address");
    private JLabel lbl7 = new JLabel("Password");
    private JLabel lbl8 = new JLabel("First Deposit");
    private JLabel lbl9 = new JLabel("You already have an Account?	      ENTER");
    private JLabel lbl10 = new JLabel("------------------------------------------------------------------------------------------------------------------------------------------------------------------");

    private JTextField text4 = new JTextField();
    private JTextField text5 = new JTextField();
    private JPasswordField pass = new JPasswordField();
    private JTextField text7 = new JTextField();

    private JButton btn1 = new JButton("Login");
    private JButton btn2 = new JButton("Save");
    private JButton btn3 = new JButton("Clear");

    public Banking() {
        add(panel);

        setSize(1300, 700);
        setTitle("Welcome To The Bank");
        setVisible(true);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        panel.add(lbl1);
        panel.add(lbl2);
        panel.add(lbl9);
        lbl9.setFont(font1);
        panel.add(lbl10);
        lbl10.setFont(font1);
        panel.add(text1);
        panel.add(text2);
        panel.add(btn1);
        panel.add(btn2);
        panel.add(btn3);

        panel.add(lbl3);
        lbl3.setFont(font);

        panel.add(lbl5);
        panel.add(lbl6);
        panel.add(lbl7);
        panel.add(lbl8);

        panel.add(text4);
        panel.add(text5);
        panel.add(pass);
        panel.add(text7);

        lbl5.setFont(font1);
        lbl6.setFont(font1);
        lbl7.setFont(font1);
        lbl8.setFont(font1);

        lbl10.setBounds(150, 60, 2000, 20);
        lbl9.setBounds(150, 10, 400, 20);
        lbl1.setBounds(540, 10, 80, 20);
        lbl2.setBounds(780, 10, 80, 20);
        text1.setBounds(600, 10, 150, 20);
        text2.setBounds(850, 10, 150, 20);
        btn1.setBounds(1020, 10, 100, 20);

        lbl3.setBounds(350, 120, 1000, 30);

        lbl5.setBounds(450, 270, 120, 20);
        lbl6.setBounds(450, 320, 120, 20);
        lbl7.setBounds(450, 370, 120, 20);
        lbl8.setBounds(450, 420, 120, 20);

        text4.setBounds(620, 270, 200, 20);
        text5.setBounds(620, 320, 200, 20);
        pass.setBounds(620, 370, 200, 20);
        text7.setBounds(620, 420, 200, 20);

        btn2.setBounds(500, 500, 100, 30);
        btn3.setBounds(700, 500, 100, 30);

        btn1.addActionListener(this);
        btn2.addActionListener(this);
        btn3.addActionListener(this);


    }


    static protected String acc_num;
    static protected String password;
    String name;
    String address;
    double first_deposite;

    Date d = new Date();
    String date = d.toString();

    public static void main(String[] args) {
        new Banking();
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

        if (mes.equalsIgnoreCase("login")) {
            acc_num = (text1.getText());
            password = (text2.getText());


            try {
                Class.forName(DRIVERCLASS);
                connection = DriverManager.getConnection(URL + DATABASE, USER, PASSWORD);

                pStatement = connection.prepareStatement("select * from bankaccount where name=? and pass=?");

                pStatement.setString(1, acc_num);
                pStatement.setString(2, password);

                result = pStatement.executeQuery();


                if (result.next()) {
                    new MysqlTestOne();
                } else {
                    JOptionPane.showMessageDialog(null, "INVALID LOGIN");
                    text1.setText("");
                    text2.setText("");
                }
            } catch (Exception e1) {
                e1.printStackTrace();
            }

        } else if (mes.equalsIgnoreCase("save")) {
            name = (text4.getText());
            address = (text5.getText());
            password = (pass.getText());
            first_deposite = Double.parseDouble(text7.getText());


            if (name.equalsIgnoreCase("") || address.equalsIgnoreCase("") || password.equalsIgnoreCase("")) {
                JOptionPane.showMessageDialog(null, "Account Not Created");

                text4.setText("");
                text5.setText("");
                pass.setText("");
                text7.setText("");

            } else {

                try {
                    Class.forName(DRIVERCLASS);
                    connection = DriverManager.getConnection(URL + DATABASE, USER, PASSWORD);

                    pStatement = connection.prepareStatement("insert into bankaccount (name,address,pass,first_deposite,balance) values (?,?,?,?,?)");
                    pStatement.setString(1, name);
                    pStatement.setString(2, address);
                    pStatement.setString(3, password);
                    pStatement.setDouble(4, first_deposite);
                    pStatement.setDouble(5, first_deposite);

                    pStatement.executeUpdate();

                    pStatement = connection.prepareStatement("select max(account_number) from bankaccount");
                    result = pStatement.executeQuery();


                    if (result.next()) {


                        int account_number = result.getInt(1);
                        JOptionPane.showMessageDialog(this, "Your Account Created with Account Number : " + account_number);

                        pStatement = connection.prepareStatement("insert into statement (account_number,deposited,balance,date) values (?,?,?,?)");
                        pStatement.setInt(1, account_number);
                        pStatement.setDouble(2, first_deposite);
                        pStatement.setDouble(3, first_deposite);
                        pStatement.setString(4, date);

                        pStatement.executeUpdate();

                        text4.setText("");
                        text5.setText("");
                        pass.setText("");
                        text7.setText("");
                    }


                } catch (Exception e1) {
                    e1.printStackTrace();
                }


            }
        } else if (mes.equalsIgnoreCase("clear")) {
            text4.setText("");
            text5.setText("");
            pass.setText("");
            text7.setText("");
        }
    }
}