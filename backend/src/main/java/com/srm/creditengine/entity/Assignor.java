package com.srm.creditengine.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.util.UUID;

@Entity
@Table(name = "assignors")
public class Assignor {
    @Id
    private UUID id;

    @Column(nullable = false, length = 120)
    private String name;

    protected Assignor() {}

    public Assignor(UUID id) {
        this.id = id;
        this.name = "Cedente " + id.toString().substring(0, 8);
    }

    public UUID getId() { return id; }
    public String getName() { return name; }
}
