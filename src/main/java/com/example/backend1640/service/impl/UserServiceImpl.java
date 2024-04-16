package com.example.backend1640.service.impl;

import com.example.backend1640.constants.UserRoleEnum;
import com.example.backend1640.dto.CreateUserDTO;
import com.example.backend1640.dto.EmailDetails;
import com.example.backend1640.dto.LoginDTO;
import com.example.backend1640.dto.LoginRequestDTO;
import com.example.backend1640.dto.ReadUserByIdDTO;
import com.example.backend1640.dto.ReadUserDTO;
import com.example.backend1640.dto.UpdateUserDTO;
import com.example.backend1640.dto.UserDTO;
import com.example.backend1640.entity.Contribution;
import com.example.backend1640.entity.Faculty;
import com.example.backend1640.entity.Image;
import com.example.backend1640.entity.User;
import com.example.backend1640.exception.FacultyNotExistsException;
import com.example.backend1640.exception.MissingStudentFacultyException;
import com.example.backend1640.exception.UserAlreadyExistsException;
import com.example.backend1640.exception.UserNotExistsException;
import com.example.backend1640.exception.WrongOldPasswordException;
import com.example.backend1640.repository.ContributionRepository;
import com.example.backend1640.repository.FacultyRepository;
import com.example.backend1640.repository.ImageRepository;
import com.example.backend1640.repository.UserRepository;
import com.example.backend1640.service.UserService;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

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

    public static String alphaNumericString(int len) {
        String AlphaNumericString = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789abcdefghijklmnopqrstuvxyz";
        Random random = new Random();

        StringBuilder sb = new StringBuilder(len);
        for (int i = 0; i < len; i++) {
            sb.append(AlphaNumericString.charAt(random.nextInt(AlphaNumericString.length())));
        }

        return sb.toString();
    }

    @Override
    public UserDTO createUser(CreateUserDTO userDTO) {
        if (userDTO.getUserRole() == UserRoleEnum.STUDENT && userDTO.getFaculty() == null) {
            throw new MissingStudentFacultyException("Student must have a faculty");
        }

        validateUserExists(userDTO.getEmail());
        User user = new User();
        String generatedPassword = alphaNumericString(8);
        String encodePassword = passwordEncoder.encode(generatedPassword);
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
                            .messageBody("Your account has been CREATED SUCCESSFULLY with email: " + savedUser.getEmail() + "\n" +
                                    "Password: " + generatedPassword + "\n" +
                                    "Name: " + userDTO.getName() + "\n" +
                                    "Role: " + userDTO.getUserRole() + "\n" +
                                    "Faculty: " + user.getFacultyId().getFacultyName() + "\n" + "\n" +
                                    "Please CHANGE YOUR PASSWORD after login" + "\n"
                            )
                            .recipient(user.getEmail())
                            .subject("STUDENT REGISTRATION SUCCESS")
                            .build());
        } else {
            rabbitTemplate.convertAndSend(exchange,
                    userRountingKey,
                    EmailDetails.builder()
                            .messageBody("Your account have been CREATED SUCCESSFULLY with email: " + savedUser.getEmail() + "\n" +
                                    "Password: " + generatedPassword + "\n" +
                                    "Name: " + userDTO.getName() + "\n" +
                                    "Role: " + userDTO.getUserRole() + "\n" + "\n" +
                                    "Please CHANGE YOUR PASSWORD after login" + "\n"
                            )
                            .recipient(user.getEmail())
                            .subject("USER REGISTRATION SUCCESS")
                            .build());
        }

        return responseUserDTO;
    }

    @Override
    @Transactional
    public LoginDTO loginUser(LoginRequestDTO loginRequestDTO) {
        User user = validateUserNotExists(loginRequestDTO.getEmail());
        boolean isCorrectPassword = passwordEncoder.matches(loginRequestDTO.getPassword(), user.getPassword());

        Image image = imageRepository.findByUserId(user);

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

        if (image != null) {
            loginDTO.setImageId(image.getId());
        }

        return loginDTO;
    }

    @Override
    @Transactional
    public List<ReadUserDTO> findAll() {
        List<User> users = userRepository.findAll();
        List<ReadUserDTO> readUserDTOS = new ArrayList<>();

        for (User user : users) {
            ReadUserDTO readUserDTO = new ReadUserDTO();
            BeanUtils.copyProperties(user, readUserDTO);

            Image optionalImage = imageRepository.findByUserId(user);
            if (optionalImage != null) {
                readUserDTO.setImageId(optionalImage.getId());
            }

            if (user.getUserRole() == UserRoleEnum.STUDENT) {
                readUserDTO.setFaculty(user.getFacultyId().getFacultyName());
            }
            readUserDTOS.add(readUserDTO);
        }

        readUserDTOS.sort(Comparator.comparing(ReadUserDTO::getUserRole));

        return readUserDTOS;
    }

    @Override
    @Transactional
    public ReadUserByIdDTO findById(long id) {
        User user = validateUserExists(id);
        ReadUserByIdDTO readUserByIdDTO = new ReadUserByIdDTO();
        BeanUtils.copyProperties(user, readUserByIdDTO);

        Image optionalImage = imageRepository.findByUserId(user);
        if (optionalImage != null) {
            readUserByIdDTO.setImageId(optionalImage.getId());
        }

        if (user.getUserRole() == UserRoleEnum.STUDENT) {
            readUserByIdDTO.setFaculty(user.getFacultyId().getFacultyName());
            readUserByIdDTO.setFacultyId(user.getFacultyId().getId());
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

    private Faculty validateFacultyExists(String facultyName) {
        Optional<Faculty> optionalFaculty = facultyRepository.findByFacultyName(facultyName);

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
}
