package com.clinical.management.controllers;

import com.clinical.management.exceptions.ResourceNotFoundException;
import com.clinical.management.model.User;
import com.clinical.management.repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

@SpringBootApplication
@Controller
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/all")
    public String getAllUUsers(Model model) {
        Iterable<User> users = userRepository.findAll();
        model.addAttribute("users", users);
        return "users";
    }
    @GetMapping("/add")
    public String createUser(Model model) {
        return "adduser";
    }
    @PostMapping("/add")
    public String createEmployee(@RequestParam String firstName,
                                 @RequestParam String lastName, @RequestParam String emailId, Model model) {
        User user = new User(firstName,lastName,emailId);
        userRepository.save(user);
        return "redirect:/all";
    }

    @GetMapping("/employees/{id}")
    public ResponseEntity<User> getEmployeeById(@PathVariable(value = "id") Long userId)
            throws ResourceNotFoundException {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found for this id :: " + userId));
        return ResponseEntity.ok().body(user);
    }



    @PutMapping("/employees/{id}")
    public ResponseEntity<User> updateEmployee(@PathVariable(value = "id") Long userId,
                                               @Valid @RequestBody User userDetails) throws ResourceNotFoundException {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found for this id :: " + userId));

        user.setEmailId(userDetails.getEmailId());
        user.setLastName(userDetails.getLastName());
        user.setFirstName(userDetails.getFirstName());
        final User updatedUser = userRepository.save(user);
        return ResponseEntity.ok(updatedUser);
    }

    @DeleteMapping("/employees/{id}")
    public Map<String, Boolean> deleteEmployee(@PathVariable(value = "id") Long employeeId)
            throws ResourceNotFoundException {
        User user = userRepository.findById(employeeId)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found for this id :: " + employeeId));

        userRepository.delete(user);
        Map<String, Boolean> response = new HashMap<>();
        response.put("deleted", Boolean.TRUE);
        return response;
    }
}