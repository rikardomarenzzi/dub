package de.neuefische.rikardo.dub.controller;

import de.neuefische.rikardo.dub.db.UserDao;
import de.neuefische.rikardo.dub.db.VoiceActorMongoDb;
import de.neuefische.rikardo.dub.dto.LoginDto;
import de.neuefische.rikardo.dub.model.actor.ActorPreview;
import de.neuefische.rikardo.dub.model.movie.MoviePreview;
import de.neuefische.rikardo.dub.model.user.dubUser;
import de.neuefische.rikardo.dub.model.voiceactor.VoiceActor;
import de.neuefische.rikardo.dub.model.voiceactor.VoiceActorPreview;
import de.neuefische.rikardo.dub.service.TmdbService;
import de.neuefische.rikardo.dub.model.actor.Actor;
import de.neuefische.rikardo.dub.model.actor.TmdbActor;
import de.neuefische.rikardo.dub.model.movie.Movie;
import de.neuefische.rikardo.dub.model.movie.TmdbMovie;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.TestPropertySource;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(properties = {"jwt.secretkey=somesecrettoken"})
class TmdbControllerIntegrationTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate testRestTemplate;

    @MockBean
    private TmdbService tmdbService;

    @MockBean
    private VoiceActorMongoDb voiceActorMongoDb;


    @Autowired
    private UserDao userDao;

    @BeforeEach
    public void setupDb() {

        String password = new BCryptPasswordEncoder().encode("super-password");
        userDao.save(new dubUser("rikardo", password));

    }

    private String login(){
        ResponseEntity<String> response = testRestTemplate.postForEntity("http://localhost:" + port + "/auth/login", new LoginDto(
                "rikardo",
                "super-password"
        ), String.class);

        return response.getBody();
    }

    private <T> HttpEntity<T> getValidAuthorizationEntity(T data) {
        String token = login();

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        return new HttpEntity<T>(data,headers);
    }

    TmdbMovie tmdbMovie = new TmdbMovie("603","The Matrix", "/image.jpg","overview","1999-03-30","136","en","0","0");
    List<TmdbMovie> tmdbMovies = new ArrayList<>(List.of(tmdbMovie));

    TmdbActor tmdbActor = new TmdbActor("6384","Keanu Reeves","/image.jpg","Neo","biography","1964-09-02","Beirut, Lebanon","Acting");
    List<TmdbActor> tmdbActors = new ArrayList<>(List.of(tmdbActor));

    MoviePreview moviePreview = new MoviePreview("603","The Matrix", "https://image.tmdb.org/t/p/w500/image.jpg","movie");
    List<MoviePreview> moviePreviews = new ArrayList<>(List.of(moviePreview));

    Movie movie = new Movie("603","The Matrix", "https://image.tmdb.org/t/p/w500/image.jpg","overview","1999-03-30","136","en","0","0","movie");

    ActorPreview actorPreview = new ActorPreview("6384","Keanu Reeves","Neo","https://image.tmdb.org/t/p/w500/image.jpg","actor");
    List<ActorPreview> actorPreviews = new ArrayList<>(List.of(actorPreview));

    VoiceActorPreview voiceActorPreview = new VoiceActorPreview("2","Benjamin Völz","/benjamin_voelz.jpeg","voiceactor");
    List<VoiceActorPreview> voiceActorPreviews = new ArrayList<>(List.of(voiceActorPreview));

    List<ActorPreview> actorPreviewsWunder = new ArrayList<>(List.of(
            new ActorPreview("19292","Adam Sandler","placeholder","https://image.tmdb.org/t/p/w500/image.jpg","actor"),
            new ActorPreview("9777","Cuba Gooding Jr.","placeholder","https://image.tmdb.org/t/p/w500/image.jpg","actor"),
            new ActorPreview("8784","Daniel Craig","placeholder","https://image.tmdb.org/t/p/w500/image.jpg","actor")
    ));

    List<ActorPreview> actorPreviewsVoelz = new ArrayList<>(List.of(
            new ActorPreview("6384","Keanu Reeves","placeholder","https://image.tmdb.org/t/p/w500/image.jpg","actor"),
            new ActorPreview("12640","David Duchovny","placeholder","https://image.tmdb.org/t/p/w500/image.jpg","actor"),
            new ActorPreview("13548","James Spader","placeholder","https://image.tmdb.org/t/p/w500/image.jpg","actor")
    ));
    List<VoiceActor> voiceActors = new ArrayList<>(List.of(
            new VoiceActor("1","Dietmar Wunder","/dietmar_wunder.jpeg","1965-12-05",actorPreviewsWunder,"voiceactor"),
            new VoiceActor("2","Benjamin Völz","/benjamin_voelz.jpeg","1960-05-13",actorPreviewsVoelz,"voiceactor")
    ));

    VoiceActor voiceActor = new VoiceActor("1","Dietmar Wunder","/dietmar_wunder.jpeg","1965-12-05",actorPreviews,"voiceactor");

    Actor actor = new Actor("6384","Keanu Reeves","https://image.tmdb.org/t/p/w500/image.jpg","Neo","biography","1964-09-02","Beirut, Lebanon","actor",moviePreviews,voiceActorPreviews);


    @Test
    public void getMoviePreviewsByNameTest() {
        //GIVEN
        String name = "The Matrix";
        String url = "http://localhost:" + port + "/api/search/movie/" + name;
        when(tmdbService.getTmdbMoviesByName(name)).thenReturn(tmdbMovies);
        //WHEN
        HttpEntity<Void> entity = getValidAuthorizationEntity(null);
        ResponseEntity<MoviePreview[]> response = testRestTemplate.exchange(url, HttpMethod.GET, entity, MoviePreview[].class);
        //THEN
        assertThat(response.getStatusCode(),is(HttpStatus.OK));
        assertThat(response.getBody(),is(moviePreviews.toArray()));
    }

    @Test
    public void getActorPreviewsByNameTest() {
        //GIVEN
        String name = "Keanu Reeves";
        String url = "http://localhost:" + port + "/api/search/actor/" + name;
        when(tmdbService.getTmdbActorsByName(name)).thenReturn(tmdbActors);
        //WHEN
        HttpEntity<Void> entity = getValidAuthorizationEntity(null);
        ResponseEntity<ActorPreview[]> response = testRestTemplate.exchange(url, HttpMethod.GET, entity, ActorPreview[].class);
        //THEN
        assertThat(response.getStatusCode(),is(HttpStatus.OK));
        assertThat(response.getBody(),is(actorPreviews.toArray()));
    }

    @Test
    public void getActorByIdTest() {
        //GIVEN
        String id = "6384";
        String url = "http://localhost:" + port + "/api/actor/" + id;
        when(tmdbService.getTmdbActorById(id)).thenReturn(tmdbActor);
        when(tmdbService.getTmdbActorMovieCreditsById(id)).thenReturn(tmdbMovies);
        when(voiceActorMongoDb.findAll()).thenReturn(voiceActors);
        //WHEN
        HttpEntity<Void> entity = getValidAuthorizationEntity(null);
        ResponseEntity<Actor> response = testRestTemplate.exchange(url, HttpMethod.GET, entity, Actor.class);
        //THEN
        assertThat(response.getStatusCode(),is(HttpStatus.OK));
        assertThat(response.getBody(),is(actor));
    }

    @Test
    public void getMovieByIdTest() {
        //GIVEN
        String id = "603";
        String url = "http://localhost:" + port + "/api/movie/" + id;
        when(tmdbService.getTmdbMovieById(id)).thenReturn(tmdbMovie);
        //WHEN
        HttpEntity<Void> entity = getValidAuthorizationEntity(null);
        ResponseEntity<Movie> response = testRestTemplate.exchange(url, HttpMethod.GET, entity, Movie.class);

        //THEN
        assertThat(response.getStatusCode(),is(HttpStatus.OK));
        assertThat(response.getBody(),is(movie));
    }

    @Test
    public void getMovieCrewByIdTest() {
        //GIVEN
        String id = "603";
        String url = "http://localhost:" + port + "/api/movie/" + id + "/crew";
        when(tmdbService.getTmdbMovieCrewById(id)).thenReturn(tmdbActors);
        //WHEN
        HttpEntity<Void> entity = getValidAuthorizationEntity(null);
        ResponseEntity<ActorPreview[]> response = testRestTemplate.exchange(url, HttpMethod.GET, entity, ActorPreview[].class);
        //THEN
        assertThat(response.getStatusCode(),is(HttpStatus.OK));
        assertThat(response.getBody(),is(actorPreviews.toArray()));
    }


}