package app.service;

import app.dto.storage.StorageResponse;
import app.exceptions.AppException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

public interface StorageService {

    ResponseEntity<StorageResponse> store(String token, MultipartFile file, String type) throws AppException;
}
