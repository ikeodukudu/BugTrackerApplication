package com.stss.backend.Bugtracker.services;

import com.stss.backend.Bugtracker.dtos.CommentCreationDto;
import com.stss.backend.Bugtracker.dtos.TicketCreationDto;
import com.stss.backend.Bugtracker.dtos.UpdatePwdDto;
import com.stss.backend.Bugtracker.dtos.UserResgistrationDto;
import com.stss.backend.Bugtracker.enums.ERole;
import com.stss.backend.Bugtracker.enums.Priority;
import com.stss.backend.Bugtracker.enums.Status;
import com.stss.backend.Bugtracker.enums.TicketType;
import com.stss.backend.Bugtracker.models.*;
import com.stss.backend.Bugtracker.repositories.*;
import org.hibernate.SharedSessionContract;
import org.hibernate.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
//import javax.transaction.Transaction;
import java.sql.Timestamp;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BugUserDetailsService implements BugUserService {

  public static final int MAX_FAILED_ATTEMPTS = 3;

  private static final long LOCK_TIME_DURATION = 24 * 60 * 60 * 1000; // 24 hours

  final static Logger logger = LoggerFactory.getLogger(BugUserDetailsService.class);

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private RoleRepository roleRepository;

  @Autowired
  private BCryptPasswordEncoder passwordEncoder;

  @Autowired
  private EntityManager entityManager;

  @Autowired
  private TicketRepository ticketRepository;

  @Autowired
  private CommentRepository commentRepository;

  @Autowired
  private SharedSessionContract session;

  @Override
  public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
    User user = userRepository.findByEmail(email);

    if (user == null) {
      throw new UsernameNotFoundException(String.format("USER_NOT_FOUND '%s'.", email));
    }

    return new org.springframework.security.core.userdetails.User(user.getEmail(),
            user.getPassword(),
            mapRolesToAuthorities(user.getRoles()));
  }

  //@Override
  public BugUserDetails loadUserByEmail(String email) throws UsernameNotFoundException {
    User user = userRepository.findByEmail(email);

    if (user == null) {
      throw new UsernameNotFoundException(String.format("USER_NOT_FOUND '%s'.", email));
    }

    return new BugUserDetails(user.getUserId(),user.getEmail(),
            user.getPassword(),
            user.getRoles().toString());
  }

  @Override
  public User findByEmail(String email) {
    return userRepository.findByEmail(email);
  }

  public User findByUserId(long userId){
    return userRepository.findByUserId(userId);
  }

  public List<GetUserIdAndName> findUserIdAndName(){
    return userRepository.getUserIdAndName();
  }

  @Override
  public User findByEmailAndPassword(String email, String password) {
    return userRepository.findByEmailAndPassword(email,password);
  }

  //@Override
  public User save(UserResgistrationDto registration) {
    User user = new User();
    //user.setUserId(1001L);
    user.setName(registration.getName());
    user.setEmail(registration.getEmail());
    user.setPassword(passwordEncoder.encode(registration.getPassword()));
    user.setMobile(registration.getMobile());
    Date date = new Date();
    long time = date.getTime();
    user.setDateCreated(new Timestamp(time));
    Collection<String> strRoles = registration.getRole();
    Collection<Role> roles = new HashSet<>();
    if(strRoles == null){
      try {
        Role userRole = roleRepository.findByRoleName(ERole.ROLE_USER);
        roles.add(userRole);
      }catch(RuntimeException runtimeException){
        throw new RuntimeException("Error: Role is not found.");
      }
    }else{
      strRoles.forEach(role -> {
        switch (role) {
          case "admin": {
            try {
              Role adminRole = roleRepository.findByRoleName(ERole.ROLE_ADMIN);
              roles.add(adminRole);
            }catch(RuntimeException runtimeException){
              throw new RuntimeException("Error: Role is not found.");
            }
          }
          case "mod": {
            try {
              Role modRole = roleRepository.findByRoleName(ERole.ROLE_MODERATOR);
              roles.add(modRole);
            }catch(RuntimeException runtimeException){
              throw new RuntimeException("Error: Role is not found.");
            }
          }
          default: {
            try {
              Role userRole = roleRepository.findByRoleName(ERole.ROLE_USER);
              roles.add(userRole);
            }catch(RuntimeException runtimeException){
              throw new RuntimeException("Error: Role is not found.");
            }
          }
        }
      });
    }
    user.setRoles(roles);
    user.setAccountNonLocked(true);
    return userRepository.save(user);
  }

  public Ticket saveTicket(long ticketUserId, TicketCreationDto ticketCreationDto){

    Ticket ticket = new Ticket();
    //if (ticket.getTicketId() != -1 || ticket.getTicketId() != 0) {
      ticket.setTicketName(ticketCreationDto.getTicketTitle());
      ticket.setDescription(ticketCreationDto.getTicketDescription());
      ticket.setTicketType(TicketType.valueOf(ticketCreationDto.getTicketType()));
      ticket.setPriority(Priority.valueOf(ticketCreationDto.getPriority()));
      ticket.setStatus(Status.valueOf(ticketCreationDto.getStatus()));
      //long ticketUserId = ticketCreationDto.getUserId();
      User user = userRepository.findByUserId(ticketUserId);
      ticket.setUserId(user);
      User assignUser = userRepository.findByName(ticketCreationDto.getSelectedUser());
      ticket.setAssignedTo(assignUser);
      Date date = new Date();
      long time = date.getTime();
      ticket.setDateCreated(new Timestamp(time));
    //}
    return ticketRepository.save(ticket);
  }

  public Ticket saveUpdate(long ticketUserId, int ticketId, TicketCreationDto ticketCreationDto){
    Ticket ticket = new Ticket();
    ticketRepository.findById(ticketId);
    ticket.setTicketId(ticketId);
    ticket.setTicketName(ticketCreationDto.getTicketTitle());
    ticket.setDescription(ticketCreationDto.getTicketDescription());
    ticket.setTicketType(TicketType.valueOf(ticketCreationDto.getTicketType()));
    ticket.setPriority(Priority.valueOf(ticketCreationDto.getPriority()));
    ticket.setStatus(Status.valueOf(ticketCreationDto.getStatus()));
    User user = userRepository.findByUserId(ticketUserId);
    ticket.setUserId(user);
    User assignUser = userRepository.findByName(ticketCreationDto.getSelectedUser());
    ticket.setAssignedTo(assignUser);
    Date date = new Date();
    long time = date.getTime();
    ticket.setDateCreated(new Timestamp(time));
    return ticketRepository.save(ticket);
  }

  public Comment saveComment(long userId, int ticketId, CommentCreationDto commentDto){
    User user = userRepository.findByUserId(userId);
    Ticket ticket = ticketRepository.findByTicketId(ticketId);
    Comment comment = new Comment();
    comment.setComment(commentDto.getComment());
    User newUser = new User(user.getUserId(), user.getName(), user.getEmail());
    comment.setUserId(newUser);
    comment.setTicketId(ticket);
    Date date = new Date();
    long time = date.getTime();
    comment.setDateCreated(new Timestamp(time));
    return commentRepository.save(comment);
  }

  public Comment saveUpdate(long userId, int ticketId, int commentId, CommentCreationDto commentDto){
    Comment comment = new Comment();
    commentRepository.findById(commentId);
    comment.setCommentId(commentId);
    comment.setComment(commentDto.getComment());
    User user = userRepository.findByUserId(userId);
    comment.setUserId(user);
    Ticket ticket = ticketRepository.findByTicketId(ticketId);
    comment.setTicketId(ticket);
    Date date = new Date();
    long time = date.getTime();
    comment.setDateCreated(new Timestamp(time));
    return commentRepository.save(comment);
  }

  public void updatePassword(UpdatePwdDto updatePwd){
    User user = userRepository.findByEmail(updatePwd.getEmail());
    user.setPassword(passwordEncoder.encode(updatePwd.getNewPassword()));
    Transaction txn = session.beginTransaction();
    userRepository.updatePassword(user.getPassword(), updatePwd.getEmail());
    txn.commit();
  }

  private Collection < ? extends GrantedAuthority > mapRolesToAuthorities(Collection<Role> roles) {
    return roles.stream()
            .map(role -> new SimpleGrantedAuthority(role.getRoleName().name()))
            .collect(Collectors.toList());
  }

  public void increaseFailedAttempts(User user){
    int newFailAttempts = user.getFailedAttempt() + 1;
    Transaction txn = session.beginTransaction();
    userRepository.updateFailedAttempts(newFailAttempts, user.getEmail());
    txn.commit();
  }

  public void resetFailedAttempts(String email){
    Transaction txn = session.beginTransaction();
    userRepository.updateFailedAttempts(0, email);
    txn.commit();
  }

  public void lock(User user){
    //Transaction txn = session.beginTransaction();
    user.setAccountNonLocked(false);
    user.setLockTime(new Date());
    userRepository.save(user);
    //txn.commit();
  }

  public boolean unlockWhenTimeExpired(User user){
    long lockTimeInMillis = user.getLockTime().getTime();
    long currentTimeInMillis = System.currentTimeMillis();

    if(lockTimeInMillis + LOCK_TIME_DURATION < currentTimeInMillis){
      user.setAccountNonLocked(true);
      user.setLockTime(null);
      user.setFailedAttempt(0);
      userRepository.save(user);
      return true;
    }
    return false;
  }
}


