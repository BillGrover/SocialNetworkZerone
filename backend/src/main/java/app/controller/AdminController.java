package app.controller;

import app.dto.admin.*;
import app.dto.auth.AuthRequest;
import app.dto.main.ErrorResponse;
import app.dto.main.MessageResponse;
import app.exceptions.AppException;
import app.exceptions.UnAuthorizedException;
import app.service.AdminService;
import io.swagger.annotations.*;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@Api(tags = "Функции администрирования")
@RequestMapping("/api/v1/admin")
public class AdminController {

    private final AdminService adminService;

    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    @PostMapping("/login")
    @ApiOperation(value = "Вход администратора", notes = "Вход в панель администратора")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Успешный вход", response = AdminLoginResponse.class),
            @ApiResponse(code = 400, message = "Bad Request", response = ErrorResponse.class),
            @ApiResponse(code = 401, message = "Unauthorized", response = ErrorResponse.class)})
    public ResponseEntity<AdminLoginResponse> adminLogin(@ApiParam(value = "RequestBody")
                                                         @RequestBody AuthRequest authRequest) throws AppException {
        return adminService.adminLogin(authRequest);
    }

    @GetMapping("/login")
    @ApiOperation(value = "Выход администратора", notes = "Выход из панели администратора")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Успешный выход", response = AdminLoginResponse.class),
            @ApiResponse(code = 400, message = "Bad Request", response = ErrorResponse.class),
            @ApiResponse(code = 401, message = "Unauthorized", response = ErrorResponse.class)})
    public ResponseEntity<AdminLoginResponse> loginByToken(@RequestHeader("Authorization") String token) throws AppException {
        return adminService.adminLoginByToken(token);
    }

    @PostMapping("/block/{id}")
    @ApiOperation(value = "Блокировка входа пользователя", notes = "Ограничение доспупа пользователя к социальной сети")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Успешное ограничение", response = MessageResponse.class),
            @ApiResponse(code = 400, message = "Bad Request", response = ErrorResponse.class),
            @ApiResponse(code = 401, message = "Unauthorized", response = ErrorResponse.class)})
    public ResponseEntity<MessageResponse> setBlock(@ApiParam(value = "ID пользователя", defaultValue = "1")
                                                    @PathVariable long id,
                                                    @RequestHeader("Authorization") String token) throws AppException {
        return adminService.setBlock(id, token);
    }

    @PostMapping("/total/{id}")
    @ApiOperation(value = "Полна блокировка пользователя", notes = "Блокировка доступа и контента пользователя")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Успешная блокировка", response = MessageResponse.class),
            @ApiResponse(code = 400, message = "Bad Request", response = ErrorResponse.class),
            @ApiResponse(code = 401, message = "Unauthorized", response = ErrorResponse.class)})
    public ResponseEntity<MessageResponse> setTotalBlock(@ApiParam(value = "ID пользователя", defaultValue = "1")
                                                         @PathVariable long id,
                                                         @RequestHeader("Authorization") String token) throws AppException {
        return adminService.setTotalBlock(id, token);
    }

    @PostMapping("/unblock/{id}")
    @ApiOperation(value = "Разблокировка пользователя", notes = "Снятие ограничений пользователя")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Успешное разблокировка", response = MessageResponse.class),
            @ApiResponse(code = 400, message = "Bad Request", response = ErrorResponse.class),
            @ApiResponse(code = 401, message = "Unauthorized", response = ErrorResponse.class)})
    public ResponseEntity<MessageResponse> setUnBlock(@ApiParam(value = "ID пользователя", defaultValue = "1")
                                                      @PathVariable long id,
                                                      @RequestHeader("Authorization") String token) throws AppException {
        return adminService.setUnBlock(id, token);
    }

    @PostMapping("/delete/{id}")
    @ApiOperation(value = "Удаление пользователя", notes = "Удаление пользователя")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Пользователь успешно удален", response = MessageResponse.class),
            @ApiResponse(code = 400, message = "Bad Request", response = ErrorResponse.class),
            @ApiResponse(code = 401, message = "Unauthorized", response = ErrorResponse.class)})
    public ResponseEntity<MessageResponse> delete(@ApiParam(value = "ID пользователя", defaultValue = "1")
                                                  @PathVariable long id,
                                                  @RequestHeader("Authorization") String token) throws AppException {
        return adminService.deletePerson(id, token);
    }

    @PostMapping("/restore/{id}")
    @ApiOperation(value = "Востановление пользователя", notes = "Востановление данных пользователя")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Пользователь успешно восстановлен", response = MessageResponse.class),
            @ApiResponse(code = 400, message = "Bad Request", response = ErrorResponse.class),
            @ApiResponse(code = 401, message = "Unauthorized", response = ErrorResponse.class)})
    public ResponseEntity<MessageResponse> restore(@ApiParam(value = "ID пользователя", defaultValue = "1")
                                                   @PathVariable long id,
                                                   @RequestHeader("Authorization") String token) throws AppException {
        return adminService.restorePerson(id, token);
    }

    @GetMapping("/statistic/all")
    @ApiOperation(value = "Статистика соцсети", notes = "Полная сводная статистика социальной сети")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Статистика успешно получена", response = AllStatisticsResponse.class),
            @ApiResponse(code = 400, message = "Bad Request", response = ErrorResponse.class),
            @ApiResponse(code = 401, message = "Unauthorized", response = ErrorResponse.class)})
    public ResponseEntity<AllStatisticsResponse> getAllStatistics(@RequestHeader("Authorization") String token)
            throws UnAuthorizedException {
        return adminService.getAllStatistics(token);
    }

    @GetMapping("/persons")
    @ApiOperation(value = "Получение списка пользователей", notes = "Получение списка пользователей")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Успешное получение", response = PersonForAdminDto.class),
            @ApiResponse(code = 400, message = "Bad Request", response = ErrorResponse.class),
            @ApiResponse(code = 401, message = "Unauthorized", response = ErrorResponse.class)})
    public ResponseEntity<List<PersonAdminPanelDto>> getPersons(@RequestHeader("Authorization") String token)
            throws UnAuthorizedException {
        return adminService.getPersonList(token);
    }

    @GetMapping("/admins")
    @ApiOperation(value = "Получение списка администраторов", notes = "Получение списка администраторов")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Успешное получение", response = AdminDto.class),
            @ApiResponse(code = 400, message = "Bad Request", response = ErrorResponse.class),
            @ApiResponse(code = 401, message = "Unauthorized", response = ErrorResponse.class)})
    public ResponseEntity<List<AdminDto>> getAdmins(@RequestHeader("Authorization") String token)
            throws UnAuthorizedException {
        return adminService.getAdminList(token);
    }

    @PostMapping("/add/{role}/{id}")
    @ApiOperation(value = "Добавление администратора", notes = "Добавление администратора с указанием статуса")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Успешное добовление", response = MessageResponse.class),
            @ApiResponse(code = 400, message = "Bad Request", response = ErrorResponse.class),
            @ApiResponse(code = 401, message = "Unauthorized", response = ErrorResponse.class)})
    public ResponseEntity<MessageResponse> addAdmin(@ApiParam(value = "Role", allowableValues = "ADMIN,MODERATOR")
                                                    @PathVariable String role,
                                                    @ApiParam(value = "ID пользователя", defaultValue = "5")
                                                    @PathVariable long id,
                                                    @RequestHeader("Authorization") String token) throws AppException {
        return adminService.addAdmin(role, id, token);
    }

    @GetMapping("/applicants")
    @ApiOperation(value = "Получение списка кандидатов", notes = "Получение списка кандидатов в модераторы/администраторы")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Успешное получение", response = ApplicantForAdmin.class),
            @ApiResponse(code = 400, message = "Bad Request", response = ErrorResponse.class),
            @ApiResponse(code = 401, message = "Unauthorized", response = ErrorResponse.class)})
    public ResponseEntity<List<ApplicantForAdmin>> getApplicants(@RequestHeader("Authorization") String token)
            throws UnAuthorizedException {
        return adminService.getApplicantList(token);
    }

    @PostMapping("/{id}/{role}")
    @ApiOperation(value = "Обновления данных администратора", notes = "Обновления данных администратора")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Успешное обновление", response = MessageResponse.class),
            @ApiResponse(code = 400, message = "Bad Request", response = ErrorResponse.class),
            @ApiResponse(code = 401, message = "Unauthorized", response = ErrorResponse.class)})
    public ResponseEntity<MessageResponse> updateAdmin(@ApiParam(value = "ID пользователя", defaultValue = "5")
                                                       @PathVariable long id,
                                                       @ApiParam(value = "Role", allowableValues = "ADMIN,MODERATOR")
                                                       @PathVariable String role,
                                                       @RequestHeader("Authorization") String token) throws AppException {
        return adminService.updateAdmin(id, role, token);
    }

    @DeleteMapping("/{id}")
    @ApiOperation(value = "Удаление администратора", notes = "Удаление администратора по номеру")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Успешное удаление", response = MessageResponse.class),
            @ApiResponse(code = 400, message = "Bad Request", response = ErrorResponse.class),
            @ApiResponse(code = 401, message = "Unauthorized", response = ErrorResponse.class)})
    public ResponseEntity<MessageResponse> removeAdmin(@ApiParam(value = "ID пользователя", defaultValue = "2")
                                                       @PathVariable long id,
                                                       @RequestHeader("Authorization") String token) throws AppException {
        return adminService.removeAdmin(id, token);
    }

}

