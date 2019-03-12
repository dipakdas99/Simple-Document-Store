package com.test.SimpleDocumentStore.controller;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.test.SimpleDocumentStore.model.FilesModel;
import com.test.SimpleDocumentStore.model.UploadFileResponse;
import com.test.SimpleDocumentStore.services.FileStorageServices;
/**
 * @author Dipak
 * <p>This API will work as upload, download and delete all 
 * types of files. </p>
 *
 */
@RestController
public class FileController {
	/**
	 * Binding service the class to use the services using @Annotation
	 */
	@Autowired
    private FileStorageServices fileStorageServices;
	/**
	 * <p>Accept only one file at a time to upload into database
	 * and also generate a download link for the same uploaded file for
	 * download.</p>
	 * @param file
	 * @return response as name, url, content type and size.
	 */
	@PostMapping(value = "/uploadFile", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public UploadFileResponse uploadFile(@RequestParam("file") MultipartFile file) {
        FilesModel dbFile = fileStorageServices.storeFile(file);

        String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/downloadFile/")
                .path(dbFile.getId())
                .toUriString();

        return new UploadFileResponse(dbFile.getFileName(), fileDownloadUri,
                file.getContentType(), file.getSize());
    }
	/**
	 * <p> Accept multiple times at a time to upload in the database,
	 * iterating one by one file from multipart to call <url>/uploadFile</url>
	 * for inserting into database.
	 * @param files
	 * @return response as name, url, content type and size (from @url /uploadFile)
	 */
	@PostMapping(value = "/uploadMultipleFiles", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public List<UploadFileResponse> uploadMultipleFiles(@RequestParam("files") MultipartFile[] files) {
        return Arrays.asList(files)
                .stream()
                .map(file -> uploadFile(file))
                .collect(Collectors.toList());
    }
	/**
	 * <p>Accept only string type of input to download the file from database
	 * and returning body data</p>
	 * @param fileId
	 * @return <code>ResponseEntity.ok</code>
	 */
    @GetMapping("/downloadFile/{fileId}")
    public ResponseEntity<Resource> downloadFile(@PathVariable String fileId) {
        // Load file from database
    	FilesModel dbFile = fileStorageServices.getFile(fileId);

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(dbFile.getFileType()))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + dbFile.getFileName() + "\"")
                .body(new ByteArrayResource(dbFile.getData()));
    }
    
    /**
     * <p>Accept only string type of input <code>delId</code> to delete the file from database
	 * and returning body data as 204 status</p>
     * @param delId
     * @return <code>HttpStatus.NO_CONTENT</code>
     */
    @DeleteMapping("/deleteFile/{delId}")
    public ResponseEntity<Resource> deleteFile(@PathVariable String delId){
    	
    	fileStorageServices.deleteFile(delId);
		return new ResponseEntity<Resource>(HttpStatus.NO_CONTENT);
    	
    }

}
