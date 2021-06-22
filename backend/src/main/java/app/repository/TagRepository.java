package app.repository;

import app.model.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

/**
 * Repository for working with "Tags".
 *
 * @author Surkov Aleksey (stibium128@gmail.com)
 * @date 01.02.2021 15:46
 */
public interface TagRepository extends JpaRepository<Tag, Long> {

    /**
     * Finding and returns a tag, ignoring case.
     *
     * @param name Tag name.
     * @return Returns the found tag.
     */
    Optional<Tag> findFirstByNameIgnoreCase(String name);

    List<Tag> findAllByNameIgnoreCase(String name);

    Page<Tag> findAllByNameIgnoreCase(String name, Pageable pageable);

}
