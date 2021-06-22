package app.repository;

import app.model.Person;
import app.model.Post;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Repository
public interface PostRepository extends PagingAndSortingRepository<Post, Long> {

    List<Post> findAllBy(Pageable p);

    List<Post> findAllByTimeBeforeOrderByTimeDesc(Date date, Pageable p);

    List<Post> findAllByTitleContainingAndTimeBeforeOrderByTimeDesc(String title, Date date, Pageable p);

    int countAllBy();

    int countByTitleContaining(String title);

    List<Post> findByAuthorOrderByTimeDesc(Person author);

    @Query("from Post p where (p.title like ?1 or p.text like ?1) and " +
            "p.time between ?2 and ?3 and " +
            "(?2 is null or (p.author.firstName like ?4 or p.author.lastName like ?4)) " +
            "order by p.time desc")
    List<Post> search(
            String text,
            Date timeFrom,
            Date timeTo,
            String author);

    @Modifying
    @Transactional
    void deleteById(Long id);
}
