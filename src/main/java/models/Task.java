package models;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "tasks")
public class Task
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "theme")
    private String theme;

    @Enumerated(EnumType.STRING)
    @Column(name = "priority")
    private Priority priority;

    private String type;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumns({
            @JoinColumn(name = "project_id", insertable = false, updatable = false)

    })
    private Project project;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumns({
            @JoinColumn(name = "user_id", insertable = false, updatable = false)
    })
    private User user;

    private String description;
}
