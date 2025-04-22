package study.issue_mate.jwt;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import study.issue_mate.entity.User;

import java.util.Collection;
import java.util.List;

@Slf4j
@Getter
@RequiredArgsConstructor
public class CustomUserDetails implements UserDetails {

    private final User user;



    // 추후 role 확정 후 적용
//    @Override
//    public Collection<? extends GrantedAuthority> getAuthorities() {
//        Collection<GrantedAuthority> authorities = new ArrayList<>();
//        authorities.add(new GrantedAuthority() {
//            @Override
//            public String getAuthority() {
//                return user.getRole().name();
//            }
//        });
//
//        return authorities;
//    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of();
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() { return user.getUserEmail(); }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
