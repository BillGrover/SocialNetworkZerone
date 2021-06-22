package app.controller;

import app.dto.storage.StorageResponse;
import app.exceptions.AppException;
import app.service.StorageService;
import io.swagger.annotations.*;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@Api(tags = "Работа с хранилищем сервиса")
public class StorageController {

    private final StorageService storageService;

    public StorageController(StorageService storageService) {
        this.storageService = storageService;
    }

    @PostMapping(value = "/api/v1/storage", produces = "application/json")
    @ApiOperation(value = "Работа с хранилищем сервиса", notes = "Загрузка файла в хранилище")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Успешная загрузка файла", response = StorageResponse.class)})
    public ResponseEntity<StorageResponse> upload(
            @RequestHeader("Authorization") String token,
            @ApiParam(value = "File", hidden = true)
            @Parameter(in = ParameterIn.QUERY, name = "file to upload", description = "загружаемый файл",
                    example = "file")
            @RequestParam(value = "file") MultipartFile file, @ApiParam(value = "Тип файла", allowableValues = "IMAGE")
            @Parameter(name = "type", description = "тип файла", example = "IMAGE")
            @RequestParam(value = "type") String type)
            throws AppException {
        return storageService.store(token, file, type);
    }
}
