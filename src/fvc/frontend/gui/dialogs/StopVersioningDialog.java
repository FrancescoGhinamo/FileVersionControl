package fvc.frontend.gui.dialogs;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;

import fvc.backend.beam.ControlledFile;
import fvc.backend.beam.FileVersioningManager;

public class StopVersioningDialog extends JDialog implements ActionListener {

	
	private static final long serialVersionUID = 3412439443186460729L;
	
	private JComboBox<String> cmbFiles;
	private JButton btnOK;
	
	
	
	
	public StopVersioningDialog(JFrame owner, boolean modale) {
		super(owner, "Stop versioning", modale);
		initComponents();
		pack();
	}
	
	private void initComponents() {
		setLayout(new BorderLayout());
		
		cmbFiles = new JComboBox<String>();
		
		for(ControlledFile f: FileVersioningManager.getInstance().getFiles()) {
			cmbFiles.addItem(f.toString());
		}
		
		add(cmbFiles, BorderLayout.NORTH);
		
		btnOK = new JButton("OK");
		btnOK.addActionListener(this);
		
		add(btnOK, BorderLayout.SOUTH);
		
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource().equals(btnOK)) {
			FileVersioningManager.getInstance().stopVersioning(cmbFiles.getSelectedIndex());
			dispose();
		}

	}

}
