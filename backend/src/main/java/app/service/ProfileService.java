package app.service;

import app.dto.main.MessageResponse;
import app.dto.main.PaginationSettings;
import app.dto.post.*;
import app.dto.profile.*;
import app.exceptions.AppException;
import app.exceptions.BadRequestException;
import app.model.Person;
import org.springframework.http.ResponseEntity;


public interface ProfileService {

    ResponseEntity<PersonResponse> getPerson(String token) throws AppException;

    ResponseEntity<PersonResponse> getPersonById(long id) throws BadRequestException;

    String deletePerson(String token) throws AppException;

    void deletePerson(Person person);

    void restorePerson(Person person) throws BadRequestException;

    ResponseEntity<PersonResponse> updatePerson(String token, PersonUpdateRequest update) throws AppException;

    ResponseEntity<PostResponse> updatePost(long id, long publishDate, AddPostRequest request, String token)
            throws AppException;

    ResponseEntity<PostResponse> addPost(long id, long publishDate, AddPostRequest request, String token)
            throws AppException;

    ResponseEntity<PostListResponse> getPostList(long id, int offset, int itemPerPage, String token)
            throws AppException;

    ResponseEntity<PersonListResponse> getSearch(
            String firstNameLike,
            String lastNameLike,
            String cityName,
            String countryName,
            int ageFrom,
            int ageTo,
            PaginationSettings settings);

    ResponseEntity<MessageResponse> deletePost(long id) throws BadRequestException;
}

