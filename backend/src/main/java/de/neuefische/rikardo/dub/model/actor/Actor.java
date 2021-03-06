package de.neuefische.rikardo.dub.model.actor;

import de.neuefische.rikardo.dub.model.movie.MoviePreview;
import de.neuefische.rikardo.dub.model.voiceactor.VoiceActorPreview;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Actor {

    private String id;
    private String name;
    private String image;
    private String character;
    private String biography;
    private String birthday;
    private String placeOfBirth;
    private String type;
    private List<MoviePreview> movies;
    private List<VoiceActorPreview> voiceActors;
}
