package com.gemiso.zodiac.app.appInterface.interfaceExceptionDTO;

import lombok.Data;
import org.springframework.http.HttpStatus;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.Map;

@Data
@XmlRootElement(name = "error_response")
@XmlAccessorType(XmlAccessType.FIELD)
public class InterfaceExceptionDTO {

    @XmlElement(name="code")
    private String code;
    @XmlElement(name="status")
    private HttpStatus status;
    @XmlElement(name="errors")
    private Map errors;
    @XmlElement(name="message")
    private String message;
}
