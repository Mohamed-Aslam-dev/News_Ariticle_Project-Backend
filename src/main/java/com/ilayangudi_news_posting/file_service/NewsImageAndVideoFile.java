package com.ilayangudi_news_posting.file_service;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class NewsImageAndVideoFile {

	public List<String> getNewsImageAndVideoFilepaths(MultipartFile[] files, String outputPath) throws IOException {
	    List<String> savedPaths = new ArrayList<>();
	    if (files != null) {
	        for (MultipartFile file : files) {
	            if (!file.isEmpty()) {
	                savedPaths.add(getNewsImageAndVideoFilepath(file, outputPath));
	            }
	        }
	    }
	    return savedPaths;
	}
	
	public String getNewsImageAndVideoFilepath(MultipartFile file, String outputPath) throws IOException {

		try {
			// 1. Upload directory
			
			File dir = new File(outputPath);
			if (!dir.exists())
				dir.mkdirs();

			// 2. Original file extension
			String originalFileName = file.getOriginalFilename();
			String extension = "";
			if (originalFileName != null && originalFileName.contains(".")) {
				extension = originalFileName.substring(originalFileName.lastIndexOf("."));
			}

			// 3. Generate unique filename (UUID + timestamp)
			String uniqueFileName = UUID.randomUUID().toString() + "_"
					+ LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss")) + extension;

			// 4. File path
			String filePath = outputPath + uniqueFileName;

			// 5. Save file
			file.transferTo(new File(filePath));

			// 6. Return only relative path (to store in DB)
			// ✅ Instead of returning Windows path, return URL
	        return "http://localhost:8080/images/" + uniqueFileName;
		} catch (IOException io) {
			throw new RuntimeException("Error while saving news file", io);
		}
	}

}
