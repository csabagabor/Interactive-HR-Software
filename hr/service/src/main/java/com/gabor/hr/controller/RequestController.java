package com.gabor.hr.controller;

import com.gabor.hr.model.Request;
import com.gabor.model.model.RoleName;
import com.gabor.hr.model.Status;
import com.gabor.hr.service.RequestService;
import com.gabor.hr.service.UserService;
import com.gabor.hr.service.dto.RequestDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

@RequestMapping("/api/requests")
@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@Slf4j
public class RequestController extends CrudController<Request, RequestDto,
        RequestService> {
    @Autowired
    private HttpServletRequest httpServletRequest;

    @Autowired
    private UserService userService;

    @PutMapping("/accept/{id}")
    public ResponseEntity<?> accept(@PathVariable Long id) {
        service.accept(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping("/reject/{id}")
    public ResponseEntity<?> reject(@PathVariable Long id) {
        service.reject(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/own")
    public ResponseEntity<?> findAllOwn() {
        return new ResponseEntity<>(service.findAllByUser(getCurrentUserEmail()), HttpStatus.OK);
    }

    @GetMapping("/open")
    public ResponseEntity<?> findAllOpen() {
        return new ResponseEntity<>(service.findAllByStatus(Status.OPEN), HttpStatus.OK);
    }

    @GetMapping("/rejected")
    public ResponseEntity<?> findAllRejected() {
        return new ResponseEntity<>(service.findAllByStatus(Status.REJECTED), HttpStatus.OK);
    }

    @GetMapping("/accepted")
    public ResponseEntity<?> findAllAccepted() {
        return new ResponseEntity<>(service.findAllByStatus(Status.ACCEPTED), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<?> save(@Valid @RequestBody RequestDto obj, BindingResult bindingResult) {
        String currentUserEmail = getCurrentUserEmail();

        if ("hidden".equals(obj.getHidden())) { //only used for interactive tutorial
            return new ResponseEntity<>(HttpStatus.OK);
        }

        //only user can create a new request, and only with its own email address
        obj.setUserEmail(currentUserEmail);

        return super.save(obj, bindingResult);
    }

    /*
    User can only delete its own request, otherwise moderator can delete every request
     */
    @Override
    public ResponseEntity<?> delete(@PathVariable Long id) {
        List<String> currentUserAuthorities = getCurrentUserAuthorities();

        //moderator can delete every kind of request
        if (!currentUserAuthorities.contains(RoleName.ROLE_MODERATOR.name())) {
            RequestDto requestDto = service.findById(id);
            String currentUserEmail = getCurrentUserEmail();

            //unauthorized access
            if (!requestDto.getUserEmail().equals(currentUserEmail)) {
                return error(httpServletRequest, "Not Authorized to delete this request", HttpStatus.FORBIDDEN);
            }
        }

        return super.delete(id);
    }

    @Override
    public HttpServletRequest getHttpRequest() {
        return httpServletRequest;
    }
}
