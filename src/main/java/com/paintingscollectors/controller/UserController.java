package com.paintingscollectors.controller;

import com.paintingscollectors.model.dto.UserLoginDto;
import com.paintingscollectors.model.dto.UserRegisterDto;
import com.paintingscollectors.service.CurrentUser;
import com.paintingscollectors.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;

@Controller
public class UserController {
    private final UserService userService;
    private final CurrentUser currentUser;

    public UserController(UserService userService, CurrentUser currentUser) {
        this.userService = userService;
        this.currentUser = currentUser;
    }

    @ModelAttribute("userRegister")
    public UserRegisterDto createUserRegisterDto(){
        return new UserRegisterDto();
    }
    @GetMapping("/register")
    public String viewRegister(){
        if(currentUser.isLoggedIn()){
            return "redirect:/home";
        }
        return "register";
    }
    @PostMapping("/register")
    public String doRegister(@Valid UserRegisterDto userRegisterDto,
                             BindingResult bindingResult,
                             RedirectAttributes redirectAttributes){
        if(currentUser.isLoggedIn()){
            return "redirect:/home";
        }
        if(bindingResult.hasErrors() || !userService.register(userRegisterDto)){
            redirectAttributes.addFlashAttribute("userRegister",userRegisterDto);
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.userRegister",bindingResult);

            return "redirect:/register";
        }
        return "redirect:/login";
    }

    @ModelAttribute("userLogin")
    public UserLoginDto createUserLoginDto(){
        return new UserLoginDto();
    }
    @GetMapping("/login")
    public String viewLogin(){
        if(currentUser.isLoggedIn()){
            return "redirect:/home";
        }
        return "login";
    }
    @PostMapping("/login")
    public String doLogin(@Valid UserLoginDto userLoginDto,
                          BindingResult bindingResult,
                          RedirectAttributes redirectAttributes){
        if(currentUser.isLoggedIn()){
            return "redirect:/home";
        }
        if(bindingResult.hasErrors()){
            redirectAttributes.addFlashAttribute("userLogin",userLoginDto);
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.userLogin",bindingResult);

            return "redirect:/login";
        }
        boolean success = userService.login(userLoginDto);
        if(!success){
            redirectAttributes.addFlashAttribute("userLogin",userLoginDto);
            redirectAttributes.addFlashAttribute("incorrectInput",true);
            return "redirect:/login";
        }

        return "redirect:/home";
    }
    @PostMapping("/logout")
    public String logout(){
        if(!currentUser.isLoggedIn()){
            return "redirect:/";
        }
        userService.logout();
        return "redirect:/";
    }
}
