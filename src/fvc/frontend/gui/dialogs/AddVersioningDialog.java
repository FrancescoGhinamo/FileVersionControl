package fvc.frontend.gui.dialogs;

import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class AddVersioningDialog extends JDialog implements ActionListener {


	private static final long serialVersionUID = 1980125876721594801L;

	private File linkedFile;
	private File versionDir;

	private JTextField txtFilePath;
	private JTextField txtDirPath;

	private JButton btnChooseFilePath;
	private JButton btnChooseDirPath;
	
	private JButton btnCancel, btnOK;

	public AddVersioningDialog(JFrame owner, boolean modale) {
		super(owner, "Add versioning", modale);
		initComponents();
		pack();

	}

	private void initComponents() {
		setLayout(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.fill = GridBagConstraints.BOTH;
		gbc.gridx = gbc.gridy = 0;
		gbc.insets = new Insets(5, 5, 5, 5);
		gbc.weightx = 10;
		gbc.weighty = 5;
		
		add(createInputPanel(), gbc);
		
		gbc.gridy = 1;
		gbc.weighty = 1;
		
		add(createButPanel(), gbc);
		



	}
	
	public JPanel createButPanel() {
		btnCancel = new JButton("Cancel");
		btnCancel.addActionListener(this);
		
		btnOK = new JButton("OK");
		btnOK.addActionListener(this);
		
		JPanel pan = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		pan.add(btnCancel);
		pan.add(btnOK);
		return pan;
	}

	public JPanel createInputPanel() {
		JPanel pan = new JPanel(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();

		JLabel lblFile = new JLabel("File to link");
		txtFilePath = new JTextField(20);
		txtFilePath.setEditable(false);

		btnChooseFilePath = new JButton("Choose");
		btnChooseFilePath.addActionListener(this);
		gbc.insets = new Insets(5, 5, 5, 5);
		gbc.gridx = gbc.gridy = 0;
		gbc.weighty = 5;
		gbc.weightx = 25;
		pan.add(lblFile, gbc);
		
		gbc.gridx = 1;
		gbc.weightx = 150;
		pan.add(txtFilePath, gbc);
		
		gbc.gridx = 2;
		gbc.gridx = 25;
		pan.add(btnChooseFilePath, gbc);
		
		JLabel lblDir = new JLabel("Version directory");
		txtDirPath = new JTextField(20);
		txtDirPath.setEditable(false);

		btnChooseDirPath = new JButton("Choose");
		btnChooseDirPath.addActionListener(this);
		gbc.insets = new Insets(5, 5, 5, 5);
		gbc.gridx = 0;
		gbc.gridy = 1;
		gbc.weighty = 5;
		gbc.weightx = 25;
		pan.add(lblDir, gbc);
		
		gbc.gridx = 1;
		gbc.weightx = 150;
		pan.add(txtDirPath, gbc);
		
		gbc.gridx = 2;
		gbc.gridx = 25;
		pan.add(btnChooseDirPath, gbc);
		
		
		
		return pan;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource().equals(btnChooseFilePath)) {
			performChooseFile();
		}
		else if(e.getSource().equals(btnChooseDirPath)) {
			performChooseDir();
		}
		else if(e.getSource().equals(btnOK)) {
			performOK();
		}
		else if(e.getSource().equals(btnCancel)) {
			dispose();
		}

	}
	
	public void performChooseFile() {
		JFileChooser fc = new JFileChooser();
		if(fc.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
			linkedFile = fc.getSelectedFile();
			txtFilePath.setText(linkedFile.getAbsolutePath());
		}
	}
	
	public void performChooseDir() {
		JFileChooser fc = new JFileChooser();
		fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		if(fc.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
			versionDir = fc.getSelectedFile();
			txtDirPath.setText(versionDir.getAbsolutePath());
		}
	}
	
	public void performOK() {
		if(linkedFile != null && versionDir != null) {
			dispose();
		}
	}

	public File getLinkedFile() {
		return linkedFile;
	}

	public File getVersionDir() {
		return versionDir;
	}



}
