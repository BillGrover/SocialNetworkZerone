package app.dto.support;

import app.config.AppConstant;
import app.model.SupportMessage;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.text.SimpleDateFormat;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class SupportMessageDto {

    private final String date;
    private final String customer;
    private final String admin;
    private final String text;

    public SupportMessageDto(SupportMessage message) {
        date = new SimpleDateFormat(AppConstant.DATE_FORMAT).format(message.getDate());
        customer = message.getAdmin() == null ? message.getOrder().getName() : null;
        admin = message.getAdmin() != null ? message.getAdmin().getName() : null;
        text = message.getText();
    }

    public String getDate() {
        return date;
    }

    public String getCustomer() {
        return customer;
    }

    public String getAdmin() {
        return admin;
    }

    public String getText() {
        return text;
    }

}
