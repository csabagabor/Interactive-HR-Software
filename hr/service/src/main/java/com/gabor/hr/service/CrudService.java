package com.gabor.hr.service;

import com.gabor.hr.exception.ResourceNotFoundException;
import com.gabor.hr.model.BaseModel;
import com.gabor.hr.service.dto.BaseDto;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;

import java.lang.reflect.ParameterizedType;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public abstract class CrudService<M extends BaseModel, D extends BaseDto, Repo extends JpaRepository<M, Long>> {

    private final Class<M> modelType;

    private final Class<D> dtoType;

    @SuppressWarnings("unchecked")
    public CrudService() {
        modelType = (Class<M>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
        dtoType = (Class<D>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[1];
    }

    @Autowired
    protected ModelMapper modelMapper;

    @Autowired
    protected Repo repository;

    public D findById(Long id) {
        Optional<M> byId = repository.findById(id);

        byId.orElseThrow(() -> new ResourceNotFoundException(modelType.getSimpleName(), "id", id));

        return modelMapper.map(byId.get(), dtoType);
    }

    public List<D> findAll() {
        return repository.findAll().stream()
                .map(obj -> modelMapper.map(obj, dtoType))
                .collect(Collectors.toList());
    }

    public D save(D obj) {
        M savedObj = repository.save(modelMapper.map(obj, modelType));
        return modelMapper.map(savedObj, dtoType);
    }

    public D update(D obj) {
        //checks if it exists
        findById(obj.getId());

        M savedObj = repository.save(modelMapper.map(obj, modelType));
        return modelMapper.map(savedObj, dtoType);
    }

    public void delete(Long id) {
        //checks if it exists
        findById(id);

        repository.deleteById(id);
    }
}
