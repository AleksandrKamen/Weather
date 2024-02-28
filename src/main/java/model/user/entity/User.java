package model.user.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import model.location.entity.Location;
import model.session.entity.Session;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Builder
@ToString(exclude = {"locations", "sessions"})
@Table(name = "users", indexes = {@Index(name = "idx_login", columnList = "login")})
@FieldDefaults(level = AccessLevel.PRIVATE)
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    @Column(name = "login", unique = true, nullable = false)
    String login;
    @Column(name = "password", nullable = false)
    String password;
    @Builder.Default
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    List<Location> locations = new ArrayList<>();
    @Builder.Default
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    List<Session> sessions = new ArrayList<>();

    public void addSession(Session session) {
        sessions.add(session);
        session.setUser(this);
    }

    public void addLocation(Location location) {
        locations.add(location);
        location.setUser(this);
    }

}
