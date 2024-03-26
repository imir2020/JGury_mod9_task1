package by.javagur.spring.http.controller;

import by.javagur.spring.database.entity.Role;
import by.javagur.spring.dto.*;
import by.javagur.spring.service.CompanyService;
import by.javagur.spring.service.UserImageService;
import by.javagur.spring.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Slf4j
@Controller
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final CompanyService companyService;
    private final UserImageService userImageService;

    @GetMapping
    public String findAll(Model model, UserFilter filter, Pageable pageable) {
        Page<UserToDto> page = userService.findAll(filter, pageable);
        model.addAttribute("users", PageResponse.of(page));
        model.addAttribute("filter", filter);
        return "user/users";
    }

    @GetMapping("/registration")
    public String registration(Model model, @ModelAttribute("user") DtoToUser user) {
        model.addAttribute("user", user);
        model.addAttribute("roles", Role.values());
        model.addAttribute("companies", companyService.findAll());
        return "user/registration";
    }

    @GetMapping("/{id}")
    public String findById(@PathVariable("id") Long id, Model model) {
        return userService.findById(id)
                .map(user -> {
                    model.addAttribute("user", user);
                    model.addAttribute("roles", Role.values());
                    model.addAttribute("companies", companyService.findAll());
                    return "user/user";
                })
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @GetMapping("/{userId}/userImages")
    public String findUserImagesByUserId(@PathVariable("userId") Long id,
                                         Model model) {
        model.addAttribute("user", userService.findById(id));
        model.addAttribute("imageList", userImageService.findAllByUserId(id));

        return "/user/userImage";
    }


    @PostMapping
    public String create(@ModelAttribute @Validated DtoToUser user,
                         BindingResult bindingResult,
                         RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("user", user);
            redirectAttributes.addFlashAttribute("errors", bindingResult.getAllErrors());
            return "redirect:/users/registration";
        }
        UserToDto dto = userService.create(user);
        return "redirect:/users/" + dto.getId();
    }

    @PostMapping("/{id}/update")
    public String update(@PathVariable("id") Long id, @ModelAttribute @Validated DtoToUser user) {
        return userService.update(id, user)
                .map(it -> "redirect:/users/{id}")
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @PostMapping("/{userId}/userImages/addImage")
    public String addImage(@PathVariable("userId") Long userId,
                           @Validated @ModelAttribute DtoToUserImage dtoToUserImage) {
        if (!dtoToUserImage.getImage().isEmpty()) {
            userService.addUserImage(userId, dtoToUserImage.getImage());
        }
        return "redirect:/users/" + userId + "/userImages";
    }

    @PostMapping("/{userId}/userImages/{imageId}/removeImage")
    public String removeImage(@PathVariable("userId") Long userId,
                              @PathVariable("imageId") Long imageId,
                              @ModelAttribute DtoToUserImage imageCreateEditDto) {
        userService.removeUserImage(userId, imageId);
        return "redirect:/users/" + userId + "/userImages";
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable("id") Long id) {
        if (!userService.delete(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        return "redirect:/users";
    }

}
