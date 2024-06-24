package com.paintingscollectors.repository;

import com.paintingscollectors.model.entity.Painting;
import com.paintingscollectors.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface PaintingRepository extends JpaRepository<Painting,Long> {
    List<Painting> findAllByOwner(User user);
    @Query("SELECT u.favouritePaintings FROM User u WHERE u.id = :id")

    Set<Painting> findAllFavouritesPaintingsByUser(long id);

    @Query("SELECT p FROM Painting p WHERE p.owner.id != :id")
    List<Painting> findAllPaintingsExcept(long id);

    @Query(value = "SELECT * FROM paintings ORDER BY votes DESC, name ASC LIMIT 2", nativeQuery = true)
    List<Painting> findMostRated();
}
