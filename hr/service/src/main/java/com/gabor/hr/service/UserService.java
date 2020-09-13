package com.gabor.hr.service;

import com.gabor.model.config.Constants;
import com.gabor.hr.exception.InvalidCredentials;
import com.gabor.hr.exception.ResourceNotFoundException;
import com.gabor.hr.exception.TokenInvalidException;
import com.gabor.hr.model.User;
import com.gabor.hr.repository.RoleRepository;
import com.gabor.hr.repository.UserRepository;
import com.gabor.hr.service.dto.UserDto;
import com.gabor.hr.service.dto.special.UserViewDto;
import com.gabor.hr.service.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.Date;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;


@Service(value = "userService")
public class UserService extends CrudService<User, UserDto, UserRepository> implements UserDetailsService {

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private EmailService emailService;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = repository.findByEmail(email);

        if (user == null) {
            throw new UsernameNotFoundException("Invalid email or password.");
        }

        return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPasswordHash(), getAuthoritiesForUser(user));
    }

    public void verifyTokenIssuedTime(String email, Date issuedTime) {
        User user = repository.findByEmail(email);

        if (user.getTokenStartDate() != null && user.getTokenStartDate().after(issuedTime)) {
            throw new TokenInvalidException("Token is not valid anymore");
        }
    }

    public UserViewDto findByEmail(String email) {
        User user = repository.findByEmail(email);

        if (user == null) {
            throw new UsernameNotFoundException("Invalid email or password.");
        }

        return modelMapper.map(user, UserViewDto.class);
    }

    @Override
    public UserDto save(UserDto userDto) {
        //generate random password which will be sent to user in e-mail
        //length=10
        String password = createRandomPassword(10);

        User newUser = userMapper.userDtoToUser(userDto, passwordEncoder.encode(password));
        User savedUser = repository.save(newUser);

        String body = "Hi " + savedUser.getFirstName() + ", <br/> An account has been created for you, <br/> " +
                "your password is: " + password + " <br/> " +
                "but please change your password after your first successful login(Profile Tab -> Change Password.)<br/>" +
                "<a href='" + Constants.APP_URL + "'>Log in:  </a><br/><br/>" +
                "Greetings from our app, BusinessTripRequest";

        //send email only is user was saved successfully
        emailService.sendSimpleMessage(savedUser.getEmail(), "User account was created for you", body);

        return modelMapper.map(savedUser, UserDto.class);
    }

    /**
     * update without changing the password of the user
     *
     * @param userDto
     */
    @Override
    public UserDto update(UserDto userDto) {
        Long id = userDto.getId();
        Optional<User> optionalUser = repository.findById(id);

        if (!optionalUser.isPresent()) {
            throw new ResourceNotFoundException(User.class.getSimpleName(), "id", id);
        }

        User savedUser = optionalUser.get();

        User newUser = userMapper.userDtoToUser(userDto, savedUser.getPasswordHash());

        //when updating a user, all previous tokens will be invalidated,
        //so must update the token minimum issued time
        newUser.setTokenStartDate(new Date());
        return modelMapper.map(repository.save(newUser), UserDto.class);
    }

    public UserDto changePassword(String username, String passwordOld, String password) throws Exception {
        User user = repository.findByEmail(username);

        if (!passwordEncoder.matches(passwordOld, user.getPasswordHash())) {
            throw new InvalidCredentials("Invalid credentials!");
        }

        user.setPasswordHash(passwordEncoder.encode(password));
        return modelMapper.map(repository.save(user), UserDto.class);
    }

    private Set<SimpleGrantedAuthority> getAuthoritiesForUser(User user) {
        Set<SimpleGrantedAuthority> authorities = new HashSet<>();
        authorities.add(new SimpleGrantedAuthority(user.getRole().getName()));
        return authorities;
    }

    private String createRandomPassword(int length) {
        String chars = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890!@#$%^&*";
        StringBuilder pass = new StringBuilder();
        SecureRandom random = new SecureRandom();

        while (pass.length() < length) {
            int index = random.nextInt(chars.length());
            pass.append(chars.charAt(index));
        }

        return pass.toString();
    }
}
