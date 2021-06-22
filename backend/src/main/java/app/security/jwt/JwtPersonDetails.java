package app.security.jwt;

import app.model.Person;
import app.model.enums.ApprovalStatus;
import app.model.enums.BlockStatus;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;

public class JwtPersonDetails implements UserDetails {

    private final Person person;

    public JwtPersonDetails(Person person) {
        this.person = person;
    }


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return new ArrayList<>();
    }

    @Override
    public String getPassword() {
        return person.getPassword();
    }

    @Override
    public String getUsername() {
        return person.getEmail();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return person.getBlock() == BlockStatus.UNBLOCKED;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {        return person.getApproval() == ApprovalStatus.APPROVED;
    }
}
