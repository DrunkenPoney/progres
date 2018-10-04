package tp2.models.io.data;

import tp2.models.db.documents.ClientModel;
import tp2.models.db.documents.GroupModel;

import java.io.Serializable;

public class FileData extends TransmissionData implements Serializable {
	private final String fileName;
	private final byte[] fileContent;
	
	public FileData(ClientModel sender, String fileName, byte[] fileContent, GroupModel target) {
		super(sender, target);
		this.fileName = fileName;
		this.fileContent = fileContent;
	}
	
	public String getFileName() {
		return fileName;
	}
	
	public byte[] getFileContent() {
		return fileContent;
	}
}
