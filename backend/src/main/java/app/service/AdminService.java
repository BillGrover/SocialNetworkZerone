package app.service;

import app.dto.admin.*;
import app.dto.auth.AuthRequest;
import app.dto.main.MessageResponse;
import app.exceptions.*;
import app.model.Admin;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface AdminService {

    ResponseEntity<MessageResponse> setBlock(long id, String token) throws AppException;

    ResponseEntity<MessageResponse> setTotalBlock(long id, String token) throws AppException;

    ResponseEntity<MessageResponse> setUnBlock(long id, String token) throws AppException;

    ResponseEntity<MessageResponse> deletePerson(long id, String token) throws AppException;

    ResponseEntity<MessageResponse> restorePerson(long id, String token) throws AppException;

    ResponseEntity<AllStatisticsResponse> getAllStatistics(String token) throws UnAuthorizedException;

    ResponseEntity<List<PersonAdminPanelDto>> getPersonList(String token) throws UnAuthorizedException;

    ResponseEntity<List<AdminDto>> getAdminList(String token) throws UnAuthorizedException;

    ResponseEntity<List<ApplicantForAdmin>> getApplicantList(String token) throws UnAuthorizedException;

    ResponseEntity<MessageResponse> addAdmin(String role, long id, String token) throws AppException;

    ResponseEntity<MessageResponse> updateAdmin(long id, String role, String token) throws AppException;

    ResponseEntity<MessageResponse> removeAdmin(long id, String token) throws AppException;

    ResponseEntity<AdminLoginResponse> adminLogin(AuthRequest authRequest) throws AppException;

    ResponseEntity<AdminLoginResponse> adminLoginByToken(String token) throws AppException;

    Admin getAdminByEmail(String email) throws UnAuthorizedException;

    Admin checkAdminByToken(String token) throws UnAuthorizedException;

    Admin findAdminById(long id) throws BadRequestException;

}
