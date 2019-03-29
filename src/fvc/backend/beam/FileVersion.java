package fvc.backend.beam;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.GregorianCalendar;

public class FileVersion implements Serializable {
	
	
	private static final long serialVersionUID = -2642673476860464040L;
	
	private int verNum;
	private byte[] content;
	private File relativeFile;
	private GregorianCalendar verTime;
	private long versioningTime;
	
	
	
	

	public FileVersion(File relativeFile, long versioningTime, int verNum) {
		super();
		this.verNum = verNum;
		this.relativeFile = relativeFile;
		this.versioningTime = versioningTime;
		this.verTime = new GregorianCalendar();
		this.verTime.setTimeInMillis(versioningTime);
		this.content = getFileActualContent();
	}

	
	public byte[] getFileActualContent() {
		
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		FileInputStream fis = null;
		try {
			fis = new FileInputStream(relativeFile);
			
			byte[] buffer = new byte[4096];
			int byteRead;
			
			while((byteRead = fis.read(buffer, 0, buffer.length)) != -1) {
				baos.write(buffer, 0, byteRead);
			}
			
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally {
			if(fis != null) {
				try {
					fis.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		
		return baos.toByteArray();
	}
	
	
	public byte[] getContent() {
		return content;
	}

	public GregorianCalendar getVerTime() {
		return verTime;
	}

	public File getRelativeFile() {
		return relativeFile;
	}

	public long getVersioningTime() {
		return versioningTime;
	}


	public int getVerNum() {
		return verNum;
	}
	
	
	
	
	

}
