package ru.otus.spring.controller.rest;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.otus.spring.dto.CommentDto;
import ru.otus.spring.dto.rest.CommentRestDto;
import ru.otus.spring.exception.BookNotFoundEx;
import ru.otus.spring.exception.CommentNotFoundEx;
import ru.otus.spring.service.CommentService;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class CommentRestController {

    private final CommentService commentService;

    @GetMapping("/api/comments")
    public List<CommentDto> getAllByBookId(@RequestParam String bookId) throws BookNotFoundEx {
        return commentService.getAllByBookId(bookId);
    }

    @PostMapping("/api/comments")
    public CommentDto add(@RequestBody CommentRestDto comment) throws BookNotFoundEx {
        return commentService.addByBookId(comment.getBookId(), comment.getText());
    }

    @PutMapping("/api/comments")
    public void update(@RequestBody CommentRestDto comment) throws CommentNotFoundEx {
        commentService.updateById(comment.getId(), comment.getText());
    }

    @DeleteMapping("/api/comments/{id}")
    public void delete(@PathVariable String id) throws CommentNotFoundEx {
        commentService.deleteById(id);
    }
}
