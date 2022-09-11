package com.encouragee.controller;

import com.encouragee.model.Album;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;

import javax.print.attribute.standard.Media;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Controller
@Slf4j
public class AlbumsController {

    @Autowired
    private OAuth2AuthorizedClientService oAuth2AuthorizedClientService;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private WebClient webClient;

    @GetMapping(value = "/albums")
    public String getAlbums(Model model, @AuthenticationPrincipal OidcUser principal) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        log.info("authentication - " + authentication);
        OAuth2AuthenticationToken oAuth2AuthenticationToken = (OAuth2AuthenticationToken) authentication;

        OAuth2AuthorizedClient oAuth2AuthorizedClient =
                oAuth2AuthorizedClientService.loadAuthorizedClient(oAuth2AuthenticationToken.getAuthorizedClientRegistrationId(),
                oAuth2AuthenticationToken.getName());

        String accessToken = oAuth2AuthorizedClient.getAccessToken().getTokenValue();

//        log.info("access token - " + accessToken);
//
//        log.info("Principal - " + principal);
//
        OidcIdToken idToken = principal.getIdToken();
        String idTokenString = idToken.getTokenValue();
        log.info("IdTokenValue - " + idTokenString);

        String url = "http://localhost:8087/albums";
        HttpHeaders headers = new HttpHeaders();
//        headers.add("content-type", MediaType.APPLICATION_JSON);
        headers.add("Authorization", "Bearer " +accessToken);
        HttpEntity entity = new HttpEntity<>(headers);
        ResponseEntity<List<Album>> responseEntity =
                restTemplate.exchange(url, HttpMethod.GET, entity, new ParameterizedTypeReference<List<Album>>() {});
        List<Album> albumList = responseEntity.getBody();


//        !!!!!!!!!!!!!!!!!!!!!!!!!!!
//            List<Album> albumList = webClient.get().uri(url).retrieve().bodyToMono(
//                    new ParameterizedTypeReference<List<Album>> (){}).block();
//        List<Album> albumList = Collections.singletonList(
//                Album.builder().albumTitle("my title").build());
        model.addAttribute("albums", Objects.requireNonNullElseGet(albumList, ArrayList::new));
        return "albums";
    }

}
