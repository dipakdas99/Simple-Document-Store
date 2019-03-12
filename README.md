# Simple Document store using RESTful Web Services and Spring Boot

- Creating FilesModel, UploadFileResponse and FileRepository
- Implementing GET Methods for download files in File Controller
- Implementing POST Method to upload files in File Controller.
- Enhancing POST Method to return HTTP Status Code (200) and body will contain (filename, download uri, content type, size)
- Implementing Exception Handling - 404 (if Resource Not Found)
- Implementing Generic Exception format to handle for all Resources
- Implementing DELETE Method to delete file from database
- Implementing swagger v2 for API document.
- Configuration MySQL with Spring Boot.
- All connection configuration done in application.properties file for easy flexibility and maintainability.
- Accept and provide both XML and JSON format.


## What You Will Need?

- Java 8
- Eclipse/STS
- Maven
- Embedded Tomcat
- Postman REST Services Client

## Multiple database connection?
Yes, it will support any types of database connection, all you need to do is provide all connection details inside application.properties. If multiple connection is requires then provide multiple connection details.
