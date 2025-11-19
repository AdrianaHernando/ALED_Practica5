package es.upm.dit.aled.lab5;

import java.util.LinkedList;
import java.util.Queue;

import es.upm.dit.aled.lab5.gui.Position2D;

/**
 * Extension of Area that maintains a strict queue of the Patients waiting to
 * enter in it. After a Patient exits, the first one in the queue will be
 * allowed to enter.
 * 
 * @author rgarciacarmona
 */
public class AreaQueue extends Area {
  //HECHO POR MI:
	//Defino el atributo nuevo que tiene AreaQueue respecto Area
	private Queue<Patient> waitQueue;
	
	//Implemento el nuevo constructor
	public AreaQueue (String name, int time, int capacity, Position2D position) {
		super(name, time, capacity, position); //Al llamar al constructor de la clase padre hay que meterle los parámetros
		this.waitQueue = new LinkedList<Patient>(); //La implementación del interfaz Queue es LinkedList	
	}
	
	/*
	 * Reimplemento el metodo enter de la clase Area, para que sea necesario que 
	 * el paciente que quiere entrar sea el primero de la cola de espera (waitQueue)
	 */
	@Override
	public synchronized void enter (Patient p) {
		try {
			//Si el Area está llena O no es el primer paciente de la cola: el paciente debe esperar
			while (this.numPatients == this.capacity || p != this.waitQueue.peek()) {
				System.out.println("Patient " + p.getNumber() + " is waiting to be attended.");
				this.waiting ++; //incremento en 1 el numero de pacientes que estan esperando
				if (!this.waitQueue.contains(p)) { //Si el paciente NO estaba en la cola lo añado al final
					this.waitQueue.add(p);
				}
				wait(); //hago que el paciente espere 	
			}
			//Cuando no se cumpla ninguna de las condiciones, el paciente podrá entrar
			this.numPatients ++; //incremento en 1 el numero de pacientes que están siendo atendidos (ha entrado un paciente)
			if (waiting > 0) {
				this.waiting --;  
			}
			this.waitQueue.remove(p); //Elimina al paciente de la cola de espera.
		}catch (InterruptedException e) {
		}
	}
	
}
