package com.treeleaf.model;

import lombok.*;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Table(name = "user_role")
public class UserRoleEntity implements Serializable {
    @Id
    private String role_type;

    private String description;

}
