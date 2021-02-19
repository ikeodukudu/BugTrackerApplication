package com.stss.backend.Bugtracker.controllers;

import com.stss.backend.Bugtracker.dtos.TicketCreationDto;
import com.stss.backend.Bugtracker.models.Ticket;
import com.stss.backend.Bugtracker.models.User;
import com.stss.backend.Bugtracker.repositories.GetUserIdAndName;
import com.stss.backend.Bugtracker.repositories.TicketRepository;
import com.stss.backend.Bugtracker.services.BugUserDetailsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@CrossOrigin(origins="http://localhost:3000")
public class TicketController {

    final static Logger logger = LoggerFactory.getLogger(TicketController.class);

    @Autowired
    private TicketRepository ticketRepository;

    @Autowired
    private BugUserDetailsService userService;

    @GetMapping("/users/{userId}/tickets")
    public List<Ticket> getAllTickets(@PathVariable long userId) {
        //long userIdResult = Long.parseLong(userId);
        User user = new User();
        user.setUserId(userId);
        List<Ticket> byUserId = ticketRepository.findByUserId(userId);
        return byUserId;
    }

    @GetMapping("/dropdown")
    public List<GetUserIdAndName> getAllUsers() {
        return userService.findUserIdAndName();
    }

    @PostMapping("/users/{userId}/tickets/")
    public ResponseEntity<Void> createTicket(@PathVariable long userId, @RequestBody TicketCreationDto ticketDto) {
        Ticket createdTicket = userService.saveTicket(userId, ticketDto);
        logger.info("Ticket created successfully" + createdTicket);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(createdTicket.getTicketId()).toUri();
        //return ResponseEntity.created(uri).build();
        return ResponseEntity.created(uri).build();
    }

    @GetMapping("/users/{userId}/tickets/{id}")
    public Ticket getTicket(@PathVariable long userId, @PathVariable int id){
        if(ticketRepository.findById(id).isPresent()){
            return ticketRepository.findById(id).get();
        }
        return null;
    }

    @PutMapping("/users/{userId}/tickets/{ticketId}")
    public ResponseEntity<Ticket> updateTicket(@PathVariable long userId,
                                               @PathVariable int ticketId,
                                               @RequestBody TicketCreationDto ticketDto){
        Ticket updatedTicket = userService.saveUpdate(userId, ticketId, ticketDto);
        return new ResponseEntity<Ticket>(updatedTicket, HttpStatus.OK);
    }


}
