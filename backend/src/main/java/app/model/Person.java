package app.model;

import app.dto.account.RegistrationRequest;
import app.model.enums.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.persistence.*;
import java.io.Serializable;
import java.util.*;

/**
 * Класс обычных пользователей
 * <br>(без административного фуккционала)
 */
@Entity
@Table(name = "persons")
public class Person implements Serializable {

    /**
     * Порядковый идентификатор пользователя
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    /**
     * Имя пользователя
     */
    private String firstName;

    /**
     * Фамилия пользователя
     */
    private String lastName;

    /**
     * Дата регистрации пользователя
     */
    private Date registrationDate;

    /**
     * День рождения пользователя
     */
    private Date birthDate;

    /**
     * Электронная почта пользователя
     */

    private String email;

    /**
     * Телефон пользователя
     */
    private String phone;

    /**
     * Хеш-код текущего пароля
     */
    private String password;

    /**
     * Ссылка на аватар
     */
    private String photo;

    /**
     * Текст о себе
     */
    private String about;

    @ManyToOne
    @JoinColumn(name = "city_id")
    private City city;

    @ManyToOne
    @JoinColumn(name = "country_id")
    private Country country;

    /**
     * Код восстановления пароля или подтверждения регистрации
     */
    private String confirmationCode;

    /**
     * Признак подтверждена ли регистрация
     */
    @Enumerated(EnumType.STRING)
    private ApprovalStatus approval;

    /**
     * Разрешения на получение сообщений
     */
    @Enumerated(EnumType.STRING)
    private MessagePermission messagePermission;

    /**
     * Время последнего пребывания онлайн
     */
    private Date lastOnlineTime;

    /**
     * Признак блокировки пользователя модератором/администратором
     */
    @Enumerated(EnumType.STRING)
    private BlockStatus block;

    @OneToOne(mappedBy = "person")
    private BlockHistory blockHistory;

    @OneToMany(mappedBy = "author")
    private Set<Message> outcomeMessageList;


    @OneToMany(mappedBy = "recipient")
    private Set<Message> incomeMessageList;

    @OneToMany(mappedBy = "author")
    private Set<Post> postList;

    @OneToMany(mappedBy = "person")
    private Set<PostLike> likes;

    @OneToMany(mappedBy = "author")
    private Set<PostComment> comments;

    @OneToMany(mappedBy = "targetPerson")
    private Set<Notification> notifications;

    @OneToMany(mappedBy = "authorPerson")
    private Set<Notification> authorNotifications;


    @OneToOne (mappedBy = "person")
    private ResetToken resetToken;

    // METHODS
    public static Person createDefaultPerson() {
        Person person = new Person();
        person.setPhoto("/static/img/default_avatar.png");
        person.setMessagePermission(MessagePermission.ALL);
        return person;
    }

    /**
     * Метод создает новый экземпляр класса {@link Person}
     * <br> и устанавливает ему параметры неподтвержденного объекта
     *
     * @param dto данные из запроса на регистрацию
     * @return неподтвержденный объект
     */
    public static Person createNotApprovedPerson(RegistrationRequest dto) {
        Person person = createDefaultPerson();
        person.setEmail(dto.getEmail());
        person.setFirstName(dto.getFirstName());
        person.setLastName(dto.getLastName());
        person.setApproval(ApprovalStatus.REJECTED);
        person.setBlock(BlockStatus.BLOCKED);
        person.setPassword(
                new BCryptPasswordEncoder()
                        .encode(dto.getPassword())
        );
        UUID uuid = UUID.randomUUID();
        String randomUUIDString = uuid.toString();
        person.setConfirmationCode(randomUUIDString);
        person.setRegistrationDate(new Date());
        person.setLastOnlineTime(new Date());
        return person;
    }

    /**
     * Метод устанавлевает пользователю соответствующие параметры после получения подтвержнения
     */
    public void approvalPassed() {
        setApproval(ApprovalStatus.APPROVED);
        setBlock(BlockStatus.UNBLOCKED);
    }

    public Person online() {
        this.setLastOnlineTime(null);
        return this;
    }

    public Person offline() {
        this.setLastOnlineTime(new Date());
        return this;
    }

    // GETTERS & SETTERS

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Date getRegistrationDate() {
        return registrationDate;
    }

    public void setRegistrationDate(Date registrationDate) {
        this.registrationDate = registrationDate;
    }

    public Date getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getAbout() {
        return about;
    }

    public void setAbout(String about) {
        this.about = about;
    }

    public City getCity() {
        return city;
    }

    public void setCity(City city) {
        this.city = city;
    }

    public Country getCountry() {
        return country;
    }

    public void setCountry(Country country) {
        this.country = country;
    }

    public String getConfirmationCode() {
        return confirmationCode;
    }

    public void setConfirmationCode(String confirmationCode) {
        this.confirmationCode = confirmationCode;
    }

    public ApprovalStatus getApproval() {
        return approval;
    }

    public void setApproval(ApprovalStatus approvalStatus) {
        this.approval = approvalStatus;
    }

    public MessagePermission getMessagePermission() {
        return messagePermission;
    }

    public void setMessagePermission(MessagePermission messagePermission) {
        this.messagePermission = messagePermission;
    }

    public Date getLastOnlineTime() {
        return lastOnlineTime;
    }

    public void setLastOnlineTime(Date lastOnlineTime) {
        this.lastOnlineTime = lastOnlineTime;
    }

    public BlockStatus getBlock() {
        return block;
    }

    public void setBlock(BlockStatus blockStatus) {
        this.block = blockStatus;
    }

    public BlockHistory getBlockHistory() {
        return blockHistory;
    }

    public void setBlockHistory(BlockHistory blockHistory) {
        this.blockHistory = blockHistory;
    }

    public Set<Message> getOutcomeMessageList() {
        return outcomeMessageList;
    }

    public void setOutcomeMessageList(Set<Message> outcomeMessageList) {
        this.outcomeMessageList = outcomeMessageList;
    }

    public Set<Message> getIncomeMessageList() {
        return incomeMessageList;
    }

    public void setIncomeMessageList(Set<Message> incomeMessageList) {
        this.incomeMessageList = incomeMessageList;
    }

    public Set<Post> getPostList() {
        return postList;
    }

    public void setPostList(Set<Post> postList) {
        this.postList = postList;
    }

    public Set<PostLike> getLikes() {
        return likes;
    }

    public void setLikes(Set<PostLike> likes) {
        this.likes = likes;
    }

    public Set<PostComment> getComments() {
        return comments;
    }

    public void setComments(Set<PostComment> author) {
        this.comments = author;
    }

    public Set<Notification> getNotifications() {
        return notifications;
    }

    public void setNotifications(Set<Notification> notifications) {
        this.notifications = notifications;
    }

    public ResetToken getResetToken() {
        return resetToken;
    }

    public void setResetToken(ResetToken resetToken) {
        this.resetToken = resetToken;
    }

    public Set<Notification> getAuthorNotifications() {
        return authorNotifications;
    }

    public void setAuthorNotifications(Set<Notification> authorNotifications) {
        this.authorNotifications = authorNotifications;
    }
}