package models;

import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Getter
@Setter
@RequiredArgsConstructor
@Entity
@Table(name = "projects")
public class Project
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Column(name = "deadline")
    private Date deadLine;

    @OneToMany(mappedBy = "project", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    private List<Task> tasks = new ArrayList<>();

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || o.getClass() != this.getClass()) return false;
        Project project = (Project) o;
        return id.equals(project.id)
                && (name == project.name || (name != null && name.equals(project.getName()))
                && (deadLine == project.deadLine || (deadLine != null && deadLine.equals(project.getDeadLine()))));
    }

    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        result = (int) (prime * result + id);
        result = prime * result + ((deadLine == null) ? 0 : deadLine.hashCode());
        return result;
    }

    @Override
    public String toString()
    {
        return "ID: " + getId() + ", " +
                "Name: " + getName() + ", " +
                "Deadline: " + getDeadLine().toString() + ", " +
                "Tasks on project: " + getTasks().size();
    }
}
