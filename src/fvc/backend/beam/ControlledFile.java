package fvc.backend.beam;

import java.io.File;
import java.util.ArrayList;
import java.util.Observable;

import fvc.backend.service.FileServiceFactory;

public class ControlledFile extends Observable implements Runnable {

	private File linkedFile;
	private File versionDirectory;
	
	private long lastModified;
	
	private ArrayList<FileVersion> versions;
	
	public ControlledFile(File linkedFile, File versionDirectory) {
		this.linkedFile = linkedFile;
		this.versionDirectory = versionDirectory;
		versions = new ArrayList<FileVersion>();
	}



	@Override
	public void run() {
		while(true) {
			lastModified = linkedFile.lastModified();
			long lastVersion = 0;
			
			try {
				lastVersion = versions.get(versions.size() - 1).getVersioningTime();
			}
			catch(IndexOutOfBoundsException e) {}
			
			if(lastVersion != lastModified) {				
				setChanged();
				notifyObservers(new VersionChangedInfo(linkedFile, lastModified, versions.size()));
				
			}
		}

	}

	public void createVersion() {
		FileVersion _v = new FileVersion(linkedFile, lastModified);
		versions.add(_v);
		FileServiceFactory.getFileService().saveData(_v, new File(versionDirectory.getAbsolutePath() + "\\" + versions.size() + ".ver"));
	}


	public File getLinkedFile() {
		return linkedFile;
	}



	public ArrayList<FileVersion> getVersions() {
		return versions;
	}
}
