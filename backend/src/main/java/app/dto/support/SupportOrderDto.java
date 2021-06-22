package app.dto.support;

import app.config.AppConstant;
import app.model.SupportOrder;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

@ApiModel
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class SupportOrderDto {

    private final long id;
    private final String name;
    private final String title;
    private final String date;
    private final String status;
    private final String imageUrl;
    private List<SupportMessageDto> messageList;

    public SupportOrderDto(SupportOrder order) {
        id = order.getId();
        name = order.getName();
        title = order.getTitle();
        date = new SimpleDateFormat(AppConstant.DATE_FORMAT).format(order.getDate());
        status = order.getStatus().name();
        imageUrl = "/admin/images/support-logo.svg";
        messageList = new ArrayList<>();
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getTitle() {
        return title;
    }

    public String getDate() {
        return date;
    }

    public String getStatus() {
        return status;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public List<SupportMessageDto> getMessageList() {
        return messageList;
    }

    public void setMessageList(List<SupportMessageDto> messageList) {
        this.messageList = messageList;
    }

}
