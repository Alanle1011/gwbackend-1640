package com.example.backend1640.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "IMAGES")
@Getter
@Setter
public class Image {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "CONTRIBUTION_ID", referencedColumnName = "id")
    private Contribution contributionId;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "USER_ID", referencedColumnName = "id")
    private User userId;

    @Column(name = "NAME", nullable = false)
    private String name;

    @Column(name = "TYPE", nullable = false)
    private String type;

    @Lob
    @Column(name = "DATA", nullable = false)
    private byte[] data;

    @Column(name = "CREATED_AT", nullable = false)
    private Date createdAt;

    @Column(name = "UPDATED_AT", nullable = false)
    private Date updatedAt;
}
