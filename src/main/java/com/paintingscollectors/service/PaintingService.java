package com.paintingscollectors.service;

import com.paintingscollectors.model.dto.AddPaintingDto;
import com.paintingscollectors.model.entity.Painting;

import java.util.List;

public interface PaintingService {
    List<Painting> getAllPaintingsExcept(long id);

    List<Painting> getMostRated();

    boolean add(AddPaintingDto addPaintingDto);

    void delete(long id);

    void addToFavourite(long id);

    void vote(long id);

    void removeFromFavourites(long id);
}
