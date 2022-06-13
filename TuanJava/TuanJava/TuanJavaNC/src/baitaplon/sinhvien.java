package baitaplon;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;
import java.util.Vector;

import javax.swing.*;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;
import javax.swing.tree.*;

import com.mysql.cj.jdbc.Driver;

@SuppressWarnings("serial")
public class sinhvien extends JFrame {
	Connection conn;
	JLabel jlheader, jlid, jlname, jlage, jlcombo;
	JTextField jtid, jtname, jtage;
	JButton jbthem, jbsua, jbxoa, jbshow;
	DefaultTableModel dtm;
	JTable jtb;
	JMenuItem item, item1, item2;
	JTree jtr;
	DefaultTreeModel treeModel;
	DefaultMutableTreeNode root, node;
	JPopupMenu popmenu;
	JComboBox<String> jcb;
	lop lop;

	public sinhvien() throws HeadlessException {
		super();
		display();
		connect();
		Controls();
		showtable();
		showtree();
		showcombo();
		events();
	}

	public void display() {
		setTitle("Quản Lý Sinh Viên");
		setSize(600, 400);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setVisible(true);
		setResizable(false);
	}

	public void Controls() {
		JPanel jp = new JPanel();
		jp.setLayout(new BorderLayout());

		JPanel north = new JPanel();
		jlheader = new JLabel("Quản lý sinh viên");
		jlheader.setFont(new Font("Times New Roman", Font.BOLD, 24));
		jlheader.setForeground(Color.red);
		north.add(jlheader);
		jp.add(north, BorderLayout.NORTH);

		JPanel west = new JPanel();
		west.setPreferredSize(new Dimension(270, 0));
		JPanel west1 = new JPanel();
		popmenu = new JPopupMenu();
		item = new JMenuItem("Insert", new ImageIcon("img/them.png"));
		item1 = new JMenuItem("Update", new ImageIcon("img/sua.png"));
		item2 = new JMenuItem("Delete", new ImageIcon("img/xoa.png"));
		popmenu.add(item);
		popmenu.add(item1);
		popmenu.add(item2);
		root = new DefaultMutableTreeNode("Danh sách các lớp");
		treeModel = new DefaultTreeModel(root);
		jtr = new JTree(treeModel);
		JScrollPane sc = new JScrollPane(jtr, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		sc.setPreferredSize(new Dimension(250, 250));
		jbshow = new JButton("Danh sách sinh viên", new ImageIcon("img/show.png"));
		west1.add(sc);
		west.add(west1);
		west.add(jbshow);
		jp.add(west, BorderLayout.WEST);

		JPanel center = new JPanel();
		center.setLayout(new BoxLayout(center, BoxLayout.Y_AXIS));
		jp.add(center, BorderLayout.CENTER);

		JPanel jp1 = new JPanel();
		dtm = new DefaultTableModel();
		dtm.addColumn("Mã Sinh Viên");
		dtm.addColumn("Tên Sinh Viên");
		dtm.addColumn("Tuổi Sinh Viên");
		dtm.addColumn("Lớp");
		jtb = new JTable(dtm) {
			public boolean isCellEditable(int row, int col) {
				return false;
			}
		};
		jtb.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		TableColumnModel col = jtb.getColumnModel();
		col.getColumn(0).setPreferredWidth(90);
		col.getColumn(1).setPreferredWidth(90);
		col.getColumn(2).setPreferredWidth(100);
		col.getColumn(3).setPreferredWidth(100);
		JScrollPane sc1 = new JScrollPane(jtb, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		sc1.setPreferredSize(new Dimension(320, 150));
		jp1.add(sc1);
		center.add(jp1);

		JPanel jp2 = new JPanel();
		jp2.setLayout(null);
		jp2.setPreferredSize(new Dimension(300, 120));
		jlid = new JLabel("Mã Sinh Viên :");
		jlid.setBounds(10, 0, 100, 30);
		jtid = new JTextField();
		jtid.setBounds(120, 0, 120, 22);
		jlname = new JLabel("Tên Sinh Viên :");
		jlname.setBounds(10, 30, 100, 30);
		jtname = new JTextField();
		jtname.setBounds(120, 30, 120, 22);
		jlage = new JLabel("Tuổi Sinh Viên :");
		jlage.setBounds(10, 60, 100, 30);
		jtage = new JTextField();
		jtage.setBounds(120, 60, 120, 22);
		jlcombo = new JLabel("Lớp :");
		jlcombo.setBounds(10, 90, 100, 30);
		jcb = new JComboBox<String>();
		jcb.setBounds(120, 90, 150, 25);
		jp2.add(jlid);
		jp2.add(jtid);
		jp2.add(jlname);
		jp2.add(jtname);
		jp2.add(jlage);
		jp2.add(jtage);
		jp2.add(jlcombo);
		jp2.add(jcb);

		center.add(jp2);

		JPanel jp3 = new JPanel();
		jp3.setLayout(new FlowLayout(FlowLayout.CENTER));
		jbthem = new JButton("Thêm", new ImageIcon("img/insert.png"));
		jbsua = new JButton("Lưu", new ImageIcon("img/save.png"));
		jbsua.setEnabled(false);
		jbxoa = new JButton("Xóa", new ImageIcon("img/delete.png"));
		jbxoa.setEnabled(false);
		jp3.add(jbthem);
		jp3.add(jbsua);
		jp3.add(jbxoa);
		center.add(jp3);

		Container ct = getContentPane();
		ct.add(jp);

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

	public void showtree() {
		try {
			String sql = "select * from class";
			Statement stm = conn.createStatement();
			ResultSet rs = stm.executeQuery(sql);
			while (rs.next()) {
				lop = new lop(rs.getString(1), rs.getString(2));
				node = new DefaultMutableTreeNode(lop);
				root.add(node);
			}
			jtr.expandRow(0);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void showtable() {
		try {
			String sql = "select * from students";
			Statement stm = conn.createStatement();
			ResultSet rs = stm.executeQuery(sql);
			ResultSetMetaData rsm = rs.getMetaData();
			int col = rsm.getColumnCount();
			while (rs.next()) {
				Vector<String> temp = new Vector<String>();
				for (int i = 1; i <= col; i++)
					temp.add(rs.getString(i));
				dtm.addRow(temp);
			}
			jtb.setModel(dtm);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void showcombo() {
		try {
			String sql = "select * from class";
			Statement stm = conn.createStatement();
			ResultSet rs = stm.executeQuery(sql);
			while (rs.next()) {
				lop = new lop(rs.getString(1), rs.getString(2));
				jcb.addItem(lop.getMalop());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void showStudentInclass(String lop) {
		try {
			String sql = "select * from students where id_class='" + lop + "'";
			ResultSet rs = conn.createStatement().executeQuery(sql);
			ResultSetMetaData rsm = rs.getMetaData();
			int col = rsm.getColumnCount();
			while (rs.next()) {
				Vector<String> temp = new Vector<String>();
				for (int i = 1; i <= col; i++)
					temp.add(rs.getString(i));
				dtm.addRow(temp);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void events() {
		jbthem.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				int id = 0, age = 0;
				if (jtname.getText().equals("") && jtid.getText().equals("") && jtage.getText().equals("")) {
					JOptionPane.showMessageDialog(null, "Mời nhập đầy đủ", "Thông Báo", JOptionPane.ERROR_MESSAGE);
					jtid.requestFocus();
				} else {
					try {
						id = Integer.parseInt(jtid.getText());
					} catch (Exception e2) {
						if (jtid.getText().equals(""))
							JOptionPane.showMessageDialog(null, "Mời nhập mã sinh viên !", "Thông Báo",
									JOptionPane.INFORMATION_MESSAGE);
						else
							JOptionPane.showMessageDialog(null, "Mã sinh viên phải là số !", "Thông Báo",
									JOptionPane.INFORMATION_MESSAGE);
						return;
					}
					try {
						id = Integer.parseInt(jtid.getText());
					} catch (Exception e2) {
						JOptionPane.showMessageDialog(null, "Mời nhập tên sinh viên !", "Thông Báo",
								JOptionPane.INFORMATION_MESSAGE);
					}
					try {
						if (jtname.getText().equals("")) {
							JOptionPane.showMessageDialog(null, "Mời nhập tên sinh viên !", "Thông Báo",
									JOptionPane.INFORMATION_MESSAGE);
							return;
						}
					} catch (Exception e2) {

					}
					try {
						age = Integer.parseInt(jtage.getText());
					} catch (Exception e2) {
						if (jtage.getText().equals(""))
							JOptionPane.showMessageDialog(null, "Mời nhập tuổi sinh viên !", "Thông Báo",
									JOptionPane.INFORMATION_MESSAGE);
						else
							JOptionPane.showMessageDialog(null, "Tuổi sinh viên phải là số !", "Thông Báo",
									JOptionPane.INFORMATION_MESSAGE);
						return;
					}
					try {
						
							String insert = "insert into students values (?,?,?,?)";
							PreparedStatement pre = conn.prepareStatement(insert);
							pre.setInt(1, id);
							pre.setString(2, jtname.getText());
							pre.setInt(3, age);
							pre.setString(4, jcb.getSelectedItem().toString());
							pre.executeUpdate();
							JOptionPane.showMessageDialog(null, "Thêm thành công", "Thông Báo",
									JOptionPane.INFORMATION_MESSAGE);
							jtid.setText("");
							jtname.setText("");
							jtage.setText("");
							dtm.setRowCount(0);
;
						
					} catch (SQLException ex) {
						JOptionPane.showMessageDialog(null, "Mã ID đã tồn tại", "Thông Báo",
								JOptionPane.INFORMATION_MESSAGE);
					}
				}
			}
		});
		jbsua.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				int age = 0;
				int chon = jtb.getSelectedRow();
				if (chon == -1) {
					JOptionPane.showMessageDialog(null, "Mời chọn dòng cần lưu", "Thông Báo",
							JOptionPane.INFORMATION_MESSAGE);
				} else {
					try {
						age = Integer.parseInt(jtage.getText());
					} catch (Exception e2) {
						if (jtage.getText().equals(""))
							JOptionPane.showMessageDialog(null, "Mời nhập tuổi sinh viên !", "Thông Báo",
									JOptionPane.INFORMATION_MESSAGE);
						else
							JOptionPane.showMessageDialog(null, "Tuổi sinh viên phải là số !", "Thông Báo",
									JOptionPane.INFORMATION_MESSAGE);
						return;
					}
					try {
							String update = "update students set name=?, age=?, id_class=? where id=?";
							PreparedStatement pre = conn.prepareStatement(update);
							pre.setString(1, jtname.getText());
							pre.setInt(2, age);
							pre.setString(3, jcb.getSelectedItem().toString());
							pre.setString(4, jtid.getText());
							pre.executeUpdate();
							JOptionPane.showMessageDialog(null, "Lưu thành công", "Thông Báo",
									JOptionPane.INFORMATION_MESSAGE);
							jtid.setEditable(true);
							jbthem.setEnabled(true);
							jbsua.setEnabled(false);
							jbxoa.setEnabled(false);
							jtid.setText("");
							jtname.setText("");
							jtage.setText("");
							dtm.setRowCount(0);
							showtable();
						
					} catch (SQLException ex) {
						JOptionPane.showMessageDialog(null, "Lưu không thành công", "Thông Báo",
								JOptionPane.INFORMATION_MESSAGE);
					}
				}
			}
		});
		jbxoa.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				int chon = jtb.getSelectedRow();
				if (chon == -1)
					JOptionPane.showMessageDialog(null, "Mời chọn dòng cần xóa", "Thông Báo",
							JOptionPane.INFORMATION_MESSAGE);
				else if (JOptionPane.showConfirmDialog(null, "Bạn có muốn xóa không", "Thông Báo",
						JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
					try {
						String delete = "delete from students where id=?";
						PreparedStatement pre = conn.prepareStatement(delete);
						pre.setString(1, jtid.getText());
						pre.executeUpdate();
						JOptionPane.showMessageDialog(null, "Xóa thành công", "Thông Báo",
								JOptionPane.INFORMATION_MESSAGE);
						jtid.setEditable(true);
						jbthem.setEnabled(true);
						jbsua.setEnabled(false);
						jbxoa.setEnabled(false);
						jtid.setText("");
						jtname.setText("");
						jtage.setText("");
						dtm.removeRow(chon);
					} catch (Exception e2) {
						e2.printStackTrace();
					}
				}
			}
		});
		jbshow.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				dtm.setRowCount(0);
				showtable();

			}
		});
		jtb.addMouseListener(new java.awt.event.MouseAdapter() {

			public void mouseClicked(MouseEvent e) {
				int dachon = jtb.getSelectedRow();
				jtid.setText((String) (jtb.getValueAt(dachon, 0)));
				jtname.setText((String) (jtb.getValueAt(dachon, 1)));
				jtage.setText((String) (jtb.getValueAt(dachon, 2)));
				jcb.setSelectedItem(jtb.getValueAt(dachon, 3));
				jtid.setEditable(false);
				jbthem.setEnabled(false);
				jbsua.setEnabled(true);
				jbxoa.setEnabled(true);

			}
		});
		jtr.addMouseListener(new java.awt.event.MouseAdapter() {

			@Override
			public void mouseReleased(MouseEvent e) {
				if (e.isPopupTrigger()) {
					popmenu.show(jtr, e.getX(), e.getY());
				}
			}
		});
		jtr.addTreeSelectionListener(new TreeSelectionListener() {

			@Override
			public void valueChanged(TreeSelectionEvent arg0) {
				try {
					node = (DefaultMutableTreeNode) jtr.getLastSelectedPathComponent();
					lop = (lop) node.getUserObject();
					dtm.setRowCount(0);
					jcb.setSelectedItem(lop.getMalop());
					showStudentInclass(lop.getMalop());
				} catch (Exception e) {
					JOptionPane.showMessageDialog(null, "Danh sách lớp ở dưới kìa", "Thông Báo",
							JOptionPane.INFORMATION_MESSAGE);
				}

			}
		});
		item.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				new themlop();
				dispose();

			}
		});
		item1.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					node = (DefaultMutableTreeNode) jtr.getLastSelectedPathComponent();
					lop = (lop) node.getUserObject();
					sualop edit = new sualop();
					edit.jtid.setText(lop.getMalop());
					dispose();
				} catch (Exception e2) {
					JOptionPane.showMessageDialog(null, "Mời chọn dòng cần sửa", "Thông Báo",
							JOptionPane.INFORMATION_MESSAGE);
				}
			}
		});
		item2.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					int yes = JOptionPane.showConfirmDialog(null, "Bạn có muốn xóa lớp này không", "Thông Báo",
							JOptionPane.YES_NO_OPTION);
					if (yes == JOptionPane.YES_OPTION) {
						try {
							String delete = "delete from class where name_class =?";
							PreparedStatement pre = conn.prepareStatement(delete);
							pre.setString(1, lop.getTenlop());
							pre.executeUpdate();
							JOptionPane.showMessageDialog(null, "Xóa thành công", "Thông Báo",
									JOptionPane.INFORMATION_MESSAGE);
							dispose();
							new sinhvien();
						} catch (SQLException ex) {
							JOptionPane.showMessageDialog(null, "Lớp vẫn còn sinh viên không thể xóa", "Thông Báo",
									JOptionPane.INFORMATION_MESSAGE);
						}
					}
				} catch (Exception e2) {
					JOptionPane.showMessageDialog(null, "Mời chọn dòng cần xóa", "Thông Báo",
							JOptionPane.INFORMATION_MESSAGE);
				}
			}
		});
	}
}
