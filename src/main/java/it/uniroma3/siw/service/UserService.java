package it.uniroma3.siw.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.uniroma3.siw.model.User;
import it.uniroma3.siw.oauth.AuthenticationProvider;
import it.uniroma3.siw.repository.UserRepository;

@Service
public class UserService {
	
	@Autowired
	private UserRepository userRepository;
	
    @Transactional
    public User save(User user) {
        return this.userRepository.save(user);
    }
    
    public List<User> getAllUsers() {
        List<User> result = new ArrayList<>();
        Iterable<User> iterable = this.userRepository.findAll();
        for(User user : iterable)
            result.add(user);
        return result;
    }
    
	public boolean alreadyExists(User u) {
		return userRepository.existsByNameAndSurname(u.getName(), u.getSurname());
	}
	
	public void registerNewCustomerAfterOAuthLoginSuccess(String loginName, String fullName, AuthenticationProvider provider) {
        User user = new User();
        if(loginName != null) {
            user.setUsername(loginName);
            user.setName(fullName);
        }
        else{
            user.setUsername(loginName);
        }
        user.setCreationTimestamp(LocalDateTime.now());
        user.setoAuthProvider(provider);
        userRepository.save(user);
    }
	
	public void updateExistingUser(User user,String fullName, AuthenticationProvider provider){
        user.setName(fullName);
        user.setoAuthProvider(provider);
        userRepository.save(user);
    }

	public User findByUsername(String username) {
		return this.userRepository.findByUsername(username);
	}

	public Iterable<User> findAll() {
		return this.userRepository.findAll();
	}

	public User findById(Long id) {
		return this.userRepository.findById(id).get();
	}
}
