package com.stss.backend.Bugtracker.repositories;

import com.stss.backend.Bugtracker.models.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Integer> {
    Comment findByCommentId(int ticketId);

    @Query(value = "SELECT * FROM comments c where c.ticket_id = :ticketId",nativeQuery = true)
    List<Comment> findByTicketId(@Param("ticketId") Integer ticketId );
    
    //"DELETE FROM bugtracker.comments WHERE bugtracker.comments.user_id = 28 AND bugtracker.comments.comment_id = 5"
//    @Query(value="DELETE FROM comments c WHERE c.comment_id = :commentId AND c.user_id = :userId", nativeQuery = true)
//    void deleteByCommentIdAndUserId(Integer commentId, Long userId);
}
