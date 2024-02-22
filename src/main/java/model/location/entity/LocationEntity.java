package model.location.entity;

import jakarta.persistence.*;
import lombok.*;
import model.user.entity.UserEntity;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Builder
@Table(name = "locations")
public class LocationEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "name", nullable = false)
    private String name;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity user;
    @Column(name = "latitude", nullable = false)
    private Double latitude;
    @Column(name = "longitude", nullable = false)
    private Double longitude;
}
