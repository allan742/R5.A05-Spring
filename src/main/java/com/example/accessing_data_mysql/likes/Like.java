package com.example.accessing_data_mysql.likes;

import com.example.accessing_data_mysql.publications.Publication;
import com.example.accessing_data_mysql.users.User;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

@Entity
@Table(name = "publication_likes",
       uniqueConstraints = @UniqueConstraint(columnNames = {"userId", "publicationId"}))
public class Like {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "userId ", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "publicationId", nullable = false)
    private Publication publication;

    @Column(nullable = false)
    private boolean liked = true;

    // getters / setters
}

