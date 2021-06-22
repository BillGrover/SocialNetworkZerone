package app.service.implementations;

import app.dto.storage.StorageResponse;
import app.dto.storage.StorageResponseData;
import app.exceptions.*;
import app.model.Person;
import app.repository.PersonRepository;
import app.service.MainService;
import app.service.StorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.UUID;

@Service
public class StorageServiceImpl implements StorageService {

    @Value("${upload.path}")
    private String uploadPath;

    private final PersonRepository personRepository;
    private final MainService mainService;

    @Autowired
    public StorageServiceImpl(PersonRepository personRepository,
                              MainService mainService) {
        this.personRepository = personRepository;
        this.mainService = mainService;
    }

    public ResponseEntity<StorageResponse> store(String token, MultipartFile file, String type) throws AppException {
        String filename = file.getOriginalFilename();
        if (filename == null) {
            throw new BadRequestException("Не определен файл для записи");
        } else {
            File uploadDir = new File(uploadPath);
            if (!uploadDir.exists() && !uploadDir.mkdir()) {
                throw new ServiceUnavailableException("Не удалось создать папку для хранения");
            }
            String uuidFile = UUID.randomUUID().toString();
            String resultName = uuidFile + "_" + filename;
            String fileFormat = getExpansionFromFileName(filename);
            String resultPath = uploadPath + File.separator + resultName;
            try {
                file.transferTo(new File(resultPath));
            } catch (IOException e) {
                throw new ServiceUnavailableException("Файл не сохранен");
            }
            Person person = mainService.getPersonByToken(token);
            person.setPhoto("/img/" + resultName);
            personRepository.save(person);

            StorageResponseData data = new StorageResponseData();
            String sUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                    .build()
                    .toUriString();
            data.setId("0");
            data.setOwnerId(person.getId());
            data.setFileName(resultName);
            data.setRelativeFilePath("/img/" + resultName);
            data.setRawFileURL(sUri + "/" + resultName);
            data.setFileFormat(fileFormat);
            data.setBytes(file.getSize());
            data.setFileType(type);
            data.setCreatedAt(new Date().getTime());

            return ResponseEntity.ok(new StorageResponse(data));
        }
    }

    private String getExpansionFromFileName(String filename) {
        return (filename != null) ? filename.substring(filename.lastIndexOf(".") + 1) : "";
    }


}
