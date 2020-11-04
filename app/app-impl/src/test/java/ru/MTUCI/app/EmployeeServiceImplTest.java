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
package ru.MTUCI.app;

import org.easymock.TestSubject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.unitils.UnitilsBlockJUnit4ClassRunner;
import org.unitils.easymock.annotation.Mock;
import ru.MTUCI.app.employee.EmployeeService;
import ru.MTUCI.app.employee.EmployeeServiceImpl;
import ru.MTUCI.domain.Employee;
import ru.MTUCI.domain.security.Role;
import ru.MTUCI.domain.security.RoleName;
import ru.MTUCI.hibernate.employee.EmployeeRepository;
import ru.MTUCI.hibernate.employee.RoleRepository;

import java.util.*;

import static java.util.Arrays.asList;
import static java.util.Collections.singleton;
import static org.apache.commons.collections4.SetUtils.hashSet;
import static org.easymock.EasyMock.expect;
import static org.junit.Assert.assertEquals;
import static org.unitils.easymock.EasyMockUnitils.replay;
import static ru.MTUCI.domain.security.RoleName.*;

@RunWith(UnitilsBlockJUnit4ClassRunner.class)
public class EmployeeServiceImplTest {
    private static final String ADMIN_ADMIN = "Admin Admin";
    private static final String USER_USER = "User User";
    private static final String HR_HR = "Hr Hr";
    private static final String IVAN_IVANOV = "Иван Иванович Иванов";

    @Mock
    EmployeeRepository employeeRepository;
    @Mock
    PasswordEncoder passwordEncoder;
    @Mock
    RoleRepository roleRepository;
    @TestSubject
    EmployeeService employeeService;

    @Before
    public void setUp() {
        employeeService = new EmployeeServiceImpl(employeeRepository, "HeadOfHrLogin", passwordEncoder, roleRepository);
    }

    @Test
    public void getNumberOfAdmins() {
        expect(employeeRepository.findAll())
            .andReturn(asList(
                setRoles(singleton(role(ROLE_USER))),
                setRoles(asList(role(ROLE_USER), role(ROLE_HR))),
                setRoles(asList(role(ROLE_USER), role(ROLE_ADMIN))),
                setRoles(singleton(role(ROLE_ADMIN)))

            ));
        replay();

        long numberOfAdmins = employeeService.getNumberOfAdmins();

        assertEquals(2, numberOfAdmins);
    }

    @Test
    public void getEmployeesAndTheirRoles() {
        expect(employeeRepository.findAll()).
            andStubReturn(asList(
                fullNameAndRoles(ADMIN_ADMIN, asList(role(ROLE_USER), role(ROLE_ADMIN), role(ROLE_HR))),
                fullNameAndRoles(USER_USER, singleton(role(ROLE_USER))),
                fullNameAndRoles(HR_HR, singleton(role(ROLE_HR))),
                fullNameAndRoles(IVAN_IVANOV, asList(role(ROLE_USER), role(ROLE_HR)))
            ));
        replay();

        Map<String, Set<RoleName>> EmployeesAndTheirRoles = employeeService.getEmployeesAndTheirRoles();

        assertEquals(new HashMap<String, Set<RoleName>>() {{
            put(ADMIN_ADMIN, hashSet(ROLE_ADMIN, ROLE_USER, ROLE_HR));
            put(USER_USER, hashSet(ROLE_USER));
            put(HR_HR, hashSet(ROLE_HR));
            put(IVAN_IVANOV, hashSet(ROLE_USER, ROLE_HR));
        }}, EmployeesAndTheirRoles);
    }

    @Test
    public void getMapCountOfEmployeesByRole() {
        expect(employeeRepository.findAll()).
            andStubReturn(asList(
                setRoles(singleton(role(ROLE_USER))),
                setRoles(singleton(role(ROLE_HR))),
                setRoles(asList(role(ROLE_USER), role(ROLE_HR))),
                setRoles(asList(role(ROLE_USER), role(ROLE_ADMIN))),
                setRoles(singleton(role(ROLE_USER)))
            ));
        expect(roleRepository.findAll()).
            andReturn(asList(
                role(ROLE_ADMIN),
                role(ROLE_HR),
                role(ROLE_USER)
            ));
        replay();

        Map<RoleName, Long> mapCountEmployeesByRole = employeeService.getMapCountOfEmployeesByRole();

        assertEquals(new HashMap<RoleName, Long>() {{
            put(ROLE_ADMIN, 1L);
            put(ROLE_HR, 2L);
            put(ROLE_USER, 4L);
        }}, mapCountEmployeesByRole);
    }

    // ===================================================================================================================
    // = Implementation
    // ===================================================================================================================

    private Employee setRoles(Collection<Role> roles) {
        Employee user = new Employee();
        user.setRoles(new HashSet<>(roles));

        return user;
    }

    private Role role(RoleName name) {
        Role role = new Role();
        role.setName(name);

        return role;
    }

    private Employee fullNameAndRoles(String fullName, Collection<Role> roles) {
        Employee user = setRoles(roles);
        user.setFullName(fullName);

        return user;
    }
}
