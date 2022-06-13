package baitaplon;

public class lop {
	private String malop;
	private String tenlop;

	public lop(String malop, String tenlop) {
		super();
		this.malop = malop;
		this.tenlop = tenlop;
	}

	public String getMalop() {
		return malop;
	}

	public void setMalop(String malop) {
		this.malop = malop;
	}

	public String getTenlop() {
		return tenlop;
	}

	public void setTenlop(String tenlop) {
		this.tenlop = tenlop;
	}

	public String toString() {
		return getTenlop();
	}

}
