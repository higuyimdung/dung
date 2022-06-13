package baitaplon;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;

import com.mysql.cj.jdbc.Driver;

@SuppressWarnings("serial")
public class dangnhap extends JFrame {
	JLabel jl, jlheader, jluser, jlpass;
	JTextField jtuser;
	JPasswordField jpass;
	JButton btndangnhap, btnthoat;
	JCheckBox chk;
	ImageIcon icon;
	Connection conn;

	public dangnhap() throws HeadlessException {
		super();
		connect();
		display();
		Controls();
		readfile();
		events();
	}

	public void display() {
		setTitle("Đăng nhập");
		setSize(400, 270);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setResizable(false);

	}

	public void Controls() {
		JPanel jp = new JPanel();
		jp.setLayout(new BorderLayout());
		JPanel north = new JPanel();

		jlheader = new JLabel("Đăng nhập");
		jlheader.setFont(new Font("Times New Roman", Font.BOLD, 26));
		jlheader.setForeground(Color.red);
		north.add(jlheader);
		jp.add(north, BorderLayout.NORTH);

		JPanel west = new JPanel();
		jl = new JLabel();
		icon = new ImageIcon("img/account.png");
		jl.setIcon(icon);
		west.add(jl);
		jp.add(west, BorderLayout.WEST);

		JPanel center = new JPanel();
		center.setLayout(null);
		jluser = new JLabel("Username: ");
		jluser.setBounds(10, 30, 100, 30);
		jtuser = new JTextField();
		jtuser.setBounds(100, 30, 120, 23);
		jlpass = new JLabel("Password: ");
		jlpass.setBounds(10, 70, 100, 30);
		jpass = new JPasswordField();
		jpass.setBounds(100, 70, 120, 23);
		Border bor1 = BorderFactory.createLineBorder(Color.red);
		TitledBorder tb = new TitledBorder(bor1, "Thông tin đăng nhập");
		chk = new JCheckBox("Lưu thông tin đăng nhập");
		chk.setBounds(50, 110, 200, 30);
		center.add(jluser);
		center.add(jtuser);
		center.add(jlpass);
		center.add(jpass);
		center.setBorder(tb);
		center.add(chk);
		jp.add(center, BorderLayout.CENTER);

		JPanel south = new JPanel();
		south.setLayout(new FlowLayout(FlowLayout.CENTER));
		btndangnhap = new JButton("Đăng nhập", new ImageIcon("img/login.png"));
		btnthoat = new JButton("Thoát", new ImageIcon("img/exit.png"));
		south.add(btndangnhap);
		south.add(btnthoat);
		jp.add(south, BorderLayout.SOUTH);

		Container ct = getContentPane();
		ct.add(jp);
	}
	public void connect() {
		try {
			String strConn = "jdbc:mysql://localhost/quanlysinhvien?useUnicode=true&characterEncoding=utf-8&useSSL=false";
			Properties pro = new Properties();
			pro.put("user", "root");
			pro.put("password","");
			Driver dr = new Driver();
			conn = dr.connect(strConn, pro);
		
		}catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void readfile() {
		File file = new File("save_account.txt");
		String user = "", pass = "";
		try {
			FileReader fr = new FileReader(file);
			BufferedReader br = new BufferedReader(fr);
			try {
				user = br.readLine();
				pass = br.readLine();
				br.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}

		} catch (FileNotFoundException ex) {
			JOptionPane.showMessageDialog(dangnhap.this, "Không tìm thấy file", "Thông Báo",
					JOptionPane.INFORMATION_MESSAGE);
		}

		jtuser.setText(user);
		jpass.setText(pass);
		if (!user.equals(""))
			chk.setSelected(rootPaneCheckingEnabled);
	}

	public void events() {
		btndangnhap.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				String strUsername = jtuser.getText();
				String strPassword = String.valueOf(jpass.getPassword());
				if (strUsername.equals("") || strPassword.equals("")) {
					JOptionPane.showMessageDialog(dangnhap.this, "Chưa Nhập User or Pass", "Thông Báo",
							JOptionPane.INFORMATION_MESSAGE);
				} else {
					try {
						String sql = "select * from user where username= '" + strUsername + "' and password= '"
								+ strPassword + "'";
						Statement stm = conn.createStatement();
						ResultSet rs = stm.executeQuery(sql);
						String user = "", pass = "";
						if (chk.isSelected()) {
							user = strUsername;
							pass = strPassword;
						} else {
							user = "";
							pass = "";
						}
						try {
							File file = new File("save_account.txt");
							if (!file.exists()) {
								file.createNewFile();
							} else {
								FileWriter fw = new FileWriter(file.getAbsoluteFile());
								BufferedWriter bw = new BufferedWriter(fw);
								bw.write(user + "\n" + pass);
								bw.close();
							}
						} catch (IOException e) {
							e.printStackTrace();
						}
						if (rs.next()) {
							JOptionPane.showMessageDialog(dangnhap.this, "Đăng nhập thành công", "Thông Báo",
									JOptionPane.INFORMATION_MESSAGE);
							new sinhvien();
							dispose();
						} else
							JOptionPane.showMessageDialog(dangnhap.this, "Sai user or pass", "Thông Báo",
									JOptionPane.INFORMATION_MESSAGE);
					} catch (SQLException e) {
						e.printStackTrace();
					}

				}
			}
		});
		btnthoat.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				int yes = JOptionPane.showConfirmDialog(null, "Bạn có muốn thoát không", "Thông Báo",
						JOptionPane.YES_NO_OPTION);
				if (yes == JOptionPane.YES_OPTION)
					System.exit(0);
			}
		});
	}
}
