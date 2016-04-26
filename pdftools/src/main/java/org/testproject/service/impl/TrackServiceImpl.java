package org.testproject.service.impl;

import javax.jws.WebService;

import org.testproject.entity.Track;
import org.testproject.service.TrackService;

@WebService(endpointInterface = "org.testproject.service.TrackService")
public class TrackServiceImpl implements TrackService  {

  @Override
  public Track getTrack() {
      Track track = new Track();
      track.setTitle("Test Track");
      track.setSinger("Test Singer");
      return track;
  }

}