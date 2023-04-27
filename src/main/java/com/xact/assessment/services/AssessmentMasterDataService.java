/*
 * Copyright (c) 2022 - Thoughtworks Inc. All rights reserved.
 */

package com.xact.assessment.services;

import com.xact.assessment.dtos.*;
import com.xact.assessment.exceptions.DuplicateRecordException;
import com.xact.assessment.mappers.MasterDataMapper;
import com.xact.assessment.models.*;
import jakarta.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

@Singleton
public class AssessmentMasterDataService {

    private final CategoryService categoryService;
    private final UserAssessmentModuleService userAssessmentModuleService;
    private final ModuleService moduleService;
    private final MasterDataMapper masterDataMapper = new MasterDataMapper();
    private static final String DUPLICATE_RECORDS_ARE_NOT_ALLOWED = "Duplicate records are not allowed";
    private final AccessControlService accessControlService;
    private final ModuleContributorService moduleContributorService;


    public AssessmentMasterDataService(CategoryService categoryService, ModuleService moduleService, UserAssessmentModuleService userAssessmentModuleService, AccessControlService accessControlService, ModuleContributorService moduleContributorService) {
        this.categoryService = categoryService;
        this.moduleService = moduleService;
        this.userAssessmentModuleService = userAssessmentModuleService;
        this.accessControlService = accessControlService;
        this.moduleContributorService = moduleContributorService;
    }

    public List<AssessmentCategory> getAllCategories() {
        return categoryService.getAllCategories();
    }

    public AssessmentCategory getCategory(Integer categoryId) {
        return categoryService.getCategory(categoryId);
    }

    public List<AssessmentCategory> getUserAssessmentCategories(Integer assessmentId) {
        List<AssessmentCategory> categories = new ArrayList<>();
        Set<AssessmentCategory> categorySet = new HashSet<>();
        List<AssessmentModule> assessmentModules = userAssessmentModuleService.findModuleByAssessment(assessmentId);
        if (!assessmentModules.isEmpty()) {
            for (AssessmentModule assessmentModule : assessmentModules) {
                if (assessmentModule.getCategory().getIsActive()) {
                    AssessmentCategory category = assessmentModule.getCategory();
                    assessmentModule.setTopics(assessmentModule.getActiveTopics());
                    for (AssessmentTopic assessmentTopic : assessmentModule.getTopics()) {
                        assessmentTopic.setParameters(assessmentTopic.getActiveParameters());
                        for (AssessmentParameter assessmentParameter : assessmentTopic.getParameters()) {
                            Set<Question> questionList = new HashSet<>();
                            for (Question question : assessmentParameter.getQuestions()) {
                                if (question.getQuestionStatus() == ContributorQuestionStatus.PUBLISHED) {
                                    questionList.add(question);
                                }
                            }
                            assessmentParameter.setQuestions(questionList);
                        }
                    }
                    categorySet.add(category);
                }

            }
            for (AssessmentCategory category : categorySet) {
                Set<AssessmentModule> assessmentModuleSet = new HashSet<>();
                category.getModules().forEach(module -> {
                    if (assessmentModules.contains(module)) {
                        assessmentModuleSet.add(module);
                    }
                });
                category.setModules(assessmentModuleSet);
                categories.add(category);
            }
        }
        return categories;
    }

    public AssessmentCategory createAssessmentCategory(AssessmentCategoryRequest assessmentCategoryRequest) {
        if (isCategoryUnique(assessmentCategoryRequest.getCategoryName())) {
            AssessmentCategory assessmentCategory = new AssessmentCategory(assessmentCategoryRequest.getCategoryName(), assessmentCategoryRequest.isActive(), assessmentCategoryRequest.getComments());
            categoryService.save(assessmentCategory);
            return assessmentCategory;
        } else {
            throw new DuplicateRecordException(DUPLICATE_RECORDS_ARE_NOT_ALLOWED);
        }

    }

    private boolean isCategoryUnique(String categoryName) {
        List<String> categories = categoryService.getCategoryNames();
        return isUnique(categories, categoryName);
    }


    public AssessmentModule createAssessmentModule(AssessmentModuleRequest assessmentModuleRequest) {
        AssessmentCategory assessmentCategory = categoryService.findCategoryById(assessmentModuleRequest);
        if (isModuleUnique(assessmentModuleRequest.getModuleName(), assessmentCategory)) {
            AssessmentModule assessmentModule = new AssessmentModule(assessmentModuleRequest.getModuleName(), assessmentCategory, assessmentModuleRequest.isActive(), assessmentModuleRequest.getComments());
            moduleService.createModule(assessmentModule);
            return assessmentModule;
        } else {
            throw new DuplicateRecordException(DUPLICATE_RECORDS_ARE_NOT_ALLOWED);
        }
    }


    public AssessmentCategory updateCategory(AssessmentCategory assessmentCategory, AssessmentCategoryRequest assessmentCategoryRequest) {
        if (isUnique(assessmentCategory.getCategoryName(), assessmentCategoryRequest.getCategoryName(), isCategoryUnique(assessmentCategoryRequest.getCategoryName()))) {
            assessmentCategory.setCategoryName(assessmentCategoryRequest.getCategoryName());
            assessmentCategory.setActive(assessmentCategoryRequest.isActive());
            assessmentCategory.setComments(assessmentCategoryRequest.getComments());
            categoryService.update(assessmentCategory);
            return assessmentCategory;
        } else
            throw new DuplicateRecordException(DUPLICATE_RECORDS_ARE_NOT_ALLOWED);
    }

    private boolean isModuleUnique(String moduleName, AssessmentCategory assessmentCategory) {
        List<String> modules = assessmentCategory.getModules().stream().map(AssessmentModule::getModuleName).toList();
        return isUnique(modules, moduleName);
    }

    private boolean isUnique(List<String> actualList, String name) {
        List<String> result = actualList.stream()
                .map(String::toLowerCase).map(option -> option.replaceAll("\\s", ""))
                .toList();
        return !(result.contains(name.toLowerCase().replaceAll("\\s", "")));
    }

    public AssessmentModule updateModule(Integer moduleId, AssessmentModuleRequest assessmentModuleRequest) {
        AssessmentModule assessmentModule = moduleService.getModule(moduleId);
        AssessmentCategory assessmentCategory = categoryService.findCategoryById(assessmentModuleRequest);
        if (isUnique(assessmentModule.getModuleName(), assessmentModuleRequest.getModuleName(), isModuleUnique(assessmentModuleRequest.getModuleName(), assessmentCategory))) {
            assessmentModule.setModuleName(assessmentModuleRequest.getModuleName());
            assessmentModule.setCategory(assessmentCategory);
            assessmentModule.setActive(assessmentModuleRequest.isActive());
            assessmentModule.setComments(assessmentModuleRequest.getComments());
            moduleService.updateModule(assessmentModule);
            return assessmentModule;
        } else {
            throw new DuplicateRecordException(DUPLICATE_RECORDS_ARE_NOT_ALLOWED);
        }

    }

    private boolean isUnique(String parameterName, String parameterName2, boolean isOtherDataUnique) {
        return removeWhiteSpaces(parameterName).equals(removeWhiteSpaces(parameterName2)) || isOtherDataUnique;
    }

    private String removeWhiteSpaces(String name) {
        return name.toLowerCase().replaceAll("\\s", "");
    }

    public List<AssessmentCategory> getCategoriesSortedByUpdatedDate() {
        return categoryService.getCategoriesSortedByUpdatedDate();
    }


    public Integer getAssessedCategory(List<TopicLevelRating> topicLevelRatingList, List<ParameterLevelRating> parameterLevelRatingList) {
        Set<Integer> assessedCategories = new TreeSet<>();
        for (ParameterLevelRating parameterLevelRating : parameterLevelRatingList) {
            assessedCategories.add(parameterLevelRating.getParameterLevelId().getParameter().getTopic().getModule().getCategory().getCategoryId());
        }

        for (TopicLevelRating topicLevelRating : topicLevelRatingList) {
            assessedCategories.add(topicLevelRating.getTopicLevelId().getTopic().getModule().getCategory().getCategoryId());

        }
        return assessedCategories.size();
    }

    public boolean isModuleSelectedByUser(Assessment assessment, AssessmentModule assessmentModule) {
        AssessmentModuleId assessmentModuleId = new AssessmentModuleId(assessment, assessmentModule);
        return userAssessmentModuleService.existsById(assessmentModuleId);
    }


    public List<CategoryDto> getMasterDataByRole(User loggedInUser, String role) {
        Optional<AccessControlRoles> accessControlRoles = accessControlService.getAccessControlRolesByEmail(loggedInUser.getUserEmail());
        Set<ModuleContributor> contributorRoles = moduleContributorService.getContributorsByEmail(loggedInUser.getUserEmail());

        if (accessControlRoles.isPresent() && accessControlRoles.get().toString().equalsIgnoreCase(role))
            return getAdminMasterData(accessControlRoles);
        else
            return getContributorMasterData(contributorRoles);

    }

    private List<CategoryDto> getContributorMasterData(Set<ModuleContributor> contributorRoles) {
        List<CategoryDto> assessmentCategoriesResponse = new ArrayList<>();
        Map<AssessmentCategory, List<ModuleContributor>> contributorCategories = groupContributorCategoriesByModules(contributorRoles);

        for (Map.Entry<AssessmentCategory, List<ModuleContributor>> entry : contributorCategories.entrySet()) {
            CategoryDto categoryDto = getContributorCategories(entry.getKey(), contributorCategories.get(entry.getKey()));
            assessmentCategoriesResponse.add(categoryDto);

        }

        return assessmentCategoriesResponse;
    }

    private Map<AssessmentCategory, List<ModuleContributor>> groupContributorCategoriesByModules(Set<ModuleContributor> contributorRoles) {
        Map<AssessmentCategory, List<ModuleContributor>> contributorCategories = new HashMap<>();
        for (ModuleContributor contributor : contributorRoles) {
            AssessmentModule assessmentModule = contributor.getContributorId().getModule();
            AssessmentCategory assessmentCategory = assessmentModule.getCategory();
            if (contributorCategories.containsKey(assessmentCategory)) {
                contributorCategories.get(assessmentCategory).add(contributor);
            } else {
                List<ModuleContributor> moduleContributors = new ArrayList<>();
                moduleContributors.add(contributor);
                contributorCategories.put(assessmentCategory, moduleContributors);
            }
        }
        return contributorCategories;
    }

    private List<CategoryDto> getAdminMasterData(Optional<AccessControlRoles> accessControlRoles) {
        List<CategoryDto> assessmentCategoriesResponse = new ArrayList<>();
        List<AssessmentCategory> assessmentCategories = getCategoriesSortedByUpdatedDate();
        if (Objects.nonNull(assessmentCategories)) {
            for (AssessmentCategory assessmentCategory : assessmentCategories) {
                CategoryDto categoryDto = getAdminCategories(accessControlRoles, assessmentCategory);
                assessmentCategoriesResponse.add(categoryDto);
            }

        }
        return assessmentCategoriesResponse;
    }

    private CategoryDto getContributorCategories(AssessmentCategory assessmentCategory, List<ModuleContributor> moduleContributors) {
        CategoryDto categoryDto = masterDataMapper.mapCategory(assessmentCategory);
        SortedSet<AssessmentModuleDto> moduleDtos = new TreeSet<>();
        for (ModuleContributor moduleContributor : moduleContributors) {
            setModule(moduleContributor.getContributorId().getModule(), AccessControlRoles.valueOf(moduleContributor.getContributorRole().toString()), moduleDtos);
        }
        categoryDto.setModules(moduleDtos);
        return categoryDto;
    }

    private CategoryDto getAdminCategories(Optional<AccessControlRoles> accessControlRoles, AssessmentCategory assessmentCategory) {
        CategoryDto categoryDto = masterDataMapper.mapCategory(assessmentCategory);
        SortedSet<AssessmentModuleDto> modules = new TreeSet<>();
        for (AssessmentModule assessmentModule : assessmentCategory.getModules()) {
            accessControlRoles.ifPresent(controlRoles -> setModule(assessmentModule, controlRoles, modules));
        }
        categoryDto.setModules(modules);
        return categoryDto;
    }

    private void setModule(AssessmentModule assessmentModule, AccessControlRoles role, SortedSet<AssessmentModuleDto> moduleDtos) {
        AssessmentModuleDto assessmentModuleDto = masterDataMapper.mapModule(assessmentModule);
        SortedSet<AssessmentTopicDto> topicDtos = getTopics(role, assessmentModule);
        assessmentModuleDto.setTopics(topicDtos);
        moduleDtos.add(assessmentModuleDto);
    }

    private SortedSet<AssessmentTopicDto> getTopics(AccessControlRoles contributor, AssessmentModule assessmentModule) {
        SortedSet<AssessmentTopicDto> topicDtos = new TreeSet<>();
        for (AssessmentTopic assessmentTopic : assessmentModule.getTopics()) {
            AssessmentTopicDto topicDto = masterDataMapper.mapTopic(assessmentTopic);
            SortedSet<AssessmentParameterDto> parameterDtos = getParameters(contributor, assessmentTopic);
            topicDto.setParameters(parameterDtos);
            topicDtos.add(topicDto);
        }
        return topicDtos;
    }

    private SortedSet<AssessmentParameterDto> getParameters(AccessControlRoles contributor, AssessmentTopic assessmentTopic) {
        SortedSet<AssessmentParameterDto> parameterDtos = new TreeSet<>();
        for (AssessmentParameter assessmentParameter : assessmentTopic.getParameters()) {
            AssessmentParameterDto parameterDto = masterDataMapper.mapParameter(assessmentParameter);
            SortedSet<QuestionDto> questions = getQuestions(contributor, assessmentParameter);
            parameterDto.setQuestions(questions);
            parameterDtos.add(parameterDto);
        }
        return parameterDtos;
    }

    private SortedSet<QuestionDto> getQuestions(AccessControlRoles role, AssessmentParameter assessmentParameter) {
        SortedSet<QuestionDto> questions = new TreeSet<>();
        for (Question question : assessmentParameter.getQuestions()) {
            addQuestionByRole(role, questions, question);
        }
        return questions;
    }

    private void addQuestionByRole(AccessControlRoles role, SortedSet<QuestionDto> questions, Question question) {
        QuestionDto questionDto = masterDataMapper.mapQuestion(question);
        if (role == AccessControlRoles.AUTHOR ||
                (role == AccessControlRoles.REVIEWER && (question.getQuestionStatus() != ContributorQuestionStatus.DRAFT)) ||
                (role == AccessControlRoles.Admin && question.getQuestionStatus() == ContributorQuestionStatus.PUBLISHED))
            questions.add(questionDto);
    }
}

