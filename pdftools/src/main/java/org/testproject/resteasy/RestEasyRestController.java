package org.testproject.resteasy;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.testproject.entity.Track;
import org.testproject.entity.User;

@Path("/resteasy")
public class RestEasyRestController {

    @GET
    @Path("/json/track")
    @Produces(MediaType.APPLICATION_JSON)
    public Track getTrackInJSON() {
        Track track = new Track();
        track.setTitle("Test Track");
        track.setSinger("Test Singer");
        return track;
    }

    @GET
    @Path("/json/track/add")
    @Produces(MediaType.APPLICATION_JSON)
    public Response createTrackInJSON(@QueryParam("title") String title, @QueryParam("singer") String singer) {
        Track track = new Track();
        track.setSinger(singer);
        track.setTitle(title);
        System.out.println("Track saved " + track);
        return Response.status(201).entity(track).build();
        
    }
    
    @POST
    @Path("/json/track")
    @Consumes("application/json")
    public Response createProductInJSON(Track track) {
        String result = "Product created : " + track;
        return Response.status(201).entity(result).build();
        
    }
    
    @GET
    @Path("/xml/user")
    @Produces(MediaType.APPLICATION_XML)
    public User getUserInXML(@QueryParam("title") String title, @QueryParam("singer") String singer) {
        User user = new User();
        user.setUsername("testuser");
        user.setPassword("testpassword");
        user.setPin(123456);        
        return user; 
    }
}
