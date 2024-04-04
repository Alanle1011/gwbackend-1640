package com.example.backend1640.service.impl;

import com.example.backend1640.constants.UserRoleEnum;
import com.example.backend1640.dto.*;
import com.example.backend1640.entity.Contribution;
import com.example.backend1640.entity.Faculty;
import com.example.backend1640.entity.Image;
import com.example.backend1640.entity.User;
import com.example.backend1640.entity.converters.StatusConverter;
import com.example.backend1640.entity.converters.UserRoleConverter;
import com.example.backend1640.exception.*;
import com.example.backend1640.repository.ContributionRepository;
import com.example.backend1640.repository.FacultyRepository;
import com.example.backend1640.repository.ImageRepository;
import com.example.backend1640.repository.UserRepository;
import com.example.backend1640.service.UserService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final FacultyRepository facultyRepository;
    private final ContributionRepository contributionRepository;
    private final ImageRepository imageRepository;
    private final PasswordEncoder passwordEncoder;
    private final RabbitTemplate rabbitTemplate;

    @Value("${rabbitmq.exchange.name}")
    private String exchange;

    @Value("${rabbitmq.routing.user}")
    private String userRountingKey;

    public UserServiceImpl(UserRepository userRepository, FacultyRepository facultyRepository, ContributionRepository contributionRepository, ImageRepository imageRepository, PasswordEncoder passwordEncoder, RabbitTemplate rabbitTemplate) {
        this.userRepository = userRepository;
        this.facultyRepository = facultyRepository;
        this.contributionRepository = contributionRepository;
        this.imageRepository = imageRepository;
        this.passwordEncoder = passwordEncoder;
        this.rabbitTemplate = rabbitTemplate;
    }

    @Override
    public UserDTO createUser(CreateUserDTO userDTO) {
        if (userDTO.getUserRole() == UserRoleEnum.STUDENT && userDTO.getFaculty() == null) {
            throw new MissingStudentFacultyException("Student must have a faculty");
        }

        validateUserExists(userDTO.getEmail());
        User user = new User();
        String encodePassword = passwordEncoder.encode(userDTO.getPassword());
        if (userDTO.getUserRole() == UserRoleEnum.STUDENT) {
            Faculty faculty = validateFacultyExists(userDTO.getFaculty());
            user.setFacultyId(faculty);
        }
        BeanUtils.copyProperties(userDTO, user);
        user.setCreatedAt(new Date());
        user.setUpdatedAt(new Date());
        user.setPassword(encodePassword);

        //Save User
        User savedUser = userRepository.save(user);
        UserDTO responseUserDTO = new UserDTO();
        BeanUtils.copyProperties(savedUser, responseUserDTO);
        if (savedUser.getUserRole() == UserRoleEnum.STUDENT) {
            responseUserDTO.setFaculty(savedUser.getFacultyId().getFacultyName());
        }

        //Send Email
        if (savedUser.getUserRole() == UserRoleEnum.STUDENT) {

            rabbitTemplate.convertAndSend(exchange,
                    userRountingKey,
                    EmailDetails.builder()
                            .messageBody("Your account have been Successful CREATED with mail: " + savedUser.getEmail() + "\n" +
                                    "Password: " + userDTO.getPassword() + "\n" +
                                    "Name: " + userDTO.getName() + "\n" +
                                    "Role: " + userDTO.getUserRole() + "\n" +
                                    "Faculty: " + user.getFacultyId().getFacultyName() + "\n"
                            )
                            .recipient(user.getEmail())
                            .subject("REGISTRATION SUCCESS")
                            .build());
        } else {
            rabbitTemplate.convertAndSend(exchange,
                    userRountingKey,
                    EmailDetails.builder()
                            .messageBody("Your account have been Successful CREATED with mail: " + savedUser.getEmail() + "\n" +
                                    "Password: " + userDTO.getPassword() + "\n" +
                                    "Name: " + userDTO.getName() + "\n" +
                                    "Role: " + userDTO.getUserRole() + "\n"
                            )
                            .recipient(user.getEmail())
                            .subject("USER REGISTRATION SUCCESS")
                            .build());
        }

        return responseUserDTO;
    }


    @Override
    public LoginDTO loginUser(LoginRequestDTO loginRequestDTO) {
        User user = validateUserNotExists(loginRequestDTO.getEmail());
        boolean isCorrectPassword = passwordEncoder.matches(loginRequestDTO.getPassword(), user.getPassword());

        if (!isCorrectPassword) {
            throw new UserNotExistsException("Wrong password");
        }

        Optional<User> optionalUser = userRepository.findOneByEmailAndPassword(user.getEmail(), user.getPassword());

        if (optionalUser.isEmpty()) {
            throw new UserNotExistsException("Login Fail");
        }

        LoginDTO loginDTO = new LoginDTO();
        loginDTO.setStatus(true);
        loginDTO.setUserId(optionalUser.get().getId());
        loginDTO.setName(optionalUser.get().getName());
        if (optionalUser.get().getFacultyId() != null) {
            loginDTO.setFaculty(optionalUser.get().getFacultyId().getFacultyName());
        }
        loginDTO.setEmail(optionalUser.get().getEmail());
        loginDTO.setRole(optionalUser.get().getUserRole());

        return loginDTO;

    }

    @Override
    public List<ReadUserDTO> findAll() {
        List<User> users = userRepository.findAll();
        List<ReadUserDTO> readUserDTOS = new ArrayList<>();

        for (User user : users) {
            ReadUserDTO readUserDTO = new ReadUserDTO();
            BeanUtils.copyProperties(user, readUserDTO);

            Image userImage =validateImageExist(user);
            readUserDTO.setImageId(userImage.getId());

            if (user.getUserRole() == UserRoleEnum.STUDENT) {
                readUserDTO.setFaculty(user.getFacultyId().getFacultyName());
            }
            readUserDTOS.add(readUserDTO);
        }
        return readUserDTOS;
    }

    @Override
    public ReadUserByIdDTO findById(long id) {
        User user = validateUserExists(id);
        ReadUserByIdDTO readUserByIdDTO = new ReadUserByIdDTO();
        BeanUtils.copyProperties(user, readUserByIdDTO);

        Image userImage =validateImageExist(user);
        readUserByIdDTO.setImageId(userImage.getId());

        if (user.getUserRole() == UserRoleEnum.STUDENT) {
            readUserByIdDTO.setFaculty(user.getFacultyId().getFacultyName());
        }
        return readUserByIdDTO;
    }

    @Override
    public UserDTO updateUser(UpdateUserDTO userDTO) {
        User user = validateUserExists(userDTO.getId());

        if (userDTO.getName() != null)
            user.setName(userDTO.getName());
        if (userDTO.getEmail() != null)
            user.setEmail(userDTO.getEmail());
        if (userDTO.getUserRole() != null) {
            user.setUserRole(userDTO.getUserRole());
        }
        if (userDTO.getOldPassword() != null && userDTO.getNewPassword() != null) {
            boolean isCorrectPassword = passwordEncoder.matches(userDTO.getOldPassword(), user.getPassword());
            if (!isCorrectPassword) {
                throw new WrongOldPasswordException("Wrong password");
            }
            String encodePassword = passwordEncoder.encode(userDTO.getNewPassword());
            user.setPassword(encodePassword);
        }
        if (user.getUserRole() == UserRoleEnum.STUDENT && userDTO.getFaculty() != null) {
            Faculty faculty = validateFacultyExists(userDTO.getFaculty());
            user.setFacultyId(faculty);
        }
        user.setUpdatedAt(new Date());

        User savedUser = userRepository.save(user);
        UserDTO responseUserDTO = new UserDTO();
        BeanUtils.copyProperties(savedUser, responseUserDTO);
        if (savedUser.getUserRole() == UserRoleEnum.STUDENT) {
            responseUserDTO.setFaculty(savedUser.getFacultyId().getFacultyName());
        }

        return responseUserDTO;
    }

    @Override
    public void deleteUser(long id) {
        User user = validateUserExists(id);
        List<Contribution> contributions = contributionRepository.findByUploadedUserId(user);
        contributionRepository.deleteAll(contributions);
        userRepository.delete(user);
    }

    private Faculty validateFacultyExists(Long id) {
        Optional<Faculty> optionalFaculty = facultyRepository.findById(id);

        if (optionalFaculty.isEmpty()) {
            throw new FacultyNotExistsException("Faculty does not exist");
        }
        return optionalFaculty.get();
    }


    private void validateUserExists(String email) {
        Optional<User> optionalUser = userRepository.findByEmail(email);

        if (optionalUser.isPresent()) {
            throw new UserAlreadyExistsException("User already exists");
        }
    }

    private User validateUserExists(Long id) {
        Optional<User> optionalUser = userRepository.findById(id);

        if (optionalUser.isEmpty()) {
            throw new UserNotExistsException("User does not exist");
        }

        return optionalUser.get();
    }

    private User validateUserNotExists(String email) {
        Optional<User> optionalUser = userRepository.findByEmail(email);

        if (optionalUser.isEmpty()) {
            throw new UserNotExistsException("User does not exist");
        }

        return optionalUser.get();
    }

    private Image validateImageExist(User user) {
        Image optionalImage = imageRepository.findByUserId(user);
        if (optionalImage == null) {
            throw new UserImageNotExistsException("User Image does not exist");
        }

        return optionalImage;
    }
}
