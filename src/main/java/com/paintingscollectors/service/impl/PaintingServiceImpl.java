package com.paintingscollectors.service.impl;

import com.paintingscollectors.model.dto.AddPaintingDto;
import com.paintingscollectors.model.entity.Painting;
import com.paintingscollectors.model.entity.Style;
import com.paintingscollectors.model.entity.StyleEnum;
import com.paintingscollectors.model.entity.User;
import com.paintingscollectors.repository.PaintingRepository;
import com.paintingscollectors.repository.StyleRepository;
import com.paintingscollectors.repository.UserRepository;
import com.paintingscollectors.service.CurrentUser;
import com.paintingscollectors.service.PaintingService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PaintingServiceImpl implements PaintingService {
    private final PaintingRepository paintingRepository;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final StyleRepository styleRepository;
    private final CurrentUser currentUser;

    public PaintingServiceImpl(PaintingRepository paintingRepository, UserRepository userRepository, ModelMapper modelMapper, StyleRepository styleRepository, CurrentUser currentUser) {
        this.paintingRepository = paintingRepository;
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
        this.styleRepository = styleRepository;
        this.currentUser = currentUser;
    }

    @Override
    public List<Painting> getAllPaintingsExcept(long id) {
        Optional<User> optionalUser = userRepository.findById(id);
        if(optionalUser.isEmpty()){
            return new ArrayList<>();
        }
        return paintingRepository.findAllPaintingsExcept(id);
    }

    @Override
    public List<Painting> getMostRated() {
        return paintingRepository.findMostRated().stream().limit(2).collect(Collectors.toList());
    }

    @Override
    public boolean add(AddPaintingDto addPaintingDto) {
        Painting painting = modelMapper.map(addPaintingDto, Painting.class);
        Optional<Style> optionalStyle = styleRepository.findByName(StyleEnum.valueOf(addPaintingDto.getStyle()));
        if(optionalStyle.isEmpty()){
            return false;
        }
        Style style = optionalStyle.get();
        painting.setStyle(style);

        Optional<User> optionalUser = userRepository.findById(currentUser.getId());
        if(optionalUser.isEmpty()){
            return false;
        }
        User owner = optionalUser.get();
        painting.setOwner(owner);

        paintingRepository.save(painting);
        return true;
    }

    @Override
    public void delete(long id) {
        Optional<Painting> optionalPainting = paintingRepository.findById(id);
        if(optionalPainting.isEmpty()){
            return;
        }
        Painting painting = optionalPainting.get();
        if(painting.isFavourite()){
            return;
        }
        paintingRepository.delete(painting);
    }

    @Override
    @Transactional
    public void addToFavourite(long id) {
        Optional<Painting> optionalPainting = paintingRepository.findById(id);
        if (optionalPainting.isEmpty()) {
            return;
        }
        Painting painting = optionalPainting.get();
        Optional<User> optionalUser = userRepository.findById(currentUser.getId());
        if (optionalUser.isEmpty()) {
            return;
        }
        User user = optionalUser.get();
        if(user.getFavouritePaintings().contains(painting)){
            return;
        }

        painting.setFavourite(true);
        user.getFavouritePaintings().add(painting);
        userRepository.save(user);
    }

    @Override
    @Transactional
    public void vote(long id) {
        Optional<Painting> optionalPainting = paintingRepository.findById(id);
        if (optionalPainting.isEmpty()) {
            return;
        }
        Painting painting = optionalPainting.get();
        Optional<User> optionalUser = userRepository.findById(currentUser.getId());
        if (optionalUser.isEmpty()) {
            return;
        }
        User user = optionalUser.get();
        if(user.getRatedPaintings().contains(painting)){
            return;
        }
        user.getRatedPaintings().add(painting);
        painting.setVotes(painting.getVotes() + 1);
    }

    @Override
    @Transactional
    public void removeFromFavourites(long id) {
        Optional<Painting> optionalPainting = paintingRepository.findById(id);
        if (optionalPainting.isEmpty()) {
            return;
        }
        Painting painting = optionalPainting.get();
        Optional<User> optionalUser = userRepository.findById(currentUser.getId());
        if (optionalUser.isEmpty()) {
            return;
        }
        User user = optionalUser.get();
        if(!user.getFavouritePaintings().contains(painting)){
            return;
        }
        user.getFavouritePaintings().remove(painting);
        painting.setFavourite(false);
    }
}
