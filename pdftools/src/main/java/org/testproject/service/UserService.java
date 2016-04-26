package org.testproject.service;

import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.jws.soap.SOAPBinding.Style;
import javax.servlet.annotation.WebServlet;

import org.testproject.entity.User;
 
@WebService(targetNamespace="org.testproject.service")
@SOAPBinding(style = Style.DOCUMENT)
public interface UserService {
 
    @WebMethod User getUser();

}
