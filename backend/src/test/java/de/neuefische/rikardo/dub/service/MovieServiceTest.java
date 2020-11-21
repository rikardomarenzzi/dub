package de.neuefische.rikardo.dub.service;

import de.neuefische.rikardo.dub.api.ApiService;
import de.neuefische.rikardo.dub.model.actor.Actor;
import de.neuefische.rikardo.dub.model.actor.ApiActor;
import de.neuefische.rikardo.dub.model.movie.Movie;
import de.neuefische.rikardo.dub.model.movie.ApiMovie;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;


import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;

class MovieServiceTest {

    final ApiService apiService = mock(ApiService.class);

    final MovieService movieService = new MovieService(apiService);

    ApiMovie apiMovie = new ApiMovie("603","The Matrix", "/image.jpg","overview","1999-03-30","136","en","0","0");
    List<ApiMovie> apiMovies = new ArrayList<>(List.of(apiMovie));

    Movie movie = new Movie("603","The Matrix", "/image.jpg","overview","1999-03-30","136","en","0","0","movie");
    List<Movie> movies = new ArrayList<>(List.of(movie));

    List<ApiActor> apiActors = new ArrayList<>(List.of(
            new ApiActor("6384","Keanu Reeves","/image.jpg","Neo","biography","1964-09-02","Beirut, Lebanon","Acting")
    ));

    List<Actor> actors = new ArrayList<>(List.of(
            new Actor("6384","Keanu Reeves","/image.jpg","Neo","biography","1964-09-02","Beirut, Lebanon","Acting")
    ));

    @Test
    @DisplayName("The method should return the MovieSearchResult by name")
    void getMovieSearchResultByNameTest() {
        //GIVEN
        String name = "The Matrix";
        when(apiService.getMovieSearchResultByName(name)).thenReturn(apiMovies);
        //WHEN
        List<Movie> result = movieService.getMovieSearchResultByName(name);
        //THEN
        assertThat(result,is(movies));
    }

    @Test
    @DisplayName("The method should return the movie details by id")
    void getMovieDetailsByIdTest() {
        //GIVEN
        String id = "603";
        when(apiService.getMovieDetailsById(id)).thenReturn(apiMovie);
        //WHEN
        Movie result = movieService.getMovieDetailsById(id);
        //THEN
        assertThat(result,is(movie));
    }

    @Test
    @DisplayName("The method should return the movie crew by id")
    void getMovieCrewByIdTest() {
        //GIVEN
        String id = "603";
        when(apiService.getMovieCrewById(id)).thenReturn(apiActors);
        //WHEN
        List<Actor> result = movieService.getMovieCrewById(id);
        //THEN
        assertThat(result,is(actors));
    }

}