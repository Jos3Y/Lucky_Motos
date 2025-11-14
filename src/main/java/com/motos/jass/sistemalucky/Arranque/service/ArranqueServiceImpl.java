package com.motos.jass.sistemalucky.Arranque.service;

import com.motos.jass.sistemalucky.Arranque.entity.Arranque;
import com.motos.jass.sistemalucky.Arranque.repository.ArranqueRepository;
import com.motos.jass.sistemalucky.share.service.BaseServiceImpl;

public class ArranqueServiceImpl extends BaseServiceImpl<Arranque, Long>
        implements ArranqueService
{
    public ArranqueServiceImpl(ArranqueRepository repository) {
        super(repository); // 👈 Aquí pasas el repository a la clase base
    }



}
