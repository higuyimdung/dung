package baitaplon;

import java.sql.Connection;

import java.util.Properties;

import javax.swing.JOptionPane;

import com.mysql.cj.jdbc.Driver;

public class connect {
	Connection conn;
	public connect() {
		super();
		try {
			String strConn = "jdbc:mysql://localhost/quanlysinhvien";
			Properties pro = new Properties();
			pro.put("user", "root");
			pro.put("password", "");
			Driver dr = new Driver();
			conn = dr.connect(strConn, pro);
			if (conn != null)
				System.out.print("");
			//JOptionPane.showMessageDialog(null, "Kết nối thành công", "Thông Báo",
			//JOptionPane.INFORMATION_MESSAGE);
			else
				JOptionPane.showMessageDialog(null, "Kết nối thất bại", "Thông Báo", JOptionPane.OK_OPTION);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

/*	public ResultSet ExcuteQueryGetTable(String SQL) {
		try {
			Statement stm = conn.createStatement();
			ResultSet rs = stm.executeQuery(SQL);
			return rs;
		} catch (SQLException ex) {
			ex.printStackTrace();
		}

		return null;
	}
	public void ExcuteQueryUpdateDB(String SQL) {

		try {
			Statement stm = conn.createStatement();
			stm.executeUpdate(SQL);
		} catch (SQLException ex) {
			ex.printStackTrace();
		}
	}*/
}
