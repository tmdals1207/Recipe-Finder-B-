package com.hong.recipe_finder.service;

import com.hong.recipe_finder.domain.CookingStep;
import com.hong.recipe_finder.domain.Recipe;
import com.hong.recipe_finder.dto.CookingStepDTO;
import com.hong.recipe_finder.repository.RecipeRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class RecipeService {

    private final RecipeRepository recipeRepository; // Repository 계층 사용
    private final String PROFILE_IMAGE_DIR = "/path/to/profile/images/";
    private final String COOKING_STEP_IMAGE_DIR = "/path/to/cooking/steps/";

    public String saveProfileImage(MultipartFile file) throws IOException {
        String fileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
        Path path = Paths.get(PROFILE_IMAGE_DIR + fileName);
        Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
        return "/images/profile/" + fileName; // 접근 가능한 URL 반환
    }

    public String saveCookingStepImage(MultipartFile file) throws IOException {
        String fileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
        Path path = Paths.get(COOKING_STEP_IMAGE_DIR + fileName);
        Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
        return "/images/cookingSteps/" + fileName; // 접근 가능한 URL 반환
    }
    public void saveCookingStepImages(MultipartFile[] files, Recipe recipe) throws IOException {
        for (MultipartFile file : files) {
            if (!file.isEmpty()) {
                String imageUrl = saveCookingStepImage(file); // 각 이미지를 저장
                // 여기서 CookingStep 객체를 생성하여 Recipe에 추가할 수 있습니다.
                CookingStep cookingStep = new CookingStep();
                cookingStep.setImageUrl(imageUrl);
                cookingStep.setRecipe(recipe);
                recipe.getCookingSteps().add(cookingStep); // 조리 단계에 추가
            }
        }
    }

    public List<CookingStep> convertToCookingSteps(List<CookingStepDTO> dtos, Recipe recipe) {
        List<CookingStep> steps = new ArrayList<>();
        for (CookingStepDTO dto : dtos) {
            CookingStep step = new CookingStep();
            step.setDescription(dto.getDescription());
            step.setStepNumber(dto.getStepNumber());
            step.setImageUrl(dto.getImageUrl());
            step.setRecipe(recipe);
            steps.add(step);
        }
        return steps;
    }

    public RecipeService(RecipeRepository recipeRepository) {
        this.recipeRepository = recipeRepository;
    }

    public List<Recipe> getAllRecipes() {
        return recipeRepository.findAll();

    }

    public Recipe createRecipe(Recipe recipe) {
        return recipeRepository.save(recipe);
    }

    public void saveRecipe(Recipe recipe) {
        recipeRepository.save(recipe);
    }

    public Optional<Recipe> getRecipeById(Long id) {
        return recipeRepository.findById(id);
    }

//    public Optional<Recipe> updateRecipe(Long id, Recipe recipeDetails) {
//        return recipeRepository.findById(id)
//                .map(recipe -> {
//                    recipe.setName(recipeDetails.getName());
//                    recipe.setIngredients(recipeDetails.getIngredients());
//                    recipe.setInstructions(recipeDetails.getInstructions());
//                    return recipeRepository.save(recipe);
//                });
//    }

    public Optional<Recipe> deleteRecipe(Long id) {
        return recipeRepository.findById(id)
                .map(recipe -> {
                    recipeRepository.delete(recipe);
                    return recipe;
                });
    }
}
