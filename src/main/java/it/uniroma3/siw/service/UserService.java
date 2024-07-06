package it.uniroma3.siw.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.uniroma3.siw.model.Booking;
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
    
    public User getUser(Long id) {
        Optional<User> result = this.userRepository.findById(id);
        return result.orElse(null);
    }
	
    public List<User> getAllUsers() {
        List<User> result = new ArrayList<>();
        Iterable<User> iterable = this.userRepository.findAll();
        for(User user : iterable)
            result.add(user);
        return result;
    }
/*    
    public List<User> getAllUsersDipendenti() {
        List<User> result = new ArrayList<>();
        Iterable<User> iterable = this.utenteRepository.findAll();
        String s1 = new String("ADMIN");
        for(User user : iterable) {
        	if (user.getCredentials().getRole().equals(s1))
        		 result.add(user);
        }
        return result;
    } */
    
    @Transactional
    public User getUsername(String logina) {
        Optional<User> result = this.userRepository.findByUsername(logina);
        return result.orElse(null);
    }
    
	public boolean alreadyExists(User u) {
		return userRepository.existsByNameAndSurname(u.getName(), u.getSurname());
	}
	
	@Transactional
	public void addBooking(User u, Booking booking) {
		u.getBookings().add(booking);
		this.userRepository.save(u);
	}
	
	@Transactional
	public void deleteBooking(User u, Booking booking) {
		u.getBookings().remove(booking);
		this.userRepository.save(u);
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
}
