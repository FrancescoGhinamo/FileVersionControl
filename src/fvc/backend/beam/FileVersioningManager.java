package fvc.backend.beam;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

import fvc.backend.service.FileServiceFactory;

@SuppressWarnings("deprecation")
public class FileVersioningManager implements Observer, Serializable {

	
	private static final long serialVersionUID = 4841587285857391077L;

	private ArrayList<ControlledFile> files;
	private ArrayList<Thread> fileThreads;
	
	private static FileVersioningManager me;
	
	public static FileVersioningManager getInstance() {
		if(me == null) {
			me = new FileVersioningManager();
		}
		return me;
	}
	
	private FileVersioningManager() {
		files = new ArrayList<ControlledFile>();
		fileThreads = new ArrayList<Thread>();
	}
	
	
	public void add(ControlledFile f) {
		f.addObserver(this);
		Thread _t = new Thread(f);
		_t.start();
		
		files.add(f);
		fileThreads.add(_t);
	}

	public void stopVersioning(int i) {
		try {
			ControlledFile f = files.get(i);
			fileThreads.get(i).stop();
			f.removeVersions();
		}
		catch(IndexOutOfBoundsException e) {}
		
		
	}
	
	@Override
	public synchronized void update(Observable o, Object arg) {
		ControlledFile _f = (ControlledFile) o;
		_f.createVersion();
	}
	
	public void serialize(File f) {
		FileServiceFactory.getFileService().saveData(this, f);
	}
	
	public static FileVersioningManager getInstance(File f) {
		me = (FileVersioningManager) FileServiceFactory.getFileService().loadData(f);
		return me;
	}

	public ArrayList<ControlledFile> getFiles() {
		return files;
	}
	
	
	
	
}
