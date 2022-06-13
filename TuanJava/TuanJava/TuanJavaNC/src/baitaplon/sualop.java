package baitaplon;

import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Properties;

import javax.swing.*;

import com.mysql.cj.jdbc.Driver;

@SuppressWarnings("serial")
public class sualop extends JFrame {
	JLabel jl, jlid, jlname;
	JTextField jtid, jtname;
	JButton btnluu, btnthoat;
	Connection conn;
	public sualop() throws HeadlessException {
		super();
		display();
		connect();
		Controls();
		events();
	}

	public void display() {
		setTitle("Sửa lớp");
		setSize(300, 200);
		setLocationRelativeTo(null);
		setVisible(true);
		setResizable(false);
	}
	public void connect() {
		try {
			String strConn = "jdbc:mysql://localhost/quanlysinhvien?useUnicode=true&characterEncoding=utf-8&useSSL=false";
			Properties pro = new Properties();
			pro.put("user", "root");
			pro.put("password", "");
			Driver dr = new Driver();
			conn = dr.connect(strConn, pro);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public void Controls() {
		JPanel jp = new JPanel();
		jp.setLayout(new BoxLayout(jp, BoxLayout.Y_AXIS));

		JPanel jp1 = new JPanel();
		jp1.setLayout(null);
		jp1.setPreferredSize(new Dimension(300, 120));
		jl = new JLabel("Sửa thông tin lớp");
		jl.setFont(new Font("Times New Roman", Font.BOLD, 18));
		jl.setForeground(Color.red);
		jl.setBounds(70, 10, 200, 30);
		jlid = new JLabel("Mã lớp:");
		jlid.setBounds(30, 50, 50, 30);
		jtid = new JTextField();
		jtid.setBounds(120, 50, 120, 23);
		jtid.setEditable(false);
		jlname = new JLabel("Tên Lớp:");
		jlname.setBounds(30, 80, 50, 30);
		jtname = new JTextField();
		jtname.setBounds(120, 80, 120, 23);
		jp1.add(jl);
		jp1.add(jlid);
		jp1.add(jtid);
		jp1.add(jlname);
		jp1.add(jtname);
		jp.add(jp1);

		JPanel jp2 = new JPanel();
		jp.setLayout(new FlowLayout(FlowLayout.CENTER));
		btnluu = new JButton("Lưu");
		btnthoat = new JButton("Thoát");
		jp2.add(btnluu);
		jp2.add(btnthoat);
		jp.add(jp2);

		Container ct = getContentPane();
		ct.add(jp);
	}

	public void events() {
		btnluu.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (jtname.getText().equals("")) {
					JOptionPane.showMessageDialog(null, "Mời nhập đầy đủ", "Thông Báo", JOptionPane.ERROR_MESSAGE);
					jtid.requestFocus();
				} else {
					try {
					String update = "UPDATE class SET name_class=? WHERE id_class=?";
					PreparedStatement pre = conn.prepareStatement(update);
					pre.setString(1, jtname.getText());
					pre.setString(2, jtid.getText());
					pre.executeUpdate();
					JOptionPane.showMessageDialog(null, "Sửa lớp thành công", "Thông Báo",
							JOptionPane.INFORMATION_MESSAGE);
					new sinhvien();
					dispose();
					}catch(SQLException ex) {
						JOptionPane.showMessageDialog(null, "Sửa lớp không thành công", "Thông Báo",
								JOptionPane.INFORMATION_MESSAGE);
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
					new sinhvien();
					dispose();

			}
		});
	}
}
