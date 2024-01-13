package session.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import users.entity.UsersEntity;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Builder
@Table(name = "session")
public class SessionEntity {
    @Id
    private String id;
    @OneToOne
    @JoinColumn(name = "userid")
    private UsersEntity users;
    private Long expiresat;
}
