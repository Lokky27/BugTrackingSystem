package models;

import lombok.*;
import org.hibernate.Hibernate;

import javax.persistence.*;
import java.util.Objects;

@Getter
@Setter
@RequiredArgsConstructor
@Entity
@Table(name = "tasks")
public class Task
{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String theme;

    private String type;

    @Enumerated(EnumType.STRING)
    private Priority priority;

    private String description;

    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumns({
            @JoinColumn(name = "user_id")
    })
    @ToString.Exclude
    private User user;

    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumns({
            @JoinColumn(name = "project_id")
    })
    @ToString.Exclude
    private Project project;

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || o.getClass() != this.getClass()) return false;
        Task task = (Task) o;
        return id.equals(task.id) &&
                (theme == task.theme || (theme != null && theme.equals(task.getTheme()))) &&
                (type == task.type || (type != null && type.equals(task.getType()))) &&
                (description == task.description || (description != null && description.equals(task.getDescription()))) &&
                (priority == task.priority || (priority != null && priority.equals(task.getPriority())));
    }

    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((type == null) ? 0 : type.hashCode());
        result = prime * result + id.hashCode();
        result = prime * result + ((theme == null) ? 0 : theme.hashCode());
        result = prime * result + ((description == null) ? 0 : description.hashCode());
        result = prime * result + ((priority == null) ? 0 : priority.hashCode());
        return result;
    }

    @Override
    public String toString()
    {
        return "ID: " + getId() + ", " +
                "Theme: " + getTheme() + ", " +
                "Type: " + getType() +", " +
                "Priority: " + getPriority() + ", " +
                "\nDescription: " + getDescription() + "; " +
                "\nProject: " + getProject().getName() + ", " +
                "User: " + getUser().getName();
    }
}
