package org.taxi.service;

import com.querydsl.core.types.Predicate;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.taxi.dto.UserCreateEditDto;
import org.taxi.dto.UserReadDto;
import org.taxi.dto.filters.UserFilter;
import org.taxi.entity.User;
import org.taxi.mapper.UserCreateEditMapper;
import org.taxi.mapper.UserReadMapper;
import org.taxi.repository.UserRepository;
import org.taxi.util.QueryDslPredicate;

import java.util.List;
import java.util.Optional;

import static org.taxi.entity.QUser.user;


@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {

    private final UserRepository userRepository;
    private final UserReadMapper userReadMapper;
    private final UserCreateEditMapper userCreateEditMapper;

    public Page<UserReadDto> findAll(UserFilter filter, Pageable pageable) {
        Predicate predicate = QueryDslPredicate.builder()
                .add(filter.getFirstname(), user.firstName::containsIgnoreCase)
                .add(filter.getLastname(), user.lastName::containsIgnoreCase)
                .add(filter.getEmail(), user.email::containsIgnoreCase)
                .add(filter.getPhone(), user.phone::contains)
                .add(filter.getRole(), user.role::eq)
                .buildOr();

        return userRepository.findAll(predicate, pageable)
                .map(userReadMapper::map);
    }

    public List<UserReadDto> findAll() {
        return userRepository.findAll().stream()
                .map(userReadMapper::map)
                .toList();
    }

    public Optional<UserReadDto> findById(Long id) {
        return userRepository.findById(id)
                .map(userReadMapper::map);
    }

    @Transactional
    public UserReadDto create(UserCreateEditDto userDto) {
        // Проверяем, существует ли уже пользователь с таким email
        if (userRepository.existsByEmail(userDto.getEmail())) {
            throw new IllegalArgumentException("User with email " + userDto.getEmail() + " already exists");
        }

        // Создаем нового пользователя
        return Optional.of(userDto)
                .map(userCreateEditMapper::map)
                .map(userRepository::save)
                .map(userReadMapper::map)
                .orElseThrow();
    }

    @Transactional
    public Optional<UserReadDto> update(Long id, UserCreateEditDto userDto) {
        return userRepository.findById(id)
                .map(entity -> userCreateEditMapper.map(userDto, entity))
                .map(userRepository::saveAndFlush)
                .map(userReadMapper::map);
    }

    @Transactional
    public boolean delete(Long id) {
        return userRepository.findById(id)
                .map(entity -> {
                    userRepository.delete(entity);
                    userRepository.flush();
                    return true;
                })
                .orElse(false);
    }

    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }
}
