package com.paintingscollectors.controller;

import com.paintingscollectors.model.entity.Painting;
import com.paintingscollectors.service.CurrentUser;
import com.paintingscollectors.service.PaintingService;
import com.paintingscollectors.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;
import java.util.Set;


@Controller
public class HomeController {
    private final CurrentUser currentUser;
    private final UserService userService;
    private final PaintingService paintingService;

    public HomeController(CurrentUser currentUser, UserService userService, PaintingService paintingService) {
        this.currentUser = currentUser;
        this.userService = userService;
        this.paintingService = paintingService;
    }

    @GetMapping("/")
    public String notLoggedIndex(){
        if(currentUser.isLoggedIn()){
            return "redirect:/home";
        }
        return "index";
    }
    @GetMapping("/home")
    public String loggedIndex(Model model){
        if(!currentUser.isLoggedIn()){
            return "redirect:/";
        }
        List<Painting> currentUserPaintings = userService.getCurrentUserPaintings(currentUser.getId());
        model.addAttribute("myPaintings",currentUserPaintings);

        Set<Painting> favouritesPaintings = userService.getFavouritesPainting(currentUser.getId());
        model.addAttribute("favourites",favouritesPaintings);

        List<Painting> restPaintings = paintingService.getAllPaintingsExcept(currentUser.getId());
        model.addAttribute("others",restPaintings);

        List<Painting> mostRatedPaintings = paintingService.getMostRated();
        model.addAttribute("mostRated",mostRatedPaintings);

        return "home";
    }
}
