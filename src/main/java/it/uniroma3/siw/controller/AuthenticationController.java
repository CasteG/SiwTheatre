package it.uniroma3.siw.controller;

import static it.uniroma3.siw.model.User.DIR_FOLDER_IMG;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import it.uniroma3.siw.controller.validator.CredentialsValidator;
import it.uniroma3.siw.controller.validator.UserValidator;
import it.uniroma3.siw.model.Credentials;
import it.uniroma3.siw.model.User;
import it.uniroma3.siw.service.CredentialsService;
import it.uniroma3.siw.service.UserService;
import it.uniroma3.siw.session.SessionData;
import it.uniroma3.siw.utility.FileStore;
import jakarta.validation.Valid;

@Controller
public class AuthenticationController {
	
	@Autowired
	private CredentialsService credentialsService;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private CredentialsValidator credentialsValidator;
	
	@Autowired
	private UserValidator userValidator;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Autowired
	SessionData sessionData;
	
	@GetMapping("/register")
	//@RequestMapping(value="/register", method=RequestMethod.GET)
	public String showRegisterForm(Model model) {
		model.addAttribute("user", new User());
		model.addAttribute("credentials", new Credentials());
		return "authentication/formRegisterUser";
	}
	
	@GetMapping("/login")
	//@RequestMapping(value="/login", method=RequestMethod.GET)
	public String showLoginForm(Model model) {
		return "authentication/formlogin";
	}
	
	@GetMapping("/logout")
	//@RequestMapping(value="/logout", method=RequestMethod.GET)
	public String logout(Model model) {
		return "index";
	}
	
//	@GetMapping(value = "/") 
//	public String index(Model model) {
//		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//		if (authentication instanceof AnonymousAuthenticationToken) {
//	        return "index.html";
//		}
//		else {		
//			UserDetails userDetails = (UserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//			Credentials credentials = credentialsService.getCredentials(userDetails.getUsername());
//			if (credentials.getRole().equals(Credentials.ADMIN_ROLE)) {
//				return "redirect:/admin/dipendente";
//			}
//		}
//        return "index.html";
//	}
	
	@GetMapping("/success")
	public String defaultAfterLogin(Model model) {
		UserDetails userDetails = (UserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		Credentials credentials = credentialsService.getCredentials(userDetails.getUsername());
//		if(credentials.getRole().equals(Credentials.ADMIN_ROLE)) {
//			return "redirect:/admin/dipendenti";
//		}
		
		return this.profileUser(model);
	}
	
	
	@PostMapping(value= {"/register"})
	public String registerUser(@Valid @ModelAttribute("user") User user,
								BindingResult utenteBindingResult, 
							   @Valid @ModelAttribute("credentials") Credentials credentials,
							    BindingResult credentialsBindingResult,
							    Model model) {
		
        // validazione user e credenziali
        this.userValidator.validate(user, utenteBindingResult);
        this.credentialsValidator.validate(credentials, credentialsBindingResult);
        
		if(!utenteBindingResult.hasErrors() && !credentialsBindingResult.hasErrors()) {
			user.setImg("icon-default-user.png");
			credentials.setUser(user);
			credentialsService.saveCredentials(credentials);
			return "authentication/registrationSuccessful";
		}
		return "authentication/formRegisterUser";
	}
	
	@RequestMapping(value={"login/oauth2/user"}, method = RequestMethod.GET)
	public String oAuth2Successful(Model model){
		User loggedUser = this.sessionData.getLoggedUser();
		model.addAttribute("user",loggedUser);
		return "index.html";
	}
	
	/* PROFILE */
	@GetMapping("/profile")
	public String profileUser(Model model) {
		UserDetails userDetails = (UserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		Credentials credentials = credentialsService.getCredentials(userDetails.getUsername());
		User user = userService.getUser(credentials.getUser().getId());
		model.addAttribute("user", user);
		model.addAttribute("credentials", credentials);
		return "authentication/profile";
	}
	
	@PostMapping("/changeUserAndPass/{idCred}")
	public String changeUserAndPass(@Valid @ModelAttribute("credentials") Credentials credentials,
									BindingResult credentialsBindingResult,
									@PathVariable("idCred") Long id,
									@RequestParam(name = "confirmPass") String pass,								
									Model model) {
		
		credentials.setUsername("defaultUsernameForVa");
		credentialsValidator.validate(credentials, credentialsBindingResult);
		
		if(!credentials.getPassword().equals(pass)) {
			credentialsBindingResult.addError(new ObjectError("notMatchConfirmPassword", "Le password non coincidono"));
		}
		
		Credentials c = credentialsService.getCredentials(id);
		User user = userService.getUser(c.getUser().getId());
		credentials.setUsername(c.getUsername());
		credentials.setId(id);
		
		if(!credentialsBindingResult.hasErrors()) {
			c.setPassword(this.passwordEncoder.encode(credentials.getPassword()));
			credentialsService.save(c);
			model.addAttribute("user", user);
			model.addAttribute("credentials", c);
			model.addAttribute("okChange", true);
			return "authentication/profile";
		}	
		model.addAttribute("user", user);
		model.addAttribute("credentials", credentials);
		model.addAttribute("okChange", false);
		return "authentication/profile";
	}
	
	@PostMapping("/changeImgProfile/{idUser}")
	public String changeImgProfile(@PathVariable("idUser") Long id,
								   @RequestParam("file") MultipartFile file, Model model) {
		User user = userService.getUser(id);
		if(!user.getImg().equals("icon-user-default.png")) {
			FileStore.removeImg(DIR_FOLDER_IMG, user.getImg());
		}
		user.setImg(FileStore.store(file, DIR_FOLDER_IMG));
		userService.save(user);
		return this.profileUser(model);
	}
}
