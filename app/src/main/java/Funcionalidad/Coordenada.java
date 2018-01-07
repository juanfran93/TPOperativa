package Funcionalidad;

public class Coordenada {

	private double x;
	private double y;
	private String direccion;

	public Coordenada(double x, double y) {
		this.x = x;
		this.y = y;
	}

	public String getDireccion() {
		return direccion;
	}

	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}

	public double getX() {
		return x;
	}

	public void setX(double x) {
		this.x = x;
	}

	public double getY() {
		return y;
	}

	public void setY(double y) {
		this.y = y;
	}

}
