/*
 * $Id$
 */

package ru.ifmo.cs.elements;

/**
 *
 * @author Dmitry Afanasiev <KOT@MATPOCKuH.Ru>
 */
public class DataStorage extends DataValue implements DataDestination {
	public DataStorage(String name, int width, DataSource ... inputs) {
		super(name, width, inputs);
	} 
	
	@Override
	public void setValue(int value) {
		this.value = value & mask;
		//System.out.println("Write " + this.getClass() + ": " + Integer.toString(this.value, 16));
	}
}
