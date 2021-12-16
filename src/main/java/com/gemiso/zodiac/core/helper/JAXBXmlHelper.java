package com.gemiso.zodiac.core.helper;

import javax.naming.NamingException;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import java.io.StringWriter;

public class JAXBXmlHelper {

    public static String marshal(Object obj, Class<?> instanceClass) {
        String rval = null;
        JAXBContext jaxbContext = null;
        Marshaller jaxbMarshaller = null;
        StringWriter writer = new StringWriter();

        System.out.println("obj :" + obj);
        try {
            jaxbContext = JAXBContext.newInstance(instanceClass);
            jaxbMarshaller = jaxbContext.createMarshaller();
            jaxbMarshaller.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");
            jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
            jaxbMarshaller.setProperty(Marshaller.JAXB_FRAGMENT, Boolean.TRUE);
            writer.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\r\n");
            jaxbMarshaller.marshal(obj, writer);


            rval = writer.toString();

        }catch (JAXBException e) {
            // TODO: handle exceptione
            /*e.printStackTrace();*/
            System.out.println("JAXBException Occured");
        }
        return rval;
    }
}
