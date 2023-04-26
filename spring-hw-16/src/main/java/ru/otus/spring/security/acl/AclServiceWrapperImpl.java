package ru.otus.spring.security.acl;

import lombok.RequiredArgsConstructor;
import org.springframework.security.acls.domain.BasePermission;
import org.springframework.security.acls.domain.ObjectIdentityImpl;
import org.springframework.security.acls.domain.PrincipalSid;
import org.springframework.security.acls.model.MutableAcl;
import org.springframework.security.acls.model.MutableAclService;
import org.springframework.security.acls.model.Sid;
import org.springframework.stereotype.Service;
import ru.otus.spring.domain.Comment;

import javax.transaction.Transactional;

@Service
@RequiredArgsConstructor
public class AclServiceWrapperImpl implements AclServiceWrapper {

    private final MutableAclService aclService;

    @Override
    @Transactional
    public void addCommentToAcl(Comment comment) {
        ObjectIdentityImpl objectIdentity = new ObjectIdentityImpl(Comment.class, comment.getId());
        MutableAcl acl = aclService.createAcl(objectIdentity);
        Sid sid = new PrincipalSid(comment.getUser().getUsername());
        acl.insertAce(acl.getEntries().size(), BasePermission.WRITE, sid, true);
        acl.insertAce(acl.getEntries().size(), BasePermission.DELETE, sid, true);
        aclService.updateAcl(acl);
    }
}
