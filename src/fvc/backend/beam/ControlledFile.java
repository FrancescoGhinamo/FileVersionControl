package fvc.backend.beam;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
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
	
	public void moveToVersion(int n) {
		try {
			FileVersion ver = versions.get(n - 1);
			FileOutputStream fos = null;
			try {
				fos = new FileOutputStream(linkedFile);
				fos.write(ver.getContent(), 0, ver.getContent().length);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			finally {
				if(fos != null) {
					try {
						fos.flush();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					finally {
						try {
							fos.close();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
			}
		}
		catch (ArrayIndexOutOfBoundsException e) {}
		
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
	
	
	
	public File getVersionDirectory() {
		return versionDirectory;
	}


	public int getCurrentVersion() {
		return currentVersion;
	}


	public long getLastModified() {
		return lastModified;
	}


	public String toString() {
		return linkedFile.getAbsolutePath();
	}
	
}
