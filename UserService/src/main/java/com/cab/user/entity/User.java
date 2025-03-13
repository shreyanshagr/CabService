package com.cab.user.entity;

import com.cab.user.enums.Gender;
import com.cab.user.enums.Role;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
//@Entity
//@Table(name = "users", uniqueConstraints = {
//        @UniqueConstraint(columnNames = "email"),
//        @UniqueConstraint(columnNames = "mobileNumber")
//})
@MappedSuperclass
public class User {

//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Integer userId;

    private boolean enabled;

    @NotNull
    @Column(nullable = false)
    private String name;

    @NotNull
    @Column(nullable = false, unique = true)
    private String email;

    @NotNull
    @Column(nullable = false, unique = true)
    private String mobileNumber;

    @NotNull
    @Column(nullable = false)
    private String password;

    private String profilePicture = "https://res.cloudinary.com/dwgptuzgd/image/upload/v1716280827/paisa_wapis/avatar/thlnk0rgetlygt1b8mzq.jpg";

    private LocalDate birthDate;

    @Enumerated(EnumType.STRING)  // This will store enum as STRING instead of ordinal
    @Column(nullable = false)
    private Role role;

    @Enumerated(EnumType.STRING)  // This will store enum as STRING instead of ordinal
    private Gender gender;

    @CreatedDate
    private LocalDateTime signupDate;

    private LocalDateTime lastLoginDate;
}
