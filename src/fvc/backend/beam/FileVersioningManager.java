package fvc.backend.beam;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

public class FileVersioningManager implements Observer {

	private ArrayList<ControlledFile> files;
	
	public void add(ControlledFile f) {
		f.addObserver(this);
		new Thread(f).start();
		files.add(f);
	}

	@Override
	public synchronized void update(Observable o, Object arg) {
		ControlledFile _f = (ControlledFile) o;
		_f.createVersion();
	}
	
	
	
}
