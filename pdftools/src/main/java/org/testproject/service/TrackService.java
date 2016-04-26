package org.testproject.service;

import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.jws.soap.SOAPBinding.Style;

import org.testproject.entity.Track;
 
@WebService
@SOAPBinding(style = Style.DOCUMENT)
public interface TrackService {
 
    @WebMethod Track getTrack();

}
