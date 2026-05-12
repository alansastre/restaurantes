package com.restaurantes.controller;


import com.restaurantes.model.Dish;
import com.restaurantes.model.Review;
import com.restaurantes.repository.DishRepository;
import com.restaurantes.repository.RestaurantRepository;
import com.restaurantes.repository.ReviewRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@AllArgsConstructor
public class ReviewController {

    private final ReviewRepository reviewRepository;
    private final RestaurantRepository restaurantRepository;
    private final DishRepository dishRepository;

    @GetMapping("reviews") // CONTROLADOR
    public String reviews(Model model) {
        model.addAttribute("reviews", reviewRepository.findAll());
        return "reviews/review-list"; // VISTA
    }

    @GetMapping("reviews/{id}")
    public String reviewDetail(Model model, @PathVariable Long id) {
        model.addAttribute("review", reviewRepository.findById(id).orElseThrow());
        return "reviews/review-detail";
    }

    @GetMapping("reviews/delete/{id}")
    public String delete(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        reviewRepository.deleteById(id);
        redirectAttributes.addFlashAttribute("message", "Borrado exitosamente");
        return "redirect:/reviews";
    }

    // /reviews/new?restaurantId=1
    // /reviews/new?dishId=1

    @GetMapping("reviews/new")
    public String newReview(
            Model model,
            @RequestParam(required = false) Long restaurantId,
            @RequestParam(required = false) Long dishId
    ) {
        Review review = new Review();

        if(restaurantId != null)
            review.setRestaurant(restaurantRepository.findById(restaurantId).orElseThrow());
        if (dishId != null)
            review.setDish(dishRepository.findById(dishId).orElseThrow());

        model.addAttribute("review", review);
        return "reviews/review-form";
    }

    @PostMapping("reviews")
    public String save(@ModelAttribute Review review) {
        // Analizar - Filtro de moderacion para impedir reviews con palabras prohibidas
        // findById
        reviewRepository.save(review);
        if (review.getRestaurant() != null)
            return "redirect:/restaurants/" + review.getRestaurant().getId();

        if (review.getDish() != null)
            return "redirect:/dishes/" + review.getDish().getId();

        return "redirect:/reviews";
    }
}
