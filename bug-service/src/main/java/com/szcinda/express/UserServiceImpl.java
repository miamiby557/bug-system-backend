package com.szcinda.express;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.szcinda.express.dto.UserIdentity;
import com.szcinda.express.params.PageResult;
import com.szcinda.express.params.QueryUserParams;
import com.szcinda.express.persistence.User;
import com.szcinda.express.persistence.UserRepository;
import com.szcinda.express.persistence.UserType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final SnowFlakeFactory snowFlakeFactory;

    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.snowFlakeFactory = SnowFlakeFactory.getInstance();
    }

    public PageResult<User> query(QueryUserParams params) {
        Specification<User> specification = ((root, criteriaQuery, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (!StringUtils.isEmpty(params.getAccount())) {
                Predicate likeAccount = criteriaBuilder.like(root.get("account").as(String.class), params.getAccount() + "%");
                predicates.add(likeAccount);
            }
            if (!StringUtils.isEmpty(params.getName())) {
                Predicate likeName = criteriaBuilder.like(root.get("name").as(String.class), params.getName() + "%");
                predicates.add(likeName);
            }
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        });
        Pageable pageable = new PageRequest(params.getPage() - 1, params.getPageSize());
        Page<User> userPage = userRepository.findAll(specification, pageable);
        return PageResult.of(userPage.getContent(), params.getPage(), params.getPageSize(), userPage.getTotalElements());
    }

    @Override
    public UserIdentity getUserIdentity(String account, String password) {
        User user = userRepository.findFirstByAccount(account);
        Assert.notNull(user, String.format("没有此%s的用户", account));
        boolean passwordIsTrue = passwordEncoder.matches(password, user.getPassword());
        Assert.isTrue(passwordIsTrue, "密码不正确");
        return new UserIdentity(user.getId(), user.getAccount(), user.getPassword(),user.getName(),user.getUserType());
    }

    @Override
    public void delete(String userId) {
        userRepository.deleteByUserId(userId);
    }

    @Override
    public String getToken(String userId, String password) {
        return JWT.create().withAudience(userId)
                .sign(Algorithm.HMAC256(password));
    }

    @Override
    public User findUserById(String userId) {
        return userRepository.findFirstById(userId);
    }

    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    public void init() {
        User user = new User();
        user.setId(snowFlakeFactory.nextId());
        user.setAccount("admin");
        user.setName("管理者");
        String password = passwordEncoder.encode("admin");
        user.setPassword(password);
        user.setUserType(UserType.ADMIN);
        userRepository.save(user);
    }
}
