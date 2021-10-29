package com.agenda.api.controller;

import com.agenda.api.model.Contato;
import com.agenda.api.repository.ContatoRepository;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.Part;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/contato")
@CrossOrigin("*")
@RequiredArgsConstructor
public class ContatoController {
    
    private final ContatoRepository contatoRepository;
    
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Contato salvar(@RequestBody @Valid Contato contato) {
        return contatoRepository.save(contato);
    }
    
    @GetMapping()
    public Page<Contato> todos(@RequestParam(name="page", defaultValue = "0") Integer page, 
                               @RequestParam(name = "size", defaultValue = "10") Integer size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Order.asc("nome")));
        return contatoRepository.findAll(pageable);
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
    
    @PatchMapping(path = "/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void favorito(@PathVariable Long id) {
        contatoRepository.findById(id)
            .map(cliente -> {
                cliente.setFavorito(!cliente.getFavorito());
                contatoRepository.save(cliente);
                return Void.TYPE;
            }).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }
    
    @PutMapping("{id}/foto")
    public byte[] addFoto(@PathVariable Long id, @RequestParam("foto") Part arquivo) {
        Optional<Contato> contato = contatoRepository.findById(id);
        if(!contato.isEmpty()) {
            Contato contatoFoto = contato.get();
            try {
                try (InputStream is = arquivo.getInputStream()) {
                    byte[] bytes = new byte[(int) arquivo.getSize()];
                    IOUtils.readFully(is, bytes);
                    contatoFoto.setFoto(bytes);
                    contatoRepository.save(contatoFoto);
                    return bytes;
                }
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        }
        return null;
    }
    
}


