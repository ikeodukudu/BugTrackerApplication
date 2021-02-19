package com.stss.backend.Bugtracker;

import com.stss.backend.Bugtracker.dtos.CommentCreationDto;
import com.stss.backend.Bugtracker.enums.Priority;
import com.stss.backend.Bugtracker.enums.Status;
import com.stss.backend.Bugtracker.enums.TicketType;
import com.stss.backend.Bugtracker.jwt.JwtTokenUtil;
import com.stss.backend.Bugtracker.jwt.resource.JwtTokenRequest;
import com.stss.backend.Bugtracker.models.BugUserDetails;
import com.stss.backend.Bugtracker.models.Comment;
import com.stss.backend.Bugtracker.models.Ticket;
import com.stss.backend.Bugtracker.models.User;
import com.stss.backend.Bugtracker.repositories.CommentRepository;
import com.stss.backend.Bugtracker.repositories.TicketRepository;
import com.stss.backend.Bugtracker.repositories.UserRepository;
import com.stss.backend.Bugtracker.services.BugUserDetailsService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class TestingWebApplicationTest {

    @Autowired
    private MockMvc mockMvc;

    private Long id = 28L;

    private String commentText = "Test Test Test comment";

    @MockBean
    private BugUserDetailsService bugUserDetailsService;

    @MockBean
    private TicketRepository ticketRepository;

    @MockBean
    private CommentRepository commentRepository;

    @MockBean(name = "userRepository")
    private UserRepository userRepository;

    @MockBean
    private JwtTokenUtil jwtTokenUtil;

    @MockBean
    private BugUserDetails userDetails;


    @Test
    public void shouldReturnWelcomeMessage() throws Exception {
        when(bugUserDetailsService.findByUserId(id)).thenReturn(new User(28L, "Ike Odukudu", "ikeodukudu@gmail.com"));
        this.mockMvc.perform(get("/home/28")).andDo(print()).andExpect(status().isOk())
                .andExpect(content().string(containsString("Welcome Ike Odukudu")));
    }

    @Test
    public void shouldReturnAllCommentsForATicket() throws Exception {
        int ticketId = 3;
        List<Comment> comments = new ArrayList<>();
        User user = new User(28L, "Ike Odukudu", "ikeodukudu@gmail.com");
        User user1 = new User(30L, "Oyin Odukudu", "oodukudu@gmail.com");
        Ticket ticket = new Ticket(3, user1, TicketType.DEVELOPMENT, "Fix home page button", "The button on the home page does not have any functionality. Can this be fixed ASAP?", user, Priority.MEDIUM, Status.OPEN);
        comments.add(new Comment(22, user, ticket, "This ticket has been passed to me so will pick up after my current ticket"));

        List<Comment> byTicketId = commentRepository.findByTicketId(ticketId);
        when(byTicketId)
                .thenReturn(comments);
        this.mockMvc.perform(get("/users/28/tickets/3/comments/"))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    public void shouldAddNewComment() throws Exception {
        int ticketId = 3;
        CommentCreationDto commentDto = new CommentCreationDto();
        commentDto.setComment(commentText);
        System.out.println(commentDto.getComment());
        commentDto.setComment("Test Test Test");
        User user = new User(28L, "Ike Odukudu", "ikeodukudu@gmail.com");
        User user1 = new User(30L, "Oyin Odukudu", "oodukudu@gmail.com");
        Ticket ticket = new Ticket(3, user1, TicketType.DEVELOPMENT, "Fix home page button", "The button on the home page does not have any functionality. Can this be fixed ASAP?", user, Priority.MEDIUM, Status.OPEN);
        Comment newComment = new Comment(ticketId, user, ticket, commentDto.getComment());
        when(bugUserDetailsService.saveComment(id, ticketId, commentDto))
                .thenReturn(newComment);
    }

    @Test
    public void shouldLoginUser() throws Exception {
        //int ticketId = 3;
        JwtTokenRequest jwtTokenRequest = new JwtTokenRequest("ikeodukudu@gmail.com", "Ikeoluwa1!");
        User user = new User(28, "Ike Odukudu", "ikeodukudu@gmail.com");
        when(bugUserDetailsService.findByEmailAndPassword(jwtTokenRequest.getEmail(), jwtTokenRequest.getPassword()))
                .thenReturn(user);
    }

    @Test
    public void shouldReturnAllTickets() throws Exception {
        List<Ticket> ticket = new ArrayList<>();
        User user1 = new User(30L, "Oyin Odukudu", "oodukudu@gmail.com");
        User assign1 = new User(28L, "Ike Odukudu", "ikeodukudu@gmail.com");
        ticket.add(new Ticket(3, user1, TicketType.DEVELOPMENT, "Fix home page button", "The button on the home page does not have any functionality. Can this be fixed ASAP?", assign1, Priority.MEDIUM, Status.OPEN));

        User user2 = new User(29L, "Jane Doe", "janedoe@yahoo.com");
        User assign2 = new User(28L, "Ike Odukudu", "ikeodukudu@gmail.com");
        ticket.add(new Ticket(6, user2, TicketType.DEVELOPMENT, "Api not mapped correctly", "The appropriate mapping method was not used for services. It should be a POST instead of a GET. Please fix ASAP", assign2, Priority.MEDIUM, Status.OPEN));

        User user3 = new User(30L, "Oyin Odukudu", "oodukudu@gmail.com");
        User assign3 = new User(28L, "Ike Odukudu", "ikeodukudu@gmail.com");
        ticket.add(new Ticket(7, user3, TicketType.DEVELOPMENT, "Remove delete functionality", "User should not be able to delete email from their accounts", assign3, Priority.MEDIUM, Status.RESOLVED));

        User user4 = new User(28L, "Ike Odukudu", "ikeodukudu@gmail.com");
        User assign4 = new User(31L, "Bill Gates", "billgates@microsoft.com");
        ticket.add(new Ticket(8, user4, TicketType.PRODUCTION, "Page lags", "The login page lags and takes time to log user in, could you fix this please?", assign4, Priority.HIGH, Status.OPEN));

        User user5 = new User(28L, "Ike Odukudu", "ikeodukudu@gmail.com");
        User assign5 = new User(28L, "Ike Odukudu", "ikeodukudu@gmail.com");
        ticket.add(new Ticket(9, user5, TicketType.DEVELOPMENT, "105; DROP TABLE Suppliers", "Fake Fake Fake", assign5, Priority.MEDIUM, Status.OPEN));

        User user6 = new User(28L, "Ike Odukudu", "ikeodukudu@gmail.com");
        User assign6 = new User(29L, "Jane Doe", "janedoe@yahoo.com");
        ticket.add(new Ticket(10, user6, TicketType.DEVELOPMENT, "<strong>TEst</strong>", "<b>This is a test</b>", assign6, Priority.LOW, Status.OPEN));

        User user7 = new User(28L, "Ike Odukudu", "ikeodukudu@gmail.com");
        User assign7 = new User(30L, "Oyin Odukudu", "oodukudu@gmail.com");
        ticket.add(new Ticket(15, user7, TicketType.TEST, "Test System", "Could you do a proper testing of the system please when you can", assign7, Priority.MEDIUM, Status.OPEN));

        User assign8 = new User(28L, "Ike Odukudu", "ikeodukudu@gmail.com");
        User user8 = new User(30L, "Oyin Odukudu", "oodukudu@gmail.com");
        ticket.add(new Ticket(16, user8, TicketType.DEVELOPMENT, "Test ticket", "Testing to see if this works", assign8, Priority.MEDIUM, Status.OPEN));

        when(ticketRepository.findByUserId(id)).thenReturn(ticket);
        this.mockMvc.perform(get("/users/28/tickets"))
                .andDo(print())
                .andExpect(status().isOk());
    }
}
