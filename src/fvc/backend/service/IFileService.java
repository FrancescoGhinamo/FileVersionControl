package fvc.backend.service;

import java.io.File;

public interface IFileService {

	public void saveData(Object s, File dest);
	
	public Object loadData(File source);
	
}
