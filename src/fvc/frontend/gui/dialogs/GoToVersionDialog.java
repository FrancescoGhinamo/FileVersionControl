package fvc.frontend.gui.dialogs;

import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import fvc.backend.beam.ControlledFile;
import fvc.backend.beam.FileVersion;
import fvc.backend.beam.FileVersioningManager;

public class GoToVersionDialog extends JDialog implements ActionListener, ItemListener {


	private static final long serialVersionUID = -9219785911533142346L;

	private JComboBox<String> cmbFile;
	private JComboBox<String> cmbVersion;

	private JTextArea versionPreview;

	private JButton btnOK;
	private JButton btnCancel;

	public GoToVersionDialog(JFrame owner, boolean modale) {
		super(owner, "Go to version", modale);
		initComponents();
		pack();
	}

	private void initComponents() {

		setLayout(new GridBagLayout());

		cmbFile = new JComboBox<String>();
		cmbFile.addItemListener(this);
		for(ControlledFile f: FileVersioningManager.getInstance().getFiles()) {
			cmbFile.addItem(f.toString());
		}

		cmbVersion = new JComboBox<String>();
		cmbVersion.addItemListener(this);

		if(FileVersioningManager.getInstance().getFiles().size() > 0) {
			for(FileVersion v: FileVersioningManager.getInstance().getFiles().get(0).getVersions()) {
				cmbVersion.addItem(v.toString());
			}
		}

		versionPreview = new JTextArea();

		if(FileVersioningManager.getInstance().getFiles().size() > 0) {
			String prev = new String(FileVersioningManager.getInstance().getFiles().get(0).getVersions().get(0).getContent());
			versionPreview.setText(prev);
		}
		JScrollPane scrl = new JScrollPane(versionPreview);

		btnOK = new JButton("OK");
		btnOK.addActionListener(this);

		btnCancel = new JButton("Cancel");
		btnCancel.addActionListener(this);

		JPanel pan = new JPanel(new FlowLayout(FlowLayout.RIGHT));

		pan.add(btnCancel);
		pan.add(btnOK);


		GridBagConstraints gbc = new GridBagConstraints();
		gbc.fill = GridBagConstraints.BOTH;
		gbc.insets = new Insets(5, 5, 5, 5);
		gbc.gridx = gbc.gridy = 0;
		gbc.weighty = 1;

		add(cmbFile, gbc);

		gbc.gridy = 1;

		add(cmbVersion, gbc);

		gbc.gridy = 2;
		gbc.weighty = 1;

		add(scrl, gbc);

		gbc.gridy = 3;
		gbc.weighty = 1;

		add(pan, gbc);



	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource().equals(btnOK) && FileVersioningManager.getInstance().getFiles().size() > 0) {
			FileVersioningManager.getInstance().getFiles().get(cmbFile.getSelectedIndex()).moveToVersion(cmbVersion.getSelectedIndex() + 1);
			dispose();
		}
		else if(e.getSource().equals(btnCancel)) {
			dispose();
		}
	}

	@Override
	public void itemStateChanged(ItemEvent e) {
		if(e.getSource().equals(cmbFile)) {
			if(cmbVersion != null && FileVersioningManager.getInstance().getFiles().size() > 0) {
				try {
					cmbVersion.removeAllItems();
					for(FileVersion v: FileVersioningManager.getInstance().getFiles().get(cmbFile.getSelectedIndex()).getVersions()) {
						
						cmbVersion.addItem(v.toString());
					}
				}
				catch(IndexOutOfBoundsException ex) {}
				
			}
		}
		else if(e.getSource().equals(cmbVersion)) {
			if(versionPreview != null && FileVersioningManager.getInstance().getFiles().size() > 0) {
				
				try {
					String prev = new String(FileVersioningManager.getInstance().getFiles().get(cmbFile.getSelectedIndex()).getVersions().get(cmbVersion.getSelectedIndex()).getContent());
					versionPreview.setText(prev);
				}
				catch(IndexOutOfBoundsException ex) {}
			}
			

		}

	}

}
