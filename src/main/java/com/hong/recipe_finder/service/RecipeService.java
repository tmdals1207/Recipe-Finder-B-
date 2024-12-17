package com.hong.recipe_finder.service;

import com.hong.recipe_finder.domain.CookingStep;
import com.hong.recipe_finder.domain.Recipe;
import com.hong.recipe_finder.dto.CookingStepDTO;
import com.hong.recipe_finder.repository.RecipeRepository;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class RecipeService {

    private final RecipeRepository recipeRepository; // Repository 계층 사용

    public String saveProfileImage(MultipartFile profileImage) throws IOException {
        // 프로필 이미지 디렉토리 확인 및 생성
        String PROFILE_IMAGE_DIR = "C:/uploads/profileImages/";
        Path uploadPath = Paths.get(PROFILE_IMAGE_DIR);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath); // 디렉토리 생성
        }

        // 파일명 처리
        String originalFilename = profileImage.getOriginalFilename();
        if (originalFilename == null || originalFilename.isBlank()) {
            originalFilename = "default-profile.png"; // 기본 파일명 설정
        }
        String sanitizedFilename = StringUtils.cleanPath(originalFilename);
        String fileName = UUID.randomUUID() + "_" + sanitizedFilename; // 파일명에 UUID 추가

        // 파일 저장
        Path filePath = uploadPath.resolve(fileName);
        profileImage.transferTo(filePath.toFile());

        return PROFILE_IMAGE_DIR + fileName; // 저장된 파일 경로 반환
    }

    public void saveCookingStepImages(MultipartFile[] cookingStepImages, Recipe recipe) throws IOException {
        String COOKING_STEP_IMAGE_DIR = "C:/uploads/cookingStepImages/";
        Path uploadPath = Paths.get(COOKING_STEP_IMAGE_DIR);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath); // 디렉토리 생성
        }

        // 이미지 저장 및 조리 순서에 경로 설정
        for (int i = 0; i < cookingStepImages.length; i++) {
            MultipartFile image = cookingStepImages[i];
            if (!image.isEmpty()) {
                String originalFilename = image.getOriginalFilename();
                if (originalFilename == null || originalFilename.isBlank()) {
                    originalFilename = "default-step.png"; // 기본 파일명 설정
                }
                String sanitizedFilename = StringUtils.cleanPath(originalFilename);
                String fileName = UUID.randomUUID() + "_" + sanitizedFilename;
                Path filePath = uploadPath.resolve(fileName);
                image.transferTo(filePath.toFile());

                // 조리 순서에 이미지 URL 추가
                recipe.getCookingSteps().get(i).setImageUrl(COOKING_STEP_IMAGE_DIR + fileName);
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

    public Optional<Recipe> getRecipeById(Long id) {
        return recipeRepository.findById(id);
    }

    public Optional<Recipe> deleteRecipe(Long id) {
        return recipeRepository.findById(id)
                .map(recipe -> {
                    recipeRepository.delete(recipe);
                    return recipe;
                });
    }
}
