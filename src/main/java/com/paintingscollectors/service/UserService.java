package com.paintingscollectors.service;

import com.paintingscollectors.model.dto.UserLoginDto;
import com.paintingscollectors.model.dto.UserRegisterDto;
import com.paintingscollectors.model.entity.Painting;

import java.util.List;
import java.util.Set;

public interface UserService {
    boolean register(UserRegisterDto userRegisterDto);

    boolean login(UserLoginDto userLoginDto);

    void logout();

    List<Painting> getCurrentUserPaintings(long id);

    Set<Painting> getFavouritesPainting(long id);
}
