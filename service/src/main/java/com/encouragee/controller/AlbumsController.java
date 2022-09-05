package com.encouragee.controller;

import com.encouragee.model.Album;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Collections;
import java.util.List;

@Controller
public class AlbumsController {

    @GetMapping("/albums")
    public String getAlbums(Model model) {
        List<Album> albumList = Collections.singletonList(
                Album.builder().albumTitle("my title").build());
        model.addAttribute("albums", albumList);
        return "albums";
    }

}
