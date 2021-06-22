package app.dto.storage;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonPropertyOrder({"id", "ownerId", "fileName", "relativeFilePath", "rawFileURL", "fileFormat", "bytes", "fileType", "createdAt"})
@ApiModel
public class StorageResponseData {

    @ApiModelProperty(value = "id", example = "string")
    private String id;

    @ApiModelProperty(value = "ownerId", example = "12")
    private Long ownerId;

    @ApiModelProperty(value = "fileName", example = "String")
    private String fileName;

    @ApiModelProperty(value = "relativeFilePath", example = "String")
    private String relativeFilePath;

    @ApiModelProperty(value = "rawFileURL", example = "String")
    private String rawFileURL;

    @ApiModelProperty(value = "fileFormat", example = "String")
    private String fileFormat;

    @ApiModelProperty(value = "bytes", example = "0")
    private Long bytes;

    @ApiModelProperty(value = "fileType", example = "IMAGE")
    private String fileType;

    @ApiModelProperty(value = "createdAt", example = "0")
    private Long createdAt;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Long getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(Long ownerId) {
        this.ownerId = ownerId;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getRelativeFilePath() {
        return relativeFilePath;
    }

    public void setRelativeFilePath(String relativeFilePath) {
        this.relativeFilePath = relativeFilePath;
    }

    public String getRawFileURL() {
        return rawFileURL;
    }

    public void setRawFileURL(String rawFileURL) {
        this.rawFileURL = rawFileURL;
    }

    public String getFileFormat() {
        return fileFormat;
    }

    public void setFileFormat(String fileFormat) {
        this.fileFormat = fileFormat;
    }

    public Long getBytes() {
        return bytes;
    }

    public void setBytes(Long bytes) {
        this.bytes = bytes;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public Long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Long createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return "StorageResponseData{" +
                "id='" + id + '\'' +
                ", ownerId=" + ownerId +
                ", fileName='" + fileName + '\'' +
                ", relativeFilePath='" + relativeFilePath + '\'' +
                ", rawFileURL='" + rawFileURL + '\'' +
                ", fileFormat='" + fileFormat + '\'' +
                ", bytes=" + bytes +
                ", fileType='" + fileType + '\'' +
                ", createdAt=" + createdAt +
                '}';
    }
}
