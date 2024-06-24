package com.paintingscollectors.controller;

import com.paintingscollectors.model.dto.AddPaintingDto;
import com.paintingscollectors.model.entity.StyleEnum;
import com.paintingscollectors.service.CurrentUser;
import com.paintingscollectors.service.PaintingService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;

@Controller
public class PaintingController {
    private final PaintingService paintingService;
    private final CurrentUser currentUser;

    public PaintingController(PaintingService paintingService, CurrentUser currentUser) {
        this.paintingService = paintingService;
        this.currentUser = currentUser;
    }

    @ModelAttribute("addPainting")
    public AddPaintingDto createDto(){
        return new AddPaintingDto();
    }
    @GetMapping("/add-painting")
    public String viewAddPainting(Model model){
        model.addAttribute("styles", StyleEnum.values());
        return "add-painting";
    }
    @PostMapping("/add-painting")
    public String addPainting(@Valid AddPaintingDto addPaintingDto,
                              BindingResult bindingResult,
                              RedirectAttributes redirectAttributes){
        if(!currentUser.isLoggedIn()){
            return "redirect:/";
        }
        if(bindingResult.hasErrors() || !paintingService.add(addPaintingDto)){
            redirectAttributes.addFlashAttribute("addPainting",addPaintingDto);
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.addPainting",bindingResult);

            return "redirect:/add-painting";
        }

        return "redirect:/home";
    }

    @DeleteMapping("/paintings/remove/{id}")
    public String remove(@PathVariable("id") long id){
        paintingService.delete(id);
        return "redirect:/home";
    }
    @GetMapping("/paintings/add-to-favourites/{id}")
    public String addToFavourites(@PathVariable("id") long id){
        paintingService.addToFavourite(id);
        return "redirect:/home";
    }
    @GetMapping("/paintings/vote/{id}")
    public String vote(@PathVariable("id") long id){
        paintingService.vote(id);
        return "redirect:/home";
    }
    @GetMapping("/paintings/remove-favorites/{id}")
    public String removeFromFavourites(@PathVariable("id") long id){
        paintingService.removeFromFavourites(id);
        return "redirect:/home";
    }
}
