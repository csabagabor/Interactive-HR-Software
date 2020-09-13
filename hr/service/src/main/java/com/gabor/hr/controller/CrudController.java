package com.gabor.hr.controller;

import com.gabor.hr.controller.error.ApiErrors;
import com.gabor.hr.model.BaseModel;
import com.gabor.hr.service.CrudService;
import com.gabor.hr.service.dto.BaseDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@Slf4j
public abstract class CrudController<M extends BaseModel, D extends BaseDto,
        Serv extends CrudService<M, D, ?>> extends BaseController {

    @Autowired
    protected Serv service;

    @GetMapping("{id}")
    public ResponseEntity<?> findById(@PathVariable Long id) {
        return new ResponseEntity<>(service.findById(id), HttpStatus.OK);
    }

    @GetMapping("")
    public ResponseEntity<?> findAll() {
        return new ResponseEntity<>(service.findAll(), HttpStatus.OK);
    }

    @PostMapping(value = "")
    public ResponseEntity<?> save(@Valid @RequestBody D obj,
                                  BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return new ResponseEntity<>(new ApiErrors(bindingResult), HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(service.save(obj), HttpStatus.OK);
    }

    @PutMapping(value = "{id}")
    public ResponseEntity<?> update(@PathVariable Long id,
                                    @Valid @RequestBody D obj,
                                    BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return new ResponseEntity<>(new ApiErrors(bindingResult), HttpStatus.BAD_REQUEST);
        } else if (obj.getId() != null && !obj.getId().equals(id)) {
            return error(getHttpRequest(), "object id does not match Path variable", HttpStatus.BAD_REQUEST);
        }

        obj.setId(id);
        return new ResponseEntity<>(service.update(obj), HttpStatus.OK);
    }

    @DeleteMapping(value = "{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        service.delete(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    public abstract HttpServletRequest getHttpRequest();

}
