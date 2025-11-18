package es.upm.dit.aled.lab5;

import java.awt.Color;
import java.util.Objects;

import es.upm.dit.aled.lab5.gui.Position2D;

/**
 * Models an area in a hospital ER. Each Area is characterized by its name
 * (which must be unique), the time a patient must spend there to be treated (in
 * milliseconds), the capacity (maximum number of patients that can be treated
 * at the same time), the number of patients being treated at a given time, and
 * the number of patients currently waiting.
 * 
 * This class is a monitor that ensures that there is a thread safe way to enter
 * and exit the area.
 * 
 * Each Area is represented graphically by a square of side 120 px, centered in
 * a given position and with a custom color.
 * 
 * @author rgarciacarmona
 */
public class Area {

	protected String name;
	private int time;
	private Position2D position;
	private Color color;
	protected int capacity;
	protected int numPatients;
	protected int waiting;

	/**
	 * Builds a new Area.
	 * 
	 * @param name     The name of the Area.
	 * @param time     The amount of time (in milliseconds) needed to treat a
	 *                 Patient in this Area.
	 * @param capacity The number of Patients that can be treated at the same time.
	 * @param position The location of the Area in the GUI.
	 */
	public Area(String name, int time, int capacity, Position2D position) {
		// HECHO
		this.name = name;
		this.time = time;
		this.position = position;
		this.color = Color.GRAY; // Default color
		this.capacity = capacity;
		//Al crear un nuevo Area los pacientes siendo atendidos y esperando son cero
		this.numPatients = 0;
		this.waiting = 0;
		
	}

	/**
	 * Returns the name of the Area.
	 * 
	 * @return The name.
	 */
	public String getName() {
		return name;
	}

	/**
	 * Returns the time (in milliseconds) that a Patient must spend in the Area to
	 * be treated.
	 * 
	 * @return The time.
	 */
	public int getTime() {
		return time;
	}

	/**
	 * Returns the position of the center of the Area in the GUI.
	 * 
	 * @return The position.
	 */
	public Position2D getPosition() {
		return position;
	}

	/**
	 * Returns the color of Area in the GUI.
	 * 
	 * @return The color.
	 */
	public Color getColor() {
		return color;
	}

	/**
	 * Changes the color of the Area in the GUI.
	 * 
	 * @param color The new color.
	 */
	public void setColor(Color color) {
		this.color = color;
	}

	/**
	 * Thread safe method that allows a Patient to enter the area. If the Area is
	 * currently full, the Patient thread must wait.
	 * 
	 * @param p The patient that wants to enter.
	 */
	//method enter : HECHO
	public synchronized void enter (Patient p) {
		try {
			//Si el Area está llena el paciente debe esperar
			while (this.numPatients == this.capacity) {
				System.out.println("Patient " + p.getNumber() + " is waiting to be attended.");
				this.waiting ++; //incremento en 1 el numero de pacientes que estan esperando
				wait(); //hago que el paciente espere 	
			}
			//Cuando no se cumpla, numPatients < capacity, el paciente podrá entrar
			this.numPatients ++; //incremento en 1 el numero de pacientes que están siendo atendidos (ha entrado un paciente)
			if (waiting > 0) {
				this.waiting --;  
			}	
		}catch (InterruptedException e) {
		}
	}
	
	/**
	 * Thread safe method that allows a Patient to exit the area. After the Patient
	 * has left, this method notifys all waiting Patients.
	 * 
	 * @param p The patient that wants to enter.
	 */
	//method exit : HECHO
	public synchronized void exit (Patient p) { //EN ESTE CUANDO EL PACIENTE SALE: notifyALL()
		this.numPatients --; //reduzco en 1 el numero de pacientes que están siendo atendidos
		System.out.println("Patient" + p.getNumber() + " has exited.");
		notifyAll(); //despierto a todas las hebras de Patients que estaban esperando
	}
	/**
	 * Returns the capacity of the Area. This method must be thread safe.
	 * //Ser THREAD SAVE implica que:
	 * nos aseguremos de que si van a usarlo diferentes threads no "pete". En el caso de getCapacity, como
	 * solamente se va a leer siempre, no hace falta poner synchronized para que sea thread save.
	 * 
	 * @return The capacity.
	 */
	//method getCapacity : HECHO
	public int getCapacity() {
		return this.capacity;
	}
	/**
	 * Returns the current number of Patients being treated at the Area. This method must be thread safe.
	 * 
	 * @return The number of Patients being treated.
	 */
	//method getNumPatients : HECHO
	public synchronized int getNumPatients() {
		return this.numPatients;
	}
	/**
	 * Returns the current number of Patients waiting to be treated at the Area. This method must be thread safe.
	 * 
	 * @return The number of Patients waiting to be treated.
	 */
	//method getWaiting : HECHO
	public synchronized int getWaiting() {
		return this.waiting;
	}

	@Override
	public int hashCode() {
		return Objects.hash(name);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Area other = (Area) obj;
		return Objects.equals(name, other.name);
	}

	@Override
	public String toString() {
		return name;
	}
}