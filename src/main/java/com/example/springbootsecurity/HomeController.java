package com.example.springbootsecurity;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;


@Controller
public class HomeController {
    @Autowired
    private UserService userService;

    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    MessageRepository messageRepository;

    @GetMapping("/register")
    public String showRegistrationPage(Model model) {
        model.addAttribute("user", new User());
        return "registration";
    }

    @PostMapping("/register")
    public String processRegistrationPage(
            @Valid @ModelAttribute("user") User user,
            BindingResult result,
            Model model) {
        model.addAttribute("user", user);
        if (result.hasErrors()) {
            return "registration";
        } else {
            userService.saveUser(user);
            model.addAttribute("message",
                    "User Account Successfully Created");
        }
        return "index";
    }

    @RequestMapping("/")
    public String index(Model model) {
        model.addAttribute("messages", messageRepository.findAll());
        return "index";
    }

    @RequestMapping("/login")
    public String login() {
        return "login";
    }

    @RequestMapping("/securelist")
    public String secure(Model model) {
        model.addAttribute("user", userService.getCurrentUser());
        String username = userService.getCurrentUser().getUsername();
        model.addAttribute("messages", messageRepository.findByUsername(username));
        return "list";
    }

    @GetMapping("/add")
    public String addMessage(Model model){
        model.addAttribute("message", new Message());
        model.addAttribute("user", userService.getCurrentUser());
        return "form";
    }

    @PostMapping("/process")
    public String processMessage(@Valid Message message, BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "form";
        }
        String username = userService.getCurrentUser().getUsername();
        message.setUsername(username);
        messageRepository.save(message);
        model.addAttribute("messages", messageRepository.findByUsername(username));
        return "list";
    }

    @RequestMapping("/list")
    public String listMessages(Model model){
        String username = userService.getCurrentUser().getUsername();
        model.addAttribute("messages", messageRepository.findByUsername(username));
        return "list";
    }

//    @RequestMapping("/timeline")
//    public String bullMessages(Model model){
//        model.addAttribute("messages", messageRepository.findAll());
//        return "timeline";
//    }


    @RequestMapping("/update/{id}")
    public String showUpdate(@PathVariable("id") long id, Model model){
        model.addAttribute("message", messageRepository.findById(id).get());
        return "form";
    }

    @RequestMapping("/delete/{id}")
    public String delMessage(@PathVariable("id") long id){
        messageRepository.deleteById(id);
        return "redirect:/list";
    }


}
