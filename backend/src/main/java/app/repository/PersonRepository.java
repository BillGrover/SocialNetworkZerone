package app.repository;

import app.model.*;
import app.model.enums.ApprovalStatus;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Repository
public interface PersonRepository extends CrudRepository<Person, Long> {

    List<Person> findAllByApproval(ApprovalStatus approvalStatus);

    Optional<Person> findByEmail(String email);

    boolean existsByEmail(String email);

    /**
     * Поиск пользователя по id
     *
     * @param id id пользователя
     * @return {@link Person экземпляр пользователя}, обернут в {@link Optional}.
     */
    Optional<Person> findById(long id);

    @Query("from Person p where p.approval = 'APPROVED' and p.firstName like ?1 or p.lastName like ?1")
    List<Person> search(String firstName);

    @Query("from Person p where p.approval = 'APPROVED' and " +
            "(?1 is null or p.firstName like ?1) and "
            + "(?2 is null or p.lastName like ?2) and "
            + "(?3 is null or p.country.name like ?3) and "
            + "(?4 is null or p.city.name like ?4) and "
            + "((p.birthDate is null or ?6 is null) or p.birthDate between ?5 and ?6)"
    )
    List<Person> strictSearch(
            String firstName,
            String lastName,
            String country, String city,
            Date from, Date to
    );

    @Transactional
    @Modifying
    @Query("delete from Person p where p.approval = 'REJECTED' and p.registrationDate < ?1")
    void deleteOldRegistrationRequest(Date date);

    @Query(nativeQuery = true, value = "SELECT * FROM  persons " +
            "where id in (SELECT distinct  src_person_id FROM friendship as a " +
            "left join  friendship_status as b on a.friendship_status_id = b.id " +
            "where b.friendship_code = 'FRIEND' and dst_person_id= ?1 and (first_name like %?2% or last_name like %?2% )) ")
    List<Person> findByFriends(Long id, String name, Pageable p);

    @Query(nativeQuery = true, value = "SELECT count(*) FROM persons " +
            "where id in (SELECT distinct  src_person_id FROM  friendship as a " +
            "left join  friendship_status as b on a.friendship_status_id = b.id " +
            "where b.friendship_code = 'FRIEND' and dst_person_id= ?1 and (first_name like %?2% or last_name like %?2% )) ")
    int countByFriends(Long id, String name);

    @Query(nativeQuery = true, value = "SELECT * FROM  persons " +
            "where id in (SELECT distinct  src_person_id FROM friendship as a " +
            "left join  friendship_status as b on a.friendship_status_id = b.id " +
            "where b.friendship_code = 'REQUEST' and dst_person_id= ?1 and (first_name like %?2% or last_name like %?2% )) ")
    List<Person> findByFriendsRequest(Long id, String name, Pageable p);

    @Query(nativeQuery = true, value = "SELECT count(*) FROM  persons " +
            "where id in (SELECT distinct  src_person_id FROM  friendship as a " +
            "left join  friendship_status as b on a.friendship_status_id = b.id " +
            "where b.friendship_code = 'REQUEST' and dst_person_id= ?1 and (first_name like %?2% or last_name like %?2% )) ")
    int countByFriendsRequest(Long id, String name);

    @Query(nativeQuery = true, value = "SELECT * FROM persons " +
            "where id in " +
            "(SELECT distinct  src_person_id FROM  friendship as a " +
            "left join  friendship_status as b on a.friendship_status_id = b.id " +
            "where b.friendship_code = 'FRIEND' and dst_person_id in " +
            "(SELECT distinct  src_person_id FROM  friendship as a " +
            "left join  friendship_status as b on a.friendship_status_id = b.id " +
            "where b.friendship_code = 'FRIEND' and dst_person_id= ?1)" +
            ") and id <> ?1 and id not in (SELECT distinct  src_person_id FROM  friendship as a " +
            "left join  friendship_status as b on a.friendship_status_id = b.id " +
            "where b.friendship_code ='FRIEND' and dst_person_id= ?1) "+
            "and id not in (SELECT distinct  src_person_id FROM  friendship as a " +
            "left join  friendship_status as b on a.friendship_status_id = b.id " +
            "where b.friendship_code ='REQUEST' and dst_person_id= ?1)")
    List<Person> findByFriendsRecommendations(Long id, Pageable p);

    @Query(nativeQuery = true, value = "SELECT count(*) FROM  persons " +
            "where id in " +
            "(SELECT distinct  src_person_id FROM  friendship as a " +
            "left join  friendship_status as b on a.friendship_status_id = b.id " +
            "where b.friendship_code = 'FRIEND' and dst_person_id in " +
            "(SELECT distinct  src_person_id FROM  friendship as a " +
            "left join  friendship_status as b on a.friendship_status_id = b.id " +
            "where b.friendship_code = 'FRIEND' and dst_person_id= ?1)" +
            ") and id <> ?1 and id not in (SELECT distinct  src_person_id FROM  friendship as a " +
            "left join  friendship_status as b on a.friendship_status_id = b.id " +
            "where b.friendship_code ='FRIEND' and dst_person_id= ?1) "+
            "and id not in (SELECT distinct  src_person_id FROM  friendship as a " +
            "left join  friendship_status as b on a.friendship_status_id = b.id " +
            "where b.friendship_code ='REQUEST' and dst_person_id= ?1)")
    int countByFriendsRecommendations(Long id);

    @Query(nativeQuery = true, value = "SELECT * FROM  persons " +
            "where id in (SELECT distinct  src_person_id FROM friendship as a " +
            "left join  friendship_status as b on a.friendship_status_id = b.id " +
            "where b.friendship_code = 'FRIEND' and dst_person_id= ?1 ) ")
    List<Person> findByFriendsAll(Long id);


    List<Person> findTop10ByCity(City city);

    List<Person> findTop10ByCountry(Country country);

    List<Person> findTop30By();

    @Query(nativeQuery = true, value = "SELECT * FROM persons where approval = 'APPROVED' AND " +
            "birth_date IS NOT NULL AND city_id IS NOT NULL AND country_id IS NOT NULL AND " +
            "phone IS NOT NULL AND photo IS NOT NULL AND block = 'UNBLOCKED'")
    List<Person> findForApplicants();
}

