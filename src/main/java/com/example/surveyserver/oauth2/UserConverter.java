package com.example.surveyserver.oauth2;

import com.example.surveyserver.model.Delegate;
import com.example.surveyserver.model.User;
import com.example.surveyserver.service.DelegateService;
import com.example.surveyserver.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.provider.token.DefaultUserAuthenticationConverter;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class UserConverter extends DefaultUserAuthenticationConverter {

    @Autowired
    private UserService userService;

    @Autowired
    private DelegateService delegateService;

    @Override
    public Authentication extractAuthentication(Map<String, ?> map) {
        String employeeId = (String) map.get("employeeId");
        String displayName = (String) map.get("displayName");
        String email = (String) map.get("email");
        User dbuser = userService.findEmployeeId(employeeId);
        if (dbuser == null) {
            dbuser = new User();
            dbuser.setEmployeeId(employeeId);
            dbuser.setDisplayName(displayName);
            dbuser.setEmail(email);
            dbuser = userService.createUser(dbuser);
        }
        Integer myId = dbuser.getId();
        PrincipalUser principalUser = new PrincipalUser(myId, employeeId, displayName, email);
        List<Delegate> delegates = delegateService.getDelegatorsByDelegateId(myId);
        List<Integer> delegatorIds = delegates.stream().map(d -> d.getDelegator().getId()).collect(Collectors.toList());
        delegatorIds.add(myId);
        principalUser.setDelegators(delegatorIds);
        return new UsernamePasswordAuthenticationToken(principalUser, null, null);
    }

}
