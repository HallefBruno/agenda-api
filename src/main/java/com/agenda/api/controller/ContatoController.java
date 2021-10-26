package com.agenda.api.controller;

import com.agenda.api.model.Contato;
import com.agenda.api.repository.ContatoRepository;
import java.util.Objects;
import java.util.Optional;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/contato")
@RequiredArgsConstructor
public class ContatoController {
    
    private final ContatoRepository contatoRepository;
    
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Contato salvar(@RequestBody @Valid Contato contato) {
        return contatoRepository.save(contato);
    }
    
    @DeleteMapping(path = "/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void excluir(@PathVariable Long id) {
       contatoRepository.findById(id)
            .map(cliente -> {
                contatoRepository.deleteById(id);
                return Void.TYPE;
            }).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }
    
    @PatchMapping(path = "/{id}/{favorito}")
    @ResponseStatus(HttpStatus.OK)
    public void favorito(@PathVariable Long id, @PathVariable Boolean favorito) {
        contatoRepository.findById(id)
            .map(cliente -> {
                cliente.setFavorito(favorito);
                contatoRepository.save(cliente);
                return Void.TYPE;
            }).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }
    
}
