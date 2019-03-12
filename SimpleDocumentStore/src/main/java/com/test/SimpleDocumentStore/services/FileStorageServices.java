package com.test.SimpleDocumentStore.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;

import com.test.SimpleDocumentStore.exception.FileNotFoundException;
import com.test.SimpleDocumentStore.exception.FileStorageException;
import com.test.SimpleDocumentStore.model.FileRepository;
import com.test.SimpleDocumentStore.model.FilesModel;

@Service
public class FileStorageServices {
	@Autowired
    private FileRepository fileRepository;

    public FilesModel storeFile(MultipartFile file) {
        // Normalize file name
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());

        try {
            // Check if the file's name contains invalid characters
            if(fileName.contains("..")) {
                throw new FileStorageException("Sorry! Filename contains invalid path sequence " + fileName);
            }

            FilesModel dbFile = new FilesModel(fileName, file.getContentType(), file.getBytes());

            return fileRepository.save(dbFile);
        } catch (IOException ex) {
            throw new FileStorageException("Could not store file " + fileName + ". Please try again!", ex);
        }
    }

    public FilesModel getFile(String fileId) {
        return fileRepository.findById(fileId)
                .orElseThrow(() -> new FileNotFoundException("File not found with id " + fileId));
    }

	public FilesModel deleteFile(String delId) {
		//Id is exist or not in database
		if (fileRepository.findById(delId) != null)
		{
			fileRepository.deleteById(delId);
		}else
			new FileNotFoundException("File not found with id " + delId);
		
		return null;
				
	}
}
