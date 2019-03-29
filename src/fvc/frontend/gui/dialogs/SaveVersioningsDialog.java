package fvc.frontend.gui.dialogs;

import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;

import fvc.backend.beam.FileVersioningManager;

public class SaveVersioningsDialog extends JDialog implements ActionListener {


	private static final long serialVersionUID = -8874784045297467382L;

	private JCheckBox[] chkFiles;

	private JButton btnOK;
	private JButton btnCancel;

	public SaveVersioningsDialog(JFrame owner, boolean modale) {
		super(owner, "Save versionings", modale);
		initComponents();
		pack();
	}

	private void initComponents() {
		chkFiles = new JCheckBox[FileVersioningManager.getInstance().getFiles().size()];
		for(int i = 0; i < FileVersioningManager.getInstance().getFiles().size(); i++) {
			chkFiles[i] = new JCheckBox(FileVersioningManager.getInstance().getFiles().get(i).toString(), false);
			if(FileVersioningManager.getInstance().getFiles().get(i).isVersioningEnded()) {
				chkFiles[i].setEnabled(false);
			}
		}

		setLayout(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.fill = GridBagConstraints.BOTH;
		gbc.insets = new Insets(5, 5, 5, 5);
		gbc.weightx = 15;
		gbc.weighty = 1;
		gbc.gridx = 0;
		gbc.gridy = 0;
		for(JCheckBox j: chkFiles) {
			add(j, gbc);
			gbc.gridy++;
		}
		
		
		gbc.weighty = 2;
		add(createButtonPan(), gbc);
	}


	private JPanel createButtonPan() {
		JPanel pan = new JPanel(new FlowLayout(FlowLayout.RIGHT));

		btnCancel = new JButton("Cancel");
		btnCancel.addActionListener(this);

		btnOK = new JButton("OK");
		btnOK.addActionListener(this);

		pan.add(btnCancel);
		pan.add(btnOK);

		return pan;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource().equals(btnOK)) {
			for(int i = 0; i < chkFiles.length; i++) {
				if(chkFiles[i].isEnabled() && chkFiles[i].isSelected()) {
					FileVersioningManager.getInstance().getFiles().get(i).saveCurrentStatusAndVersions();
				}
			}
			dispose();
		}
		else if(e.getSource().equals(btnCancel)) {
			dispose();
		}
	}

}
