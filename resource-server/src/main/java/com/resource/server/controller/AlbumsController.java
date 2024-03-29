package com.resource.server.controller;

import com.resource.server.model.Album;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.ws.rs.core.MediaType;
import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping(value = "/albums", produces = MediaType.APPLICATION_JSON)
public class AlbumsController {

    @GetMapping(produces = "application/json")
    @ResponseBody
    public List<Album> getAlbums() {
        return Collections.singletonList(Album.builder().albumId("album id").build());
    }

}
