package com.gemiso.zodiac.app.appInterface.interfaceExceptionDTO;

import lombok.Data;
import org.springframework.http.HttpStatus;

import javax.xml.bind.annotation.*;
import java.util.Map;

@Data
@XmlRootElement(name = "response")
@XmlAccessorType(XmlAccessType.FIELD)
public class InterfaceExceptionDTO {

    @XmlElement(name = "result")
    private InterfaceExceptionResultDTO result;

  /*  @XmlElement(name="code")
    private String code;
    @XmlElement(name="status")
    private HttpStatus status;
    @XmlElement(name="errors")
    private Map errors;
    @XmlElement(name="message")
    private String message;*/
}
