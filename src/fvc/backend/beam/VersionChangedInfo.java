package fvc.backend.beam;

import java.io.File;
import java.util.GregorianCalendar;

public class VersionChangedInfo {

	private File relatedFile;
	private GregorianCalendar verTime;
	private int verNumber;
	
	public VersionChangedInfo(File relatedFile, long verTime, int verNumber) {
		super();
		this.relatedFile = relatedFile;
		this.verTime = new GregorianCalendar();
		this.verTime.setTimeInMillis(verTime);
		this.verNumber = verNumber;
	}

	public File getRelatedFile() {
		return relatedFile;
	}

	public GregorianCalendar getVerTime() {
		return verTime;
	}

	public int getVerNumber() {
		return verNumber;
	}
	
	
	
	
	
}
