package fvc.backend.beam;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

import fvc.backend.service.FileServiceFactory;

@SuppressWarnings("deprecation")
public class FileVersioningManager implements Observer, Serializable {


	private static final long serialVersionUID = 4841587285857391077L;

	private ArrayList<ControlledFile> files;
	private Observer otherObserver;
	
	private boolean changed;

	//	private ArrayList<Thread> fileThreads;

	private static FileVersioningManager me;

	public static FileVersioningManager getInstance(Observer other) {
		if(me == null) {
			me = new FileVersioningManager(other);
		}
		
		return me;
	}

	public static FileVersioningManager getInstance() {
		if(me == null) {
			me = new FileVersioningManager();
		}
		return me;
	}



	private FileVersioningManager() {
		files = new ArrayList<ControlledFile>();
		changed = false;
		//		fileThreads = new ArrayList<Thread>();
	}

	public FileVersioningManager(Observer other) {
		this();
		otherObserver = other;
	}


	public void add(ControlledFile f) {
		f.addObserver(this);
		Thread _t = new Thread(f);
		_t.start();

		files.add(f);
		changed = true;
		//		fileThreads.add(_t);
	}

	public void stopVersioning(int i) {
		try {
			ControlledFile f = files.get(i);
			f.stopVersioning();
			//			fileThreads.get(i).stop();
			f.removeVersions();
		}
		catch(IndexOutOfBoundsException e) {}


	}
	
	public void saveAllVersions() {
		for(ControlledFile f: files) {
			f.saveCurrentStatusAndVersions();
		}
	}

	public void startVersioning() {
		for(ControlledFile cf: files) {
			new Thread(cf).start();
		}
	}

	public void loadVersioningConfifuration(File f) {
		BufferedReader br = null;
		try {
			br = new BufferedReader(new FileReader(f));
			files.clear();
			String line = br.readLine();
			while(line != null) {
				ControlledFile cf = (ControlledFile) FileServiceFactory.getFileService().loadData(new File(line).getAbsolutePath());
				if(otherObserver != null) {
					cf.addObserver(otherObserver);
				}
				cf.addObserver(this);
				files.add(cf);
				line = br.readLine();
			}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally {
			if(br != null) {
				try {
					br.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}

	public void saveVersionigConfiguration(File f) {
		FileWriter fw = null;
		try {
			fw = new FileWriter(f);

			for(ControlledFile cf: files) {
				cf.saveCurrentStatusAndVersions();
				fw.write(cf.getVersionFile().getAbsolutePath());
				fw.write("\r\n");
			}
			changed = false;

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally {
			if(fw != null) {
				try {
					fw.flush();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				finally {
					try {
						fw.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}
	}

	@Override
	public synchronized void update(Observable o, Object arg) {
		ControlledFile _f = (ControlledFile) o;
		_f.createVersion();
	}

	public void serialize(File f) {
		FileServiceFactory.getFileService().saveData(this, f.getAbsolutePath());
	}

	public static FileVersioningManager getInstance(File f) {
		me = (FileVersioningManager) FileServiceFactory.getFileService().loadData(f.getAbsolutePath());
		return me;
	}

	
	
	public void setOtherObserver(Observer otherObserver) {
		this.otherObserver = otherObserver;
	}

	public ArrayList<ControlledFile> getFiles() {
		return files;
	}

	public boolean isChanged() {
		return changed;
	}




}
