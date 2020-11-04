/*
 * Copyright (c) 2008-2020
 * LANIT
 * All rights reserved.
 *
 * This product and related documentation are protected by copyright and
 * distributed under licenses restricting its use, copying, distribution, and
 * decompilation. No part of this product or related documentation may be
 * reproduced in any form by any means without prior written authorization of
 * LANIT and its licensors, if any.
 *
 * $
 */
package ru.MTUCI.adapter.restservice.controller;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ru.MTUCI.adapter.restservice.dto.SignUpFormDto;
import ru.MTUCI.app.employee.EmployeeRegistrationException;
import ru.MTUCI.app.employee.EmployeeService;
import ru.MTUCI.domain.Employee;

import javax.servlet.ServletContext;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.regex.Pattern;

@RestController
@RequestMapping("/hr-rest/employees")
public class EmployeeController {
    private static final String VALID_EMAIL = "[a-zA-Zа-яА-Я0-9-.]+@[a-zA-Zа-яА-Я0-9-.]+";

    private EmployeeService employeeService;
    private ServletContext servletContext;

    public EmployeeController(EmployeeService employeeService, ServletContext servletContext) {
        this.employeeService = employeeService;
        this.servletContext = servletContext;
    }

    @PostMapping("/current/update-email")
    public ResponseEntity<String> updateEmail(@RequestAttribute String currentUser, @RequestBody String email) {
        if (!email.matches(VALID_EMAIL)) {
            return ResponseEntity.badRequest().body("Email is not correct. Valid symbols: a-z,A-Z,а-я,А-Я" +
                "0-9,'.','-',@");
        } else {
            Employee employee = employeeService.findByLogin(currentUser);
            employee.setEmail(email);
            employeeService.save(employee);
            return ResponseEntity.ok("Email changed!");
        }
    }

    @PostMapping()
    @PreAuthorize("hasRole('OMNI') or hasRole('ADMIN')")
    public ResponseEntity<String> createEmployee(@RequestBody SignUpFormDto signUpRequest) {
        try {
            employeeService.createEmployee(
                signUpRequest.getLogin(),
                signUpRequest.getFirstName(),
                signUpRequest.getPatronymic(),
                signUpRequest.getLastName(),
                signUpRequest.getPassword(),
                signUpRequest.getEmail(),
                signUpRequest.getRoles());
        } catch (EmployeeRegistrationException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }

        return ResponseEntity.ok("Employee registered successfully!");
    }

    @GetMapping("/{login}")
    public Employee findEmployee(@PathVariable String login) {
        return employeeService.findByLogin(login);
    }

    @GetMapping()
    @PreAuthorize("hasRole('OMNI') or hasRole('ADMIN')")
    public List<Employee> getAll() {
        return employeeService.getAll();
    }

    @GetMapping("/{employeeLogin}/fullName")
    @PreAuthorize("hasRole('OMNI') or hasRole('ADMIN') or hasRole('HR') or hasRole('USER')")
    public String getEmployeeFullNameByLogin(@PathVariable("employeeLogin") String employeeLogin) {
        return employeeService.getEmployeeFullNameByLogin(employeeLogin);
    }

    @PostMapping("/current/avatar")
    public ResponseEntity<byte[]> getAvatar(@RequestAttribute String currentUser) {
        Path avatarPath = Paths.get("hr-jedi-framework", "target", "classes", "images", currentUser + ".png");
        try {
            return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(servletContext.getMimeType(avatarPath.toAbsolutePath().toString())))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + avatarPath.getFileName() + "\"")
                .body(Files.readAllBytes(avatarPath));
        } catch (IOException e) {
            return ResponseEntity.ok().body(null);
        }
    }

}
