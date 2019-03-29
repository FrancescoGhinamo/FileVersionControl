package fvc.frontend.gui.mainFrame;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;

import fvc.backend.beam.ControlledFile;
import fvc.backend.beam.FileVersioningManager;
import fvc.backend.beam.VersionChangedInfo;
import fvc.frontend.gui.dialogs.AddVersioningDialog;
import fvc.frontend.gui.dialogs.GoToVersionDialog;
import fvc.frontend.gui.dialogs.SaveVersioningsDialog;
import fvc.frontend.gui.dialogs.StopVersioningDialog;

@SuppressWarnings("deprecation")
public class VersionControlGUI extends JFrame implements ActionListener, Observer {

	
	private static final long serialVersionUID = 8102287318479997817L;
	private static final String[] TABLE_HEADER = {"File", "Date and time", "Version number"};
	private static final String CONFIG_EXT = "verconf";
	
	private DefaultTableModel tblEvents;
	
	private JMenuItem itemLoadConfiguration;
	private JMenuItem itemSaveConfiguration;
	
	private JMenuItem itemAddFileVersioning;
	private JMenuItem itemStopVersioning;
	private JMenuItem itemGoToVersion;
	private JMenuItem itemSaveVersioningStatus;
	
	private boolean savedConfiguration;

	
	public VersionControlGUI() {
		super("Version control manger");
		FileVersioningManager.getInstance(this);
		setExtendedState(MAXIMIZED_BOTH);
		setMinimumSize(new Dimension(300, 170));
		addWindowListener(new WinMan(this));
		savedConfiguration = false;
		initComponents();
	}
	
	private void initComponents() {
		setLayout(new BorderLayout());
		add(createTblPane(), BorderLayout.CENTER);
		
		setJMenuBar(createJMenuBar());
		
	}

	public JScrollPane createTblPane() {
		tblEvents = new DefaultTableModel(TABLE_HEADER, 0);
		JTable tbl = new JTable(tblEvents);
		return new JScrollPane(tbl);
	}
	
	public JMenu createFileJMenu() {
		JMenu mnuFile = new JMenu("File");
		
		itemLoadConfiguration = new JMenuItem("Load versioning configuration");
		itemLoadConfiguration.addActionListener(this);
		
		itemSaveConfiguration = new JMenuItem("Save versioning configuration");
		itemSaveConfiguration.addActionListener(this);
		
		mnuFile.add(itemLoadConfiguration);
		mnuFile.add(itemSaveConfiguration);
		
		return mnuFile;
	}
	
	public JMenu createVersioningJMenu() {
		JMenu mnuVersioning = new JMenu("Versioning");
		
		itemAddFileVersioning = new JMenuItem("Add file versioning");
		itemAddFileVersioning.addActionListener(this);
		
		itemStopVersioning = new JMenuItem("Stop versioning");
		itemStopVersioning.addActionListener(this);
		
		itemGoToVersion = new JMenuItem("Go to version");
		itemGoToVersion.addActionListener(this);
		
		itemSaveVersioningStatus = new JMenuItem("Save versioning status");
		itemSaveVersioningStatus.addActionListener(this);
		
		mnuVersioning.add(itemAddFileVersioning);
		mnuVersioning.add(itemStopVersioning);
		mnuVersioning.add(itemGoToVersion);
		mnuVersioning.add(itemSaveVersioningStatus);
		
		return mnuVersioning;
	}
	
	public JMenuBar createJMenuBar() {
		JMenuBar bar = new JMenuBar();
		bar.add(createFileJMenu());
		bar.add(createVersioningJMenu());
		return bar;
	}
	
	
	@Override
	public synchronized void update(Observable o, Object arg) {
		if(o instanceof ControlledFile && arg instanceof VersionChangedInfo) {
			VersionChangedInfo i = (VersionChangedInfo) arg;
			SimpleDateFormat fmt = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
			String[] entry = {i.getRelatedFile().getAbsolutePath(), fmt.format(i.getVerTime().getTime()), String.valueOf(i.getVerNumber())};
			tblEvents.addRow(entry);
		}

	}

	@Override
	public void actionPerformed(ActionEvent e) {
		
		if(e.getSource().equals(itemLoadConfiguration)) {
			performLoadConfiguration();
		}
		else if(e.getSource().equals(itemSaveConfiguration)) {
			performSaveConfiguration();
		}
		
		if(e.getSource().equals(itemAddFileVersioning)) {
			performAddVersioning();
		}
		else if(e.getSource().equals(itemStopVersioning)) {
			performStopVersioning();
		}
		else if(e.getSource().equals(itemGoToVersion)) {
			performGoToVersion();
		}
		else if(e.getSource().equals(itemSaveVersioningStatus)) {
			performSaveVersioningStatus();
		}

	}
	
	public JFileChooser initFileChooser() {
		JFileChooser fc = new JFileChooser();
		fc.setFileFilter(new FileNameExtensionFilter("Versioning configuration", CONFIG_EXT));
		return fc;
	}
	
	public void performLoadConfiguration() {
		JFileChooser c = initFileChooser();
		if(c.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
			tblEvents.setRowCount(0);
			FileVersioningManager.getInstance().loadVersioningConfifuration(c.getSelectedFile());
			FileVersioningManager.getInstance().startVersioning();
			savedConfiguration = true;
		}
	}
	
	public void performSaveConfiguration() {
		JFileChooser c = initFileChooser();
		if(c.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
			File _f = c.getSelectedFile();
			if(!_f.getAbsolutePath().endsWith(CONFIG_EXT)) {
				_f = new File(_f.getAbsolutePath() + "." + CONFIG_EXT);
			}
			FileVersioningManager.getInstance().saveVersionigConfiguration(_f);
			savedConfiguration = true;
		}
	}
	
	public void performAddVersioning() {
		AddVersioningDialog d = new AddVersioningDialog(this, true);
		d.setVisible(true);
		if(d.getLinkedFile() != null && d.getVersionDir() != null) {
			ControlledFile _c = new ControlledFile(d.getLinkedFile(), d.getVersionDir());
			_c.addObserver(this);
			FileVersioningManager.getInstance().add(_c);
		}
	}
	
	public void performStopVersioning() {
		new StopVersioningDialog(this, true).setVisible(true);
	}
	
	public void performGoToVersion() {
		new GoToVersionDialog(this, true).setVisible(true);
	}
	
	public void performSaveVersioningStatus() {
		new SaveVersioningsDialog(this, true).setVisible(true);
	}
	
	

	public boolean isSavedConfiguration() {
		return savedConfiguration;
	}

	public static void main(String[] args) {
		VersionControlGUI vc = new VersionControlGUI();
		vc.setVisible(true);
	

	}

}
