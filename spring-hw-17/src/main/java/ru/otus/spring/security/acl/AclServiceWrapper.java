package ru.otus.spring.security.acl;

import ru.otus.spring.domain.Comment;

public interface AclServiceWrapper {
    void addCommentToAcl(Comment comment);
}
