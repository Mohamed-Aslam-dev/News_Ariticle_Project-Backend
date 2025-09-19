package com.ilayangudi_news_posting.file_service;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import reactor.core.publisher.Mono;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.reactive.function.client.WebClient;

import reactor.core.publisher.Flux;

@Service
public class NewsImageAndVideoFile {

    private final WebClient webClient;

    @Value("${supabase.url}")
    private String supabaseUrl;

    @Value("${supabase.key}")
    private String supabaseKey;

    @Value("${supabase.bucket}")
    private String bucketName;

    public NewsImageAndVideoFile(WebClient.Builder builder) {
        this.webClient = builder.build();
    }

    public List<String> getNewsImageAndVideoFilepaths(MultipartFile[] files, String folder) throws IOException {
        return Flux.fromArray(files)
            .filter(file -> !file.isEmpty())
            .flatMap(file -> Mono.fromCallable(() -> getNewsImageAndVideoFilepath(file, folder)))
            .collectList()
            .block(); // waits for all uploads
    }

    public String getNewsImageAndVideoFilepath(MultipartFile file, String folder) throws IOException{
        try {
            String originalFileName = file.getOriginalFilename();
            String extension = (originalFileName != null && originalFileName.contains(".")) ?
                    originalFileName.substring(originalFileName.lastIndexOf(".")) : "";

            String uniqueFileName = UUID.randomUUID().toString() + "_"
                    + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss")) + extension;

            String filePath = folder + "/" + uniqueFileName;

            String url = supabaseUrl + "/storage/v1/object/" + bucketName + "/" + filePath;

            webClient.post()
                    .uri(url)
                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + supabaseKey)
                    .header(HttpHeaders.CONTENT_TYPE, "application/octet-stream")
                    .bodyValue(new InputStreamResource(file.getInputStream()))
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();

            return supabaseUrl + "/storage/v1/object/public/" + bucketName + "/" + filePath;

        } catch (IOException e) {
            throw new RuntimeException("Error uploading file: " + file.getOriginalFilename(), e);
        }
    }
    
    public void deleteFileFromSupabase(String fileUrl) {
        if (fileUrl == null || fileUrl.isEmpty()) return;

        // fileUrl la irundhu relative path extract pannanum
        // ex: https://xyz.supabase.co/storage/v1/object/public/mybucket/userProfilePics/abc.jpg
        String relativePath = fileUrl.substring(fileUrl.indexOf(bucketName) + bucketName.length() + 1);

        String url = supabaseUrl + "/storage/v1/object/" + bucketName + "/" + relativePath;

        webClient.delete()
            .uri(url)
            .header(HttpHeaders.AUTHORIZATION, "Bearer " + supabaseKey)
            .retrieve()
            .bodyToMono(Void.class)
            .block();
    }
    
    public List<String> generateSignedUrls(List<String> filePaths, int expiryInSeconds) {
        return filePaths.stream()
                .map(path -> {
                	String url = supabaseUrl + "/storage/v1/object/sign/" + bucketName + "/" + filePaths;
                    return webClient.post()
                            .uri(url)
                            .header(HttpHeaders.AUTHORIZATION, "Bearer " + supabaseKey)
                            .bodyValue("{\"expiresIn\": " + expiryInSeconds + "}")
                            .retrieve()
                            .bodyToMono(String.class)
                            .block();
                })
                .toList();
    }

    
}

