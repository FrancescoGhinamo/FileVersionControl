package fvc.backend.service;

public interface IFileService {

	public void saveData(Object s, String dest);
	
	public Object loadData(String source);
	
}
