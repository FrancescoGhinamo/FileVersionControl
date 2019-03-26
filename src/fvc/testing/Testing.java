package fvc.testing;

import java.io.File;

import fvc.backend.beam.ControlledFile;
import fvc.backend.beam.FileVersioningManager;

public class Testing {

	public static void main(String[] args) {
		ControlledFile f = new ControlledFile(new File("C:\\Users\\franc\\OneDrive\\Documents\\output.txt"), new File("C:\\Users\\franc\\OneDrive\\Desktop"));
		FileVersioningManager fs = new FileVersioningManager();
		fs.add(f);
		try {
			Thread.sleep(1000000000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
