package com.example.lesson59.entity;

import com.example.lesson59.entity.helper.AbstractHelper;
import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToMany;
import java.util.List;

@Entity(name = "users")
@AllArgsConstructor
@NoArgsConstructor
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class User extends AbstractHelper{
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    Long id;

    @Column(unique = true,nullable = false)
    String userName;
    String password;
    String fullName;
    @Column(unique = true,nullable = false)
    String phoneNumber;

    @ManyToMany(fetch = FetchType.EAGER)
    List<Role> roleList;  // ROLE_ADMIN ROLE_USER

//    @CreationTimestamp
//    Date createAt;
//
//
//    @UpdateTimestamp
//    Date updateAt;
}
