package com.stss.backend.Bugtracker.controllers;

import com.stss.backend.Bugtracker.dtos.CommentCreationDto;
import com.stss.backend.Bugtracker.models.Comment;
import com.stss.backend.Bugtracker.repositories.CommentRepository;
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
public class CommentController {

    final static Logger logger = LoggerFactory.getLogger(CommentController.class);

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private BugUserDetailsService userService;

    @GetMapping("/users/{userId}/tickets/{ticketId}/comments/")
    public List<Comment> getAllComments(@PathVariable long userId, @PathVariable int ticketId) {
        List<Comment> byUserId = commentRepository.findByTicketId(ticketId);
        return byUserId;
    }

    @PostMapping("/users/{userId}/tickets/{ticketId}/comments/")
    public ResponseEntity<Void> createComment(@PathVariable long userId,
                                              @PathVariable int ticketId,
                                              @RequestBody CommentCreationDto commentDto){
        Comment createdComment = userService.saveComment(userId,ticketId, commentDto);
        logger.info("Comment created successfully: " + createdComment);
        URI uri = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(createdComment.getTicketId())
                .toUri();
        //return ResponseEntity.created(uri).build();
        return ResponseEntity.created(uri).build();
    }

    @DeleteMapping("/users/{userId}/tickets/{ticketId}/comments/{id}")
    public ResponseEntity<Void> deleteComment(@PathVariable long userId,
                                              @PathVariable int ticketId,
                                              @PathVariable int id){
        commentRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }


    @GetMapping("/users/{userId}/tickets/{ticketId}/comments/{id}")
    public Comment getComment(@PathVariable long userId, @PathVariable int ticketId, @PathVariable int id){
        if(commentRepository.findById(id).isPresent()){
            return commentRepository.findById(id).get();
        }
        return null;
    }


    @PutMapping("/users/{userId}/tickets/{ticketId}/comments/{id}")
    public ResponseEntity<Comment> updateComment(@PathVariable long userId,
                                               @PathVariable int ticketId,
                                               @PathVariable int id,
                                               @RequestBody CommentCreationDto commentDto){
        Comment updatedComment = userService.saveUpdate(userId, ticketId, id, commentDto);
        return new ResponseEntity<Comment>(updatedComment, HttpStatus.OK);
    }


}
