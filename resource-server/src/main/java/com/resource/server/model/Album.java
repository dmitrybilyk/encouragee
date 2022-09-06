package com.resource.server.model;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@Builder
@Data
public class Album {
    private String userId;
    private String albumId;
    private String albumTitle;
    private String albumDescription;
    private String albumUrl;
}
