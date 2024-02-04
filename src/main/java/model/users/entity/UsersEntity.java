package model.users.entity;

import jakarta.persistence.*;
import lombok.*;
import model.locations.entity.LocationsEntity;
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
public class UsersEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "login", unique = true, nullable = false)
    private String login;
    @Column(name = "password", nullable = false)
    private String password;
    @Builder.Default
    @OneToMany(mappedBy = "user",cascade = CascadeType.ALL,orphanRemoval = true, fetch = FetchType.LAZY)
    private List<LocationsEntity> locations = new ArrayList<>();
    @Builder.Default
    @OneToMany(mappedBy = "user",cascade = CascadeType.ALL,orphanRemoval = true, fetch = FetchType.LAZY)
    private List<SessionEntity> sessions = new ArrayList<>();

    public void addSession(SessionEntity session){
        sessions.add(session);
        session.setUser(this);
    }
    public void addLocation(LocationsEntity location){
        locations.add(location);
        location.setUser(this);
    }

}
