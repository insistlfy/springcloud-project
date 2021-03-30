package org.lfy.jwt.config;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.Instant;
import java.util.Collection;
import java.util.List;

/**
 * JwtUserDetails
 *
 * @author lfy
 * @date 2021/3/30
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
public class JwtUserDetails implements UserDetails {

    @ApiModelProperty(value = "用户id")
    private Long userId;

    @ApiModelProperty(value = "用户密码")
    private String password;

    @ApiModelProperty(value = "用户名")
    private String username;

    @ApiModelProperty(value = "用户角色权限")
    private Collection<? extends GrantedAuthority> authorities;

    @ApiModelProperty(value = "账号是否过期")
    private Boolean isAccountNonExpired = false;

    @ApiModelProperty(value = "账号是否锁定")
    private Boolean isAccountNonLocked = false;

    @ApiModelProperty(value = "密码是否过期")
    private Boolean isCredentialsNonExpired = false;

    @ApiModelProperty(value = "账号是否激活")
    private Boolean isEnabled = true;

    @ApiModelProperty("上次密码重置时间")
    private Instant lastPasswordResetDate;

    public JwtUserDetails(Long userId, String username, String password, List<GrantedAuthority> authorities) {
        this.userId = userId;
        this.username = username;
        this.password = password;
        this.authorities = authorities;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return isAccountNonExpired;
    }

    @Override
    public boolean isAccountNonLocked() {
        return isAccountNonLocked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return isCredentialsNonExpired;
    }

    @Override
    public boolean isEnabled() {
        return isEnabled;
    }
}
