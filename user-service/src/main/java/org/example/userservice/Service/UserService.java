package org.example.userservice.Service;
import jakarta.transaction.Transactional;
import org.example.userservice.Entity.RefreshTokenEntity;
import org.example.userservice.Entity.UserEntity;
import org.example.userservice.Enums.Role;
import org.example.userservice.Exception.*;
import org.example.userservice.Mapper.UserMapper;
import org.example.userservice.Repository.RefreshTokenRepo;
import org.example.userservice.Repository.UserRepo;
import org.example.userservice.Request.*;
import org.example.userservice.Response.AuthResponse;
import org.example.userservice.Response.OAuth2UserResponse;
import org.example.userservice.Response.SellerResponse;
import org.example.userservice.Response.UserResponse;
import org.example.userservice.Security.JwtUtils;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.time.Instant;
import java.util.UUID;

@Service
public class UserService {

    private final UserRepo userRepo;
    private final PasswordEncoder passwordEncoder;
    private final   JwtUtils jwtUtils;
    private final   AuthenticationManager authenticationManager;
    private final SellerProfileService sellerService;
    private final RefreshTokenRepo tokenRepo;

    public UserService(UserRepo userRepo, PasswordEncoder passwordEncoder, JwtUtils jwtUtils, AuthenticationManager authenticationManager, SellerProfileService sellerService, RefreshTokenRepo tokenRepo) {
        this.userRepo = userRepo;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtils = jwtUtils;
        this.authenticationManager = authenticationManager;
        this.sellerService = sellerService;
        this.tokenRepo = tokenRepo;
    }

    @Transactional
    public AuthResponse register(CreateUserRequest createUserRequest){
        userRepo.findByEmail(createUserRequest.getEmail()).ifPresent((x)-> {
            throw new EmailAlreadyExistsException("User with email " + createUserRequest.getEmail() + " already exists");
        });

        UserEntity userEntity = new UserEntity();
        userEntity.setEmail(createUserRequest.getEmail());
        userEntity.setLastName(createUserRequest.getLastName());
        userEntity.setFirstName(createUserRequest.getFirstName());
        userEntity.setPassword(passwordEncoder.encode(createUserRequest.getPassword()));
        userEntity.setRole(Role.BUYER);
        userEntity.setCreatedAt(Instant.now());
        userEntity.setUpdatedAt(Instant.now());
        userRepo.save(userEntity);

        String token = jwtUtils.generateTokenFromId(userEntity.getId(),Role.BUYER.name());
        String refreshToken = jwtUtils.generateRefreshTokenFromId(userEntity.getId());

        RefreshTokenEntity refreshTokenEntity = new RefreshTokenEntity();
        refreshTokenEntity.setUser(userEntity);
        refreshTokenEntity.setToken(refreshToken);
        refreshTokenEntity.setRevoked(false);
        tokenRepo.save(refreshTokenEntity);

        return new AuthResponse(token,
                refreshToken,
                createUserRequest.getEmail(),
                userEntity.getRole().name()
        );
    }

    public UserResponse getProfile(Long id){
        return userRepo.findById(id)
                .map(x->UserMapper.entityToUser(x))
                .orElseThrow(()->new NotFoundById("user not found by id: " + id));
    }

    @Transactional
    public AuthResponse login(UserRequest request){
        var user = userRepo.findByEmail(request.getEmail())
                .orElseThrow(()->new UserNotFoundByEmail("user not found by email: " + request.getEmail()));
        UsernamePasswordAuthenticationToken authToken =
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword());
        Authentication authentication = authenticationManager.authenticate(authToken);

        tokenRepo.revokeAllByUserId(user.getId());

        String token = jwtUtils.generateJwtToken(authentication);
        String refreshToken = jwtUtils.generateRefreshToken(authentication);

        RefreshTokenEntity refreshTokenEntity = new RefreshTokenEntity();
        refreshTokenEntity.setUser(user);
        refreshTokenEntity.setToken(refreshToken);
        refreshTokenEntity.setRevoked(false);
        tokenRepo.save(refreshTokenEntity);

        return new AuthResponse(token,refreshToken,request.getEmail(),user.getRole().name());
    }

    @Transactional
    public UserResponse updateBasicProfile(UpdateUserRequest userRequest,Long id) {
        var user = userRepo.findById(id)
                .orElseThrow(()->new NotFoundById("user not found by id:" + id));
        if (userRequest.getFirstName()!=null) {
            user.setFirstName(userRequest.getFirstName());
            user.setUpdatedAt(Instant.now());
        }
        if (userRequest.getLastName()!=null) {
            user.setLastName(userRequest.getLastName());
            user.setUpdatedAt(Instant.now());
        }
        userRepo.save(user);
        return UserMapper.entityToUser(user);
    }

    @Transactional
    public AuthResponse becomeSeller(SellerRequest request, Long id){
        UserEntity userEntity =userRepo.findById(id).orElseThrow(()->new NotFoundById("user not found by id: " + id));
        if (userEntity.getSellerProfile()!=null){
            throw new SellerProfileExists("user with id: " + id + " already exists SellerProfile");
        }
        userEntity.setRole(Role.SELLER);
        userEntity.setUpdatedAt(Instant.now());
        var seller = sellerService.createSellerProfile(request,userEntity);

        String token = jwtUtils.generateTokenFromId(userEntity.getId(),userEntity.getRole().name());
        String refreshToken = jwtUtils.generateRefreshTokenFromId(userEntity.getId());
        return  new AuthResponse(token,
                refreshToken,
                userEntity.getEmail(),
                userEntity.getRole().name()
        );
    }

    @Transactional
    public void deleteProfile(Long id){
        var userEntity = userRepo.findById(id)
                .orElseThrow(()->new NotFoundById("user not found by id: " + id));
        userRepo.delete(userEntity);
    }

    @Transactional
    public AuthResponse refreshAccessToken(RefreshTokenRequest request) {
        String refreshToken = request.getRefreshToken();
        if (refreshToken == null || refreshToken.isEmpty()) {
            throw new InvalidRefreshTokenException("Invalid refresh token");
        }

        if (!jwtUtils.validateRefreshToken(refreshToken)) {
            throw new InvalidRefreshTokenException("Invalid or expired refresh token");
        }

        var tokenEntity = tokenRepo.findByToken(refreshToken)
                .orElseThrow(() -> new InvalidRefreshTokenException("Invalid refresh token"));

        if (tokenEntity.isRevoked()) {
            throw new InvalidRefreshTokenException("Invalid refresh token");
        }

        String id = jwtUtils.getIdFromJWTToken(refreshToken,"REFRESH");
        if (id == null) {
            throw new InvalidRefreshTokenException("Invalid refresh token");
        }

        var user = userRepo.findById(Long.parseLong(id))
                .orElseThrow(() -> new NotFoundById("User not found by id: " + id));

        tokenEntity.setRevoked(true);
        tokenRepo.save(tokenEntity);

        String newAccessToken = jwtUtils.generateTokenFromId(Long.parseLong(id),user.getRole().name());
        String newRefreshToken = jwtUtils.generateRefreshTokenFromId(Long.parseLong(id));

        RefreshTokenEntity refreshTokenEntity = new RefreshTokenEntity();
        refreshTokenEntity.setUser(user);
        refreshTokenEntity.setToken(newRefreshToken);
        refreshTokenEntity.setRevoked(false);
        tokenRepo.save(refreshTokenEntity);

        return new AuthResponse(newAccessToken,newRefreshToken,user.getEmail(), user.getRole().name());
    }


    @Transactional
    public void logout(RefreshTokenRequest request){
        String refreshToken = request.getRefreshToken();

        if (refreshToken == null || refreshToken.isEmpty()) {
            throw new InvalidRefreshTokenException("Invalid refresh token");
        }

        RefreshTokenEntity tokenEntity = tokenRepo.findByToken(refreshToken)
                .orElseThrow(() -> new InvalidRefreshTokenException("Invalid refresh token"));

        if (tokenEntity.isRevoked()) {
            throw new InvalidRefreshTokenException("Invalid refresh token");
        }

        tokenRepo.deleteAllByUserId(tokenEntity.getUser().getId());
    }

    public UserResponse getUserById(Long userId) {
        return userRepo.findById(userId)
                .map(x->UserMapper.entityToUser(x))
                .orElseThrow(()->new NotFoundById("user not found by id: " + userId));
    }

    @Transactional
    public OAuth2UserResponse findOrCreate(OAuth2UserRequest request)
    {
        return userRepo.findByEmail(request.getEmail())
                .map(user -> new OAuth2UserResponse(user.getId(),user.getRole().name()))
                .orElseGet(() -> {
                    UserEntity newUser = new UserEntity();
                    newUser.setEmail(request.getEmail());
                    newUser.setFirstName(request.getName());
                    newUser.setRole(Role.BUYER);
                    newUser.setPassword(UUID.randomUUID().toString());
                    userRepo.save(newUser);
                    return new OAuth2UserResponse(newUser.getId(),newUser.getRole().name());
                });
    }
}
