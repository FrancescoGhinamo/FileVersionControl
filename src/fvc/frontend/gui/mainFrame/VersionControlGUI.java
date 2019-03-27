package fvc.frontend.gui.mainFrame;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import fvc.backend.beam.ControlledFile;
import fvc.backend.beam.FileVersioningManager;
import fvc.backend.beam.VersionChangedInfo;
import fvc.frontend.gui.dialogs.AddVersioningDialog;
import fvc.frontend.gui.dialogs.StopVersioningDialog;

@SuppressWarnings("deprecation")
public class VersionControlGUI extends JFrame implements ActionListener, Observer {

	
	private static final long serialVersionUID = 8102287318479997817L;
	private static final String[] TABLE_HEADER = {"File", "Date and time", "Version number"};
	
	private DefaultTableModel tblEvents;
	
	private JMenuItem itemAddFileVersioning;
	private JMenuItem itemStopVersioning;
	
	public VersionControlGUI() {
		super("Version control manger");
		setExtendedState(MAXIMIZED_BOTH);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
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
	
	public JMenu createVersioningJMenu() {
		JMenu mnuVersioning = new JMenu("Versioning");
		
		itemAddFileVersioning = new JMenuItem("Add file versioning");
		itemAddFileVersioning.addActionListener(this);
		
		itemStopVersioning = new JMenuItem("Stop versioning");
		itemStopVersioning.addActionListener(this);
		
		mnuVersioning.add(itemAddFileVersioning);
		mnuVersioning.add(itemStopVersioning);
		
		return mnuVersioning;
	}
	
	public JMenuBar createJMenuBar() {
		JMenuBar bar = new JMenuBar();
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
		if(e.getSource().equals(itemAddFileVersioning)) {
			performAddVersioning();
		}
		else if(e.getSource().equals(itemStopVersioning)) {
			performStopVersioning();
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

	public static void main(String[] args) {
		VersionControlGUI vc = new VersionControlGUI();
		vc.setVisible(true);
	

	}

}
