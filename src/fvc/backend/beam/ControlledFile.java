package fvc.backend.beam;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Observable;

import fvc.backend.service.FileServiceFactory;


@SuppressWarnings("deprecation")
public class ControlledFile extends Observable implements Runnable, Serializable {
	
	
	private static final long serialVersionUID = -5272898050452014335L;

	private static final String VER_EXT = ".ver";

	private File linkedFile;
	private File versionFile;
	private int currentVersion;
	private long lastModified;
	
	private boolean versioningStopped;
	
	private boolean versioningEnded;
	
	private ArrayList<FileVersion> versions;
	
	public ControlledFile(File linkedFile, File versionDirectory) {
		this.linkedFile = linkedFile;
		this.versionFile = new File(versionDirectory.getAbsolutePath() + "\\" + linkedFile.getName() + VER_EXT);
		this.currentVersion = 1;
		versions = new ArrayList<FileVersion>();
		this.versioningEnded = false;
	}


	public void removeVersions() {
		versionFile.delete();
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
		versioningStopped = false;
		
		while(!versioningStopped) {
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
		FileVersion _v = new FileVersion(linkedFile, lastModified, currentVersion);
		versions.add(_v);
//		FileServiceFactory.getFileService().saveData(_v, _v.getVersionFile());
		currentVersion++;
	}

	public void saveCurrentStatusAndVersions() {
		if(!versioningStopped) {
			this.stopVersioning();
			FileServiceFactory.getFileService().saveData(this, versionFile);
			new Thread(this).start();
		}
		
	}
	
	
	public void stopVersioning() {
		versioningStopped = true;
	}

	public File getLinkedFile() {
		return linkedFile;
	}



	public ArrayList<FileVersion> getVersions() {
		return versions;
	}
	
	
	
	public File getVersionFile() {
		return versionFile;
	}


	public int getCurrentVersion() {
		return currentVersion;
	}


	public long getLastModified() {
		return lastModified;
	}


	
	public boolean isVersioningEnded() {
		return versioningEnded;
	}


	public void setVersioningEnded(boolean versioningEnded) {
		this.versioningEnded = versioningEnded;
	}


	public String toString() {
		return linkedFile.getAbsolutePath();
	}
	
}
