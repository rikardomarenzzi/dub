package de.neuefische.rikardo.dub.service;

import de.neuefische.rikardo.dub.model.actor.Actor;
import de.neuefische.rikardo.dub.model.actor.ActorPreview;
import de.neuefische.rikardo.dub.model.actor.TmdbActor;
import de.neuefische.rikardo.dub.model.movie.Movie;
import de.neuefische.rikardo.dub.model.movie.MoviePreview;
import de.neuefische.rikardo.dub.model.movie.TmdbMovie;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MovieService {

    private final TmdbService tmdbService;

    private final String tmdbUrlPath = "https://image.tmdb.org/t/p/w500";

    public MovieService(TmdbService tmdbService) {
        this.tmdbService = tmdbService;
    }

    public List<MoviePreview> getMoviePreviewsByName(String name) {

        return tmdbService.getTmdbMoviesByName(name)
                .stream()
                .filter(item -> item.getPoster_path() != null)
                .map(tmdbMovie -> MoviePreview.builder()
                        .id(tmdbMovie.getId())
                        .name(tmdbMovie.getTitle())
                        .image(tmdbUrlPath + tmdbMovie.getPoster_path())
                        .type("movie")
                        .build())
                .collect(Collectors.toList());
    }

    public List<MoviePreview> getMoviePreviewsById(String id) {

        return tmdbService.getTmdbActorMovieCreditsById(id)
                .stream()
                .filter(item -> item.getPoster_path() != null)
                .map(tmdbMovie -> MoviePreview.builder()
                        .id(tmdbMovie.getId())
                        .name(tmdbMovie.getTitle())
                        .image(tmdbUrlPath + tmdbMovie.getPoster_path())
                        .type("movie")
                        .build())
                .collect(Collectors.toList());
    }



    public Movie getMovieById(String id) {
        TmdbMovie tmdbMovie = tmdbService.getTmdbMovieById(id);
        return new Movie(
                tmdbMovie.getId(),
                tmdbMovie.getTitle(),
                tmdbUrlPath + tmdbMovie.getPoster_path(),
                tmdbMovie.getOverview(),
                tmdbMovie.getRelease_date(),
                tmdbMovie.getRuntime(),
                tmdbMovie.getOriginal_language(),
                tmdbMovie.getBudget(),
                tmdbMovie.getRevenue(),
                "movie");
    }

    public List<ActorPreview> getMovieCrewById(String id) {

        List<ActorPreview> actorPreviews = new ArrayList<>();
        List<TmdbActor> tmdbActors = tmdbService.getTmdbMovieCrewById(id)
                .stream()
                .filter(item -> item.getProfile_path() != null)
                .filter(item -> item.getKnown_for_department().equals("Acting"))
                .collect(Collectors.toList());

        for (TmdbActor tmdbActor : tmdbActors) {
            ActorPreview actorPreview = new ActorPreview(
                    tmdbActor.getId(),
                    tmdbActor.getName(),
                    tmdbActor.getCharacter(),
                    tmdbUrlPath + tmdbActor.getProfile_path(),
                    "actor");
            actorPreviews.add(actorPreview);
        }

        return actorPreviews;
    }
}
