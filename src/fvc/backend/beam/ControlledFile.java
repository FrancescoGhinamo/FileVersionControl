package fvc.backend.beam;

import java.io.File;
import java.util.ArrayList;
import java.util.Observable;

import fvc.backend.service.FileServiceFactory;

@SuppressWarnings("deprecation")
public class ControlledFile extends Observable implements Runnable {

	private File linkedFile;
	private File versionDirectory;
	private int currentVersion;
	private long lastModified;
	
	private ArrayList<FileVersion> versions;
	
	public ControlledFile(File linkedFile, File versionDirectory) {
		this.linkedFile = linkedFile;
		this.versionDirectory = versionDirectory;
		this.currentVersion = 1;
		versions = new ArrayList<FileVersion>();
	}


	public void removeVersions() {
		for(FileVersion v: versions) {
			v.getVersionFile().delete();
		}
		
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
				notifyObservers(new VersionChangedInfo(linkedFile, lastModified, currentVersion));
				
			}
		}

	}

	public void createVersion() {
		FileVersion _v = new FileVersion(linkedFile, lastModified, currentVersion, new File(versionDirectory.getAbsolutePath() + "\\" + currentVersion + ".ver"));
		versions.add(_v);
		FileServiceFactory.getFileService().saveData(_v, _v.getVersionFile());
		currentVersion++;
	}


	public File getLinkedFile() {
		return linkedFile;
	}



	public ArrayList<FileVersion> getVersions() {
		return versions;
	}
	
	public String toString() {
		return linkedFile.getAbsolutePath();
	}
}
