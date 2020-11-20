package de.neuefische.rikardo.dub.model.movie;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MovieCatch{

    private String id;
    private String title;
    private String poster_path;
}