package fvc.frontend.gui.mainFrame;

import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.JOptionPane;

import fvc.backend.beam.FileVersioningManager;

public class WinMan implements WindowListener {
	
	private VersionControlGUI owner;
	
	public WinMan(VersionControlGUI owner) {
		this.owner = owner;
	}

	@Override
	public void windowOpened(WindowEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void windowClosing(WindowEvent e) {
		JOptionPane.showMessageDialog(owner, "Saving versioning", "Closing", JOptionPane.INFORMATION_MESSAGE);
		if(FileVersioningManager.getInstance().isChanged()) {
			owner.performSaveConfiguration();
		}
		
		if(owner.isSavedConfiguration()) {
			FileVersioningManager.getInstance().saveAllVersions();
		}
		System.exit(0);

	}

	@Override
	public void windowClosed(WindowEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void windowIconified(WindowEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void windowDeiconified(WindowEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void windowActivated(WindowEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void windowDeactivated(WindowEvent e) {
		// TODO Auto-generated method stub

	}

}
