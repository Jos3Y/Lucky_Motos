package com.motos.jass.sistemalucky.share.service;

import com.motos.jass.sistemalucky.share.repository.BaseRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Transactional
public abstract class BaseServiceImpl<T,ID> implements BaseService<T,ID> {

    protected final BaseRepository<T, ID> repository;

    public BaseServiceImpl(BaseRepository<T, ID> repository) {
        this.repository = repository;
    }


    @Override
    public List<T> findAll() {
        return repository.findAll();
    }

    @Override
    public Optional<T> findById(ID id) {
        return repository.findById(id);
    }

    @Override
    public T save(T entity) {
        return repository.save(entity);
    }

    @Override
    public T update(ID id, T entity) {
         if(!repository.existsById(id)) {
             throw new RuntimeException("No existe el registro con ID: " + id);

        }
        return repository.save(entity);

    }

    @Override
    public void deleteById(ID id) {
        if (!repository.existsById(id)) {
            throw new RuntimeException("No existe el registro con ID: " + id);
    }
    repository.deleteById(id);
    }
}
