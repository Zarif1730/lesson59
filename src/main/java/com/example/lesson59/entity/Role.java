package com.example.lesson59.entity;

import com.example.lesson59.entity.helper.AbstractHelper;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.Entity;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class Role extends AbstractHelper implements GrantedAuthority {
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    Long id;

    String name;  // ROLE_ADMIN  ROLE_USER  ROLE_OWNER

    @Override
    public String getAuthority() {
        return name;
    }


//    @CreationTimestamp
//    Date createAt;
//
//
//    @UpdateTimestamp
//    Date updateAt;
}
