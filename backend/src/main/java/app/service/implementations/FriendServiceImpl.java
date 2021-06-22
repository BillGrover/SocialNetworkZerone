package app.service.implementations;

import app.dto.friend.*;
import app.dto.main.MessageResponse;
import app.dto.profile.PersonDto;
import app.dto.profile.PersonListResponse;
import app.exceptions.*;
import app.model.*;
import app.model.enums.ApprovalStatus;
import app.model.enums.BlockStatus;
import app.model.enums.FriendshipCode;
import app.model.enums.NotificationType;
import app.repository.*;
import app.service.FriendService;
import app.service.MainService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class FriendServiceImpl implements FriendService {

    @Value("${friend.recommendation.count}")
    private int friendRecommendationCount;

    private final FriendshipRepository friendshipRepository;
    private final FriendshipStatusRepository friendshipStatusRepository;
    private final MainService mainService;

    private final PersonRepository personRepository;

    @Autowired
    public FriendServiceImpl(FriendshipRepository friendshipRepository,
                             FriendshipStatusRepository friendshipStatusRepository,
                             MainService mainService,
                             PersonRepository personRepository,
                             NotificationRepository notificationRepository) {
        this.friendshipRepository = friendshipRepository;
        this.friendshipStatusRepository = friendshipStatusRepository;
        this.mainService = mainService;
        this.personRepository = personRepository;
    }

    @Override
    public ResponseEntity<PersonListResponse> friendsAll(String name, int offset, int itemPerPage, String token)
            throws UnAuthorizedException {

        Person person = mainService.getPersonByToken(token);

        int page = offset / itemPerPage;
        Pageable sorted = PageRequest.of(page, itemPerPage);
        List<Person> personList = personRepository.findByFriends(person.getId(), name, sorted);
        List<PersonDto> list = insertPersonDtoList(personList);

        PersonListResponse response = new PersonListResponse();
        response.setTotal(personRepository.countByFriends(person.getId(), name));
        response.setOffset(offset);
        response.setPerPage(itemPerPage);
        response.setData(list);

        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<MessageResponse> friendDelete(Long friendId, String token) throws AppException {

        Person person = mainService.getPersonByToken(token);
        Person friend = mainService.getPersonById(friendId);

        friendshipDelete(person.getId(), friend.getId());
        friendshipDelete(friend.getId(), person.getId());

        return ResponseEntity.ok(MessageResponse.ok());
    }

    public ResponseEntity<MessageResponse> friendAdd(Long friendId, String token) throws AppException {

        Person person = mainService.getPersonByToken(token);

        //получаем пользователя отправившего запрос
        Person friend = mainService.getPersonById(friendId);
        if (friend.getId() == person.getId()) throw new BadRequestException("Нельзя добавлять самого себя");

        //получаем связку двух пользователей: запросившего и текущего
        Optional<Friendship> friendshipOptional = friendshipRepository
                .findAllByDstPersonAndSrcPerson(person.getId(), friend.getId());
        //обратная связь
        Optional<Friendship> revertFriendshipOptional = friendshipRepository
                .findAllByDstPersonAndSrcPerson(friend.getId(), person.getId());

        Friendship revertFriendship = revertFriendshipOptional.orElseGet(Friendship::new);
        FriendshipStatus friendshipStatus;
        FriendshipStatus revertFriendshipStatus;

        //если есть связь пользователь-друг
        //есть ли  пользователю заявка от друга .
        if (friendshipOptional.isPresent()
                && friendshipOptional.get().getStatus().getFriendshipCode().equals(FriendshipCode.REQUEST)) {

            Friendship friendship = friendshipOptional.get();
            friendshipStatus = friendship.getStatus();
            friendshipStatus.setFriendshipCode(FriendshipCode.FRIEND);
            friendshipStatus.setTime(new Date());
            friendshipStatusRepository.save(friendshipStatus);
            friendship.setStatus(friendshipStatus);
            friendshipRepository.save(friendship);

            //обратное подтверждение дружбы
            if (revertFriendshipOptional.isEmpty()) {

                //если раньше связи не было
                revertFriendship = createNewFriendship(person, friend);
                // создаем новый статус дружбы
                revertFriendshipStatus = new FriendshipStatus();

            } else {
                //получаем статус запрос
                revertFriendshipStatus = revertFriendshipOptional.get().getStatus();
            }
            revertFriendshipStatus.setFriendshipCode(FriendshipCode.FRIEND);
        } else {
            if (revertFriendshipOptional.isEmpty()) {
                revertFriendship = createNewFriendship(person, friend);
                // создаем новый статус дружбы
                revertFriendshipStatus = new FriendshipStatus();
            } else {
                revertFriendshipStatus = revertFriendshipOptional.get().getStatus();
            }
            revertFriendshipStatus.setFriendshipCode(FriendshipCode.REQUEST);
            //оповещение
            mainService.addNotification(friend, person, NotificationType.FRIEND_REQUEST);
        }
        revertFriendshipStatus.setTime(new Date());
        friendshipStatusRepository.save(revertFriendshipStatus);
        revertFriendship.setStatus(revertFriendshipStatus);
        friendshipRepository.save(revertFriendship);
        return ResponseEntity.ok(MessageResponse.ok());
    }
    @Override
    public ResponseEntity<PersonListResponse> friendsRequest(String name, int offset, int itemPerPage, String token)
            throws UnAuthorizedException {

        int page = offset / itemPerPage;
        Pageable sorted = PageRequest.of(page, itemPerPage);
        Person personTek = mainService.getPersonByToken(token);

        List<Person> personList = personRepository.findByFriendsRequest(personTek.getId(), name, sorted);
        List<PersonDto> list = insertPersonDtoList(personList);

        PersonListResponse response = new PersonListResponse();
        response.setTotal(personRepository.countByFriendsRequest(personTek.getId(), name));
        response.setOffset(offset);
        response.setPerPage(itemPerPage);
        response.setData(list);

        return ResponseEntity.ok(response);
    }
    @Override
    public ResponseEntity<FriendStatusListResponse> friendAndFriend(FriendRequest request, String token) throws UnAuthorizedException {

        FriendStatusListResponse response = new FriendStatusListResponse();
        Person personTek = mainService.getPersonByToken(token);

        List<Long> listId = request.getUserIds();
        List<FriendStatusDto> friendStatusDtoList = new ArrayList<>();
        for (Long idFriends : listId) {
            int count = friendshipRepository.countFriendAndFriend(personTek.getId(), idFriends);
            FriendStatusDto friendStatusDto = new FriendStatusDto();
            friendStatusDto.setUserId(idFriends);
            friendStatusDto.setStatus(count > 0 ? "FRIEND" : "NOT FRIEND");
            friendStatusDtoList.add(friendStatusDto);
        }
        response.setData(friendStatusDtoList);

        return ResponseEntity.ok(response);
    }
    @Override
    public ResponseEntity<PersonListResponse> friendsRecommendations(int offset, int itemPerPage, String token)
            throws UnAuthorizedException {

        Person person = mainService.getPersonByToken(token);

        int page = offset / itemPerPage;
        Pageable sorted = PageRequest.of(page, itemPerPage);
        List<Person> personList = new ArrayList<>();
        List<Person> recommendationPersonList = personRepository.findByFriendsRecommendations(person.getId(), sorted);

        //показываем только 5 рекомендаций
        for (Person personAdd : recommendationPersonList) {
            personList.add(personAdd);
            if (personList.size() == friendRecommendationCount) {
                break;
            }
        }
        if (personList.size() < friendRecommendationCount) {
            //добавляем рекомендованных друзей до 5

            List<Person> frendsList = personRepository.findByFriendsAll(person.getId());
            List<Person> requestList = personRepository.findByFriendsRequest(person.getId(), "", null);
            //по городу
            if (person.getCity() != null) {
                recommendationPersonList = personRepository.findTop10ByCity(person.getCity());
                addRecommend(personList, recommendationPersonList, frendsList, requestList, person);
            }
            //по стране
            if (personList.size() < friendRecommendationCount && person.getCountry() != null) {
                recommendationPersonList = personRepository.findTop10ByCountry(person.getCountry());
                addRecommend(personList, recommendationPersonList, frendsList, requestList, person);
            }
            //любых
            if (personList.size() < friendRecommendationCount) {
                recommendationPersonList = personRepository.findTop30By();
                addRecommend(personList, recommendationPersonList, frendsList, requestList, person);
            }
        }
        List<PersonDto> list = insertPersonDtoList(personList);

        PersonListResponse response = new PersonListResponse();
        response.setTotal(personRepository.countByFriendsRecommendations(person.getId()));
        response.setOffset(offset);
        response.setPerPage(itemPerPage);
        response.setData(list);

        return ResponseEntity.ok(response);
    }


    private Friendship createNewFriendship(Person person, Person friend) {
        Friendship friendship = new Friendship();
        friendship.setSrcPerson(person);
        friendship.setDstPerson(friend);
        return friendship;
    }

    private void friendshipDelete(long personId, long friendId) {
        Optional<Friendship> friendship = friendshipRepository
                .findAllByDstPersonAndSrcPerson(personId, friendId);
        FriendshipStatus friendshipStatus;
        if (friendship.isPresent()) {
            friendshipStatus = setDeclinedStatus(friendship.get());
            friendshipRepository.save(friendship.get());
            friendshipStatusRepository.save(friendshipStatus);
        }
    }

    private FriendshipStatus setDeclinedStatus(Friendship friendship) {
        FriendshipStatus friendshipStatus;
        friendshipStatus = friendship.getStatus();
        friendshipStatus.setFriendshipCode(FriendshipCode.DECLINED);
        friendshipStatus.setTime(new Date());
        friendship.setStatus(friendshipStatus);
        return friendshipStatus;
    }

    private void addRecommend(List<Person> personList,
                              List<Person> recommendationPersonList,
                              List<Person> friendList, List<Person> requestList,
                              Person personTek) {
        for (Person person : recommendationPersonList) {
            if (!personList.contains(person) &&
                    !friendList.contains(person) &&
                    !requestList.contains(person) &&
                    personTek != person &&
                    person.getApproval().equals(ApprovalStatus.APPROVED) &&
                    person.getBlock().equals(BlockStatus.UNBLOCKED)) {
                personList.add(person);
            }
            if (personList.size() == friendRecommendationCount) {
                break;
            }
        }
    }

    private List<PersonDto> insertPersonDtoList(List<Person> personList){
        return personList.stream()
                .map(PersonDto::new)
                .collect(Collectors.toList());
    }

}
