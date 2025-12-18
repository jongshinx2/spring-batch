package com.example.springbatch.infrastructure;

import com.example.springbatch.domain.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.infrastructure.item.ItemProcessor;

@Slf4j
public class UserProcessor implements ItemProcessor<User, User> {

    @Override
    public User process(User user) throws Exception {
        // Example processing: Convert the user's name to uppercase
        user.setEmail(user.getEmail().toLowerCase());
        user.setProcessed(true);

        log.info("Processing user: {}", user.getName());
        return user;
    }
}
