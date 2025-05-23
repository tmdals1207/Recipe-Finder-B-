package com.hong.recipe_finder.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hong.recipe_finder.domain.CookingStep;
import com.hong.recipe_finder.domain.Ingredient;
import com.hong.recipe_finder.domain.Recipe;
import com.hong.recipe_finder.domain.User;
import com.hong.recipe_finder.dto.RecipeWithDetails;
import com.hong.recipe_finder.repository.UserRepository;
import com.hong.recipe_finder.security.CustomUserDetails;
import com.hong.recipe_finder.service.RecipeService;
import com.hong.recipe_finder.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Slf4j
@RestController
@CrossOrigin(origins = "http://localhost:3000") // 프론트엔드 포트
@RequestMapping("/api/recipes")
public class RecipeController {

    private final RecipeService recipeService; // Service 계층 사용
    private final UserService userService;
    private final UserRepository userRepository;

    public RecipeController(RecipeService recipeService, UserService userService, UserRepository userRepository) {
        this.recipeService = recipeService;
        this.userService = userService;
        this.userRepository = userRepository;
    }

    @GetMapping("/all")
    public ResponseEntity<List<Recipe>> getAllRecipes() {
        log.info("레시피 모두 가져오기 요청 수신됨");  // 요청 수신 시 로그 출력
        List<Recipe> recipes = recipeService.getAllRecipes();
        if (recipes.isEmpty()) {
            log.warn("레시피 목록이 비어 있습니다.");
        }
        return ResponseEntity.ok(recipes);
    }

    @PostMapping("/create")
    public ResponseEntity<Recipe> createRecipe(
            @RequestParam("title") String title,
            @RequestParam("summation") String summation,
            @RequestParam("content") String content,
            @RequestParam("servings") int servings,
            @RequestParam("cookingTime") int cookingTime,
            @RequestParam("difficulty") String difficulty,
            @RequestParam("ingredients") String ingredientsJson,
            @RequestParam("seasonings") String seasoningsJson,
            @RequestParam("tags") String tagsJson,
            @RequestParam("cookingSteps") String cookingStepsJson,
            @RequestParam("profileImage") MultipartFile profileImage, // 프로필 이미지 파일
            @RequestParam(value = "cookingStepImages", required = false) MultipartFile[] cookingStepImages // 조리 단계 이미지들
    ) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        log.info("레시피 만들기 요청 수신됨");
        String email = "";
        String userName = "";
        // 작성자 프로필 설정
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication.getPrincipal() instanceof CustomUserDetails userDetails) {
            userName = userDetails.getName();
            email = userDetails.getEmail();
            log.info("Extracted user details: username={}, email={}", userName, email);
        } else {
            log.warn("Principal is not of type CustomUserDetails");
        }
        User currentUser = userRepository.findByEmailAndUsername(email, userName);
        String currentUserName = userService.getUsername(currentUser);


        // JSON 문자열을 실제 리스트로 변환 (Jackson 또는 Gson 등을 사용할 수 있음)
        List<Ingredient> ingredients = Arrays.asList(mapper.readValue(ingredientsJson, Ingredient[].class));
        List<Ingredient> seasonings = Arrays.asList(mapper.readValue(seasoningsJson, Ingredient[].class));
        List<String> tags = convertJsonToList(tagsJson);
        List<CookingStep> cookingSteps = convertJsonToCookingSteps(cookingStepsJson);

        // 서비스로 전달할 Recipe 객체 생성 및 설정
        Recipe recipe = new Recipe();
        recipe.setTitle(title);
        recipe.setSummation(summation);
        recipe.setContent(content);
        recipe.setServings(servings);
        recipe.setCookingTime(cookingTime);
        recipe.setDifficulty(difficulty);
        recipe.setIngredients(ingredients);
        recipe.setSeasonings(seasonings);
        recipe.setTags(tags);
        recipe.setCookingSteps(cookingSteps);
        recipe.setAuthorProfile(currentUserName);

        // 프로필 이미지 처리
        if (profileImage != null && !profileImage.isEmpty()) {
            String profileImageUrl = recipeService.saveProfileImage(profileImage); // 이미지 저장 서비스 로직
            recipe.setProfileImage(profileImageUrl);
        }

        // 조리 순서 이미지 처리 (필요 시)
        if (cookingStepImages != null && cookingStepImages.length > 0) {
            recipeService.saveCookingStepImages(cookingStepImages, recipe);
        }

        Recipe createdRecipe = recipeService.createRecipe(recipe);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdRecipe);
    }

    // JSON 문자열을 리스트로 변환하는 메서드 (Jackson 또는 Gson 사용)
    private List<String> convertJsonToList(String json) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.readValue(json, new TypeReference<List<String>>() {});
        } catch (IOException e) {
            throw new RuntimeException("JSON 변환 오류", e);
        }
    }

    private List<Ingredient> convertJsonToIngredients(String json) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.readValue(json, new TypeReference<List<Ingredient>>() {}); // Ingredient 타입으로 변환
        } catch (IOException e) {
            throw new RuntimeException("JSON 변환 오류", e);
        }
    }

    private List<CookingStep> convertJsonToCookingSteps(String json) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.readValue(json, new TypeReference<List<CookingStep>>() {});
        } catch (IOException e) {
            throw new RuntimeException("JSON 변환 오류", e);
        }
    }

    @GetMapping("/images/get")
    public ResponseEntity<Resource> getImage(@RequestParam("path") String imagePath) {
        try {
            Resource resource = recipeService.loadImage(imagePath);
            return ResponseEntity.ok()
                    .contentType(MediaType.IMAGE_JPEG)
                    .body(resource);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    //TODO 여기 수정.
    @GetMapping("/{id}")
    public ResponseEntity<RecipeWithDetails> getRecipeById(@PathVariable Long id) {
        try {
            RecipeWithDetails recipeWithDetails = recipeService.getRecipeWithDetails(id);
            return ResponseEntity.ok(recipeWithDetails);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
//    public ResponseEntity<Recipe> getRecipeById(@PathVariable Long id) {
//        return recipeService.getRecipeById(id)
//                .map(ResponseEntity::ok)
//                .orElse(ResponseEntity.notFound().build());
//    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteRecipe(@PathVariable Long id) {
        return recipeService.deleteRecipe(id)
                .map(recipe -> ResponseEntity.noContent().build())
                .orElse(ResponseEntity.notFound().build());
    }
}
