package org.testproject.service.impl;

import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.servlet.annotation.WebServlet;

import org.testproject.entity.User;
import org.testproject.service.UserService;

@WebService(endpointInterface = "org.testproject.service.UserService")
@WebServlet
public class UserServiceImpl implements UserService  {

  @Override
  @WebMethod
  public User getUser() {
      User user = new User();
      user.setUsername("testuser");
      user.setPassword("testpassword");
      user.setPin(123456);        
      return user; 
  }
  

}