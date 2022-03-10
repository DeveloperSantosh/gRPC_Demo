package com.treeleaf.model;

import com.sun.istack.NotNull;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Set;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Table(name = "user")
public class UserEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int userId;
    @NotNull
    private String username;
    @NotNull
    private String password;
    @OneToOne(targetEntity = UserDetailsEntity.class, cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private UserDetailsEntity details;
    @ManyToMany(targetEntity = UserRoleEntity.class, cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinTable(name = "user_roles",
            joinColumns = {@JoinColumn(name = "user_id")},
            inverseJoinColumns = {@JoinColumn(name = "role_type")})
    private Set<UserRoleEntity> role;
}
