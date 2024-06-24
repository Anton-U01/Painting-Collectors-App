package com.paintingscollectors.service.impl;

import com.paintingscollectors.model.dto.UserLoginDto;
import com.paintingscollectors.model.dto.UserRegisterDto;
import com.paintingscollectors.model.entity.Painting;
import com.paintingscollectors.model.entity.User;
import com.paintingscollectors.repository.PaintingRepository;
import com.paintingscollectors.repository.UserRepository;
import com.paintingscollectors.service.CurrentUser;
import com.paintingscollectors.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;
    private final CurrentUser currentUser;
    private final PaintingRepository paintingRepository;

    public UserServiceImpl(UserRepository userRepository, ModelMapper modelMapper, PasswordEncoder passwordEncoder, CurrentUser currentUser, PaintingRepository paintingRepository) {
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
        this.passwordEncoder = passwordEncoder;
        this.currentUser = currentUser;
        this.paintingRepository = paintingRepository;
    }

    @Override
    public boolean register(UserRegisterDto userRegisterDto) {
        if(!userRegisterDto.getPassword().equals(userRegisterDto.getConfirmPassword())){
            return false;
        }
        Optional<User> optionalUser = userRepository.findByUsernameAndEmail(userRegisterDto.getUsername(), userRegisterDto.getEmail());
        if(optionalUser.isPresent()){
            return false;
        }
        User user = modelMapper.map(userRegisterDto, User.class);
        user.setPassword(passwordEncoder.encode(userRegisterDto.getPassword()));
        userRepository.save(user);

        return true;
    }

    @Override
    public boolean login(UserLoginDto userLoginDto) {
        Optional<User> optionalUser = userRepository.findByUsername(userLoginDto.getUsername());
        if(optionalUser.isEmpty()){
            return false;
        }
        User user = optionalUser.get();
        if(!passwordEncoder.matches(userLoginDto.getPassword(),user.getPassword())){
            return false;
        }
        currentUser.login(user);

        return true;
    }

    @Override
    public void logout() {
        currentUser.logout();
    }

    @Override
    public List<Painting> getCurrentUserPaintings(long id) {
        Optional<User> optionalUser = userRepository.findById(id);
        if(optionalUser.isEmpty()){
            return new ArrayList<>();
        }
        return paintingRepository.findAllByOwner(optionalUser.get());
    }

    @Override
    public Set<Painting> getFavouritesPainting(long id) {
        Optional<User> optionalUser = userRepository.findById(id);
        if(optionalUser.isEmpty()){
            return new HashSet<>();
        }
        return paintingRepository.findAllFavouritesPaintingsByUser(optionalUser.get().getId());
    }
}
