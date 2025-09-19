package com.ilayangudi_news_posting.file_service;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;
import reactor.core.publisher.Mono;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.reactive.function.client.WebClient;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
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
		return Flux.fromArray(files).filter(file -> !file.isEmpty())
				.flatMap(file -> Mono.fromCallable(() -> getNewsImageAndVideoFilepath(file, folder))).collectList()
				.block(); // waits for all uploads
	}

	public String getNewsImageAndVideoFilepath(MultipartFile file, String folder) throws IOException {
		try {
			String originalFileName = file.getOriginalFilename();
			String extension = (originalFileName != null && originalFileName.contains("."))
					? originalFileName.substring(originalFileName.lastIndexOf("."))
					: "";

			String uniqueFileName = UUID.randomUUID().toString() + "_"
					+ LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss")) + extension;

			String filePath = folder + "/" + uniqueFileName;

			String url = supabaseUrl + "/storage/v1/object/" + bucketName + "/" + filePath;

			webClient.put().uri(url).header(HttpHeaders.AUTHORIZATION, "Bearer " + supabaseKey)
					.header(HttpHeaders.CONTENT_TYPE, "application/octet-stream")
					.bodyValue(new InputStreamResource(file.getInputStream())).retrieve().bodyToMono(String.class)
					.block();

			return supabaseUrl + "/storage/v1/object/public/" + bucketName + "/" + filePath;

		} catch (IOException e) {
			throw new RuntimeException("Error uploading file: " + file.getOriginalFilename(), e);
		}
	}

	public void deleteFileFromSupabase(String fileUrl) {
		if (fileUrl == null || fileUrl.isEmpty())
			return;

		// fileUrl la irundhu relative path extract pannanum
		// ex:
		// https://xyz.supabase.co/storage/v1/object/public/mybucket/userProfilePics/abc.jpg
		String relativePath = fileUrl.substring(fileUrl.indexOf(bucketName) + bucketName.length() + 1);

		String url = supabaseUrl + "/storage/v1/object/" + bucketName + "/" + relativePath;

		webClient.delete().uri(url).header(HttpHeaders.AUTHORIZATION, "Bearer " + supabaseKey).retrieve()
				.bodyToMono(Void.class).block();
	}

	// Generate signed URL for single file
	private String generateSignedUrl(String publicUrl, int expiryInSeconds) {
		if (publicUrl == null || publicUrl.isEmpty())
			return "";
		try {
			String relativePath = publicUrl.replace(supabaseUrl + "/storage/v1/object/public/" + bucketName + "/", "");

			// encode path safely except "/"
			String safePath = URLEncoder.encode(relativePath, StandardCharsets.UTF_8.toString())
					.replaceAll("\\+", "%20").replaceAll("%2F", "/"); // keep slashes

			String url = supabaseUrl + "/storage/v1/object/sign/" + bucketName + "/" + safePath;

			Map<String, Object> requestBody = Map.of("expiresIn", expiryInSeconds);

			String signedUrlJson = webClient.post().uri(url).header(HttpHeaders.AUTHORIZATION, "Bearer " + supabaseKey)
					.bodyValue(requestBody).retrieve().bodyToMono(String.class).block();

			JsonNode jsonNode = new ObjectMapper().readTree(signedUrlJson);
			return supabaseUrl + "/storage/v1" + jsonNode.get("signedURL").asText(); // Full https:// URL

		} catch (Exception e) {
			throw new RuntimeException("Error generating signed URL "+e.getMessage(), e);
		}
	}

	public List<String> generateSignedUrls(List<String> fileUrls, int expiryInSeconds) {
		return fileUrls.stream().map(url -> generateSignedUrl(url, expiryInSeconds)).collect(Collectors.toList());
	}

}
