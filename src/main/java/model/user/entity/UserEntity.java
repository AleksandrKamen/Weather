package model.user.entity;

import jakarta.persistence.*;
import lombok.*;
import model.location.entity.LocationEntity;
import model.session.entity.SessionEntity;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Builder
@ToString(exclude = {"locations","sessions"})
@Table(name = "users")
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "login", unique = true, nullable = false)
    private String login;
    @Column(name = "password", nullable = false)
    private String password;
    @Builder.Default
    @OneToMany(mappedBy = "user",cascade = CascadeType.ALL,orphanRemoval = true, fetch = FetchType.LAZY)
    private List<LocationEntity> locations = new ArrayList<>();
    @Builder.Default
    @OneToMany(mappedBy = "user",cascade = CascadeType.ALL,orphanRemoval = true, fetch = FetchType.LAZY)
    private List<SessionEntity> sessions = new ArrayList<>();

    public void addSession(SessionEntity session){
        sessions.add(session);
        session.setUser(this);
    }
    public void addLocation(LocationEntity location){
        locations.add(location);
        location.setUser(this);
    }

}
