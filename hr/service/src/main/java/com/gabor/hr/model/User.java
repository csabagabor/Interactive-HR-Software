package com.gabor.hr.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.gabor.model.config.Constants;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.util.Date;


@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "users")
public class User extends BaseModel {

    @Column(length = Constants.USER_FIRST_NAME_LENGTH_MAX)
    private String firstName;

    @Column(length = Constants.USER_LAST_NAME_LENGTH_MAX)
    private String lastName;

    @Column(length = Constants.USER_EMAIL_LENGTH_MAX, unique = true)
    private String email;

    @Column
    @JsonIgnore
    private String passwordHash;

    @ManyToOne
    @JoinColumn(name = "role_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Role role;

    @Column
    private Date tokenStartDate;

    public User(String firstName, String lastName, String email, String passwordHash) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.passwordHash = passwordHash;
    }
}
